package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CareerData
import com.example.ui.components.BugCheckerCard
import com.example.ui.components.CodeBlockCard
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors

private data class SkillSection(
    val id: String,
    val title: String,
    val intro: String,
    val rules: List<String>,
    val bad: String? = null,
    val tpl: String? = null,
    val exercise: String? = null
)

private val SKILL_SECTIONS = listOf(
    SkillSection(
        id = "tc",
        title = "Test cases",
        intro = "الـ Test case الجيدة يفهمها مختبر آخر وينفذها دون أن يسألك، ونتيجتها المتوقعة قابلة للتحقق.",
        rules = listOf(
            "هدف واحد لكل حالة: إن فشلت عرفت السبب فوراً.",
            "خطوات بأفعال واضحة: افتح، أدخل، اضغط. وليس «جرّب» أو «تأكد».",
            "نتيجة متوقعة قابلة للتحقق، لا «يعمل بشكل صحيح».",
            "تغطي الحالة الإيجابية والسلبية والحدود (Boundary).",
            "اربطها بالمتطلب (Traceability) وحدد الأولوية.",
            "لا تربطها بتفاصيل تنفيذ تتغير كل أسبوع."
        ),
        bad = "Title: Test login\nSteps: Try to login\nExpected: Works",
        tpl = CareerData.TEMPLATES["tc"],
        exercise = "اكتب 5 حالات لصفحة تسجيل الدخول: بيانات صحيحة، كلمة مرور خاطئة، حقل فارغ، حد أقصى للطول، قفل الحساب بعد محاولات فاشلة. ثم نفذها على saucedemo.com."
    ),
    SkillSection(
        id = "bug",
        title = "Defect reports",
        intro = "هدف التقرير أن يستطيع المطور إعادة المشكلة وفهمها وإصلاحها دون سؤال واحد.",
        rules = listOf(
            "عنوان يذكر المكان والمشكلة والشرط، لا «لا يعمل».",
            "أعد إنتاج الـ defect بنفسك قبل الإبلاغ، وسجّل عدد مرات تكراره.",
            "افصل بين Actual وExpected، واذكر مرجع المتوقع.",
            "اذكر البيئة بدقة: الإصدار والنظام والمتصفح.",
            "أرفق دليلاً: لقطة شاشة أو فيديو أو سجل أخطاء (Console / Network).",
            "Defect واحد لكل تقرير، وابحث عن تقرير مكرر قبل الإرسال.",
            "لغة محايدة بلا اتهام أو انفعال."
        ),
        bad = "Payment doesn't work!!!",
        tpl = CareerData.TEMPLATES["bug"],
        exercise = "افتح the-internet.herokuapp.com أو الموقع التدريبي Sauce Demo (مستخدم problem_user) واكتب 3 Defect reports كاملة."
    ),
    SkillSection(
        id = "sev",
        title = "Severity مقابل Priority",
        intro = "من أكثر الأسئلة تكراراً في الامتحان والمقابلات.",
        rules = listOf(
            "Severity: مدى تأثير الـ defect على النظام (تقني) ويحدده غالباً المختبر.",
            "Priority: مدى استعجال الإصلاح (قرار عمل) ويحدده Product Owner/المدير.",
            "عالية Severity + منخفضة Priority: انهيار تقرير إداري نادر الاستخدام.",
            "منخفضة Severity + عالية Priority: خطأ إملائي في اسم الشركة على الصفحة الرئيسية.",
            "عالية + عالية: تعذّر الدفع في متجر إلكتروني.",
            "منخفضة + منخفضة: اختلاف بسيط في لون زر في صفحة نادرة."
        )
    ),
    SkillSection(
        id = "story",
        title = "User Story وAcceptance criteria",
        intro = "مهارة مطلوبة في Agile ومرتبطة بـ ATDD في الفصل 4.",
        rules = listOf(
            "As a [دور] I want [هدف] so that [فائدة].",
            "Acceptance criteria بصيغة Given / When / Then وقابلة للاختبار.",
            "اسأل «ماذا لو...؟» لكشف الغموض قبل التطوير.",
            "تحقق من INVEST: Independent, Negotiable, Valuable, Estimable, Small, Testable."
        ),
        tpl = CareerData.TEMPLATES["story"],
        exercise = "حوّل صفحة «التسجيل» إلى 3 user stories بمعايير قبول."
    ),
    SkillSection(
        id = "charter",
        title = "Exploratory testing (Charter)",
        intro = "جلسة محددة الوقت بهدف واضح، تُسجَّل ملاحظاتها.",
        rules = listOf(
            "حدد المنطقة والأدوات والمعلومة التي تبحث عنها.",
            "وقت محدد: 45–90 دقيقة دون مقاطعة.",
            "دوّن: ما اختبرته، أسئلتك، الـ defects، أفكار الجلسة التالية.",
            "استخدم نماذج تفكير: SFDIPOT وتجاوز الحدود والبيانات الغريبة."
        ),
        tpl = CareerData.TEMPLATES["charter"],
        exercise = "نفّذ جلسة 45 دقيقة على Practice Software Testing: Explore checkout with invalid data to discover validation gaps."
    ),
    SkillSection(
        id = "plan",
        title = "Test plan",
        intro = "وثيقة قصيرة تجيب: ماذا نختبر؟ كيف؟ متى نبدأ وننتهي؟ ما المخاطر؟",
        rules = listOf(
            "حدد النطاق بوضوح: داخل النطاق وخارجه.",
            "اكتب Entry وExit criteria قابلة للقياس.",
            "حدد المخاطر وكيف تتعامل معها (Likelihood × Impact).",
            "اجعلها قصيرة وحيّة وتُحدَّث."
        ),
        tpl = CareerData.TEMPLATES["plan"]
    ),
    SkillSection(
        id = "summary",
        title = "Test summary report",
        intro = "يقرؤه المدير: ما الوضع؟ هل نُصدر؟ ما المخاطر المتبقية؟",
        rules = listOf(
            "ما اختُبر وما لم يُختبر.",
            "أرقام: منفذة، ناجحة، فاشلة، محجوبة.",
            "الـ defects المفتوحة حسب الخطورة.",
            "المخاطر المتبقية والتوصية (Go / No-go)."
        ),
        tpl = CareerData.TEMPLATES["summary"]
    ),
    SkillSection(
        id = "req",
        title = "مراجعة المتطلبات",
        intro = "أرخص مكان لاكتشاف الـ defects (Shift-left): قبل كتابة الكود.",
        rules = listOf(
            "ابحث عن كلمات غامضة: «سريع»، «سهل»، «مناسب»، «إلخ».",
            "ابحث عن متطلبات ناقصة أو متعارضة أو غير قابلة للاختبار.",
            "اسأل عن الحالات الحدية والأخطاء والصلاحيات.",
            "اسأل: كيف سأعرف أن هذا نجح؟ إن لم تعرف فالمتطلب ناقص."
        ),
        exercise = "اقرأ: «يجب أن يكون النظام سريعاً وسهل الاستخدام». اكتب 5 أسئلة توضيحية تجعله قابلاً للاختبار."
    ),
    SkillSection(
        id = "comm",
        title = "التواصل مع المطورين",
        intro = "مهارة ناعمة تحدد نجاحك أكثر مما تتصور.",
        rules = listOf(
            "تحدث عن المنتج لا عن الشخص: «الزر لا يستجيب» لا «أخطأت».",
            "قدّم الدليل وخطوات الإعادة، واعرض مساعدتك.",
            "كن مستعداً لشرح الخطوات مباشرة إذا لم تُفهم.",
            "شاركهم مبكراً: جلسات Three Amigos ومراجعة المتطلبات."
        )
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsScreen(initialSection: String? = null) {
    val qa = LocalQaColors.current
    @Suppress("DEPRECATION")
    val clipboard = LocalClipboardManager.current
    var selectedId by rememberSaveable(initialSection) {
        mutableStateOf(
            SKILL_SECTIONS.firstOrNull { it.id == initialSection }?.id ?: "tc"
        )
    }
    var copied by rememberSaveable(selectedId) { mutableStateOf(false) }
    val section = SKILL_SECTIONS.first { it.id == selectedId }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "المهارات العملية للمختبر 🛠️",
                sub = "ما يُقيَّم فيه المختبر في العمل اليومي، وتظهر منه أسئلة في الامتحان."
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SKILL_SECTIONS.forEach { s ->
                    QaFilterChip(
                        text = s.title,
                        selected = selectedId == s.id,
                        onClick = {
                            selectedId = s.id
                            copied = false
                        }
                    )
                }
            }
        }

        item {
            SectionCard {
                Text(section.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(section.intro, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                Spacer(modifier = Modifier.height(10.dp))
                section.rules.forEach { r ->
                    Row(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(r, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        if (section.bad != null) {
            item {
                Text("❌ سيئة", style = MaterialTheme.typography.labelLarge, color = qa.bad, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                CodeBlockCard(code = section.bad, borderColor = qa.bad, containerColor = qa.badBg)
            }
        }

        if (section.tpl != null) {
            item {
                Text("✅ قالب جيد", style = MaterialTheme.typography.labelLarge, color = qa.ok, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                CodeBlockCard(code = section.tpl, borderColor = qa.ok, containerColor = qa.okBg)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(onClick = {
                    clipboard.setText(AnnotatedString(section.tpl))
                    copied = true
                }) {
                    Text(if (copied) "تم النسخ ✓" else "نسخ القالب")
                }
            }
        }

        if (section.exercise != null) {
            item {
                SectionCard(borderColor = MaterialTheme.colorScheme.primary) {
                    Text("🏋️ تمرين:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(section.exercise, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        if (selectedId == "bug") {
            item { BugCheckerCard() }
        }
    }
}
