package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.ConfirmActionDialog
import com.example.ui.components.PageHeader
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    state: AppState,
    viewModel: QaViewModel
) {
    val qa = LocalQaColors.current
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var isEditorOpen by remember { mutableStateOf(false) }
    var editingNoteId by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var topicId by remember { mutableStateOf("") }
    var statusMsg by remember { mutableStateOf("") }
    var topicMenuOpen by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val filtered = remember(state.notes, query) {
        val s = query.trim().lowercase()
        state.notes.filter { n ->
            s.isEmpty() || n.title.lowercase().contains(s) || n.body.lowercase().contains(s)
        }
    }

    fun openEditor(id: String?) {
        editingNoteId = id
        statusMsg = ""
        if (id == null) {
            title = ""
            body = ""
            topicId = ""
        } else {
            val found = state.notes.firstOrNull { it.id == id }
            if (found != null) {
                title = found.title
                body = found.body
                topicId = found.topic ?: ""
            }
        }
        isEditorOpen = true
    }

    if (showDeleteConfirm && editingNoteId != null) {
        ConfirmActionDialog(
            title = "حذف الملاحظة",
            message = "هل تريد حذف هذه الملاحظة؟",
            confirmText = "حذف",
            onConfirm = {
                editingNoteId?.let { viewModel.deleteNote(it) }
                isEditorOpen = false
                editingNoteId = null
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "ملاحظاتي 🗒️",
                sub = "دوّن المصطلحات الصعبة وأخطاءك ودروسك.",
                trailing = {
                    Button(
                        onClick = { openEditor(null) },
                        modifier = Modifier.testTag("new_note_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Text("ملاحظة جديدة")
                    }
                }
            )
        }

        if (isEditorOpen) {
            item {
                SectionCard(borderColor = MaterialTheme.colorScheme.primary) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("العنوان") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_title_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box {
                        val topicLabel = SyllabusData.ALL_TOPICS.firstOrNull { it.id == topicId }
                            ?.let { "${it.id} — ${it.ar}" } ?: "غير مرتبطة بموضوع"
                        OutlinedButton(
                            onClick = { topicMenuOpen = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = topicLabel,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "اختر الموضوع")
                        }
                        DropdownMenu(
                            expanded = topicMenuOpen,
                            onDismissRequest = { topicMenuOpen = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("غير مرتبطة بموضوع") },
                                onClick = {
                                    topicId = ""
                                    topicMenuOpen = false
                                }
                            )
                            SyllabusData.ALL_TOPICS.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text("${t.id} — ${t.ar}") },
                                    onClick = {
                                        topicId = t.id
                                        topicMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = body,
                        onValueChange = { body = it },
                        label = { Text("النص") },
                        placeholder = { Text("اكتب هنا…") },
                        minLines = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_body_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val savedId = viewModel.saveNote(
                                        id = editingNoteId,
                                        title = title.trim().ifEmpty { "بدون عنوان" },
                                        body = body,
                                        topic = topicId.ifEmpty { null }
                                    )
                                    editingNoteId = savedId
                                    statusMsg = "تم الحفظ ✓"
                                    delay(1800)
                                    statusMsg = ""
                                }
                            },
                            modifier = Modifier.testTag("note_save_button")
                        ) {
                            Text("حفظ")
                        }
                        OutlinedButton(onClick = { isEditorOpen = false }) {
                            Text("إغلاق")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (editingNoteId != null) {
                            TextButton(
                                onClick = { showDeleteConfirm = true },
                                colors = ButtonDefaults.textButtonColors(contentColor = qa.bad),
                                modifier = Modifier.testTag("note_delete_button")
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف")
                                Text("حذف")
                            }
                        }
                    }
                    if (statusMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = statusMsg,
                            color = qa.ok,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("بحث في الملاحظات…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "بحث") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "مسح البحث")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notes_search_input")
            )
        }

        if (filtered.isEmpty()) {
            item {
                SectionCard {
                    Text(
                        text = "لا ملاحظات بعد.",
                        color = qa.muted,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            items(filtered, key = { it.id }) { note ->
                SectionCard(
                    onClick = { openEditor(note.id) },
                    modifier = Modifier.testTag("note_card_${note.id}")
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note.body.ifBlank { "—" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val topicPart = note.topic?.let { "الموضوع $it · " } ?: ""
                    Text(
                        text = "$topicPart${DateUtils.fmtDateArabic(note.updatedAt)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                }
            }
        }
    }
}
