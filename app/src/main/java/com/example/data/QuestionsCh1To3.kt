package com.example.data

internal object QuestionsCh1To3 {
    val items: List<RawQ> = listOf(
        // ==================== CHAPTER 1 (20 Questions) ====================
        RawQ(
            id = "q001", ch = 1,
            q = "أي مبدأ يقول إن الاختبار الشامل لكل المدخلات والتركيبات مستحيل؟",
            correct = "Exhaustive testing is impossible",
            wrong = listOf("Defects cluster", "Absence-of-defects fallacy principle", "Tests wear out (Pesticide)"),
            exp = "المبدأ الثاني: لا يمكن اختبار كل التركيبات، لذلك نستخدم المخاطر والأولويات وتقنيات تصميم الاختبار.",
            qEn = "Which testing principle states that testing all combinations of inputs and preconditions is infeasible?",
            correctEn = "Exhaustive testing is impossible",
            wrongEn = listOf("Defects cluster together", "The absence-of-defects fallacy rule", "Tests wear out over repeated runs"),
            expEn = "Principle 2: Testing everything is impossible except in trivial cases; instead, risk analysis, prioritization, and test techniques are used."
        ),
        RawQ(
            id = "q002", ch = 1,
            q = "ما العلاقة الصحيحة بين Error وDefect وFailure؟",
            correct = "Error بشري يدخل Defect وقد يسبب Failure بالتشغيل",
            wrong = listOf(
                "Failure أثناء التشغيل يُنتج Defect في الكود يظهر كـ Error",
                "Defect في المتطلبات يسبب Error برمجياً لا يظهر أبداً",
                "Error في البيئة يؤدي إلى Failure يولّد Defect في التصميم"
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
            correct = "Testing يكشف الـ failures عند التنفيذ، بينما Debugging يحلل سبب الـ defect ويصلحه",
            wrong = listOf(
                "Testing يصلح الـ defects، وDebugging يثبت وجود failure للمستخدم",
                "Debugging ينفذ حالات الاختبار، وTesting يعدل الشيفرة المصدرية",
                "كلاهما نشاط تطويري لإعادة كتابة المتطلبات قبل بدء التصميم"
            ),
            exp = "Debugging نشاط تطويري لتحليل السبب وإصلاحه، ثم يأتي Confirmation testing للتحقق من الإصلاح.",
            qEn = "Which of the following BEST distinguishes testing from debugging?",
            correctEn = "Testing reveals runtime failures, whereas debugging locates and fixes the defect",
            wrongEn = listOf(
                "Testing fixes source code defects; debugging shows failures",
                "Debugging runs test scripts; testing modifies the program code",
                "Both are development activities aimed at rewriting specifications"
            ),
            expEn = "Dynamic testing shows failures; debugging is a development activity to locate and fix the defect, followed by confirmation testing."
        ),
        RawQ(
            id = "q004", ch = 1,
            q = "فريق يعيد تشغيل نفس مجموعة الاختبارات منذ سنوات ولم تعد تكشف defects جديدة. أي مبدأ ينطبق؟",
            correct = "Tests wear out",
            wrong = listOf("Early testing saves time and money", "Context dependent", "Exhaustive testing is impossible"),
            exp = "المبدأ الخامس (كان يُعرف سابقاً بـ Pesticide paradox): يجب مراجعة الاختبارات وتحديثها وإضافة حالات جديدة.",
            qEn = "A team has executed the same regression test suite unchanged for several releases and it no longer finds new defects. Which testing principle explains this?",
            correctEn = "Tests wear out",
            wrongEn = listOf("Early testing pays", "Testing is context dependent", "Exhaustive testing is impossible"),
            expEn = "Principle 5 (Tests wear out, formerly known as the pesticide paradox): existing tests lose effectiveness over time and must be updated or complemented with new tests."
        ),
        RawQ(
            id = "q005", ch = 1,
            q = "في أي من الحالات التالية يقع الفريق في فخ Absence-of-defects fallacy؟",
            correct = "نظام بلا defects لكنه لا يلبي حاجة المستخدم",
            wrong = listOf(
                "تتركز معظم الـ defects داخل وحدة برمجية واحدة",
                "لم تُنفَّذ اختبارات الكود الأبيض رغم تغطية حالات الاستخدام المحددة",
                "تأخر إطلاق النظام بسبب إعادة تشغيل نفس الاختبارات دون أي تحديث"
            ),
            exp = "إصلاح كل الـ defects لا يضمن نجاح النظام إن لم يحقق حاجة المستخدم (Validation).",
            qEn = "Which of the following situations illustrates the 'absence-of-defects fallacy'?",
            correctEn = "A defect-free system fails to meet user needs",
            wrongEn = listOf(
                "Defects cluster heavily inside a single module",
                "White-box coverage was omitted despite executing all use cases",
                "Release slipped after running an unmaintained automation suite"
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
                "تقليص زمن التنفيذ عبر توليد بيانات آلياً",
                "الاستغناء عن test plan في المشاريع الرشيقة",
                "ضمان خلو النظام من defects في الإنتاج"
            ),
            exp = "Traceability تدعم تحليل الأثر وتدقيق التغطية وإعداد تقارير الحالة.",
            qEn = "Which of the following is a primary benefit of maintaining traceability between the test basis and test cases?",
            correctEn = "Supporting impact analysis of changes and evaluating requirement coverage",
            wrongEn = listOf(
                "Generating synthetic test data automatically",
                "Eliminating test plans and completion reports",
                "Guaranteeing zero defects in live production"
            ),
            expEn = "Bidirectional traceability facilitates impact analysis, coverage assessment, audits, and clear status communication."
        ),
        RawQ(
            id = "q008", ch = 1,
            q = "أي مما يلي من Testware المنتَجة أثناء Test implementation؟",
            correct = "Test procedures و test suites",
            wrong = listOf("Test completion report و lessons learned", "Defect reports", "Test plan"),
            exp = "Test implementation تعد الـ procedures والـ suites وجدول التنفيذ وبيانات وبيئة الاختبار.",
            qEn = "Which of the following testware items are typically produced during the 'test implementation' activity?",
            correctEn = "Test procedures and test suites",
            wrongEn = listOf("Completion report and lessons learned", "Defect reports", "Project risk log"),
            expEn = "Test implementation creates or finalizes test procedures, automated scripts, test suites, test data, the execution schedule, and the test environment."
        ),
        RawQ(
            id = "q009", ch = 1,
            q = "ما الميزة الرئيسية لاستقلالية الاختبار (Independence of testing)؟",
            correct = "تقليل التحيز وكشف عيوب يغفل عنها المؤلف",
            wrong = listOf(
                "ضمان فهم مشترك للمتطلبات تلقائياً بالمشروع",
                "خفض تكلفة المشروع الإجمالية وإلغاء مراجعات الكود بين الزملاء",
                "تسريع إصلاح الأخطاء عبر نقل مسؤولية الجودة لفريق الاختبار"
            ),
            exp = "Independence يقلل التحيز المعرفي، لكنه قد يسبب عزلة أو فجوة تواصل إن لم يُدر جيداً.",
            qEn = "Which of the following is a key advantage of independence in testing?",
            correctEn = "Reducing author bias to uncover new failures",
            wrongEn = listOf(
                "Guaranteeing shared understanding effortlessly",
                "Reducing total project cost by eliminating all peer code reviews",
                "Accelerating defect resolution by transferring quality ownership solely to testers"
            ),
            expEn = "Independent testers bring different assumptions and avoid confirmation bias, though high independence can risk isolation if communication is poor."
        ),
        RawQ(
            id = "q010", ch = 1,
            q = "أي عبارة تصف العلاقة بين QA وQC بدقة؟",
            correct = "QA نهج وقائي موجه للعملية، بينما الاختبار من أنشطة QC الموجهة للمنتج",
            wrong = listOf(
                "QA مرادف للاختبار، وQC يقتصر على كتابة الخطط",
                "QC موجه للعمليات دون فحص المنتج البرمجي",
                "QA يبدأ بعد التسليم، وQC أثناء التحليل فقط"
            ),
            exp = "QA يمنع الـ defects بتحسين العمليات، والاختبار من أنشطة Quality Control لاكتشاف العيوب في المنتج.",
            qEn = "Which statement MOST accurately describes the relationship between Quality Assurance (QA) and Quality Control (QC)?",
            correctEn = "QA is process-oriented and preventive, whereas testing is product-oriented QC",
            wrongEn = listOf(
                "QA means dynamic testing; QC means policy writing",
                "QC audits processes without running software",
                "QA begins after release; QC runs during analysis"
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
            correct = "نقل المعرفة والتعاون مع الفريق للجودة",
            wrong = listOf(
                "تحمّل الجودة وحدهم ومنع اختبارات الوحدة",
                "العمل في عزلة تامة وعدم حضور اجتماعات التخطيط اليومية",
                "تأجيل جميع أنشطة الاختبار حتى استلام النظام مكتملاً"
            ),
            exp = "Whole-team approach يجعل الجودة مسؤولية مشتركة حيث يوجّه المختبرون الفريق ويدعمون بناء الاختبارات.",
            qEn = "Under the whole-team approach in CTFL v4.0, what is a key responsibility of testers?",
            correctEn = "Sharing test skills so all own quality",
            wrongEn = listOf(
                "Owning product quality entirely alone",
                "Working in strict isolation and skipping daily refinement sessions",
                "Postponing all test activities until final integrated delivery"
            ),
            expEn = "In the whole-team approach, quality is a shared team responsibility; testers coach teammates and collaborate on test strategies."
        ),
        RawQ(
            id = "q060", ch = 1,
            q = "أثناء تحليل المواصفات، استخلص المختبر جملة «يجب أن يرفض النظام كلمات المرور الأقل من 8 أحرف» كهدف قابل للاختبار. ماذا يُسمى هذا الناتج؟",
            correct = "Test condition",
            wrong = listOf("Execution log", "Test completion report", "Defect summary report"),
            exp = "في Test analysis نشتق الـ Test conditions (ماذا نختبر) من الـ Test basis.",
            qEn = "While analyzing a specification, a tester derives the testable aspect 'System must reject passwords shorter than 8 characters'. What is this work product called?",
            correctEn = "Test condition",
            wrongEn = listOf("Execution log", "Test completion report", "Defect summary report"),
            expEn = "A test condition is a testable aspect of a component or system derived from the test basis during test analysis."
        ),
        RawQ(
            id = "q061", ch = 1,
            q = "تغيير عملة الدفع دون تحديث واجهة حساب الضريبة تسبب في فشل الحسابات. أي مهارة اختبارية تساعد المختبر على الربط الفعال مع المطورين لتوضيح هذا الخلل؟",
            correct = "التواصل البناء والتفكير النقدي الموضوعي",
            wrong = listOf(
                "تجنب التواصل ورفض النسخة مباشرة دون شرح",
                "حفظ أسماء خوارزميات الترتيب دون فهم منطق الأعمال المالي",
                "كتابة تقارير العيوب بأسلوب حاد لإجبار المطور على رفع الأولوية"
            ),
            exp = "منهج CTFL v4.0 يؤكد أهمية المهارات الشخصية مثل التواصل الموضوعي والتفكير النقدي والفضول.",
            qEn = "Which generic tester skill MOST helps a tester communicate defect findings constructively without creating friction with developers?",
            correctEn = "Constructive talk and objective reporting",
            wrongEn = listOf(
                "Avoiding dialogue and rejecting all builds",
                "Memorizing sorting algorithms without understanding business logic",
                "Writing confrontational reports to force higher priority fixes"
            ),
            expEn = "CTFL v4.0 Section 1.5.1 highlights communication skills, critical thinking, curiosity, attention to detail, and domain knowledge."
        ),
        RawQ(
            id = "q096", ch = 1,
            q = "أي مما يلي لا يُعد من أهداف الاختبار النموذجية في منهج CTFL v4.0؟",
            correct = "تصحيح الشيفرة المصدرية مباشرة وإصدار التصحيحات للمستخدمين",
            wrong = listOf(
                "تقييم المتطلبات وقصص المستخدمين والتصميم",
                "التحقق من استيفاء المتطلبات المحددة",
                "بناء الثقة في جودة كائن الاختبار"
            ),
            exp = "الاختبار يقيّم الجودة ويكشف العيوب ويوفر المعلومات، أما إصلاح الشيفرة فهو مهمة Debugging التطويرية.",
            qEn = "Which of the following is NOT a typical objective of testing according to CTFL v4.0?",
            correctEn = "Directly modifying source code to fix defects and deploy patches",
            wrongEn = listOf(
                "Evaluating work products like user stories",
                "Verifying whether specified requirements are met",
                "Building confidence in software quality"
            ),
            expEn = "Fixing source code is debugging (a development activity), whereas testing evaluates work products, finds defects, verifies requirements, and builds confidence."
        ),
        RawQ(
            id = "q097", ch = 1,
            q = "أي من المهام التالية تقع عادةً ضمن مسؤولية دور إدارة الاختبار (Test management role) في CTFL v4.0؟",
            correct = "تخطيط الاختبار ومراقبته وإعداد تقاريره",
            wrong = listOf(
                "تصميم حالات الاختبار وتنفيذها وتقييم نتائجها",
                "كتابة اختبارات الوحدة آلياً وإصلاح الأخطاء في الكود",
                "صياغة قصص المستخدمين وإقرار الميزانية للمنظمة"
            ),
            exp = "القسم 1.4.5: دور إدارة الاختبار يركز على Planning, Monitoring, Control, Completion، بينما Testing role يركز على التحليل والتصميم والتنفيذ.",
            qEn = "Which set of responsibilities typically belongs to the 'test management role' in CTFL v4.0?",
            correctEn = "Planning, monitoring, control, and completion",
            wrongEn = listOf(
                "Detailed test case design, data, and execution",
                "Writing component unit tests and debugging production bugs",
                "Authoring business user stories and approving org budgets"
            ),
            expEn = "CTFL v4.0 Section 1.4.5 splits responsibilities into the test management role (planning, monitoring, control, completion) and the testing role (analysis, design, implementation, execution)."
        ),
        RawQ(
            id = "q098", ch = 1,
            q = "أي مثال مما يلي يوضح مبدأ «الاختبار يعتمد على السياق» (Testing is context dependent) بأدق صورة؟",
            correct = "تطبيق صرامة أكبر للبرامج الطبية مقارنة بموقع ترويجي بسيط",
            wrong = listOf(
                "تركّز 80% من عيوب تطبيق بنكي داخل وحدة حساب الفوائد المصرفية المعقدة",
                "تراجع فاعلية مجموعة الانحدار عند إعادة تشغيلها باستمرار دون أي تحديث",
                "اكتشاف عيب تصميمي أثناء مراجعة المواصفات المبكرة قبل بدء البرمجة"
            ),
            exp = "المبدأ السادس: يختلف أسلوب الاختبار وتقنياته ومستوى صرامته باختلاف سياق النظام ومجاله ومخاطره.",
            qEn = "Which of the following examples BEST illustrates the principle 'Testing is context dependent'?",
            correctEn = "Medical devices undergo stricter testing than a simple promo site",
            wrongEn = listOf(
                "80% of banking defects clustered inside a single interest module",
                "Re-running an unchanged regression suite stopped finding new defects",
                "A design flaw was detected in a requirements review before coding"
            ),
            expEn = "Principle 6: Testing cannot be applied identically everywhere; methodology, rigor, and techniques depend on domain, risk, and SDLC context."
        ),
        RawQ(
            id = "q099", ch = 1,
            q = "أي مما يلي يمكن أن يسبب Failure في نظام برمجي دون أن يكون ناجماً عن Error بشري في كتابة الشيفرة؟",
            correct = "الإشعاع أو التداخل المغناطيسي أو التخريب",
            wrong = listOf(
                "تخطي إعداد خطة الاختبار الرئيسية بالمشروع",
                "انخفاض عدد حالات الاختبار المنفذة يومياً في بيئة الجودة",
                "تعيين مراجع تقني خارجي لمراجعة وثيقة المتطلبات النهائية"
            ),
            exp = "القسم 1.2.3: قد تنشأ الـ Failures أيضاً عن ظروف بيئية مادية (حرارة، إشعاع، مجالات مغناطيسية) أو أفعال متعمدة دون خطأ برمجي.",
            qEn = "According to CTFL v4.0, which of the following can cause a system failure WITHOUT being triggered by a human coding error?",
            correctEn = "Radiation, magnetic fields, or malice",
            wrongEn = listOf(
                "Omitting the project's master test plan",
                "Executing fewer regression tests in staging",
                "Assigning an external reviewer for requirements"
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
            correct = "في الأنظمة عالية الخطورة التي تتطلب استقلالية صارمة للتحقق",
            wrong = listOf(
                "في الفرق الرشيقة الصغيرة التي تتواصل يومياً",
                "عندما يشارك المختبرون في معايير القبول",
                "عندما يتعاون الفريق في اختبارات الـ API"
            ),
            exp = "القسم 1.5.2: رغم مزايا Whole-team، فإن البيئات التنظيمية الحساسة للسلامة قد تتطلب مستوى أعلى من استقلالية الاختبار.",
            qEn = "In which situation might the 'whole-team approach' be less suitable on its own and require higher test independence?",
            correctEn = "Safety-critical projects mandating strict independent verification",
            wrongEn = listOf(
                "Small co-located Agile teams collaborating",
                "Testers helping refine acceptance criteria early",
                "Developers and testers pairing on API checks"
            ),
            expEn = "Section 1.5.2 notes that while whole-team dynamics improve collaboration, high-risk or regulated domains often require stronger test independence."
        ),

        // ==================== CHAPTER 2 (16 Questions) ====================
        RawQ(
            id = "q011", ch = 2,
            q = "أي مما يلي يُعد مستوى اختبار (Test level)؟",
            correct = "Component integration testing",
            wrong = listOf("Smoke testing", "Regression suite execution", "لا شيء من الخيارات المذكورة"),
            exp = "المستويات الخمسة: Component, Component integration, System, System integration, Acceptance.",
            qEn = "Which of the following is classified as a 'test level' in CTFL v4.0?",
            correctEn = "Component integration testing",
            wrongEn = listOf("Smoke testing", "Automated regression test execution", "None of the listed options"),
            expEn = "The five test levels are component, component integration, system, system integration, and acceptance testing."
        ),
        RawQ(
            id = "q012", ch = 2,
            q = "بعد إصلاح defect، الاختبار الذي يتأكد أن نفس الحالة لم تعد تفشل هو:",
            correct = "Confirmation testing",
            wrong = listOf("Smoke testing", "Regression test suite execution", "Non-functional load test"),
            exp = "Confirmation (Re-testing) يعيد نفس الحالة الفاشلة. أما Regression فيتحقق من عدم كسر وظائف أخرى.",
            qEn = "After a developer fixes a reported defect, which type of testing verifies that the original failing test case now passes?",
            correctEn = "Confirmation testing",
            wrongEn = listOf("Smoke testing", "Build verification smoke suite", "Performance load testing"),
            expEn = "Confirmation testing (re-testing) confirms that a specific defect has been fixed; regression testing checks for unintended side effects elsewhere."
        ),
        RawQ(
            id = "q013", ch = 2,
            q = "ما الهدف الأساسي من Regression testing؟",
            correct = "اكتشاف آثار جانبية غير مقصودة في أجزاء أخرى بعد التغيير",
            wrong = listOf(
                "قياس زمن استجابة الخادم تحت الضغط",
                "مراجعة المتطلبات قبل بدء البرمجة",
                "التحقق من إصلاح defect واحد فقط"
            ),
            exp = "يتأكد أن التعديل أو الإصلاح لم يكسر ما كان يعمل مسبقاً، وهو مرشح قوي للأتمتة.",
            qEn = "What is the primary objective of regression testing?",
            correctEn = "Detecting unintended side effects in unchanged parts after changes",
            wrongEn = listOf(
                "Measuring server latency under peak load",
                "Reviewing requirements before coding starts",
                "Checking only one fixed defect in isolation"
            ),
            expEn = "Regression testing confirms that modifications (fixes, features, environment updates) have not broken existing working functionality."
        ),
        RawQ(
            id = "q014", ch = 2,
            q = "أي مما يلي مثال على اختبار غير وظيفي (Non-functional testing)؟",
            correct = "Performance efficiency testing",
            wrong = listOf("فحص تسجيل الدخول الأساسي للمستخدم", "إضافة منتج إلى سلة التسوق الخاصة بك", "إتمام مسار إعادة تعيين كلمة المرور"),
            exp = "الأداء والأمان والاستخدامية والتوافق والموثوقية اختبارات غير وظيفية.",
            qEn = "Which of the following is an example of non-functional testing?",
            correctEn = "Performance efficiency testing",
            wrongEn = listOf("Login flow", "Adding an item to the shopping cart", "Completing the password reset workflow"),
            expEn = "Non-functional testing evaluates quality characteristics such as performance efficiency, security, usability, compatibility, and reliability."
        ),
        RawQ(
            id = "q015", ch = 2,
            q = "اختبار قبول يجريه مستخدمون حقيقيون في بيئتهم الفعلية قبل الإصدار التجاري يسمى:",
            correct = "Beta testing",
            wrong = listOf("Alpha test", "Component testing", "Contractual acceptance testing"),
            exp = "Alpha في موقع المنظمة المطورة، وBeta عند مستخدمين فعليين في بيئاتهم الحقيقية.",
            qEn = "Acceptance testing performed by external potential or existing users at their own locations before commercial release is called:",
            correctEn = "Beta testing",
            wrongEn = listOf("Alpha", "Component testing", "Contractual acceptance testing"),
            expEn = "Alpha testing takes place at the developing organization's site; beta testing takes place at the customers' own operational locations."
        ),
        RawQ(
            id = "q016", ch = 2,
            q = "ما هي محفزات Maintenance testing الثلاثة في المنهج؟",
            correct = "Modification و Migration و Retirement",
            wrong = listOf("Planning و Design و Execution الكاملة", "Review و Inspection و Walkthrough", "Confirmation و Regression و Smoke"),
            exp = "Maintenance testing يُطلق بعد التعديل أو الترحيل أو إيقاف النظام عن الخدمة.",
            qEn = "Which three triggers initiate maintenance testing on an operational system?",
            correctEn = "Modification, migration, and retirement",
            wrongEn = listOf("Plan, design, run", "Review, inspection, and walkthrough", "Confirmation, regression, and smoke testing"),
            expEn = "CTFL v4.0 Section 2.3 defines three maintenance triggers: modifications (enhancements/fixes), migrations (platform shifts), and retirement (decommissioning)."
        ),
        RawQ(
            id = "q017", ch = 2,
            q = "كيف يساعد Impact analysis في تخطيط Maintenance testing؟",
            correct = "تحديد الأجزاء المتأثرة لتقدير إعادة الاختبار",
            wrong = listOf(
                "قياس سرعة كتابة الشيفرة البرمجية في المشروع",
                "حذف حالات الاختبار القديمة واستبدالها يدوياً",
                "تقليص حجم قاعدة البيانات قبل ترحيل البيانات"
            ),
            exp = "تحليل الأثر يحدد ما يجب إعادة اختباره (regression) بحسب المخاطر وحجم التغيير.",
            qEn = "How does impact analysis assist in planning maintenance testing?",
            correctEn = "Identifying impacted areas to scope regression",
            wrongEn = listOf(
                "Measuring developer feature coding velocity now",
                "Deleting legacy suites to convert them manually",
                "Compressing live databases prior to migration"
            ),
            expEn = "Impact analysis evaluates which parts of an existing system may be impacted by a change so regression testing can be accurately scoped."
        ),
        RawQ(
            id = "q018", ch = 2,
            q = "ما الذي يميز نهج Test-Driven Development (TDD)؟",
            correct = "كتابة اختبار وحدة فاشل قبل كتابة الكود",
            wrong = listOf(
                "كتابة الكود الكامل ثم الاختبار بعده لاحقاً",
                "الاعتماد على الاستكشاف اليدوي بدلاً من الوحدة",
                "تأجيل تصميم الاختبارات لما بعد تكامل الأنظمة"
            ),
            exp = "TDD نهج Test-first على مستوى المكونات يتبعه Refactoring، بينما ATDD يشتق الاختبارات من Acceptance criteria.",
            qEn = "Which statement BEST characterizes Test-Driven Development (TDD)?",
            correctEn = "Writing a failing unit test before code",
            wrongEn = listOf(
                "Coding everything first, testing post-launch",
                "Replacing unit tests with manual exploration",
                "Deferring test design until system integration"
            ),
            expEn = "TDD is a test-first approach (Red → Green → Refactor) at the component level, whereas ATDD derives tests from acceptance criteria."
        ),
        RawQ(
            id = "q062", ch = 2,
            q = "أي ممارسة تجسّد مفهوم Shift-left في دورة حياة تطوير البرمجيات؟",
            correct = "مراجعة المتطلبات مبكراً والاختبار قبل الكود",
            wrong = listOf(
                "زيادة المختبرين في الأسبوع الأخير قبل النشر",
                "إلغاء اختبارات الوحدة والاعتماد على Beta فقط",
                "تأجيل فحص الأمان والأداء لما بعد التسليم"
            ),
            exp = "Shift-left يعني تقديم أنشطة الاختبار والمراجعات والـ Test-first إلى أبكر وقت ممكن في SDLC.",
            qEn = "Which of the following practices BEST exemplifies the 'shift-left' approach in the SDLC?",
            correctEn = "Reviewing early and writing tests before code",
            wrongEn = listOf(
                "Adding extra manual QA staff right before launch",
                "Skipping unit tests to rely solely on external beta",
                "Postponing security checks until post-release upkeep"
            ),
            expEn = "Shift-left moves testing earlier in the lifecycle via early reviews, test-first practices (TDD/ATDD), CI, and non-functional checks early."
        ),
        RawQ(
            id = "q063", ch = 2,
            q = "ما الهدف الرئيسي من عقد Retrospectives في نهاية كل تكرار (Iteration)؟",
            correct = "تقييم ما نجح وما تعثّر واستخلاص إجراءات لتحسين العملية",
            wrong = listOf(
                "تحديد أولويات العيوب داخل التكرار",
                "استبدال الوثائق بمحاضر شفوية",
                "إعادة تنفيذ اختبارات القبول يدوياً"
            ),
            exp = "تساعد الـ Retrospectives على التحسين المستمر لجودة العمليات والاختبارات وتقليل العيوب المتكررة.",
            qEn = "What is the primary purpose of holding retrospectives at the end of an iteration or project milestone?",
            correctEn = "Evaluating lessons learned to define actionable process improvements",
            wrongEn = listOf(
                "Triaging and prioritizing sprint defects",
                "Replacing artifacts with verbal updates",
                "Re-running acceptance tests manually"
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
            correct = "Change-related (Regression)",
            wrong = listOf("Portability test", "Static informal document review", "Contractual acceptance validation"),
            exp = "التحقق من عدم حدوث آثار جانبية في مناطق لم تتغير مباشرة بعد تعديل الكود هو Regression testing.",
            qEn = "After modifying a discount calculation function, a tester checks that invoice rendering and PDF export remain intact. What is this testing called?",
            correctEn = "Regression testing",
            wrongEn = listOf("Portability", "Static informal document review", "Contractual acceptance testing"),
            expEn = "Regression testing is change-related testing aimed at confirming unchanged features still work after modifications."
        ),
        RawQ(
            id = "q102", ch = 2,
            q = "كيف يؤثر اختيار نموذج SDLC التسلسلي (مثل Waterfall) مقارنة بالنماذج التكرارية على أنشطة الاختبار؟",
            correct = "يتأخر Dynamic testing في التسلسلي ويتكرر بالرشيق",
            wrong = listOf(
                "يلغي النموذج التسلسلي الحاجة للمراجعات الساكنة المبكرة",
                "يمنع النموذج التكراري إجراء أي اختبارات نظام أو قبول",
                "لا يتأثر توقيت الاختبار مطلقاً بنوع نموذج دورة الحياة"
            ),
            exp = "في النماذج التسلسلية يكون الاختبار الساكن مبكراً والديناميكي متأخراً، أما في التكرارية/الرشيقة فيختبر كل تكرار مبكراً وبوتيرة مستمرة.",
            qEn = "How does a sequential SDLC model (e.g., Waterfall) typically differ from an iterative model in terms of testing?",
            correctEn = "Sequential runs dynamic tests late vs. each iteration",
            wrongEn = listOf(
                "Sequential workflows eliminate early static code checks",
                "Iterative models prohibit running system or UAT cycles",
                "Testing schedules are completely unaffected by the SDLC"
            ),
            expEn = "Section 2.1.1: Sequential models defer dynamic testing until later phases; iterative/incremental models test small deliveries repeatedly."
        ),
        RawQ(
            id = "q103", ch = 2,
            q = "التحقق من سهولة نقل التطبيق للعمل من مزوّد سحابي (AWS) إلى مزوّد آخر (Azure) يُعد اختبار:",
            correct = "Portability testing",
            wrong = listOf("Usability test", "Functional suitability check", "Confirmation regression test"),
            exp = "Portability تقيس سهولة نقل النظام من بيئة أو بنية تحتية إلى أخرى، وهي ضمن Non-functional testing.",
            qEn = "Testing how easily an application can be transferred from one cloud infrastructure (AWS) to another (Azure) is an example of:",
            correctEn = "Portability testing",
            wrongEn = listOf("Usability test", "Functional correctness test", "Confirmation defect re-test"),
            expEn = "Portability (ISO 25010 quality characteristic) evaluates ease of transferring a system between hardware, software, or operational environments."
        ),
        RawQ(
            id = "q104", ch = 2,
            q = "اختبار التحقق من النسخ الاحتياطي والاستعادة من الكوارث وإجراءات الصيانة التشغيلية قبل التسليم هو:",
            correct = "Operational acceptance testing",
            wrong = listOf("Beta field trial", "Component integration check", "Exploratory usability testing"),
            exp = "OAT ضمن مستويات Acceptance testing يتحقق من جاهزية فرق التشغيل: Backup/restore, Disaster recovery, User management, Maintenance.",
            qEn = "Testing backup/restore procedures, disaster recovery, and system administration tasks before go-live is known as:",
            correctEn = "Operational acceptance testing",
            wrongEn = listOf("Alpha testing", "Component integration testing", "Exploratory usability checking"),
            expEn = "Operational acceptance testing (Section 2.2.1) evaluates operational readiness by administrators/operators, including backup, failover, and security checks."
        ),
        RawQ(
            id = "q105", ch = 2,
            q = "أي عامل مما يلي يجعل إجراء Impact analysis في Maintenance testing صعباً ومكلفاً؟",
            correct = "تقادم المواصفات أو غياب الـ Traceability",
            wrong = listOf(
                "ربط اختبارات الانحدار الآلية بمتطلبات النظام",
                "تشغيل النظام على سحابة حديثة تدعم الحاويات والعزل",
                "إصدار النظام وفق جدول صيانة مخطط له مع التشغيل"
            ),
            exp = "القسم 2.3: يصعب تحليل الأثر عند تقادم المواصفات أو فقدان الـ traceability أو عدم فهم بنية الكود القديم.",
            qEn = "Which of the following makes impact analysis especially difficult during maintenance testing?",
            correctEn = "Outdated specs or missing traceability",
            wrongEn = listOf(
                "Automated regression suites tied to specs",
                "Running the live system in modern cloud setups",
                "Scheduling updates inside planned maintenance"
            ),
            expEn = "Section 2.3 explicitly warns that impact analysis is difficult when specifications are out of date or missing, or when traceability was not maintained."
        ),

        // ==================== CHAPTER 3 (14 Questions) ====================
        RawQ(
            id = "q019", ch = 3,
            q = "أي مما يلي يُعد من أنشطة Static testing؟",
            correct = "Code review",
            wrong = listOf("Load test", "Smoke testing", "Beta field test"),
            exp = "Static testing لا ينفذ الكود، ويشمل Reviews وStatic analysis.",
            qEn = "Which of the following is an example of static testing?",
            correctEn = "Code review",
            wrongEn = listOf("Load test", "Smoke testing", "Beta testing"),
            expEn = "Static testing (reviews and static analysis) examines work products without executing the software under test."
        ),
        RawQ(
            id = "q020", ch = 3,
            q = "أي عيب يسهل اكتشافه بـ Static analysis أكثر من Dynamic testing؟",
            correct = "كود ميت (Unreachable) ومخالفات الترميز",
            wrong = listOf(
                "بطء قاعدة البيانات تحت أحمال متزايدة",
                "تدهور الأداء نتيجة تسرب الذاكرة الطويل",
                "أعطال المهلة الزمنية مع بوابة الدفع"
            ),
            exp = "الأدوات الساكنة تحلل الكود بلا تنفيذ، فتكتشف الكود غير القابل للوصول بفعالية.",
            qEn = "Which defect type is typically MUCH easier to detect using static analysis than dynamic testing?",
            correctEn = "Unreachable (dead) code and coding standard violations",
            wrongEn = listOf(
                "Slow queries under load",
                "Gradual performance degradation due to memory leaks over long uptime",
                "Network timeout failures during live integration with a payment gateway"
            ),
            expEn = "Static analysis inspects code structure without running it, easily catching unreachable code, unused variables, and style violations."
        ),
        RawQ(
            id = "q021", ch = 3,
            q = "أي نوع مراجعة هو الأكثر رسمية؟",
            correct = "Inspection",
            wrong = listOf("Walkthrough", "Peer check", "Technical review"),
            exp = "Inspection لها أدوار محددة وقوائم فحص ومقاييس وعملية رسمية موثقة.",
            qEn = "Which type of review is the MOST formal and relies on defined roles, checklists, and metrics?",
            correctEn = "Inspection",
            wrongEn = listOf("Walkthrough", "Peer check", "Technical review"),
            expEn = "Inspection follows a strictly defined formal process with entry/exit criteria, checklists, designated roles (not led by author), and defect metrics."
        ),
        RawQ(
            id = "q022", ch = 3,
            q = "من المسؤول عن إدارة اجتماع المراجعة وتهيئة بيئة آمنة للنقاش؟",
            correct = "Moderator (Facilitator)",
            wrong = listOf("Author", "Scribe", "Senior Resource Manager"),
            exp = "الـ Facilitator يدير الاجتماع، والـ Scribe يوثق، والـ Author مؤلف المنتج المُراجَع.",
            qEn = "Which review role is responsible for running review meetings effectively and ensuring a safe space for discussion?",
            correctEn = "Moderator (Facilitator)",
            wrongEn = listOf("Author", "Scribe", "Senior Project Manager"),
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
            correct = "Static يكشف الـ defects مباشرة؛ Dynamic يكشف الـ failures",
            wrong = listOf(
                "Static يكشف أعطال الإنتاج فقط",
                "كلاهما يتطلب بيئة تشغيل متكاملة لتنفيذ الشيفرة المصدرية وقياس المخرجات",
                "Dynamic لا يعتمد على test basis؛ بينما Static يقتصر على اختبار الأداء"
            ),
            exp = "التنفيذ هو الفارق: Dynamic يظهر failures أثناء التشغيل، وStatic يفحص work product مباشرة.",
            qEn = "Which statement BEST captures the difference between static testing and dynamic testing?",
            correctEn = "Static finds defects directly; dynamic triggers runtime failures",
            wrongEn = listOf(
                "Static finds crashes; dynamic reads docs",
                "Both approaches require deploying compiled binaries into a live environment",
                "Dynamic testing needs no test basis; static testing evaluates response speed"
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
            correct = "استخدام نتائج المراجعة لمعاقبة المؤلف",
            wrong = listOf(
                "تقسيم الوثائق الكبيرة لقطع صغيرة للمراجعة",
                "تزويد المراجعين بقوائم فحص محددة للمخاطر",
                "منح المشاركين وقتاً كافياً للتحضير المسبق"
            ),
            exp = "البيئة الخالية من اللوم (Trust & non-judgmental culture) ركيزة أساسية لنجاح المراجعات.",
            qEn = "Which of the following factors is MOST likely to undermine the success of a review process?",
            correctEn = "Using defect counts to judge the author",
            wrongEn = listOf(
                "Splitting large documents into smaller parts",
                "Providing reviewers with tailored risk checklists",
                "Allocating sufficient time for review preparation"
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
            correct = "يفحص معظم work products كالمتطلبات والكود",
            wrong = listOf(
                "يقتصر الاختبار الساكن على الشيفرة المصرَّفة فقط",
                "لا تُفحص خطط الاختبار أو القصص إلا عبر التشغيل",
                "المواصفات المكتوبة بلغة طبيعية لا تناسب المراجعة"
            ),
            exp = "القسم 3.1.1: يمكن فحص معظم work products بـ Static testing (مواصفات، قصص مستخدمين، خطط، حالات اختبار، كود، عقود).",
            qEn = "Which of the following statements about the scope of static testing in CTFL v4.0 is MOST accurate?",
            correctEn = "Checks nearly any artifact from code to plans",
            wrongEn = listOf(
                "Static testing examines only compiled binaries",
                "Test plans and stories need dynamic execution",
                "Natural-language specs cannot undergo static checks"
            ),
            expEn = "Section 3.1.1 states that almost any work product (requirements, designs, code, testware, user stories, contracts) can be examined using reviews."
        ),
        RawQ(
            id = "q107", ch = 3,
            q = "من المسؤول عن اختيار فريق المراجعة وتحديد موعد ومكان المراجعة وضمان إتمامها بنجاح في القسم 3.2.3؟",
            correct = "Review leader",
            wrong = listOf("Scribe", "Author", "System administrator"),
            exp = "الـ Manager يقرر ما يُراجع ويوفر الموارد، والـ Review leader يتولى المسؤولية الشاملة عن تنظيم المراجعة وتشكيل الفريق.",
            qEn = "According to CTFL v4.0 Section 3.2.3, which role takes overall responsibility for the review, including selecting participants and organizing when and where it occurs?",
            correctEn = "Review leader",
            wrongEn = listOf("Scribe", "Author", "System administrator"),
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
                "تجنب التوثيق والتحضير المسبق تماماً",
                "تدريب المستخدمين على واجهة التطبيق",
                "الاعتماد على المؤلف وحده في القرار"
            ),
            exp = "المراجعة التقنية يشارك فيها خبراء تقنيون (Peers) وميسّر لتحقيق التوافق واتخاذ قرارات حول مسائل تقنية واكتشاف العيوب.",
            qEn = "What is a primary distinguishing purpose of a 'technical review' compared to an informal review?",
            correctEn = "Building consensus and making technical decisions with qualified peers",
            wrongEn = listOf(
                "Avoiding preparation and written logs",
                "Training non-technical end users on the UI",
                "Relying solely on the author's judgment"
            ),
            expEn = "A technical review involves technically qualified reviewers and a moderator to evaluate quality, solve technical problems, and gain consensus."
        )
    )
}
