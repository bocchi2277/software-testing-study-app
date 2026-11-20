package com.example.data

data class CardDef(
    val id: String,
    val term: String,
    val def: String,
    val ch: Int
)

object CardsData {
    private val raw: List<Triple<String, String, Int>> = listOf(
        Triple("Error (Mistake)", "خطأ بشري يؤدي إلى defect في work product.", 1),
        Triple("Defect (Bug / Fault)", "عيب أو خلل في work product قد يسبب failure عند التنفيذ.", 1),
        Triple("Failure", "ظهور سلوك غير متوافق مع المتوقع أثناء تشغيل النظام.", 1),
        Triple("Root cause", "السبب الأساسي الذي أدى إلى حدوث الـ Defect أو الـ Error.", 1),
        Triple("Debugging", "نشاط تطوير يحلل سبب الـ failure ويصلح الـ defect.", 1),
        Triple("Verification", "هل بنينا المنتج صحيحاً؟ (مطابقة المواصفات).", 1),
        Triple("Validation", "هل بنينا المنتج الصحيح؟ (يلبي حاجة المستخدم).", 1),
        Triple("Quality Assurance (QA)", "نشاط وقائي موجّه للعملية لتحسين الجودة ومنع الـ defects.", 1),
        Triple("Quality Control (QC)", "أنشطة موجهة للمنتج مثل الاختبار لاكتشاف الـ defects.", 1),
        Triple("Test basis", "الوثائق والمعلومات التي تُبنى عليها Test cases.", 1),
        Triple("Test condition", "جانب قابل للاختبار مستخرج من test basis.", 1),
        Triple("Test case", "مدخلات وشروط مسبقة ونتائج متوقعة لتحقيق هدف اختبار.", 1),
        Triple("Test procedure", "ترتيب تنفيذ Test cases وخطواته.", 1),
        Triple("Test suite", "مجموعة Test cases تُنفَّذ معاً.", 1),
        Triple("Test oracle", "مصدر يحدد النتيجة المتوقعة للاختبار.", 1),
        Triple("Testware", "كل مخرجات أنشطة الاختبار: خطط، حالات، بيانات، سكربتات، تقارير.", 1),
        Triple("Traceability", "ربط test basis بالاختبارات والنتائج لدعم الـ coverage وتحليل الأثر.", 1),
        Triple("Shift-left", "نقل أنشطة الاختبار لمراحل مبكرة من دورة التطوير.", 1),
        Triple("Pesticide paradox", "تكرار نفس الاختبارات يقلل اكتشاف defects جديدة.", 1),
        Triple("Defect clustering", "تتركز معظم الـ defects في عدد قليل من المكونات.", 1),
        Triple("Component (Unit) testing", "اختبار components الفردية منعزلة، غالباً يقوم به المطور.", 2),
        Triple("Integration testing", "اختبار التفاعل بين المكونات أو الأنظمة.", 2),
        Triple("System testing", "اختبار النظام ككل، وظيفياً وغير وظيفياً.", 2),
        Triple("Acceptance testing", "تحقق من أن النظام جاهز للنشر ويلبي احتياجات المستخدم/الأعمال.", 2),
        Triple("Alpha / Beta testing", "Alpha عند موقع المطور، وBeta لدى مستخدمين فعليين في بيئتهم.", 2),
        Triple("Functional testing", "تقييم الوظائف: ماذا يفعل النظام.", 2),
        Triple("Non-functional testing", "تقييم خصائص الجودة: أداء، أمان، استخدامية، توافق، موثوقية.", 2),
        Triple("Smoke testing", "فحص سريع لأهم الوظائف قبل الاختبار العميق.", 2),
        Triple("Confirmation testing", "إعادة الحالة الفاشلة للتأكد أن الـ defect أُصلح.", 2),
        Triple("Regression testing", "اكتشاف آثار جانبية لتغيير على ما كان يعمل.", 2),
        Triple("Maintenance testing", "اختبار بعد تعديل أو ترحيل أو إيقاف نظام قيد التشغيل.", 2),
        Triple("Impact analysis", "تقييم أثر التغيير لتحديد ما يجب إعادة اختباره.", 2),
        Triple("TDD / ATDD / BDD", "منهجيات Test-first: من الوحدة، من Acceptance criteria، ومن السلوك.", 2),
        Triple("Static testing", "فحص work products دون تنفيذ: Reviews وStatic analysis.", 3),
        Triple("Static analysis", "تحليل الكود بأدوات دون تنفيذه.", 3),
        Triple("Inspection", "أكثر أنواع المراجعة رسمية: أدوار ومقاييس وقوائم فحص.", 3),
        Triple("Walkthrough", "مراجعة يقودها المؤلف لشرح المنتج وجمع الملاحظات.", 3),
        Triple("Technical review", "مراجعة يجريها خبراء تقنيون للوصول إلى إجماع.", 3),
        Triple("Black-box testing", "اختبار مبني على المواصفات دون النظر للبنية الداخلية.", 4),
        Triple("White-box testing", "اختبار مبني على البنية الداخلية للكود.", 4),
        Triple("Equivalence partitioning", "تقسيم المدخلات لأقسام تتصرف بنفس الطريقة واختبار عنصر من كل قسم.", 4),
        Triple("Boundary value analysis", "اختبار قيم حدود الأقسام وما يجاورها.", 4),
        Triple("Decision table testing", "تغطية تركيبات الشروط ونتائجها.", 4),
        Triple("State transition testing", "اختبار الحالات والانتقالات والأحداث والشروط.", 4),
        Triple("Statement coverage", "نسبة الأوامر التنفيذية التي نُفذت.", 4),
        Triple("Branch coverage", "نسبة الفروع (True/False لكل قرار) التي نُفذت.", 4),
        Triple("Error guessing", "توقع الأخطاء بناءً على خبرة المختبر.", 4),
        Triple("Exploratory testing", "تعلم وتصميم وتنفيذ متزامن، غالباً بـ test charter.", 4),
        Triple("Checklist-based testing", "اختبار مبني على قائمة بنود مشتقة من الخبرة.", 4),
        Triple("User story", "وصف قصير لمتطلب من منظور المستخدم: Card, Conversation, Confirmation.", 4),
        Triple("Acceptance criteria", "شروط يجب تحققها ليُقبل المنتج/الـ story.", 4),
        Triple("Test plan", "وثيقة تصف الأهداف والنطاق والنهج والموارد والجدول والمخاطر.", 5),
        Triple("Entry criteria", "شروط الجاهزية قبل بدء نشاط اختبار.", 5),
        Triple("Exit criteria (DoD)", "شروط يجوز عندها إنهاء نشاط الاختبار.", 5),
        Triple("Three-point estimation", "E = (a + 4m + b) ÷ 6.", 5),
        Triple("Wideband Delphi", "تقدير جماعي بالخبراء عبر جولات.", 5),
        Triple("Test pyramid", "الكثير من اختبارات الوحدة، أقل للتكامل، أقل للواجهة.", 5),
        Triple("Risk", "احتمال الحدوث × الأثر.", 5),
        Triple("Project risk / Product risk", "مخاطر إدارة المشروع مقابل مخاطر جودة المنتج.", 5),
        Triple("Risk-based testing", "توجيه الجهد حسب مستوى المخاطر.", 5),
        Triple("Test progress report", "تقرير دوري يبين التقدم والعوائق والمقاييس.", 5),
        Triple("Test completion report", "ملخص نهائي: ما اختُبر وما تبقّى وLessons learned.", 5),
        Triple("Configuration management", "التحكم في النسخ وعناصر التهيئة وTraceability.", 5),
        Triple("Defect report", "وصف defect يحتوي خطوات إعادة الإنتاج والفعلي والمتوقع.", 5),
        Triple("Severity vs Priority", "Severity أثر تقني، Priority إلحاح إصلاح.", 5),
        Triple("Test automation", "تنفيذ الاختبارات بأدوات: مفيدة للتكرار، تحتاج صيانة.", 6),
        Triple("Static analysis tool", "أداة تفحص الكود بلا تنفيذ (مثل SonarQube).", 6),
        Triple("CI/CD", "دمج وبناء ونشر آلي مع تشغيل الاختبارات تلقائياً.", 6)
    )

    private fun slug(s: String): String =
        s.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .replace(Regex("^-|-$"), "")

    val CARDS: List<CardDef> = raw.map { (term, def, ch) ->
        CardDef(id = slug(term), term = term, def = def, ch = ch)
    }
}
