package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppTheme
import com.example.data.model.ColorPresets
import com.example.data.model.FontFamilyOption
import com.example.data.model.HeaderTemplate
import com.example.data.model.HeroTemplate
import com.example.data.model.PortfolioState
import com.example.ui.viewmodel.PortfolioViewModel
import com.example.ui.viewmodel.PublishUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlPanel(
    viewModel: PortfolioViewModel,
    portfolioState: PortfolioState,
    savedPortfolios: List<PortfolioState>,
    publishUiState: PublishUiState,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Tree" to Icons.Default.AccountTree,
        "Theme" to Icons.Default.Palette,
        "Inspector" to Icons.Default.Tune,
        "Templates" to Icons.Default.Style,
        "Typography" to Icons.Default.TextFields,
        "Content" to Icons.Default.Edit,
        "Export" to Icons.Default.CloudUpload
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Tab Navigation Header (Design Tool Mode Selector)
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E293B),
            contentColor = Color.White,
            edgePadding = 8.dp
        ) {
            tabs.forEachIndexed { index, (title, icon) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                icon,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = if (selectedTab == index) Color(0xFF6366F1) else Color(0xFF94A3B8)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.White else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                )
            }
        }

        // Scrollable Control Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                0 -> ComponentTreeTab(viewModel, portfolioState, onOpenInspector = { selectedTab = 2 })
                1 -> ThemeTab(viewModel, portfolioState)
                2 -> FigmaInspectorTab(viewModel, portfolioState)
                3 -> TemplatesTab(viewModel, portfolioState)
                4 -> TypographyTab(viewModel, portfolioState)
                5 -> ContentTab(viewModel, portfolioState)
                6 -> ExportTab(viewModel, portfolioState, savedPortfolios, publishUiState)
            }
        }
    }
}

