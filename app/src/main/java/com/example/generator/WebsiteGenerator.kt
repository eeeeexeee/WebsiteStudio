package com.example.generator

import com.example.data.model.HeaderTemplate
import com.example.data.model.HeroTemplate
import com.example.data.model.PortfolioState
import com.example.data.model.ProjectCard

object WebsiteGenerator {

    fun generateHtml(state: PortfolioState): String {
        val googleFontLink = "https://fonts.googleapis.com/css2?family=${state.fontFamily.googleFontQuery}&display=swap"
        val fontCssName = state.fontFamily.cssFontName

        val headerHtml = when (state.headerTemplate) {
            HeaderTemplate.MINIMALIST -> generateMinimalistHeader(state)
            HeaderTemplate.CENTERED_NAV -> generateCenteredNavHeader(state)
            HeaderTemplate.GLASSMORPHIC -> generateGlassmorphicHeader(state)
            HeaderTemplate.DARK_GRADIENT -> generateDarkGradientHeader(state)
        }

        val heroHtml = when (state.heroTemplate) {
            HeroTemplate.BIG_TYPOGRAPHY -> generateBigTypographyHero(state)
            HeroTemplate.CENTERED_MINIMAL -> generateCenteredMinimalHero(state)
            HeroTemplate.SPLIT_SCREEN -> generateSplitScreenHero(state)
            HeroTemplate.TECH_MINIMAL -> generateTechMinimalHero(state)
        }

        val aboutHtml = if (state.showAboutSection) generateAboutSection(state) else ""
        val skillsHtml = if (state.showSkillsSection) generateSkillsSection(state) else ""
        val experienceHtml = if (state.showExperienceSection) generateExperienceSection(state) else ""
        val projectsHtml = if (state.showProjectsSection) generateProjectsSection(state) else ""
        val testimonialsHtml = if (state.showTestimonialsSection) generateTestimonialsSection(state) else ""
        val contactHtml = if (state.showContactSection) generateContactSection(state) else ""
        val footerHtml = if (state.showFooterSection) generateFooterSection(state) else ""

        val isDarkText = state.textColorHex.startsWith("#0") || state.textColorHex.startsWith("#1") || state.textColorHex.startsWith("#2") || state.textColorHex.startsWith("#3")
        val textMutedColor = if (isDarkText) "rgba(29, 29, 31, 0.65)" else "rgba(255, 255, 255, 0.65)"

        val cardShadowCss = when (state.currentTheme.shadowStyle) {
            "frosted_glass" -> "background: rgba(255, 255, 255, 0.7) !important; backdrop-filter: blur(25px) saturate(190%) !important; -webkit-backdrop-filter: blur(25px) saturate(190%) !important; border: 1px solid rgba(255, 255, 255, 0.8) !important; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06), inset 0 0 0 1px rgba(255, 255, 255, 0.5) !important;"
            "brutalist" -> "border: 3px solid #000000 !important; box-shadow: 5px 5px 0px #000000 !important; border-radius: ${state.borderRadiusPx}px !important;"
            "glow" -> "box-shadow: 0 0 30px rgba(99, 102, 241, 0.3) !important; border: 1px solid rgba(255, 255, 255, 0.2) !important; border-radius: ${state.borderRadiusPx}px !important;"
            "none" -> "box-shadow: none !important; border: 1px solid rgba(255, 255, 255, 0.12) !important; border-radius: ${state.borderRadiusPx}px !important;"
            else -> "box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15) !important; border: 1px solid rgba(255, 255, 255, 0.12) !important; border-radius: ${state.borderRadiusPx}px !important;"
        }

        return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${escapeHtml(state.authorName)} — Portfolio</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="$googleFontLink" rel="stylesheet">
    <style>
        :root {
            --primary: ${state.primaryColorHex};
            --accent: ${state.accentColorHex};
            --bg: ${state.backgroundColorHex};
            --card-bg: ${state.cardBackgroundColorHex};
            --text: ${state.textColorHex};
            --text-muted: $textMutedColor;
            --border-radius: ${state.borderRadiusPx}px;
            --base-font-size: ${state.baseFontSizePx}px;
            --hero-padding: ${state.heroVerticalPaddingPx}px;
            --max-width: ${state.containerMaxWidthPx}px;
            --font-family: $fontCssName;
            --header-padding-v: ${state.headerPaddingVerticalPx}px;
            --header-padding-h: ${state.headerPaddingHorizontalPx}px;
            --section-padding-top: ${state.sectionPaddingTopPx}px;
            --section-padding-bottom: ${state.sectionPaddingBottomPx}px;
            --card-padding: ${state.cardPaddingPx}px;
            --gap: ${state.gapPx}px;
            --layout-flex-dir: ${state.layoutFlexDirection};
            --layout-align: ${state.layoutAlignItems};
            --layout-justify: ${state.layoutJustifyContent};
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        html {
            scroll-behavior: smooth;
        }

        body {
            font-family: var(--font-family);
            background-color: var(--bg);
            color: var(--text);
            font-size: var(--base-font-size);
            line-height: 1.6;
            min-height: 100vh;
            overflow-x: hidden;
            -webkit-font-smoothing: antialiased;
        }

        a {
            color: var(--primary);
            text-decoration: none;
            transition: all 0.2s ease;
        }

        a:hover {
            color: var(--accent);
            opacity: 0.9;
        }

        .container {
            max-width: var(--max-width);
            margin: 0 auto;
            padding: 0 24px;
        }

        /* Header Styles */
        header {
            width: 100%;
            z-index: 100;
        }

        .header-minimalist {
            padding: var(--header-padding-v) var(--header-padding-h);
            display: flex;
            flex-direction: var(--layout-flex-dir);
            justify-content: var(--layout-justify);
            align-items: var(--layout-align);
            gap: var(--gap);
            width: 100%;
            max-width: var(--max-width);
            margin: 0 auto;
        }

        .header-centered {
            padding: var(--header-padding-v) var(--header-padding-h);
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: var(--gap);
            width: 100%;
            max-width: var(--max-width);
            margin: 0 auto;
        }

        .header-glassmorphic {
            position: sticky;
            top: 16px;
            margin: 16px auto;
            width: calc(100% - (var(--header-padding-h) * 2));
            max-width: var(--max-width);
            background: rgba(255, 255, 255, 0.65);
            backdrop-filter: blur(25px) saturate(190%);
            -webkit-backdrop-filter: blur(25px) saturate(190%);
            border: 1px solid rgba(255, 255, 255, 0.8);
            border-radius: calc(var(--border-radius) * 1.5);
            padding: var(--header-padding-v) var(--header-padding-h);
            display: flex;
            flex-direction: var(--layout-flex-dir);
            justify-content: var(--layout-justify);
            align-items: var(--layout-align);
            gap: var(--gap);
            box-shadow: 0 10px 30px rgba(0,0,0,0.06), inset 0 0 0 1px rgba(255, 255, 255, 0.5);
        }

        .glass-ambient-sphere-1 {
            position: fixed;
            top: -10%;
            right: -10%;
            width: 650px;
            height: 650px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(0, 122, 255, 0.22) 0%, rgba(90, 200, 250, 0.12) 50%, transparent 70%);
            filter: blur(80px);
            pointer-events: none;
            z-index: -1;
        }
        .glass-ambient-sphere-2 {
            position: fixed;
            bottom: -10%;
            left: -10%;
            width: 700px;
            height: 700px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(175, 82, 222, 0.18) 0%, rgba(255, 45, 85, 0.1) 50%, transparent 70%);
            filter: blur(90px);
            pointer-events: none;
            z-index: -1;
        }

