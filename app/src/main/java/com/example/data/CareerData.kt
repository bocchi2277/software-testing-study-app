package com.example.data

data class WeekPlan(val n: Int, val title: String, val topics: List<String>, val goal: String)
data class StageItem(val id: String, val text: String)
data class CareerStage(val id: String, val title: String, val why: String, val items: List<StageItem>)
data class PortfolioIdea(val title: String, val desc: String)
data class InterviewQa(val q: String, val a: String)
data class NextCert(val name: String, val desc: String)
data class ExamFact(val key: String, val value: String)
data class BookingStep(val id: String, val text: String)

object CareerData {
    val DAILY_TIPS: List<String> = listOf(
        "راجع مصطلحات الـ Glossary 10 دقائق يومياً: أسئلة كثيرة تُحسم بالتعريف الدقيق.",
        "لا تحفظ فقط، اشرح المبدأ بصوت عالٍ كأنك تعلّمه لزميل (طريقة فاينمان).",
        "أسئلة K3 (الحسابات) هي أسهل نقاط تُكسب إن تدربت عليها. حلّ 3 منها يومياً.",
        "المراجعة المتباعدة أقوى من القراءة المتكررة: استخدم البطاقات يومياً.",
        "بعد كل سؤال خاطئ، اكتب لماذا أخطأت: معلومة ناقصة أم قراءة متسرعة؟",
        "جرّب تطبيق ما تعلمته على موقع حقيقي: اكتب حالات اختبار لصفحة تسجيل الدخول.",
        "النوم الجيد قبل الامتحان أهم من ساعة مراجعة إضافية.",
        "ابدأ الجلسة بأصعب موضوع عندما يكون تركيزك في أعلى مستوى.",
        "اقرأ المنهج الرسمي مرة كاملة: الامتحان مبني عليه حرفياً.",
        "كل 25 دقيقة تركيز تتبعها 5 دقائق راحة بلا هاتف."
    )

    val WEEKS: List<WeekPlan> = listOf(
        WeekPlan(1, "أساسيات الاختبار", listOf("1.1", "1.2", "1.3", "1.4", "1.5"), "أنهِ الفصل 1 + 15 بطاقة + 10 أسئلة من الفصل 1"),
        WeekPlan(2, "دورة التطوير وStatic testing", listOf("2.1", "2.2", "2.3", "3.1", "3.2"), "افهم المستويات والأنواع جيداً + 20 سؤالاً"),
        WeekPlan(3, "تقنيات Black-box (1)", listOf("4.1", "4.2.1", "4.2.2"), "طبّق EP وBVA على 5 أمثلة من موقع حقيقي"),
        WeekPlan(4, "تقنيات Black-box (2) و White-box", listOf("4.2.3", "4.2.4", "4.3"), "ارسم 3 جداول قرار ومخطط حالات + حل أسئلة الـ coverage"),
        WeekPlan(5, "الخبرة والتعاون والأدوات", listOf("4.4", "4.5", "6.1", "6.2"), "اكتب 3 user stories بمعايير قبول + جلسة استكشافية 60 دقيقة"),
        WeekPlan(6, "إدارة الاختبار (1)", listOf("5.1", "5.2", "5.3"), "تدرب على حسابات التقدير والمخاطر"),
        WeekPlan(7, "إدارة الاختبار (2) + مراجعة", listOf("5.4", "5.5"), "اكتب 3 Defect reports + راجع كل أخطائك السابقة"),
        WeekPlan(8, "مراجعة شاملة ومحاكاة الامتحان", emptyList(), "4 محاكاة امتحان بنتيجة ≥ 80% ثم احجز الامتحان")
    )