@Composable
private fun ComponentTreeTab(
    viewModel: PortfolioViewModel,
    state: PortfolioState,
    onOpenInspector: () -> Unit
) {
    ControlCard(title = "Real Component Tree", icon = Icons.Default.AccountTree) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Click a section to inspect its properties in Figma Inspector or toggle visibility:",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
            )

            // Root Node
            TreeNodeRow(
                label = "Portfolio (Root Container)",
                depth = 0,
                isSelected = state.selectedTreeNode == "GLOBAL",
                isVisible = true,
                onSelect = { viewModel.selectTreeNode("GLOBAL") },
                onToggleVisible = null
            )

            // Header Node
            TreeNodeRow(
                label = "Header (Navigation Bar)",
                depth = 1,
                isSelected = state.selectedTreeNode == "HEADER",
                isVisible = true,
                onSelect = { viewModel.selectTreeNode("HEADER") },
                onToggleVisible = null
            )

            // Hero Node
            TreeNodeRow(
                label = "Hero (Showcase)",
                depth = 1,
                isSelected = state.selectedTreeNode == "HERO",
                isVisible = true,
                onSelect = { viewModel.selectTreeNode("HERO") },
                onToggleVisible = null
            )

            // About Node
            TreeNodeRow(
                label = "About Me",
                depth = 1,
                isSelected = state.selectedTreeNode == "ABOUT",
                isVisible = state.showAboutSection,
                onSelect = { viewModel.selectTreeNode("ABOUT") },
                onToggleVisible = { viewModel.toggleSectionVisibility(about = it) }
            )

            // Skills Node
            TreeNodeRow(
                label = "Skills & Tech",
                depth = 1,
                isSelected = state.selectedTreeNode == "SKILLS",
                isVisible = state.showSkillsSection,
                onSelect = { viewModel.selectTreeNode("SKILLS") },
                onToggleVisible = { viewModel.toggleSectionVisibility(skills = it) }
            )

            // Experience Node
            TreeNodeRow(
                label = "Work Experience",
                depth = 1,
                isSelected = state.selectedTreeNode == "EXPERIENCE",
                isVisible = state.showExperienceSection,
                onSelect = { viewModel.selectTreeNode("EXPERIENCE") },
                onToggleVisible = { viewModel.toggleSectionVisibility(experience = it) }
            )

            // Projects Node
            TreeNodeRow(
                label = "Featured Projects",
                depth = 1,
                isSelected = state.selectedTreeNode == "PROJECTS",
                isVisible = state.showProjectsSection,
                onSelect = { viewModel.selectTreeNode("PROJECTS") },
                onToggleVisible = { viewModel.toggleSectionVisibility(projects = it) }
            )

            // Testimonials Node
            TreeNodeRow(
                label = "Testimonials",
                depth = 1,
                isSelected = state.selectedTreeNode == "TESTIMONIALS",
                isVisible = state.showTestimonialsSection,
                onSelect = { viewModel.selectTreeNode("TESTIMONIALS") },
                onToggleVisible = { viewModel.toggleSectionVisibility(testimonials = it) }
            )

            // Contact Node
            TreeNodeRow(
                label = "Contact Section",
                depth = 1,
                isSelected = state.selectedTreeNode == "CONTACT",
                isVisible = state.showContactSection,
                onSelect = { viewModel.selectTreeNode("CONTACT") },
                onToggleVisible = { viewModel.toggleSectionVisibility(contact = it) }
            )

            // Footer Node
            TreeNodeRow(
                label = "Footer & Links",
                depth = 1,
                isSelected = state.selectedTreeNode == "FOOTER",
                isVisible = state.showFooterSection,
                onSelect = { viewModel.selectTreeNode("FOOTER") },
                onToggleVisible = { viewModel.toggleSectionVisibility(footer = it) }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onOpenInspector,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Inspect '${state.selectedTreeNode}' in Figma Inspector →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TreeNodeRow(
    label: String,
    depth: Int,
    isSelected: Boolean,
    isVisible: Boolean,
    onSelect: () -> Unit,
    onToggleVisible: ((Boolean) -> Unit)?
) {
    val prefix = if (depth == 0) "❖ " else " ├── "
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF312E81) else Color(0xFF0F172A)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF6366F1) else Color(0xFF334155)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = prefix,
                    color = Color(0xFF818CF8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = if (isVisible) Color.White else Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }

            if (onToggleVisible != null) {
                IconButton(
                    onClick = { onToggleVisible(!isVisible) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Section",
                        tint = if (isVisible) Color(0xFF38BDF8) else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(title = "Design System Themes", icon = Icons.Default.Palette) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AppTheme.entries.filter { it != AppTheme.CUSTOM }.forEach { theme ->
                val isSelected = state.currentTheme == theme
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.applyTheme(theme) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E1B4B) else Color(0xFF0F172A)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) Color(0xFF818CF8) else Color(0xFF334155)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = theme.displayName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF6366F1))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("ACTIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = theme.description,
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    theme.primaryColorHex,
                                    theme.accentColorHex,
                                    theme.backgroundColorHex,
                                    theme.cardBackgroundColorHex,
                                    theme.textColorHex
                                ).forEach { hex ->
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(parseHexColor(hex))
                                            .border(1.dp, Color(0x33FFFFFF), CircleShape)
                                    )
                                }
                            }

                            Text(
                                text = "${theme.fontFamily.displayName} • ${theme.borderRadiusPx}px",
                                color = Color(0xFF38BDF8),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FigmaInspectorTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(
        title = "Figma Inspector — ${state.selectedTreeNode}",
        icon = Icons.Default.Tune
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            Text(
                "Fine-tune spacing, radius, layout direction, typography & colors for ${state.selectedTreeNode}:",
                color = Color(0xFFCBD5E1),
                fontSize = 11.sp
            )

            // Padding Sliders
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Header Vertical Padding", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.headerPaddingVerticalPx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.headerPaddingVerticalPx.toFloat(),
                onValueChange = { viewModel.updateLayoutSpacing(headerPaddingV = it.toInt()) },
                valueRange = 8f..60f,
                colors = controlSliderColors()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Section Padding Top / Bottom", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.sectionPaddingTopPx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.sectionPaddingTopPx.toFloat(),
                onValueChange = { viewModel.updateLayoutSpacing(sectionPaddingTop = it.toInt(), sectionPaddingBottom = it.toInt()) },
                valueRange = 20f..150f,
                colors = controlSliderColors()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Card Inner Padding", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.cardPaddingPx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.cardPaddingPx.toFloat(),
                onValueChange = { viewModel.updateLayoutSpacing(cardPadding = it.toInt()) },
                valueRange = 12f..48f,
                colors = controlSliderColors()
            )

            // Border Radius Slider
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Border Radius", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.borderRadiusPx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.borderRadiusPx.toFloat(),
                onValueChange = { viewModel.updateBorderRadius(it.toInt()) },
                valueRange = 0f..32f,
                colors = controlSliderColors()
            )

            // Gap Slider
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Flex & Grid Gap Spacing", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.gapPx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.gapPx.toFloat(),
                onValueChange = { viewModel.updateLayoutSpacing(gap = it.toInt()) },
                valueRange = 8f..64f,
                colors = controlSliderColors()
            )

            // Flex Direction & Align
            Text("Flex Direction", color = Color(0xFFCBD5E1), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("row" to "Row (Horizontal)", "column" to "Column (Vertical)").forEach { (value, label) ->
                    val isSelected = state.layoutFlexDirection == value
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateFlexLayout(flexDirection = value) },
                        label = { Text(label, fontSize = 11.sp, maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6366F1),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text("Justify Content", color = Color(0xFFCBD5E1), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                listOf(
                    "space-between" to "Space Between",
                    "center" to "Center",
                    "flex-start" to "Start",
                    "flex-end" to "End"
                ).forEach { (value, label) ->
                    val isSelected = state.layoutJustifyContent == value
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateFlexLayout(justifyContent = value) },
                        label = { Text(label, fontSize = 10.sp, maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6366F1),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Colors
            Text("Color Inspector", color = Color(0xFFCBD5E1), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            ColorPickerItem(
                label = "Primary Brand Color",
                currentHex = state.primaryColorHex,
                presetSwatches = listOf("#6366F1", "#007AFF", "#238636", "#D0BCFF", "#FF3366"),
                onColorChange = { viewModel.updateColorHex(primary = it) }
            )
            ColorPickerItem(
                label = "Accent Color",
                currentHex = state.accentColorHex,
                presetSwatches = listOf("#38BDF8", "#EC4899", "#58A6FF", "#00F0FF", "#00E676"),
                onColorChange = { viewModel.updateColorHex(accent = it) }
            )
        }
    }
}

@Composable
private fun TemplatesTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(title = "Header Template Picker", icon = Icons.Default.Style) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HeaderTemplate.entries.forEach { template ->
                val isSelected = state.headerTemplate == template
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateHeaderTemplate(template) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF312E81) else Color(0xFF0F172A)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF6366F1) else Color(0xFF334155)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = template.displayName,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    ControlCard(title = "Hero Layout Template Picker", icon = Icons.Default.Style) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HeroTemplate.entries.forEach { template ->
                val isSelected = state.heroTemplate == template
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateHeroTemplate(template) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF312E81) else Color(0xFF0F172A)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF6366F1) else Color(0xFF334155)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = template.displayName,
                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypographyTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(title = "Typography Engine", icon = Icons.Default.TextFields) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Google Font Family", color = Color(0xFFCBD5E1), fontSize = 11.sp)

            var fontExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = fontExpanded,
                onExpandedChange = { fontExpanded = !fontExpanded }
            ) {
                OutlinedTextField(
                    value = state.fontFamily.displayName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fontExpanded) },
                    colors = controlTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = fontExpanded,
                    onDismissRequest = { fontExpanded = false },
                    modifier = Modifier.background(Color(0xFF1E293B))
                ) {
                    FontFamilyOption.entries.forEach { fontOption ->
                        DropdownMenuItem(
                            text = { Text(fontOption.displayName, color = Color.White) },
                            onClick = {
                                viewModel.updateFontFamily(fontOption)
                                fontExpanded = false
                            }
                        )
                    }
                }
            }

            // Font size slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Base Font Size", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("${state.baseFontSizePx}px", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.baseFontSizePx.toFloat(),
                onValueChange = { viewModel.updateFontSize(it.toInt()) },
                valueRange = 12f..24f,
                colors = controlSliderColors()
            )
        }
    }
}

