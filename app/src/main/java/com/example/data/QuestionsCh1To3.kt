package com.example.data

internal object QuestionsCh1To3 {
    val items: List<RawQ> = listOf(
        // ==================== CHAPTER 1 (20 Questions) ====================
        RawQ(
            id = "q001", ch = 1,
            q = "أي مبدأ يقول إن الاختبار الشامل لكل المدخلات والتركيبات مستحيل؟",
            correct = "Exhaustive testing is impossible",
            wrong = listOf("Defects cluster together principle", "Absence-of-defects fallacy principle", "Tests wear out (Pesticide paradox)"),
            exp = "المبدأ الثاني: لا يمكن اختبار كل التركيبات، لذلك نستخدم المخاطر والأولويات وتقنيات تصميم الاختبار.",
            qEn = "Which testing principle states that testing all combinations of inputs and preconditions is infeasible?",
            correctEn = "Exhaustive testing is impossible",
            wrongEn = listOf("Defects tend to cluster in modules", "The absence-of-defects fallacy rule", "Tests wear out over repeated runs"),
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
            correctEn = "Testing reveals runtime failures; debugging locates and fixes the defect",
            wrongEn = listOf(
                "Testing repairs defects in source code; debugging demonstrates failures to users",
                "Debugging executes scripted test cases; testing modifies the program code directly",
                "Both are development activities aimed exclusively at rewriting specifications"
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
            correct = "نظام بلا defects معروفة لكنه لا يلبي حاجة المستخدمين",
            wrong = listOf(
                "نظام تتركز معظم الـ defects المكتشفة فيه داخل وحدة برمجية واحدة عالية التعقيد",
                "نظام لم تُنفَّذ فيه اختبارات الكود الأبيض رغم تغطية جميع حالات الاستخدام المحددة",
                "نظام تأخر موعد إطلاقه بسبب إعادة تشغيل نفس مجموعة الاختبارات دون أي تحديث"
            ),
            exp = "إصلاح كل الـ defects لا يضمن نجاح النظام إن لم يحقق حاجة المستخدم (Validation).",
            qEn = "Which of the following situations illustrates the 'absence-of-defects fallacy'?",
            correctEn = "A defect-free system fails to meet actual user needs",
            wrongEn = listOf(
                "Most discovered defects cluster inside one complex module",
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
                "تقليص زمن تنفيذ الاختبارات عبر توليد بيانات الاختبار تلقائياً",
                "الاستغناء عن إعداد test plan وتقارير الإغلاق في المشاريع الرشيقة",
                "ضمان خلو النظام من أي defects غير متوقعة في بيئة الإنتاج"
            ),
            exp = "Traceability تدعم تحليل الأثر وتدقيق التغطية وإعداد تقارير الحالة.",
            qEn = "Which of the following is a primary benefit of maintaining traceability between the test basis and test cases?",
            correctEn = "Supporting impact analysis and measuring requirement coverage",
            wrongEn = listOf(
                "Reducing execution time by automatically generating synthetic test data sets",
                "Eliminating the need for any test plan or completion reports in Agile teams",
                "Guaranteeing zero unexpected defects can ever occur in live production systems"
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
            correct = "تقليل التحيز المعرفي وكشف عيوب قد يغفل عنها المؤلف",
            wrong = listOf(
                "ضمان فهم فوري ومشترك للمتطلبات دون الحاجة إلى جلسات توضيح أو مراجعة",
                "خفض تكلفة المشروع الإجمالية وإلغاء الحاجة إلى مراجعات الكود بين الزملاء",
                "تسريع إصلاح الأخطاء عبر نقل مسؤولية الجودة كاملة إلى فريق الاختبار"
            ),
            exp = "Independence يقلل التحيز المعرفي، لكنه قد يسبب عزلة أو فجوة تواصل إن لم يُدر جيداً.",
            qEn = "Which of the following is a key advantage of independence in testing?",
            correctEn = "Avoiding the author's bias to uncover different failure types",
            wrongEn = listOf(
                "Guaranteeing immediate shared understanding without clarification meetings",
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
                "QA مرادف للاختبار الديناميكي، بينما QC يقتصر على كتابة الخطط والسياسات",
                "QC موجه لتحسين العمليات ومنع الأخطاء قبل وقوعها دون فحص المنتج",
                "QA يبدأ بعد تسليم النظام للعميل، بينما QC يتم أثناء التطوير المبكر"
            ),
            exp = "QA يمنع الـ defects بتحسين العمليات، والاختبار من أنشطة Quality Control لاكتشاف العيوب في المنتج.",
            qEn = "Which statement MOST accurately describes the relationship between Quality Assurance (QA) and Quality Control (QC)?",
            correctEn = "QA is process-oriented and preventive; testing is product-oriented QC",
            wrongEn = listOf(
                "QA is synonymous with dynamic testing; QC covers writing quality policy manuals",
                "QC focuses exclusively on process audits without ever executing software products",
                "QA begins only after customer release; QC occurs strictly during initial analysis"
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
            correct = "نقل المعرفة الاختبارية والتعاون مع الفريق للجودة",
            wrong = listOf(
                "تحمّل المسؤولية الحصرية عن الجودة ومنع أي مطور من كتابة اختبارات الوحدة",
                "العمل في عزلة تامة عن الفريق وعدم حضور اجتماعات التخطيط اليومية",
                "تأجيل جميع أنشطة الاختبار حتى استلام النظام مكتملاً في نهاية المشروع"
            ),
            exp = "Whole-team approach يجعل الجودة مسؤولية مشتركة حيث يوجّه المختبرون الفريق ويدعمون بناء الاختبارات.",
            qEn = "Under the whole-team approach in CTFL v4.0, what is a key responsibility of testers?",
            correctEn = "Sharing testing knowledge so everyone owns quality",
            wrongEn = listOf(
                "Owning quality alone and prohibiting developers from writing tests",
                "Working in strict isolation and skipping daily refinement sessions",
                "Postponing all test activities until final integrated delivery"
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
            correct = "التواصل البناء والتفكير النقدي والتقارير الموضوعية",
            wrong = listOf(
                "تجنب التواصل المباشر والاكتفاء بإغلاق النسخة الاختبارية فوراً دون شرح",
                "حفظ أسماء خوارزميات الترتيب دون فهم منطق الأعمال الخاص بالنظام المالي",
                "كتابة تقارير العيوب بأسلوب حاد لإجبار المطور على رفع الأولوية مباشرة"
            ),
            exp = "منهج CTFL v4.0 يؤكد أهمية المهارات الشخصية مثل التواصل الموضوعي والتفكير النقدي والفضول.",
            qEn = "Which generic tester skill MOST helps a tester communicate defect findings constructively without creating friction with developers?",
            correctEn = "Constructive communication and objective reporting",
            wrongEn = listOf(
                "Avoiding direct dialogue and rejecting builds without documented evidence",
                "Memorizing sorting algorithms without understanding the business domain logic",
                "Writing confrontational defect reports to pressure developers into escalating priority"
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
            correct = "الإشعاع أو التداخل الكهرومغناطيسي أو الهجمات المتعمدة",
            wrong = listOf(
                "تخطي مرحلة إعداد خطة الاختبار الرئيسية في بداية المشروع التطويري",
                "انخفاض عدد حالات الاختبار المنفذة يومياً في بيئة ضمان الجودة",
                "تعيين مراجع تقني خارجي لمراجعة وثيقة المتطلبات الوظيفية النهائية"
            ),
            exp = "القسم 1.2.3: قد تنشأ الـ Failures أيضاً عن ظروف بيئية مادية (حرارة، إشعاع، مجالات مغناطيسية) أو أفعال متعمدة دون خطأ برمجي.",
            qEn = "According to CTFL v4.0, which of the following can cause a system failure WITHOUT being triggered by a human coding error?",
            correctEn = "Radiation, electromagnetic fields, or deliberate attacks",
            wrongEn = listOf(
                "Omitting the formal master test plan document at project kickoff",
                "Executing fewer regression test cases in the staging environment",
                "Assigning an external technical reviewer to inspect specifications"
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
            correct = "في الأنظمة عالية الخطورة التي تتطلب استقلالية صارمة",
            wrong = listOf(
                "في الفرق الرشيقة الصغيرة التي تسعى لتسريع التواصل اليومي المستمر",
                "عندما يشارك المختبرون في صياغة معايير القبول لقصص المستخدمين",
                "عندما يتعاون المطورون والمختبرون في بناء اختبارات آلية مشتركة"
            ),
            exp = "القسم 1.5.2: رغم مزايا Whole-team، فإن البيئات التنظيمية الحساسة للسلامة قد تتطلب مستوى أعلى من استقلالية الاختبار.",
            qEn = "In which situation might the 'whole-team approach' be less suitable on its own and require higher test independence?",
            correctEn = "Safety-critical projects mandating strict independent verification",
            wrongEn = listOf(
                "Small co-located Agile teams aiming to improve daily collaboration",
                "Teams where testers help refine user story acceptance criteria early",
                "Developers and testers pairing together on automated API checks"
            ),
            expEn = "Section 1.5.2 notes that while whole-team dynamics improve collaboration, high-risk or regulated domains often require stronger test independence."
        ),

        // ==================== CHAPTER 2 (16 Questions) ====================
        RawQ(
            id = "q011", ch = 2,
            q = "أي مما يلي يُعد مستوى اختبار (Test level)؟",
            correct = "Component integration testing",
            wrong = listOf("Smoke & sanity testing process", "Regression suite execution", "لا شيء من الخيارات المذكورة"),
            exp = "المستويات الخمسة: Component, Component integration, System, System integration, Acceptance.",
            qEn = "Which of the following is classified as a 'test level' in CTFL v4.0?",
            correctEn = "Component integration testing",
            wrongEn = listOf("Smoke and sanity testing", "Automated regression suite", "None of the listed options"),
            expEn = "The five test levels are component, component integration, system, system integration, and acceptance testing."
        ),
        RawQ(
            id = "q012", ch = 2,
            q = "بعد إصلاح defect، الاختبار الذي يتأكد أن نفس الحالة لم تعد تفشل هو:",
            correct = "Confirmation testing",
            wrong = listOf("Regression test execution", "Operational smoke testing", "Non-functional load test"),
            exp = "Confirmation (Re-testing) يعيد نفس الحالة الفاشلة. أما Regression فيتحقق من عدم كسر وظائف أخرى.",
            qEn = "After a developer fixes a reported defect, which type of testing verifies that the original failing test case now passes?",
            correctEn = "Confirmation testing",
            wrongEn = listOf("Regression suite testing", "Build verification smoke test", "Performance load testing"),
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
            correctEn = "Detecting unintended side effects in unchanged areas",
            wrongEn = listOf(
                "Measuring server response latency under peak concurrent load",
                "Reviewing requirement specifications before programming starts",
                "Verifying solely that one fixed defect no longer fails alone"
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
            correct = "تحديد الأجزاء المتأثرة لتقدير نطاق إعادة الاختبار",
            wrong = listOf(
                "قياس سرعة كتابة المطورين للشيفرة الجديدة مقارنة بالإصدار السابق في المشروع",
                "حذف جميع حالات الاختبار القديمة تلقائياً واستبدالها بسكربتات يدوية حديثة",
                "تقليص حجم قاعدة البيانات الإنتاجية الفعلي قبل تنفيذ عملية ترحيل البيانات"
            ),
            exp = "تحليل الأثر يحدد ما يجب إعادة اختباره (regression) بحسب المخاطر وحجم التغيير.",
            qEn = "How does impact analysis assist in planning maintenance testing?",
            correctEn = "Identifying affected components to scope regression testing",
            wrongEn = listOf(
                "Measuring developer coding velocity relative to previous releases",
                "Automatically deleting legacy suites and converting them into manual steps",
                "Compressing the live production database prior to executing migration"
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
            correctEn = "Writing a failing unit test before minimal code",
            wrongEn = listOf(
                "Writing full application code first and acceptance tests post-launch",
                "Replacing automated unit tests completely with manual exploration",
                "Deferring test design until system integration is fully completed"
            ),
            expEn = "TDD is a test-first approach (Red → Green → Refactor) at the component level, whereas ATDD derives tests from acceptance criteria."
        ),
        RawQ(
            id = "q062", ch = 2,
            q = "أي ممارسة تجسّد مفهوم Shift-left في دورة حياة تطوير البرمجيات؟",
            correct = "مراجعة المتطلبات مبكراً وكتابة الاختبارات قبل الكود",
            wrong = listOf(
                "زيادة عدد المختبرين اليدويين في الأسبوع الأخير قبل الإطلاق الإنتاجي فقط",
                "إلغاء اختبارات الوحدة والمكونات والاعتماد على Beta testing وحده للإطلاق",
                "تأجيل فحص الأمان والأداء بالكامل إلى مرحلة صيانة النظام بعد التسليم"
            ),
            exp = "Shift-left يعني تقديم أنشطة الاختبار والمراجعات والـ Test-first إلى أبكر وقت ممكن في SDLC.",
            qEn = "Which of the following practices BEST exemplifies the 'shift-left' approach in the SDLC?",
            correctEn = "Reviewing specs early and writing tests before coding",
            wrongEn = listOf(
                "Adding extra manual testers during the final week before launch",
                "Skipping unit and component tests to rely solely on external beta runs",
                "Postponing security and performance checks until post-release upkeep"
            ),
            expEn = "Shift-left moves testing earlier in the lifecycle via early reviews, test-first practices (TDD/ATDD), CI, and non-functional checks early."
        ),
        RawQ(
            id = "q063", ch = 2,
            q = "ما الهدف الرئيسي من عقد Retrospectives في نهاية كل تكرار (Iteration)؟",
            correct = "تقييم النتائج واستخلاص إجراءات لتحسين العملية",
            wrong = listOf(
                "محاسبة المختبرين على عدد العيوب التي وصلت إلى بيئة الاختبار الوظيفي",
                "استبدال جميع وثائق الاختبار الرسمية بمحاضر شفوية غير متابعة لاحقاً",
                "إعادة تنفيذ جميع اختبارات القبول السابقة يدوياً أمام العميل المباشر"
            ),
            exp = "تساعد الـ Retrospectives على التحسين المستمر لجودة العمليات والاختبارات وتقليل العيوب المتكررة.",
            qEn = "What is the primary purpose of holding retrospectives at the end of an iteration or project milestone?",
            correctEn = "Reviewing lessons learned to improve team processes",
            wrongEn = listOf(
                "Assigning personal blame for defects detected during the sprint",
                "Replacing documented test artifacts with informal verbal updates",
                "Re-executing every acceptance test manually before the customer"
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
            wrong = listOf("Non-functional portability testing", "Static informal document review", "Contractual acceptance validation"),
            exp = "التحقق من عدم حدوث آثار جانبية في مناطق لم تتغير مباشرة بعد تعديل الكود هو Regression testing.",
            qEn = "After modifying a discount calculation function, a tester checks that invoice rendering and PDF export remain intact. What is this testing called?",
            correctEn = "Regression testing",
            wrongEn = listOf("Non-functional portability check", "Static informal document review", "Contractual acceptance testing"),
            expEn = "Regression testing is change-related testing aimed at confirming unchanged features still work after modifications."
        ),
        RawQ(
            id = "q102", ch = 2,
            q = "كيف يؤثر اختيار نموذج SDLC التسلسلي (مثل Waterfall) مقارنة بالنماذج التكرارية على أنشطة الاختبار؟",
            correct = "يبدأ Dynamic testing غالباً متأخراً في التسلسلي ومتواصلاً في التكراري",
            wrong = listOf(
                "يلغي النموذج التسلسلي الحاجة إلى static testing ومراجعات الوثائق تماماً منذ البداية",
                "يمنع النموذج التكراري إجراء أي اختبارات نظام أو اختبارات قبول نهائية قبل النشر",
                "لا يتأثر توقيت الاختبار أو مستوى التوثيق مطلقاً بنوع نموذج دورة حياة التطوير"
            ),
            exp = "في النماذج التسلسلية يكون الاختبار الساكن مبكراً والديناميكي متأخراً، أما في التكرارية/الرشيقة فيختبر كل تكرار مبكراً وبوتيرة مستمرة.",
            qEn = "How does a sequential SDLC model (e.g., Waterfall) typically differ from an iterative model in terms of testing?",
            correctEn = "Dynamic testing occurs later in sequential models vs. each sprint",
            wrongEn = listOf(
                "Sequential models completely eliminate static testing and early specification reviews",
                "Iterative models prohibit executing system-level or user acceptance testing cycles",
                "Testing schedules and documentation depth are unaffected by the chosen SDLC"
            ),
            expEn = "Section 2.1.1: Sequential models defer dynamic testing until later phases; iterative/incremental models test small deliveries repeatedly."
        ),
        RawQ(
            id = "q103", ch = 2,
            q = "التحقق من سهولة نقل التطبيق للعمل من مزوّد سحابي (AWS) إلى مزوّد آخر (Azure) يُعد اختبار:",
            correct = "Portability testing",
            wrong = listOf("Usability efficiency testing", "Functional suitability check", "Confirmation regression test"),
            exp = "Portability تقيس سهولة نقل النظام من بيئة أو بنية تحتية إلى أخرى، وهي ضمن Non-functional testing.",
            qEn = "Testing how easily an application can be transferred from one cloud infrastructure (AWS) to another (Azure) is an example of:",
            correctEn = "Portability testing",
            wrongEn = listOf("Usability & UX evaluation", "Functional correctness test", "Confirmation defect re-test"),
            expEn = "Portability (ISO 25010 quality characteristic) evaluates ease of transferring a system between hardware, software, or operational environments."
        ),
        RawQ(
            id = "q104", ch = 2,
            q = "اختبار التحقق من النسخ الاحتياطي والاستعادة من الكوارث وإجراءات الصيانة التشغيلية قبل التسليم هو:",
            correct = "Operational acceptance testing",
            wrong = listOf("Customer beta field trial", "Component integration check", "Exploratory usability testing"),
            exp = "OAT ضمن مستويات Acceptance testing يتحقق من جاهزية فرق التشغيل: Backup/restore, Disaster recovery, User management, Maintenance.",
            qEn = "Testing backup/restore procedures, disaster recovery, and system administration tasks before go-live is known as:",
            correctEn = "Operational acceptance testing",
            wrongEn = listOf("Early internal alpha testing", "Component integration testing", "Exploratory usability checking"),
            expEn = "Operational acceptance testing (Section 2.2.1) evaluates operational readiness by administrators/operators, including backup, failover, and security checks."
        ),
        RawQ(
            id = "q105", ch = 2,
            q = "أي عامل مما يلي يجعل إجراء Impact analysis في Maintenance testing صعباً ومكلفاً؟",
            correct = "تقادم المواصفات أو غياب الـ Traceability بين الأصول",
            wrong = listOf(
                "وجود اختبارات انحدار آلية مرتبطة بأسماء الميزات في مستودع العمل",
                "تنفيذ النظام على بيئة سحابية حديثة تدعم الحاويات والعزل البرمجي",
                "إصدار النظام وفق جدول صيانة مخطط له مسبقاً مع فرق التشغيل"
            ),
            exp = "القسم 2.3: يصعب تحليل الأثر عند تقادم المواصفات أو فقدان الـ traceability أو عدم فهم بنية الكود القديم.",
            qEn = "Which of the following makes impact analysis especially difficult during maintenance testing?",
            correctEn = "Outdated specs or missing traceability links",
            wrongEn = listOf(
                "Automated regression suites linked clearly to feature specs",
                "Running the operational system in modern containerized clouds",
                "Scheduling enhancements inside a planned maintenance cycle"
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
            wrong = listOf("Author (Content creator)", "Scribe / Note Recorder", "Senior Resource Manager"),
            exp = "الـ Facilitator يدير الاجتماع، والـ Scribe يوثق، والـ Author مؤلف المنتج المُراجَع.",
            qEn = "Which review role is responsible for running review meetings effectively and ensuring a safe space for discussion?",
            correctEn = "Moderator (Facilitator)",
            wrongEn = listOf("Author of the work product", "Scribe / Minutes Recorder", "Senior Project Manager"),
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
                "Static يكشف أعطال التشغيل في الإنتاج؛ Dynamic يفحص الوثائق دون تنفيذها",
                "كلاهما يتطلب بيئة تشغيل متكاملة لتنفيذ الشيفرة المصدرية وقياس المخرجات",
                "Dynamic لا يعتمد على test basis؛ بينما Static يقتصر على اختبار الأداء"
            ),
            exp = "التنفيذ هو الفارق: Dynamic يظهر failures أثناء التشغيل، وStatic يفحص work product مباشرة.",
            qEn = "Which statement BEST captures the difference between static testing and dynamic testing?",
            correctEn = "Static finds defects directly; dynamic triggers runtime failures",
            wrongEn = listOf(
                "Static testing detects production crashes; dynamic inspects documents offline",
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
            correct = "تحويل المراجعة لأداة لتقييم المؤلف ومعاقبته شخصياً",
            wrong = listOf(
                "تقسيم الوثائق الكبيرة إلى أجزاء صغيرة سهلة التركيز والفحص",
                "تزويد المراجعين بقوائم فحص محددة ومحدثة للمخاطر الهندسية",
                "منح المشاركين وقتاً كافياً للتحضير والمراجعة الفردية المسبقة"
            ),
            exp = "البيئة الخالية من اللوم (Trust & non-judgmental culture) ركيزة أساسية لنجاح المراجعات.",
            qEn = "Which of the following factors is MOST likely to undermine the success of a review process?",
            correctEn = "Using defect counts to penalize the author",
            wrongEn = listOf(
                "Splitting large work products into small manageable chunks",
                "Providing reviewers with tailored risk-focused checklists",
                "Allocating sufficient scheduled time for individual review"
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
            correct = "يمكن فحص معظم منتجات العمل بما فيها المتطلبات والكود والخطط",
            wrong = listOf(
                "يقتصر Static testing على ملفات الشيفرة المصدرية المكتوبة بلغات مُصرَّفة فقط",
                "لا يمكن فحص خطط الاختبار أو قصص المستخدمين إلا عبر Dynamic execution",
                "المنتجات المكتوبة بلغة طبيعية لا تناسب المراجعات الساكنة بسبب غياب المترجم"
            ),
            exp = "القسم 3.1.1: يمكن فحص معظم work products بـ Static testing (مواصفات، قصص مستخدمين، خطط، حالات اختبار، كود، عقود).",
            qEn = "Which of the following statements about the scope of static testing in CTFL v4.0 is MOST accurate?",
            correctEn = "Almost any work product can be statically checked, from code to plans",
            wrongEn = listOf(
                "Static testing is restricted exclusively to compiled source code modules",
                "Test plans and user stories can only be verified via dynamic test execution",
                "Natural-language specifications cannot undergo static testing without a compiler"
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
            correctEn = "Building consensus and deciding technical issues with qualified peers",
            wrongEn = listOf(
                "Avoiding individual preparation or records to keep meetings under five minutes",
                "Training non-technical end users on navigating the released user interface",
                "Relying solely on the author to unilaterally resolve issues without peer input"
            ),
            expEn = "A technical review involves technically qualified reviewers and a moderator to evaluate quality, solve technical problems, and gain consensus."
        )
    )
}
