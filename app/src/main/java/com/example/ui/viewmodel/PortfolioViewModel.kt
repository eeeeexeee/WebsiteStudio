package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PortfolioRepository
import com.example.data.model.ColorPreset
import com.example.data.model.FontFamilyOption
import com.example.data.model.HeaderTemplate
import com.example.data.model.HeroTemplate
import com.example.data.model.PortfolioState
import com.example.data.model.ProjectCard
import com.example.data.remote.GitHubPublisher
import com.example.data.remote.PublishResult
import com.example.generator.WebsiteGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

sealed class PublishUiState {
    object Idle : PublishUiState()
    object Loading : PublishUiState()
    data class Success(val liveUrl: String, val commitSha: String) : PublishUiState()
    data class Error(val message: String) : PublishUiState()
}

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PortfolioRepository
    private val publisher = GitHubPublisher()

    init {
        val dao = AppDatabase.getDatabase(application).portfolioDao()
        repository = PortfolioRepository(dao)
    }

    val savedPortfolios: StateFlow<List<PortfolioState>> = repository.allPortfolios
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _portfolioState = MutableStateFlow(PortfolioState())
    val portfolioState: StateFlow<PortfolioState> = _portfolioState.asStateFlow()

    private val _generatedHtml = MutableStateFlow("")
    val generatedHtml: StateFlow<String> = _generatedHtml.asStateFlow()

    private val _publishUiState = MutableStateFlow<PublishUiState>(PublishUiState.Idle)
    val publishUiState: StateFlow<PublishUiState> = _publishUiState.asStateFlow()

    init {
        // Generate initial HTML
        _generatedHtml.value = WebsiteGenerator.generateHtml(_portfolioState.value)
    }

    private fun updateState(transform: (PortfolioState) -> PortfolioState) {
        _portfolioState.update { currentState ->
            val newState = transform(currentState)
            _generatedHtml.value = WebsiteGenerator.generateHtml(newState)
            newState
        }
    }

    fun updatePortfolioName(name: String) {
        updateState { it.copy(portfolioName = name) }
    }

    fun updateHeaderTemplate(template: HeaderTemplate) {
        updateState { it.copy(headerTemplate = template) }
    }

    fun updateHeroTemplate(template: HeroTemplate) {
        updateState { it.copy(heroTemplate = template) }
    }

    fun applyTheme(theme: com.example.data.model.AppTheme) {
        updateState { current ->
            if (theme == com.example.data.model.AppTheme.CUSTOM) {
                current.copy(currentTheme = theme)
            } else {
                current.copy(
                    currentTheme = theme,
                    primaryColorHex = theme.primaryColorHex,
                    accentColorHex = theme.accentColorHex,
                    backgroundColorHex = theme.backgroundColorHex,
                    cardBackgroundColorHex = theme.cardBackgroundColorHex,
                    textColorHex = theme.textColorHex,
                    fontFamily = theme.fontFamily,
                    borderRadiusPx = theme.borderRadiusPx
                )
            }
        }
    }

    fun selectTreeNode(nodeName: String) {
        updateState { it.copy(selectedTreeNode = nodeName) }
    }

    fun toggleSectionVisibility(
        about: Boolean? = null,
        skills: Boolean? = null,
        experience: Boolean? = null,
        projects: Boolean? = null,
        testimonials: Boolean? = null,
        contact: Boolean? = null,
        footer: Boolean? = null
    ) {
        updateState { current ->
            current.copy(
                showAboutSection = about ?: current.showAboutSection,
                showSkillsSection = skills ?: current.showSkillsSection,
                showExperienceSection = experience ?: current.showExperienceSection,
                showProjectsSection = projects ?: current.showProjectsSection,
                showTestimonialsSection = testimonials ?: current.showTestimonialsSection,
                showContactSection = contact ?: current.showContactSection,
                showFooterSection = footer ?: current.showFooterSection
            )
        }
    }

    fun updateFontFamily(font: FontFamilyOption) {
        updateState { it.copy(fontFamily = font) }
    }

    fun updateFontSize(sizePx: Int) {
        updateState { it.copy(baseFontSizePx = sizePx.coerceIn(12, 24)) }
    }

    fun updateHeroPadding(paddingPx: Int) {
        updateState { it.copy(heroVerticalPaddingPx = paddingPx.coerceIn(20, 150)) }
    }

    fun updateBorderRadius(radiusPx: Int) {
        updateState { it.copy(borderRadiusPx = radiusPx.coerceIn(0, 32)) }
    }

    fun updateContainerMaxWidth(widthPx: Int) {
        updateState { it.copy(containerMaxWidthPx = widthPx.coerceIn(800, 1400)) }
    }

    fun updateLayoutSpacing(
        headerPaddingV: Int? = null,
        headerPaddingH: Int? = null,
        sectionPaddingTop: Int? = null,
        sectionPaddingBottom: Int? = null,
        cardPadding: Int? = null,
        gap: Int? = null
    ) {
        updateState { current ->
            current.copy(
                headerPaddingVerticalPx = headerPaddingV?.coerceIn(4, 80) ?: current.headerPaddingVerticalPx,
                headerPaddingHorizontalPx = headerPaddingH?.coerceIn(8, 100) ?: current.headerPaddingHorizontalPx,
                sectionPaddingTopPx = sectionPaddingTop?.coerceIn(10, 180) ?: current.sectionPaddingTopPx,
                sectionPaddingBottomPx = sectionPaddingBottom?.coerceIn(10, 180) ?: current.sectionPaddingBottomPx,
                cardPaddingPx = cardPadding?.coerceIn(8, 60) ?: current.cardPaddingPx,
                gapPx = gap?.coerceIn(4, 80) ?: current.gapPx
            )
        }
    }

    fun updateFlexLayout(
        flexDirection: String? = null,
        alignItems: String? = null,
        justifyContent: String? = null
    ) {
        updateState { current ->
            current.copy(
                layoutFlexDirection = flexDirection ?: current.layoutFlexDirection,
                layoutAlignItems = alignItems ?: current.layoutAlignItems,
                layoutJustifyContent = justifyContent ?: current.layoutJustifyContent
            )
        }
    }

    fun updateColorHex(
        primary: String? = null,
        accent: String? = null,
        bg: String? = null,
        text: String? = null,
        cardBg: String? = null
    ) {
        updateState { current ->
            current.copy(
                primaryColorHex = primary ?: current.primaryColorHex,
                accentColorHex = accent ?: current.accentColorHex,
                backgroundColorHex = bg ?: current.backgroundColorHex,
                textColorHex = text ?: current.textColorHex,
                cardBackgroundColorHex = cardBg ?: current.cardBackgroundColorHex
            )
        }
    }

    fun applyColorPreset(preset: ColorPreset) {
        updateState { current ->
            current.copy(
                primaryColorHex = preset.primary,
                accentColorHex = preset.accent,
                backgroundColorHex = preset.background,
                textColorHex = preset.text,
                cardBackgroundColorHex = preset.cardBackground
            )
        }
    }

    fun updateAuthorContent(
        authorName: String? = null,
        heroTitle: String? = null,
        heroSubtitle: String? = null,
        statusBadge: String? = null,
        aboutMeText: String? = null
    ) {
        updateState { current ->
            current.copy(
                authorName = authorName ?: current.authorName,
                heroTitle = heroTitle ?: current.heroTitle,
                heroSubtitle = heroSubtitle ?: current.heroSubtitle,
                statusBadge = statusBadge ?: current.statusBadge,
                aboutMeText = aboutMeText ?: current.aboutMeText
            )
        }
    }

    fun updateSocialLinks(
        github: String? = null,
        linkedin: String? = null,
        twitter: String? = null,
        email: String? = null
    ) {
        updateState { current ->
            current.copy(
                githubUrl = github ?: current.githubUrl,
                linkedinUrl = linkedin ?: current.linkedinUrl,
                twitterUrl = twitter ?: current.twitterUrl,
                email = email ?: current.email
            )
        }
    }

    fun updateSkillsString(skillsCsv: String) {
        val list = skillsCsv.split(",").map { it.trim() }.filter { it.isNotBlank() }
        updateState { it.copy(skillsList = list) }
    }

    fun addProjectCard(card: ProjectCard) {
        updateState { current ->
            current.copy(projectCards = current.projectCards + card)
        }
    }

    fun updateProjectCard(card: ProjectCard) {
        updateState { current ->
            val updated = current.projectCards.map { if (it.id == card.id) card else it }
            current.copy(projectCards = updated)
        }
    }

    fun deleteProjectCard(cardId: String) {
        updateState { current ->
            current.copy(projectCards = current.projectCards.filter { it.id != cardId })
        }
    }

    fun reorderProjectCards(reorderedList: List<ProjectCard>) {
        updateState { current ->
            current.copy(projectCards = reorderedList)
        }
    }

    fun updateGitHubCredentials(
        username: String? = null,
        repo: String? = null,
        pat: String? = null,
        commitMessage: String? = null
    ) {
        updateState { current ->
            current.copy(
                githubUsername = username ?: current.githubUsername,
                githubRepo = repo ?: current.githubRepo,
                githubPat = pat ?: current.githubPat,
                commitMessage = commitMessage ?: current.commitMessage
            )
        }
    }

    fun resetPublishStatus() {
        _publishUiState.value = PublishUiState.Idle
    }

    fun publishToGitHub() {
        val state = _portfolioState.value
        _publishUiState.value = PublishUiState.Loading

        viewModelScope.launch {
            val html = WebsiteGenerator.generateHtml(state)
            val result = publisher.publishWebsite(
                username = state.githubUsername,
                repoName = state.githubRepo,
                patToken = state.githubPat,
                htmlContent = html,
                commitMessage = state.commitMessage
            )

            when (result) {
                is PublishResult.Success -> {
                    _publishUiState.value = PublishUiState.Success(
                        liveUrl = result.liveUrl,
                        commitSha = result.commitSha
                    )
                    // Auto save to Room after successful deployment
                    saveCurrentPortfolio()
                }
                is PublishResult.Error -> {
                    _publishUiState.value = PublishUiState.Error(result.message)
                }
            }
        }
    }

    fun saveCurrentPortfolio() {
        viewModelScope.launch {
            repository.savePortfolio(_portfolioState.value)
        }
    }

    fun loadPortfolio(state: PortfolioState) {
        _portfolioState.value = state
        _generatedHtml.value = WebsiteGenerator.generateHtml(state)
    }

    fun createNewDraft() {
        val newId = UUID.randomUUID().toString()
        val newState = PortfolioState(
            id = newId,
            portfolioName = "Draft Portfolio " + (System.currentTimeMillis() / 1000 % 1000)
        )
        _portfolioState.value = newState
        _generatedHtml.value = WebsiteGenerator.generateHtml(newState)
    }
}
