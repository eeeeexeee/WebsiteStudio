package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    var mobileSelectedTab by remember { mutableIntStateOf(0) } // 0 = Editor Controls, 1 = Live Preview
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = Color(0xFF0F172A)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isMobileView = maxWidth < 650.dp

            Column(modifier = Modifier.fillMaxSize()) {
                // App Bar Header - Responsive Frosted Glass Style
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
                            .padding(horizontal = if (isMobileView) 12.dp else 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo & App Name
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
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

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "WebStudio",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isMobileView) "Visual Builder" else "Frosted Glass Visual Builder",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Status Badge - Collapses cleanly on small screen
                        if (!isMobileView) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0x1F10B981))
                                    .border(1.dp, Color(0x4D10B981), CircleShape)
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "⚡ Live WYSIWYG Engine",
                                    color = Color(0xFF34D399),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // Layout Decision: Mobile Switcher vs Tablet Side-by-Side
                if (isMobileView) {
                    // Mobile Top Switcher Tab
                    TabRow(
                        selectedTabIndex = mobileSelectedTab,
                        containerColor = Color(0xFF1E293B),
                        contentColor = Color.White
                    ) {
                        Tab(
                            selected = mobileSelectedTab == 0,
                            onClick = { mobileSelectedTab = 0 },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (mobileSelectedTab == 0) Color(0xFF818CF8) else Color(0xFF94A3B8)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Editor Controls",
                                        color = if (mobileSelectedTab == 0) Color.White else Color(0xFF94A3B8),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        )

                        Tab(
                            selected = mobileSelectedTab == 1,
                            onClick = { mobileSelectedTab = 1 },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.Language,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (mobileSelectedTab == 1) Color(0xFF38BDF8) else Color(0xFF94A3B8)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Live Canvas",
                                        color = if (mobileSelectedTab == 1) Color.White else Color(0xFF94A3B8),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        )
                    }

                    // Mobile Single-Pane Body
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (mobileSelectedTab == 0) {
                            ControlPanel(
                                viewModel = viewModel,
                                portfolioState = portfolioState,
                                savedPortfolios = savedPortfolios,
                                publishUiState = publishUiState,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            LiveCanvasPane(
                                generatedHtml = generatedHtml,
                                refreshTrigger = refreshTrigger,
                                onRefresh = { refreshTrigger++ },
                                onOpenFullPreview = { showFullPreviewDialog = true },
                                context = context,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    // Tablet Side-by-Side 2-Pane Body
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

                        // Right Pane (60% width): Live Canvas Preview
                        LiveCanvasPane(
                            generatedHtml = generatedHtml,
                            refreshTrigger = refreshTrigger,
                            onRefresh = { refreshTrigger++ },
                            onOpenFullPreview = { showFullPreviewDialog = true },
                            context = context,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.60f)
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

@Composable
private fun LiveCanvasPane(
    generatedHtml: String,
    refreshTrigger: Int,
    onRefresh: () -> Unit,
    onOpenFullPreview: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color(0xFF090D16))
    ) {
        // Preview Pane Header Toolbar
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
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Live Canvas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Copy HTML Button
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

                    // Refresh Button
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh Preview",
                            tint = Color(0xFF94A3B8)
                        )
                    }

                    // Full Preview Button
                    Button(
                        onClick = onOpenFullPreview,
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
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Full Preview",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        // Live WebView Frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(16.dp))
        ) {
            LiveWebView(
                htmlContent = generatedHtml,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

