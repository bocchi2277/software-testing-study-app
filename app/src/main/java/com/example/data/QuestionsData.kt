package com.example.data

import java.util.Locale

data class Question(
    val id: String,
    val ch: Int,
    val q: String,
    val options: List<String>, // index 0 is always correct before shuffling
    val exp: String
)

data class PreparedQuestion(
    val question: Question,
    val order: List<Int>,
    val correctIdx: Int
)

object QuestionsData {
    private data class RawQ(
        val ch: Int,
        val q: String,
        val correct: String,
        val wrong: List<String>,
        val exp: String
    )

    private val raw: List<RawQ> = listOf(
        // Chapter 1
        RawQ(1, "أي مبدأ يقول إن الاختبار الشامل لكل المدخلات والتركيبات مستحيل؟", "Exhaustive testing is impossible", listOf("Defects cluster together", "Absence-of-defects fallacy", "Pesticide paradox"), "المبدأ الثاني: لا يمكن اختبار كل التركيبات، لذلك نستخدم المخاطر والأولويات والتقنيات."),
        RawQ(1, "ما العلاقة الصحيحة بين Error وDefect وFailure؟", "Error بشري يسبب Defect في work product قد يسبب Failure عند التنفيذ", listOf("Failure يسبب Error ثم Defect", "Defect يسبب Error عند المستخدم", "الثلاثة مترادفات لنفس المعنى"), "Error بشري ← Defect في المنتج ← Failure أثناء التشغيل. وليست كل Defects تسبب Failure."),
        RawQ(1, "ما الفرق الرئيسي بين Testing وDebugging؟", "Testing يظهر الـ failures، وDebugging يجد سبب الـ defect ويصلحه", listOf("Testing يصلح الـ defects وDebugging يكتشفها", "Debugging يتم دائماً بواسطة المختبر", "لا فرق بينهما"), "Debugging نشاط تطوير: تحليل وإصلاح السبب، ثم يأتي Confirmation testing."),
        RawQ(1, "فريق يعيد تشغيل نفس مجموعة الاختبارات منذ سنوات ولم تعد تكشف defects جديدة. أي مبدأ ينطبق؟", "Tests wear out (Pesticide paradox)", listOf("Early testing saves time and money", "Testing is context dependent", "Exhaustive testing is impossible"), "الحل: مراجعة الاختبارات وتحديثها وإضافة اختبارات جديدة."),
        RawQ(1, "في أي من الحالات التالية يقع Absence-of-defects fallacy؟", "نظام خالٍ من الـ defects المعروفة لكنه لا يلبي احتياجات المستخدمين", listOf("نظام كل الـ defects فيه في وحدة واحدة", "نظام لم تُختبر بعض مساراته", "نظام تأخر تسليمه"), "إصلاح كل الـ defects لا يضمن نجاح النظام إن لم يحقق حاجة المستخدم (Validation)."),
        RawQ(1, "أي نشاط من أنشطة الاختبار يجيب عن سؤال «ماذا نختبر؟»", "Test analysis", listOf("Test design", "Test implementation", "Test completion"), "Test analysis تحدد test conditions من test basis، وTest design تجيب عن «كيف نختبر؟»."),
        RawQ(1, "ما فائدة Traceability بين test basis وTest cases؟", "تحليل أثر التغيير وقياس تغطية المتطلبات", listOf("تقليل عدد الاختبارات دائماً", "الاستغناء عن test plan", "ضمان خلو المنتج من الـ defects"), "Traceability تدعم تحليل الأثر وتدقيق الـ coverage وإبلاغ الحالة."),
        RawQ(1, "أي مما يلي من Testware المنتَجة أثناء Test implementation؟", "Test procedures و test suites", listOf("Test completion report", "Defect reports", "Test plan"), "التنفيذ يعد الـ procedures والـ suites وجدول التنفيذ والبيئة والبيانات."),
        RawQ(1, "ما ميزة استقلالية الاختبار (Independence of testing)؟", "مختبر مستقل قد يكتشف defects لا يراها المؤلف بسبب التحيز", listOf("تسريع التواصل مع المطورين", "تقليل التكلفة دائماً", "الاستغناء عن مراجعة الكود"), "Independence يقلل التحيز المعرفي، لكن قد يسبب عزلة عن الفريق."),
        RawQ(1, "أي عبارة صحيحة عن QA وQC؟", "QA موجه للعملية ووقائي، والاختبار من أنشطة QC", listOf("QA هو نفسه Testing", "QC موجه للعملية فقط", "QA يتم فقط بعد التسليم"), "QA يمنع الـ defects بتحسين العمليات، والاختبار من أنشطة Quality Control."),

        // Chapter 2
        RawQ(2, "أي مما يلي يُعد مستوى اختبار (Test level)؟", "Component integration testing", listOf("Smoke testing", "Regression testing", "Exploratory testing"), "المستويات: Component, Component integration, System, System integration, Acceptance."),
        RawQ(2, "بعد إصلاح defect، الاختبار الذي يتأكد أن نفس الحالة لم تعد تفشل هو:", "Confirmation testing", listOf("Regression testing", "Smoke testing", "Load testing"), "Confirmation (Re-testing) يعيد نفس الحالة الفاشلة. أما Regression فيتحقق من عدم كسر وظائف أخرى."),
        RawQ(2, "Regression testing يهدف إلى:", "اكتشاف آثار جانبية غير مقصودة بعد تغيير", listOf("قياس زمن الاستجابة", "مراجعة المتطلبات", "التحقق من إصلاح الـ defect فقط"), "يتأكد أن التغيير لم يكسر ما كان يعمل، ومرشح قوي للأتمتة."),
        RawQ(2, "أي مما يلي مثال على اختبار غير وظيفي (Non-functional)؟", "Performance efficiency testing", listOf("Login with valid user", "Add item to cart", "Reset password"), "الأداء والأمان والاستخدامية والتوافق والموثوقية اختبارات غير وظيفية."),
        RawQ(2, "اختبار يجريه مستخدمون حقيقيون في بيئتهم قبل الإصدار النهائي يسمى:", "Beta testing", listOf("Alpha testing", "Component testing", "Contractual acceptance"), "Alpha عند المطور بموقعه، وBeta عند مستخدمين فعليين في بيئتهم."),
        RawQ(2, "ما هي محفزات Maintenance testing؟", "Modification و Migration و Retirement", listOf("Planning و Design و Execution", "Review و Inspection و Walkthrough", "Alpha و Beta و Gamma"), "Maintenance testing بعد التعديل أو الترحيل أو إيقاف النظام."),
        RawQ(2, "ما الذي يساعد Impact analysis على تحديد نطاق Maintenance testing؟", "تحديد أجزاء النظام المتأثرة بالتغيير", listOf("حساب عدد المطورين", "حذف الاختبارات القديمة", "تقليل حجم الشيفرة"), "تحليل الأثر يحدد ما يجب إعادة اختباره (regression) بحسب المخاطر وحجم التغيير."),
        RawQ(2, "ما الذي يميز نهج TDD؟", "كتابة الاختبار قبل كتابة الكود", listOf("كتابة الكود قبل أي اختبار", "الاعتماد على الاختبار اليدوي فقط", "الاختبار بعد الإصدار"), "TDD نهج Test-first على مستوى الوحدة. وATDD يشتق الاختبارات من Acceptance criteria."),

        // Chapter 3
        RawQ(3, "أي مما يلي يُعد من Static testing؟", "Code review", listOf("Load testing", "Smoke testing", "Beta testing"), "Static testing لا ينفذ الكود: Reviews وStatic analysis."),
        RawQ(3, "أي defect يسهل اكتشافه بـ Static analysis أكثر من Dynamic testing؟", "كود لا يمكن الوصول إليه (Unreachable code)", listOf("بطء الاستجابة تحت حمل مرتفع", "تسرب الذاكرة أثناء التشغيل الطويل", "مشاكل التكامل مع الشبكة"), "الأدوات الساكنة تحلل الكود بلا تنفيذ، فتكتشف الكود غير القابل للوصول وانتهاك المعايير."),
        RawQ(3, "أي نوع مراجعة هو الأكثر رسمية؟", "Inspection", listOf("Walkthrough", "Informal review", "Technical review"), "Inspection لها أدوار محددة ومقاييس وقوائم فحص وعملية رسمية."),
        RawQ(3, "من المسؤول عن إدارة اجتماع المراجعة وضمان سيره؟", "Moderator (Facilitator)", listOf("Author", "Scribe", "Manager"), "الـ Facilitator يدير الاجتماع، والـ Scribe يسجل، والـ Author مؤلف المنتج المُراجَع."),
        RawQ(3, "ما الترتيب الصحيح لأنشطة عملية المراجعة؟", "Planning, Review initiation, Individual review, Communication and analysis, Fixing and reporting", listOf("Individual review, Planning, Fixing, Initiation, Analysis", "Initiation, Planning, Analysis, Individual review, Fixing", "Planning, Fixing, Initiation, Individual review, Analysis"), "هذا الترتيب الرسمي لخمسة أنشطة في منهج CTFL v4.0."),
        RawQ(3, "ما الفرق الصحيح بين Static وDynamic testing؟", "Static يجد الـ defects مباشرة، وDynamic يجد الـ failures الناتجة عنها", listOf("Static يجد الـ failures وDynamic يجد الـ defects فقط", "كلاهما ينفذ الكود", "Dynamic لا يحتاج لبيئة تشغيل"), "التنفيذ هو الفارق: Dynamic يظهر failures، وStatic يفحص work product ويجد defects."),

        // Chapter 4
        RawQ(4, "Boundary Value Analysis (2-value) لمجال صالح من 1 إلى 100. ما قيم الاختبار؟", "0, 1, 100, 101", listOf("1, 50, 100", "1, 2, 99, 100", "0, 50, 101"), "لكل حد: قيمة الحد وأقرب قيمة في القسم المجاور."),
        RawQ(4, "BVA (3-value) لمجال صالح من 10 إلى 20. ما قيم الاختبار؟", "9, 10, 11, 19, 20, 21", listOf("9, 10, 20, 21", "10, 15, 20", "10, 11, 19, 20"), "لكل حد: القيمة والقيمتان المجاورتان له."),
        RawQ(4, "يقبل الحقل عمراً من 18 إلى 60 سنة. كم قسم تكافؤ (Partition) يوجد؟", "3", listOf("2", "4", "60"), "أقل من 18 (غير صالح)، 18–60 (صالح)، أكثر من 60 (غير صالح)."),
        RawQ(4, "لأي سبب تحتوي Test case سلبية في EP على قيمة غير صالحة واحدة فقط؟", "لتجنب Defect masking وتحديد سبب الفشل", listOf("لتقليل عدد الأقسام", "لأن المعايير تمنع ذلك", "لزيادة سرعة التنفيذ"), "لو جمعنا عدة قيم غير صالحة فقد يخفي أحد الـ defects الآخر."),
        RawQ(4, "جدول قرار كامل بـ 3 شروط ثنائية (T/F) يحتوي كم قاعدة؟", "8", listOf("3", "6", "9"), "2^3 = 8 قواعد قبل أي طي (Collapse)."),
        RawQ(4, "Decision Table Testing مناسب عندما:", "تعتمد النتيجة على تركيبة من عدة شروط", listOf("نريد قياس تغطية الكود", "نختبر الأداء", "نراجع المتطلبات بلا تنفيذ"), "يعرض كل تركيبة شروط ونتائجها، فيكشف تركيبات منسية ومتطلبات متناقضة."),
        RawQ(4, "أي مستوى تغطية في State transition testing هو الأقوى؟", "All transitions coverage", listOf("All states coverage", "Valid transitions coverage", "Statement coverage"), "All transitions تشمل الانتقالات الصالحة وغير الصالحة وتحقق تغطية الحالات."),
        RawQ(4, "أي تقنية من التالية تنتمي إلى White-box؟", "Branch testing", listOf("Equivalence partitioning", "Error guessing", "State transition testing"), "Statement وBranch هما تقنيتا White-box في CTFL."),
        RawQ(4, "100% Branch coverage تعني بالضرورة:", "100% Statement coverage", listOf("عدم وجود defects في الكود", "اختبار كل قيم الإدخال", "100% Boundary coverage"), "Branch تشمل Statement ولا العكس، لكنها لا تضمن خلو الكود من الـ defects."),
        RawQ(4, "كود يحتوي 20 أمراً تنفيذياً، نُفذ منها 15 بالـ Test cases الحالية. كم Statement coverage؟", "75%", listOf("15%", "25%", "80%"), "15 ÷ 20 = 75%."),
        RawQ(4, "Exploratory testing هو:", "تعلم وتصميم وتنفيذ متزامن للاختبارات", listOf("تنفيذ سكربتات مكتوبة مسبقاً فقط", "مراجعة المتطلبات بلا تنفيذ", "اختبار آلي بالكامل"), "يناسب الوقت المحدود والمتطلبات الناقصة ويستخدم test charter."),
        RawQ(4, "تقنية يقوم فيها المختبر بتوقع الأخطاء المحتملة بناءً على خبرته:", "Error guessing", listOf("Statement testing", "Equivalence partitioning", "Decision table testing"), "تعتمد على الخبرة والـ defects السابقة، مثل القسمة على صفر أو الحقول الفارغة."),
        RawQ(4, "ما العناصر الثلاثة (3Cs) في User story؟", "Card, Conversation, Confirmation", listOf("Code, Compile, Commit", "Create, Check, Close", "Cost, Capacity, Cycle"), "Card (وصف)، Conversation (نقاش)، Confirmation (Acceptance criteria)."),
        RawQ(4, "في ATDD تُشتق Test cases من:", "Acceptance criteria", listOf("الكود المصدري", "Defect reports", "خطة الاستضافة"), "ATDD نهج Test-first يشتق الاختبارات من Acceptance criteria قبل التطوير."),
        RawQ(4, "صيغة Given/When/Then تُستخدم في:", "كتابة سيناريوهات Acceptance criteria", listOf("قياس Structural coverage", "تقدير الجهد", "Configuration Management"), "تُستخدم في BDD وATDD لصياغة Acceptance criteria كسيناريوهات."),
        RawQ(4, "أي تقنية يُفضّل استخدامها عندما تكون المواصفات ناقصة والوقت ضيقاً؟", "Exploratory testing", listOf("Decision table testing", "Branch testing", "State transition testing"), "Exploratory testing يعوض نقص التوثيق ويكمل الاختبارات الرسمية."),

        // Chapter 5
        RawQ(5, "مستوى المخاطرة (Risk level) يُحدد من:", "احتمال الحدوث × الأثر", listOf("عدد المختبرين", "حجم الكود", "تكلفة الأدوات"), "Likelihood × Impact."),
        RawQ(5, "تقدير بثلاث نقاط: متفائل = 2، محتمل = 5، متشائم = 14 (أيام). ما الجهد المقدّر؟", "6", listOf("5", "7", "7.5"), "(2 + 4×5 + 14) ÷ 6 = 36 ÷ 6 = 6."),
        RawQ(5, "Exit criteria تحدد:", "متى يمكن إنهاء نشاط الاختبار", listOf("متى يبدأ الاختبار", "من يكتب الكود", "أي أداة تُستخدم"), "Entry criteria للبدء، وExit criteria/Definition of Done للانتهاء."),
        RawQ(5, "تأخر تسليم البيئة بسبب نقص الموارد هو مثال على:", "Project risk", listOf("Product risk", "Defect", "Failure"), "المخاطر المرتبطة بإدارة المشروع هي Project risks، أما جودة المنتج فـ Product risks."),
        RawQ(5, "ثغرة أمنية محتملة في نظام الدفع هي مثال على:", "Product risk", listOf("Project risk", "Entry criterion", "Test oracle"), "Product risk مرتبطة بخصائص المنتج وجودته."),
        RawQ(5, "أي خيار يُعد استجابة للمخاطر (Risk response)؟", "Mitigation أو Acceptance أو Transfer أو Contingency plan", listOf("Debugging", "Review", "Regression"), "التخفيف، القبول، النقل، أو خطة الطوارئ."),
        RawQ(5, "أي عنصر ضروري في Defect report الجيد؟", "خطوات إعادة الإنتاج والنتيجة الفعلية والمتوقعة", listOf("رأي المطور في المختبر", "اسم أقدم موظف", "ميزانية المشروع"), "تسهّل المعلومات الدقيقة إعادة الإنتاج والإصلاح."),
        RawQ(5, "ما الفرق بين Severity وPriority؟", "Severity أثر الـ defect التقني، وPriority إلحاح الإصلاح", listOf("هما نفس المعنى", "Severity يحددها المطور فقط وPriority المختبر فقط", "Priority أثر الـ defect التقني وSeverity إلحاح الإصلاح"), "خطأ إملائي في شعار الشركة: Severity منخفضة وPriority عالية."),
        RawQ(5, "ما هو الغرض من Configuration Management في الاختبار؟", "التحكم في إصدارات testware وعناصر التهيئة وضمان Traceability", listOf("زيادة سرعة الحاسوب", "تقدير الجهد", "كتابة test cases"), "تضمن معرفة أي نسخة اختُبرت وإعادة إنتاج النتائج."),
        RawQ(5, "أي مما يلي مقياس شائع في مراقبة الاختبار (Test monitoring)؟", "نسبة Test cases المنفذة والناجحة والفاشلة", listOf("عدد الموظفين الجدد", "ساعات الإجازة", "عدد أسطر التعليقات"), "المقاييس: حالات منفذة/ناجحة، كثافة الـ defects، تغطية المتطلبات، الجهد."),
        RawQ(5, "في Test pyramid، أي نوع من الاختبارات ينبغي أن يكون الأكثر عدداً؟", "اختبارات الوحدة (Unit/Component)", listOf("اختبارات UI الشاملة", "اختبارات القبول اليدوية", "اختبارات الأداء"), "الوحدة سريعة ورخيصة، لذا الأكثر عدداً، وUI الأقل لأنها بطيئة وهشة."),
        RawQ(5, "أي تقدير يعتمد على آراء مجموعة خبراء تتقارب عبر جولات؟", "Wideband Delphi", listOf("Ratio-based", "Extrapolation", "Three-point"), "Wideband Delphi تقدير جماعي بالخبراء، أما Ratio-based فنسب مشاريع سابقة."),
        RawQ(5, "من يشترك في «Three Amigos»؟", "Business، Developer، Tester", listOf("Manager، Auditor، Customer", "Tester، Architect، DBA", "Author، Moderator، Scribe"), "يضمن فهماً مشتركاً للـ story وAcceptance criteria."),

        // Chapter 6
        RawQ(6, "أي من التالية فائدة لـ Test automation؟", "تقليل الجهد في الاختبارات المتكررة وتحسين الاتساق", listOf("إلغاء الحاجة للاختبار اليدوي نهائياً", "ضمان خلو المنتج من الـ defects", "إلغاء صيانة الاختبارات"), "الأتمتة تدعم التكرار والاتساق لكنها لا تغني عن الاختبار اليدوي."),
        RawQ(6, "أي مما يلي مخاطرة للأتمتة؟", "توقعات غير واقعية وإهمال صيانة السكربتات", listOf("نتائج أكثر اتساقاً", "تغطية أكبر", "تنفيذ أسرع"), "من المخاطر أيضاً سوء تقدير الجهد والاعتماد الزائد على الأداة."),
        RawQ(6, "أي أداة تُصنّف كأداة Static analysis؟", "SonarQube", listOf("JMeter", "Postman", "Selenium"), "SonarQube تحلل الكود بلا تنفيذ، وJMeter للأداء، وPostman للـ API، وSelenium للتنفيذ الآلي."),
        RawQ(6, "أي نوع أدوات يدعم Continuous Integration؟", "DevOps tools مثل Jenkins وGitHub Actions", listOf("Static analysis فقط", "Defect reporting فقط", "Test data fakers فقط"), "أدوات DevOps تبني وتختبر تلقائياً مع كل تغيير.")
    )

    val QUESTIONS: List<Question> = raw.mapIndexed { idx, r ->
        Question(
            id = "q" + "%03d".format(Locale.US, idx + 1),
            ch = r.ch,
            q = r.q,
            options = listOf(r.correct) + r.wrong,
            exp = r.exp
        )
    }

    val QMAP: Map<String, Question> = QUESTIONS.associateBy { it.id }

    fun prepare(q: Question): PreparedQuestion {
        val order = q.options.indices.shuffled()
        return PreparedQuestion(
            question = q,
            order = order,
            correctIdx = order.indexOf(0)
        )
    }

    fun <T> shuffle(items: List<T>): List<T> = items.shuffled()
}
