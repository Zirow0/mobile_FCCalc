package com.fccalc.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.fccalc.data.models.SubstationInputData
import com.fccalc.data.models.SubstationModeResult
import com.fccalc.data.models.SubstationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * ViewModel для розрахунку струмів КЗ підстанції
 */
class SubstationViewModel : ViewModel() {

    private val _inputData = MutableStateFlow(SubstationInputData())
    val inputData: StateFlow<SubstationInputData> = _inputData.asStateFlow()

    private val _calculationResult = MutableStateFlow<SubstationResult?>(null)
    val calculationResult: StateFlow<SubstationResult?> = _calculationResult.asStateFlow()

    /**
     * Оновлення вхідних даних
     */
    fun updateInputData(data: SubstationInputData) {
        _inputData.value = data
    }

    /**
     * Перевірка валідності введених даних
     */
    fun isInputValid(data: SubstationInputData): Boolean {
        return data.transformerPower > 0 &&
                data.voltageHigh > 0 &&
                data.voltageLow > 0 &&
                data.voltageShortCircuit > 0 &&
                data.resistanceNormal > 0 &&
                data.reactanceNormal > 0 &&
                data.resistanceMin > 0 &&
                data.reactanceMin > 0
    }

    /**
     * Виконання розрахунку струмів КЗ підстанції для двох режимів
     *
     * Формули:
     * 1. X_T = (U_к.max × U²_в.н) / (100 × S_ном.т) - опір трансформатора
     * 2. k_пр = U²_н.н / U²_в.н - коефіцієнт приведення
     * 3. Для кожного режиму:
     *    R_III = R_с × k_пр
     *    X_III = (X_с + X_T) × k_пр
     *    Z_III = √(R²_III + X²_III)
     * 4. Струми КЗ:
     *    I³ = U_н.н / (√3 × Z_III)
     *    I² = I³ × √3 / 2
     */
    fun calculateSubstationShortCircuit(data: SubstationInputData) {
        try {
            // 1. Опір трансформатора (Ом)
            // Переводимо S_ном.т з кВ·А в МВ·А: S_ном.т / 1000
            val transformerPowerMVA = data.transformerPower / 1000.0
            val transformerResistance = (data.voltageShortCircuit * data.voltageHigh * data.voltageHigh) /
                    (100.0 * transformerPowerMVA)

            // 2. Коефіцієнт приведення (безрозмірний)
            val reductionCoefficient = (data.voltageLow * data.voltageLow) /
                    (data.voltageHigh * data.voltageHigh)

            // 3. Розрахунок для нормального режиму
            val normalMode = calculateModeResult(
                resistance = data.resistanceNormal,
                reactance = data.reactanceNormal,
                transformerResistance = transformerResistance,
                reductionCoefficient = reductionCoefficient,
                voltageLow = data.voltageLow
            )

            // 4. Розрахунок для мінімального режиму
            val minMode = calculateModeResult(
                resistance = data.resistanceMin,
                reactance = data.reactanceMin,
                transformerResistance = transformerResistance,
                reductionCoefficient = reductionCoefficient,
                voltageLow = data.voltageLow
            )

            val result = SubstationResult(
                inputData = data,
                transformerResistance = (transformerResistance * 100).roundToInt() / 100.0,
                reductionCoefficient = (reductionCoefficient * 10000).roundToInt() / 10000.0,
                normalMode = normalMode,
                minMode = minMode
            )

            _calculationResult.value = result

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Розрахунок параметрів для одного режиму роботи
     */
    private fun calculateModeResult(
        resistance: Double,
        reactance: Double,
        transformerResistance: Double,
        reductionCoefficient: Double,
        voltageLow: Double
    ): SubstationModeResult {
        // R_III = R_с × k_пр (Ом)
        val resistanceIII = resistance * reductionCoefficient

        // X_III = (X_с + X_T) × k_пр (Ом)
        val reactanceIII = (reactance + transformerResistance) * reductionCoefficient

        // Z_III = √(R²_III + X²_III) (Ом)
        val impedanceIII = sqrt(resistanceIII * resistanceIII + reactanceIII * reactanceIII)

        // I³ = U_н.н / (√3 × Z_III) (кА → А)
        val current3Phase = (voltageLow * 1000.0) / (sqrt(3.0) * impedanceIII)

        // I² = I³ × √3 / 2 (А)
        val current2Phase = current3Phase * sqrt(3.0) / 2.0

        return SubstationModeResult(
            resistance = (resistanceIII * 1000).roundToInt() / 1000.0,
            reactance = (reactanceIII * 1000).roundToInt() / 1000.0,
            impedance = (impedanceIII * 1000).roundToInt() / 1000.0,
            current3Phase = current3Phase.roundToInt().toDouble(),
            current2Phase = current2Phase.roundToInt().toDouble()
        )
    }

    /**
     * Очищення результатів
     */
    fun clearResults() {
        _calculationResult.value = null
    }
}
