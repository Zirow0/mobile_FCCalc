package com.fccalc.ui.screens.cable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fccalc.R
import com.fccalc.data.models.CableSelectionData
import com.fccalc.ui.components.InfoBanner
import com.fccalc.ui.components.NumberInputField
import com.fccalc.ui.viewmodels.CableSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CableSelectionInputScreen(
    viewModel: CableSelectionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    // Константа для номінальної напруги (10 кВ для ТП 10 кВ)
    val nominalVoltage = 10.0

    var transformerPower by remember { mutableStateOf("1000.0") }
    var calculatedLoad by remember { mutableStateOf("1300.0") }
    var maxUsageTime by remember { mutableStateOf("4000.0") }
    var shortCircuitCurrent by remember { mutableStateOf("2.5") }
    var switchingTime by remember { mutableStateOf("2.5") }

    val isValid by remember {
        derivedStateOf {
            listOf(
                transformerPower,
                calculatedLoad,
                maxUsageTime,
                shortCircuitCurrent,
                switchingTime
            ).all { it.toDoubleOrNull() != null && it.toDouble() > 0 }
        }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Info banner
            InfoBanner(text = stringResource(R.string.cable_input_info))

            Spacer(modifier = Modifier.height(8.dp))

            // Input fields
            NumberInputField(
                value = transformerPower,
                onValueChange = { transformerPower = it },
                label = stringResource(R.string.transformer_power)
            )

            NumberInputField(
                value = calculatedLoad,
                onValueChange = { calculatedLoad = it },
                label = stringResource(R.string.calculated_load)
            )

            NumberInputField(
                value = maxUsageTime,
                onValueChange = { maxUsageTime = it },
                label = stringResource(R.string.max_usage_time)
            )

            NumberInputField(
                value = shortCircuitCurrent,
                onValueChange = { shortCircuitCurrent = it },
                label = stringResource(R.string.short_circuit_current)
            )

            NumberInputField(
                value = switchingTime,
                onValueChange = { switchingTime = it },
                label = stringResource(R.string.switching_time)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clear button
                OutlinedButton(
                    onClick = {
                        transformerPower = ""
                        calculatedLoad = ""
                        maxUsageTime = ""
                        shortCircuitCurrent = ""
                        switchingTime = ""
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.clear),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Calculate button
                Button(
                    onClick = {
                        val data = CableSelectionData(
                            nominalVoltage = nominalVoltage,
                            transformerPower = transformerPower.toDouble(),
                            calculatedLoad = calculatedLoad.toDouble(),
                            maxUsageTime = maxUsageTime.toDouble(),
                            shortCircuitCurrent = shortCircuitCurrent.toDouble(),
                            switchingTime = switchingTime.toDouble()
                        )
                        viewModel.calculateCableSelection(data)
                        onNavigateToResult()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2),
                        disabledContainerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.calculate_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