    val STAGES: List<CareerStage> = listOf(
        CareerStage(
            id = "s1",
            title = "1. الأساس النظري",
            why = "اللغة المشتركة للمهنة وبوابة أغلب الوظائف.",
            items = listOf(
                StageItem("s1a", "إنهاء منهج CTFL v4.0 كاملاً"),
                StageItem("s1b", "اجتياز محاكاة الامتحان 3 مرات متتالية بنتيجة ≥ 80%"),
                StageItem("s1c", "اجتياز امتحان ISTQB CTFL الفعلي")
            )
        ),
        CareerStage(
            id = "s2",
            title = "2. أساسيات تقنية",
            why = "المختبر الجيد يفهم كيف يعمل النظام الذي يختبره.",
            items = listOf(
                StageItem("s2a", "SQL: SELECT, JOIN, GROUP BY, WHERE"),
                StageItem("s2b", "Git: commit, branch, merge, pull request"),
                StageItem("s2c", "HTTP وREST وأكواد الحالة (200, 400, 401, 404, 500)"),
                StageItem("s2d", "استخدام Chrome DevTools (Network, Console, Elements)"),
                StageItem("s2e", "أساسيات Linux/Command line")
            )
        ),
        CareerStage(
            id = "s3",
            title = "3. الاختبار اليدوي العملي",
            why = "المهارة الأكثر طلباً في وظائف المبتدئين.",
            items = listOf(
                StageItem("s3a", "كتابة 30 Test cases حقيقية لموقع تدريبي"),
                StageItem("s3b", "كتابة 10 Defect reports (حقيقية أو من مواقع التدريب)"),
                StageItem("s3c", "إجراء 3 جلسات استكشافية مع Test charter"),
                StageItem("s3d", "كتابة Test plan بسيط وTest summary report"),
                StageItem("s3e", "مراجعة متطلبات (Requirements review) وإيجاد ثغرات")
            )
        ),
        CareerStage(
            id = "s4",
            title = "4. اختبار الـ API",
            why = "أغلب الأنظمة الحديثة تعتمد على APIs والاختبار عندها أسرع وأكثر ثباتاً.",
            items = listOf(
                StageItem("s4a", "إنشاء Collection في Postman بـ 15 طلباً"),
                StageItem("s4b", "كتابة Assertions وتشغيلها بـ Collection Runner"),
                StageItem("s4c", "اختبار حالات سلبية: توكن منتهٍ وبيانات ناقصة")
            )
        ),
        CareerStage(
            id = "s5",
            title = "5. الأتمتة",
            why = "ترفع قيمتك الوظيفية وراتبك بشكل ملحوظ.",
            items = listOf(
                StageItem("s5a", "تعلم لغة واحدة جيداً: JavaScript/TypeScript أو Python أو Java"),
                StageItem("s5b", "أتمتة 10 سيناريوهات بـ Playwright أو Selenium"),
                StageItem("s5c", "تطبيق Page Object Model"),
                StageItem("s5d", "تشغيل الاختبارات على CI (GitHub Actions)"),
                StageItem("s5e", "أتمتة اختبارات API بنفس اللغة")
            )
        ),
        CareerStage(
            id = "s6",
            title = "6. التخصص والتوظيف",
            why = "ابنِ ملفاً يثبت مهاراتك وليس شهاداتك فقط.",
            items = listOf(
                StageItem("s6a", "اختيار تخصص: أداء، أمان، جوال، Accessibility، أو SDET"),
                StageItem("s6b", "نشر Portfolio على GitHub (3 مشاريع على الأقل)"),
                StageItem("s6c", "تحديث LinkedIn وCV بصيغة ATS"),
                StageItem("s6d", "التقدم لـ 30 وظيفة/تدريب وتسجيل التعلم من كل رفض"),
                StageItem("s6e", "التحضير لمقابلات: حل 30 سؤالاً شائعاً بصوت عالٍ")
            )
        )
    )

