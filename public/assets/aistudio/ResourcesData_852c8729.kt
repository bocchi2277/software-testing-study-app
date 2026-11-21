package com.example.data

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class StudyResource(
    val id: String,
    val cat: String,
    val title: String,
    val url: String,
    val desc: String,
    val level: String,
    val lang: String,
    val ch: List<Int> = emptyList(),
    val top: Boolean = false
) {
    /** مصدر عام (موسوم بثلاثة فصول أو أكثر): لا يُعرض داخل صفحات الفصول، ويبقى في شاشة «المصادر». */
    val isGeneral: Boolean get() = ch.size >= ResourcesData.GENERAL_MIN_CHAPTERS
}

object ResourcesData {
    /** عدد الفصول الذي يُعدّ عنده المصدر «عاماً» وليس خاصاً بفصل. */
    const val GENERAL_MIN_CHAPTERS = 3

    /** المحتوى التعليمي أولاً ثم الفيديو ثم الأدوات والتدريب. */
    private fun catRank(cat: String): Int = when (cat) {
        "تعلّم" -> 0
        "فيديو" -> 1
        else -> 2
    }

    /**
     * مصادر الفصل وحده: تُستبعد المصادر العامة (الموسومة بثلاثة فصول أو أكثر).
     * الترتيب: المحتوى التعليمي، ثم الفيديو، ثم الأدوات؛ وداخل كل فئة الأخصّ بالفصل أولاً.
     * الفرز مستقر فيبقى ترتيب القائمة الأصلي بين المتساوين.
     */
    fun forChapter(chapter: Int, limit: Int = 10): List<StudyResource> =
        RESOURCES
            .filter { chapter in it.ch && !it.isGeneral }
            .sortedWith(compareBy({ catRank(it.cat) }, { it.ch.size }))
            .take(limit)

    val RES_CATS: List<String> = listOf(
        "رسمي",
        "تعلّم",
        "فيديو",
        "أتمتة",
        "API وقواعد بيانات",
        "أداء وأمان",
        "تدريب عملي",
        "أساسيات تقنية",
        "مجتمع ووظائف"
    )

    private fun yt(q: String): String =
        "https://www.youtube.com/results?search_query=" +
            URLEncoder.encode(q, StandardCharsets.UTF_8.toString())

