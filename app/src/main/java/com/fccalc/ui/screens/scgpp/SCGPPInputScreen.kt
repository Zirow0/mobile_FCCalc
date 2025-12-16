package com.fccalc.ui.screens.scgpp

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
import com.fccalc.data.models.SCGPPInputData
import com.fccalc.ui.components.InfoBanner
import com.fccalc.ui.components.NumberInputField
import com.fccalc.ui.viewmodels.SCGPPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SCGPPInputScreen(
    viewModel: SCGPPViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    var shortCircuitPower by remember { mutableStateOf("200") }
    var nominalVoltage by remember { mutableStateOf("10.5") }
    var transformerVoltage by remember { mutableStateOf("10.5") }
    var transformerPower by remember { mutableStateOf("6.3") }

    val inputData by viewModel.inputData.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Info banner
            InfoBanner(text = stringResource(R.string.sc_gpp_info))

            Spacer(modifier = Modifier.height(16.dp))

            // Input parameters title
            Text(
                text = stringResource(R.string.input_parameters),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            NumberInputField(
                value = shortCircuitPower,
                onValueChange = { shortCircuitPower = it },
                label = stringResource(R.string.short_circuit_power)
            )

            NumberInputField(
                value = nominalVoltage,
                onValueChange = { nominalVoltage = it },
                label = stringResource(R.string.nominal_voltage)
            )

            NumberInputField(
                value = transformerVoltage,
                onValueChange = { transformerVoltage = it },
                label = stringResource(R.string.transformer_voltage_percent)
            )

            NumberInputField(
                value = transformerPower,
                onValueChange = { transformerPower = it },
                label = stringResource(R.string.transformer_power_mva)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Validation
            val isValid = shortCircuitPower.toDoubleOrNull()?.let { it > 0 } == true &&
                    nominalVoltage.toDoubleOrNull()?.let { it > 0 } == true &&
                    transformerVoltage.toDoubleOrNull()?.let { it > 0 } == true &&
                    transformerPower.toDoubleOrNull()?.let { it > 0 } == true

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clear button
                OutlinedButton(
                    onClick = {
                        shortCircuitPower = ""
                        nominalVoltage = ""
                        transformerVoltage = ""
                        transformerPower = ""
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
                        val data = SCGPPInputData(
                            shortCircuitPower = shortCircuitPower.toDouble(),
                            nominalVoltage = nominalVoltage.toDouble(),
                            transformerVoltage = transformerVoltage.toDouble(),
                            transformerPower = transformerPower.toDouble()
                        )
                        viewModel.calculateShortCircuit(data)
                        onNavigateToResult()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.calculate),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
