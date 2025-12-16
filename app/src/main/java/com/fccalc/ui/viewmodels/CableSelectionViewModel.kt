package com.fccalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.fccalc.data.models.CableCalculationIntermediate
import com.fccalc.data.models.CableSelectionData
import com.fccalc.data.models.CableSelectionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * ViewModel для розрахунку вибору кабелів
 */
class CableSelectionViewModel : ViewModel() {

    private val _inputData = MutableStateFlow(CableSelectionData())
    val inputData: StateFlow<CableSelectionData> = _inputData.asStateFlow()

    private val _calculationResult = MutableStateFlow<CableSelectionResult?>(null)
    val calculationResult: StateFlow<CableSelectionResult?> = _calculationResult.asStateFlow()

    /**
     * Оновлення вхідних даних
     */
    fun updateInputData(data: CableSelectionData) {
        _inputData.value = data
    }

    /**
     * Перевірка валідності введених даних
     */
    fun isInputValid(data: CableSelectionData): Boolean {
        return data.nominalVoltage > 0 &&
                data.transformerPower > 0 &&
                data.calculatedLoad > 0 &&
                data.maxUsageTime > 0 &&
                data.shortCircuitCurrent > 0 &&
                data.switchingTime > 0
    }

    /**
     * Виконання розрахунку вибору кабелів
     */
    fun calculateCableSelection(data: CableSelectionData) {
        try {
            // 1. Розрахунковий струм для нормального режиму
            val calculatedCurrent = data.calculatedLoad / (sqrt(3.0) * data.nominalVoltage)

            // Струм на один кабель (для двох кабелів)
            val currentPerCable = calculatedCurrent / 2

            // 2. Економічна густина струму (для T_M = 4000 год)
            val economicCurrentDensity = getEconomicCurrentDensity(data.maxUsageTime)

            // 3. Економічний переріз
            val economicCrossSection = currentPerCable / economicCurrentDensity

            // 4. Термічний коефіцієнт для паперових кабелів
            val thermalCoefficient = 92.0 // А·с^0.5/мм²

            // 5. Мінімальний переріз за термічною стійкістю
            val shortCircuitCurrentA = data.shortCircuitCurrent * 1000 // перетворюємо кА в А
            val minCrossSection = (shortCircuitCurrentA * sqrt(data.switchingTime)) / thermalCoefficient

            // 6. Вибір кабелю
            val selectedCrossSection = maxOf(economicCrossSection, minCrossSection)
            val standardCrossSection = selectStandardCrossSection(selectedCrossSection)
            val recommendedCable = "ААБ 10 3×${standardCrossSection.toInt()} мм²"

            // 7. Пояснення вибору
            val explanation = if (minCrossSection > economicCrossSection) {
                "Переріз вибрано за термічною стійкістю до струмів КЗ"
            } else {
                "Переріз вибрано за економічною густиною струму"
            }

            // Округлення значень до 1 знаку після коми
            val intermediate = CableCalculationIntermediate(
                calculatedCurrent = (currentPerCable * 10).roundToInt() / 10.0,
                economicCurrentDensity = economicCurrentDensity,
                economicCrossSection = (economicCrossSection * 10).roundToInt() / 10.0,
                thermalCoefficient = thermalCoefficient,
                minCrossSection = (minCrossSection * 10).roundToInt() / 10.0
            )

            val result = CableSelectionResult(
                inputData = data,
                intermediate = intermediate,
                recommendedCable = recommendedCable,
                explanation = explanation
            )

            _calculationResult.value = result

        } catch (e: Exception) {
            // Обробка помилок
            e.printStackTrace()
        }
    }

    /**
     * Отримання економічної густини струму залежно від часу використання максимуму
     */
    private fun getEconomicCurrentDensity(maxUsageTime: Double): Double {
        return when {
            maxUsageTime <= 3000 -> 1.6
            maxUsageTime <= 5000 -> 1.4
            else -> 1.2
        }
    }

    /**
     * Вибір стандартного перерізу кабелю
     */
    private fun selectStandardCrossSection(calculatedCrossSection: Double): Double {
        val standardCrossSections = listOf(16.0, 25.0, 35.0, 50.0, 70.0, 95.0, 120.0, 150.0, 185.0, 240.0)
        return standardCrossSections.firstOrNull { it >= calculatedCrossSection } ?: standardCrossSections.last()
    }

    /**
     * Очищення результатів
     */
    fun clearResults() {
        _calculationResult.value = null
    }
}
