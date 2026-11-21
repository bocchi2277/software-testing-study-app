package com.example.data

data class ChapterZatona(
    val chapter: Int,
    val title: String,
    val coreSummary: List<String>,
    val keyFormulasAndDiffs: List<Pair<String, String>>,
    val examTraps: List<String>
)

object ZatonaData {
    val CHAPTER_SUMMARIES: Map<Int, ChapterZatona> = mapOf(
        1 to ChapterZatona(
            chapter = 1,
            title = "زتونة الفصل 1: أساسيات الاختبار",
            coreSummary = listOf(
                "الاختبار (Testing) ليس مجرد تشغيل الكود: يشمل Static (مراجعة وتحليل بدون تنفيذ) وDynamic (تشغيل الكود)، ويُقيّم الجودة ويكتشف العيوب.",
                "السلسلة السببية الأساسية: Error (غلط بشري) ← Defect/Bug (عيب في المنتج) ← Failure (فشل عند التشغيل).",
                "المبادئ الـ 7: يثبت وجود العيوب لا غيابها · الشامل مستحيل · المبكر أوفر · تجمّع العيوب (80/20) · Tests wear out · حسب السياق · وهم خلوّ العيوب.",
                "أنشطة الاختبار الـ 7 بالترتيب: Planning ← Monitoring & Control ← Analysis (ماذا؟) ← Design (كيف؟) ← Implementation (تجهيز) ← Execution ← Completion."
            ),
            keyFormulasAndDiffs = listOf(
                "Testing vs Debugging" to "الاختبار يُظهر الفشل؛ Debugging يحلل السبب ويُصلحه (نشاط تطوير)، ثم Confirmation testing للتأكد.",
                "QA vs QC" to "QA وقائي يركّز على العملية (Process)؛ QC يركّز على المنتج (Product) ويشمل الاختبار.",
                "Verification vs Validation" to "Verification: هل بنينا المنتج صح؟ Validation: هل بنينا المنتج الصحيح للعميل؟",
                "مسمى v4.0" to "المبدأ الخامس أصبح Tests wear out (بدلاً من Pesticide paradox فقط في v3.1)."
            ),
            examTraps = listOf(
                "Debugging لا يُعدّ أبداً جزءاً من أنشطة الاختبار.",
                "ليس كل Defect يسبب Failure عند التشغيل (قد يكون في مسار نادر أو شاشة لا تُستخدم).",
                "Whole-team approach يعني مشاركة الجميع في الجودة، لكن استقلال المختبر يقلل التحيز المعرفي."
            )
        ),
        2 to ChapterZatona(
            chapter = 2,
            title = "زتونة الفصل 2: الاختبار عبر SDLC",
            coreSummary = listOf(
                "في النماذج التسلسلية (Waterfall/V-model) يتأخر Dynamic testing، بينما التكرارية (Scrum/Kanban) تسلّم دفعات متقاربة مع Continuous testing.",
                "نهج Test-first: TDD (اختبار وحدة قبل الكود)، ATDD (اختبارات قبول قبل التطوير)، BDD (Given/When/Then بلغة مشتركة).",
                "Shift-left: تقديم أنشطة الاختبار لمراحل مبكرة مثل مراجعة المتطلبات قبل البرمجة.",
                "Maintenance testing يبدأ بعد الإطلاق بسبب تعديل (Modification)، ترحيل (Migration)، أو إيقاف نظام (Retirement)."
            ),
            keyFormulasAndDiffs = listOf(
                "مستويات الاختبار (5 في v4.0)" to "Component ← Component integration ← System ← System integration ← Acceptance.",
                "Confirmation vs Regression" to "Confirmation: إعادة اختبار العيب المصلّح فقط. Regression: التأكد أن التعديل لم يكسر باقي النظام.",
                "Alpha vs Beta" to "Alpha عند المطورين/بيئة داخلية؛ Beta عند مستخدمين حقيقيين في بيئتهم.",
                "Types vs Levels" to "أي Type (Functional / Non-functional / Black-box / White-box) يمكن تنفيذه في أي Level."
            ),
            examTraps = listOf(
                "فخ متكرر: عدد المستويات في CTFL v4.0 خمسة لأن Integration انقسم إلى Component integration وSystem integration.",
                "Regression وSmoke وConfirmation ليست Test levels بل أنواع/أنشطة مرتبطة بالتغيير.",
                "Impact analysis ضروري في Maintenance testing ويصعب إذا غابت التوثيقات أو الـ Traceability."
            )
        ),
        3 to ChapterZatona(
            chapter = 3,
            title = "زتونة الفصل 3: الاختبار الساكن (Static Testing)",
            coreSummary = listOf(
                "Static testing يفحص Work products (متطلبات، قصص مستخدم، تصميم، كود، خطط) دون تشغيل البرنامج.",
                "يشمل نوعين: Reviews (مراجعة يدوية بشرية) وStatic analysis (فحص آلي بأدوات مثل Linter وSonarQube).",
                "عملية المراجعة الرسمية (5 خطوات): Planning ← Review initiation ← Individual review ← Communication & analysis ← Fixing & reporting.",
                "أنواع المراجعات من الأقل للأعلى رسمية: Informal → Walkthrough → Technical review → Inspection."
            ),
            keyFormulasAndDiffs = listOf(
                "Static vs Dynamic" to "Static يجد الـ Defect مباشرة؛ Dynamic يظهر الـ Failure ثم نبحث عن الـ Defect.",
                "Inspection" to "الأكثر رسمية: أدوار محددة، قوائم فحص، مقاييس، وهدفه اكتشاف أكبر عدد defects وتجميع بيانات.",
                "Walkthrough" to "يقوده المؤلف (Author) غالباً للشرح والتعلم وبناء فهم مشترك.",
                "أدوار المراجعة" to "Manager (الموارد/القرار)، Author (المؤلف)، Review leader، Facilitator/Moderator، Reviewers، Scribe (التوثيق)."
            ),
            examTraps = listOf(
                "اختبار الأداء والاستجابة (Performance) لا يُكتشف بـ Static testing بل يتطلب Dynamic testing.",
                "التغذية الراجعة المبكرة (Early feedback) تمنع بناء ميزات خاطئة وتقلل تكلفة إعادة العمل.",
                "المراجعات غير مخصصة للكود فقط؛ تطبق على المتطلبات والـ User stories والخطط أيضاً."
            )
        ),
        4 to ChapterZatona(
            chapter = 4,
            title = "زتونة الفصل 4: تقنيات التصميم (الأعلى وزناً: 11 سؤالاً)",
            coreSummary = listOf(
                "Black-box (على المواصفات): EP، BVA، Decision Table، State Transition.",
                "White-box (على بنية الكود): Statement coverage وBranch coverage.",
                "Experience-based: Error guessing، Exploratory testing (بـ Test charter)، Checklist-based.",
                "Collaboration-based: User stories (3Cs + INVEST)، Acceptance criteria (Given/When/Then أو قواعد)، وATDD مع Three Amigos."
            ),
            keyFormulasAndDiffs = listOf(
                "2-value BVA لمجال [a..b]" to "تختبر: a-1, a, b, b+1 (قيمتان لكل حد).",
                "3-value BVA لمجال [a..b]" to "تختبر: a-1, a, a+1, b-1, b, b+1 (ثلاث قيم لكل حد — أدق).",
                "Decision Table (n شروط ثنائية)" to "عدد القواعد الكامل = 2^n؛ المطوي (Collapsed) يدمج الشروط غير المؤثرة «-».",
                "Branch vs Statement" to "100% Branch coverage يضمن 100% Statement coverage، والعكس غير صحيح.",
                "State Transition" to "All transitions (0-switch صالحة + غير صالحة) ⊃ Valid transitions ⊃ All states."
            ),
            examTraps = listOf(
                "في الاختبار السلبي بـ EP: ضع قيمة غير صالحة واحدة فقط في الحالة حتى لا يحجب خطأ خطأً آخر (Defect masking).",
                "انتبه إن كان السؤال يطلب Valid EP فقط أم Valid + Invalid، وهل يطلب 2-value أم 3-value BVA.",
                "في Branch testing لكل `if` من غير `else` يوجد فرعان (True وFalse) حتى لو لم يُكتب `else`."
            )
        ),
        5 to ChapterZatona(
            chapter = 5,
            title = "زتونة الفصل 5: إدارة أنشطة الاختبار (9 أسئلة)",
            coreSummary = listOf(
                "التخطيط يشمل النطاق، الموارد، الجدول، Entry criteria (جاهزية البدء)، وExit criteria / Definition of Done (متى ننهي).",
                "تقنيات التقدير الأربع: Ratio-based، Extrapolation، Wideband Delphi، وThree-point estimation.",
                "إدارة المخاطر (Risk = Likelihood × Impact): تشمل Project risks (مشروع/موارد) وProduct risks (جودة المنتج).",
                "تقرير العيب الجيد يشمل خطوات إعادة الإنتاج (Steps)، المتوقع vs الفعلي، البيئة، الشدة، الأولوية، والمرفقات بحياد تام."
            ),
            keyFormulasAndDiffs = listOf(
                "Three-point estimation" to "E = (a + 4m + b) ÷ 6  |  SD = (b − a) ÷ 6",
                "Severity vs Priority" to "Severity = أثر تقني (يحدده غالباً المختبر)؛ Priority = استعجال تجاري (يحدده Product Owner/الإدارة).",
                "Test Quadrant Q1..Q4" to "Q1 وحدة/تقني يدعم الفريق · Q2 وظيفي/أعمال يدعم الفريق · Q3 استكشافي/قبول ينتقد المنتج · Q4 أداء/أمان ينتقد المنتج.",
                "Configuration Management" to "تضبط النسخ (Versioning/Baselines) والعلاقة بين الكود والـ Testware وبيئة الاختبار."
            ),
            examTraps = listOf(
                "خطأ إملائي في شعار الصفحة الرئيسية = Severity منخفضة لكن Priority عالية.",
                "Test pyramid يضع الوحدة كقاعدة واسعة (سريعة ورخيصة) والـ UI كقمة ضيقة.",
                "التمييز بين Monitoring (جمع أرقام التقدم) وControl (اتخاذ إجراء تصحيحي)."
            )
        ),
        6 to ChapterZatona(
            chapter = 6,
            title = "زتونة الفصل 6: أدوات الاختبار (سؤالان)",
            coreSummary = listOf(
                "الأدوات تدعم جميع أنشطة الاختبار: الإدارة (Jira/TestRail)، التحليل الساكن (ESLint/SonarQube)، التصميم/البيانات، التنفيذ والتغطية (Playwright/Selenium)، الأداء (JMeter/k6)، وDevOps/CI.",
                "فوائد الأتمتة: سرعة Regression، تقليل الخطأ البشري في الأعمال المكررة، اتساق النتائج، وتوفير وقت للاختبار الاستكشافي.",
                "مخاطر الأتمتة: توقعات مبالغ فيها، إهمال صيانة السكربتات، تقدير خاطئ للتكلفة والوقت، واستبدال الاختبار الاستكشافي/البشري كلياً."
            ),
            keyFormulasAndDiffs = listOf(
                "متى تنجح الأتمتة؟" to "للاختبارات المتكررة المستقرة (Regression) مع صيانة مستمرة ودمج في CI/CD.",
                "ما لا تغنيه الأتمتة" to "Exploratory testing وUsability testing وتقييم تجربة المستخدم الحقيقية."
            ),
            examTraps = listOf(
                "أتمتة الاختبار لا تلغي دور المختبر اليدوي ولا تضمن خلو البرمجيات من العيوب.",
                "أكثر سبب عملي لفشل مشاريع الأتمتة هو الاستهانة بتكلفة الصيانة المستمرة للسكربتات."
            )
        )
    )

    fun forChapter(chapter: Int): ChapterZatona? = CHAPTER_SUMMARIES[chapter]
}
