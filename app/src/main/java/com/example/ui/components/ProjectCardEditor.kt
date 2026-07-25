package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.data.model.ProjectCard

@Composable
fun ProjectCardEditor(
    projectCards: List<ProjectCard>,
    onAddCard: (ProjectCard) -> Unit,
    onUpdateCard: (ProjectCard) -> Unit,
    onDeleteCard: (String) -> Unit,
    onReorderCards: (List<ProjectCard>) -> Unit
) {
    var cardToEdit by remember { mutableStateOf<ProjectCard?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Reorder drag tracking variables (Rule 1: Stable IDs)
    var draggingId by remember { mutableStateOf<String?>(null) }
    var dragAccumulatedOffset by remember { mutableStateOf(0f) }

    val currentList by rememberUpdatedState(projectCards)
    val currentDraggingId by rememberUpdatedState(draggingId)

    val localDensity = LocalDensity.current
    val itemHeightPx = with(localDensity) { 90.dp.toPx() + 8.dp.toPx() } // Card height estimate
    val hysteresisPx = with(localDensity) { 16.dp.toPx() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Project Cards (${projectCards.size})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.border(1.dp, Color(0x4DFFFFFF), RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Project", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (projectCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xCC0F172A), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No project cards added. Tap 'Add Project' above.",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(projectCards, key = { _, item -> item.id }) { index, item ->
                    val isDraggingThis = draggingId == item.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(if (isDraggingThis) 1f else 0f)
                            .graphicsLayer {
                                // Rule 2: GPU thread translation & shadow elevation
                                if (isDraggingThis) {
                                    translationY = dragAccumulatedOffset
                                    scaleX = 1.03f
                                    scaleY = 1.03f
                                    shadowElevation = 16.dp.toPx()
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xCC1E293B)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDraggingThis) Color(0xFF6366F1) else Color(0x26FFFFFF)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Drag Handle Icon
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .pointerInput(item.id) {
                                        detectDragGestures(
                                            onDragStart = {
                                                val liveIndex = currentList.indexOfFirst { it.id == item.id }
                                                if (liveIndex != -1) {
                                                    draggingId = item.id
                                                    dragAccumulatedOffset = 0f
                                                }
                                            },
                                            onDragEnd = {
                                                draggingId = null
                                                dragAccumulatedOffset = 0f
                                            },
                                            onDragCancel = {
                                                draggingId = null
                                                dragAccumulatedOffset = 0f
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragAccumulatedOffset += dragAmount.y

                                                val liveIndex = currentList.indexOfFirst { it.id == item.id }
                                                if (liveIndex != -1 && currentDraggingId == item.id) {
                                                    if (dragAccumulatedOffset > (itemHeightPx / 2f + hysteresisPx)) {
                                                        val nextIndex = liveIndex + 1
                                                        if (nextIndex in currentList.indices) {
                                                            val list = currentList.toMutableList()
                                                            val temp = list[liveIndex]
                                                            list[liveIndex] = list[nextIndex]
                                                            list[nextIndex] = temp

                                                            onReorderCards(list)
                                                            dragAccumulatedOffset -= itemHeightPx
                                                        }
                                                    } else if (dragAccumulatedOffset < -(itemHeightPx / 2f + hysteresisPx)) {
                                                        val prevIndex = liveIndex - 1
                                                        if (prevIndex in currentList.indices) {
                                                            val list = currentList.toMutableList()
                                                            val temp = list[liveIndex]
                                                            list[liveIndex] = list[prevIndex]
                                                            list[prevIndex] = temp

                                                            onReorderCards(list)
                                                            dragAccumulatedOffset += itemHeightPx
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.DragHandle,
                                    contentDescription = "Drag to reorder",
                                    tint = Color(0xFF94A3B8)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = item.description,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Tags: ${item.tags}",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }

                            // Action buttons
                            IconButton(
                                onClick = { cardToEdit = item },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit card",
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = { onDeleteCard(item.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete card",
                                    tint = Color(0xFFF43F5E),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        ProjectCardDialog(
            card = null,
            onDismiss = { showAddDialog = false },
            onSave = { newCard ->
                onAddCard(newCard)
                showAddDialog = false
            }
        )
    }

    // Edit Dialog
    cardToEdit?.let { card ->
        ProjectCardDialog(
            card = card,
            onDismiss = { cardToEdit = null },
            onSave = { updatedCard ->
                onUpdateCard(updatedCard)
                cardToEdit = null
            }
        )
    }
}

@Composable
fun ProjectCardDialog(
    card: ProjectCard?,
    onDismiss: () -> Unit,
    onSave: (ProjectCard) -> Unit
) {
    var title by remember { mutableStateOf(card?.title ?: "") }
    var description by remember { mutableStateOf(card?.description ?: "") }
    var tags by remember { mutableStateOf(card?.tags ?: "Kotlin, Compose") }
    var repoUrl by remember { mutableStateOf(card?.repoUrl ?: "") }
    var demoUrl by remember { mutableStateOf(card?.demoUrl ?: "") }
    var gradientPreset by remember { mutableStateOf(card?.gradientPreset ?: "indigo_violet") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xE61E293B),
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (card == null) "Add Project Card" else "Edit Project Card",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title") },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short Description") },
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = repoUrl,
                    onValueChange = { repoUrl = it },
                    label = { Text("GitHub Repo URL") },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = demoUrl,
                    onValueChange = { demoUrl = it },
                    label = { Text("Live Demo URL") },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Preset selector
                Text(
                    text = "Header Color Accent Preset",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val presets = listOf("indigo_violet", "emerald_teal", "sunset_orange", "midnight_cyan", "rose_amber")
                    presets.forEach { presetKey ->
                        val isSel = gradientPreset == presetKey
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when (presetKey) {
                                        "emerald_teal" -> Color(0xFF10B981)
                                        "sunset_orange" -> Color(0xFFF43F5E)
                                        "midnight_cyan" -> Color(0xFF38BDF8)
                                        "rose_amber" -> Color(0xFFEC4899)
                                        else -> Color(0xFF6366F1)
                                    }
                                )
                                .border(
                                    width = if (isSel) 3.dp else 1.dp,
                                    color = if (isSel) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { gradientPreset = presetKey }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    ProjectCard(
                                        id = card?.id ?: java.util.UUID.randomUUID().toString(),
                                        title = title,
                                        description = description,
                                        tags = tags,
                                        repoUrl = repoUrl,
                                        demoUrl = demoUrl,
                                        gradientPreset = gradientPreset
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                    ) {
                        Text("Save Card")
                    }
                }
            }
        }
    }
}

@Composable
private fun dialogTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF6366F1),
    unfocusedBorderColor = Color(0x26FFFFFF),
    focusedContainerColor = Color(0xCC0F172A),
    unfocusedContainerColor = Color(0x660F172A),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = Color(0xFF818CF8),
    unfocusedLabelColor = Color(0xFF94A3B8)
)