    val PORTFOLIO: List<PortfolioIdea> = listOf(
        PortfolioIdea("خطة اختبار كاملة لموقع تجارة إلكترونية", "Test plan، Test cases، Defect reports، وTest summary على Sauce Demo أو Practice Software Testing."),
        PortfolioIdea("مجموعة Postman موثقة", "Collection مع Assertions وEnvironments وتقرير تشغيل لـ Restful-booker."),
        PortfolioIdea("إطار أتمتة بـ Playwright + POM", "10 اختبارات على الأقل مع تقارير وتشغيل CI على GitHub Actions."),
        PortfolioIdea("جلسات استكشافية موثقة", "3 Charters ونتائجها وdefects مكتشفة بلقطات شاشة."),
        PortfolioIdea("مدونة قصيرة", "اكتب عن تجربتك مع كل تقنية (BVA، Decision table) بأمثلة. تُظهر قدرتك على التواصل."),
        PortfolioIdea("تقرير Accessibility وأداء", "فحص موقع حقيقي بـ Lighthouse وWAVE مع توصيات.")
    )

    val INTERVIEW: List<InterviewQa> = listOf(
        InterviewQa("ما الفرق بين Verification وValidation؟", "Verification: هل بنينا المنتج صحيحاً حسب المواصفات؟ Validation: هل بنينا المنتج الصحيح الذي يريده المستخدم؟"),
        InterviewQa("ما الفرق بين Severity وPriority؟ أعطني مثالاً.", "Severity أثر الـ defect على النظام، وPriority ترتيب الإصلاح. شعار الشركة الخاطئ: Severity منخفضة وPriority عالية. انهيار تقرير نادر الاستخدام: Severity عالية وPriority منخفضة."),
        InterviewQa("اشرح دورة حياة الـ defect.", "New ← Assigned/Open ← Fixed ← Retest ← Closed. مع حالات Reopened وRejected وDuplicate وDeferred."),
        InterviewQa("ما الفرق بين Smoke وSanity وRegression؟", "Smoke: فحص سريع لأهم الوظائف لتقرير قبول البناء. Sanity: فحص مركز بعد إصلاح محدد. Regression: تأكد من عدم كسر وظائف قائمة بعد تغيير."),
        InterviewQa("كيف تختبر صفحة تسجيل دخول؟", "إيجابي (بيانات صحيحة)، سلبي (كلمة مرور خاطئة، حقل فارغ)، حدود (طول الحقول)، أمان (SQL injection، قفل بعد محاولات)، استخدامية (Tab، رسائل الخطأ)، توافق (متصفحات)، أداء."),
        InterviewQa("ما أنواع تقنيات تصميم الاختبار التي تعرفها؟", "Black-box: EP, BVA, Decision table, State transition. White-box: Statement وBranch. Experience-based: Error guessing وExploratory وChecklist."),
        InterviewQa("ماذا تفعل إذا رفض المطور الـ defect وقال إنه ليس defect؟", "أراجع المتطلبات والنتيجة المتوقعة معاً، وأعرض الأدلة وخطوات الإعادة، وإن بقي الخلاف أرفعه لـ Product Owner لقرار. أركز على الحقائق لا الأشخاص."),
        InterviewQa("متى لا يجب أتمتة اختبار؟", "الاختبارات التي تُجرى مرة واحدة، أو الواجهة المتغيرة باستمرار، أو الاستكشافية والاستخدامية، أو إذا كانت تكلفة الأتمتة أعلى من فائدتها."),
        InterviewQa("ما هو Test pyramid؟", "كثير من اختبارات الوحدة (سريعة ورخيصة)، أقل للتكامل/API، وأقل للواجهة UI (بطيئة وهشة)."),
        InterviewQa("كيف تحدد أولويات الاختبار عند ضيق الوقت؟", "Risk-based: أحدد المجالات الأعلى احتمالاً وأثراً، وأبدأ بالوظائف الأساسية للمستخدم، ثم أبلغ stakeholders بما لم يُختبر."),
        InterviewQa("ما الفرق بين Test case وTest scenario؟", "Scenario وصف عالي المستوى لما يُختبر (إتمام عملية شراء). Test case تفصيلي بخطوات وبيانات ونتيجة متوقعة."),
        InterviewQa("ما هو Defect leakage؟", "نسبة الـ defects التي لم تُكتشف أثناء الاختبار وظهرت في الإنتاج. تقيس فعالية الاختبار."),
        InterviewQa("ما هو Page Object Model؟", "نمط تصميم يفصل محددات وعمليات كل صفحة في كلاس مستقل، فتقل التكرار وتسهل الصيانة."),
        InterviewQa("اشرح استعلام SQL يعرض أكثر 5 عملاء طلباً.", "SELECT customer_id, COUNT(*) c FROM orders GROUP BY customer_id ORDER BY c DESC LIMIT 5;"),
        InterviewQa("حدثني عن defect مهم اكتشفته.", "جهّز قصة بصيغة STAR: الموقف، المهمة، ما فعلته، النتيجة. استخدم defect من مشروع تدريبي في محفظتك.")
    )

