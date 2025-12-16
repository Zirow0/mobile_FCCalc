package com.fccalc.ui.screens.scgpp

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
import com.fccalc.ui.viewmodels.SCGPPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SCGPPResultScreen(
    viewModel: SCGPPViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val result by viewModel.calculationResult.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.sc_gpp_input_title),
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
                        label = stringResource(R.string.short_circuit_power),
                        value = "${calculationResult.inputData.shortCircuitPower} МВ·А"
                    )
                    ResultText(
                        label = stringResource(R.string.nominal_voltage),
                        value = "${calculationResult.inputData.nominalVoltage} кВ"
                    )
                    ResultText(
                        label = stringResource(R.string.transformer_voltage_percent),
                        value = "${calculationResult.inputData.transformerVoltage} %"
                    )
                    ResultText(
                        label = stringResource(R.string.transformer_power_mva),
                        value = "${calculationResult.inputData.transformerPower} МВ·А"
                    )
                }

                // Intermediate Calculations Card
                InfoCard(title = stringResource(R.string.intermediate_calc_title)) {
                    ResultText(
                        label = stringResource(R.string.system_resistance),
                        value = "X_c = ${calculationResult.intermediate.systemResistance} Ом"
                    )
                    ResultText(
                        label = stringResource(R.string.transformer_resistance),
                        value = "X_T = ${calculationResult.intermediate.transformerResistance} Ом"
                    )
                    ResultText(
                        label = stringResource(R.string.total_resistance),
                        value = "X_Σ = ${calculationResult.intermediate.totalResistance} Ом"
                    )
                }

                // Result Card
                InfoCard(title = stringResource(R.string.result_title)) {
                    Text(
                        text = stringResource(R.string.short_circuit_current_result),
                        fontSize = 14.sp,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "I_п0 = ${calculationResult.shortCircuitCurrent} кА",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
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
