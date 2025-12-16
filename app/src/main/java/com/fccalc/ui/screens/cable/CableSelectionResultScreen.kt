package com.fccalc.ui.screens.cable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fccalc.R
import com.fccalc.ui.components.InfoCard
import com.fccalc.ui.components.ResultText
import com.fccalc.ui.viewmodels.CableSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CableSelectionResultScreen(
    viewModel: CableSelectionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val result by viewModel.calculationResult.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cable_input_title),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Балансуючий елемент для центрування тексту
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        result?.let { calculationResult ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Input Data Card
                InfoCard(title = stringResource(R.string.input_data_title)) {
                    ResultText(
                        label = stringResource(R.string.nominal_voltage),
                        value = "${calculationResult.inputData.nominalVoltage} кВ"
                    )
                    ResultText(
                        label = stringResource(R.string.transformer_power),
                        value = "2×${calculationResult.inputData.transformerPower.toInt()} кВ·А"
                    )
                    ResultText(
                        label = stringResource(R.string.calculated_load),
                        value = "${calculationResult.inputData.calculatedLoad.toInt()} кВ·А"
                    )
                    ResultText(
                        label = stringResource(R.string.max_usage_time),
                        value = "${calculationResult.inputData.maxUsageTime.toInt()} год"
                    )
                    ResultText(
                        label = stringResource(R.string.short_circuit_current),
                        value = "${calculationResult.inputData.shortCircuitCurrent} кА"
                    )
                    ResultText(
                        label = stringResource(R.string.switching_time),
                        value = "${calculationResult.inputData.switchingTime} с"
                    )
                }

                // Intermediate Calculations Card
                InfoCard(title = stringResource(R.string.intermediate_calc_title)) {
                    ResultText(
                        label = stringResource(R.string.calculated_current),
                        value = "I_M = ${calculationResult.intermediate.calculatedCurrent} А"
                    )
                    ResultText(
                        label = stringResource(R.string.economic_density),
                        value = "j_ек = ${calculationResult.intermediate.economicCurrentDensity} А/мм²"
                    )
                    ResultText(
                        label = stringResource(R.string.economic_cross_section),
                        value = "s_ек = ${calculationResult.intermediate.economicCrossSection} мм²"
                    )
                    ResultText(
                        label = stringResource(R.string.thermal_coefficient),
                        value = "C_T = ${calculationResult.intermediate.thermalCoefficient} А·с^0.5/мм²"
                    )
                    ResultText(
                        label = stringResource(R.string.min_cross_section),
                        value = "s_min = ${calculationResult.intermediate.minCrossSection} мм²"
                    )
                }

                // Result Card
                InfoCard(title = stringResource(R.string.result_title)) {
                    Text(
                        text = stringResource(R.string.recommended_cable),
                        fontSize = 14.sp,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = calculationResult.recommendedCable,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = calculationResult.explanation,
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Back to Main Button
                Button(
                    onClick = onNavigateToMain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.back_to_main),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } ?: run {
            // Loading or error state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1976D2))
            }
        }
    }
}