    val CERTS: List<NextCert> = listOf(
        NextCert("CTFL — Foundation Level v4.0", "نقطة البداية، وتغطي بالفعل أساسيات الاختبار في Agile."),
        NextCert("CTAL-AT — Advanced Agile Tester v2.0", "بديل الترقية المتقدم لـ Agile (بدلاً من CTFL-AT التي ستتوقف تدريجياً في 2027)."),
        NextCert("CTAL-TA — Test Analyst v4.0", "تحليل وتصميم الاختبار المتقدم."),
        NextCert("CTAL-TAE — Test Automation Engineer", "هندسة الأتمتة."),
        NextCert("CTAL-TM — Test Manager v3.0", "إدارة الاختبار (لاحقاً بعد خبرة)."),
        NextCert("CT-SEC / CT-PT / CT-MAT / CT-AI", "أمان، أداء، جوال، وذكاء اصطناعي: حسب التخصص (راجع istqb.org للتوفر والمتطلبات).")
    )

    val CV_TIPS: List<String> = listOf(
        "اجعل CV صفحة واحدة، وضع المهارات التقنية (SQL، Postman، Playwright) والشهادة أعلاه.",
        "اكتب الإنجازات بأرقام: «كتبت 40 Test cases واكتشفت 12 defects» أقوى من «أجريت اختبارات».",
        "ضع رابط GitHub وLinkedIn واجعلهما متسقين مع CV.",
        "استخدم كلمات من إعلان الوظيفة (ATS): Test cases, Bug reports, API testing, Agile, Jira.",
        "للمبتدئين: المشاريع التدريبية والتطوع في اختبار تطبيقات مفتوحة المصدر تُحسب خبرة."
    )

    val EXAM_FACTS: List<ExamFact> = listOf(
        ExamFact("عدد الأسئلة", "40 سؤال اختيار من متعدد (اختيار واحد صحيح)"),
        ExamFact("الزمن", "60 دقيقة (و+25% أي 75 دقيقة لغير الناطقين بالإنجليزية كلغة أم، تحقق مع مزود الامتحان)"),
        ExamFact("درجة النجاح", "65% أي 26 إجابة صحيحة من 40"),
        ExamFact("الدرجات", "كل سؤال بدرجة واحدة، لا خصم على الخطأ"),
        ExamFact("مستويات الأسئلة", "K1 Remember (تذكّر)، K2 Understand (فهم)، K3 Apply (تطبيق: حسابات وتقنيات)"),
        ExamFact("الصلاحية", "شهادة CTFL صالحة مدى الحياة (حسب سياسة ISTQB الحالية)")
    )

