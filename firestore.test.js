const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read or write any user profile or subcollections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const past = new Date(Date.now() - 2000);
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      startDate: "2026-11-19",
      dailyHours: 2.0,
      startTime: "18:00",
      theme: "auto",
      focusMin: 25,
      breakMin: 5,
      createdAt: past,
      updatedAt: past,
    })
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("notes").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("tasks").get()
  );
});

test("Authenticated owner: UserProfile create, partial update, ghost-field & immutable rejection", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = new Date(Date.now() - 10000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      startDate: "2026-11-19",
      dailyHours: 2.0,
      startTime: "18:00",
      theme: "auto",
      focusMin: 25,
      breakMin: 5,
      createdAt: created,
      updatedAt: created,
    })
  );

  // Owner read succeeds; listing /users is forbidden
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
  await assertFails(aliceDb.collection("users").get());

  // Valid partial update succeeds (including daily streak persistence)
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      examDate: "2027-01-15",
      theme: "dark",
      dailyHours: 3.5,
      currentStreak: 4,
      bestStreak: 7,
      lastStudyDate: "2026-11-19",
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Negative streak fails validation
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      currentStreak: -1,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Immutable createdAt change fails
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Ghost field & invalid theme fail
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      theme: "neon",
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Future timestamp spoofing fails
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      theme: "light",
      updatedAt: new Date(Date.now() + 60000),
    })
  );
});

test("Authenticated user: cross-user isolation on profile and subcollections", async () => {
  const created = new Date(Date.now() - 10000);
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    const db = ctx.firestore();
    await db.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      startDate: "2026-11-19",
      dailyHours: 2.0,
      startTime: "18:00",
      theme: "auto",
      focusMin: 25,
      breakMin: 5,
      createdAt: created,
      updatedAt: created,
    });
    await db.collection("users").doc(BOB_UID).collection("notes").doc("note_1").set({
      title: "Bob Note",
      body: "Secret",
      updatedAtIso: "2026-11-19T10:00:00Z",
      createdAt: created,
      updatedAt: created,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("notes").get());
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("notes").doc("note_1").delete()
  );
});

test("Authenticated owner: progress, sessions, cards, attempts, exams, notes, tasks parity", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = new Date(Date.now() - 10000);
  const updated = new Date(Date.now() - 1000);

  // Progress
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("progress").doc("t__1_1").set({
      key: "t:1.1",
      value: "done",
      createdAt: created,
      updatedAt: created,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("progress").doc("t__1_1").update({
      value: "review",
      updatedAt: updated,
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("progress").get());

  // Sessions
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("sessions").doc("sess_1").set({
      day: "2026-11-19",
      minutes: 25,
      kind: "focus",
      topic: "1.1",
      createdAt: created,
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("sessions").get());
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("sessions").doc("sess_1").delete()
  );

  // Cards
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("cards").doc("error-mistake").set({
      box: 2,
      due: "2026-11-20",
      reps: 1,
      lapses: 0,
      createdAt: created,
      updatedAt: created,
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("cards").doc("error-mistake").update({
      box: 99,
      updatedAt: updated,
    })
  );

  // Attempts
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("attempts").doc("att_1").set({
      questionId: "q1",
      correct: true,
      mode: "practice",
      at: "2026-11-19T12:00:00Z",
      createdAt: created,
    })
  );

  // Exams
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("exams").doc("exam_1").set({
      score: 32,
      total: 40,
      seconds: 2400,
      breakdown: { "1": { c: 7, t: 8 }, "4": { c: 9, t: 11 } },
      at: "2026-11-19T12:30:00Z",
      createdAt: created,
    })
  );

  // Notes (create, update, reject ghost id field)
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1").set({
      title: "Boundary Value Analysis",
      body: "Test min-1, min, max, max+1",
      topic: "4.2.2",
      updatedAtIso: "2026-11-19T12:00:00Z",
      createdAt: created,
      updatedAt: created,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1").update({
      title: "BVA Updated",
      body: "Updated body",
      topic: null,
      updatedAtIso: "2026-11-19T12:15:00Z",
      updatedAt: updated,
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_2").set({
      id: "note_2",
      title: "Bad Note",
      body: "Should fail",
      updatedAtIso: "2026-11-19T12:00:00Z",
      createdAt: created,
      updatedAt: created,
    })
  );

  // Tasks (create & toggle)
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("tasks").doc("task_1").set({
      title: "Review Chapter 4",
      day: "2026-11-19",
      done: false,
      createdAt: created,
      updatedAt: created,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("tasks").doc("task_1").update({
      done: true,
      updatedAt: updated,
    })
  );
});
