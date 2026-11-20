package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.auth.signOut
import com.example.ui.components.ConfirmActionDialog
import com.example.ui.components.PageHeader
import com.example.ui.components.QaDatePickerDialog
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: AppState,
    viewModel: QaViewModel
) {
    val qa = LocalQaColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val currentUser = Firebase.auth.currentUser
    val p = state.profile

    var statusMsg by remember { mutableStateOf("") }
    var showExamDatePicker by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showFocusMenu by remember { mutableStateOf(false) }
    var showBreakMenu by remember { mutableStateOf(false) }

    var pendingImportJson by remember { mutableStateOf<String?>(null) }
    var showClearAttemptsDialog by remember { mutableStateOf(false) }
    var showResetAllDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            val json = viewModel.exportBackupJson()
            context.contentResolver.openOutputStream(uri)?.use { out ->
                out.write(json.toByteArray(Charsets.UTF_8))
            }
            statusMsg = "تم تصدير النسخة الاحتياطية بنجاح."
        }.onFailure {
            statusMsg = "تعذّر حفظ ملف النسخة الاحتياطية."
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            val raw = context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                ?: ""
            if (!raw.contains("\"profile\"")) {
                statusMsg = "الملف ليس نسخة احتياطية صالحة من هذا التطبيق."
            } else {
                pendingImportJson = raw
            }
        }.onFailure {
            statusMsg = "تعذّر قراءة الملف: ليس JSON صالحاً."
        }
    }

    if (showExamDatePicker) {
        QaDatePickerDialog(
            initialDateStr = p.examDate ?: DateUtils.todayStr(),
            allowClear = true,
            onDateSelected = { selected -> viewModel.updateProfile { it.copy(examDate = selected) } },
            onDismiss = { showExamDatePicker = false }
        )
    }

    if (showStartDatePicker) {
        QaDatePickerDialog(
            initialDateStr = p.startDate.ifBlank { DateUtils.todayStr() },
            allowClear = false,
            onDateSelected = { selected ->
                if (!selected.isNullOrBlank()) viewModel.updateProfile { it.copy(startDate = selected) }
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showStartTimePicker) {
        val parts = p.startTime.split(":")
        val initHour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 18
        val initMin = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
        val timeState = rememberTimePickerState(initialHour = initHour, initialMinute = initMin, is24Hour = true)
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            title = { Text("وقت البدء المعتاد", fontWeight = FontWeight.Bold) },
            text = { TimePicker(state = timeState) },
            confirmButton = {
                TextButton(onClick = {
                    val formatted = String.format(Locale.US, "%02d:%02d", timeState.hour, timeState.minute)
                    viewModel.updateProfile { it.copy(startTime = formatted) }
                    showStartTimePicker = false
                }) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) { Text("إلغاء") }
            }
        )
    }

    if (pendingImportJson != null) {
        ConfirmActionDialog(
            title = "استيراد نسخة احتياطية",
            message = "سيتم استبدال بيانات حسابك الحالية بمحتوى الملف. متأكد؟",
            confirmText = "استيراد",
            onConfirm = {
                val raw = pendingImportJson ?: return@ConfirmActionDialog
                scope.launch {
                    val ok = viewModel.importBackupJson(raw)
                    statusMsg = if (ok) "تم استيراد البيانات إلى حسابك بنجاح." else "تعذّر استيراد البيانات: تأكد من صحة الملف."
                }
            },
            onDismiss = { pendingImportJson = null }
        )
    }

    if (showClearAttemptsDialog) {
        ConfirmActionDialog(
            title = "مسح سجل الأسئلة",
            message = "سيتم حذف سجل إجاباتك على الأسئلة من حسابك. متأكد؟",
            confirmText = "مسح السجل",
            onConfirm = {
                viewModel.clearAttempts()
                statusMsg = "تم مسح سجل الأسئلة."
            },
            onDismiss = { showClearAttemptsDialog = false }
        )
    }

    if (showResetAllDialog) {
        ConfirmActionDialog(
            title = "حذف كل البيانات",
            message = "سيتم حذف كل بياناتك (تقدم، جلسات، بطاقات، ملاحظات) من حسابك. لا رجعة فيه. متأكد؟",
            confirmText = "حذف الكل",
            onConfirm = {
                viewModel.resetAll()
                statusMsg = "تمت إعادة تعيين جميع البيانات."
            },
            onDismiss = { showResetAllDialog = false }
        )
    }

    if (showSignOutDialog) {
        ConfirmActionDialog(
            title = "تسجيل الخروج",
            message = "بياناتك محفوظة في حسابك. هل تريد تسجيل الخروج الآن؟",
            confirmText = "خروج",
            isDestructive = false,
            onConfirm = {
                signOut(credentialManager = credentialManager, scope = scope)
            },
            onDismiss = { showSignOutDialog = false }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(title = "الإعدادات ⚙️")
        }

        // Cloud Account Card
        item {
            SectionCard {
                Text("حسابك السحابي", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "الحساب",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.displayName?.takeIf { it.isNotBlank() } ?: "مستخدم Google",
                            fontWeight = FontWeight.Bold
                        )
                        if (!currentUser?.email.isNullOrBlank()) {
                            Text(
                                text = currentUser?.email ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = qa.muted
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = { showSignOutDialog = true },
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = null)
                        Text("خروج")
                    }
                }
            }
        }

        // Theme Settings
        item {
            SectionCard {
                Text("المظهر", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("auto" to "تلقائي", "light" to "فاتح", "dark" to "داكن").forEach { (key, label) ->
                        QaFilterChip(
                            text = label,
                            selected = p.theme == key,
                            onClick = { viewModel.updateProfile { it.copy(theme = key) } },
                            testTag = "theme_$key"
                        )
                    }
                }
            }
        }

        // Study Settings
        item {
            SectionCard {
                Text("الدراسة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("موعد الامتحان", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showExamDatePicker = true },
                            modifier = Modifier.fillMaxWidth().testTag("settings_exam_date_btn")
                        ) {
                            Text(p.examDate ?: "لم يحدد")
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("تاريخ بداية الخطة", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showStartDatePicker = true },
                            modifier = Modifier.fillMaxWidth().testTag("settings_start_date_btn")
                        ) {
                            Text(p.startDate.ifBlank { DateUtils.todayStr() })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ساعات المذاكرة يومياً", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val next = (p.dailyHours - 0.5).coerceAtLeast(0.5)
                                    viewModel.updateProfile { it.copy(dailyHours = next) }
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) { Text("−") }
                            Text(
                                text = "${p.dailyHours} س",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            OutlinedButton(
                                onClick = {
                                    val next = (p.dailyHours + 0.5).coerceAtMost(12.0)
                                    viewModel.updateProfile { it.copy(dailyHours = next) }
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) { Text("+") }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("وقت البدء المعتاد", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showStartTimePicker = true },
                            modifier = Modifier.fillMaxWidth().testTag("settings_start_time_btn")
                        ) {
                            Text(p.startTime)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text("مدة التركيز (دقيقة)", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedButton(
                                onClick = { showFocusMenu = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${p.focusMin} دقيقة")
                            }
                        }
                        DropdownMenu(expanded = showFocusMenu, onDismissRequest = { showFocusMenu = false }) {
                            listOf(15, 25, 30, 45, 50, 60, 90).forEach { mins ->
                                DropdownMenuItem(
                                    text = { Text("$mins دقيقة") },
                                    onClick = {
                                        viewModel.updateProfile { it.copy(focusMin = mins) }
                                        showFocusMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text("مدة الراحة (دقيقة)", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedButton(
                                onClick = { showBreakMenu = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${p.breakMin} دقيقة")
                            }
                        }
                        DropdownMenu(expanded = showBreakMenu, onDismissRequest = { showBreakMenu = false }) {
                            listOf(3, 5, 10, 15).forEach { mins ->
                                DropdownMenuItem(
                                    text = { Text("$mins دقيقة") },
                                    onClick = {
                                        viewModel.updateProfile { it.copy(breakMin = mins) }
                                        showBreakMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Data Backup & Danger Zone
        item {
            SectionCard {
                Text("بياناتك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "بياناتك محفوظة وتُزامَن تلقائياً مع حساب Google الخاص بك في السحابة. يمكنك أيضاً تصدير أو استيراد نسخة احتياطية JSON.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                exportLauncher.launch("qa-path-backup-${DateUtils.todayStr()}.json")
                            },
                            modifier = Modifier.weight(1f).testTag("export_json_button")
                        ) {
                            Text("⬇️ تصدير JSON")
                        }
                        OutlinedButton(
                            onClick = { importLauncher.launch(arrayOf("application/json", "text/plain", "*/*")) },
                            modifier = Modifier.weight(1f).testTag("import_json_button")
                        ) {
                            Text("⬆️ استيراد JSON")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showClearAttemptsDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad),
                            modifier = Modifier.weight(1f).testTag("clear_attempts_button")
                        ) {
                            Text("مسح سجل الأسئلة")
                        }
                        Button(
                            onClick = { showResetAllDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = qa.bad),
                            modifier = Modifier.weight(1f).testTag("reset_all_button")
                        ) {
                            Text("حذف كل البيانات")
                        }
                    }
                }
                if (statusMsg.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = statusMsg,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
