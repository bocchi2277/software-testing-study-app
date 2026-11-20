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
)

object ResourcesData {
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
        StudyResource("linkedin-ar", "مجتمع ووظائف", "وظائف Software Tester (المنطقة العربية)", "https://www.linkedin.com/jobs/search/?keywords=software%20tester&location=Middle%20East", "فرص في الشرق الأوسط وشمال أفريقيا.", "مبتدئ", "ar")
    )
}
