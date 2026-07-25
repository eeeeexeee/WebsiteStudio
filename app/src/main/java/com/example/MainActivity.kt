package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MainTabletScreen
import com.example.ui.theme.WebStudioTheme
import com.example.ui.viewmodel.PortfolioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebStudioTheme {
                val portfolioViewModel: PortfolioViewModel = viewModel()
                MainTabletScreen(viewModel = portfolioViewModel)
            }
        }
    }
}
