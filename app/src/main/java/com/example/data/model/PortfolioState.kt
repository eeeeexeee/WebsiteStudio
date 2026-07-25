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

data class ExperienceItem(
    val id: String = UUID.randomUUID().toString(),
    val company: String,
    val role: String,
    val period: String,
    val description: String
)

data class TestimonialItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val title: String,
    val company: String = "",
    val quote: String
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

enum class AppTheme(
    val displayName: String,
    val description: String,
    val primaryColorHex: String,
    val accentColorHex: String,
    val backgroundColorHex: String,
    val cardBackgroundColorHex: String,
    val textColorHex: String,
    val fontFamily: FontFamilyOption,
    val borderRadiusPx: Int,
    val shadowStyle: String = "standard" // "standard", "none", "brutalist", "glow"
) {
    APPLE(
        displayName = "Apple Frosted Glass",
        description = "Sleek translucent frosted glass with soft backdrop blur, refined accents & Apple-style geometry",
        primaryColorHex = "#007AFF",
        accentColorHex = "#30B0C7",
        backgroundColorHex = "#EBF2FA",
        cardBackgroundColorHex = "rgba(255, 255, 255, 0.65)",
        textColorHex = "#1D1D1F",
        fontFamily = FontFamilyOption.JAKARTA,
        borderRadiusPx = 20,
        shadowStyle = "frosted_glass"
    ),
    LINEAR(
        displayName = "Linear",
        description = "Futuristic dark slate canvas with purple glow & crisp borders",
        primaryColorHex = "#5E6AD2",
        accentColorHex = "#7050E5",
        backgroundColorHex = "#0B0C10",
        cardBackgroundColorHex = "#151722",
        textColorHex = "#F3F4F6",
        fontFamily = FontFamilyOption.INTER,
        borderRadiusPx = 12,
        shadowStyle = "glow"
    ),
    VERCEL(
        displayName = "Vercel",
        description = "Ultra-minimal monochrome design with high contrast & crisp lines",
        primaryColorHex = "#FFFFFF",
        accentColorHex = "#0070F3",
        backgroundColorHex = "#000000",
        cardBackgroundColorHex = "#111111",
        textColorHex = "#FFFFFF",
        fontFamily = FontFamilyOption.INTER,
        borderRadiusPx = 8,
        shadowStyle = "none"
    ),
    GITHUB(
        displayName = "GitHub",
        description = "Developer-favorite dark palette with emerald commit green & blue accents",
        primaryColorHex = "#238636",
        accentColorHex = "#58A6FF",
        backgroundColorHex = "#0D1117",
        cardBackgroundColorHex = "#161B22",
        textColorHex = "#C9D1D9",
        fontFamily = FontFamilyOption.INTER,
        borderRadiusPx = 6,
        shadowStyle = "standard"
    ),
    MATERIAL_3(
        displayName = "Material 3",
        description = "Expressive M3 organic dark palette with rounded pill geometry",
        primaryColorHex = "#D0BCFF",
        accentColorHex = "#38BDF8",
        backgroundColorHex = "#141218",
        cardBackgroundColorHex = "#2B2930",
        textColorHex = "#E6E1E5",
        fontFamily = FontFamilyOption.OUTFIT,
        borderRadiusPx = 24,
        shadowStyle = "standard"
    ),
    GLASS(
        displayName = "Glass",
        description = "Luminous glassmorphic gradient theme with glowing cyan highlights",
        primaryColorHex = "#818CF8",
        accentColorHex = "#38BDF8",
        backgroundColorHex = "#0F172A",
        cardBackgroundColorHex = "#1E293B",
        textColorHex = "#F8FAFC",
        fontFamily = FontFamilyOption.JAKARTA,
        borderRadiusPx = 20,
        shadowStyle = "glow"
    ),
    BRUTALIST(
        displayName = "Brutalist",
        description = "Stark neo-brutalist theme with thick 3px black borders & offset shadows",
        primaryColorHex = "#FF3366",
        accentColorHex = "#00E676",
        backgroundColorHex = "#FFFBEB",
        cardBackgroundColorHex = "#FFFFFF",
        textColorHex = "#000000",
        fontFamily = FontFamilyOption.FIRA_CODE,
        borderRadiusPx = 0,
        shadowStyle = "brutalist"
    ),
    RETRO(
        displayName = "Retro Arcade",
        description = "Nostalgic 80s arcade neon synthwave palette with pixel/monospace vibes",
        primaryColorHex = "#FF007F",
        accentColorHex = "#00F0FF",
        backgroundColorHex = "#1A0933",
        cardBackgroundColorHex = "#2D124D",
        textColorHex = "#FFE5EC",
        fontFamily = FontFamilyOption.FIRA_CODE,
        borderRadiusPx = 4,
        shadowStyle = "glow"
    ),
    CUSTOM(
        displayName = "Custom",
        description = "Manually customized colors, typography, and spacing settings",
        primaryColorHex = "#6366F1",
        accentColorHex = "#38BDF8",
        backgroundColorHex = "#0F172A",
        cardBackgroundColorHex = "#1E293B",
        textColorHex = "#F8FAFC",
        fontFamily = FontFamilyOption.JAKARTA,
        borderRadiusPx = 16,
        shadowStyle = "standard"
    )
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

    // Active Theme & Component Inspector State
    val currentTheme: AppTheme = AppTheme.APPLE,
    val selectedTreeNode: String = "GLOBAL", // "GLOBAL", "HEADER", "HERO", "ABOUT", "SKILLS", "EXPERIENCE", "PROJECTS", "TESTIMONIALS", "CONTACT", "FOOTER"

    // Layout & Templates
    val headerTemplate: HeaderTemplate = HeaderTemplate.GLASSMORPHIC,
    val heroTemplate: HeroTemplate = HeroTemplate.BIG_TYPOGRAPHY,
    val showAboutSection: Boolean = true,
    val showSkillsSection: Boolean = true,
    val showExperienceSection: Boolean = true,
    val showProjectsSection: Boolean = true,
    val showTestimonialsSection: Boolean = true,
    val showContactSection: Boolean = true,
    val showFooterSection: Boolean = true,

    // Typography & Styling
    val fontFamily: FontFamilyOption = FontFamilyOption.JAKARTA,
    val baseFontSizePx: Int = 16,
    val heroVerticalPaddingPx: Int = 80,
    val borderRadiusPx: Int = 20,
    val containerMaxWidthPx: Int = 1100,

    // Layout Spacing & Interactive Flex Controls
    val headerPaddingVerticalPx: Int = 20,
    val headerPaddingHorizontalPx: Int = 24,
    val sectionPaddingTopPx: Int = 70,
    val sectionPaddingBottomPx: Int = 70,
    val cardPaddingPx: Int = 28,
    val gapPx: Int = 24,
    val layoutFlexDirection: String = "row", // "row" or "column"
    val layoutAlignItems: String = "center", // "flex-start", "center", "flex-end"
    val layoutJustifyContent: String = "space-between", // "space-between", "center", "flex-start", "flex-end"

    // Colors
    val primaryColorHex: String = "#007AFF",
    val accentColorHex: String = "#30B0C7",
    val backgroundColorHex: String = "#EBF2FA",
    val textColorHex: String = "#1D1D1F",
    val cardBackgroundColorHex: String = "rgba(255, 255, 255, 0.65)",

    // Profile & Content
    val authorName: String = "Alex Rivera",
    val heroTitle: String = "Crafting Exceptional Android & Web Digital Experiences",
    val heroSubtitle: String = "Senior Mobile Architect & Frontend Engineer specializing in Jetpack Compose, Kotlin Multiplatform, and modern reactive web UI design.",
    val statusBadge: String = "⚡ Open to Freelance & Full-time Roles",
    val aboutMeText: String = "Passionate product developer with 6+ years of building native mobile applications and interactive web systems. Focused on buttery-smooth UI performance, crisp design systems, and developer tooling.",

    // Skills
    val skillsList: List<String> = listOf("Kotlin", "Jetpack Compose", "Android SDK", "TypeScript", "React / Next.js", "GraphQL", "Room DB", "Firebase", "Git & CI/CD"),

    // Experience List
    val experienceList: List<ExperienceItem> = listOf(
        ExperienceItem(
            company = "Google / AI Studio",
            role = "Staff Mobile Architect",
            period = "2022 — Present",
            description = "Leading next-gen Android app developer tools, streaming UI preview engines, and Kotlin Compose build pipelines."
        ),
        ExperienceItem(
            company = "Vercel Labs",
            role = "Senior Frontend Specialist",
            period = "2020 — 2022",
            description = "Architected high-performance edge web components and cloud deployment interfaces."
        )
    ),

    // Testimonials List
    val testimonialsList: List<TestimonialItem> = listOf(
        TestimonialItem(
            name = "Sarah Chen",
            title = "VP of Engineering",
            company = "TechScale Inc",
            quote = "Alex transformed our developer interface into an intuitive power tool. Attention to detail and speed of delivery are unmatched."
        ),
        TestimonialItem(
            name = "David Kistler",
            title = "Lead Product Designer",
            company = "DesignSystem Co",
            quote = "Exceptional understanding of layout physics, typography, and crisp animations."
        )
    ),

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

