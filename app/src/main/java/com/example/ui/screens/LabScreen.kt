package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeBlockCard
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import kotlin.math.roundToInt

@Composable
fun LabScreen() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "المختبر التفاعلي 🧪",
                sub = "تدرب على تقنيات الامتحان الحسابية (K3) بنفسك."
            )
        }
        item { EpBvaCard() }
        item { DecisionTableCard() }
        item { CoverageSimCard() }
        item { EstimateCard() }
        item { RiskCard() }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EpBvaCard() {
    val qa = LocalQaColors.current
    var label by remember { mutableStateOf("العمر") }
    var minText by remember { mutableStateOf("18") }
    var maxText by remember { mutableStateOf("60") }

    val minVal = minText.toIntOrNull()
    val maxVal = maxText.toIntOrNull()
    val ok = minVal != null && maxVal != null && minVal <= maxVal

    SectionCard {
        Text("1) مولّد EP و BVA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("أدخل مجالاً صالحاً (أعداد صحيحة) لترى الأقسام وقيم الحدود.", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("الحقل") },
                singleLine = true,
                modifier = Modifier.weight(1.2f)
            )
            OutlinedTextField(
                value = minText,
                onValueChange = { minText = it },
                label = { Text("أدنى") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).testTag("ep_min_input")
            )
            OutlinedTextField(
                value = maxText,
                onValueChange = { maxText = it },
                label = { Text("أعلى") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).testTag("ep_max_input")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        if (!ok || minVal == null || maxVal == null) {
            Text("الحد الأدنى يجب ألا يتجاوز الأعلى.", color = qa.bad)
        } else {
            val parts = listOf(
                Triple("$label < $minVal", false, minVal - 5),
                Triple("$minVal ≤ $label ≤ $maxVal", true, (minVal + maxVal) / 2),
                Triple("$label > $maxVal", false, maxVal + 5)
            )
            val twoVal = listOf(minVal - 1, minVal, maxVal, maxVal + 1).distinct()
            val threeVal = listOf(minVal - 1, minVal, minVal + 1, maxVal - 1, maxVal, maxVal + 1).distinct().sorted()

            Text("Equivalence partitions (قيمة تمثيلية من كل قسم)", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            parts.forEach { (partName, valid, sampleVal) ->
                HorizontalDivider(color = qa.border)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LtrContent { Text(partName, modifier = Modifier.weight(1f)) }
                    Text(
                        text = if (valid) "صالح" else "غير صالح",
                        color = if (valid) qa.ok else qa.bad,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    LtrContent { Text("$sampleVal", fontFamily = JetBrainsMono) }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            listOf("2-value BVA" to twoVal, "3-value BVA" to threeVal).forEach { (title, vals) ->
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LtrContent {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vals.forEach { v ->
                            val isValid = v in minVal..maxVal
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isValid) qa.okBg else qa.badBg,
                                border = BorderStroke(1.dp, if (isValid) qa.ok else qa.bad)
                            ) {
                                Text(
                                    text = "$v",
                                    fontFamily = JetBrainsMono,
                                    color = if (isValid) qa.ok else qa.bad,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = "الأخضر = قيمة صالحة، الأحمر = غير صالحة. إجمالي القيم: ${twoVal.size} (2-value) و${threeVal.size} (3-value).",
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DecisionTableCard() {
    val qa = LocalQaColors.current
    val conds = remember { mutableStateListOf("عميل مسجل", "رصيد كافٍ", "عنوان صحيح") }
    val acts = remember { mutableStateMapOf<Int, String>() }
    val n = conds.size
    val numRules = 1 shl n

    SectionCard {
        Text("2) مولّد Decision table", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "عدّل أسماء الشروط (حتى 4)، وسيولّد الجدول الكامل 2^n. اكتب النتيجة المتوقعة لكل قاعدة ثم فكّر أي القواعد يمكن طيّها.",
            style = MaterialTheme.typography.bodyMedium,
            color = qa.muted
        )
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            conds.forEachIndexed { idx, c ->
                OutlinedTextField(
                    value = c,
                    onValueChange = { conds[idx] = it },
                    singleLine = true,
                    label = { Text("شرط ${idx + 1}") },
                    modifier = Modifier.width(140.dp)
                )
            }
            if (n < 4) {
                OutlinedButton(onClick = { conds.add("شرط ${n + 1}") }) { Text("+ شرط") }
            }
            if (n > 1) {
                OutlinedButton(onClick = { conds.removeAt(conds.lastIndex) }) { Text("− شرط") }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column {
                // Header
                Row {
                    Text("الشرط / القاعدة", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(6.dp))
                    for (r in 0 until numRules) {
                        Text("R${r + 1}", fontWeight = FontWeight.Bold, modifier = Modifier.width(72.dp).padding(6.dp))
                    }
                }
                HorizontalDivider(color = qa.border)
                // Conditions
                conds.forEachIndexed { cIdx, cName ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(cName, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(120.dp).padding(6.dp))
                        for (r in 0 until numRules) {
                            val isTrue = ((r shr (n - 1 - cIdx)) and 1) == 0
                            Text(
                                text = if (isTrue) "T" else "F",
                                color = if (isTrue) qa.ok else qa.bad,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(72.dp).padding(6.dp)
                            )
                        }
                    }
                    HorizontalDivider(color = qa.border)
                }
                // Expected actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("الإجراء المتوقع", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp).padding(6.dp))
                    for (r in 0 until numRules) {
                        OutlinedTextField(
                            value = acts[r] ?: "",
                            onValueChange = { acts[r] = it },
                            singleLine = true,
                            modifier = Modifier.width(72.dp).padding(2.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "للحصول على 100% Decision table coverage اختبر كل قاعدة ممكنة ($numRules قاعدة هنا).",
            style = MaterialTheme.typography.labelSmall,
            color = qa.muted
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoverageSimCard() {
    val qa = LocalQaColors.current
    val selected = remember { mutableStateMapOf<String, Boolean>() }
    var showSolution by remember { mutableStateOf(false) }

    val cases = listOf(
        Triple("TT", true, true) to "a>0, b>0",
        Triple("TF", true, false) to "a>0, b≤0",
        Triple("FT", false, true) to "a≤0, b>0",
        Triple("FF", false, false) to "a≤0, b≤0"
    )

    val stmts = mutableSetOf<Int>()
    val branches = mutableSetOf<String>()
    cases.forEach { (tc, _) ->
        if (selected[tc.first] == true) {
            stmts.addAll(listOf(1, 2, 4))
            if (tc.second) stmts.add(3)
            if (tc.third) stmts.add(5)
            branches.add(if (tc.second) "if1-T" else "if1-F")
            branches.add(if (tc.third) "if2-T" else "if2-F")
        }
    }
    val stmtPct = ((stmts.size / 5.0) * 100.0).roundToInt()
    val branchPct = ((branches.size / 4.0) * 100.0).roundToInt()

    SectionCard {
        Text("3) محاكي Statement و Branch Coverage", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        CodeBlockCard(
            code = """1  read(a, b)
2  if (a > 0)
3      x = 1
4  if (b > 0)
5      y = 1"""
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "اختر Test cases (قيمة a وقيمة b) وراقب الـ coverage. جرّب: ما أقل عدد حالات يعطي 100% Statement؟ وما أقل عدد لـ 100% Branch؟",
            style = MaterialTheme.typography.bodyMedium,
            color = qa.muted
        )
        Spacer(modifier = Modifier.height(8.dp))
        LtrContent {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cases.forEach { (tc, label) ->
                    QaFilterChip(
                        text = label,
                        selected = selected[tc.first] == true,
                        onClick = { selected[tc.first] = !(selected[tc.first] ?: false) },
                        testTag = "cov_${tc.first}"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, qa.border),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                    Text("$stmtPct%", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("Statement (${stmts.size}/5)", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                }
            }
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, qa.border),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                    Text("$branchPct%", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("Branch (${branches.size}/4)", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        TextButton(onClick = { showSolution = !showSolution }) {
            Text(if (showSolution) "إخفاء الحل" else "الحل")
        }
        if (showSolution) {
            Text(
                text = "Statement 100%: حالة واحدة تكفي (a>0, b>0). Branch 100%: حالتان على الأقل: (a>0, b>0) و (a≤0, b≤0). هذا يوضح أن Branch أقوى من Statement.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun EstimateCard() {
    val qa = LocalQaColors.current
    var oText by remember { mutableStateOf("2") }
    var mText by remember { mutableStateOf("5") }
    var pText by remember { mutableStateOf("14") }
    val o = oText.toDoubleOrNull()
    val m = mText.toDoubleOrNull()
    val p = pText.toDoubleOrNull()
    val e = if (o != null && m != null && p != null) (o + 4 * m + p) / 6.0 else null
    val sd = if (o != null && p != null) (p - o) / 6.0 else null

    SectionCard {
        Text("4) تقدير الجهد (Three-point)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("E = (a + 4m + b) ÷ 6 — a متفائل، m الأرجح، b متشائم.", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = oText,
                onValueChange = { oText = it },
                label = { Text("a") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = mText,
                onValueChange = { mText = it },
                label = { Text("m") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = pText,
                onValueChange = { pText = it },
                label = { Text("b") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        val eStr = e?.let { ((it * 100.0).roundToInt() / 100.0).toString() } ?: "—"
        val sdStr = sd?.let { ((it * 100.0).roundToInt() / 100.0).toString() } ?: "—"
        Text(
            text = "الجهد المقدّر: $eStr ± $sdStr",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RiskCard() {
    val qa = LocalQaColors.current
    var likelihood by remember { mutableIntStateOf(3) }
    var impact by remember { mutableIntStateOf(4) }
    val lvl = likelihood * impact
    val (label, color) = when {
        lvl >= 15 -> "عالية جداً" to qa.bad
        lvl >= 9 -> "عالية" to qa.warn
        lvl >= 4 -> "متوسطة" to MaterialTheme.colorScheme.primary
        else -> "منخفضة" to qa.ok
    }

    SectionCard {
        Text("5) حاسبة مستوى المخاطرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("المخاطرة = الاحتمال × الأثر (من 1 إلى 5).", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
        Spacer(modifier = Modifier.height(8.dp))
        Text("الاحتمال: $likelihood", style = MaterialTheme.typography.bodyMedium)
        Slider(
            value = likelihood.toFloat(),
            onValueChange = { likelihood = it.roundToInt().coerceIn(1, 5) },
            valueRange = 1f..5f,
            steps = 3
        )
        Text("الأثر: $impact", style = MaterialTheme.typography.bodyMedium)
        Slider(
            value = impact.toFloat(),
            onValueChange = { impact = it.roundToInt().coerceIn(1, 5) },
            valueRange = 1f..5f,
            steps = 3
        )
        Text(
            text = "المستوى: $lvl — $label",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
