package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalQaColors

private data class BugCheckRule(
    val id: String,
    val label: String,
    val tip: String,
    val ok: (String) -> Boolean
)

private val LABEL_REGEX = Regex("^\\s*(title|العنوان)\\s*[:：]\\s*", RegexOption.IGNORE_CASE)
private val GENERIC_REGEX = Regex(
    "^(bug|issue|error|problem|not working|doesn'?t work|لا يعمل|مش شغال|خطأ|مشكلة|عطل)[\\s!.؟?]*$",
    RegexOption.IGNORE_CASE
)
private val STEPS_REGEX = Regex("^\\s*\\d+\\s*[.)\\-]\\s*\\S", setOf(RegexOption.MULTILINE))
private val ACTUAL_REGEX = Regex("(actual|النتيجة الفعلية|الفعلي|ما حدث|what happens)", RegexOption.IGNORE_CASE)
private val EXPECTED_REGEX = Regex("(expected|النتيجة المتوقعة|المتوقع)", RegexOption.IGNORE_CASE)
private val ENV_REGEX = Regex(
    "(environment|البيئة|build|version|الإصدار|الاصدار|chrome|firefox|safari|edge|android|ios|windows|macos|linux|browser|المتصفح)",
    RegexOption.IGNORE_CASE
)
private val REPRO_REGEX = Regex(
    "(reproducib|frequency|التكرار|تكرار|\\d+\\s*(of|/|من)\\s*\\d+|always|intermittent|every time|دائماً|دائما|أحياناً|احيانا|كل مرة)",
    RegexOption.IGNORE_CASE
)
private val SEV_REGEX = Regex("(severity|priority|الخطورة|الأولوية|الاولوية)", RegexOption.IGNORE_CASE)

private val BUG_CHECKS = listOf(
    BugCheckRule(
        id = "title",
        label = "Title محدد",
        tip = "اذكر المكان والمشكلة والشرط، مثل: Checkout: زر الدفع لا يستجيب عند تطبيق كوبون.",
        ok = { t ->
            val lines = t.lines().filter { it.isNotBlank() }
            val firstLine = (lines.firstOrNull { LABEL_REGEX.containsMatchIn(it) } ?: lines.firstOrNull() ?: "")
                .replace(LABEL_REGEX, "")
                .trim()
            firstLine.length in 15..160 && !GENERIC_REGEX.matches(firstLine)
        }
    ),
    BugCheckRule(
        id = "steps",
        label = "Steps to reproduce مرقّمة",
        tip = "اكتب خطوتين مرقّمتين على الأقل (1. 2. ...) بأفعال واضحة.",
        ok = { t -> STEPS_REGEX.findAll(t).count() >= 2 }
    ),
    BugCheckRule(
        id = "actual",
        label = "Actual result (النتيجة الفعلية)",
        tip = "أضف سطراً بعنوان Actual result أو «النتيجة الفعلية» يصف ما حدث.",
        ok = { t -> ACTUAL_REGEX.containsMatchIn(itOr(t)) }
    ),
    BugCheckRule(
        id = "expected",
        label = "Expected result (النتيجة المتوقعة)",
        tip = "أضف سطراً بعنوان Expected result أو «النتيجة المتوقعة» مع مرجعها إن وجد.",
        ok = { t -> EXPECTED_REGEX.containsMatchIn(t) }
    ),
    BugCheckRule(
        id = "env",
        label = "Environment (البيئة)",
        tip = "اذكر الإصدار (Build) والنظام والمتصفح أو الجهاز.",
        ok = { t -> ENV_REGEX.containsMatchIn(t) }
    ),
    BugCheckRule(
        id = "repro",
        label = "Reproducibility (معدل التكرار)",
        tip = "اكتب كم مرة تكرر الـ defect، مثل: 3 من 3 محاولات، أو Always، أو Intermittent.",
        ok = { t -> REPRO_REGEX.containsMatchIn(t) }
    ),
    BugCheckRule(
        id = "sev",
        label = "Severity وPriority",
        tip = "أضف Severity وPriority (أو الخطورة والأولوية).",
        ok = { t -> SEV_REGEX.containsMatchIn(t) }
    )
)

private fun itOr(s: String) = s

@Composable
fun BugCheckerCard() {
    val qa = LocalQaColors.current
    var text by remember { mutableStateOf("") }
    var shown by remember { mutableStateOf(false) }

    val results = remember(text) {
        BUG_CHECKS.map { it to it.ok(text) }
    }
    val score = results.count { it.second }
    val good = score >= 6

    SectionCard {
        Text(
            text = "🔎 مدقّق Defect report",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "الصق تقريرك (عربي أو إنجليزي) لتفحص اكتمال عناصره السبعة. هذا فحص للشكل فقط، ولا يحكم على جودة المحتوى.",
            style = MaterialTheme.typography.bodyMedium,
            color = qa.muted
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                shown = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bug_checker_input"),
            minLines = 6,
            placeholder = {
                Text("Title: ...\nSteps to reproduce:\n1. ...\n2. ...\nActual result: ...\nExpected result: ...")
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { shown = true },
                enabled = text.isNotBlank(),
                modifier = Modifier.testTag("bug_checker_submit")
            ) {
                Text("افحص التقرير")
            }
            OutlinedButton(
                onClick = {
                    text = ""
                    shown = false
                },
                modifier = Modifier.testTag("bug_checker_clear")
            ) {
                Text("مسح")
            }
        }
        if (shown) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${if (good) "مقبول" else "ناقص"} ($score/${BUG_CHECKS.size})",
                color = if (good) qa.ok else qa.bad,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                results.forEach { (rule, pass) ->
                    Column(modifier = Modifier.padding(vertical = 2.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = if (pass) "✓" else "✗",
                                color = if (pass) qa.ok else qa.bad,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = rule.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if (!pass) {
                            Text(
                                text = rule.tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = qa.muted,
                                modifier = Modifier.padding(start = 18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