        .header-darkgradient {
            background: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, rgba(0,0,0,0) 100%);
            padding: 0;
            width: 100%;
        }

        .logo {
            font-weight: 800;
            font-size: 1.35rem;
            letter-spacing: -0.02em;
            color: var(--text);
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .logo-dot {
            width: 8px;
            height: 8px;
            background: var(--primary);
            border-radius: 50%;
            display: inline-block;
        }

        nav ul {
            display: flex;
            list-style: none;
            gap: 28px;
        }

        nav a {
            color: var(--text-muted);
            font-weight: 500;
            font-size: 0.95rem;
        }

        nav a:hover {
            color: var(--text);
        }

        /* Hero Section */
        .hero-section {
            padding-top: var(--hero-padding);
            padding-bottom: var(--hero-padding);
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 6px 16px;
            background: rgba(99, 102, 241, 0.12);
            border: 1px solid rgba(99, 102, 241, 0.3);
            color: var(--accent);
            border-radius: 100px;
            font-size: 0.85rem;
            font-weight: 600;
            margin-bottom: 24px;
        }

        .badge-dot {
            width: 6px;
            height: 6px;
            background: #10B981;
            border-radius: 50%;
            box-shadow: 0 0 8px #10B981;
        }

        .hero-title {
            font-size: clamp(2.2rem, 5vw, 3.8rem);
            font-weight: 800;
            line-height: 1.15;
            letter-spacing: -0.03em;
            margin-bottom: 20px;
            background: linear-gradient(135deg, var(--text) 30%, var(--primary) 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .hero-subtitle {
            font-size: 1.15rem;
            color: var(--text-muted);
            max-width: 680px;
            margin-bottom: 36px;
            line-height: 1.7;
        }

        .hero-actions {
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
        }

        .btn-primary {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 14px 28px;
            background: var(--primary);
            color: #FFFFFF;
            font-weight: 600;
            border-radius: var(--border-radius);
            box-shadow: 0 10px 25px rgba(99, 102, 241, 0.3);
            transition: all 0.25s ease;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 15px 30px rgba(99, 102, 241, 0.45);
            color: #FFFFFF;
        }

        .btn-secondary {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 14px 28px;
            background: var(--card-bg);
            color: var(--text);
            border: 1px solid rgba(255, 255, 255, 0.12);
            font-weight: 600;
            border-radius: var(--border-radius);
            transition: all 0.25s ease;
        }

        .btn-secondary:hover {
            border-color: var(--primary);
            color: var(--text);
            transform: translateY(-2px);
        }

        /* Tech Minimal Code Window */
        .code-window {
            background: #090D16;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: var(--border-radius);
            padding: 20px;
            font-family: 'Fira Code', monospace;
            font-size: 0.9rem;
            color: #A5B4FC;
            box-shadow: 0 20px 40px rgba(0,0,0,0.5);
            margin-top: 32px;
        }

        .code-header {
            display: flex;
            gap: 8px;
            margin-bottom: 16px;
        }

        .code-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
        }

        .dot-red { background: #EF4444; }
        .dot-yellow { background: #F59E0B; }
        .dot-green { background: #10B981; }

        /* Sections */
        .section {
            padding-top: var(--section-padding-top);
            padding-bottom: var(--section-padding-bottom);
        }

        .section-title {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 12px;
            letter-spacing: -0.02em;
        }

        .section-desc {
            color: var(--text-muted);
            margin-bottom: 40px;
            max-width: 600px;
        }

        /* Cards & Grids */
        .grid-cards {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: var(--gap);
        }

        .card {
            background: var(--card-bg);
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: var(--border-radius);
            padding: var(--card-padding);
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
            position: relative;
            overflow: hidden;
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
        }

        .card:hover {
            transform: translateY(-6px);
            border-color: rgba(255, 255, 255, 0.25);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5), 0 0 20px rgba(99, 102, 241, 0.2);
        }

        .card-preview-bar {
            height: 6px;
            width: 100%;
            position: absolute;
            top: 0;
            left: 0;
        }

        .preset-indigo_violet { background: linear-gradient(90deg, #6366F1, #8B5CF6); }
        .preset-emerald_teal { background: linear-gradient(90deg, #10B981, #06B6D4); }
        .preset-sunset_orange { background: linear-gradient(90deg, #F43F5E, #FB923C); }
        .preset-midnight_cyan { background: linear-gradient(90deg, #38BDF8, #6366F1); }
        .preset-rose_amber { background: linear-gradient(90deg, #EC4899, #F59E0B); }

        .card-title {
            font-size: 1.25rem;
            font-weight: 700;
            margin-bottom: 10px;
            margin-top: 8px;
        }

        .card-desc {
            color: var(--text-muted);
            font-size: 0.92rem;
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .card-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin-bottom: 24px;
        }

        .tag {
            font-size: 0.78rem;
            font-weight: 600;
            padding: 4px 10px;
            background: rgba(255,255,255,0.06);
            border-radius: 6px;
            color: var(--accent);
        }

        .card-links {
            display: flex;
            gap: 16px;
            font-size: 0.9rem;
            font-weight: 600;
        }

        /* Skills Chips */
        .skills-wrap {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
        }

        .skill-chip {
            padding: 10px 20px;
            background: var(--card-bg);
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: var(--border-radius);
            font-weight: 600;
            font-size: 0.92rem;
            display: flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s ease;
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
        }

        .skill-chip:hover {
            border-color: rgba(255, 255, 255, 0.3);
            color: var(--accent);
            transform: translateY(-2px);
        }

        /* About & Footer */
        .about-box {
            background: var(--card-bg);
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: var(--border-radius);
            padding: 36px;
            line-height: 1.8;
            color: var(--text);
            font-size: 1.05rem;
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
        }

        footer {
            border-top: 1px solid rgba(255, 255, 255, 0.08);
            padding: 60px 0 40px;
            margin-top: 80px;
            text-align: center;
        }

        .social-links {
            display: flex;
            justify-content: center;
            gap: 24px;
            margin-bottom: 28px;
        }

        .social-link {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: var(--card-bg);
            border: 1px solid rgba(255, 255, 255, 0.1);
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--text);
            font-weight: 600;
            transition: all 0.2s ease;
        }

        .social-link:hover {
            background: var(--primary);
            color: #FFFFFF;
            transform: scale(1.1);
        }

        .footer-text {
            color: var(--text-muted);
            font-size: 0.88rem;
        }

        @media (max-width: 768px) {
            .hero-title { font-size: 2.2rem; }
            .header-minimalist, .header-centered, .header-glassmorphic {
                flex-direction: column !important;
                align-items: center !important;
                justify-content: center !important;
                text-align: center !important;
                gap: 12px !important;
                padding-left: 16px !important;
                padding-right: 16px !important;
            }
            .header-glassmorphic {
                position: relative !important;
                top: 0 !important;
                width: calc(100% - 24px) !important;
                margin: 12px auto !important;
            }
            nav ul {
                flex-wrap: wrap !important;
                justify-content: center !important;
                gap: 8px 16px !important;
            }
            .grid-cards { grid-template-columns: 1fr !important; }
            .container { padding: 0 16px !important; }
        }
    </style>
</head>
<body>
    <div class="glass-ambient-sphere-1"></div>
    <div class="glass-ambient-sphere-2"></div>

    $headerHtml

    <main class="container">
        $heroHtml
        $aboutHtml
        $skillsHtml
        $experienceHtml
        $projectsHtml
        $testimonialsHtml
        $contactHtml
    </main>

    $footerHtml

</body>
</html>
        """.trimIndent()
    }

    private fun generateMinimalistHeader(state: PortfolioState): String {
        return """
        <header class="container header-minimalist">
            <div class="logo">
                <span class="logo-dot"></span>
                <span>${escapeHtml(state.authorName)}</span>
            </div>
            <nav>
                <ul>
                    ${if (state.showAboutSection) "<li><a href=\"#about\">About</a></li>" else ""}
                    ${if (state.showSkillsSection) "<li><a href=\"#skills\">Skills</a></li>" else ""}
                    ${if (state.showProjectsSection) "<li><a href=\"#projects\">Projects</a></li>" else ""}
                    ${if (state.showContactSection) "<li><a href=\"#contact\">Contact</a></li>" else ""}
                </ul>
            </nav>
        </header>
        """.trimIndent()
    }

    private fun generateCenteredNavHeader(state: PortfolioState): String {
        return """
        <header class="container header-centered">
            <div class="logo">
                <span class="logo-dot"></span>
                <span>${escapeHtml(state.authorName)}</span>
            </div>
            <nav>
                <ul>
                    ${if (state.showAboutSection) "<li><a href=\"#about\">About</a></li>" else ""}
                    ${if (state.showSkillsSection) "<li><a href=\"#skills\">Skills</a></li>" else ""}
                    ${if (state.showProjectsSection) "<li><a href=\"#projects\">Projects</a></li>" else ""}
                    ${if (state.showContactSection) "<li><a href=\"#contact\">Contact</a></li>" else ""}
                </ul>
            </nav>
        </header>
        """.trimIndent()
    }

    private fun generateGlassmorphicHeader(state: PortfolioState): String {
        return """
        <header class="header-glassmorphic">
            <div class="logo">
                <span class="logo-dot"></span>
                <span>${escapeHtml(state.authorName)}</span>
            </div>
            <nav>
                <ul>
                    ${if (state.showAboutSection) "<li><a href=\"#about\">About</a></li>" else ""}
                    ${if (state.showSkillsSection) "<li><a href=\"#skills\">Skills</a></li>" else ""}
                    ${if (state.showProjectsSection) "<li><a href=\"#projects\">Projects</a></li>" else ""}
                    ${if (state.showContactSection) "<li><a href=\"#contact\">Contact</a></li>" else ""}
                </ul>
            </nav>
        </header>
        """.trimIndent()
    }

    private fun generateDarkGradientHeader(state: PortfolioState): String {
        return """
        <header class="header-darkgradient">
            <div class="container header-minimalist">
                <div class="logo">
                    <span class="logo-dot"></span>
                    <span>${escapeHtml(state.authorName)}</span>
                </div>
                <nav>
                    <ul>
                        ${if (state.showAboutSection) "<li><a href=\"#about\">About</a></li>" else ""}
                        ${if (state.showSkillsSection) "<li><a href=\"#skills\">Skills</a></li>" else ""}
                        ${if (state.showProjectsSection) "<li><a href=\"#projects\">Projects</a></li>" else ""}
                        ${if (state.showContactSection) "<li><a href=\"#contact\">Contact</a></li>" else ""}
                    </ul>
                </nav>
            </div>
        </header>
        """.trimIndent()
    }

    private fun generateBigTypographyHero(state: PortfolioState): String {
        return """
        <section class="hero-section">
            ${if (state.statusBadge.isNotBlank()) "<div class=\"badge\"><span class=\"badge-dot\"></span> ${escapeHtml(state.statusBadge)}</div>" else ""}
            <h1 class="hero-title">${escapeHtml(state.heroTitle)}</h1>
            <p class="hero-subtitle">${escapeHtml(state.heroSubtitle)}</p>
            <div class="hero-actions">
                ${if (state.showProjectsSection) "<a href=\"#projects\" class=\"btn-primary\">Explore My Work ↓</a>" else ""}
                ${if (state.email.isNotBlank()) "<a href=\"mailto:${escapeHtml(state.email)}\" class=\"btn-secondary\">Get In Touch ✉</a>" else ""}
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateCenteredMinimalHero(state: PortfolioState): String {
        return """
        <section class="hero-section" style="text-align: center; display: flex; flex-direction: column; align-items: center;">
            ${if (state.statusBadge.isNotBlank()) "<div class=\"badge\"><span class=\"badge-dot\"></span> ${escapeHtml(state.statusBadge)}</div>" else ""}
            <h1 class="hero-title" style="max-width: 800px;">${escapeHtml(state.heroTitle)}</h1>
            <p class="hero-subtitle" style="margin-left: auto; margin-right: auto;">${escapeHtml(state.heroSubtitle)}</p>
            <div class="hero-actions" style="justify-content: center;">
                ${if (state.showProjectsSection) "<a href=\"#projects\" class=\"btn-primary\">View Featured Projects</a>" else ""}
                ${if (state.githubUrl.isNotBlank()) "<a href=\"${escapeHtml(state.githubUrl)}\" target=\"_blank\" class=\"btn-secondary\">GitHub Profile</a>" else ""}
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateSplitScreenHero(state: PortfolioState): String {
        return """
        <section class="hero-section" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 40px; align-items: center;">
            <div>
                ${if (state.statusBadge.isNotBlank()) "<div class=\"badge\"><span class=\"badge-dot\"></span> ${escapeHtml(state.statusBadge)}</div>" else ""}
                <h1 class="hero-title">${escapeHtml(state.heroTitle)}</h1>
                <p class="hero-subtitle">${escapeHtml(state.heroSubtitle)}</p>
                <div class="hero-actions">
                    ${if (state.showProjectsSection) "<a href=\"#projects\" class=\"btn-primary\">View Works</a>" else ""}
                    ${if (state.email.isNotBlank()) "<a href=\"mailto:${escapeHtml(state.email)}\" class=\"btn-secondary\">Email Me</a>" else ""}
                </div>
            </div>
            <div class="code-window" style="margin-top: 0;">
                <div class="code-header">
                    <span class="code-dot dot-red"></span>
                    <span class="code-dot dot-yellow"></span>
                    <span class="code-dot dot-green"></span>
                </div>
                <pre style="white-space: pre-wrap;"><code><span style="color:#F43F5E;">const</span> developer = {
  name: <span style="color:#10B981;">'${escapeHtml(state.authorName)}'</span>,
  title: <span style="color:#38BDF8;">'Software Architect'</span>,
  status: <span style="color:#F59E0B;">'Available for Hire'</span>,
  skills: [${state.skillsList.take(4).joinToString(", ") { "'$it'" }}]
};

<span style="color:#6366F1;">function</span> buildNextGenWeb() {
  <span style="color:#F43F5E;">return</span> <span style="color:#10B981;">'100% Responsive & Fast'</span>;
}</code></pre>
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateTechMinimalHero(state: PortfolioState): String {
        return """
        <section class="hero-section">
            <p style="font-family: 'Fira Code', monospace; color: var(--accent); margin-bottom: 12px;">$ // developer portfolio init</p>
            <h1 class="hero-title">${escapeHtml(state.heroTitle)}</h1>
            <p class="hero-subtitle">${escapeHtml(state.heroSubtitle)}</p>
            <div class="hero-actions">
                ${if (state.showProjectsSection) "<a href=\"#projects\" class=\"btn-primary\">Execute ./view-projects</a>" else ""}
                ${if (state.githubUrl.isNotBlank()) "<a href=\"${escapeHtml(state.githubUrl)}\" target=\"_blank\" class=\"btn-secondary\">GitHub</a>" else ""}
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateAboutSection(state: PortfolioState): String {
        return """
        <section id="about" class="section">
            <h2 class="section-title">About Me</h2>
            <p class="section-desc">Background, philosophy, and experience.</p>
            <div class="about-box">
                <p>${escapeHtml(state.aboutMeText)}</p>
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateSkillsSection(state: PortfolioState): String {
        val chips = state.skillsList.filter { it.isNotBlank() }.joinToString("") { skill ->
            "<div class=\"skill-chip\">⚡ ${escapeHtml(skill.trim())}</div>"
        }
        return """
        <section id="skills" class="section">
            <h2 class="section-title">Skills & Technologies</h2>
            <p class="section-desc">Core stack, framework tools, and architectural expertise.</p>
            <div class="skills-wrap">
                $chips
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateProjectsSection(state: PortfolioState): String {
        val cardsHtml = state.projectCards.joinToString("") { card -> generateProjectCardHtml(card) }
        return """
        <section id="projects" class="section">
            <h2 class="section-title">Featured Projects</h2>
            <p class="section-desc">Selected work, open-source repositories, and digital products.</p>
            <div class="grid-cards">
                $cardsHtml
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateProjectCardHtml(card: ProjectCard): String {
        val tagsHtml = card.tags.split(",").filter { it.isNotBlank() }.joinToString("") { tag ->
            "<span class=\"tag\">${escapeHtml(tag.trim())}</span>"
        }
        val repoBtn = if (card.repoUrl.isNotBlank()) "<a href=\"${escapeHtml(card.repoUrl)}\" target=\"_blank\">GitHub Repo →</a>" else ""
        val demoBtn = if (card.demoUrl.isNotBlank()) "<a href=\"${escapeHtml(card.demoUrl)}\" target=\"_blank\">Live Demo ↗</a>" else ""

        return """
        <div class="card">
            <div class="card-preview-bar preset-${card.gradientPreset}"></div>
            <div>
                <h3 class="card-title">${escapeHtml(card.title)}</h3>
                <p class="card-desc">${escapeHtml(card.description)}</p>
                <div class="card-tags">$tagsHtml</div>
            </div>
            <div class="card-links">
                $repoBtn
                $demoBtn
            </div>
        </div>
        """.trimIndent()
    }

    private fun generateExperienceSection(state: PortfolioState): String {
        val expCards = state.experienceList.joinToString("") { exp ->
            """
            <div class="card" style="margin-bottom: 16px;">
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">
                    <div>
                        <h3 class="card-title" style="margin: 0; font-size: 1.15rem;">${escapeHtml(exp.role)}</h3>
                        <p style="color: var(--primary); font-weight: 600; font-size: 0.95rem;">${escapeHtml(exp.company)}</p>
                    </div>
                    <span class="tag">${escapeHtml(exp.period)}</span>
                </div>
                <p class="card-desc" style="margin: 0;">${escapeHtml(exp.description)}</p>
            </div>
            """.trimIndent()
        }
        return """
        <section id="experience" class="section">
            <h2 class="section-title">Work Experience</h2>
            <p class="section-desc">Career timeline and engineering achievements.</p>
            <div style="display: flex; flex-direction: column; gap: 16px;">
                $expCards
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateTestimonialsSection(state: PortfolioState): String {
        val testCards = state.testimonialsList.joinToString("") { item ->
            """
            <div class="card">
                <p style="font-style: italic; color: var(--text); margin-bottom: 16px; line-height: 1.6;">“${escapeHtml(item.quote)}”</p>
                <div>
                    <h4 style="font-weight: 700; font-size: 1rem; color: var(--accent);">${escapeHtml(item.name)}</h4>
                    <p style="color: var(--text-muted); font-size: 0.85rem;">${escapeHtml(item.title)} ${if (item.company.isNotBlank()) "at ${escapeHtml(item.company)}" else ""}</p>
                </div>
            </div>
            """.trimIndent()
        }
        return """
        <section id="testimonials" class="section">
            <h2 class="section-title">Testimonials</h2>
            <p class="section-desc">Endorsements from engineering leaders and collaborators.</p>
            <div class="grid-cards">
                $testCards
            </div>
        </section>
        """.trimIndent()
    }

    private fun generateFooterSection(state: PortfolioState): String {
        return """
        <footer>
            <div class="container">
                <div class="social-links">
                    ${if (state.githubUrl.isNotBlank()) "<a href=\"${escapeHtml(state.githubUrl)}\" target=\"_blank\" class=\"social-link\" title=\"GitHub\">GH</a>" else ""}
                    ${if (state.linkedinUrl.isNotBlank()) "<a href=\"${escapeHtml(state.linkedinUrl)}\" target=\"_blank\" class=\"social-link\" title=\"LinkedIn\">IN</a>" else ""}
                    ${if (state.twitterUrl.isNotBlank()) "<a href=\"${escapeHtml(state.twitterUrl)}\" target=\"_blank\" class=\"social-link\" title=\"X / Twitter\">X</a>" else ""}
                    ${if (state.email.isNotBlank()) "<a href=\"mailto:${escapeHtml(state.email)}\" class=\"social-link\" title=\"Email\">✉</a>" else ""}
                </div>
                <p class="footer-text">© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} ${escapeHtml(state.authorName)}. Crafted with WebStudio Tablet.</p>
            </div>
        </footer>
        """.trimIndent()
    }

    private fun generateContactSection(state: PortfolioState): String {
        return """
        <section id="contact" class="section" style="text-align: center;">
            <h2 class="section-title">Let's Work Together</h2>
            <p class="section-desc" style="margin-left: auto; margin-right: auto;">Have an interesting project or role? Reach out directly via email or social links.</p>
            <div style="margin-top: 24px;">
                ${if (state.email.isNotBlank()) "<a href=\"mailto:${escapeHtml(state.email)}\" class=\"btn-primary\" style=\"font-size: 1.1rem; padding: 16px 36px;\">Send Email Message ✉</a>" else ""}
            </div>
        </section>
        """.trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
