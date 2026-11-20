package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.CareerData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.PageHeader
import com.example.ui.components.ReadinessRing
import com.example.ui.components.SectionCard
import com.example.ui.components.openExternalUrl
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlin.math.roundToInt

@Composable
fun CertificateScreen(
    state: AppState,
    viewModel: QaViewModel,
    onStartExam: () -> Unit
) {
    val qa = LocalQaColors.current
    val context = LocalContext.current
    val last = state.exams.take(3)
    val avg = if (last.isNotEmpty()) {
        (last.sumOf { (it.score.toDouble() / it.total) * 100.0 } / last.size).roundToInt()
    } else null
    val ready = avg != null && avg >= 80 && last.size >= 3

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "شهادة ISTQB CTFL 🎓",
                sub = "كل ما تحتاجه لتعرف الامتحان وتحجزه وتجتازه."
            )
        }

        // Readiness check
        item {
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ReadinessRing(pct = StatsUtils.readiness(state), sub = "الجاهزية")
                    Column(modifier = Modifier.weight(1f)) {
                        Text("هل أنت جاهز للحجز؟", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "المعيار الجيد: آخر 3 محاكاة بمتوسط ≥ 80%. " +
                                if (avg == null) "لم تجرِ محاكاة بعد." else "متوسط آخر ${last.size}: $avg%.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = qa.muted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (ready) "✅ نعم، احجز الآن" else "⏳ استمر في التدريب",
                            fontWeight = FontWeight.Bold,
                            color = if (ready) qa.ok else qa.warn
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onStartExam) {
                            Text("ابدأ محاكاة الآن")
                        }
                    }
                }
            }
        }

        // Exam Facts
        item {
            SectionCard {
                Text("حقائق الامتحان", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.EXAM_FACTS.forEachIndexed { idx, f ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(f.key, fontWeight = FontWeight.Bold, modifier = Modifier.width(105.dp))
                        Text(f.value, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Question Distribution
        item {
            SectionCard {
                Text("توزيع الأسئلة على الفصول", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("الفصل", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                    Text("الأسئلة", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                    Text("النسبة", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                    Text("الوقت", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                }
                SyllabusData.CHAPTERS.forEach { c ->
                    HorizontalDivider(color = qa.border)
                    val pct = ((c.examQ / 40.0) * 100.0).roundToInt()
                    val hours = ((c.minutes / 60.0) * 10.0).roundToInt() / 10.0
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(SyllabusData.chapterLabel(c), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(2f))
                        Text("${c.examQ}", modifier = Modifier.weight(0.7f))
                        Text("$pct%", modifier = Modifier.weight(0.7f))
                        Text("$hours س", modifier = Modifier.weight(0.7f))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("الفصلان 4 و5 يشكلان 50% من الامتحان: ركّز عليهما.", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
            }
        }

        // Booking steps
        item {
            SectionCard {
                Text("خطوات الحجز والتحضير", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                CareerData.BOOKING_STEPS.forEachIndexed { idx, b ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    val done = state.progress["cert:${b.id}"] == "1"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setProgress("cert:${b.id}", if (done) null else "1") }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = done,
                            onCheckedChange = { viewModel.setProgress("cert:${b.id}", if (done) null else "1") }
                        )
                        Text(
                            text = b.text,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = if (done) qa.muted else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                TextButton(onClick = {
                    openExternalUrl(context, "https://www.istqb.org/certifications/certified-tester-foundation-level")
                }) {
                    Text("الصفحة الرسمية للشهادة ↗")
                }
            }
        }

        // Exam Tips
        item {
            SectionCard {
                Text("نصائح يوم الامتحان", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.EXAM_TIPS.forEach { t ->
                    Row(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(t, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
