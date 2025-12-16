package com.fccalc.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fccalc.ui.screens.MainScreen
import com.fccalc.ui.screens.cable.CableSelectionInputScreen
import com.fccalc.ui.screens.cable.CableSelectionResultScreen
import com.fccalc.ui.screens.scgpp.SCGPPInputScreen
import com.fccalc.ui.screens.scgpp.SCGPPResultScreen
import com.fccalc.ui.screens.substation.SubstationInputScreen
import com.fccalc.ui.screens.substation.SubstationResultScreen
import com.fccalc.ui.viewmodels.CableSelectionViewModel
import com.fccalc.ui.viewmodels.SCGPPViewModel
import com.fccalc.ui.viewmodels.SubstationViewModel

/**
 * Маршрути навігації додатку
 */
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object CableInput : Screen("cable_input")
    object CableResult : Screen("cable_result")
    // Місця для інших екранів
    object SCGPPInput : Screen("sc_gpp_input")
    object SCGPPResult : Screen("sc_gpp_result")
    object SubstationInput : Screen("substation_input")
    object SubstationResult : Screen("substation_result")
}

/**
 * Граф навігації додатку
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Main.route
) {
    // ViewModels для спільного використання між екранами
    val cableViewModel: CableSelectionViewModel = viewModel()
    val scgppViewModel: SCGPPViewModel = viewModel()
    val substationViewModel: SubstationViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Головний екран
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToCableSelection = {
                    cableViewModel.clearResults()
                    navController.navigate(Screen.CableInput.route)
                },
                onNavigateToSCGPP = {
                    scgppViewModel.clearResults()
                    navController.navigate(Screen.SCGPPInput.route)
                },
                onNavigateToSubstation = {
                    substationViewModel.clearResults()
                    navController.navigate(Screen.SubstationInput.route)
                }
            )
        }

        // Екран введення даних для вибору кабелів
        composable(Screen.CableInput.route) {
            CableSelectionInputScreen(
                viewModel = cableViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { navController.navigate(Screen.CableResult.route) }
            )
        }

        // Екран результатів для вибору кабелів
        composable(Screen.CableResult.route) {
            CableSelectionResultScreen(
                viewModel = cableViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        // Екран введення даних для струмів КЗ на шинах ГПП
        composable(Screen.SCGPPInput.route) {
            SCGPPInputScreen(
                viewModel = scgppViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { navController.navigate(Screen.SCGPPResult.route) }
            )
        }

        // Екран результатів для струмів КЗ на шинах ГПП
        composable(Screen.SCGPPResult.route) {
            SCGPPResultScreen(
                viewModel = scgppViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        // Екран введення даних для струмів КЗ підстанції
        composable(Screen.SubstationInput.route) {
            SubstationInputScreen(
                viewModel = substationViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { navController.navigate(Screen.SubstationResult.route) }
            )
        }

        // Екран результатів для струмів КЗ підстанції
        composable(Screen.SubstationResult.route) {
            SubstationResultScreen(
                viewModel = substationViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }
    }
}