@Composable
private fun ContentTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(title = "Author & Bio Content", icon = Icons.Default.Edit) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state.authorName,
                onValueChange = { viewModel.updateAuthorContent(authorName = it) },
                label = { Text("Author / Brand Name") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.statusBadge,
                onValueChange = { viewModel.updateAuthorContent(statusBadge = it) },
                label = { Text("Status Badge (e.g. Open to Hire)") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.heroTitle,
                onValueChange = { viewModel.updateAuthorContent(heroTitle = it) },
                label = { Text("Hero Heading Title") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.heroSubtitle,
                onValueChange = { viewModel.updateAuthorContent(heroSubtitle = it) },
                label = { Text("Hero Subtitle Text") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.aboutMeText,
                onValueChange = { viewModel.updateAuthorContent(aboutMeText = it) },
                label = { Text("About Me Paragraph") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    ControlCard(title = "Skills List (Comma Separated)", icon = Icons.Default.Edit) {
        OutlinedTextField(
            value = state.skillsList.joinToString(", "),
            onValueChange = { viewModel.updateSkillsString(it) },
            label = { Text("Skills (e.g. Kotlin, Compose, React)") },
            colors = controlTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    ControlCard(title = "Featured Projects (${state.projectCards.size})", icon = Icons.Default.Work) {
        ProjectCardEditor(
            projectCards = state.projectCards,
            onAddCard = { viewModel.addProjectCard(it) },
            onUpdateCard = { viewModel.updateProjectCard(it) },
            onDeleteCard = { viewModel.deleteProjectCard(it) },
            onReorderCards = { viewModel.reorderProjectCards(it) }
        )
    }
}

@Composable
private fun ExportTab(
    viewModel: PortfolioViewModel,
    state: PortfolioState,
    savedPortfolios: List<PortfolioState>,
    publishUiState: PublishUiState
) {
    val context = LocalContext.current

    ControlCard(title = "GitHub Pages Deploy", icon = Icons.Default.CloudUpload) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state.githubUsername,
                onValueChange = { viewModel.updateGitHubCredentials(username = it) },
                label = { Text("GitHub Username") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.githubRepo,
                onValueChange = { viewModel.updateGitHubCredentials(repo = it) },
                label = { Text("Repository Name (e.g. portfolio)") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.githubPat,
                onValueChange = { viewModel.updateGitHubCredentials(pat = it) },
                label = { Text("Personal Access Token (PAT)") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.publishToGitHub() },
                enabled = publishUiState !is PublishUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                if (publishUiState is PublishUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publishing...", fontSize = 12.sp)
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish to GitHub Pages", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }

    ControlCard(title = "Local Draft Persistence", icon = Icons.Default.Folder) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.portfolioName,
                onValueChange = { viewModel.updatePortfolioName(it) },
                label = { Text("Draft Title") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.saveCurrentPortfolio() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Draft", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.createNewDraft() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Draft", color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ControlCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xCC1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x26FFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF818CF8), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun controlTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF6366F1),
    unfocusedBorderColor = Color(0x26FFFFFF),
    focusedContainerColor = Color(0xCC0F172A),
    unfocusedContainerColor = Color(0x660F172A),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = Color(0xFF818CF8),
    unfocusedLabelColor = Color(0xFF94A3B8)
)

@Composable
private fun controlSliderColors() = SliderDefaults.colors(
    thumbColor = Color(0xFF38BDF8),
    activeTrackColor = Color(0xFF6366F1),
    inactiveTrackColor = Color(0xFF334155)
)