    val RESOURCES: List<StudyResource> = listOf(
        // رسمي
        StudyResource("istqb-ctfl", "رسمي", "ISTQB — CTFL v4.0 (المنهج والعينات A–D)", "https://www.istqb.org/certifications/certified-tester-foundation-level", "الصفحة الرسمية للشهادة: المنهج v4.0.1 ونماذج الامتحان الرسمية A وB وC وD بالإجابات. ابدأ بتحميل المنهج وقراءته.", "مبتدئ", "en", listOf(1, 2, 3, 4, 5, 6), true),
        StudyResource("istqb-glossary", "رسمي", "ISTQB Glossary", "https://glossary.istqb.org", "القاموس الرسمي لكل مصطلحات الامتحان.", "مبتدئ", "en", top = true),
        StudyResource("istqb-home", "رسمي", "ISTQB.org — الموقع الرئيسي", "https://www.istqb.org", "دليل الهيئات الوطنية ومزودي الامتحان وبقية الشهادات.", "مبتدئ", "en"),
        StudyResource("astqb", "رسمي", "ASTQB", "https://astqb.org", "الهيئة الأمريكية: توفر موارد وعينات أسئلة إضافية متوافقة مع v4.0.", "مبتدئ", "en"),
        StudyResource("estb-egypt", "رسمي", "ESTB — الهيئة المصرية لاختبار البرمجيات", "https://estb.org.eg", "البوابة الرسمية للهيئة المصرية: تقويم الامتحانات والرسوم (CTFL v4.0: 1800 ج.م للمصريين / 3000 ج.م لغير المصريين، وتخفيضات التدريب المعتمد).", "مبتدئ", "ar", top = true),
        StudyResource("secc-egypt", "رسمي", "SECC — مركز تقييم واعتماد هندسة البرمجيات", "https://secc.org.eg", "الجهة الوحيدة المعتمدة من ESTB لإدارة امتحانات ISTQB وإصدار شهاداتها في مصر.", "مبتدئ", "ar"),
        StudyResource("ukstqb", "رسمي", "شرح عينات ISTQB الرسمية (فيديو)", yt("ISTQB sample exam CTFL v4.0 explained"), "شروحات فيديو لحل نماذج Sample Exams A, B, C, D الرسمية.", "متوسط", "en", listOf(1, 2, 3, 4, 5, 6)),

        // تعلّم
        StudyResource("maharatech-istqb", "تعلّم", "مهارة-تك (ITI): كورس ISTQB v4.0", "https://maharatech.gov.eg", "منصة معهد تكنولوجيا المعلومات المجانية — ابحث فيها عن كورس «ISTQB Foundation Level V4.0 – From Basics to Certification» ومسار Software Tester.", "مبتدئ", "ar", listOf(1, 2, 3, 4, 5, 6), true),
        StudyResource("testing-arabic", "تعلّم", "Testing بالعربي — دورة CTFL v4.0", "https://testing-arabic.com", "منصة عربية مجانية بدون تسجيل: دورة ISTQB CTFL v4.0 (7 دروس، 5 ساعات) مع شهادة PDF، ودورات Jira وScrum وPostman.", "مبتدئ", "ar", listOf(1, 2, 3, 4, 5, 6), true),
        StudyResource("utest-academy", "تعلّم", "uTest Academy", "https://www.utest.com/academy", "منهج تفاعلي مجاني في اصطياد الـ defects وكتابة التقارير واستخدام أدوات التصوير والسجلات.", "مبتدئ", "en", top = true),
        StudyResource("guru99", "تعلّم", "Guru99 — Software Testing", "https://www.guru99.com/software-testing.html", "دروس مبسطة من الصفر مع أمثلة.", "مبتدئ", "en", listOf(1, 2, 4), true),
        StudyResource("sth", "تعلّم", "Software Testing Help", "https://www.softwaretestinghelp.com", "شروحات كاملة وأسئلة مقابلات وأدوات.", "مبتدئ", "en", listOf(1, 2, 4, 5)),
        StudyResource("stm", "تعلّم", "Software Testing Material", "https://www.softwaretestingmaterial.com", "ملخصات وأسئلة ISTQB ومقابلات.", "مبتدئ", "en", listOf(1, 2, 4, 5)),
        StudyResource("tp", "تعلّم", "Tutorialspoint — Software Testing", "https://www.tutorialspoint.com/software_testing/index.htm", "مرجع سريع مرتب.", "مبتدئ", "en", listOf(1, 2)),
        StudyResource("bs-guide", "تعلّم", "BrowserStack Guide", "https://www.browserstack.com/guide", "أدلة عملية في الاختبار والأتمتة وTest types.", "متوسط", "en", listOf(2, 6)),
        StudyResource("lt-hub", "تعلّم", "LambdaTest Learning Hub", "https://www.lambdatest.com/learning-hub", "دروس مجانية في الاختبار والأتمتة.", "متوسط", "en", listOf(2, 6)),
        StudyResource("testrail-blog", "تعلّم", "TestRail Blog", "https://www.testrail.com/blog/", "مقالات في إدارة الاختبار والـ QA.", "متوسط", "en", listOf(5)),
        StudyResource("fowler-test", "تعلّم", "Martin Fowler — Testing", "https://martinfowler.com/testing/", "مفاهيم هندسية عميقة مثل Test Pyramid.", "متقدم", "en", listOf(2, 5, 6)),
        StudyResource("fowler-pyramid", "تعلّم", "The Practical Test Pyramid", "https://martinfowler.com/articles/practical-test-pyramid.html", "أفضل شرح لـ Test pyramid عملياً.", "متوسط", "en", listOf(5, 6)),
        StudyResource("google-blog", "تعلّم", "Google Testing Blog", "https://testing.googleblog.com", "كيف تختبر الشركات الكبرى.", "متقدم", "en"),
        StudyResource("bbst", "تعلّم", "BBST® Open Course Materials (Cem Kaner)", "https://bbst.courses", "شرائح وفيديوهات BBST المفتوحة بترخيص Creative Commons للتعلّم الذاتي العميق.", "متقدم", "en", listOf(4)),
        StudyResource("satisfice", "تعلّم", "Satisfice (James Bach) — RST", "https://www.satisfice.com", "Rapid Software Testing وHeuristic Test Strategy Model لـ Exploratory testing.", "متقدم", "en", listOf(4)),
        StudyResource("eviltester", "تعلّم", "EvilTester (Alan Richardson)", "https://www.eviltester.com", "مقالات ودورات مجانية وكتب وأدوات في الاختبار اليدوي والأتمتة.", "متوسط", "en"),
        StudyResource("roadmap-qa", "تعلّم", "roadmap.sh — QA Roadmap", "https://roadmap.sh/qa", "خريطة طريق بصرية كاملة لمهندس الجودة.", "مبتدئ", "en", top = true),
        StudyResource("devto-testing", "تعلّم", "DEV — وسم Testing", "https://dev.to/t/testing", "مقالات عملية من مطورين ومختبرين.", "متوسط", "en"),

        // فيديو
        StudyResource("yt-ar", "فيديو", "YouTube: شرح ISTQB بالعربي", yt("ISTQB Foundation Level بالعربي شرح كامل"), "شروحات عربية للمنهج.", "مبتدئ", "ar", top = true),
        StudyResource("yt-ar-qa", "فيديو", "YouTube: كورس Software Testing بالعربي", yt("كورس software testing بالعربي من الصفر"), "كورسات عربية كاملة من الصفر.", "مبتدئ", "ar"),
        StudyResource("yt-v4", "فيديو", "YouTube: ISTQB CTFL v4.0 Full Course", yt("ISTQB CTFL v4.0 full course"), "كورسات إنجليزية للمنهج الجديد.", "مبتدئ", "en", listOf(1, 2, 3, 4, 5, 6)),
        StudyResource("yt-fcc", "فيديو", "freeCodeCamp — Software Testing", yt("freecodecamp software testing course"), "كورسات طويلة بالفيديو.", "مبتدئ", "en"),
        StudyResource("yt-bva", "فيديو", "YouTube: BVA and Equivalence Partitioning", yt("equivalence partitioning boundary value analysis ISTQB examples"), "حلول أمثلة تقنيات التصميم خطوة بخطوة.", "متوسط", "en", listOf(4)),
        StudyResource("yt-dt", "فيديو", "YouTube: Decision Table & State Transition", yt("decision table state transition testing ISTQB example"), "تدريب على أسئلة K3.", "متوسط", "en", listOf(4)),

        // أتمتة
        StudyResource("tau", "أتمتة", "Test Automation University", "https://testautomationuniversity.applitools.com", "كورسات أتمتة مجانية بالكامل بمشاركة خبراء.", "مبتدئ", "en", listOf(6), true),
        StudyResource("playwright", "أتمتة", "Playwright", "https://playwright.dev", "أتمتة الويب الحديثة، سريعة ومستقرة. موصى بها للبداية.", "متوسط", "en", listOf(6), true),
        StudyResource("selenium", "أتمتة", "Selenium", "https://www.selenium.dev/documentation/", "الأشهر في إعلانات الوظائف.", "متوسط", "en", listOf(6)),
        StudyResource("cypress", "أتمتة", "Cypress", "https://docs.cypress.io", "اختبار الويب لمطوري JavaScript.", "متوسط", "en", listOf(6)),
        StudyResource("appium", "أتمتة", "Appium", "https://appium.io", "أتمتة تطبيقات الجوال.", "متقدم", "en", listOf(6)),
        StudyResource("cucumber", "أتمتة", "Cucumber / Gherkin", "https://cucumber.io/docs/", "BDD وصيغة Given-When-Then (مرتبط بالفصل 4).", "متوسط", "en", listOf(4, 6)),
        StudyResource("gh-actions", "أتمتة", "GitHub Actions", "https://docs.github.com/actions", "شغّل اختباراتك تلقائياً مع كل push (CI).", "متوسط", "en", listOf(6)),
        StudyResource("katalon", "أتمتة", "Katalon Academy", "https://academy.katalon.com", "دروس مجانية في الأتمتة بأدوات low-code.", "مبتدئ", "en", listOf(6)),
        StudyResource("selectorshub", "أتمتة", "SelectorsHub", "https://selectorshub.com", "إضافة متصفح لاستخراج واختبار محددات Playwright وXPath وCSS (بما فيها Shadow DOM وiframes).", "متوسط", "en", listOf(6)),

        // API وقواعد بيانات
        StudyResource("postman", "API وقواعد بيانات", "Postman Learning Center", "https://learning.postman.com", "اختبار الـ API: Collections، Tests، Environments.", "مبتدئ", "en", top = true),
        StudyResource("api-challenges", "API وقواعد بيانات", "API Challenges (EvilTester)", "https://apichallenges.eviltester.com", "تحديات تفاعلية لتعلّم اختبار REST API خطوة بخطوة.", "متوسط", "en"),
        StudyResource("jsonph", "API وقواعد بيانات", "JSONPlaceholder", "https://jsonplaceholder.typicode.com", "API وهمي مجاني للتجربة.", "مبتدئ", "en"),
        StudyResource("reqres", "API وقواعد بيانات", "ReqRes", "https://reqres.in", "API جاهز للتجربة (ديمو عام بدون مفتاح + حساب مجاني لـ 250 طلباً يومياً).", "مبتدئ", "en"),
        StudyResource("booker", "API وقواعد بيانات", "Restful-booker", "https://restful-booker.herokuapp.com", "API للحجوزات فيه defects مقصودة للتدريب.", "متوسط", "en"),
        StudyResource("petstore", "API وقواعد بيانات", "Swagger Petstore", "https://petstore.swagger.io", "تعلّم قراءة OpenAPI وتجربة endpoints.", "مبتدئ", "en"),
        StudyResource("sqlbolt", "API وقواعد بيانات", "SQLBolt", "https://sqlbolt.com", "دروس SQL تفاعلية قصيرة. المختبر يحتاجها يومياً.", "مبتدئ", "en", top = true),
        StudyResource("mdn-http", "API وقواعد بيانات", "MDN — HTTP", "https://developer.mozilla.org/en-US/docs/Web/HTTP", "افهم الطلبات والاستجابات وأكواد الحالة والـ headers.", "مبتدئ", "en"),

        // أداء وأمان
        StudyResource("jmeter", "أداء وأمان", "Apache JMeter", "https://jmeter.apache.org", "Performance testing وLoad testing.", "متوسط", "en", listOf(2)),
        StudyResource("k6", "أداء وأمان", "Grafana k6", "https://grafana.com/docs/k6/latest/", "اختبار أداء حديث مكتوب بـ JavaScript.", "متقدم", "en", listOf(2)),
        StudyResource("lighthouse", "أداء وأمان", "Lighthouse", "https://developer.chrome.com/docs/lighthouse/overview", "قياس الأداء وإمكانية الوصول وSEO.", "مبتدئ", "en", listOf(2)),
        StudyResource("owasp-wstg", "أداء وأمان", "OWASP Web Security Testing Guide", "https://owasp.org/www-project-web-security-testing-guide/", "المرجع الأشهر لاختبار أمان الويب.", "متقدم", "en", listOf(2)),
        StudyResource("juice", "أداء وأمان", "OWASP Juice Shop", "https://owasp.org/www-project-juice-shop/", "متجر ضعيف متعمداً للتدرب على الأمان.", "متقدم", "en"),
        StudyResource("wave", "أداء وأمان", "WAVE — Web Accessibility Evaluation", "https://wave.webaim.org", "فحص إمكانية الوصول (Accessibility).", "مبتدئ", "en", listOf(2)),
        StudyResource("devtools", "أداء وأمان", "Chrome DevTools", "https://developer.chrome.com/docs/devtools", "الأداة الأولى للمختبر: Network وConsole وElements.", "مبتدئ", "en", top = true),

        // تدريب عملي
        StudyResource("sqadojo", "تدريب عملي", "SQA Dojo — محاكاة وأسئلة CTFL v4.0", "https://sqadojo.com", "امتحان محاكاة بـ 40 سؤالاً موزوناً وموقّتاً + تدريبات K1/K2/K3 وقاموس 580+ مصطلح مجاناً.", "مبتدئ", "en", listOf(1, 2, 3, 4, 5, 6), true),
        StudyResource("istqb-expand", "تدريب عملي", "ISTQB ExpandTesting — اختبارات مجانية", "https://istqb.expandtesting.com", "بنك اختبارات مجاني لـ Foundation وAgile بدون تسجيل.", "مبتدئ", "en", listOf(1, 2, 3, 4, 5, 6)),
        StudyResource("academybugs", "تدريب عملي", "AcademyBugs", "https://academybugs.com/find-bugs/", "موقع فيه 25 defects مزروعة: ابحث عنها ووثّقها بتقرير defect مكتمل.", "مبتدئ", "en", top = true),
        StudyResource("saucedemo", "تدريب عملي", "Sauce Demo", "https://www.saucedemo.com", "موقع تجارة تجريبي بمستخدمين مختلفين (منهم مستخدم به مشاكل).", "مبتدئ", "en", top = true),
        StudyResource("the-internet", "تدريب عملي", "The Internet (Herokuapp)", "https://the-internet.herokuapp.com", "حالات جاهزة: alerts وiframes وuploads.", "مبتدئ", "en"),
        StudyResource("pst", "تدريب عملي", "Practice Software Testing", "https://practicesoftwaretesting.com", "متجر كامل لتدريب الاختبار والأتمتة وAPI.", "متوسط", "en", top = true),
        StudyResource("qa-practice", "تدريب عملي", "QA Practice", "https://qa-practice.netlify.app/", "نماذج وصفحات للتدرب على الاختبار اليدوي والأتمتة.", "مبتدئ", "en"),
        StudyResource("expand-practice", "تدريب عملي", "Practice ExpandTesting", "https://practice.expandtesting.com", "موقع تدريب غني على واجهة المستخدم وREST API.", "مبتدئ", "en"),
        StudyResource("testpages", "تدريب عملي", "TestPages (EvilTester)", "https://testpages.eviltester.com", "صفحات تجريبية متنوعة لتجربة سيناريوهات الاختبار والأتمتة.", "متوسط", "en"),
        StudyResource("autoex", "تدريب عملي", "Automation Exercise", "https://automationexercise.com", "موقع تجارة إلكترونية للتدريب.", "مبتدئ", "en"),
        StudyResource("demoqa", "تدريب عملي", "DEMOQA", "https://demoqa.com", "عناصر واجهة متنوعة للتدرب على المحددات (Locators).", "مبتدئ", "en"),
        StudyResource("uitp", "تدريب عملي", "UI Testing Playground", "https://uitestingplayground.com", "حالات صعبة في الأتمتة: عناصر ديناميكية وتأخير.", "متوسط", "en"),
        StudyResource("pta", "تدريب عملي", "Practice Test Automation", "https://practicetestautomation.com", "صفحات تدريب وشروحات بسيطة.", "مبتدئ", "en"),
        StudyResource("bugmagnet", "تدريب عملي", "Bug Magnet (إضافة متصفح)", "https://bugmagnet.org", "إضافة مفتوحة المصدر لـ Chrome/Firefox تحقن قيم حدودية وحالات خاصة في الحقول بالزر الأيمن.", "مبتدئ", "en", listOf(4)),

        // أساسيات تقنية
        StudyResource("git-branching", "أساسيات تقنية", "Learn Git Branching", "https://learngitbranching.js.org", "تعلم Git بصرياً وتفاعلياً.", "مبتدئ", "en"),
        StudyResource("javascript-info", "أساسيات تقنية", "javascript.info", "https://javascript.info", "أفضل مرجع مجاني لتعلم JavaScript (لأتمتة Playwright/Cypress).", "متوسط", "en"),
        StudyResource("python-tut", "أساسيات تقنية", "Python Tutorial (الرسمي)", "https://docs.python.org/3/tutorial/", "للأتمتة بـ Python/Selenium/pytest.", "متوسط", "en"),

        // مجتمع ووظائف
        StudyResource("mot", "مجتمع ووظائف", "Ministry of Testing", "https://www.ministryoftesting.com", "أكبر مجتمع للمختبرين: مقالات ومحاضرات ونقاش.", "مبتدئ", "en", top = true),
        StudyResource("testtribe", "مجتمع ووظائف", "The Test Tribe", "https://thetesttribe.com", "مجتمع كبير للمختبرين: فعاليات مجانية ومجتمع Discord.", "مبتدئ", "en"),
        StudyResource("stw", "مجتمع ووظائف", "Software Testing Weekly", "https://softwaretestingweekly.com", "نشرة أسبوعية مجانية بخلاصة مقالات وأدوات المجال.", "مبتدئ", "en"),
        StudyResource("reddit", "مجتمع ووظائف", "r/softwaretesting", "https://www.reddit.com/r/softwaretesting/", "نقاشات ونصائح وظيفية.", "مبتدئ", "en"),
        StudyResource("linkedin-jobs", "مجتمع ووظائف", "وظائف QA على LinkedIn", "https://www.linkedin.com/jobs/search/?keywords=QA%20engineer", "اقرأ متطلبات الوظائف الحقيقية وابنِ خطتك عليها.", "مبتدئ", "en"),
        StudyResource("linkedin-ar", "مجتمع ووظائف", "وظائف Software Tester (المنطقة العربية)", "https://www.linkedin.com/jobs/search/?keywords=software%20tester&location=Middle%20East", "فرص في الشرق الأوسط وشمال أفريقيا.", "مبتدئ", "ar"),

        // مصادر خاصة بفصول المنهج (CTFL v4.0)
        // الفصل 1
        StudyResource("g99-principles", "تعلّم", "Guru99 — 7 Principles of Software Testing", "https://www.guru99.com/software-testing-seven-principles.html", "المبادئ السبعة بأمثلة. تنبيه: يسمّي المبدأ الخامس Pesticide paradox، وفي v4.0 اسمه Tests wear out.", "مبتدئ", "en", listOf(1)),
        StudyResource("g99-vv", "تعلّم", "Guru99 — Verification vs Validation", "https://www.guru99.com/verification-v-s-validation-in-a-software-testing.html", "الفرق بين Verification وValidation بأمثلة، وهو من أكثر المفاهيم ورودًا في أسئلة الفصل 1.", "مبتدئ", "en", listOf(1)),
        StudyResource("g99-stlc", "تعلّم", "Guru99 — Software Testing Life Cycle", "https://www.guru99.com/software-testing-life-cycle.html", "شرح مراحل STLC. تنبيه: تقسيمه يختلف عن أنشطة v4.0 (Planning وAnalysis وDesign وImplementation وExecution وCompletion)، فاعتمد تسميات المنهج في الامتحان.", "مبتدئ", "en", listOf(1)),
        StudyResource("g99-condition", "تعلّم", "Guru99 — Test Condition vs Test Scenario", "https://www.guru99.com/test-scenario-vs-test-condition.html", "يوضّح الفرق بين Test condition وTest scenario وTest case.", "مبتدئ", "en", listOf(1, 4)),
        StudyResource("yt-ch1", "فيديو", "YouTube: الفصل 1 — Fundamentals of Testing", yt("ISTQB CTFL v4.0 chapter 1 fundamentals of testing principles test activities"), "نتائج بحث عن شرح الفصل الأول: المبادئ السبعة وأنشطة الاختبار والأدوار.", "مبتدئ", "en", listOf(1)),

        // الفصل 2
        StudyResource("yt-ch2", "فيديو", "YouTube: الفصل 2 — Testing Throughout the SDLC", yt("ISTQB CTFL v4.0 chapter 2 test levels test types maintenance testing"), "نتائج بحث عن شرح الفصل الثاني: Test levels وTest types وMaintenance testing.", "مبتدئ", "en", listOf(2)),

        // الفصل 3
        StudyResource("g99-static", "تعلّم", "Guru99 — What is Static Testing?", "https://www.guru99.com/testing-review.html", "شرح Static testing وأنواع الـ reviews وأدوار المشاركين فيها.", "مبتدئ", "en", listOf(3)),
        StudyResource("g99-static-dyn", "تعلّم", "Guru99 — Static vs Dynamic Testing", "https://www.guru99.com/static-dynamic-testing.html", "مقارنة مباشرة بين Static وDynamic testing، وهي نقطة متكررة في الامتحان.", "مبتدئ", "en", listOf(3)),
        StudyResource("ss-ch3-v4", "تعلّم", "شرائح الفصل 3 بترقيم v4.0 (SlideShare)", "https://www.slideshare.net/slideshow/chapter-3-static-testing-review-v4-0/269918583", "شرائح تتبع أقسام v4.0 (3.1 و3.2) من طرف ثالث وليست رسمية، فطابقها مع المنهج.", "مبتدئ", "en", listOf(3)),
        StudyResource("uio-ch3-qa", "تعلّم", "أسئلة الفصل 3 بإجاباتها (جامعة أوسلو)", "https://www.uio.no/studier/emner/matnat/ifi/IN3240/v26/ukeoppgaver/chapter3withanswers.pdf", "تمارين على Reviews وStatic analysis بإجاباتها من مقرر جامعي. قد تتبع بعض الصياغات إصدار v3.1.", "متوسط", "en", listOf(3)),
        StudyResource("yt-ch3", "فيديو", "YouTube: الفصل 3 — Static Testing", yt("ISTQB CTFL v4.0 chapter 3 static testing review process review types"), "نتائج بحث عن شرح الفصل الثالث: Review process وأدوار المراجعة وأنواعها.", "مبتدئ", "en", listOf(3)),

        // الفصل 4
        StudyResource("yt-ch4", "فيديو", "YouTube: الفصل 4 — Test Analysis and Design", yt("ISTQB CTFL v4.0 chapter 4 test techniques black-box white-box experience-based"), "نتائج بحث عن شرح الفصل الرابع وتقنياته الثلاث.", "مبتدئ", "en", listOf(4)),

        // الفصل 5
        StudyResource("g99-estimation", "تعلّم", "Guru99 — Test Estimation Techniques", "https://www.guru99.com/testing-estimation.html", "تقنيات التقدير ومنها Three-point estimation وWideband Delphi.", "متوسط", "en", listOf(5)),
        StudyResource("testlio-est", "تعلّم", "Testlio — Test Estimation Techniques", "https://testlio.com/blog/test-estimation-techniques", "مثال محلول على صيغة Three-point estimation: E = (O + 4M + P) / 6، وهي نفسها (a + 4m + b) / 6 في المنهج.", "متوسط", "en", listOf(5)),
        StudyResource("ss-ch5-v4", "تعلّم", "شرائح الفصل 5 بترقيم v4.0 (SlideShare)", "https://www.slideshare.net/slideshow/chapter-5-managing-test-activities-v4-0/269918691", "شرائح تتبع أقسام v4.0 من طرف ثالث وليست رسمية: Test plan وEstimation وRisk وDefect management.", "مبتدئ", "en", listOf(5)),
        StudyResource("yt-ch5", "فيديو", "YouTube: الفصل 5 — Managing the Test Activities", yt("ISTQB CTFL v4.0 chapter 5 managing test activities risk estimation defect report"), "نتائج بحث عن شرح الفصل الخامس: Test plan وRisk وEstimation وDefect report.", "مبتدئ", "en", listOf(5)),

        // الفصل 6
        StudyResource("yt-ch6", "فيديو", "YouTube: الفصل 6 — Test Tools", yt("ISTQB CTFL v4.0 chapter 6 test tools benefits risks test automation"), "نتائج بحث عن شرح الفصل السادس: أنواع الأدوات وفوائد الأتمتة ومخاطرها.", "مبتدئ", "en", listOf(6)),
    )
}
