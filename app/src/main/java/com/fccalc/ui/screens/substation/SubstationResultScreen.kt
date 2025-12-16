package com.fccalc.ui.screens.substation

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
import com.fccalc.ui.components.ResultText
import com.fccalc.ui.viewmodels.SubstationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstationResultScreen(
    viewModel: SubstationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val result by viewModel.calculationResult.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.substation_input_title),
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.input_data_title),
                            fontSize = 16.sp,
                            color = Color(0xFF1976D2),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ResultText(
                            label = stringResource(R.string.transformer_power_kva),
                            value = "${calculationResult.inputData.transformerPower.toInt()} кВ·А"
                        )
                        ResultText(
                            label = stringResource(R.string.voltage_high),
                            value = "${calculationResult.inputData.voltageHigh} кВ"
                        )
                        ResultText(
                            label = stringResource(R.string.voltage_low),
                            value = "${calculationResult.inputData.voltageLow} кВ"
                        )
                        ResultText(
                            label = stringResource(R.string.voltage_short_circuit),
                            value = "${calculationResult.inputData.voltageShortCircuit} %"
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.normal_mode),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        ResultText(
                            label = stringResource(R.string.resistance_normal),
                            value = "${calculationResult.inputData.resistanceNormal} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.reactance_normal),
                            value = "${calculationResult.inputData.reactanceNormal} Ом"
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.min_mode),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107)
                        )
                        ResultText(
                            label = stringResource(R.string.resistance_min),
                            value = "${calculationResult.inputData.resistanceMin} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.reactance_min),
                            value = "${calculationResult.inputData.reactanceMin} Ом"
                        )
                    }
                }

                // Common Parameters Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.common_parameters),
                            fontSize = 16.sp,
                            color = Color(0xFF1976D2),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ResultText(
                            label = stringResource(R.string.transformer_resistance_result),
                            value = "X_T = ${calculationResult.transformerResistance} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.reduction_coefficient),
                            value = "k_пр = ${calculationResult.reductionCoefficient}"
                        )
                    }
                }

                // Normal Mode Result Card (Green accent)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.normal_mode_results),
                            fontSize = 16.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ResultText(
                            label = stringResource(R.string.resistance_result),
                            value = "R_III = ${calculationResult.normalMode.resistance} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.reactance_result),
                            value = "X_III = ${calculationResult.normalMode.reactance} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.impedance_result),
                            value = "Z_III = ${calculationResult.normalMode.impedance} Ом"
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFF4CAF50)
                        )

                        Text(
                            text = "I³ = ${calculationResult.normalMode.current3Phase.toInt()} А",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "I² = ${calculationResult.normalMode.current2Phase.toInt()} А",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Minimum Mode Result Card (Yellow accent)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.min_mode_results),
                            fontSize = 16.sp,
                            color = Color(0xFFFFA000),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ResultText(
                            label = stringResource(R.string.resistance_result),
                            value = "R_III = ${calculationResult.minMode.resistance} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.reactance_result),
                            value = "X_III = ${calculationResult.minMode.reactance} Ом"
                        )
                        ResultText(
                            label = stringResource(R.string.impedance_result),
                            value = "Z_III = ${calculationResult.minMode.impedance} Ом"
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFFFA000)
                        )

                        Text(
                            text = "I³ = ${calculationResult.minMode.current3Phase.toInt()} А",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFA000),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "I² = ${calculationResult.minMode.current2Phase.toInt()} А",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFA000),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
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
