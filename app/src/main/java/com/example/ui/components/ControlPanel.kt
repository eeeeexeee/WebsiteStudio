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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        "Design & Theme" to Icons.Default.Palette,
        "Templates & Layout" to Icons.Default.Style,
        "Content & Bio" to Icons.Default.Edit,
        "Project Cards" to Icons.Default.Work,
        "GitHub Deploy" to Icons.Default.CloudUpload,
        "Saved Drafts" to Icons.Default.Folder
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Tab Navigation Header
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF1E293B),
            contentColor = Color.White,
            edgePadding = 12.dp
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
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTab == index) Color(0xFF6366F1) else Color(0xFF94A3B8)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.White else Color(0xFF94A3B8),
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> DesignTab(viewModel, portfolioState)
                1 -> LayoutTab(viewModel, portfolioState)
                2 -> ContentTab(viewModel, portfolioState)
                3 -> ProjectsTab(viewModel, portfolioState)
                4 -> DeployTab(viewModel, portfolioState, publishUiState)
                5 -> DraftsTab(viewModel, portfolioState, savedPortfolios)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DesignTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ControlCard(title = "Preset Color Palettes", icon = Icons.Default.ColorLens) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorPresets.presets.forEach { preset ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.applyColorPreset(preset) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = preset.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(preset.primary, preset.accent, preset.background, preset.cardBackground).forEach { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(parseHexColor(hex))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    ControlCard(title = "Typography Engine", icon = Icons.Default.Palette) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Google Font Family", color = Color(0xFFCBD5E1), fontSize = 12.sp)

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
                Text("Base Font Size", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                Text("${state.baseFontSizePx}px", color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.baseFontSizePx.toFloat(),
                onValueChange = { viewModel.updateFontSize(it.toInt()) },
                valueRange = 12f..24f,
                colors = controlSliderColors()
            )
        }
    }

    ControlCard(title = "Spacing & Border Radius Sliders", icon = Icons.Default.Palette) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Hero Padding
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Hero Vertical Padding", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                Text("${state.heroVerticalPaddingPx}px", color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.heroVerticalPaddingPx.toFloat(),
                onValueChange = { viewModel.updateHeroPadding(it.toInt()) },
                valueRange = 20f..150f,
                colors = controlSliderColors()
            )

            // Border Radius
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Element Border Radius", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                Text("${state.borderRadiusPx}px", color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.borderRadiusPx.toFloat(),
                onValueChange = { viewModel.updateBorderRadius(it.toInt()) },
                valueRange = 0f..32f,
                colors = controlSliderColors()
            )

            // Container Max Width
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Container Max Width", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                Text("${state.containerMaxWidthPx}px", color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = state.containerMaxWidthPx.toFloat(),
                onValueChange = { viewModel.updateContainerMaxWidth(it.toInt()) },
                valueRange = 800f..1400f,
                colors = controlSliderColors()
            )
        }
    }

    ControlCard(title = "Custom Color Hex Pickers", icon = Icons.Default.Palette) {
        Column {
            ColorPickerItem(
                label = "Primary Brand Color",
                currentHex = state.primaryColorHex,
                presetSwatches = listOf("#6366F1", "#8B5CF6", "#10B981", "#F43F5E", "#0284C7"),
                onColorChange = { viewModel.updateColorHex(primary = it) }
            )
            ColorPickerItem(
                label = "Accent Highlight Color",
                currentHex = state.accentColorHex,
                presetSwatches = listOf("#38BDF8", "#EC4899", "#06B6D4", "#FB923C", "#A855F7"),
                onColorChange = { viewModel.updateColorHex(accent = it) }
            )
            ColorPickerItem(
                label = "Background Color",
                currentHex = state.backgroundColorHex,
                presetSwatches = listOf("#0F172A", "#090D16", "#064E3B", "#FAFAF9", "#F8FAFC"),
                onColorChange = { viewModel.updateColorHex(bg = it) }
            )
            ColorPickerItem(
                label = "Surface / Card Color",
                currentHex = state.cardBackgroundColorHex,
                presetSwatches = listOf("#1E293B", "#131C2E", "#022C22", "#FFFFFF", "#27272A"),
                onColorChange = { viewModel.updateColorHex(cardBg = it) }
            )
            ColorPickerItem(
                label = "Body Text Color",
                currentHex = state.textColorHex,
                presetSwatches = listOf("#F8FAFC", "#ECFDF5", "#FAFAFA", "#1C1917", "#0F172A"),
                onColorChange = { viewModel.updateColorHex(text = it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LayoutTab(viewModel: PortfolioViewModel, state: PortfolioState) {
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
                            fontSize = 13.sp
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
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    ControlCard(title = "Section Visibility Toggles", icon = Icons.Default.Style) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SectionToggleRow("About Me Section", state.showAboutSection) {
                viewModel.toggleSectionVisibility(about = it)
            }
            SectionToggleRow("Skills & Technologies", state.showSkillsSection) {
                viewModel.toggleSectionVisibility(skills = it)
            }
            SectionToggleRow("Featured Projects Section", state.showProjectsSection) {
                viewModel.toggleSectionVisibility(projects = it)
            }
            SectionToggleRow("Contact & Social Footer", state.showContactSection) {
                viewModel.toggleSectionVisibility(contact = it)
            }
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
                label = { Text("About Me Section Paragraph") },
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

    ControlCard(title = "Social Links", icon = Icons.Default.Edit) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state.githubUrl,
                onValueChange = { viewModel.updateSocialLinks(github = it) },
                label = { Text("GitHub Profile Link") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.linkedinUrl,
                onValueChange = { viewModel.updateSocialLinks(linkedin = it) },
                label = { Text("LinkedIn Profile Link") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.twitterUrl,
                onValueChange = { viewModel.updateSocialLinks(twitter = it) },
                label = { Text("X / Twitter Link") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.updateSocialLinks(email = it) },
                label = { Text("Contact Email Address") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProjectsTab(viewModel: PortfolioViewModel, state: PortfolioState) {
    ProjectCardEditor(
        projectCards = state.projectCards,
        onAddCard = { viewModel.addProjectCard(it) },
        onUpdateCard = { viewModel.updateProjectCard(it) },
        onDeleteCard = { viewModel.deleteProjectCard(it) },
        onReorderCards = { viewModel.reorderProjectCards(it) }
    )
}

@Composable
private fun DeployTab(
    viewModel: PortfolioViewModel,
    state: PortfolioState,
    publishUiState: PublishUiState
) {
    val context = LocalContext.current

    ControlCard(title = "GitHub Pages Credentials & Deploy", icon = Icons.Default.CloudUpload) {
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

            OutlinedTextField(
                value = state.commitMessage,
                onValueChange = { viewModel.updateGitHubCredentials(commitMessage = it) },
                label = { Text("Commit Message") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { viewModel.publishToGitHub() },
                enabled = publishUiState !is PublishUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (publishUiState is PublishUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Publishing to GitHub...")
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Publish Website to GitHub Pages", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Status Card Feedback
    when (publishUiState) {
        is PublishUiState.Success -> {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Published Successfully!", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your website was committed to GitHub index.html.",
                        color = Color(0xFFECFDF5),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Live URL: ${publishUiState.liveUrl}",
                        color = Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(publishUiState.liveUrl))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Live Site")
                        }

                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Live URL", publishUiState.liveUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "URL copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Link", color = Color.White)
                        }
                    }
                }
            }
        }
        is PublishUiState.Error -> {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF43F5E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color(0xFFF43F5E))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Deployment Error", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = publishUiState.message,
                        color = Color(0xFFFECDD3),
                        fontSize = 12.sp
                    )
                }
            }
        }
        else -> {}
    }
}

@Composable
private fun DraftsTab(
    viewModel: PortfolioViewModel,
    currentState: PortfolioState,
    savedPortfolios: List<PortfolioState>
) {
    ControlCard(title = "Local Tablet Draft Persistence", icon = Icons.Default.Folder) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = currentState.portfolioName,
                onValueChange = { viewModel.updatePortfolioName(it) },
                label = { Text("Draft Name") },
                colors = controlTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.saveCurrentPortfolio() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Draft")
                }

                OutlinedButton(
                    onClick = { viewModel.createNewDraft() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Draft", color = Color.White)
                }
            }
        }
    }

    ControlCard(title = "Saved Drafts List (${savedPortfolios.size})", icon = Icons.Default.Folder) {
        if (savedPortfolios.isEmpty()) {
            Text("No saved drafts found. Tap 'Save Draft' above to save state to Room.", color = Color(0xFF94A3B8), fontSize = 12.sp)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                savedPortfolios.forEach { draft ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(draft.portfolioName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${draft.authorName} • ${draft.projectCards.size} projects", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.loadPortfolio(draft) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                            ) {
                                Text("Restore", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 13.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF6366F1),
                uncheckedThumbColor = Color(0xFF64748B),
                uncheckedTrackColor = Color(0xFF1E293B)
            )
        )
    }
}

@Composable
private fun ControlCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xCC1E293B)),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x26FFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF818CF8), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
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
