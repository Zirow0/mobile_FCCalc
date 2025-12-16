package com.fccalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.fccalc.data.models.SCGPPCalculationIntermediate
import com.fccalc.data.models.SCGPPInputData
import com.fccalc.data.models.SCGPPResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * ViewModel для розрахунку струмів КЗ на шинах ГПП
 */
class SCGPPViewModel : ViewModel() {

    private val _inputData = MutableStateFlow(SCGPPInputData())
    val inputData: StateFlow<SCGPPInputData> = _inputData.asStateFlow()

    private val _calculationResult = MutableStateFlow<SCGPPResult?>(null)
    val calculationResult: StateFlow<SCGPPResult?> = _calculationResult.asStateFlow()

    /**
     * Оновлення вхідних даних
     */
    fun updateInputData(data: SCGPPInputData) {
        _inputData.value = data
    }

    /**
     * Перевірка валідності введених даних
     */
    fun isInputValid(data: SCGPPInputData): Boolean {
        return data.shortCircuitPower > 0 &&
                data.nominalVoltage > 0 &&
                data.transformerVoltage > 0 &&
                data.transformerPower > 0
    }

    /**
     * Виконання розрахунку струмів КЗ на шинах ГПП
     *
     * Формули:
     * 1. X_c = U²_с.н / S_k - опір системи
     * 2. X_T = (U_k% / 100) × (U²_с.н / S_ном.т) - опір трансформатора
     * 3. X_Σ = X_c + X_T - сумарний опір
     * 4. I_п0 = U_с.н / (√3 × X_Σ) - струм трифазного КЗ
     */
    fun calculateShortCircuit(data: SCGPPInputData) {
        try {
            // 1. Опір системи (Ом)
            val systemResistance = (data.nominalVoltage * data.nominalVoltage) / data.shortCircuitPower

            // 2. Опір трансформатора (Ом)
            val transformerResistance = (data.transformerVoltage / 100.0) *
                    ((data.nominalVoltage * data.nominalVoltage) / data.transformerPower)

            // 3. Сумарний опір (Ом)
            val totalResistance = systemResistance + transformerResistance

            // 4. Струм трифазного КЗ (кА)
            val shortCircuitCurrent = data.nominalVoltage / (sqrt(3.0) * totalResistance)

            // Округлення значень
            val intermediate = SCGPPCalculationIntermediate(
                systemResistance = (systemResistance * 100).roundToInt() / 100.0,
                transformerResistance = (transformerResistance * 100).roundToInt() / 100.0,
                totalResistance = (totalResistance * 100).roundToInt() / 100.0
            )

            val result = SCGPPResult(
                inputData = data,
                intermediate = intermediate,
                shortCircuitCurrent = (shortCircuitCurrent * 10).roundToInt() / 10.0
            )

            _calculationResult.value = result

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Очищення результатів
     */
    fun clearResults() {
        _calculationResult.value = null
    }
}
