package com.fccalc.ui.screens.substation

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
import com.fccalc.data.models.SubstationInputData
import com.fccalc.ui.components.InfoBanner
import com.fccalc.ui.components.NumberInputField
import com.fccalc.ui.viewmodels.SubstationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstationInputScreen(
    viewModel: SubstationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    var transformerPower by remember { mutableStateOf("6300") }
    var voltageHigh by remember { mutableStateOf("115") }
    var voltageLow by remember { mutableStateOf("11") }
    var voltageShortCircuit by remember { mutableStateOf("11.1") }

    var resistanceNormal by remember { mutableStateOf("0.15") }
    var reactanceNormal by remember { mutableStateOf("0.6") }

    var resistanceMin by remember { mutableStateOf("0.15") }
    var reactanceMin by remember { mutableStateOf("1.8") }

    val inputData by viewModel.inputData.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Info banner
            InfoBanner(text = stringResource(R.string.substation_info))

            Spacer(modifier = Modifier.height(16.dp))

            // Transformer Parameters
            Text(
                text = stringResource(R.string.transformer_parameters),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            NumberInputField(
                value = transformerPower,
                onValueChange = { transformerPower = it },
                label = stringResource(R.string.transformer_power_kva)
            )

            NumberInputField(
                value = voltageHigh,
                onValueChange = { voltageHigh = it },
                label = stringResource(R.string.voltage_high)
            )

            NumberInputField(
                value = voltageLow,
                onValueChange = { voltageLow = it },
                label = stringResource(R.string.voltage_low)
            )

            NumberInputField(
                value = voltageShortCircuit,
                onValueChange = { voltageShortCircuit = it },
                label = stringResource(R.string.voltage_short_circuit)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Normal Mode
            Text(
                text = stringResource(R.string.normal_mode),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            NumberInputField(
                value = resistanceNormal,
                onValueChange = { resistanceNormal = it },
                label = stringResource(R.string.resistance_normal)
            )

            NumberInputField(
                value = reactanceNormal,
                onValueChange = { reactanceNormal = it },
                label = stringResource(R.string.reactance_normal)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Minimum Mode
            Text(
                text = stringResource(R.string.min_mode),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            NumberInputField(
                value = resistanceMin,
                onValueChange = { resistanceMin = it },
                label = stringResource(R.string.resistance_min)
            )

            NumberInputField(
                value = reactanceMin,
                onValueChange = { reactanceMin = it },
                label = stringResource(R.string.reactance_min)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Validation
            val isValid = transformerPower.toDoubleOrNull()?.let { it > 0 } == true &&
                    voltageHigh.toDoubleOrNull()?.let { it > 0 } == true &&
                    voltageLow.toDoubleOrNull()?.let { it > 0 } == true &&
                    voltageShortCircuit.toDoubleOrNull()?.let { it > 0 } == true &&
                    resistanceNormal.toDoubleOrNull()?.let { it > 0 } == true &&
                    reactanceNormal.toDoubleOrNull()?.let { it > 0 } == true &&
                    resistanceMin.toDoubleOrNull()?.let { it > 0 } == true &&
                    reactanceMin.toDoubleOrNull()?.let { it > 0 } == true

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clear button
                OutlinedButton(
                    onClick = {
                        transformerPower = ""
                        voltageHigh = ""
                        voltageLow = ""
                        voltageShortCircuit = ""
                        resistanceNormal = ""
                        reactanceNormal = ""
                        resistanceMin = ""
                        reactanceMin = ""
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
                        val data = SubstationInputData(
                            transformerPower = transformerPower.toDouble(),
                            voltageHigh = voltageHigh.toDouble(),
                            voltageLow = voltageLow.toDouble(),
                            voltageShortCircuit = voltageShortCircuit.toDouble(),
                            resistanceNormal = resistanceNormal.toDouble(),
                            reactanceNormal = reactanceNormal.toDouble(),
                            resistanceMin = resistanceMin.toDouble(),
                            reactanceMin = reactanceMin.toDouble()
                        )
                        viewModel.calculateSubstationShortCircuit(data)
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
