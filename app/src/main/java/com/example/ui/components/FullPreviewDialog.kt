package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

enum class ViewportMode(val title: String, val widthDp: Int?) {
    DESKTOP("Desktop (100%)", null),
    TABLET("Tablet (768px)", 768),
    MOBILE("Mobile (375px)", 375)
}

@Composable
fun FullPreviewDialog(
    htmlContent: String,
    onDismiss: () -> Unit
) {
    var selectedViewport by remember { mutableStateOf(ViewportMode.DESKTOP) }
    var showCodeViewer by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F172A)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Toolbar - Frosted Glass Bar
                Surface(
                    color = Color(0xE61E293B),
                    modifier = Modifier.border(width = (0.5).dp, color = Color(0x26FFFFFF))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Title & Icon
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x336366F1))
                                    .border(1.dp, Color(0x4D6366F1), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeveloperBoard,
                                    contentDescription = null,
                                    tint = Color(0xFF818CF8),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Full Device Frame Preview",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        // Viewport Switcher Controls - Scrollable Frosted Glass Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 8.dp)
                        ) {
                            FilterChip(
                                selected = selectedViewport == ViewportMode.DESKTOP,
                                onClick = { selectedViewport = ViewportMode.DESKTOP },
                                label = { Text("Desktop", maxLines = 1, softWrap = false) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DesktopWindows,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF6366F1),
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color(0x331E293B),
                                    labelColor = Color(0xFF94A3B8)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color(0x26FFFFFF),
                                    selectedBorderColor = Color(0x80FFFFFF),
                                    enabled = true,
                                    selected = selectedViewport == ViewportMode.DESKTOP
                                )
                            )

                            FilterChip(
                                selected = selectedViewport == ViewportMode.TABLET,
                                onClick = { selectedViewport = ViewportMode.TABLET },
                                label = { Text("Tablet (768px)", maxLines = 1, softWrap = false) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Tablet,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF6366F1),
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color(0x331E293B),
                                    labelColor = Color(0xFF94A3B8)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color(0x26FFFFFF),
                                    selectedBorderColor = Color(0x80FFFFFF),
                                    enabled = true,
                                    selected = selectedViewport == ViewportMode.TABLET
                                )
                            )

                            FilterChip(
                                selected = selectedViewport == ViewportMode.MOBILE,
                                onClick = { selectedViewport = ViewportMode.MOBILE },
                                label = { Text("Mobile (375px)", maxLines = 1, softWrap = false) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.PhoneIphone,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF6366F1),
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color(0x331E293B),
                                    labelColor = Color(0xFF94A3B8)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color(0x26FFFFFF),
                                    selectedBorderColor = Color(0x80FFFFFF),
                                    enabled = true,
                                    selected = selectedViewport == ViewportMode.MOBILE
                                )
                            )
                        }

                        // Right Action Buttons
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { showCodeViewer = !showCodeViewer }) {
                                Icon(
                                    Icons.Default.Code,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (showCodeViewer) "Hide HTML" else "View HTML",
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Website HTML", htmlContent)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "HTML code copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy HTML",
                                    tint = Color(0xFF94A3B8)
                                )
                            }

                            IconButton(onClick = onDismiss) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close preview",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // Main Content Body
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF090D16))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showCodeViewer) {
                        // Frosted Glass Code Viewer Card
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xCC1E293B)),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x26FFFFFF))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Generated Semantic HTML5 & CSS3 Source Code",
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xCC0F172A), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = htmlContent,
                                        color = Color(0xFFA5B4FC),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Viewport Web Preview
                        val targetWidth = selectedViewport.widthDp

                        if (targetWidth == null) {
                            // Full Desktop
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(16.dp))
                            ) {
                                LiveWebView(
                                    htmlContent = htmlContent,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            // Device Frame Container with Glass Bezel
                            Card(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .widthIn(max = targetWidth.dp)
                                    .border(
                                        width = 6.dp,
                                        color = Color(0x4D38BDF8),
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xCC0F172A)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // Device Top Notch / Speaker Bar
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(28.dp)
                                            .background(Color(0xCC1E293B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 60.dp, height = 6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x8094A3B8))
                                        )
                                    }

                                    // WebView
                                    LiveWebView(
                                        htmlContent = htmlContent,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
