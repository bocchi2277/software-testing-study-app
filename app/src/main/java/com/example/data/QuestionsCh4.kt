package com.example.data

internal object QuestionsCh4 {
    val items: List<RawQ> = listOf(
        // ==================== CHAPTER 4 (38 Questions) ====================
        RawQ(
            id = "q025", ch = 4,
            q = "ما الفئات الثلاث لتقنيات الاختبار في منهج CTFL v4.0؟",
            correct = "Black-box و White-box و Experience-based",
            wrong = listOf("Static و Dynamic و Regression", "Functional و Performance و Security", "Unit و System و Acceptance"),
            exp = "Black-box مبنية على المواصفات، وWhite-box على البنية الداخلية، وExperience-based على الخبرة.",
            qEn = "Which three categories of test techniques are defined in the ISTQB CTFL v4.0 syllabus?",
            correctEn = "Black-box, white-box, and experience-based test techniques",
            wrongEn = listOf(
                "Static, dynamic, and regression test techniques",
                "Functional, performance, and security test techniques",
                "Component, system, and acceptance test techniques"
            ),
            expEn = "Section 4.1 classifies test techniques into black-box (specification-based), white-box (structure-based), and experience-based."
        ),
        RawQ(
            id = "q026", ch = 4,
            q = "حقل عمر يقبل من 18 إلى 65. أي مجموعة تحقق 100% Equivalence Partitioning coverage بأقل عدد حالات؟",
            correct = "10، 30، 70",
            wrong = listOf("18، 40، 65", "17، 18، 65، 66", "18، 19، 64، 65"),
            exp = "الأقسام الثلاثة: (<18 غير صالح)، (18–65 صالح)، (>65 غير صالح). قيمة واحدة من كل قسم تكفي.",
            qEn = "An input field accepts integer ages from 18 to 65 inclusive. Which set of test inputs achieves 100% equivalence partitioning coverage with the minimum number of test cases?",
            correctEn = "10, 30, 70",
            wrongEn = listOf("18, 40, 65", "17, 18, 65, 66", "18, 19, 64, 65"),
            expEn = "There are three equivalence partitions: invalid (<18), valid (18–65), and invalid (>65). Selecting one value from each partition achieves 100% EP coverage."
        ),
        RawQ(
            id = "q027", ch = 4,
            q = "لمجال صحيح من 1 إلى 100 (شاملاً)، ما القيم اللازمة لـ 2-value BVA؟",
            correct = "0، 1، 100، 101",
            wrong = listOf("1، 50، 100", "0، 1، 2، 99، 100، 101", "1، 2، 99، 100"),
            exp = "في 2-value نأخذ قيمة الحد وأقرب قيمة مجاورة من القسم التالي: (0,1) و(100,101).",
            qEn = "For a valid integer range of 1 to 100 inclusive, which set of test values is derived using 2-value Boundary Value Analysis (BVA)?",
            correctEn = "0, 1, 100, 101",
            wrongEn = listOf("1, 50, 100", "0, 1, 2, 99, 100, 101", "1, 2, 99, 100"),
            expEn = "2-value BVA selects each boundary value and its closest neighbor outside the partition: 0 and 1 for the lower boundary, and 100 and 101 for the upper boundary."
        ),
        RawQ(
            id = "q028", ch = 4,
            q = "لنفس المجال [1..100]، ما القيم اللازمة لـ 3-value BVA؟",
            correct = "0، 1، 2، 99، 100، 101",
            wrong = listOf("0، 1، 100، 101", "1، 50، 100", "-1، 0، 1، 100، 101، 102"),
            exp = "3-value BVA تضيف القيمة المجاورة داخل القسم أيضاً (2 و99)، فتصبح أكثر حساسية لأخطاء المقارنة.",
            qEn = "For the same valid integer range of 1 to 100 inclusive, which set of test values is required for 3-value BVA?",
            correctEn = "0, 1, 2, 99, 100, 101",
            wrongEn = listOf("0, 1, 100, 101", "1, 50, 100", "-1, 0, 1, 100, 101, 102"),
            expEn = "3-value BVA uses each boundary plus both immediate neighbors (before and after): {0, 1, 2} and {99, 100, 101}."
        ),
        RawQ(
            id = "q029", ch = 4,
            q = "كم عدد الأعمدة (Rules) في جدول قرار كامل يحتوي على 3 شروط ثنائية (True/False) مستقلة؟",
            correct = "8",
            wrong = listOf("3", "6", "16"),
            exp = "2^3 = 8 قواعد تغطي كل التركيبات الممكنة للشروط الثلاثة.",
            qEn = "How many rule columns exist in a full decision table with 3 independent binary (True/False) conditions?",
            correctEn = "8",
            wrongEn = listOf("3", "6", "16"),
            expEn = "A full decision table with n independent binary conditions has 2^n rules; here 2^3 = 8."
        ),
        RawQ(
            id = "q030", ch = 4,
            q = "ما الفائدة الأساسية من استخدام Decision Table Testing في تحليل المتطلبات؟",
            correct = "كشف التركيبات المنسية أو المتناقضة عندما تتفاعل عدة شروط معاً",
            wrong = listOf(
                "قياس استهلاك الذاكرة أثناء تشغيل الشيفرة تحت ضغط عالٍ",
                "حساب النسبة المئوية للأسطر البرمجية المنفذة داخل دالة",
                "إيجاد الأخطاء الإملائية والتنسيقية داخل واجهة المستخدم فقط"
            ),
            exp = "جداول القرار تضمن ألا تفوتنا أي تركيبة شروط وتكشف الفجوات أو التناقض في قواعد العمل.",
            qEn = "What is a primary benefit of using Decision Table Testing during requirements and test analysis?",
            correctEn = "Identifying overlooked, ambiguous, or contradictory combinations of business conditions",
            wrongEn = listOf(
                "Measuring runtime memory consumption under peak concurrent user load",
                "Calculating the percentage of executable code lines run within a module",
                "Detecting cosmetic typos and alignment issues in the graphical layout"
            ),
            expEn = "Decision tables systematically map combinations of conditions to actions, revealing gaps or contradictions in specifications."
        ),
        RawQ(
            id = "q031", ch = 4,
            q = "أي معيار تغطية هو الأقوى في State Transition Testing؟",
            correct = "All transitions coverage",
            wrong = listOf("All states coverage", "Valid transitions coverage (0-switch)", "Initial state coverage"),
            exp = "All transitions تغطي الصالحة وغير الصالحة، وتحقق تلقائياً Valid transitions وAll states coverage.",
            qEn = "Which coverage criterion is the STRONGEST in State Transition Testing?",
            correctEn = "All transitions coverage",
            wrongEn = listOf("All states coverage", "Valid transitions coverage (0-switch)", "Initial state coverage"),
            expEn = "100% all transitions coverage exercises both valid and invalid transitions and therefore subsumes valid transitions coverage and all states coverage."
        ),
        RawQ(
            id = "q032", ch = 4,
            q = "ما الميزة التي يضيفها State Table مقارنة بـ State Transition Diagram؟",
            correct = "يعرض بوضوح الانتقالات غير الصالحة (Invalid transitions) لكل ثنائية (حالة، حدث)",
            wrong = listOf(
                "يحذف الحالات النهائية لتبسيط التصميم للعميل غير التقني",
                "يحسب التغطية البرمجية للفروع داخل الكود المصدري تلقائياً",
                "يتجاهل الأحداث التي لا تغير الحالة ولا يشمل شروط الحماية"
            ),
            exp = "جدول الحالات يعرض كل تركيبة (State, Event)، بما فيها الانتقالات غير المسموح بها لاختبار رفضها.",
            qEn = "What advantage does a state table offer compared to a state transition diagram?",
            correctEn = "It explicitly displays invalid transitions for every (state, event) combination",
            wrongEn = listOf(
                "It omits terminal states to keep the model minimal for business demos",
                "It automatically measures white-box branch coverage of source code",
                "It ignores events that keep the system in the same state"
            ),
            expEn = "State transition diagrams typically depict only valid transitions, whereas state tables expose every (state, event) pair including invalid transitions."
        ),
        RawQ(
            id = "q033", ch = 4,
            q = "ما العلاقة الصحيحة بين Statement coverage وBranch coverage في CTFL v4.0؟",
            correct = "100% Branch coverage تعني دائماً 100% Statement coverage، والعكس غير صحيح",
            wrong = listOf(
                "100% Statement coverage تعني دائماً 100% Branch coverage في كل الأحوال",
                "التغطيتان متكافئتان تماماً وتنتجان نفس عدد حالات الاختبار دائماً",
                "Statement coverage أقوى من Branch coverage لأنها تفحص الأوامر الشرطية فقط"
            ),
            exp = "تحقيق كل الفروع (True/False) يزور كل الأوامر القابلة للتنفيذ، لذا Branch أقوى من Statement.",
            qEn = "What is the relationship between statement coverage and branch coverage?",
            correctEn = "100% branch coverage guarantees 100% statement coverage, but not vice versa",
            wrongEn = listOf(
                "100% statement coverage always guarantees 100% branch coverage",
                "Both criteria are equivalent and always require identical test suites",
                "Statement coverage subsumes branch coverage in code containing loops"
            ),
            expEn = "Exercising every branch (True and False outcomes) reaches every reachable executable statement; the reverse fails for empty else-branches."
        ),
        RawQ(
            id = "q034", ch = 4,
            q = "في مكوّن يحتوي على 20 أمراً قابلاً للتنفيذ، نفذت الاختبارات 15 أمراً منها. ما Statement coverage؟",
            correct = "75%",
            wrong = listOf("25%", "50%", "80%"),
            exp = "(15 ÷ 20) × 100% = 75%.",
            qEn = "A component contains 20 executable statements. A test suite executes 15 of them. What is the statement coverage?",
            correctEn = "75%",
            wrongEn = listOf("25%", "50%", "80%"),
            expEn = "Statement coverage = (15 executed / 20 total executable statements) × 100% = 75%."
        ),
        RawQ(
            id = "q035", ch = 4,
            q = "متى تبقى عيوب خطيرة حتى لو حققت مجموعة الاختبارات 100% Statement و100% Branch coverage؟",
            correct = "عندما يكون هناك متطلب مفقود لم يُكتب له كود أصلاً في البرنامج",
            wrong = listOf(
                "عندما يحتوي الكود على متغير محلي غير مستخدم داخل دالة مغلقة",
                "عندما تُختبر الفروع الشرطية True و False لكل جملة if في الدالة",
                "عندما يكون كل سطر قابل للتنفيذ قد نُفذ مرتين أثناء اختبار الوحدة"
            ),
            exp = "White-box testing تقيس الكود الموجود فقط، ولا تستطيع اكتشاف متطلبات منسية لم تُبرمج (Defects of omission).",
            qEn = "Why can severe defects still exist in software even after achieving 100% statement and 100% branch coverage?",
            correctEn = "Code coverage cannot detect missing requirements that were never implemented in the code",
            wrongEn = listOf(
                "Because branch coverage skips conditional statements inside loops",
                "Because achieving 100% branch coverage leaves executable statements unvisited",
                "Because code coverage tools require production traffic rather than unit tests"
            ),
            expEn = "White-box techniques only measure existing code structure; they cannot uncover defects of omission (missing functionality)."
        ),
        RawQ(
            id = "q036", ch = 4,
            q = "تقنية Error guessing تعتمد أساساً على:",
            correct = "استباق أخطاء المطور المحتملة استناداً إلى الخبرة وقوائم Fault attacks والأعطال السابقة",
            wrong = listOf(
                "توليد قيم عشوائية متصلة دون أي معرفة بالمجال أو الأخطاء الشائعة",
                "تحليل مخطط انتقال الحالات لإيجاد كل الانتقالات الصالحة وغير الصالحة",
                "قياس نسبة الفروع التنفيذية المغطاة داخل الشيفرة المصدرية"
            ),
            exp = "Error guessing تقنية خبرة منظَّمة عبر Fault attacks لاستهداف نقاط الضعف المعتادة (مثل القسمة على صفر).",
            qEn = "What does the experience-based technique 'error guessing' primarily rely on?",
            correctEn = "Anticipating likely developer mistakes using tester experience, past defect history, and fault attacks",
            wrongEn = listOf(
                "Generating purely random strings without any domain or failure knowledge",
                "Constructing a formal state table to cover all valid and invalid state transitions",
                "Measuring the percentage of source code branches executed during component tests"
            ),
            expEn = "Error guessing anticipates errors, defects, and failures based on experience and can be structured via fault attack lists."
        ),
        RawQ(
            id = "q037", ch = 4,
            q = "أي وصف ينطبق بأدق صورة على Exploratory testing؟",
            correct = "تصميم الاختبارات وتنفيذها وتقييمها بالتزامن مع التعلم المستمر، وغالباً عبر Session-based testing",
            wrong = listOf(
                "تنفيذ نصوص اختبار مكتوبة مسبقاً كلمة بكلمة دون السماح بأي انحراف",
                "تشغيل أدوات التحليل الساكن على الشيفرة قبل كتابة حالات الاختبار",
                "قياس أداء الخادم بتوليد آلاف الطلبات المتزامنة عبر أداة أتمتة"
            ),
            exp = "Exploratory testing يُوجَّه بـ Test charters في جلسات محددة الوقت (Session-based) ويكمل التقنيات الرسمية.",
            qEn = "Which statement BEST describes exploratory testing?",
            correctEn = "Tests are simultaneously designed, executed, and evaluated while the tester learns about the test object",
            wrongEn = listOf(
                "Pre-written step-by-step scripts are executed strictly without deviation",
                "Automated static analyzers scan source code prior to compiling the binary",
                "Automated load generators simulate thousands of concurrent API requests"
            ),
            expEn = "In exploratory testing, test design, execution, and evaluation happen concurrently, often structured via session-based test management and test charters."
        ),
        RawQ(
            id = "q038", ch = 4,
            q = "متى يكون Checklist-based testing أكثر فاعلية؟",
            correct = "عند تحديث القائمة دورياً من الخبرة والأعطال السابقة مع إبقاء البنود واضحة وغير مفرطة التفصيل",
            wrong = listOf(
                "عند تحويل كل بند إلى نص اختباري جامد من عشرات الخطوات الإلزامية",
                "عند تجميد القائمة لسنوات وعدم تعديلها حتى لو تغيرت معمارية النظام",
                "عند استخدامها كبديل وحيد يمنع تطبيق BVA وDecision Tables"
            ),
            exp = "القوائم الجيدة مختصرة وواضحة ومحدّثة بانتظام لتغطي المخاطر المهمة دون تقييد المختبر كسكربت جامد.",
            qEn = "When is checklist-based testing MOST effective?",
            correctEn = "When checklists are high-level enough to allow flexibility and are regularly updated based on defect trends",
            wrongEn = listOf(
                "When every checklist item is expanded into a rigid 30-step test script",
                "When checklists remain frozen across releases despite major architecture changes",
                "When used as a complete replacement for BVA, decision tables, and code reviews"
            ),
            expEn = "Section 4.4.3: Checklists should not contain overly detailed step-by-step instructions and should be regularly maintained and updated."
        ),
        RawQ(
            id = "q039", ch = 4,
            q = "ما المكونات الثلاثة لقصة المستخدم (3Cs of a User Story) في CTFL v4.0؟",
            correct = "Card و Conversation و Confirmation",
            wrong = listOf("Code و Compile و Commit", "Check و Control و Close", "Condition و Case و Coverage"),
            exp = "Card (بطاقة الوصف)، Conversation (النقاش المشترك)، Confirmation (معايير القبول).",
            qEn = "What do the '3 Cs' of a user story stand for?",
            correctEn = "Card, Conversation, Confirmation",
            wrongEn = listOf("Code, Compile, Commit", "Check, Control, Close", "Condition, Case, Coverage"),
            expEn = "Section 4.5.1: A user story consists of Card (written medium), Conversation (shared understanding), and Confirmation (acceptance criteria)."
        ),
        RawQ(
            id = "q040", ch = 4,
            q = "ما الصيغتان الرئيسيتان لكتابة Acceptance Criteria في منهج CTFL v4.0؟",
            correct = "Scenario-oriented (مثل Given/When/Then) و Rule-oriented (قواعد/نقاط محددة)",
            wrong = listOf(
                "Statement coverage و Branch coverage",
                "Alpha testing و Beta testing",
                "Master plan و Completion report"
            ),
            exp = "القسم 4.5.2 يحدد الصيغة السلوكية Scenario-oriented (GWT) والصيغة القائمة على القواعد Rule-oriented.",
            qEn = "Which two main formats are commonly used to document acceptance criteria in CTFL v4.0?",
            correctEn = "Scenario-oriented (e.g., Given/When/Then) and rule-oriented (e.g., bulleted verification lists)",
            wrongEn = listOf(
                "Statement coverage and branch coverage formats",
                "Alpha testing and beta testing protocols",
                "Master test plan and test completion templates"
            ),
            expEn = "CTFL v4.0 Section 4.5.2 describes two common formats: scenario-oriented (Given/When/Then) and rule-oriented (bulleted lists or tables)."
        ),
        RawQ(
            id = "q053", ch = 4,
            q = "في ممارسات ATDD والتطوير الرشيق (القسم 4.5 في CTFL v4.0)، من يشكّلون جلسة Three Amigos؟",
            correct = "ممثل الأعمال (Business/PO)، المطور (Developer)، المختبر (Tester)",
            wrong = listOf(
                "مدير المشروع، مسؤول الشبكات، مهندس الأمان",
                "ثلاثة مختبرين يديرون أدوات الأتمتة في فريق مستقل",
                "العميل النهائي، مسؤول الدعم الفني، مصمم واجهات UI"
            ),
            exp = "Three Amigos تجمع منظورات الأعمال والتطوير والاختبار لتوضيح قصص المستخدمين ومعايير القبول قبل التنفيذ.",
            qEn = "In collaborative user story refinement and ATDD (CTFL v4.0 Section 4.5), which three roles make up the 'Three Amigos'?",
            correctEn = "A business representative (PO/BA), a developer, and a tester",
            wrongEn = listOf(
                "A project manager, a network administrator, and a security auditor",
                "Three test automation engineers working inside a separate QA department",
                "An external end user, a helpdesk agent, and a graphic UI designer"
            ),
            expEn = "The Three Amigos bring together business, development, and testing perspectives to build shared understanding and clarify acceptance criteria early."
        ),
        RawQ(
            id = "q070", ch = 4,
            q = "حساب رسوم الشحن: أقل من 50$ رسوم 10$، من 50$ إلى 199$ رسوم 5$، ومن 200$ فأكثر مجاني (القيم السالبة مرفوضة). أي مجموعة تحقق 100% Equivalence Partitioning؟",
            correct = "-5، 25، 120، 250",
            wrong = listOf("10، 50، 199، 200", "-1، 49، 50، 200", "25، 100، 200"),
            exp = "الأقسام الأربعة: (<0 غير صالح)، (0–49)، (50–199)، (≥200). المجموعة {-5, 25, 120, 250} تغطي الأقسام الأربعة بدقة.",
            qEn = "Shipping fee rules: orders below $50 pay $10, $50–$199 pay $5, $200 or more ship free, and negative amounts are rejected. Which input set achieves 100% EP coverage?",
            correctEn = "-5, 25, 120, 250",
            wrongEn = listOf("10, 50, 199, 200", "-1, 49, 50, 200", "25, 100, 200"),
            expEn = "There are four partitions: invalid (<0), $10 tier (0–49), $5 tier (50–199), and free tier (>=200). {-5, 25, 120, 250} hits every partition once."
        ),
        RawQ(
            id = "q071", ch = 4,
            q = "نظام يمنح خصماً للمبالغ العشرية بدقة سنت واحد من 100.00 إلى 500.00 شاملاً. ما القيم المطلوبة لـ 2-value BVA؟",
            correct = "99.99، 100.00، 500.00، 500.01",
            wrong = listOf(
                "99.00، 100.00، 500.00، 501.00",
                "99.99، 100.00، 100.01، 499.99، 500.00، 500.01",
                "100.00، 250.00، 500.00"
            ),
            exp = "عند دقة 0.01 تكون أقرب قيمة خارجية قبل 100.00 هي 99.99 وبعد 500.00 هي 500.01.",
            qEn = "A discount applies to purchase totals from $100.00 to $500.00 inclusive (precision 0.01). Which values are required for 2-value BVA?",
            correctEn = "99.99, 100.00, 500.00, 500.01",
            wrongEn = listOf(
                "99.00, 100.00, 500.00, 501.00",
                "99.99, 100.00, 100.01, 499.99, 500.00, 500.01",
                "100.00, 250.00, 500.00"
            ),
            expEn = "With two-decimal precision (0.01), the immediate outer neighbors are 99.99 and 500.01 alongside boundaries 100.00 and 500.00."
        ),
        RawQ(
            id = "q072", ch = 4,
            q = "حقل يقبل أعداداً صحيحة من 10 إلى 20 شاملاً. أي مجموعة تمثل 3-value BVA كاملة لهذا المجال؟",
            correct = "9، 10، 11، 19، 20، 21",
            wrong = listOf("9، 10، 20، 21", "10، 15، 20", "8، 9، 10، 20، 21، 22"),
            exp = "في 3-value BVA نختبر كل حد مع القيمتين المجاورتين له مباشرة: {9, 10, 11} للحد الأدنى و{19, 20, 21} للحد الأعلى.",
            qEn = "An input field accepts integers from 10 to 20 inclusive. Which set represents a complete 3-value BVA test suite for this range?",
            correctEn = "9, 10, 11, 19, 20, 21",
            wrongEn = listOf("9, 10, 20, 21", "10, 15, 20", "8, 9, 10, 20, 21, 22"),
            expEn = "3-value BVA requires each boundary plus both immediate neighbors: {9, 10, 11} and {19, 20, 21}."
        ),
        RawQ(
            id = "q073", ch = 4,
            q = "عند كتابة حالات اختبار سلبية (Negative tests) لنموذج يحتوي على حقلي «العمر» و«كود الخصم» وفق EP، ما الممارسة الصحيحة لتجنب إخفاء العيوب (Defect masking)؟",
            correct = "وضع قيمة غير صالحة في حقل واحد فقط مع إبقاء الحقل الآخر صالحاً في كل حالة اختبار",
            wrong = listOf(
                "إدخال قيم غير صالحة في جميع الحقول معاً في حالة اختبار واحدة لتوفير الوقت",
                "اختبار الحقول الصالحة فقط وتجاهل الأقسام غير الصالحة في التحليل",
                "حذف رسائل الخطأ عند فشل أكثر من حقل في الوقت نفسه"
            ),
            exp = "جمع عدة قيم غير صالحة معاً قد يجعل تحقق الحقل الأول يوقف التحقق من الحقل الثاني (Defect masking).",
            qEn = "When designing negative equivalence partition tests for a form with multiple fields (Age and Promo Code), how do you prevent defect masking?",
            correctEn = "Include only one invalid partition value per test case while keeping all other inputs valid",
            wrongEn = listOf(
                "Combine invalid values for every field in a single test case to save execution time",
                "Test only valid partitions and omit invalid partitions from test design",
                "Suppress error messages whenever two fields fail validation simultaneously"
            ),
            expEn = "Combining multiple invalid inputs in one test can mask defects in the second field if validation aborts on the first invalid field."
        ),
        RawQ(
            id = "q074", ch = 4,
            q = "قواعد الخصم: يحصل العميل على خصم إذا كان «عضو VIP» أو إذا كان «الطلب ≥ 100$ ومعه كوبون». في جدول قرار كامل (8 قواعد) للشروط الثلاثة، كم قاعدة تمنح الخصم؟",
            correct = "5 قواعد",
            wrong = listOf("3 قواعد", "4 قواعد", "6 قواعد"),
            exp = "VIP=Y تمنح الخصم في 4 قواعد، ومع VIP=N تمنحه قاعدة واحدة (الطلب≥100=Y والكوبون=Y). المجموع = 5 قواعد.",
            qEn = "A customer receives a discount if they are a VIP member OR if (Order >= $100 AND has Coupon). Out of all 8 rules in the full decision table, how many grant the discount?",
            correctEn = "5 rules",
            wrongEn = listOf("3 rules", "4 rules", "6 rules"),
            expEn = "All 4 rules with VIP=True grant the discount, plus 1 rule with VIP=False, Order>=100=True, Coupon=True. Total = 5 of 8 rules."
        ),
        RawQ(
            id = "q075", ch = 4,
            q = "في جدول قرار كامل من 4 قواعد، لاحظت أن قاعدتين تشتركان في (الشرط1 = T) وتختلفان في (الشرط2 = T/F) لكن النتيجة متطابقة في كلتيهما. ماذا نفعل عند طيّ الجدول (Collapsed table)؟",
            correct = "ندمج القاعدتين في قاعدة واحدة ونضع رمز (–) للشرط2 ليصبح المجموع 3 قواعد",
            wrong = listOf(
                "نحذف الشرط1 ونضاعف عدد القواعد إلى 8 قواعد لضمان التغطية",
                "نترك الجدول بـ 4 قواعد كما هو ونغيّر إحدى النتائج عشوائياً",
                "نحذف كل الحالات التي تكون فيها النتيجة إيجابية لتقليص الاختبار"
            ),
            exp = "عند عدم تأثير الشرط الثاني على النتيجة عند تحقق الشرط الأول، يُرمز له بـ (– Don't care) وتندمج القاعدتان.",
            qEn = "In a 4-rule full decision table, two rules with Condition1=True produce the exact same actions regardless of whether Condition2 is True or False. How is the table collapsed?",
            correctEn = "Merge both rules into one rule with Condition2 marked irrelevant ('-'), leaving 3 rules",
            wrongEn = listOf(
                "Remove Condition1 and double the remaining table to 8 rules",
                "Keep all 4 rules and arbitrarily modify one action outcome",
                "Delete every rule that yields a positive action to minimize tests"
            ),
            expEn = "Section 4.2.3: Rules where a condition does not affect the outcome are merged using '-' (don't care), reducing test effort without losing coverage."
        ),
        RawQ(
            id = "q076", ch = 4,
            q = "دورة حياة طلب تبدأ من Created: (Created--pay-->Paid)، (Paid--ship-->Shipped)، (Shipped--deliver-->Delivered)، وكذلك (Created--cancel-->Cancelled) و(Paid--refund-->Cancelled). ما أقل عدد حالات اختبار لتحقيق 100% Valid transitions coverage؟",
            correct = "3 حالات اختبار",
            wrong = listOf("2 حالات اختبار", "4 حالات اختبار", "5 حالات اختبار"),
            exp = "1) Created→Paid→Shipped→Delivered (تغطي pay, ship, deliver)، 2) Created→Cancelled (تغطي cancel)، 3) Created→Paid→Cancelled (تغطي refund).",
            qEn = "An order starts at Created with 5 valid transitions: Created-(pay)->Paid, Paid-(ship)->Shipped, Shipped-(deliver)->Delivered, Created-(cancel)->Cancelled, Paid-(refund)->Cancelled. What is the minimum number of test cases for 100% valid transitions (0-switch) coverage?",
            correctEn = "3 test cases",
            wrongEn = listOf("2 test cases", "4 test cases", "5 test cases"),
            expEn = "Three end-to-end paths from Created are needed: (1) Created→Paid→Shipped→Delivered, (2) Created→Cancelled, and (3) Created→Paid→Cancelled."
        ),
        RawQ(
            id = "q077", ch = 4,
            q = "صراف آلي يحتوي على الانتقال: Idle --(withdraw [balance >= amount] / dispense)--> Dispensing. إذا كان الرصيد 50$ وطلب المستخدم سحب 80$، ماذا يحدث وفق مخطط الحالة؟",
            correct = "لا ينفذ الانتقال لأن شرط الحماية (Guard condition) غير متحقق ويبقى النظام في Idle",
            wrong = listOf(
                "ينتقل فوراً إلى Dispensing ويصرف 50$ تلقائياً دون فحص الشرط",
                "ينتقل إلى حالة تم تعديلها قسراً ويغلق الجلسة مع صرف المبلغ كاملاً",
                "يُلغى الحدث وتُسجَّل العملية كـ Paid رغم عدم كفاية الرصيد"
            ),
            exp = "الانتقال المقيد بـ [Guard condition] لا يقع إلا إذا كان الشرط صحيحاً عند وصول الحدث.",
            qEn = "An ATM defines the transition: Idle --(withdraw [balance >= amount] / dispense)--> Dispensing. If balance is $50 and the user requests $80, what happens according to the state model?",
            correctEn = "The transition does not fire because the guard condition is false, so the ATM stays in Idle",
            wrongEn = listOf(
                "It immediately moves to Dispensing and outputs $50 regardless of the guard",
                "It bypasses the guard condition and dispenses $80 before alerting the bank",
                "It permanently transitions into an undefined state while crediting the account"
            ),
            expEn = "A transition labeled 'event [guard] / action' only triggers when the event occurs AND the guard condition evaluates to true."
        ),
        RawQ(
            id = "q078", ch = 4,
            q = "نظام له 3 حالات (Draft وPublished وArchived) وحدثان (publish وarchive). إذا كان المسموح فقط 3 انتقالات صالحة، كم انتقالاً غير صالح (Invalid) يظهر في جدول الحالات (State table)؟",
            correct = "3 انتقالات غير صالحة",
            wrong = listOf("2 انتقالات غير صالحة", "5 انتقالات غير صالحة", "6 انتقالات غير صالحة"),
            exp = "إجمالي خلايا جدول الحالات = 3 حالات × 2 حدث = 6 خلايا؛ وبما أن 3 منها صالحة، فالمتبقي (6 - 3 = 3) انتقالات غير صالحة.",
            qEn = "A document workflow has 3 states (Draft, Published, Archived) and 2 events (publish, archive). Only 3 (state, event) transitions are valid. How many invalid transitions appear in the state table?",
            correctEn = "3 invalid transitions",
            wrongEn = listOf("2 invalid transitions", "5 invalid transitions", "6 invalid transitions"),
            expEn = "The state table contains 3 states × 2 events = 6 cells. Subtracting the 3 valid transitions leaves 3 invalid transitions."
        ),
        RawQ(
            id = "q079", ch = 4,
            q = "في المقطع: read(x); if (x > 0) { y = x * 2; } print(y); — ما قيمة Statement coverage عند التشغيل بمدخل واحد x = 5؟",
            correct = "100% (بينما Branch coverage = 50% فقط)",
            wrong = listOf(
                "75% (ولتحقيق 100% يجب تشغيل x = -1 أيضاً)",
                "50% (لأن الجملة الشرطية تحتوي على فرعين)",
                "100% Statement و 100% Branch coverage معاً"
            ),
            exp = "x=5 ينفذ كل الأوامر الأربعة (100% Statement)، لكنه لا يزور فرع False للشرط if (x > 0) فتبقى Branch = 50%.",
            qEn = "Given the pseudocode: read(x); if (x > 0) { y = x * 2; } print(y); — what coverage is achieved by a single test case x = 5?",
            correctEn = "100% statement coverage, while branch coverage is only 50%",
            wrongEn = listOf(
                "75% statement coverage; x = -1 is still needed to reach 100%",
                "50% statement coverage because the if-statement has two branches",
                "100% statement coverage and 100% branch coverage simultaneously"
            ),
            expEn = "With x = 5, all 4 executable statements run (100% statement coverage), but the False branch of 'if (x > 0)' is skipped (50% branch coverage)."
        ),
        RawQ(
            id = "q080", ch = 4,
            q = "في المقطع: if (a > 10) res=1 else res=2; if (b == 0) err=true; — ما أقل عدد حالات اختبار لتحقيق 100% Branch coverage؟",
            correct = "2 حالات اختبار (مثل: a=15, b=0 و a=5, b=1)",
            wrong = listOf("1 حالة اختبار تكفي لجميع الفروع", "3 حالات اختبار كحد أدنى", "4 حالات اختبار كحد أدنى"),
            exp = "لدينا 4 فروع مستقلة: الحالة الأولى تغطي (True, True) والثانية تغطي (False, False)، فحالتان تكفيان لتغطية 100% Branch.",
            qEn = "Given two sequential conditions: if (a > 10) res = 1 else res = 2; if (b == 0) err = true; — what is the minimum number of test cases required for 100% branch coverage?",
            correctEn = "2 test cases (e.g., a=15, b=0 and a=5, b=1)",
            wrongEn = listOf(
                "1 test case is sufficient to exercise every branch",
                "3 test cases minimum",
                "4 test cases minimum"
            ),
            expEn = "There are 4 branches across two independent decisions. Test 1 (15, 0) takes both True branches; Test 2 (5, 1) takes both False branches."
        ),
        RawQ(
            id = "q081", ch = 4,
            q = "لماذا قد تفشل مجموعة اختبارات تحقق 100% Statement coverage في الوصول إلى 100% Branch coverage داخل جملة `if (x > 10) log(x);` بدون `else`؟",
            correct = "لأن حالة واحدة بقيمة x = 15 تنفذ كل الأوامر (100% Statement) دون أخذ فرع False المخفي",
            wrong = listOf(
                "لأن جملة log(x) لا تُحتسب ضمن الأوامر التنفيذية في White-box testing",
                "لأن جملة if التي لا تحتوي على else لا تملك فرع False إطلاقاً",
                "لأن Branch coverage تقيس فقط الحلقات التكرارية وتتجاهل الجمل الشرطية"
            ),
            exp = "فرع False في `if` بدون `else` هو مسار حقيقي نحو ما بعد الشرط، ولا يتطلب أي أمر بداخله ليتأثر، لذا يغيب في Statement coverage.",
            qEn = "Why can a test suite achieve 100% statement coverage on `if (x > 10) log(x);` (with no `else` block) yet still fail to reach 100% branch coverage?",
            correctEn = "A single test with x = 15 executes every statement (100%), while never taking the implicit False branch",
            wrongEn = listOf(
                "Because the log(x) call is ignored when calculating executable statements",
                "Because an if-statement without an explicit else block has no False branch",
                "Because branch coverage measures loops exclusively and excludes conditions"
            ),
            expEn = "An if-statement without an else still has an implicit False branch skipping the block; missing it gives 100% statement coverage but 50% branch coverage."
        ),
        RawQ(
            id = "q082", ch = 4,
            q = "قبل كتابة الكود لميزة قفل الحساب، اجتمع مالك المنتج والمطور والمختبر لصياغة أمثلة قابلة للتنفيذ: «بعد 3 محاولات فاشلة يُغلق الحساب؛ المحاولة الثالثة الناجحة تبقيه مفتوحاً». أي نهج هذا؟",
            correct = "Acceptance Test-Driven Development (ATDD)",
            wrong = listOf("Code-driven mutation testing", "Post-release beta acceptance", "Checklist-only regression"),
            exp = "تعاون الثلاثي قبل التنفيذ لاشتقاق حالات اختبار مباشرة من معايير القبول يمثل ATDD (القسم 4.5.3).",
            qEn = "Before coding an account-lock feature, the PO, developer, and tester write executable examples: '3 consecutive failed logins lock the account; a valid 3rd login keeps it open.' Which approach is this?",
            correctEn = "Acceptance Test-Driven Development (ATDD)",
            wrongEn = listOf(
                "Code-driven mutation testing",
                "Post-release beta acceptance testing",
                "Checklist-only maintenance regression"
            ),
            expEn = "ATDD is a test-first, collaborative approach where Three Amigos derive acceptance tests from criteria before implementation starts."
        ),
        RawQ(
            id = "q083", ch = 4,
            q = "نجحت كل حالات EP وBVA لنموذج رفع الملفات، لكن المختبر خصص جلسة 45 دقيقة لتجربة رفع ملفات بأسماء طويلة جداً ورؤوس معطوبة فانهار التطبيق. ما الدور الذي لعبه Exploratory testing هنا؟",
            correct = "تكميل الاختبارات الرسمية باكتشاف تفاعلات غير متوقعة وسيناريوهات لم تُذكر في المواصفات",
            wrong = listOf(
                "إثبات أن تقنيات الصندوق الأسود عديمة الفائدة ويجب إلغاؤها",
                "قياس التغطية الدقيقة للأسطر البرمجية في خادم التخزين",
                "الالتزام بنص اختباري مسبق الخطوات دون أي ارتجال"
            ),
            exp = "الاختبار الاستكشافي يكمل التقنيات المنهجية عبر الإبداع وسرعة الاستجابة لسلوك التطبيق الفعلي.",
            qEn = "After all formal EP and BVA tests pass on a file uploader, a tester runs a 45-minute session trying corrupted headers and huge filenames, crashing the app. How does exploratory testing add value here?",
            correctEn = "It complements black-box techniques by uncovering unexpected interactions and unstated edge cases",
            wrongEn = listOf(
                "It proves systematic specification-based techniques are obsolete",
                "It quantitatively measures branch coverage inside the storage server",
                "It enforces rigid adherence to pre-scripted step-by-step test procedures"
            ),
            expEn = "Exploratory testing complements black-box and white-box techniques by using tester insight to probe unforeseen behaviors."
        ),
        RawQ(
            id = "q084", ch = 4,
            q = "أثناء اختبار نموذج الدفع، بادر المختبر فوراً بتجربة القيمة صفر، أرقام سالبة، رموز خاصة مثل `' OR 1=1`، وتاريخ 29 فبراير استناداً لخبرته بأعطال سابقة. ما اسم هذه التقنية؟",
            correct = "Error guessing (via Fault attacks)",
            wrong = listOf("State transition diagramming", "Statement coverage analysis", "Collapsed decision tabling"),
            exp = "استخدام قوائم الأعطال الشائعة (مثل الصفر والرموز الخاصة والتواريخ الحرجة) لتصميم الاختبارات هو Error guessing / Fault attacks.",
            qEn = "While testing a payment form, a tester immediately tries zero, negative amounts, `' OR 1=1`, and Feb 29 based on prior defect experience. Which technique is this?",
            correctEn = "Error guessing (Fault attack)",
            wrongEn = listOf("State transition testing", "Statement coverage analysis", "Collapsed decision table testing"),
            expEn = "Error guessing anticipates common coding mistakes; fault attacks formalize it using known defect categories and checklists."
        ),
        RawQ(
            id = "q085", ch = 4,
            q = "قصة مستخدم تقول: «كمستخدم، أريد بحثاً سريعاً ووضعاً ليلياً وتصدير PDF وتسجيل دخول عبر Google في نفس البطاقة». أي معيار من معايير INVEST تنتهكه القصة بشكل صارخ؟",
            correct = "Small (و Independent)؛ إذ تدمج أربع ميزات كبيرة يجب تقسيمها",
            wrong = listOf(
                "Testable؛ لأن وضع الليل لا يمكن اختبار مظهره نهائياً",
                "Valuable؛ لأن الميزات المطلوبة لا تفيد أي مستخدم",
                "Negotiable؛ لأن القصة لا يجوز مناقشتها مع المطورين"
            ),
            exp = "وفق INVEST يجب أن تكون قصة المستخدم صغيرة (Small) وقابلة للإنجاز في تكرار واحد ولا تكدس عدة ميزات مستقلة.",
            qEn = "A user story reads: 'As a user, I want fast search, dark mode, PDF export, and OAuth login in this ticket.' Which INVEST criterion is MOST clearly violated?",
            correctEn = "Small (and Independent); it bundles multiple distinct features that should be split",
            wrongEn = listOf(
                "Testable; because dark mode colors can never be verified objectively",
                "Valuable; because none of the listed features benefit end users",
                "Negotiable; because user stories must never be discussed with developers"
            ),
            expEn = "Under INVEST (Independent, Negotiable, Valuable, Estimable, Small, Testable), a single story bundling four large features violates Small and should be split."
        ),
        RawQ(
            id = "q110", ch = 4,
            q = "في المقطع: `if (score >= 50) status=\"Pass\" else status=\"Fail\"; if (honors) badge=\"Gold\";` — شغّلنا حالتي اختبار: `(score=80, honors=true)` و`(score=90, honors=false)`. ما نسبة Branch coverage المحققة؟",
            correct = "75%",
            wrong = listOf("50%", "67%", "100%"),
            exp = "إجمالي الفروع = 4 (True/False للشرط الأول، وTrue/False للشرط الثاني). الحالةان غطّتا فرع True للأول وكلا فرعي الثاني، ولم تغطيا فرع False للأول، أي 3 من 4 = 75%.",
            qEn = "Given `if (score >= 50) status=\"Pass\" else status=\"Fail\"; if (honors) badge=\"Gold\";`, tests run with `(80, true)` and `(90, false)`. What branch coverage is achieved?",
            correctEn = "75%",
            wrongEn = listOf("50%", "67%", "100%"),
            expEn = "There are 4 branches total (2 per decision). Both outcomes of 'honors' are covered (2), plus only the True branch of 'score >= 50' (1). 3 / 4 = 75%."
        ),
        RawQ(
            id = "q111", ch = 4,
            q = "أي مما يلي يُعد ممارسة خاطئة تقلل فاعلية Checklist-based testing وفق منهج CTFL v4.0؟",
            correct = "الإبقاء على بنود القائمة جامدة دون تحديث عبر الإصدارات أو جعلها شديدة التفصيل كسكربت جامد",
            wrong = listOf(
                "بناء القائمة استناداً إلى أسباب الأعطال المتكررة ومعايير الجودة للمشروع",
                "توجيه كل عنصر في القائمة كسؤال أو شرط تحقق واضح يشجع الفحص الذكي",
                "استخدام القائمة كأداة مكملة للاختبار المبني على المخاطر وليس بديلاً وحيداً"
            ),
            exp = "القسم 4.4.3: قوائم الفحص التي لا تُحدَّث تصبح متقادمة، والمفرطة في التفصيل تقيّد المختبر وتضعف التنويع.",
            qEn = "Which of the following is considered a POOR practice when applying checklist-based testing according to CTFL v4.0?",
            correctEn = "Leaving checklists unmaintained across releases or making items overly detailed like rigid scripts",
            wrongEn = listOf(
                "Deriving checklist items from historical failure patterns and project quality attributes",
                "Phrasing each entry as a clear verification prompt to guide tester coverage",
                "Using checklists to supplement systematic risk-based test suites"
            ),
            expEn = "Section 4.4.3 warns against outdated checklists and overly detailed checklist entries that restrict exploration."
        ),
        RawQ(
            id = "q112", ch = 4,
            q = "يشترط النظام أن يكون طول كلمة المرور من 5 إلى 12 حرفاً شاملاً. ما أطوال الكلمات التي تغطي 100% 2-value BVA؟",
            correct = "4، 5، 12، 13",
            wrong = listOf("5، 8، 12", "4، 5، 6، 11، 12، 13", "0، 5، 12، 20"),
            exp = "الحدود الصحيحة للطول هي 5 و12، ومع أقرب قيمة خارجية مجاورة نحصل على 4 و5 و12 و13.",
            qEn = "A password policy states that passwords must be 5 to 12 characters long inclusive. Which set of password lengths achieves 100% 2-value BVA coverage?",
            correctEn = "4, 5, 12, 13",
            wrongEn = listOf("5, 8, 12", "4, 5, 6, 11, 12, 13", "0, 5, 12, 20"),
            expEn = "Applying 2-value BVA to valid length partition [5..12] tests boundaries 5 and 12 plus invalid neighbors 4 and 13."
        ),
        RawQ(
            id = "q113", ch = 4,
            q = "عند بناء جدول قرار لنظام تأمين، لاحظت أن العمود 3 ينفّذ إجراء «رفض الطلب» و«إصدار خصم» معاً لنفس مجموعة الشروط. ماذا كشف جدول القرار هنا؟",
            correct = "تناقضاً أو غموضاً في قواعد العمل داخل المواصفات قبل كتابة الكود",
            wrong = listOf(
                "وجود تسرب للذاكرة أثناء تصفح واجهة العميل في المتصفح",
                "أن عدد الشروط يجب ألا يتجاوز شرطاً واحداً في أي جدول",
                "أن اختبار الصندوق الأبيض حقق 100% Statement coverage"
            ),
            exp = "من أهم مزايا Decision Table Testing كشف التناقضات (Contradictions) والنقص في متطلبات العمل مبكراً.",
            qEn = "While building a decision table for insurance underwriting, column 3 triggers both 'Reject Application' and 'Issue Discount'. What has the decision table helped reveal?",
            correctEn = "A contradiction or ambiguity in the business specification before coding",
            wrongEn = listOf(
                "A runtime memory leak occurring in the customer browser UI",
                "A syntax rule limiting decision tables to a single condition row",
                "Confirmation that component unit tests reached 100% statement coverage"
            ),
            expEn = "Decision table analysis frequently exposes omissions, contradictions, and conflicting business rules in specifications."
        ),
        RawQ(
            id = "q114", ch = 4,
            q = "ما الذي يميز صياغة Scenario-oriented (مثل Given/When/Then) لمعايير القبول في قصص المستخدمين؟",
            correct = "وصف السلوك كسيناريو سياقي يحدد الحالة المسبقة والحدث والنتيجة المتوقعة بوضوح",
            wrong = listOf(
                "تحويل معايير القبول إلى قائمة من أرقام الأسطر البرمجية في الكود",
                "استبدال نقاش الفريق اليومي بوثيقة متطلبات شلالية مغلقة",
                "الاقتصار على قياس أزمنة الاستجابة دون فحص المنطق الوظيفي"
            ),
            exp = "صيغة Given (السياق الأولي) / When (الإجراء) / Then (النتيجة المتوقعة) تسهّل تحويل المعايير مباشرة إلى اختبارات BDD/ATDD.",
            qEn = "What characterizes the 'scenario-oriented' format (e.g., Given/When/Then) for writing acceptance criteria?",
            correctEn = "It expresses expected behavior chronologically via initial context, action trigger, and expected outcome",
            wrongEn = listOf(
                "It lists source code line numbers that must pass unit compilation",
                "It replaces team refinement conversations with a locked waterfall contract",
                "It focuses strictly on server throughput rather than functional behavior"
            ),
            expEn = "Scenario-oriented criteria (Given/When/Then) formalize preconditions, actions, and postconditions, making them easy to automate in ATDD/BDD."
        )
    )
}
