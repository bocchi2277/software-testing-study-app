package com.example.data

data class Topic(
    val id: String,
    val en: String,
    val ar: String,
    val minutes: Int,
    val points: List<String>,
    val objectives: List<String>,
    val tip: String? = null,
    val chapter: Int = 0
)

data class Chapter(
    val id: Int,
    val en: String,
    val ar: String,
    val short: String? = null,
    val examQ: Int,
    val minutes: Int,
    val topics: List<Topic>
)

object SyllabusData {
    val CHAPTERS: List<Chapter> = listOf(
        Chapter(
            id = 1,
            en = "Fundamentals of Testing",
            ar = "أساسيات الاختبار (Fundamentals of Testing)",
            examQ = 8,
            minutes = 180,
            topics = listOf(
                Topic(
                    id = "1.1",
                    en = "What is Testing?",
                    ar = "ما هو الاختبار؟ (What is Testing?)",
                    minutes = 40,
                    points = listOf(
                        "الاختبار (Testing) مجموعة أنشطة لاكتشاف الـ defects وتقييم جودة المنتج، وليس مجرد تشغيل البرنامج. يشمل Dynamic testing (تنفيذ الكود) وStatic testing (بلا تنفيذ).",
                        "أهداف الاختبار النموذجية: تقييم work products (متطلبات، تصميم، كود)، إثارة Failures واكتشاف الـ defects، التأكد من الـ coverage، تقليل مستوى المخاطر، التحقق من المتطلبات، التحقق من الامتثال للعقود والقوانين، تقديم معلومات لـ stakeholders، وبناء الثقة.",
                        "الاختبار ≠ Debugging. الاختبار يُظهر الـ failures، أما Debugging فيجد سبب الـ defect (Root cause) ويحلله ويصلحه. بعد الإصلاح يأتي Confirmation testing للتأكد من أنه زال.",
                        "الاختبار قد يكون Verification (هل بنينا المنتج صحيحاً وفق المواصفات؟) أو Validation (هل بنينا المنتج الصحيح الذي يريده المستخدم؟)."
                    ),
                    objectives = listOf(
                        "K1: تعرّف الأهداف النموذجية للاختبار.",
                        "K2: ميّز بين Testing وDebugging."
                    ),
                    tip = "في الامتحان: Debugging لا يُعتبر نشاط اختبار، هو نشاط تطوير.",
                    chapter = 1
                ),
                Topic(
                    id = "1.2",
                    en = "Why is Testing Necessary?",
                    ar = "لماذا الاختبار ضروري؟ (Why is Testing Necessary?)",
                    minutes = 35,
                    points = listOf(
                        "الاختبار يقلل مخاطر الـ failure أثناء التشغيل ويساهم في جودة المنتج، ويحقق المتطلبات التعاقدية أو القانونية أو المعايير الصناعية.",
                        "Quality Management تشمل QA (Quality Assurance) وQC (Quality Control). QA موجّه للعملية ووقائي (يمنع الـ defects)، أما الاختبار فهو من أنشطة QC وموجّه للمنتج (يكتشف الـ defects). QA الجيد يدعم الاختبار الفعّال.",
                        "السلسلة السببية: Error (Mistake) بشري ← يسبب Defect (Bug/Fault) في work product ← قد يسبب Failure عند تنفيذ الكود. ليست كل الـ defects تؤدي إلى failure.",
                        "Root cause هو السبب الأساسي لحدوث الأخطاء، مثل ضغط الوقت، قلة الخبرة، سوء التواصل، تعقيد الكود، أو تقنية جديدة. تحليل الجذور يمنع تكرار الـ defects.",
                        "الـ failures قد تنتج أيضاً من ظروف بيئية (إشعاع، مجال مغناطيسي، تلوث) أو من أفعال متعمدة (مثل الاختراق)."
                    ),
                    objectives = listOf(
                        "K2: أعطِ أمثلة على سبب ضرورة الاختبار.",
                        "K2: ميّز بين الاختبار وضمان الجودة (QA).",
                        "K2: ميّز بين Root cause وError وDefect وFailure."
                    ),
                    tip = "احفظ: Error → Defect → Failure. الـ Error يصدر من الإنسان، والـ Defect في المنتج، والـ Failure يظهر عند التشغيل.",
                    chapter = 1
                ),
                Topic(
                    id = "1.3",
                    en = "Testing Principles",
                    ar = "مبادئ الاختبار السبعة (Testing Principles)",
                    minutes = 40,
                    points = listOf(
                        "1) Testing shows the presence, not the absence of defects: الاختبار يقلل احتمال بقاء defects لكنه لا يثبت خلو البرنامج منها.",
                        "2) Exhaustive testing is impossible: لا يمكن اختبار كل التركيبات والمدخلات، لذلك نستخدم تحليل المخاطر والأولويات وتقنيات الاختبار.",
                        "3) Early testing saves time and money (Shift-left): الـ defect المكتشف مبكراً أرخص في إصلاحه.",
                        "4) Defects cluster together: أغلب الـ defects تتركز في عدد قليل من الوحدات (مبدأ باريتو 80/20)، وتُستخدم في التنبؤ وتحليل المخاطر.",
                        "5) Tests wear out (Pesticide paradox): تكرار نفس الاختبارات يقلل قدرتها على كشف defects جديدة، فيجب مراجعتها وتحديثها وإضافة اختبارات جديدة.",
                        "6) Testing is context dependent: اختبار تطبيق بنكي يختلف عن لعبة أو نظام طبي.",
                        "7) Absence-of-defects fallacy: ظنّ أن إصلاح كل الـ defects ينتج نظاماً ناجحاً خطأ، فقد لا يلبي النظام حاجة المستخدم."
                    ),
                    objectives = listOf("K2: اشرح مبادئ الاختبار السبعة."),
                    tip = "أسئلة الامتحان تعطيك سيناريو وتطلب المبدأ المطابق. تدرب على ربط كل مبدأ بمثال.",
                    chapter = 1
                ),
                Topic(
                    id = "1.4",
                    en = "Test Activities, Testware and Test Roles",
                    ar = "أنشطة الاختبار والـ Testware والأدوار (Test Activities, Testware and Test Roles)",
                    minutes = 50,
                    points = listOf(
                        "أنشطة عملية الاختبار: Test planning ← Test monitoring & control ← Test analysis (ماذا نختبر؟) ← Test design (كيف نختبر؟) ← Test implementation (هل جاهزون؟) ← Test execution ← Test completion.",
                        "Test analysis: تحليل الـ test basis لتحديد test conditions والـ defects فيه. Test design: تحويل الشروط إلى Test cases وبيانات وتصميم Test environment. Test implementation: إنشاء test procedures وsuites وجدول التنفيذ وتجهيز البيئة والبيانات.",
                        "Test execution: تنفيذ الاختبارات وتسجيل النتائج ومقارنة الفعلي بالمتوقع وإبلاغ الـ defects. Test completion: إغلاق الـ defects المفتوحة وإعداد test summary report وحفظ testware ونقل Lessons learned.",
                        "Testware = مخرجات كل نشاط: Test plan، test conditions، test cases، test data، test procedures، test suites، سكربتات الأتمتة، سجلات التنفيذ (test logs)، Defect reports، وتقرير الإنجاز.",
                        "Traceability: ربط test basis ← test conditions ← test cases ← النتائج، وتفيد في تحليل الأثر والـ coverage وقياس التقدم.",
                        "الأدوار: Test management role (التخطيط والمراقبة والتحكم والإنجاز) وTesting role (التحليل والتصميم والتنفيذ). قد يجمع شخص واحد الدورين."
                    ),
                    objectives = listOf(
                        "K2: لخّص أنشطة الاختبار المختلفة ومهامها.",
                        "K2: ميّز بين work products المرتبطة بالاختبار.",
                        "K2: اشرح قيمة الحفاظ على Traceability.",
                        "K2: قارن بين أدوار الاختبار."
                    ),
                    chapter = 1
                ),
                Topic(
                    id = "1.5",
                    en = "Essential Skills and Good Practices in Testing",
                    ar = "المهارات الأساسية وأفضل الممارسات (Essential Skills and Good Practices in Testing)",
                    minutes = 35,
                    points = listOf(
                        "المهارات العامة للمختبر: معرفة الاختبار، الدقة والالتزام بالتفاصيل، الفضول والتفكير الإبداعي، مهارات التواصل، التفكير التحليلي، المعرفة التقنية، ومعرفة المجال (Domain).",
                        "الإبلاغ عن الـ defects يحتاج لياقة: نقد المنتج لا الشخص. عقلية المختبر تختلف عن المطور (تقييم المنتج بحثاً عن مشاكل).",
                        "Whole-team approach: الفريق كله مسؤول عن الجودة والتعاون مع stakeholders، شائع في Agile.",
                        "Independence of testing: كلما زاد استقلال المختبر عن المطور زادت فرصة اكتشاف defects لا يراها المؤلف بسبب التحيز المعرفي، لكن الاستقلال التام قد يسبب عزلة وتأخر تواصل.",
                        "من الممارسات الجيدة: كل نشاط تطوير له نشاط اختبار مقابل، ومراجعة المتطلبات مبكراً، وتوثيق ما يلزم."
                    ),
                    objectives = listOf(
                        "K1: أعطِ أمثلة على المهارات العامة اللازمة للاختبار.",
                        "K2: تذكّر مزايا Whole-team approach.",
                        "K2: ميّز فوائد ومساوئ استقلال الاختبار."
                    ),
                    chapter = 1
                )
            )
        ),
        Chapter(
            id = 2,
            en = "Testing Throughout the Software Development Lifecycle",
            ar = "الاختبار عبر دورة حياة التطوير (Testing Throughout the Software Development Lifecycle)",
            short = "الاختبار عبر دورة التطوير (Testing Throughout the SDLC)",
            examQ = 6,
            minutes = 130,
            topics = listOf(
                Topic(
                    id = "2.1",
                    en = "Testing in the Context of an SDLC",
                    ar = "الاختبار ضمن نماذج دورة التطوير (Testing in the Context of an SDLC)",
                    minutes = 40,
                    points = listOf(
                        "النماذج التسلسلية (Waterfall وV-model): مراحل متتابعة، Dynamic testing غالباً متأخر. النماذج التكرارية والتزايدية (Scrum, Kanban, Spiral, Unified Process): نسخ تعمل تُسلَّم على دفعات.",
                        "ممارسات اختبار جيدة في أي SDLC: لكل نشاط تطوير نشاط اختبار مقابل، لكل مستوى اختبار أهداف محددة، يبدأ تحليل وتصميم الاختبار أثناء مرحلة التطوير المقابلة، ويشارك المختبرون في مراجعة work products مبكراً.",
                        "TDD (Test-Driven Development): اكتب الاختبار ثم الكود. ATDD: تُشتق الاختبارات من Acceptance criteria. BDD: صياغة السلوك بلغة طبيعية (Given-When-Then).",
                        "DevOps وCI/CD: تغذية راجعة سريعة، اختبارات آلية تُشغَّل مع كل تغيير، تقلل الحاجة لـ manual regression لكن تحتاج صيانة.",
                        "Shift-left: نقل أنشطة الاختبار لمراحل مبكرة (Reviews، Component testing). Retrospectives تحسّن العمليات بعد كل تكرار."
                    ),
                    objectives = listOf(
                        "K2: اشرح تأثير نموذج SDLC المختار على الاختبار.",
                        "K1: تذكّر ممارسات الاختبار الجيدة في كل SDLC.",
                        "K1: تذكّر أمثلة على نهج Test-first.",
                        "K2: لخّص تأثير DevOps على الاختبار.",
                        "K2: اشرح نهج Shift-left وRetrospectives."
                    ),
                    chapter = 2
                ),
                Topic(
                    id = "2.2",
                    en = "Test Levels and Test Types",
                    ar = "مستويات الاختبار وأنواعه (Test Levels and Test Types)",
                    minutes = 60,
                    points = listOf(
                        "المستويات (Levels): Component (Unit) testing ← Component integration ← System testing ← System integration ← Acceptance testing. كل مستوى له أهداف وtest basis وdefects نموذجية ومسؤوليات مختلفة.",
                        "Acceptance testing أشكاله: UAT (User), Operational (OAT: نسخ احتياطي، استعادة، صيانة)، Contractual and Regulatory، وAlpha (عند المطور) وBeta (عند المستخدمين الحقيقيين).",
                        "الأنواع (Types): Functional (ماذا يفعل النظام؟)، Non-functional (كيف يعمل؟ ويشمل Performance efficiency، Compatibility، Usability، Reliability، Security، Maintainability، Portability)، Black-box، White-box.",
                        "Confirmation testing (Re-testing): إعادة نفس الحالة الفاشلة بعد الإصلاح. Regression testing: التأكد أن التغيير لم يكسر ما كان يعمل، ومرشح قوي للأتمتة.",
                        "يمكن إجراء أي Test type في أي Test level: فمثلاً Performance testing ممكن على مستوى component أو system.",
                        "Functional coverage وNon-functional coverage وWhite-box coverage مقاييس لتقييم الـ coverage."
                    ),
                    objectives = listOf(
                        "K2: ميّز بين Test levels.",
                        "K2: ميّز بين Test types.",
                        "K2: ميّز بين Confirmation وRegression testing."
                    ),
                    tip = "فخ شائع: Smoke وRegression ليسا مستويات، بل أنواع/أنشطة. المستويات خمسة فقط كما سبق.",
                    chapter = 2
                ),
                Topic(
                    id = "2.3",
                    en = "Maintenance Testing",
                    ar = "اختبار الصيانة (Maintenance Testing)",
                    minutes = 30,
                    points = listOf(
                        "Maintenance testing يُجرى بعد تسليم النظام للإنتاج عند إصلاح أو تعديل أو ترحيل أو إيقاف النظام.",
                        "المحفزات (Triggers): Modification (تحسينات، إصلاحات، ترقية بيئة)، Migration (من منصة لأخرى)، Retirement (إيقاف النظام: ترحيل البيانات وأرشفتها).",
                        "Impact analysis: تقييم أثر التغيير على النظام الحالي لتحديد ما يجب إعادة اختباره (regression)، ويصعب عندما تكون الوثائق قديمة أو غائبة أو لا توجد traceability.",
                        "نطاق Maintenance testing يعتمد على: درجة مخاطر التغيير، حجم النظام الحالي، وحجم التغيير."
                    ),
                    objectives = listOf("K2: لخّص Maintenance testing ومحفزاته."),
                    chapter = 2
                )
            )
        ),
        Chapter(
            id = 3,
            en = "Static Testing",
            ar = "الاختبار الساكن (Static Testing)",
            examQ = 4,
            minutes = 80,
            topics = listOf(
                Topic(
                    id = "3.1",
                    en = "Static Testing Basics",
                    ar = "أساسيات الاختبار الساكن (Static Testing Basics)",
                    minutes = 40,
                    points = listOf(
                        "Static testing لا ينفذ الكود: Reviews (يدوية) وStatic analysis (بأدوات). يطبق على أي work product: متطلبات، تصميم، كود، خطط، user stories.",
                        "قيمته: اكتشاف الـ defects مبكراً (Shift-left)، تحسين جودة المتطلبات، تقليل تكلفة الإصلاح، وتحسين التواصل ضمن الفريق.",
                        "الفرق بين Static وDynamic: Static يجد الـ defects مباشرة، وDynamic يجد الـ failures الناتجة عنها.",
                        "الـ defects الأسهل اكتشافاً بـ Static: متطلبات غير متسقة أو غامضة، انتهاك المعايير، الكود الذي لا يمكن الوصول إليه (unreachable)، تصميم غير سليم، فجوات تغطية المتطلبات. أما مشاكل الأداء وتأثيرات التشغيل فتكشفها Dynamic."
                    ),
                    objectives = listOf(
                        "K1: تعرّف أنواع work products التي تُفحص بـ Static testing.",
                        "K2: اشرح قيمة Static testing.",
                        "K2: قارن بين Static testing وDynamic testing."
                    ),
                    chapter = 3
                ),
                Topic(
                    id = "3.2",
                    en = "Feedback and Review Process",
                    ar = "التغذية الراجعة وعملية المراجعة (Feedback and Review Process)",
                    minutes = 40,
                    points = listOf(
                        "التغذية الراجعة المبكرة والمتكررة من stakeholders تمنع سوء فهم المتطلبات وتقلل إعادة العمل.",
                        "أنشطة Review process: Planning ← Review initiation ← Individual review ← Communication & analysis ← Fixing & reporting.",
                        "الأدوار: Manager (يقرر ما يُراجع ويوفر الموارد)، Author (المؤلف)، Moderator/Facilitator (يدير الاجتماع)، Scribe (يسجل)، Reviewers، Review leader (مسؤول عام).",
                        "أنواع الـ Reviews: Informal (بلا عملية رسمية)، Walkthrough (يقوده المؤلف)، Technical review (يقوم بها خبراء تقنيون)، Inspection (الأكثر رسمية: أدوار ومقاييس وقوائم فحص).",
                        "عوامل النجاح: أهداف واضحة، اختيار النوع المناسب، تقسيم المراجعة لأجزاء صغيرة، توفير تغذية راجعة، وقت كافٍ، دعم الإدارة، ثقافة بلا لوم."
                    ),
                    objectives = listOf(
                        "K1: تذكّر فوائد التغذية الراجعة المبكرة.",
                        "K2: لخّص أنشطة Review process.",
                        "K1: تذكّر الأدوار والمسؤوليات في المراجعة.",
                        "K2: قارن بين أنواع الـ Reviews.",
                        "K1: تذكّر عوامل نجاح المراجعة."
                    ),
                    tip = "Inspection = الأكثر رسمية وهدفها اكتشاف الـ defects وجمع المقاييس. Informal = الأقل رسمية.",
                    chapter = 3
                )
            )
        ),
        Chapter(
            id = 4,
            en = "Test Analysis and Design",
            ar = "تحليل وتصميم الاختبار (Test Analysis and Design)",
            examQ = 11,
            minutes = 330,
            topics = listOf(
                Topic(
                    id = "4.1",
                    en = "Test Techniques Overview",
                    ar = "نظرة عامة على تقنيات الاختبار (Test Techniques Overview)",
                    minutes = 20,
                    points = listOf(
                        "Black-box techniques (مبنية على المواصفات): Equivalence Partitioning، Boundary Value Analysis، Decision Table، State Transition. لا تحتاج معرفة بنية الكود.",
                        "White-box techniques (مبنية على البنية الداخلية): Statement testing وBranch testing.",
                        "Experience-based techniques: Error guessing، Exploratory testing، Checklist-based testing. تعتمد على خبرة المختبر وتكمّل التقنيات الأخرى.",
                        "اختيار التقنية يعتمد على: نوع المكوّن، المخاطر، الوثائق المتاحة، خبرة الفريق، الوقت والميزانية."
                    ),
                    objectives = listOf("K2: ميّز بين Black-box وWhite-box والتقنيات القائمة على الخبرة."),
                    chapter = 4
                ),
                Topic(
                    id = "4.2.1",
                    en = "Equivalence Partitioning (EP)",
                    ar = "تقسيم التكافؤ (Equivalence Partitioning)",
                    minutes = 40,
                    points = listOf(
                        "الفكرة: نقسم المدخلات إلى أقسام (Partitions) يُفترض أن عناصر القسم الواحد تُعامَل بنفس الطريقة، فنختبر قيمة واحدة من كل قسم.",
                        "أقسام صالحة (Valid) وغير صالحة (Invalid). مثال: عمر مقبول 18–60: (أقل من 18) غير صالح، (18–60) صالح، (أكثر من 60) غير صالح = 3 أقسام.",
                        "Coverage = عدد الأقسام المختبرة ÷ إجمالي الأقسام. للحصول على 100% يلزم اختبار قيمة واحدة على الأقل من كل قسم.",
                        "في الاختبار السلبي يجب أن تحتوي كل Test case على قيمة غير صالحة واحدة فقط، لتجنب إخفاء defect بآخر (Defect masking).",
                        "ينطبق أيضاً على المخرجات والقيم الزمنية والواجهات (APIs)."
                    ),
                    objectives = listOf("K3: استخدم Equivalence Partitioning لاشتقاق Test cases."),
                    tip = "جرّب مولّد EP/BVA في صفحة «المختبر» للتدرب بسرعة.",
                    chapter = 4
                ),
                Topic(
                    id = "4.2.2",
                    en = "Boundary Value Analysis (BVA)",
                    ar = "تحليل القيم الحدية (Boundary Value Analysis)",
                    minutes = 40,
                    points = listOf(
                        "BVA امتداد للـ EP: الـ defects تتجمع عند حدود الأقسام. يُطبَّق على الأقسام المرتبة (رقمية أو متسلسلة).",
                        "2-value BVA: لكل حد نختبر قيمة الحد وأقرب قيمة في القسم المجاور. لمجال 1–100: القيم 0 و1 و100 و101.",
                        "3-value BVA: لكل حد نختبر قيمة الحد والقيمتين المجاورتين له. لمجال 1–100: القيم 0 و1 و2 و99 و100 و101. أكثر تفصيلاً (يكشف defects أكثر).",
                        "Coverage = عدد Boundary values المختبرة ÷ إجمالي Boundary values المشتقة.",
                        "الحدود في الأعداد العشرية تعتمد على الدقة المسموحة (مثلاً خانتان عشريتان: 0.99 و1.00 و1.01)."
                    ),
                    objectives = listOf("K3: استخدم Boundary Value Analysis لاشتقاق Test cases."),
                    tip = "تذكر: 2-value لمجال [a,b] = a-1, a, b, b+1. و3-value تضيف a+1 وb-1.",
                    chapter = 4
                ),
                Topic(
                    id = "4.2.3",
                    en = "Decision Table Testing",
                    ar = "اختبار جدول القرار (Decision Table Testing)",
                    minutes = 40,
                    points = listOf(
                        "مناسب لاختبار قواعد العمل المركبة حيث تعتمد النتيجة على تركيبة شروط (Conditions). يوضّح التركيبات والنتائج (Actions).",
                        "الجدول: الصفوف العليا شروط، السفلى إجراءات، وكل عمود Rule. القيم: T/F أو Y/N، و«-» للشرط غير المهم (Irrelevant). في الجدول الكامل: 2^n قاعدة لـ n شرط ثنائي.",
                        "الجدول المطوي (Collapsed) يدمج قواعد تعطي نفس النتيجة بغض النظر عن شرط ما، فيقل عدد الحالات.",
                        "Coverage: عدد القواعد المختبرة ÷ عدد القواعد الممكنة (أو المجدية). للحصول على 100% نختبر كل قاعدة ممكنة.",
                        "قيمته: يكشف تركيبات شروط منسية ونقص أو تناقض في المتطلبات."
                    ),
                    objectives = listOf("K3: استخدم Decision Table Testing لاشتقاق Test cases."),
                    chapter = 4
                ),
                Topic(
                    id = "4.2.4",
                    en = "State Transition Testing",
                    ar = "اختبار انتقال الحالات (State Transition Testing)",
                    minutes = 40,
                    points = listOf(
                        "State transition diagram: حالات (States)، انتقالات (Transitions)، أحداث (Events)، شروط حماية (Guard conditions)، وإجراءات (Actions). الصيغة: event [guard] / action.",
                        "نماذج مناسبة: بوابة الدفع، حالات الطلب (Order)، ATM، حالات تذكرة الـ defect.",
                        "جدول الحالات (State table) يعرض كل تركيبة (حالة، حدث) بما فيها الانتقالات غير الصالحة (Invalid) التي نختبر عدم حدوثها.",
                        "مستويات الـ coverage: All states coverage (زيارة كل حالة)، Valid transitions coverage (0-switch: كل انتقال صالح)، All transitions coverage (الصالحة وغير الصالحة).",
                        "تحقيق 100% All transitions coverage يعني تلقائياً تحقيق Valid transitions coverage وAll states coverage."
                    ),
                    objectives = listOf("K3: استخدم State Transition Testing لاشتقاق Test cases."),
                    tip = "All transitions ⊃ Valid transitions ⊃ All states (من الأقوى إلى الأضعف).",
                    chapter = 4
                ),
                Topic(
                    id = "4.3",
                    en = "White-box Test Techniques",
                    ar = "اختبار الصندوق الأبيض (White-box Test Techniques)",
                    minutes = 50,
                    points = listOf(
                        "Statement testing: تغطي الأوامر التنفيذية (Statements). Statement coverage = الأوامر المنفذة ÷ إجمالي الأوامر القابلة للتنفيذ.",
                        "Branch testing: تغطي الفروع (نتيجة True وFalse لكل قرار، والحلقات). Branch coverage = الفروع المنفذة ÷ إجمالي الفروع.",
                        "100% Branch coverage تعني 100% Statement coverage، والعكس غير صحيح. لذلك Branch أقوى.",
                        "حتى مع 100% coverage قد تبقى defects (مثل متطلبات منسية أو منطق ناقص)، لأن Code coverage لا تكشف ما لم يُكتب أصلاً.",
                        "قيمة White-box: تأخذ التنفيذ كله في الحسبان، فتكشف الـ defects حتى مع مواصفات غامضة أو قديمة، وتدعم Static testing (مراجعة الكود)."
                    ),
                    objectives = listOf(
                        "K2: اشرح Statement coverage.",
                        "K2: اشرح Branch coverage.",
                        "K2: اشرح قيمة White-box testing."
                    ),
                    tip = "جرّب محاكي الـ coverage في صفحة «المختبر» لترى الفرق بين Statement وBranch بنفسك.",
                    chapter = 4
                ),
                Topic(
                    id = "4.4",
                    en = "Experience-based Test Techniques",
                    ar = "التقنيات القائمة على الخبرة (Experience-based Test Techniques)",
                    minutes = 40,
                    points = listOf(
                        "Error guessing: تخمين الأخطاء المحتملة بناءً على الخبرة (مثل القسمة على صفر، حقول فارغة، رموز خاصة)، وتُنظَّم عبر Fault attacks وقوائم defects سابقة.",
                        "Exploratory testing: تصميم وتنفيذ وتقييم متزامن للاختبارات مع التعلم عن النظام، وغالباً ضمن Session-based testing بـ Test charter محدد الوقت. مناسب عند نقص التوثيق وضيق الوقت وتكميل الاختبار الرسمي.",
                        "Checklist-based testing: اختبارات مبنية على قائمة بنود (من الخبرة، أو معايير Usability، أو أسباب فشل سابقة). القوائم يجب أن تُحدَّث ولا تكون تفصيلية جداً.",
                        "تغطيتها غير محددة رقمياً كالتقنيات الأخرى، لذا تعتمد على الخبرة ويجب توثيق ما تم."
                    ),
                    objectives = listOf(
                        "K2: اشرح Error guessing.",
                        "K2: اشرح Exploratory testing.",
                        "K2: اشرح Checklist-based testing."
                    ),
                    chapter = 4
                ),
                Topic(
                    id = "4.5",
                    en = "Collaboration-based Test Approaches",
                    ar = "نهج الاختبار القائم على التعاون (Collaboration-based Test Approaches)",
                    minutes = 60,
                    points = listOf(
                        "User story: يتكون من 3Cs: Card (الوصف)، Conversation (النقاش)، Confirmation (Acceptance criteria). صيغة: As a [role], I want [goal], so that [benefit]. وتتحقق جودته بمعايير INVEST.",
                        "Acceptance criteria: شروط تحقق المنتج ليُقبل. تُكتب بصيغ مثل Given/When/Then (Scenario-oriented) أو قواعد (Rule-oriented) وتفيد في تحديد نطاق الـ story.",
                        "المختبر يساهم في صياغة المعايير مبكراً بأسئلة مثل «ماذا لو...؟» ويكشف الغموض.",
                        "ATDD (Acceptance Test-Driven Development): نهج Test-first تشتق فيه الاختبارات من acceptance criteria قبل التنفيذ، بتعاون Three Amigos (Business، Developer، Tester).",
                        "ATDD يخلق فهماً مشتركاً ويقلل الـ defects في المتطلبات ويحوّل المعايير لاختبارات قابلة للتنفيذ."
                    ),
                    objectives = listOf(
                        "K2: اشرح كيف تكتب user stories بتعاون المطورين وممثلي الأعمال.",
                        "K2: صنّف الأنواع المختلفة لـ acceptance criteria.",
                        "K3: استخدم ATDD لاشتقاق Test cases."
                    ),
                    chapter = 4
                )
            )
        ),
        Chapter(
            id = 5,
            en = "Managing the Test Activities",
            ar = "إدارة أنشطة الاختبار (Managing the Test Activities)",
            examQ = 9,
            minutes = 335,
            topics = listOf(
                Topic(
                    id = "5.1",
                    en = "Test Planning",
                    ar = "تخطيط الاختبار (Test Planning)",
                    minutes = 70,
                    points = listOf(
                        "Test plan يوثق الأهداف والنطاق (Scope) والنهج والموارد والجدول والمخاطر. أنواعه: Master test plan وLevel test plan، وتتحدّث باستمرار.",
                        "Entry criteria (جاهزية البدء: توفر البيئة والبيانات والكود المبني) وExit criteria / Definition of Done (متى ننهي: الـ coverage المطلوبة، عدد الـ defects المفتوحة أقل من حد، تقارير جاهزة).",
                        "تقدير الجهد: Ratio-based (نسب من مشاريع سابقة)، Extrapolation (بيانات من تكرارات حالية)، Wideband Delphi (خبراء)، Three-point estimation: E = (a + 4m + b) / 6 حيث a متفائل وm محتمل وb متشائم.",
                        "ترتيب الأولويات: Risk-based، Coverage-based، Requirements-based. ويجب مراعاة الاعتماديات بين الاختبارات.",
                        "Test pyramid: كثير من اختبارات الوحدة (سريعة)، أقل للتكامل، وأقل للـ UI (بطيئة). Testing quadrants (Agile): Q1 تقنية تدعم الفريق، Q2 أعمال تدعم الفريق، Q3 أعمال تنتقد المنتج، Q4 تقنية تنتقد المنتج."
                    ),
                    objectives = listOf(
                        "K2: اشرح الغرض والمحتوى لـ Test plan.",
                        "K1: تعرّف كيف يساهم المختبر في التخطيط للتكرارات.",
                        "K2: ميّز بين Entry وExit criteria.",
                        "K3: استخدم تقنيات التقدير لحساب الجهد.",
                        "K3: طبّق ترتيب أولوية Test cases.",
                        "K1: تذكّر مفاهيم Test pyramid.",
                        "K2: لخّص Testing quadrants."
                    ),
                    tip = "سؤال حسابي محتمل: Three-point. مثال a=2, m=5, b=14 → (2+20+14)/6 = 6.",
                    chapter = 5
                ),
                Topic(
                    id = "5.2",
                    en = "Risk Management",
                    ar = "إدارة المخاطر (Risk Management)",
                    minutes = 60,
                    points = listOf(
                        "المخاطرة = احتمال حدوث حدث مستقبلي ضار (Likelihood) × أثره (Impact). مستوى المخاطرة يحدد أولويات الاختبار.",
                        "Project risks (تتعلق بإدارة المشروع: تأخر التسليم، نقص موارد، تغير المتطلبات، مشاكل الفريق) وProduct risks (تتعلق بجودة المنتج: وظيفة خاطئة، أداء ضعيف، ثغرة أمنية، تجربة مستخدم سيئة).",
                        "Risk analysis: تحديد المخاطر (Identification) ثم تقييمها (Assessment: تصنيف، احتمال، أثر، أولوية).",
                        "Risk control: Risk mitigation (تخفيف عبر الاختبار وإجراءات أخرى)، Risk monitoring، وخيارات: قبول المخاطرة، نقلها (تأمين/طرف ثالث)، خطة طوارئ (Contingency).",
                        "Risk-based testing: الاختبار الأعمق للمجالات الأعلى خطراً، ويُحدَّث مع تغير المخاطر."
                    ),
                    objectives = listOf(
                        "K1: اعرّف مستوى المخاطرة بالاحتمال والأثر.",
                        "K2: ميّز بين Project risks وProduct risks.",
                        "K3: اشرح كيف يؤثر تحليل Product risk في الاختبار.",
                        "K2: اشرح إجراءات الاستجابة للمخاطر."
                    ),
                    chapter = 5
                ),
                Topic(
                    id = "5.3",
                    en = "Test Monitoring, Test Control and Test Completion",
                    ar = "المراقبة والتحكم والإنجاز (Test Monitoring, Test Control and Test Completion)",
                    minutes = 40,
                    points = listOf(
                        "Monitoring: جمع معلومات لتقييم التقدم مقابل الخطة وExit criteria. Control: اتخاذ إجراءات تصحيحية (إعادة ترتيب أولويات، تعديل الجدول، إضافة موارد).",
                        "المقاييس الشائعة: نسبة Test cases المنفذة/الناجحة/الفاشلة، كثافة الـ defects (Defect density)، تغطية المتطلبات والكود، التكلفة، الجهد، وعدد الـ defects المفتوحة والمغلقة.",
                        "Test progress report (دوري) وTest completion report (عند الإنجاز): ملخص الاختبار، الانحرافات عن الخطة، العوائق، المقاييس، المخاطر المتبقية والـ defects المفتوحة، التوصيات.",
                        "Test completion يشمل: التأكد من إغلاق الـ defects أو توثيق المتبقي، أرشفة testware، تسليمه لفرق الصيانة، تحليل Lessons learned وتحسين النضج.",
                        "يختلف الإبلاغ حسب الجمهور (إداري يحتاج ملخصاً، تقني يحتاج تفاصيل)."
                    ),
                    objectives = listOf(
                        "K1: تذكّر المقاييس المستخدمة للاختبار.",
                        "K2: لخّص غرض ومحتوى وجمهور تقارير الاختبار.",
                        "K2: ميّز بين نشاطي المراقبة والتحكم."
                    ),
                    chapter = 5
                ),
                Topic(
                    id = "5.4",
                    en = "Configuration Management",
                    ar = "إدارة التهيئة (Configuration Management)",
                    minutes = 30,
                    points = listOf(
                        "Configuration Management (CM) تتحكم في identification وversion control لعناصر التهيئة (CIs): كود، testware، متطلبات، بيئات.",
                        "يضمن تتبع النسخ والعلاقات بين العناصر (Traceability) وإمكانية إعادة إنتاج الاختبار على نفس الإصدار (Baselines).",
                        "تُحدد في مرحلة التخطيط، وتتكامل مع CI/CD لبناء ونشر وإعادة إنتاج البيئات.",
                        "فوائدها للاختبار: معرفة أي إصدار اختُبر، إدارة التغييرات، منع الفوضى بين النسخ، وتفسير سبب فشل اختبار."
                    ),
                    objectives = listOf("K2: لخّص كيف تدعم Configuration Management الاختبار."),
                    chapter = 5
                ),
                Topic(
                    id = "5.5",
                    en = "Defect Management",
                    ar = "إدارة العيوب (Defect Management)",
                    minutes = 40,
                    points = listOf(
                        "Defect management: تسجيل وتصنيف وتحليل ومتابعة الـ defects حتى الإغلاق. الـ defect يُنشأ في أي مرحلة (أثناء المراجعة أو التطوير أو الاختبار أو الاستخدام).",
                        "محتويات Defect report: معرف، عنوان وملخص، تاريخ وكاتب التقرير، عنصر الاختبار والبيئة، مرحلة SDLC، وصف الـ defect مع خطوات إعادة الإنتاج (Steps to reproduce)، النتيجة الفعلية والمتوقعة، الخطورة (Severity)، الأولوية (Priority)، الحالة، المراجع، ومرفقات (لقطات/سجلات).",
                        "Severity = أثر الـ defect التقني على النظام. Priority = إلحاح الإصلاح (قرار عمل). قد يختلفان: خطأ إملائي في الشعار (Severity منخفضة، Priority عالية).",
                        "دورة حياة الـ defect النموذجية: New ← Open/Assigned ← Fixed ← Retest/Verified ← Closed (أو Reopened / Rejected / Deferred / Duplicate).",
                        "Defect report الجيد يوفر معلومات كافية لإعادة الإنتاج والإصلاح دون أسئلة إضافية، ويكتب بحياد وبلا اتهام."
                    ),
                    objectives = listOf("K3: اكتب Defect report."),
                    tip = "تدرب في صفحة «المهارات» على قالب Defect report.",
                    chapter = 5
                )
            )
        ),
        Chapter(
            id = 6,
            en = "Test Tools",
            ar = "أدوات الاختبار (Test Tools)",
            examQ = 2,
            minutes = 20,
            topics = listOf(
                Topic(
                    id = "6.1",
                    en = "Tool Support for Testing",
                    ar = "دعم الأدوات للاختبار (Tool Support for Testing)",
                    minutes = 10,
                    points = listOf(
                        "تدعم الأدوات أنشطة الاختبار: أدوات الإدارة (إدارة الاختبار والمتطلبات والـ defects والتهيئة)، Static testing (static analysis)، التصميم والتنفيذ (توليد بيانات، BDD)، التنفيذ والـ coverage (تنفيذ آلي، coverage)، الأداء (Load, Stress)، DevOps (CI/CD)، التعاون، وأخرى (مثل الأدوات المساعدة للمستخدمين المحتاجين).",
                        "أمثلة: Jira وTestRail (إدارة)، SonarQube (ساكن)، Selenium وPlaywright وCypress (تنفيذ)، JMeter وk6 (أداء)، Postman (API)، Jenkins وGitHub Actions (CI)."
                    ),
                    objectives = listOf("K2: اشرح كيف تدعم أنواع الأدوات الاختبار."),
                    chapter = 6
                ),
                Topic(
                    id = "6.2",
                    en = "Benefits and Risks of Test Automation",
                    ar = "فوائد ومخاطر أتمتة الاختبار (Benefits and Risks of Test Automation)",
                    minutes = 10,
                    points = listOf(
                        "الفوائد: توفير الجهد في الاختبارات المتكررة، تغطية أكبر، نتائج أسرع وأكثر موضوعية واتساقاً، وتقليل الأخطاء البشرية في المهام الرتيبة.",
                        "المخاطر: توقعات غير واقعية، تقدير خاطئ للوقت والتكلفة، استخدام الأتمتة حيث الاختبار اليدوي أنسب، الاعتماد الزائد على الأداة، إهمال صيانة السكربتات، مشاكل التوافق مع بيئة الأداة.",
                        "الأتمتة لا تعني اختفاء الاختبار اليدوي: Exploratory testing وUsability testing لا يُغنيهما الآلي."
                    ),
                    objectives = listOf("K1: تذكّر فوائد ومخاطر الأتمتة."),
                    chapter = 6
                )
            )
        )
    )

    val ALL_TOPICS: List<Topic> = CHAPTERS.flatMap { it.topics }

    val EXAM_TOTAL_Q: Int = CHAPTERS.sumOf { it.examQ }

    fun findTopic(id: String): Topic? = ALL_TOPICS.find { it.id == id }

    fun findChapter(id: Int): Chapter? = CHAPTERS.find { it.id == id }

    fun chapterLabel(c: Chapter): String = "${c.id}. ${c.short ?: c.ar}"
}
