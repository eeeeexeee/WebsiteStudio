package com.example.data.model

import java.util.UUID

data class ProjectCard(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val tags: String, // e.g. "Kotlin, Compose, Room"
    val repoUrl: String = "",
    val demoUrl: String = "",
    val gradientPreset: String = "indigo_violet" // indigo_violet, emerald_teal, sunset_orange, midnight_cyan, rose_amber
)

enum class HeaderTemplate(val displayName: String) {
    MINIMALIST("Minimalist Left"),
    CENTERED_NAV("Centered Navigation"),
    GLASSMORPHIC("Glassmorphic Floating"),
    DARK_GRADIENT("Dark Gradient Bar")
}

enum class HeroTemplate(val displayName: String) {
    BIG_TYPOGRAPHY("Big Typography Left"),
    CENTERED_MINIMAL("Centered Minimal"),
    SPLIT_SCREEN("Split-Screen Showcase"),
    TECH_MINIMAL("Tech Minimalist Code")
}

enum class FontFamilyOption(val displayName: String, val cssFontName: String, val googleFontQuery: String) {
    INTER("Inter", "Inter, sans-serif", "Inter:wght@400;600;700;800"),
    PLAYFAIR("Playfair Display", "'Playfair Display', serif", "Playfair+Display:wght@500;700;900"),
    FIRA_CODE("Fira Code", "'Fira Code', monospace", "Fira+Code:wght@400;600;700"),
    JAKARTA("Plus Jakarta Sans", "'Plus Jakarta Sans', sans-serif", "Plus+Jakarta+Sans:wght@400;600;700;800"),
    OUTFIT("Outfit", "'Outfit', sans-serif", "Outfit:wght@400;600;700;800"),
    MONTSERRAT("Montserrat", "'Montserrat', sans-serif", "Montserrat:wght@400;600;700;800")
}

data class ColorPreset(
    val name: String,
    val primary: String,
    val accent: String,
    val background: String,
    val cardBackground: String,
    val text: String
)

object ColorPresets {
    val presets = listOf(
        ColorPreset(
            name = "Dark Slate (Default)",
            primary = "#6366F1",
            accent = "#38BDF8",
            background = "#0F172A",
            cardBackground = "#1E293B",
            text = "#F8FAFC"
        ),
        ColorPreset(
            name = "Midnight Cyber",
            primary = "#8B5CF6",
            accent = "#EC4899",
            background = "#090D16",
            cardBackground = "#131C2E",
            text = "#F3F4F6"
        ),
        ColorPreset(
            name = "Emerald Developer",
            primary = "#10B981",
            accent = "#06B6D4",
            background = "#064E3B",
            cardBackground = "#022C22",
            text = "#ECFDF5"
        ),
        ColorPreset(
            name = "Warm Organic Light",
            primary = "#D97706",
            accent = "#2563EB",
            background = "#FAFAF9",
            cardBackground = "#FFFFFF",
            text = "#1C1917"
        ),
        ColorPreset(
            name = "Sunset Rose",
            primary = "#F43F5E",
            accent = "#FB923C",
            background = "#18181B",
            cardBackground = "#27272A",
            text = "#FAFAFA"
        ),
        ColorPreset(
            name = "Nordic Clean Light",
            primary = "#0284C7",
            accent = "#0D9488",
            background = "#F8FAFC",
            cardBackground = "#FFFFFF",
            text = "#0F172A"
        )
    )
}

data class PortfolioState(
    val id: String = "default_portfolio",
    val portfolioName: String = "My Developer Portfolio",

    // Layout & Templates
    val headerTemplate: HeaderTemplate = HeaderTemplate.GLASSMORPHIC,
    val heroTemplate: HeroTemplate = HeroTemplate.BIG_TYPOGRAPHY,
    val showAboutSection: Boolean = true,
    val showSkillsSection: Boolean = true,
    val showProjectsSection: Boolean = true,
    val showContactSection: Boolean = true,

    // Typography & Styling
    val fontFamily: FontFamilyOption = FontFamilyOption.JAKARTA,
    val baseFontSizePx: Int = 16,
    val heroVerticalPaddingPx: Int = 80,
    val borderRadiusPx: Int = 16,
    val containerMaxWidthPx: Int = 1100,

    // Colors
    val primaryColorHex: String = "#6366F1",
    val accentColorHex: String = "#38BDF8",
    val backgroundColorHex: String = "#0F172A",
    val textColorHex: String = "#F8FAFC",
    val cardBackgroundColorHex: String = "#1E293B",

    // Profile & Content
    val authorName: String = "Alex Rivera",
    val heroTitle: String = "Crafting Exceptional Android & Web Digital Experiences",
    val heroSubtitle: String = "Senior Mobile Architect & Frontend Engineer specializing in Jetpack Compose, Kotlin Multiplatform, and modern reactive web UI design.",
    val statusBadge: String = "⚡ Open to Freelance & Full-time Roles",
    val aboutMeText: String = "Passionate product developer with 6+ years of building native mobile applications and interactive web systems. Focused on buttery-smooth UI performance, crisp design systems, and developer tooling.",

    // Skills
    val skillsList: List<String> = listOf("Kotlin", "Jetpack Compose", "Android SDK", "TypeScript", "React / Next.js", "GraphQL", "Room DB", "Firebase", "Git & CI/CD"),

    // Social Links
    val githubUrl: String = "https://github.com",
    val linkedinUrl: String = "https://linkedin.com",
    val twitterUrl: String = "https://x.com",
    val email: String = "alex.rivera@example.com",

    // Projects
    val projectCards: List<ProjectCard> = listOf(
        ProjectCard(
            title = "WebStudio Tablet",
            description = "A visual website builder and GitHub publisher app built natively for Android tablets using Jetpack Compose.",
            tags = "Kotlin, Jetpack Compose, Room, OkHttp",
            repoUrl = "https://github.com/example/webstudio",
            demoUrl = "https://example.github.io/webstudio",
            gradientPreset = "indigo_violet"
        ),
        ProjectCard(
            title = "Aura Mindfulness",
            description = "Offline-first meditation app with dynamic audio breathing engine and interactive canvas visualizer.",
            tags = "Android, Canvas, Coroutines, Room",
            repoUrl = "https://github.com/example/aura",
            demoUrl = "https://example.com/aura",
            gradientPreset = "emerald_teal"
        ),
        ProjectCard(
            title = "Flux Crypto Dashboard",
            description = "Real-time cryptocurrency analytics suite with high-frequency WebSocket chart rendering.",
            tags = "Kotlin, WebSockets, Jetpack Compose, Ktor",
            repoUrl = "https://github.com/example/flux",
            demoUrl = "https://example.com/flux",
            gradientPreset = "sunset_orange"
        )
    ),

    // GitHub Deployment Settings
    val githubUsername: String = "",
    val githubRepo: String = "",
    val githubPat: String = "",
    val commitMessage: String = "Publish website update via WebStudio Android App"
)
