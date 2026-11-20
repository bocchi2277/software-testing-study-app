package com.example.data

internal object QuestionsCh1To3 {
    val items: List<RawQ> = listOf(
        // ==================== CHAPTER 1 (20 Questions) ====================
        RawQ(
            id = "q001", ch = 1,
            q = "أي مبدأ يقول إن الاختبار الشامل لكل المدخلات والتركيبات مستحيل؟",
            correct = "Exhaustive testing is impossible",
            wrong = listOf("Defects cluster together", "Absence-of-defects fallacy", "Tests wear out"),
            exp = "المبدأ الثاني: لا يمكن اختبار كل التركيبات، لذلك نستخدم المخاطر والأولويات وتقنيات تصميم الاختبار.",
            qEn = "Which of the following testing principles states that testing all combinations of inputs and preconditions is infeasible?",
            correctEn = "Exhaustive testing is impossible",
            wrongEn = listOf("Defects cluster together", "Absence-of-defects fallacy", "Tests wear out"),
            expEn = "Principle 2: Testing everything is impossible except in trivial cases; instead, risk analysis, prioritization, and test techniques are used."
        ),
        RawQ(
            id = "q002", ch = 1,
            q = "ما العلاقة الصحيحة بين Error وDefect وFailure؟",
            correct = "Error بشري يُدخل Defect في work product قد يسبب Failure عند التنفيذ",
            wrong = listOf(
                "Failure أثناء التشغيل يُنتج Defect في الكود يظهر لاحقاً كـ Error للمبرمج",
                "Defect في المتطلبات يسبب Error برمجياً لا يتحول إلى Failure عند التشغيل",
                "Error في البيئة يؤدي إلى Failure يولّد بدوره Defect في وثيقة التصميم"
            ),
            exp = "Error بشري ← Defect في المنتج ← Failure أثناء التشغيل. وليست كل Defects تسبب Failure.",
            qEn = "Which of the following statements BEST describes the causal relationship between an error, a defect, and a failure?",
            correctEn = "A human error can introduce a defect in a work product, which may cause a failure when executed",
            wrongEn = listOf(
                "A runtime failure produces a defect in source code that later triggers a programmer error",
                "A defect in requirements causes a code error that never results in an operational failure",
                "An environmental error leads to a failure, which in turn generates a defect in the design specification"
            ),
            expEn = "Humans make errors (mistakes), which introduce defects (faults/bugs) into work products; executing a defective code may cause a failure."
        ),
        RawQ(
            id = "q003", ch = 1,
            q = "ما الفرق الرئيسي بين Testing وDebugging؟",
            correct = "Testing يظهر الـ failures، بينما Debugging يحلل سبب الـ defect ويصلحه",
            wrong = listOf(
                "Testing يصلح الـ defects في الكود، بينما Debugging يثبت وجود failure للمستخدم",
                "Debugging ينفذ حالات الاختبار في البيئة، بينما Testing يعدل الشيفرة المصدرية",
                "كلاهما نشاط تطويري يهدف لإعادة كتابة المتطلبات قبل بدء التصميم"
            ),
            exp = "Debugging نشاط تطويري لتحليل السبب وإصلاحه، ثم يأتي Confirmation testing للتحقق من الإصلاح.",
            qEn = "Which of the following BEST distinguishes testing from debugging?",
            correctEn = "Testing reveals failures caused by defects, whereas debugging finds, analyzes, and fixes the root cause in code",
            wrongEn = listOf(
                "Testing repairs defects in source code, whereas debugging demonstrates failures to end users",
                "Debugging executes scripted test cases in the environment, whereas testing modifies the program code",
                "Both are development activities aimed exclusively at rewriting specifications before design"
            ),
            expEn = "Dynamic testing shows failures; debugging is a development activity to locate and fix the defect, followed by confirmation testing."
        ),
        RawQ(
            id = "q004", ch = 1,
            q = "فريق يعيد تشغيل نفس مجموعة الاختبارات منذ سنوات ولم تعد تكشف defects جديدة. أي مبدأ ينطبق؟",
            correct = "Tests wear out",
            wrong = listOf("Early testing saves time and money", "Testing is context dependent", "Exhaustive testing is impossible"),
            exp = "المبدأ الخامس (كان يُعرف سابقاً بـ Pesticide paradox): يجب مراجعة الاختبارات وتحديثها وإضافة حالات جديدة.",
            qEn = "A team has executed the same regression test suite unchanged for several releases and it no longer finds new defects. Which testing principle explains this?",
            correctEn = "Tests wear out",
            wrongEn = listOf("Early testing saves time and money", "Testing is context dependent", "Exhaustive testing is impossible"),
            expEn = "Principle 5 (Tests wear out, formerly known as the pesticide paradox): existing tests lose effectiveness over time and must be updated or complemented with new tests."
        ),
        RawQ(
            id = "q005", ch = 1,
            q = "في أي من الحالات التالية يقع الفريق في فخ Absence-of-defects fallacy؟",
            correct = "نظام خالٍ من الـ defects المعروفة لكنه لا يلبي احتياجات المستخدمين الفعلية",
            wrong = listOf(
                "نظام تتركز معظم الـ defects المكتشفة فيه داخل وحدة برمجية واحدة عالية التعقيد",
                "نظام لم تُنفَّذ فيه اختبارات الكود الأبيض رغم تغطية جميع حالات الاستخدام",
                "نظام تأخر موعد إطلاقه بسبب إعادة تشغيل نفس مجموعة الاختبارات دون تحديث"
            ),
            exp = "إصلاح كل الـ defects لا يضمن نجاح النظام إن لم يحقق حاجة المستخدم (Validation).",
            qEn = "Which of the following situations illustrates the 'absence-of-defects fallacy'?",
            correctEn = "A system is free of known specification defects, yet fails to meet actual user needs and business goals",
            wrongEn = listOf(
                "A system where most discovered defects are concentrated inside one highly complex software module",
                "A system where white-box coverage was omitted despite full execution of all documented use cases",
                "A system delayed past release because the same automated test suite was re-run without maintenance"
            ),
            expEn = "Principle 7: Fixing all defects against a flawed specification does not ensure success if the product is unusable or fails validation."
        ),
        RawQ(
            id = "q006", ch = 1,
            q = "أي نشاط من أنشطة الاختبار يجيب عن سؤال «ماذا نختبر؟»",
            correct = "Test analysis",
            wrong = listOf("Test design", "Test implementation", "Test completion"),
            exp = "Test analysis تحدد test conditions من test basis، وTest design تجيب عن «كيف نختبر؟».",
            qEn = "Which test activity focuses on answering the question 'What to test?' in terms of measurable coverage criteria?",
            correctEn = "Test analysis",
            wrongEn = listOf("Test design", "Test implementation", "Test completion"),
            expEn = "Test analysis analyzes the test basis to identify testable features and test conditions ('what to test'), while test design addresses 'how to test'."
        ),
        RawQ(
            id = "q007", ch = 1,
            q = "ما الفائدة الأساسية من Traceability بين test basis وTest cases؟",
            correct = "تسهيل تحليل أثر التغيير وقياس تغطية المتطلبات بحالات الاختبار",
            wrong = listOf(
                "تقليص زمن تنفيذ الاختبارات عبر توليد بيانات الاختبار تلقائياً",
                "الاستغناء عن إعداد test plan وتقارير الإغلاق في المشاريع الرشيقة",
                "ضمان خلو النظام من أي defects غير متوقعة في بيئة الإنتاج"
            ),
            exp = "Traceability تدعم تحليل الأثر وتدقيق التغطية وإعداد تقارير الحالة.",
            qEn = "Which of the following is a primary benefit of maintaining traceability between the test basis and test cases?",
            correctEn = "Supporting impact analysis of changes and evaluating requirement coverage by test cases",
            wrongEn = listOf(
                "Reducing test execution duration by automatically generating synthetic test data",
                "Eliminating the need for a test plan and completion reports in Agile projects",
                "Guaranteeing that no unexpected defects will occur in production after release"
            ),
            expEn = "Bidirectional traceability facilitates impact analysis, coverage assessment, audits, and clear status communication."
        ),
        RawQ(
            id = "q008", ch = 1,
            q = "أي مما يلي من Testware المنتَجة أثناء Test implementation؟",
            correct = "Test procedures و test suites",
            wrong = listOf("Test completion report و lessons learned", "Defect reports و test execution logs", "Test plan و risk register"),
            exp = "Test implementation تعد الـ procedures والـ suites وجدول التنفيذ وبيانات وبيئة الاختبار.",
            qEn = "Which of the following testware items are typically produced during the 'test implementation' activity?",
            correctEn = "Test procedures and test suites",
            wrongEn = listOf("Test completion report and lessons learned", "Defect reports and test execution logs", "Test plan and project risk register"),
            expEn = "Test implementation creates or finalizes test procedures, automated scripts, test suites, test data, the execution schedule, and the test environment."
        ),
        RawQ(
            id = "q009", ch = 1,
            q = "ما الميزة الرئيسية لاستقلالية الاختبار (Independence of testing)؟",
            correct = "تقليل التحيز المعرفي للمؤلف وكشف defects قد يغفل عنها من أنشأ المنتج",
            wrong = listOf(
                "ضمان فهم فوري ومشترك للمتطلبات دون الحاجة إلى جلسات توضيح",
                "خفض تكلفة المشروع الإجمالية وإلغاء الحاجة إلى مراجعات الكود النظيرة",
                "تسريع إصلاح الأخطاء بنقل مسؤولية الجودة كاملة إلى فريق الاختبار"
            ),
            exp = "Independence يقلل التحيز المعرفي، لكنه قد يسبب عزلة أو فجوة تواصل إن لم يُدر جيداً.",
            qEn = "Which of the following is a key advantage of independence in testing?",
            correctEn = "Independent testers avoid the author's cognitive bias and often uncover different kinds of failures",
            wrongEn = listOf(
                "It guarantees immediate shared understanding of requirements without team clarification meetings",
                "It reduces total project cost and eliminates the need for peer code reviews",
                "It accelerates defect resolution by transferring full quality ownership exclusively to testers"
            ),
            expEn = "Independent testers bring different assumptions and avoid confirmation bias, though high independence can risk isolation if communication is poor."
        ),
        RawQ(
            id = "q010", ch = 1,
            q = "أي عبارة تصف العلاقة بين QA وQC بدقة؟",
            correct = "QA نهج وقائي موجه للعملية، بينما الاختبار من أنشطة QC الموجهة للمنتج",
            wrong = listOf(
                "QA مرادف للاختبار الديناميكي، بينما QC يقتصر على كتابة الخطط والسياسات",
                "QC موجه لتحسين العمليات ومنع الأخطاء قبل وقوعها دون فحص المنتج",
                "QA يبدأ بعد تسليم النظام للعميل، بينما QC يتم أثناء التطوير المبكر"
            ),
            exp = "QA يمنع الـ defects بتحسين العمليات، والاختبار من أنشطة Quality Control لاكتشاف العيوب في المنتج.",
            qEn = "Which statement MOST accurately describes the relationship between Quality Assurance (QA) and Quality Control (QC)?",
            correctEn = "QA is a process-oriented, preventive approach, whereas testing is a product-oriented QC activity",
            wrongEn = listOf(
                "QA is synonymous with dynamic testing, whereas QC is limited to writing quality policies",
                "QC focuses solely on improving development processes without inspecting or executing products",
                "QA begins only after customer handover, whereas QC occurs exclusively during early analysis"
            ),
            expEn = "QA focuses on adhering to proper processes to prevent defects; QC comprises product-oriented activities such as testing to detect defects."
        ),
        RawQ(
            id = "q058", ch = 1,
            q = "اكتشاف خطأ منطقي في وثيقة المتطلبات أثناء مراجعة مبكرة بدلاً من اكتشافه في الإنتاج يوضح فائدة أي مبدأ؟",
            correct = "Early testing saves time and money",
            wrong = listOf("Testing shows the presence, not the absence of defects", "Defects cluster together", "Tests wear out"),
            exp = "المبدأ الثالث: الاختبار المبكر (Static وDynamic) يمنع تضاعف تكلفة إصلاح العيوب لاحقاً.",
            qEn = "Detecting a logical inconsistency in a requirements specification during an early review rather than in production demonstrates which testing principle?",
            correctEn = "Early testing saves time and money",
            wrongEn = listOf("Testing shows the presence, not the absence of defects", "Defects cluster together", "Tests wear out"),
            expEn = "Principle 3: Both static and dynamic testing started as early as possible reduce overall cost and rework."
        ),
        RawQ(
            id = "q059", ch = 1,
            q = "في نموذج Whole-team approach (CTFL v4.0)، ما الدور الأساسي للمختبرين مع الفريق؟",
            correct = "نقل المعرفة الاختبارية والتعاون مع المطورين والأعمال لضمان جودة شاملة",
            wrong = listOf(
                "تحمّل المسؤولية الحصرية عن الجودة ومنع أي مطور من كتابة اختبارات",
                "العمل في عزلة تامة عن الفريق وعدم حضور اجتماعات التخطيط اليومية",
                "تأجيل جميع أنشطة الاختبار حتى استلام النظام مكتملاً في نهاية المشروع"
            ),
            exp = "Whole-team approach يجعل الجودة مسؤولية مشتركة حيث يوجّه المختبرون الفريق ويدعمون بناء الاختبارات.",
            qEn = "Under the whole-team approach in CTFL v4.0, what is a key responsibility of testers?",
            correctEn = "Sharing testing expertise and collaborating with developers and business reps so everyone owns quality",
            wrongEn = listOf(
                "Assuming sole responsibility for quality and prohibiting developers from writing tests",
                "Working in strict isolation and skipping daily planning and refinement sessions",
                "Postponing all test activities until a fully integrated build is handed over at the end"
            ),
            expEn = "In the whole-team approach, quality is a shared team responsibility; testers coach teammates and collaborate on test strategies."
        ),
        RawQ(
            id = "q060", ch = 1,
            q = "أثناء تحليل المواصفات، استخلص المختبر جملة «يجب أن يرفض النظام كلمات المرور الأقل من 8 أحرف» كهدف قابل للاختبار. ماذا يُسمى هذا الناتج؟",
            correct = "Test condition",
            wrong = listOf("Test execution log", "Test completion report", "Defect summary report"),
            exp = "في Test analysis نشتق الـ Test conditions (ماذا نختبر) من الـ Test basis.",
            qEn = "While analyzing a specification, a tester derives the testable aspect 'System must reject passwords shorter than 8 characters'. What is this work product called?",
            correctEn = "Test condition",
            wrongEn = listOf("Test execution log", "Test completion report", "Defect summary report"),
            expEn = "A test condition is a testable aspect of a component or system derived from the test basis during test analysis."
        ),
        RawQ(
            id = "q061", ch = 1,
            q = "تغيير عملة الدفع دون تحديث واجهة حساب الضريبة تسبب في فشل الحسابات. أي مهارة اختبارية تساعد المختبر على الربط الفعال مع المطورين لتوضيح هذا الخلل؟",
            correct = "مهارات التواصل البناء والتفكير النقدي وتقديم تقارير موضوعية",
            wrong = listOf(
                "تجنب التواصل المباشر والاكتفاء بإغلاق النسخة فوراً دون شرح",
                "حفظ أسماء كل خوارزميات الترتيب دون فهم منطق الأعمال للنظام",
                "كتابة التقارير بأسلوب هجومي لإجبار المطور على قبول الأولوية"
            ),
            exp = "منهج CTFL v4.0 يؤكد أهمية المهارات الشخصية مثل التواصل الموضوعي والتفكير النقدي والفضول.",
            qEn = "Which generic tester skill MOST helps a tester communicate defect findings constructively without creating friction with developers?",
            correctEn = "Constructive communication, critical thinking, and objective, non-judgmental reporting",
            wrongEn = listOf(
                "Avoiding direct dialogue and immediately rejecting builds without documented evidence",
                "Memorizing sorting algorithms without understanding the application's business domain",
                "Writing confrontational defect reports to force developers to escalate priority"
            ),
            expEn = "CTFL v4.0 Section 1.5.1 highlights communication skills, critical thinking, curiosity, attention to detail, and domain knowledge."
        ),
        RawQ(
            id = "q096", ch = 1,
            q = "أي مما يلي لا يُعد من أهداف الاختبار النموذجية في منهج CTFL v4.0؟",
            correct = "تصحيح الشيفرة المصدرية مباشرة وإصدار التصحيحات للمستخدمين",
            wrong = listOf(
                "تقييم منتجات العمل مثل المتطلبات وقصص المستخدمين والتصميم",
                "التحقق من استيفاء جميع المتطلبات المحددة والالتزامات التعاقدية",
                "بناء الثقة في مستوى جودة كائن الاختبار وتقديم معلومات لأصحاب المصلحة"
            ),
            exp = "الاختبار يقيّم الجودة ويكشف العيوب ويوفر المعلومات، أما إصلاح الشيفرة فهو مهمة Debugging التطويرية.",
            qEn = "Which of the following is NOT a typical objective of testing according to CTFL v4.0?",
            correctEn = "Directly modifying source code to fix root causes of defects and deploy patches",
            wrongEn = listOf(
                "Evaluating work products such as requirements, user stories, designs, and code",
                "Verifying whether all specified requirements and contractual obligations are fulfilled",
                "Building confidence in the quality level of the test object and informing stakeholders"
            ),
            expEn = "Fixing source code is debugging (a development activity), whereas testing evaluates work products, finds defects, verifies requirements, and builds confidence."
        ),
        RawQ(
            id = "q097", ch = 1,
            q = "أي من المهام التالية تقع عادةً ضمن مسؤولية دور إدارة الاختبار (Test management role) في CTFL v4.0؟",
            correct = "تخطيط الاختبار ومراقبته والتحكم فيه وإعداد تقارير الإنجاز",
            wrong = listOf(
                "تصميم حالات الاختبار التفصيلية وتنفيذ الإجراءات وتقييم المخرجات",
                "كتابة اختبارات الوحدة آلياً وإصلاح الأخطاء البرمجية المكتشفة",
                "صياغة قصص المستخدمين وإقرار الميزانية المالية للمنظمة كاملة"
            ),
            exp = "القسم 1.4.5: دور إدارة الاختبار يركز على Planning, Monitoring, Control, Completion، بينما Testing role يركز على التحليل والتصميم والتنفيذ.",
            qEn = "Which set of responsibilities typically belongs to the 'test management role' in CTFL v4.0?",
            correctEn = "Test planning, test monitoring and control, and test completion",
            wrongEn = listOf(
                "Detailed test design, test implementation, and manual or automated test execution",
                "Writing component unit tests and debugging defects in production modules",
                "Authoring business user stories and approving corporate financial budgets"
            ),
            expEn = "CTFL v4.0 Section 1.4.5 splits responsibilities into the test management role (planning, monitoring, control, completion) and the testing role (analysis, design, implementation, execution)."
        ),
        RawQ(
            id = "q098", ch = 1,
            q = "أي مثال مما يلي يوضح مبدأ «الاختبار يعتمد على السياق» (Testing is context dependent) بأدق صورة؟",
            correct = "يُختبر برنامج أجهزة طبية بصرامة توثيق وتغطية أعلى بكثير من موقع ترويجي بسيط",
            wrong = listOf(
                "تركّزت 80% من عيوب تطبيق بنكي داخل وحدة حساب الفوائد المعقدة",
                "أدت إعادة تشغيل نفس مجموعة الانحدار دون تحديث إلى تراجع العيوب المكتشفة",
                "اكتشف الفريق عيباً في التصميم أثناء مراجعة المواصفات قبل كتابة سطر كود"
            ),
            exp = "المبدأ السادس: يختلف أسلوب الاختبار وتقنياته ومستوى صرامته باختلاف سياق النظام ومجاله ومخاطره.",
            qEn = "Which of the following examples BEST illustrates the principle 'Testing is context dependent'?",
            correctEn = "Safety-critical medical software undergoes far stricter coverage and audit rigor than a simple promotional website",
            wrongEn = listOf(
                "80% of defects in a banking application were clustered inside its complex interest calculation module",
                "Re-running an unchanged regression suite repeatedly stopped revealing new defects",
                "A design flaw was detected during an early specification review before coding started"
            ),
            expEn = "Principle 6: Testing cannot be applied identically everywhere; methodology, rigor, and techniques depend on domain, risk, and SDLC context."
        ),
        RawQ(
            id = "q099", ch = 1,
            q = "أي مما يلي يمكن أن يسبب Failure في نظام برمجي دون أن يكون ناجماً عن Error بشري في كتابة الشيفرة؟",
            correct = "ظروف بيئية قاسية مثل الإشعاع أو التداخل الكهرومغناطيسي أو هجمات أمنية متعمدة",
            wrong = listOf(
                "تخطي مرحلة كتابة خطة الاختبار في بداية المشروع التطويري",
                "انخفاض عدد حالات الاختبار المنفذة في بيئة ضمان الجودة",
                "اختيار مراجع تقني خارجي لمراجعة وثيقة المتطلبات الوظيفية"
            ),
            exp = "القسم 1.2.3: قد تنشأ الـ Failures أيضاً عن ظروف بيئية مادية (حرارة، إشعاع، مجالات مغناطيسية) أو أفعال متعمدة دون خطأ برمجي.",
            qEn = "According to CTFL v4.0, which of the following can cause a system failure WITHOUT being triggered by a human coding error?",
            correctEn = "Environmental conditions such as radiation or electromagnetic fields, or deliberate attacks",
            wrongEn = listOf(
                "Omitting the formal master test plan document at project kickoff",
                "Executing fewer regression test cases in the staging environment",
                "Assigning an external technical reviewer to inspect requirements"
            ),
            expEn = "Section 1.2.3 notes that failures can also result from environmental conditions (e.g., radiation, magnetic fields, pollution) or deliberate malicious actions."
        ),
        RawQ(
            id = "q100", ch = 1,
            q = "أي نشاط يتضمن أرشفة الـ Testware وتسليمها لفرق الصيانة وتلخيص الدروس المستفادة (Lessons learned)؟",
            correct = "Test completion",
            wrong = listOf("Test monitoring and control", "Test analysis", "Test implementation"),
            exp = "في Test completion نغلق العيوب أو نوثق المتبقي منها، ونؤرشف testware، وننقل المعرفة والدروس المستفادة.",
            qEn = "Which test activity includes archiving testware, handing over assets to maintenance teams, and capturing lessons learned?",
            correctEn = "Test completion",
            wrongEn = listOf("Test monitoring and control", "Test analysis", "Test implementation"),
            expEn = "Test completion occurs at project milestones or releases: creating the test completion report, archiving testware, closing environments, and documenting lessons learned."
        ),
        RawQ(
            id = "q101", ch = 1,
            q = "متى قد يكون نهج Whole-team approach أقل ملاءمة أو يحتاج توازناً إضافياً في المؤسسة؟",
            correct = "في الأنظمة عالية الخطورة أو التنظيمية التي تتطلب استقلالية صارمة لفريق الاختبار",
            wrong = listOf(
                "في الفرق الرشيقة الصغيرة التي تسعى لتسريع التواصل اليومي",
                "عندما يشارك المختبرون في صياغة معايير القبول لقصص المستخدمين",
                "عندما يتعاون المطورون والمختبرون في بناء اختبارات آلية مشتركة"
            ),
            exp = "القسم 1.5.2: رغم مزايا Whole-team، فإن البيئات التنظيمية الحساسة للسلامة قد تتطلب مستوى أعلى من استقلالية الاختبار.",
            qEn = "In which situation might the 'whole-team approach' be less suitable on its own and require higher test independence?",
            correctEn = "In heavily regulated or safety-critical projects that mandate strict independent test verification",
            wrongEn = listOf(
                "In small co-located Agile teams aiming to improve daily collaboration",
                "When testers participate early in refining user story acceptance criteria",
                "When developers and testers pair together to maintain automated API tests"
            ),
            expEn = "Section 1.5.2 notes that while whole-team dynamics improve collaboration, high-risk or regulated domains often require stronger test independence."
        ),

        // ==================== CHAPTER 2 (16 Questions) ====================
        RawQ(
            id = "q011", ch = 2,
            q = "أي مما يلي يُعد مستوى اختبار (Test level)؟",
            correct = "Component integration testing",
            wrong = listOf("Smoke testing", "Regression testing", "Exploratory testing"),
            exp = "المستويات الخمسة: Component, Component integration, System, System integration, Acceptance.",
            qEn = "Which of the following is classified as a 'test level' in CTFL v4.0?",
            correctEn = "Component integration testing",
            wrongEn = listOf("Smoke testing", "Regression testing", "Exploratory testing"),
            expEn = "The five test levels are component, component integration, system, system integration, and acceptance testing."
        ),
        RawQ(
            id = "q012", ch = 2,
            q = "بعد إصلاح defect، الاختبار الذي يتأكد أن نفس الحالة لم تعد تفشل هو:",
            correct = "Confirmation testing",
            wrong = listOf("Regression testing", "Smoke testing", "Load testing"),
            exp = "Confirmation (Re-testing) يعيد نفس الحالة الفاشلة. أما Regression فيتحقق من عدم كسر وظائف أخرى.",
            qEn = "After a developer fixes a reported defect, which type of testing verifies that the original failing test case now passes?",
            correctEn = "Confirmation testing",
            wrongEn = listOf("Regression testing", "Smoke testing", "Load testing"),
            expEn = "Confirmation testing (re-testing) confirms that a specific defect has been fixed; regression testing checks for unintended side effects elsewhere."
        ),
        RawQ(
            id = "q013", ch = 2,
            q = "ما الهدف الأساسي من Regression testing؟",
            correct = "اكتشاف آثار جانبية غير مقصودة في أجزاء أخرى بعد إجراء تغيير",
            wrong = listOf(
                "قياس زمن استجابة الخادم تحت أقصى عدد متوقع من المستخدمين",
                "مراجعة وثائق المتطلبات للتأكد من وضوحها قبل بدء البرمجة",
                "التحقق من أن الـ defect الذي أُصلح لم يعد يظهر في نفس الحالة فقط"
            ),
            exp = "يتأكد أن التعديل أو الإصلاح لم يكسر ما كان يعمل مسبقاً، وهو مرشح قوي للأتمتة.",
            qEn = "What is the primary objective of regression testing?",
            correctEn = "Detecting unintended side effects in unchanged parts of the software after a change",
            wrongEn = listOf(
                "Measuring server response latency under peak concurrent user traffic",
                "Reviewing requirement specifications for ambiguity before coding begins",
                "Verifying solely that the single fixed defect no longer fails in isolation"
            ),
            expEn = "Regression testing confirms that modifications (fixes, features, environment updates) have not broken existing working functionality."
        ),
        RawQ(
            id = "q014", ch = 2,
            q = "أي مما يلي مثال على اختبار غير وظيفي (Non-functional testing)؟",
            correct = "Performance efficiency testing",
            wrong = listOf("Login with valid user", "Add item to cart", "Reset password"),
            exp = "الأداء والأمان والاستخدامية والتوافق والموثوقية اختبارات غير وظيفية.",
            qEn = "Which of the following is an example of non-functional testing?",
            correctEn = "Performance efficiency testing",
            wrongEn = listOf("Verifying login with valid credentials", "Adding an item to the shopping cart", "Completing the password reset workflow"),
            expEn = "Non-functional testing evaluates quality characteristics such as performance efficiency, security, usability, compatibility, and reliability."
        ),
        RawQ(
            id = "q015", ch = 2,
            q = "اختبار قبول يجريه مستخدمون حقيقيون في بيئتهم الفعلية قبل الإصدار التجاري يسمى:",
            correct = "Beta testing",
            wrong = listOf("Alpha testing", "Component testing", "Contractual acceptance testing"),
            exp = "Alpha في موقع المنظمة المطورة، وBeta عند مستخدمين فعليين في بيئاتهم الحقيقية.",
            qEn = "Acceptance testing performed by external potential or existing users at their own locations before commercial release is called:",
            correctEn = "Beta testing",
            wrongEn = listOf("Alpha testing", "Component testing", "Contractual acceptance testing"),
            expEn = "Alpha testing takes place at the developing organization's site; beta testing takes place at the customers' own operational locations."
        ),
        RawQ(
            id = "q016", ch = 2,
            q = "ما هي محفزات Maintenance testing الثلاثة في المنهج؟",
            correct = "Modification و Migration و Retirement",
            wrong = listOf("Planning و Design و Execution", "Review و Inspection و Walkthrough", "Confirmation و Regression و Smoke"),
            exp = "Maintenance testing يُطلق بعد التعديل أو الترحيل أو إيقاف النظام عن الخدمة.",
            qEn = "Which three triggers initiate maintenance testing on an operational system?",
            correctEn = "Modification, migration, and retirement",
            wrongEn = listOf("Planning, design, and execution", "Review, inspection, and walkthrough", "Confirmation, regression, and smoke testing"),
            expEn = "CTFL v4.0 Section 2.3 defines three maintenance triggers: modifications (enhancements/fixes), migrations (platform shifts), and retirement (decommissioning)."
        ),
        RawQ(
            id = "q017", ch = 2,
            q = "كيف يساعد Impact analysis في تخطيط Maintenance testing؟",
            correct = "يحدد أجزاء النظام المتأثرة بالتغيير لتقدير نطاق إعادة الاختبار ومخاطره",
            wrong = listOf(
                "يحسب سرعة كتابة المطورين للشيفرة الجديدة مقارنة بالإصدار السابق",
                "يحذف كل حالات الاختبار القديمة تلقائياً ويستبدلها باختبارات يدوية",
                "يقلص حجم قاعدة البيانات الإنتاجية قبل تنفيذ ترحيل البيانات"
            ),
            exp = "تحليل الأثر يحدد ما يجب إعادة اختباره (regression) بحسب المخاطر وحجم التغيير.",
            qEn = "How does impact analysis assist in planning maintenance testing?",
            correctEn = "It identifies affected areas of the system to scope regression testing based on risk and change size",
            wrongEn = listOf(
                "It measures developer coding velocity compared to previous software releases",
                "It automatically deletes legacy test suites and converts them into manual scripts",
                "It compresses the live production database prior to executing a data migration"
            ),
            expEn = "Impact analysis evaluates which parts of an existing system may be impacted by a change so regression testing can be accurately scoped."
        ),
        RawQ(
            id = "q018", ch = 2,
            q = "ما الذي يميز نهج Test-Driven Development (TDD)؟",
            correct = "كتابة اختبار وحدة فاشل أولاً ثم كتابة الحد الأدنى من الكود لتمريره",
            wrong = listOf(
                "كتابة الكود الكامل للنظام ثم تأليف اختبارات القبول بعد إطلاق النسخة",
                "الاعتماد على الاختبار الاستكشافي اليدوي بدلاً من اختبارات الوحدة الآلية",
                "تأجيل تصميم حالات الاختبار إلى ما بعد انتهاء مرحلة تكامل الأنظمة"
            ),
            exp = "TDD نهج Test-first على مستوى المكونات يتبعه Refactoring، بينما ATDD يشتق الاختبارات من Acceptance criteria.",
            qEn = "Which statement BEST characterizes Test-Driven Development (TDD)?",
            correctEn = "Writing a failing component test first, then writing just enough code to pass it and refactoring",
            wrongEn = listOf(
                "Writing the full application code first and authoring acceptance tests after release",
                "Replacing automated unit tests entirely with unscripted manual exploratory testing",
                "Deferring all test case design until system integration testing is completed"
            ),
            expEn = "TDD is a test-first approach (Red → Green → Refactor) at the component level, whereas ATDD derives tests from acceptance criteria."
        ),
        RawQ(
            id = "q062", ch = 2,
            q = "أي ممارسة تجسّد مفهوم Shift-left في دورة حياة تطوير البرمجيات؟",
            correct = "مراجعة المتطلبات مبكراً وكتابة حالات الاختبار قبل كتابة الكود وتشغيل Static analysis",
            wrong = listOf(
                "زيادة عدد المختبرين اليدويين في الأسبوع الأخير قبل الإطلاق فقط",
                "إلغاء اختبارات الوحدة والمكونات والاعتماد على Beta testing وحده",
                "تأجيل فحص الأمان والأداء إلى مرحلة صيانة النظام بعد التسليم"
            ),
            exp = "Shift-left يعني تقديم أنشطة الاختبار والمراجعات والـ Test-first إلى أبكر وقت ممكن في SDLC.",
            qEn = "Which of the following practices BEST exemplifies the 'shift-left' approach in the SDLC?",
            correctEn = "Reviewing specifications early, writing tests before code, and running static analysis continuously",
            wrongEn = listOf(
                "Adding extra manual testers only during the final week before production launch",
                "Skipping unit and component tests to rely solely on external beta testing",
                "Postponing security and performance evaluations until post-release maintenance"
            ),
            expEn = "Shift-left moves testing earlier in the lifecycle via early reviews, test-first practices (TDD/ATDD), CI, and non-functional checks early."
        ),
        RawQ(
            id = "q063", ch = 2,
            q = "ما الهدف الرئيسي من عقد Retrospectives في نهاية كل تكرار (Iteration)؟",
            correct = "تقييم ما نجح وما تعثّر واستخلاص إجراءات لتحسين عمليات التطوير والاختبار",
            wrong = listOf(
                "محاسبة المختبرين على عدد العيوب التي وصلت إلى بيئة الاختبار",
                "استبدال جميع وثائق الاختبار الرسمية بمحاضر شفوية غير متابعة",
                "إعادة تنفيذ جميع اختبارات القبول السابقة يدوياً أمام العميل"
            ),
            exp = "تساعد الـ Retrospectives على التحسين المستمر لجودة العمليات والاختبارات وتقليل العيوب المتكررة.",
            qEn = "What is the primary purpose of holding retrospectives at the end of an iteration or project milestone?",
            correctEn = "Evaluating what worked well and what needs improvement to define actionable process improvements",
            wrongEn = listOf(
                "Assigning blame to testers for the number of defects detected during the sprint",
                "Replacing all documented test artifacts with informal verbal status updates",
                "Re-executing every prior acceptance test manually in front of the customer"
            ),
            expEn = "Section 2.1.6: Retrospectives help teams continuously improve test effectiveness, defect prevention, and collaboration."
        ),
        RawQ(
            id = "q064", ch = 2,
            q = "اختبار التفاعل بين نظام المتجر الداخلي وبوابة دفع خارجية تابعة لبنك (Stripe/Visa) يندرج تحت:",
            correct = "System integration testing",
            wrong = listOf("Component testing", "Component integration testing", "White-box unit testing"),
            exp = "التكامل بين النظام ككل وأنظمة أو خدمات خارجية مستقلّة هو System integration testing.",
            qEn = "Testing the interfaces and data exchange between an internal e-commerce system and an external bank payment gateway is an example of:",
            correctEn = "System integration testing",
            wrongEn = listOf("Component testing", "Component integration testing", "White-box unit testing"),
            expEn = "System integration testing focuses on interactions between the system under test and external systems or third-party services."
        ),
        RawQ(
            id = "q065", ch = 2,
            q = "تعديل دالة حساب الخصم تطلّب التأكد من أن شاشة الفاتورة وتصدير PDF لم يتأثرا. كيف يُصنَّف اختبار الشاشة والتصدير هنا؟",
            correct = "Change-related testing (Regression testing)",
            wrong = listOf("Non-functional portability testing", "Static informal review", "Acceptance contractual testing"),
            exp = "التحقق من عدم حدوث آثار جانبية في مناطق لم تتغير مباشرة بعد تعديل الكود هو Regression testing.",
            qEn = "After modifying a discount calculation function, a tester checks that invoice rendering and PDF export remain intact. What is this testing called?",
            correctEn = "Change-related testing (Regression testing)",
            wrongEn = listOf("Non-functional portability testing", "Static informal review", "Contractual acceptance testing"),
            expEn = "Regression testing is change-related testing aimed at confirming unchanged features still work after modifications."
        ),
        RawQ(
            id = "q102", ch = 2,
            q = "كيف يؤثر اختيار نموذج SDLC التسلسلي (مثل Waterfall) مقارنة بالنماذج التكرارية على أنشطة الاختبار؟",
            correct = "يبدأ الاختبار الديناميكي غالباً في مراحل متأخرة في النماذج التسلسلية بينما يتم باستمرار في التكرارية",
            wrong = listOf(
                "يلغي النموذج التسلسلي الحاجة إلى static testing ومراجعات الوثائق تماماً",
                "يمنع النموذج التكراري إجراء أي اختبارات نظام أو اختبارات قبول نهائية",
                "لا يتأثر توقيت الاختبار أو مستوى التوثيق بنوع نموذج دورة حياة التطوير"
            ),
            exp = "في النماذج التسلسلية يكون الاختبار الساكن مبكراً والديناميكي متأخراً، أما في التكرارية/الرشيقة فيختبر كل تكرار مبكراً وبوتيرة مستمرة.",
            qEn = "How does a sequential SDLC model (e.g., Waterfall) typically differ from an iterative model in terms of testing?",
            correctEn = "Dynamic testing typically occurs later in sequential models, whereas iterative models test working increments continuously",
            wrongEn = listOf(
                "Sequential models completely eliminate static testing and early document reviews",
                "Iterative models prohibit executing system-level or user acceptance testing",
                "Testing schedules and documentation rigor are unaffected by the chosen SDLC model"
            ),
            expEn = "Section 2.1.1: Sequential models defer dynamic testing until later phases; iterative/incremental models test small deliveries repeatedly."
        ),
        RawQ(
            id = "q103", ch = 2,
            q = "التحقق من سهولة نقل التطبيق للعمل من مزوّد سحابي (AWS) إلى مزوّد آخر (Azure) يُعد اختبار:",
            correct = "Portability testing (Non-functional)",
            wrong = listOf("Usability testing", "Functional suitability testing", "Confirmation testing"),
            exp = "Portability تقيس سهولة نقل النظام من بيئة أو بنية تحتية إلى أخرى، وهي ضمن Non-functional testing.",
            qEn = "Testing how easily an application can be transferred from one cloud infrastructure (AWS) to another (Azure) is an example of:",
            correctEn = "Portability testing (Non-functional)",
            wrongEn = listOf("Usability testing", "Functional suitability testing", "Confirmation testing"),
            expEn = "Portability (ISO 25010 quality characteristic) evaluates ease of transferring a system between hardware, software, or operational environments."
        ),
        RawQ(
            id = "q104", ch = 2,
            q = "اختبار التحقق من النسخ الاحتياطي والاستعادة من الكوارث وإجراءات الصيانة التشغيلية قبل التسليم هو:",
            correct = "Operational acceptance testing (OAT)",
            wrong = listOf("Alpha testing", "Component integration testing", "Exploratory usability testing"),
            exp = "OAT ضمن مستويات Acceptance testing يتحقق من جاهزية فرق التشغيل: Backup/restore, Disaster recovery, User management, Maintenance.",
            qEn = "Testing backup/restore procedures, disaster recovery, and system administration tasks before go-live is known as:",
            correctEn = "Operational acceptance testing (OAT)",
            wrongEn = listOf("Alpha testing", "Component integration testing", "Exploratory usability testing"),
            expEn = "Operational acceptance testing (Section 2.2.1) evaluates operational readiness by administrators/operators, including backup, failover, and security checks."
        ),
        RawQ(
            id = "q105", ch = 2,
            q = "أي عامل مما يلي يجعل إجراء Impact analysis في Maintenance testing صعباً ومكلفاً؟",
            correct = "غياب التوثيق المحدّث أو انعدام الـ Traceability بين المتطلبات والكود والاختبارات",
            wrong = listOf(
                "وجود اختبارات انحدار آلية مرتبطة بأسماء الميزات في مستودع العمل",
                "تنفيذ النظام على بيئة سحابية حديثة تدعم الحاويات",
                "إصدار النظام وفق جدول صيانة مخطط له مسبقاً"
            ),
            exp = "القسم 2.3: يصعب تحليل الأثر عند تقادم المواصفات أو فقدان الـ traceability أو عدم فهم بنية الكود القديم.",
            qEn = "Which of the following makes impact analysis especially difficult during maintenance testing?",
            correctEn = "Missing or outdated specifications and lack of traceability between requirements, code, and tests",
            wrongEn = listOf(
                "Having automated regression suites linked clearly to feature identifiers",
                "Running the operational system inside modern containerized cloud clusters",
                "Scheduling enhancements as part of a planned release-based maintenance cycle"
            ),
            expEn = "Section 2.3 explicitly warns that impact analysis is difficult when specifications are out of date or missing, or when traceability was not maintained."
        ),

        // ==================== CHAPTER 3 (14 Questions) ====================
        RawQ(
            id = "q019", ch = 3,
            q = "أي مما يلي يُعد من أنشطة Static testing؟",
            correct = "Code review",
            wrong = listOf("Load testing", "Smoke testing", "Beta testing"),
            exp = "Static testing لا ينفذ الكود، ويشمل Reviews وStatic analysis.",
            qEn = "Which of the following is an example of static testing?",
            correctEn = "Code review",
            wrongEn = listOf("Load testing", "Smoke testing", "Beta testing"),
            expEn = "Static testing (reviews and static analysis) examines work products without executing the software under test."
        ),
        RawQ(
            id = "q020", ch = 3,
            q = "أي عيب يسهل اكتشافه بـ Static analysis أكثر من Dynamic testing؟",
            correct = "كود لا يمكن الوصول إليه (Unreachable code) وانتهاك معايير الترميز",
            wrong = listOf(
                "بطء زمن استجابة قاعدة البيانات تحت ضغط ألف مستخدم متزامن",
                "تدهور الأداء التدريجي نتيجة تسرب الذاكرة بعد ساعات تشغيل طويلة",
                "أعطال المهلة الزمنية (Timeouts) عند التكامل الفعلي مع بوابة دفع"
            ),
            exp = "الأدوات الساكنة تحلل الكود بلا تنفيذ، فتكتشف الكود غير القابل للوصول بفعالية.",
            qEn = "Which defect type is typically MUCH easier to detect using static analysis than dynamic testing?",
            correctEn = "Unreachable (dead) code and coding standard violations",
            wrongEn = listOf(
                "Slow database response times under 1,000 concurrent users",
                "Gradual performance degradation due to memory leaks over long uptime",
                "Network timeout failures during live integration with a payment gateway"
            ),
            expEn = "Static analysis inspects code structure without running it, easily catching unreachable code, unused variables, and style violations."
        ),
        RawQ(
            id = "q021", ch = 3,
            q = "أي نوع مراجعة هو الأكثر رسمية؟",
            correct = "Inspection",
            wrong = listOf("Walkthrough", "Informal review", "Technical review"),
            exp = "Inspection لها أدوار محددة وقوائم فحص ومقاييس وعملية رسمية موثقة.",
            qEn = "Which type of review is the MOST formal and relies on defined roles, checklists, and metrics?",
            correctEn = "Inspection",
            wrongEn = listOf("Walkthrough", "Informal review", "Technical review"),
            expEn = "Inspection follows a strictly defined formal process with entry/exit criteria, checklists, designated roles (not led by author), and defect metrics."
        ),
        RawQ(
            id = "q022", ch = 3,
            q = "من المسؤول عن إدارة اجتماع المراجعة وتهيئة بيئة آمنة للنقاش؟",
            correct = "Moderator (Facilitator)",
            wrong = listOf("Author", "Scribe (Recorder)", "Manager"),
            exp = "الـ Facilitator يدير الاجتماع، والـ Scribe يوثق، والـ Author مؤلف المنتج المُراجَع.",
            qEn = "Which review role is responsible for running review meetings effectively and ensuring a safe space for discussion?",
            correctEn = "Moderator (Facilitator)",
            wrongEn = listOf("Author", "Scribe (Recorder)", "Manager"),
            expEn = "CTFL v4.0 Section 3.2.3: The moderator (facilitator) runs meetings, mediates discussions, manages time, and fosters a psychologically safe atmosphere."
        ),
        RawQ(
            id = "q023", ch = 3,
            q = "ما الترتيب الصحيح لأنشطة عملية المراجعة في CTFL v4.0؟",
            correct = "Planning, Review initiation, Individual review, Communication and analysis, Fixing and reporting",
            wrong = listOf(
                "Individual review, Planning, Review initiation, Communication and analysis, Fixing and reporting",
                "Review initiation, Planning, Communication and analysis, Individual review, Fixing and reporting",
                "Planning, Communication and analysis, Review initiation, Individual review, Fixing and reporting"
            ),
            exp = "هذا الترتيب الرسمي للأنشطة الخمسة في القسم 3.2.2 من منهج CTFL v4.0.",
            qEn = "What is the correct chronological sequence of the five activities in the review process (CTFL v4.0)?",
            correctEn = "Planning, Review initiation, Individual review, Communication and analysis, Fixing and reporting",
            wrongEn = listOf(
                "Individual review, Planning, Review initiation, Communication and analysis, Fixing and reporting",
                "Review initiation, Planning, Communication and analysis, Individual review, Fixing and reporting",
                "Planning, Communication and analysis, Review initiation, Individual review, Fixing and reporting"
            ),
            expEn = "Section 3.2.2 defines the 5 review process activities in order: Planning → Review initiation → Individual review → Communication and analysis → Fixing and reporting."
        ),
        RawQ(
            id = "q024", ch = 3,
            q = "ما الفارق الجوهري الصحيح بين Static وDynamic testing؟",
            correct = "Static يكشف الـ defects مباشرة في المنتج، بينما Dynamic يكشف الـ failures عند التنفيذ",
            wrong = listOf(
                "Static يكشف الـ failures في الإنتاج، بينما Dynamic يفحص الوثائق دون تشغيل",
                "كلاهما يتطلب بيئة تشغيل متكاملة لتنفيذ الشيفرة المصدرية وقياس المخرجات",
                "Dynamic لا يعتمد على test basis، بينما Static يقتصر على اختبار الأداء"
            ),
            exp = "التنفيذ هو الفارق: Dynamic يظهر failures أثناء التشغيل، وStatic يفحص work product مباشرة.",
            qEn = "Which statement BEST captures the difference between static testing and dynamic testing?",
            correctEn = "Static testing finds defects directly in work products without execution, while dynamic testing finds failures during execution",
            wrongEn = listOf(
                "Static testing detects runtime failures in production, while dynamic testing inspects documents offline",
                "Both static and dynamic testing require deploying executable binaries into a live test environment",
                "Dynamic testing requires no test basis, while static testing evaluates only runtime performance"
            ),
            expEn = "Dynamic testing executes code to trigger failures; static testing examines work products directly to find defects early."
        ),
        RawQ(
            id = "q066", ch = 3,
            q = "يقود المؤلف اجتماعاً لشرح تصميم معماري جديد أمام الفريق بهدف بناء فهم مشترك وتبادل الأفكار، دون اشتراط مقاييس رسمية. ما نوع المراجعة؟",
            correct = "Walkthrough",
            wrong = listOf("Inspection", "Informal review", "Technical review"),
            exp = "الـ Walkthrough يقودها الـ Author غالباً للتعليم وبناء الفهم المشترك وجمع ملاحظات أولية.",
            qEn = "The author leads a meeting to step through a new architectural design with colleagues to build shared understanding and gather ideas, without collecting formal metrics. Which review type is this?",
            correctEn = "Walkthrough",
            wrongEn = listOf("Inspection", "Informal review", "Technical review"),
            expEn = "Walkthroughs are typically led by the author to educate reviewers, gain shared understanding, evaluate quality, and generate ideas."
        ),
        RawQ(
            id = "q067", ch = 3,
            q = "أي تقنية مراجعة فردية يتبنى فيها المراجع منظور صاحب مصلحة (مثل مختبر أو مصمم) ويحاول عملياً اشتقاق المخرج التالي من الوثيقة كحالات الاختبار؟",
            correct = "Perspective-based reading",
            wrong = listOf("Ad hoc reviewing", "Checklist-based reviewing", "Role-based reviewing"),
            exp = "تتميز Perspective-based reading عن Role-based بمحاولة توليد المنتج اللاحق (مثل اشتقاق Test cases من المتطلبات) لكشف الفجوات.",
            qEn = "In which individual review technique do reviewers adopt specific stakeholder viewpoints and attempt to derive downstream work products (e.g., deriving test cases from requirements)?",
            correctEn = "Perspective-based reading",
            wrongEn = listOf("Ad hoc reviewing", "Checklist-based reviewing", "Role-based reviewing"),
            expEn = "Perspective-based reading goes beyond role-based reviewing by actively trying to produce the downstream work product from the reviewed document."
        ),
        RawQ(
            id = "q068", ch = 3,
            q = "أي عامل يُضعف فرص نجاح عملية المراجعة الرسمية في الفريق؟",
            correct = "تحويل المراجعة إلى وسيلة لتقييم أداء المؤلف وعقابه بدلاً من تحسين المنتج",
            wrong = listOf(
                "تقسيم الوثائق الكبيرة إلى أجزاء صغيرة سهلة التركيز",
                "تزويد المراجعين بقوائم فحص محددة ومحدثة للمخاطر",
                "منح المشاركين وقتاً كافياً للتحضير والمراجعة الفردية"
            ),
            exp = "البيئة الخالية من اللوم (Trust & non-judgmental culture) ركيزة أساسية لنجاح المراجعات.",
            qEn = "Which of the following factors is MOST likely to undermine the success of a review process?",
            correctEn = "Using review defect counts to evaluate and penalize the author's personal performance",
            wrongEn = listOf(
                "Splitting large work products into small, manageable chunks for focused reading",
                "Providing reviewers with tailored, risk-focused checklists during initiation",
                "Allocating sufficient scheduled time for participants to prepare individually"
            ),
            expEn = "Section 3.2.5: Reviews require an atmosphere of trust; the outcome must never be used to evaluate participants."
        ),
        RawQ(
            id = "q069", ch = 3,
            q = "أداة Static analysis نبّهت إلى متغير يُقرأ قبل تهيئته في أحد المسارات، لكن المراجع تأكد أن المسار مستحيل عملياً. كيف يُصنَّف هذا التنبيه؟",
            correct = "False positive",
            wrong = listOf("False negative", "System failure", "Escaped defect"),
            exp = "إبلاغ الأداة عن مشكلة غير حقيقية في السياق الفعلي يُسمى False positive.",
            qEn = "A static analysis tool flags an uninitialized variable warning along an execution path that a reviewer confirms can never actually occur. How is this warning classified?",
            correctEn = "False positive",
            wrongEn = listOf("False negative", "System failure", "Escaped defect"),
            expEn = "A false positive is a warning reported by a tool about an anomaly that is not an actual defect."
        ),
        RawQ(
            id = "q106", ch = 3,
            q = "أي عبارة حول نطاق Static testing هي الأدق وفق CTFL v4.0؟",
            correct = "يمكن إخضاع أي منتج عمل تقريباً للمراجعة الساكنة، بما فيها المتطلبات والاختبارات والكود والخطط",
            wrong = listOf(
                "يقتصر Static testing على ملفات الشيفرة المصدرية المكتوبة بلغات مُصرَّفة (Compiled)",
                "لا يمكن فحص خطط الاختبار أو قصص المستخدمين إلا عبر Dynamic execution",
                "المنتجات المكتوبة بلغة طبيعية لا تناسب المراجعات الساكنة بسبب غياب المترجم"
            ),
            exp = "القسم 3.1.1: يمكن فحص معظم work products بـ Static testing (مواصفات، قصص مستخدمين، خطط، حالات اختبار، كود، عقود).",
            qEn = "Which of the following statements about the scope of static testing in CTFL v4.0 is MOST accurate?",
            correctEn = "Almost any work product can be examined by static testing, including requirements, user stories, test plans, and code",
            wrongEn = listOf(
                "Static testing is restricted exclusively to compiled source code modules",
                "Test plans and user stories can only be verified through dynamic test execution",
                "Natural-language specifications cannot undergo static testing due to lack of a compiler"
            ),
            expEn = "Section 3.1.1 states that almost any work product (requirements, designs, code, testware, user stories, contracts) can be examined using reviews."
        ),
        RawQ(
            id = "q107", ch = 3,
            q = "من المسؤول عن اختيار فريق المراجعة وتحديد موعد ومكان المراجعة وضمان إتمامها بنجاح في القسم 3.2.3؟",
            correct = "Review leader",
            wrong = listOf("Scribe (Recorder)", "Author", "System administrator"),
            exp = "الـ Manager يقرر ما يُراجع ويوفر الموارد، والـ Review leader يتولى المسؤولية الشاملة عن تنظيم المراجعة وتشكيل الفريق.",
            qEn = "According to CTFL v4.0 Section 3.2.3, which role takes overall responsibility for the review, including selecting participants and organizing when and where it occurs?",
            correctEn = "Review leader",
            wrongEn = listOf("Scribe (Recorder)", "Author", "System administrator"),
            expEn = "The review leader takes overall responsibility for the review (choosing team members, scheduling time/place), whereas the manager decides what to review and allocates resources."
        ),
        RawQ(
            id = "q108", ch = 3,
            q = "في أي تقنية مراجعة يتتبع المراجعون المستند وفق سيناريوهات استخدام مهيكلة وتجارب جافة (Dry runs) بدلاً من القراءة العشوائية؟",
            correct = "Scenario-based reviewing",
            wrong = listOf("Ad hoc reviewing", "Checklist-based reviewing", "Exploratory execution"),
            exp = "القسم 3.2.4: Scenario-based reviewing تزوّد المراجعين بإرشادات مهيكلة لأداء dry runs حول كيفية عمل منتج العمل في سيناريوهات محددة.",
            qEn = "Which individual review technique provides reviewers with structured guidelines to perform 'dry runs' following expected usage paths?",
            correctEn = "Scenario-based reviewing",
            wrongEn = listOf("Ad hoc reviewing", "Checklist-based reviewing", "Exploratory execution"),
            expEn = "Scenario-based reviewing (and dry runs) guides reviewers using specific operational scenarios to find omissions and contradictions."
        ),
        RawQ(
            id = "q109", ch = 3,
            q = "ما الغرض الأساسي الذي يميز Technical review عن Informal review؟",
            correct = "الوصول إلى إجماع واتخاذ قرارات تقنية بواسطة مراجعين مؤهلين تقنياً",
            wrong = listOf(
                "تجنب وجود أي توثيق أو تحضير مسبق لتقليل الوقت إلى بضع دقائق",
                "تدريب المستخدمين النهائيين غير التقنيين على استخدام واجهة التطبيق",
                "تجاهل آراء الخبراء والاعتماد على مؤلف الوثيقة وحده في إغلاق النقاش"
            ),
            exp = "المراجعة التقنية يشارك فيها خبراء تقنيون (Peers) وميسّر لتحقيق التوافق واتخاذ قرارات حول مسائل تقنية واكتشاف العيوب.",
            qEn = "What is a primary distinguishing purpose of a 'technical review' compared to an informal review?",
            correctEn = "Gaining consensus and making technical decisions through evaluation by technically qualified peers",
            wrongEn = listOf(
                "Avoiding any individual preparation or written records to keep sessions under five minutes",
                "Training non-technical end users on how to navigate the released user interface",
                "Relying solely on the author to unilaterally resolve defects without peer input"
            ),
            expEn = "A technical review involves technically qualified reviewers and a moderator to evaluate quality, solve technical problems, and gain consensus."
        )
    )
}
