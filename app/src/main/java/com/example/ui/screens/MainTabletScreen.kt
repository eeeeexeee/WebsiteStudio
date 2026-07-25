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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.PortfolioViewModel

@Composable
fun MainTabletScreen(viewModel: PortfolioViewModel) {
    val portfolioState by viewModel.portfolioState.collectAsState()
    val generatedHtml by viewModel.generatedHtml.collectAsState()
    val savedPortfolios by viewModel.savedPortfolios.collectAsState()
    val publishUiState by viewModel.publishUiState.collectAsState()

    var showFullPreviewDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F172A)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar Header - Frosted Glass Style
            Surface(
                color = Color(0xE61E293B),
                modifier = Modifier.border(
                    width = (0.5).dp,
                    color = Color(0x26FFFFFF)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & App Name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF6366F1))
                                .border(1.dp, Color(0x4DFFFFFF), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Web,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "WebStudio Tablet",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Frosted Glass Visual Website Builder",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Header Right Actions - Frosted Glass Status Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x1F10B981))
                                .border(1.dp, Color(0x4D10B981), CircleShape)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "⚡ Frosted Glass Live WYSIWYG Engine",
                                color = Color(0xFF34D399),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Main Split-Screen 2-Pane Body
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Pane (40% width): Control Panel
                ControlPanel(
                    viewModel = viewModel,
                    portfolioState = portfolioState,
                    savedPortfolios = savedPortfolios,
                    publishUiState = publishUiState,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.40f)
                )

                // Vertical Glass Divider Line
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color(0x1FA1A1AA))
                )

                // Right Pane (60% width): Real-Time WYSIWYG Live Preview
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.60f)
                        .background(Color(0xFF090D16))
                ) {
                    // Preview Pane Header Toolbar (Glass style)
                    Surface(
                        color = Color(0xCC1E293B),
                        modifier = Modifier.border(
                            width = (0.5).dp,
                            color = Color(0x1FFFFFFF)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Live Website Canvas",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Copy HTML Code Button
                                IconButton(onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Website HTML", generatedHtml)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "HTML copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Copy HTML",
                                        tint = Color(0xFF94A3B8)
                                    )
                                }

                                // Refresh Preview
                                IconButton(onClick = { refreshTrigger++ }) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Refresh Preview",
                                        tint = Color(0xFF94A3B8)
                                    )
                                }

                                // Full Preview Button - Glass Pill Button
                                Button(
                                    onClick = { showFullPreviewDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF6366F1),
                                        contentColor = Color.White
                                    ),
                                    shape = CircleShape,
                                    modifier = Modifier.border(1.dp, Color(0x66FFFFFF), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Fullscreen,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Full Preview", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Live WebView Frame
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(16.dp))
                            .key1(refreshTrigger)
                    ) {
                        LiveWebView(
                            htmlContent = generatedHtml,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    // Modal Fullscreen Preview with Viewport Frame Switcher
    if (showFullPreviewDialog) {
        FullPreviewDialog(
            htmlContent = generatedHtml,
            onDismiss = { showFullPreviewDialog = false }
        )
    }
}

// Helper key Modifier
private fun Modifier.key1(key: Any?): Modifier = this
