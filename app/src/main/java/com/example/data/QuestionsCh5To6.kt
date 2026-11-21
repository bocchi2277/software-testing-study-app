package com.example.data

internal object QuestionsCh5To6 {
    val items: List<RawQ> = listOf(
        // ==================== CHAPTER 5 (22 Questions) ====================
        RawQ(
            id = "q041", ch = 5,
            q = "كيف يتحدد مستوى المخاطرة (Risk level) وفق منهج CTFL v4.0؟",
            correct = "من احتمال الحدوث (Likelihood) وأثره (Impact)",
            wrong = listOf(
                "قسمة عدد المطورين العاملين على المشروع على عدد المختبرين",
                "مجموع حالات الاختبار المنفذة مطروحاً منه عدد العيوب المغلقة",
                "زمن تنفيذ مجموعة الانحدار الآلية في بيئة الاختبار الوظيفي"
            ),
            exp = "القسم 5.2.1: يُعبَّر عن مستوى المخاطرة بـ Risk likelihood (احتمال الحدوث) وRisk impact (شدة الضرر).",
            qEn = "How is the level of a risk determined in the ISTQB CTFL v4.0 syllabus?",
            correctEn = "Combining risk likelihood and risk impact",
            wrongEn = listOf(
                "By dividing the number of software developers by the number of testers",
                "By subtracting closed defect reports from executed test cases",
                "By measuring total runtime of automated regression suites in staging"
            ),
            expEn = "Section 5.2.1 defines risk level by two factors: risk likelihood (probability) and risk impact (harm/severity), often combined via multiplication."
        ),
        RawQ(
            id = "q042", ch = 5,
            q = "أي مما يلي مثال على Project risk وليس Product risk؟",
            correct = "تأخر تسليم بيئة الاختبار أو استقالة خبير",
            wrong = listOf(
                "بطء زمن استجابة صفحة الدفع عن 5 ثوانٍ تحت الضغط الطبيعي",
                "ثغرة حقن SQL تسمح بكشف بيانات حسابات العملاء في القاعدة",
                "خطأ حسابي في معادلة احتساب الضريبة داخل الفاتورة النهائية"
            ),
            exp = "Project risks تخص إدارة المشروع والموارد والجدول، بينما Product risks تخص جودة وسلوك البرمجية نفسها.",
            qEn = "Which of the following is an example of a PROJECT risk rather than a product risk?",
            correctEn = "Delayed delivery of the test environment",
            wrongEn = listOf(
                "Checkout page response times exceeding 5 seconds under normal traffic",
                "An SQL injection vulnerability exposing sensitive customer records",
                "An incorrect rounding formula miscalculating sales tax on invoices"
            ),
            expEn = "Project risks affect schedule, budget, resources, or skills; product risks relate to quality characteristics of the test object."
        ),
        RawQ(
            id = "q043", ch = 5,
            q = "في تقدير الثلاث نقاط (Three-point estimation): إذا كان المتفائل a=2 والمحتمل m=5 والمتشائم b=14، فما التقدير؟",
            correct = "6",
            wrong = listOf("5", "7", "8"),
            exp = "E = (a + 4m + b) / 6 = (2 + 4×5 + 14) / 6 = 36 / 6 = 6.",
            qEn = "Using three-point estimation with optimistic (a) = 2, most likely (m) = 5, and pessimistic (b) = 14, what is the final estimate?",
            correctEn = "6",
            wrongEn = listOf("5", "7", "8"),
            expEn = "Formula: E = (a + 4m + b) / 6 = (2 + 20 + 14) / 6 = 36 / 6 = 6."
        ),
        RawQ(
            id = "q044", ch = 5,
            q = "أي عبارة تصف شكل Test Pyramid القياسي بدقة؟",
            correct = "قاعدة واسعة للوحدة السريعة وأقل للواجهات E2E",
            wrong = listOf(
                "التركيز على اختبارات واجهة المستخدم اليدوية بنسبة 80% والوحدة بنسبة 5%",
                "تنفيذ اختبارات الأداء والأمان فقط قبل اختبارات الوحدة في كل دفعة",
                "مساواة عدد اختبارات النظام البطيئة بعدد اختبارات الوحدة السريعة تماماً"
            ),
            exp = "Test pyramid يوصي بالاعتماد الأكبر على اختبارات سريعة وعالية العزل (Unit/API) وتقليل UI الهشة والبطيئة.",
            qEn = "Which statement BEST describes the standard Test Pyramid model?",
            correctEn = "Broad base of fast unit tests; fewest slow E2E UI tests",
            wrongEn = listOf(
                "Focusing 80% of automation on slow UI tests and only 5% on unit tests",
                "Executing load and penetration tests prior to running component tests",
                "Maintaining an identical count of end-to-end UI tests and unit tests"
            ),
            expEn = "The test pyramid shows high granularity and speed at the bottom (unit/component) and decreasing test counts toward slower UI end-to-end tests."
        ),
        RawQ(
            id = "q045", ch = 5,
            q = "في Agile Testing Quadrants، أي ربع يحتوي على اختبارات موجهة للأعمال وتنتقد المنتج (مثل Exploratory وUsability)؟",
            correct = "Quadrant 3 (Q3)",
            wrong = listOf("Quadrant 1 (Q1)", "Quadrant 2 (Q2)", "Quadrant 4 (Q4)"),
            exp = "Q1 تقني يدعم الفريق، Q2 أعمال يدعم الفريق، Q3 أعمال ينتقد المنتج (Exploratory/UAT/Usability)، Q4 تقني ينتقد المنتج.",
            qEn = "In the Agile Testing Quadrants, which quadrant covers business-facing tests that critique the product (e.g., exploratory and usability testing)?",
            correctEn = "Quadrant 3 (Q3)",
            wrongEn = listOf("Quadrant 1 (Q1)", "Quadrant 2 (Q2)", "Quadrant 4 (Q4)"),
            expEn = "Q3 is business-facing & critiques the product (exploratory, usability, UAT). Q1/Q2 support the team; Q4 is technology-facing & critiques the product."
        ),
        RawQ(
            id = "q046", ch = 5,
            q = "ما الغرض الرئيسي من Exit criteria (أو Definition of Done في Agile)؟",
            correct = "شروط إعلان اكتمال نشاط أو مستوى اختبار",
            wrong = listOf(
                "تحديد الحد الأدنى من جاهزية البيئة والبيانات قبل بدء تنفيذ الاختبارات",
                "توثيق خطوات إعادة إنتاج خطأ برمجي محدد داخل تقرير العيوب",
                "حساب التكلفة السنوية لتراخيص أدوات إدارة الاختبار والتقارير"
            ),
            exp = "Entry criteria للبدء، أما Exit criteria (أو DoD) فتشترط الوفاء بمعايير التغطية والعيوب والتقارير قبل الإنهاء.",
            qEn = "What is the primary purpose of exit criteria (or Definition of Done in Agile) in test planning?",
            correctEn = "Conditions required to declare a test activity done",
            wrongEn = listOf(
                "Specifying the prerequisites needed before test execution can begin",
                "Documenting exact reproduction steps inside an individual bug report",
                "Calculating annual licensing renewals for test management software"
            ),
            expEn = "Exit criteria specify conditions (coverage, defect thresholds, schedules, reports) required to conclude a test activity or level."
        ),
        RawQ(
            id = "q047", ch = 5,
            q = "ما الفرق بين Severity وPriority في Defect report؟",
            correct = "Severity تقيس الأثر التقني؛ وPriority إلحاح الإصلاح",
            wrong = listOf(
                "Severity يحددها مدير المبيعات فقط، بينما Priority هي درجة صعوبة كتابة الكود",
                "كلاهما مصطلح واحد ويجب أن تتطابق قيمتهما دائماً في كل تقرير عيب",
                "Priority تصف بيئة التشغيل، بينما Severity تحدد اسم المبرمج المسؤول"
            ),
            exp = "مثال كلاسيكي: خطأ إملائي في شعار الصفحة الرئيسية (Severity منخفضة لكن Priority عالية للسمعة التجارية).",
            qEn = "How do 'severity' and 'priority' differ in a defect report?",
            correctEn = "Severity is technical impact; priority is fix urgency",
            wrongEn = listOf(
                "Severity is set exclusively by sales teams; priority measures coding effort",
                "Both terms are synonyms and must always hold identical values for every bug",
                "Priority records the test environment; severity names the assigned developer"
            ),
            expEn = "Severity assesses technical failure impact; priority dictates fix scheduling based on business value and risk."
        ),
        RawQ(
            id = "q048", ch = 5,
            q = "كيف تدعم Configuration Management (CM) موثوقية أنشطة الاختبار؟",
            correct = "بضبط نسخ الكود والبيئات كـ Baselines للتتبع وإعادة الإنتاج",
            wrong = listOf(
                "بتحويل جميع الاختبارات اليدوية إلى اختبارات أداء آلية عالية الضغط",
                "بمنع المطورين والمختبرين من إنشاء فروع كود أثناء مراحل التطوير",
                "بإلغاء الحاجة إلى تقارير العيوب والربط التتبعي (Traceability)"
            ),
            exp = "CM تربط كل إصدار برمجي بإصدار المتطلبات وحالات الاختبار وبيئة التنفيذ، مما يمنع تضارب النسخ.",
            qEn = "How does Configuration Management (CM) directly support reliable testing?",
            correctEn = "Versioning code, testware, and environments as baselines",
            wrongEn = listOf(
                "By automatically converting manual functional scripts into load tests",
                "By prohibiting developers and testers from using source control branches",
                "By eliminating the need for defect tracking and requirements traceability"
            ),
            expEn = "CM identifies and versions configuration items (code, testware, environments), ensuring tested items match baselines and failures can be reproduced."
        ),
        RawQ(
            id = "q049", ch = 5,
            q = "تقرير يُصدر دورياً أثناء التنفيذ لعرض نسبة الحالات المنفذة والعيوب المفتوحة والعوائق الحالية يُسمى:",
            correct = "Test progress report",
            wrong = listOf("Test completion report", "Defect summary policy", "Master risk register"),
            exp = "Test progress report يصدر بانتظام أثناء الاختبار، بينما Test completion report يصدر عند نهاية مرحلة أو إصدار.",
            qEn = "A report issued periodically during test execution to summarize test progress, deviations, blockers, and open defects is called a:",
            correctEn = "Test progress report",
            wrongEn = listOf("Test completion report", "Organizational test policy", "Master risk register"),
            expEn = "Test progress reports are produced regularly during ongoing testing; test completion reports summarize results once a milestone finishes."
        ),
        RawQ(
            id = "q050", ch = 5,
            q = "ما أفضل ترتيب عند تطبيق Risk-based testing لتنفيذ حالات الاختبار في وقت محدود؟",
            correct = "تشغيل اختبارات Product risks الأعلى خطورة أولاً",
            wrong = listOf(
                "ترتيب حالات الاختبار أبجدياً حسب اسم المختبر الذي كتبها في النظام",
                "تأجيل الميزات الحرجة والأكثر مخاطرة إلى اليوم الأخير قبل الإطلاق",
                "تنفيذ حالات الاختبار الأسرع فقط وتجاهل المدفوعات والأمان تماماً"
            ),
            exp = "في الاختبار المبني على المخاطر تُنفَّذ الاختبارات الموجهة للمناطق الأعلى خطراً أولاً لضمان اكتشاف العيوب الحرجة مبكراً.",
            qEn = "When execution time is constrained under risk-based testing, how should test cases be prioritized?",
            correctEn = "Run tests covering highest product risks first",
            wrongEn = listOf(
                "Execute test cases alphabetically by the author's last name",
                "Defer all high-risk payment and security tests until the final hour",
                "Run only the shortest trivial UI tests and skip risk mitigation"
            ),
            expEn = "Risk-based prioritization ensures the most severe risks are mitigated earliest, balancing importance with test dependencies."
        ),
        RawQ(
            id = "q051", ch = 5,
            q = "أثناء الاختبار، قبل العميل بالوعي بقاء خطر منخفض الأثر وتكلفة إصلاحه مرتفعة جداً دون إجراء اختبار إضافي. أي استراتيجية استجابة للمخاطر هذه؟",
            correct = "Risk acceptance",
            wrong = listOf("Risk mitigation", "Risk transfer", "Risk escalation"),
            exp = "استجابات المخاطر: التخفيف (Mitigation)، القبول (Acceptance)، النقل (Transfer)، وخطة الطوارئ (Contingency).",
            qEn = "Stakeholders consciously decide to live with a low-impact risk without extra testing because the mitigation cost exceeds the potential loss. Which risk response is this?",
            correctEn = "Risk acceptance",
            wrongEn = listOf("Risk mitigation", "Risk transfer", "Risk elimination"),
            expEn = "CTFL v4.0 Section 5.2.3 lists risk responses including mitigation, acceptance, transfer, and contingency plans."
        ),
        RawQ(
            id = "q052", ch = 5,
            q = "ما المعلومة الجوهرية التي يعتمد عليها المطور أكثر من غيرها في Defect report لإيجاد سبب العيب بسرعة؟",
            correct = "خطوات إعادة الإنتاج، النتيجة الفعلية والمتوقعة، والبيئة",
            wrong = listOf(
                "اسم المختبر الكامل ورقمه الوظيفي وتاريخ ميلاده في سجلات الشركة",
                "تقييم شخصي من المختبر حول كفاءة فريق التطوير وأسلوب برمجته",
                "القائمة الكاملة لجميع الموظفين الذين حضروا اجتماع المراجعة الدوري"
            ),
            exp = "Defect report الفعّال يوفر Steps to reproduce وActual/Expected results وسجلات البيئة لإعادة توليد العيب فوراً.",
            qEn = "Which information in a defect report is MOST critical for enabling a developer to quickly reproduce and investigate a failure?",
            correctEn = "Reproduction steps, expected vs. actual results, and setup",
            wrongEn = listOf(
                "The tester's employee ID number and years of QA experience",
                "Subjective commentary criticizing the developer's coding style",
                "Attendance logs from previous sprint planning meetings and calls"
            ),
            expEn = "Section 5.5 highlights reproducible steps, expected/actual outcomes, build/environment versions, and logs/screenshots."
        ),
        RawQ(
            id = "q086", ch = 5,
            q = "تقدير ثلاثي النقاط (Three-point) لاختبار وحدة الدفع: متفائل a = 4 ساعات، محتمل m = 7 ساعات، متشائم b = 16 ساعة. ما التقدير المتوقع E؟",
            correct = "8 ساعات",
            wrong = listOf("7 ساعات", "9 ساعات", "10 ساعات"),
            exp = "E = (4 + 4×7 + 16) / 6 = (4 + 28 + 16) / 6 = 48 / 6 = 8 ساعات.",
            qEn = "Three-point estimation for testing a payment module yields a = 4 hours, m = 7 hours, and b = 16 hours. What is the expected effort E?",
            correctEn = "8 hours",
            wrongEn = listOf("7 hours", "9 hours", "10 hours"),
            expEn = "E = (a + 4m + b) / 6 = (4 + 28 + 16) / 6 = 48 / 6 = 8 hours."
        ),
        RawQ(
            id = "q087", ch = 5,
            q = "في شاشة «نبذة عنا» غير المستخدمة غالباً ظهر خطأ إملائي واضح في اسم المؤسسة الرسمي قبل يومين من حملة إعلامية كبرى. كيف تُقيّم Severity وPriority لهذا العيب؟",
            correct = "Severity منخفضة (تجميلي) و Priority عالية (للحملة)",
            wrong = listOf(
                "Severity عالية جداً (انهيار كامل للنظام) و Priority منخفضة جداً",
                "كلاهما منخفض جداً لأن الخطأ في النصوص لا يُسجَّل كـ defect برمجي",
                "كلاهما حرج لأن كل خطأ إملائي يتلف سجلات قاعدة البيانات مباشرة"
            ),
            exp = "التصنيف التقني للعيب التجميلي منخفض Severity، لكن السياق التجاري قبل الإطلاق يفرض Priority عالية لإصلاحه فوراً.",
            qEn = "Two days before a major press launch, a typo is spotted in the company's official name on a rarely visited 'About Us' screen. How should severity and priority be classified?",
            correctEn = "Low severity (cosmetic) and high priority (urgent launch)",
            wrongEn = listOf(
                "High severity (critical system crash) and very low priority",
                "Both low because text typos are excluded from defect trackers",
                "Both critical because any spelling error corrupts database rows"
            ),
            expEn = "A typo causes minimal technical disruption (low severity) yet carries high commercial urgency prior to launch (high priority)."
        ),
        RawQ(
            id = "q088", ch = 5,
            q = "لديك 4 حالات اختبار: TC1 لميزة نادرة ومنخفضة الأثر (الأولوية 4)، TC2 لعملية الدفع الأساسية الحرجة (الأولوية 1)، لكن TC2 تعتمد تقنياً على TC3 (إنشاء حساب منخفض الأولوية). إذا رتّبنا التنفيذ مع مراعاة الاعتماديات، ما الترتيب الصحيح؟",
            correct = "TC3 أولاً ثم TC2 لتلبية الاعتماد التقني",
            wrong = listOf(
                "TC1 ثم TC4 وتجاهل الاعتماديات التقنية الخاصة بحالة الدفع TC2",
                "TC2 مباشرة قبل TC3 رغم أن الحساب غير موجود بعد، مما يفشل البيئة",
                "تأجيل TC2 وTC3 لنهاية الدورة لأن TC3 منخفضة الأولوية في القائمة"
            ),
            exp = "القسم 5.1.5: إذا اعتمد اختبار عالي الأولوية (TC2) على اختبار أقل أولوية (TC3)، فيجب تنفيذ TC3 قبله.",
            qEn = "Test case TC2 covers high-risk checkout (Priority 1) but functionally depends on TC3 (account creation, Priority 3). Following CTFL v4.0 test prioritization rules, what order is required?",
            correctEn = "Run TC3 first, then TC2 to satisfy dependencies",
            wrongEn = listOf(
                "Execute TC2 before TC3 even though no user account exists yet",
                "Defer both TC3 and TC2 to the end because TC3 is lower priority",
                "Run only Priority 1 tests and skip TC3 regardless of test failure"
            ),
            expEn = "Section 5.1.5 explains that if a higher-priority test case depends on a lower-priority test case, the prerequisite test must run first."
        ),
        RawQ(
            id = "q089", ch = 5,
            q = "في جدول اختبارات رشيقة (Agile Testing Quadrants)، أين تصنف اختبارات الأداء الآلية (Load/Stress) وفحص الثغرات الأمنية الآلي؟",
            correct = "Quadrant 4 (Q4) — اختبارات تقنية تنتقد المنتج",
            wrong = listOf(
                "Quadrant 1 (Q1) — اختبارات وحدة ومكونات تدعم الفريق",
                "Quadrant 2 (Q2) — اختبارات قبول وظيفية تدعم الفريق",
                "Quadrant 3 (Q3) — اختبارات استكشافية يدوية موجهة للأعمال"
            ),
            exp = "الربع Q4 يشمل الاختبارات غير الوظيفية التقنية التي تقيّم المنتج وتنتقده (الأداء، الأمان، الموثوقية) وغالباً ما تكون بأدوات.",
            qEn = "Where do automated performance (load/stress) tests and automated security vulnerability scans belong in the Agile Testing Quadrants?",
            correctEn = "Quadrant 4 (Q4) — technology-facing tests that critique the product",
            wrongEn = listOf(
                "Quadrant 1 (Q1) — technology-facing unit tests supporting the team",
                "Quadrant 2 (Q2) — business-facing functional acceptance tests supporting the team",
                "Quadrant 3 (Q3) — business-facing manual exploratory tests"
            ),
            expEn = "Q4 comprises technology-facing tests that critique the product, mainly non-functional evaluations like performance, security, and maintainability."
        ),
        RawQ(
            id = "q090", ch = 5,
            q = "نجح فحص الانحدار على النسخة v2.1 في بيئة الاختبار، لكن أثناء مراجعة النشر تبيّن أن البيئة كانت متصلة بنسخة قاعدة بيانات من الإصدار القديم v1.9. أي نشاط إدارة اختبار أُهمل؟",
            correct = "Configuration Management (ضبط التهيئة)",
            wrong = listOf("Three-point effort estimation methods", "Exploratory session test chartering", "Boundary value partition derivation"),
            exp = "غياب ضبط نسخ عناصر التهيئة (تطبيق v2.1 مع قاعدة بيانات v1.9) يبطل مصداقية نتائج الاختبار وإعادة الإنتاج.",
            qEn = "A regression run on build v2.1 passed in staging, yet investigators later realized the staging database schema was still on v1.9. Which test management activity failed?",
            correctEn = "Configuration Management (baseline control)",
            wrongEn = listOf("Three-point effort estimation formulas", "Exploratory session chartering", "Boundary value data derivation"),
            expEn = "Configuration Management prevents version mismatches between application builds, testware, and database/environment baselines."
        ),
        RawQ(
            id = "q091", ch = 5,
            q = "للتخفيف من Risk خاص باحتمال فقدان بيانات المدفوعات أثناء الترحيل السحابي، خصص القائد اختبار استعادة النسخ الاحتياطي وتدقيق تكامل القيم كأولوية قصوى. ما دور الاختبار هنا؟",
            correct = "Risk mitigation (تخفيف مخاطرة المنتج)",
            wrong = listOf("Risk transfer via insurer policy", "Risk denial before project audit", "Project schedule closure auditing"),
            exp = "في إدارة المخاطر، يُستخدم الاختبار نفسه كأداة أساسية لتخفيف (Mitigate) مخاطر المنتج من خلال كشف العيوب مبكراً في المناطق الخطرة.",
            qEn = "To reduce the risk of payment data loss during cloud migration, the test manager prioritizes backup-restore and data integrity checks. How is testing acting here?",
            correctEn = "Risk mitigation reducing product risk",
            wrongEn = listOf("Risk transfer to a third-party insurer", "Pure project schedule compression", "Passive risk acceptance by stakeholders"),
            expEn = "Testing is a primary means of risk mitigation: identifying defects early in high-risk areas reduces the likelihood of production failure."
        ),
        RawQ(
            id = "q115", ch = 5,
            q = "أي مما يلي يُعد مثالاً دقيقاً على Entry Criterion وليس Exit Criterion لاختبار النظام؟",
            correct = "جاهزية بيئة الاختبار ونشر البناء والبيانات",
            wrong = listOf(
                "تحقيق تغطية 100% لجميع المتطلبات الحرجة وعدم بقاء عيوب حرجة مفتوحة",
                "اعتماد تقرير إنجاز الاختبار (Test completion report) من أصحاب المصلحة",
                "استيفاء حد زمن الاستجابة المطلوب لجميع نقاط النهاية في الخادم الإنتاجي"
            ),
            exp = "Entry criteria شروط الجاهزية للبدء (بيئة، كود مبني، بيانات)، بينما Exit criteria شروط الاكتمال قبل الإغلاق.",
            qEn = "Which of the following is an example of an ENTRY criterion rather than an exit criterion for system testing?",
            correctEn = "Environment, compiled build, and test data are ready",
            wrongEn = listOf(
                "All critical requirements reach 100% test coverage with zero open bugs",
                "The final test completion report is reviewed and signed off by leads",
                "All production-like API response times meet the defined SLA thresholds"
            ),
            expEn = "Entry criteria specify preconditions to start a test activity (build deployed, environment and data ready); exit criteria define completion thresholds."
        ),
        RawQ(
            id = "q116", ch = 5,
            q = "لاحظ مدير الاختبار عبر المقاييس تأخر تنفيذ اختبارات الدفع، فقرر إعادة توزيع اثنين من المختبرين للتركيز على وحدة الدفع وتحديث جدول التنفيذ. أي نشاط يمارسه هنا؟",
            correct = "Test control (إجراء تصحيحي للمسار)",
            wrong = listOf("Static informal document review", "Detailed test case design", "Final test completion reporting"),
            exp = "رصد التأخير هو Test monitoring، أما اتخاذ قرارات وإجراءات تصحيحية (إعادة توزيع الموارد/تعديل الأولويات) فهو Test control.",
            qEn = "After metrics reveal test execution falling behind on payment features, a test manager reallocates two testers and reprioritizes the schedule. Which activity is this?",
            correctEn = "Test control",
            wrongEn = listOf("Detailed test design", "Test completion summary", "Static informal review"),
            expEn = "Test monitoring gathers metrics and compares actual vs. planned progress; test control implements corrective actions based on those findings."
        ),
        RawQ(
            id = "q117", ch = 5,
            q = "يستخدم الفريق متوسط جهد آخر 3 تكرارات (Sprints) في المشروع الحالي للتنبؤ بجهد الاختبار للتكرار القادم. أي تقنية تقدير هذه؟",
            correct = "Extrapolation (الاستقراء)",
            wrong = listOf("Wideband Delphi expert round", "Three-point PERT formula", "Error guessing estimation"),
            exp = "القسم 5.1.4: Extrapolation يستند إلى قياسات سابقة مبكرة ضمن المشروع نفسه للتنبؤ بالمتبقي، بينما Estimation based on ratios يعتمد نسب مشاريع سابقة.",
            qEn = "An Agile team uses velocity and effort metrics measured from the first three sprints of the current project to forecast testing effort for Sprint 4. Which estimation technique is this?",
            correctEn = "Extrapolation",
            wrongEn = listOf("Wideband Delphi", "Three-point PERT", "Error guessing"),
            expEn = "CTFL v4.0 Section 5.1.4 defines extrapolation as making measurements early in the current project and projecting future effort from them."
        ),
        RawQ(
            id = "q118", ch = 5,
            q = "ما المعلومات التي ينبغي تضمينها في Test Completion Report عند تسليم الإصدار وفق CTFL v4.0؟",
            correct = "النتائج والانحرافات والعيوب والمخاطر المتبقية",
            wrong = listOf(
                "خطوات إعادة التشغيل المفصلة لكل حالة اختبار فردية وبيانات الاختبار الخام",
                "الشيفرة المصدرية الكاملة لكل وحدة برمجية أُصلحت أثناء المشروع",
                "قائمة برواتب الفريق وتقييمات الأداء السنوية للمطورين والمختبرين"
            ),
            exp = "القسم 5.3.2: تقرير الإنجاز يلخص ما تم إنجازه والانحرافات والمقاييس والعيوب المتبقية والمخاطر القائمة والدروس المستفادة.",
            qEn = "Which set of items should typically be included in a Test Completion Report according to CTFL v4.0?",
            correctEn = "Summary, plan deviations, open defects, residual risks",
            wrongEn = listOf(
                "Full step-by-step procedures and raw database dumps for every test case",
                "Complete source code listings of all bug fixes merged during release",
                "Individual HR performance appraisals and salary data for team members"
            ),
            expEn = "Section 5.3.2 specifies that completion reports summarize test results, plan deviations, quality metrics, open/residual risks, and lessons learned."
        ),

        // ==================== CHAPTER 6 (10 Questions) ====================
        RawQ(
            id = "q054", ch = 6,
            q = "أي مما يلي يُعد من فوائد أتمتة الاختبار في منهج CTFL v4.0؟",
            correct = "تسريع المهام المتكررة بدقة واتساق أعلى",
            wrong = listOf(
                "إلغاء الحاجة إلى تصميم حالات اختبار أو فهم المتطلبات الهندسية تماماً",
                "ضمان خلو البرنامج بنسبة 100% من العيوب دون اختبار استكشافي",
                "إزالة أي تكلفة لصيانة السكربتات عند تغيير واجهات النظام المستمر"
            ),
            exp = "الأتمتة تسرّع المهام المتكررة وتزيل الأخطاء البشرية في التحقق المتكرر، لكنها تحتاج صيانة وتخطيطاً.",
            qEn = "Which of the following is a recognized benefit of test automation in CTFL v4.0?",
            correctEn = "Faster, consistent execution of repetitive tests",
            wrongEn = listOf(
                "Eliminating the need for test analysis, design, and domain knowledge",
                "Guaranteeing zero defects in production without exploratory testing",
                "Removing all maintenance effort whenever application UIs or APIs evolve"
            ),
            expEn = "Section 6.2 notes automation saves repetitive effort, increases execution consistency and speed, and supports earlier/faster feedback."
        ),
        RawQ(
            id = "q055", ch = 6,
            q = "أي مما يلي يُعد مخاطرة معروفة (Risk) عند اعتماد أدوات أتمتة الاختبار؟",
            correct = "توقعات مبالغ فيها وإغفال تكلفة الصيانة",
            wrong = listOf(
                "زيادة سرعة تنفيذ اختبارات الانحدار في خطوط التكامل المستمر CI/CD",
                "تقليل أخطاء النسخ البشري أثناء فحص مخرجات البيانات الضخمة",
                "إمكانية إعادة تشغيل نفس التحقق الآلي ليلاً على عدة متصفحات"
            ),
            exp = "المخاطر تشمل توقعات مبالغاً فيها، التقليل من تكلفة الصيانة، إهمال الاختبار اليدوي والاستكشافي، والاعتماد المفرط على الأداة.",
            qEn = "Which of the following is a common RISK associated with adopting test automation?",
            correctEn = "Unrealistic expectations and maintenance costs",
            wrongEn = listOf(
                "Faster feedback cycles when regression suites run inside CI/CD pipelines",
                "Reduced human clerical error when checking large complex data outputs",
                "Capability to execute identical checks unattended across browsers"
            ),
            expEn = "Section 6.2 highlights unrealistic expectations, underestimated time/cost to maintain testware, over-reliance on tools, and vendor risk."
        ),
        RawQ(
            id = "q056", ch = 6,
            q = "أداة مثل Selenium أو Playwright تُصنَّف أساساً ضمن فئة:",
            correct = "Test execution and coverage tools",
            wrong = listOf("Static testing & review tools", "Test planning & management tools", "Deployment containerization tools only"),
            exp = "أدوات مثل Selenium وPlaywright تنفذ حالات الاختبار الآلية على مستوى الـ UI وتدعم أتمتة التنفيذ.",
            qEn = "In CTFL v4.0's tool classification, tools such as Selenium or Playwright primarily belong to which category?",
            correctEn = "Test execution and coverage tools",
            wrongEn = listOf("Static testing & analysis tools", "Test planning & management tools", "Configuration versioning databases only"),
            expEn = "Section 6.1 places automated test execution runners (Selenium, Playwright, JUnit) under test execution and coverage tools."
        ),
        RawQ(
            id = "q057", ch = 6,
            q = "أداة SonarQube التي تفحص الكود لاكتشاف الـ bugs المحتملة وانتهاك المعايير دون تشغيله تصنَّف ضمن:",
            correct = "Static testing tools",
            wrong = listOf("Dynamic performance tools", "Test management dashboards", "Deployment container tools"),
            exp = "SonarQube تجري تحليلاً ساكناً للشيفرة دون تنفيذها، لذلك هي أداة Static testing.",
            qEn = "An automated analyzer such as SonarQube that inspects source code for potential defects and standard violations without executing it falls into which category?",
            correctEn = "Static testing tools",
            wrongEn = listOf("Non-functional load testing tools", "Test management dashboards", "Scalability and deployment standardization tools"),
            expEn = "Static testing tools assist reviews and perform static code/data-flow analysis without running the software."
        ),
        RawQ(
            id = "q092", ch = 6,
            q = "في بيئة DevOps مع CI/CD، ما الدور الأهم الذي تؤديه أدوات أتمتة الاختبار وتنسيق البناء (مثل GitHub Actions وJenkins)؟",
            correct = "تشغيل الاختبارات مع كل commit لتسريع التغذية الراجعة",
            wrong = listOf(
                "إلغاء دور المختبر البشري تماماً واستبداله بشبكة تقارير صماء لا تراقَب",
                "تخزين بيانات المستخدمين الإنتاجية دون تشفير في مستودع الشيفرة",
                "منع كتابة أي اختبارات وحدة سريعة لصالح الفحص اليدوي فقط في الإنتاج"
            ),
            exp = "في DevOps (2.1.4 & 6.1)، تدعم أدوات DevOps والأتمتة تشغيل التحققات الآلية في كل دفعة لضمان سرعة الجودة.",
            qEn = "In a DevOps CI/CD pipeline, what key value do DevOps and automated test execution tools (e.g., Jenkins, GitHub Actions) provide?",
            correctEn = "Automated tests per commit for rapid feedback",
            wrongEn = listOf(
                "Replacing human testers completely so exploratory testing vanishes",
                "Storing unencrypted live customer credentials directly inside git",
                "Blocking fast unit tests in favor of purely manual release runs"
            ),
            expEn = "DevOps infrastructure tools automate build, deployment, and regression checks per commit, accelerating reliable feedback."
        ),
        RawQ(
            id = "q093", ch = 6,
            q = "أنشأ الفريق 500 اختبار UI آلي هشة، ومع كل تغيير طفيف في التصميم يتعطل نصفها وتُستنفد ساعات الأسبوع في إصلاح محددات DOM. أي مخاطرة أتمتة تجسّدت هنا؟",
            correct = "التقليل من تكلفة صيانة السكربتات الآلية",
            wrong = listOf(
                "نقص دعم المعايير الدولية لترجمة واجهة المستخدم متعددة اللغات",
                "ارتفاع فائض وقت الفريق بحيث لم يجد المبرمجون مهام جديدة لإنجازها",
                "اختفاء جميع عيوب الأداء في قاعدة البيانات الإنتاجية بالكامل"
            ),
            exp = "أكبر مخاطر أتمتة واجهات المستخدم المفرطة إغفال تكلفة الصيانة الدورية عند تغيّر الـ UI (Test pyramid يعالج ذلك).",
            qEn = "A team builds 500 brittle UI tests. After minor layout updates, half fail due to changed DOM locators and engineers spend days fixing scripts. Which automation risk materialized?",
            correctEn = "Underestimating ongoing testware maintenance cost",
            wrongEn = listOf(
                "Insufficient multi-language localization support in static analyzers",
                "Elimination of all performance bottlenecks in production databases",
                "Excess developer free time resulting from over-simplified work"
            ),
            expEn = "Section 6.2 warns that teams frequently underestimate testware maintenance costs, especially for fragile UI-level automation."
        ),
        RawQ(
            id = "q094", ch = 6,
            q = "يحتاج الفريق لمحاكاة 5,000 مستخدم متزامن لقياس زمن الاستجابة واختناق الذاكرة في الخادم. أي فئة أدوات هي الأنسب؟",
            correct = "Non-functional testing tools (أدوات الأداء والحمل)",
            wrong = listOf(
                "Static analysis linters لفحص الأسلوب البرمجي مثل SonarQube",
                "Defect tracking boards لإدارة تذاكر المهام مثل Jira فقط",
                "User story card templates الورقية والرقمية البسيطة"
            ),
            exp = "أدوات الأداء تقيس مقاييس يستحيل اختبارها يدوياً مثل الأحمال الضخمة والتزامن والاستقرار تحت الضغط.",
            qEn = "A team must simulate 5,000 concurrent users to measure server latency and memory bottlenecks. Which tool category is MOST appropriate?",
            correctEn = "Non-functional performance tools",
            wrongEn = listOf(
                "Static code quality and style linters",
                "Defect and workflow tracking boards alone",
                "Manual test case checklist spreadsheets"
            ),
            expEn = "Non-functional performance tools generate heavy concurrent workloads and collect latency/resource metrics infeasible to gather manually."
        ),
        RawQ(
            id = "q095", ch = 6,
            q = "في تصنيف أدوات الاختبار وفق منهج CTFL v4.0 (القسم 6.1)، أي فئة تشمل الآلات الافتراضية (VMs) والحاويات (Containers) لضمان بيئات اختبار متطابقة؟",
            correct = "Scalability and deployment standardization",
            wrong = listOf(
                "Static testing tools (أدوات الاختبار الساكن للشيفرة)",
                "Test design and data generation tools (أدوات التصميم)",
                "Collaboration and communication tools (أدوات التواصل)"
            ),
            exp = "القسم 6.1 في CTFL v4.0 يذكر فئة Scalability and deployment standardization (مثل Virtual machines والحاويات Containers) لتوفير بيئات معزولة وقابلة للتكرار.",
            qEn = "In CTFL v4.0 Section 6.1, which tool category explicitly covers virtual machines (VMs) and containers used to provide consistent, repeatable test environments?",
            correctEn = "Scalability and deployment tools",
            wrongEn = listOf(
                "Static testing and code review suites",
                "Test design and synthetic data generation tools",
                "Team collaboration and messaging applications"
            ),
            expEn = "CTFL v4.0 Section 6.1 includes 'Scalability and deployment standardization' (e.g., virtual machines, containerization tools) to standardize environments."
        ),
        RawQ(
            id = "q119", ch = 6,
            q = "اعتمد الفريق على أداة أتمتة مفتوحة المصدر غير مدعومة، ثم توقف مطوروها عن تحديثها مع إصدارات المتصفحات الجديدة وتعذّر ترحيل السكربتات بسهولة. أي مخاطرة في القسم 6.2 تجسّدت؟",
            correct = "مخاطر المورد/الأداة (التوقف أو صعوبة الترحيل)",
            wrong = listOf(
                "تحقيق تغطية كاملة لفروع الكود دون صيانة مستمرة",
                "زيادة تفاعل الفريق البشري في جلسات الاختبار الاستكشافي",
                "تسريع مراجعات المتطلبات الساكنة المبكرة في الفريق"
            ),
            exp = "القسم 6.2 يدرج مخاطر استمرارية المورد أو المشروع مفتوح المصدر (التوقف، غياب التحديثات الأمنية، وصعوبة الترحيل Vendor lock-in).",
            qEn = "A team adopted an unmaintained open-source automation tool that later stopped receiving browser compatibility updates, locking in their test scripts. Which CTFL v4.0 Section 6.2 risk occurred?",
            correctEn = "Tool/vendor lock-in or discontinuation risk",
            wrongEn = listOf(
                "Achieving 100% branch coverage without needing maintenance",
                "Increased human creativity during exploratory test sessions",
                "Faster static reviews of natural-language specifications"
            ),
            expEn = "Section 6.2 highlights tool dependency risks, including vendor shutdown, open-source project abandonment, missing updates, and vendor lock-in."
        ),
        RawQ(
            id = "q120", ch = 6,
            q = "بعد نجاح أتمتة فحوصات النظام الرئيسية، أوقف الفريق جلسات الاختبار الاستكشافي البشري ظناً أن الأداة تكشف كل العيوب الممكنة، فتسرّبت مشاكل تجربة مستخدم حرجة للإنتاج. أي مخاطرة وقعت؟",
            correct = "الاعتماد المفرط على الأداة وإهمال الاختبار البشري",
            wrong = listOf(
                "عدم كفاية سرعة خطوط التشغيل الآلي المستمر CI/CD في الخادم",
                "استخدام حاويات Containers لتوحيد بيئات التشغيل المتطابقة",
                "صغر حجم اختبارات الوحدة في قاعدة هرم الاختبار القياسي"
            ),
            exp = "الأتمتة تتحقق مما برمجناه لفحصه فقط (Oracle problem)، لكنها لا تعوض التفكير النقدي والإبداع البشري في الاختبار الاستكشافي.",
            qEn = "Assuming their automated suite catches everything, a team stops human exploratory testing and subsequently misses major usability flaws in production. Which automation risk does this show?",
            correctEn = "Over-reliance on tools over human testing",
            wrongEn = listOf(
                "Slow continuous integration execution speeds across tests",
                "Using standardized container images to mirror staging setups",
                "Building too many fast component tests at the pyramid base"
            ),
            expEn = "Section 6.2 warns against over-relying on automation; tools only check scripted expectations and cannot replace human observation and critical thinking."
        )
    )
}