    val BOOKING_STEPS: List<BookingStep> = listOf(
        BookingStep("b1", "حمّل منهج CTFL v4.0 الرسمي واقرأه كاملاً مرة واحدة على الأقل"),
        BookingStep("b2", "في مصر: سجّل عبر الهيئة المصرية ESTB ومركز SECC (estb.org.eg)، أو راجع مزود بلدك على istqb.org"),
        BookingStep("b3", "قارن الأسعار والصيغة (في المركز أو عن بعد Online Proctored)"),
        BookingStep("b4", "أنهِ 4 محاكاة امتحان بنتيجة ≥ 80% قبل الحجز"),
        BookingStep("b5", "احجز موعداً قبل 2–3 أسابيع من اليوم المستهدف وسجّله هنا"),
        BookingStep("b6", "جهّز الهوية، وجرّب كاميرا/مايك/إنترنت إن كان الامتحان عن بعد"),
        BookingStep("b7", "نم جيداً وراجع المصطلحات فقط ليلة الامتحان")
    )

    val EXAM_TIPS: List<String> = listOf(
        "وزّع الوقت: دقيقة ونصف لكل سؤال، تخطَّ الصعب وارجع له.",
        "اقرأ السؤال كاملاً وانتبه لكلمات NOT / EXCEPT / MOST / BEST.",
        "استبعد الخيارات الخاطئة أولاً (غالباً خياران خاطئان بوضوح).",
        "لا يوجد خصم على الخطأ: أجب عن جميع الأسئلة.",
        "أسئلة K3 (الحسابات): EP، BVA، Decision table، State transition، Coverage، Three-point. تدرب عليها كثيراً.",
        "اعتمد على تعريفات المنهج لا على خبرتك العملية إن تعارضت.",
        "استخدم Glossary الرسمي، فبعض الأسئلة تعتمد على المصطلح بدقة."
    )

    val TEMPLATES: Map<String, String> = mapOf(
        "tc" to """ID: TC-LOGIN-001
Title: Login succeeds with valid credentials
Priority: High | Traces to: REQ-12
Preconditions: An active registered user exists
Test data: email=user@test.com, password=Valid#123
Steps:
1. Open /login
2. Enter the email and the password
3. Click "Login"
Expected result: User is redirected to /dashboard and the user name appears in the header""",
        "bug" to """Title: Checkout: "Pay now" does nothing when a coupon is applied
Environment: Build 2.4.1 (staging), Chrome 126, Windows 11
Preconditions: Logged in, cart has 1 item
Steps to reproduce:
1. Add any item to the cart
2. Apply coupon SAVE10
3. Click "Pay now"
Actual result: Nothing happens; console shows a 500 error from /api/pay
Expected result: Payment page opens (per REQ-31)
Reproducibility: 3 of 3 attempts; works without the coupon
Severity: High | Priority: High
Attachments: screen recording, console log""",
        "charter" to """Charter: Explore checkout
With: expired and invalid coupons, different currencies
To discover: pricing and rounding errors
Time-box: 60 minutes
Notes: (areas covered, questions, bugs found, ideas for next session)""",
        "plan" to """Test Plan — [Project name]
1. Objectives & scope (in / out of scope)
2. Test approach: levels, types, techniques, automation
3. Entry criteria: build deployed, test data ready, smoke passed
4. Exit criteria: all critical tests executed, no open Sev-1/2, coverage >= 90% of requirements
5. Environment & tools
6. Roles & schedule
7. Risks & mitigation (Likelihood x Impact)
8. Deliverables: test cases, defect reports, summary report""",
        "summary" to """Test Summary Report — [Release x.y]
Period: [from] – [to]
Scope tested / not tested:
Results: planned N, executed N, passed N, failed N, blocked N
Defects: total N | open by severity: Critical 0, High 1, Medium 3, Low 5
Deviations from plan:
Residual risks:
Recommendation: Go / No-go with conditions""",
        "story" to """As a registered customer
I want to reset my password by email
So that I can regain access to my account

Acceptance criteria (Given / When / Then):
- Given a registered email, When I request a reset, Then I receive a link valid for 30 minutes
- Given an expired link, When I open it, Then I see an error and a button to request a new link
- Given a weak new password, When I submit, Then the system rejects it and explains the rules"""
    )
}
