package com.fccalc.data.models

/**
 * Вхідні дані для розрахунку струмів КЗ на шинах ГПП
 */
data class SCGPPInputData(
    val shortCircuitPower: Double = 200.0,      // S_k, МВ·А
    val nominalVoltage: Double = 10.5,          // U_с.н, кВ
    val transformerVoltage: Double = 10.5,      // U_k%, %
    val transformerPower: Double = 6.3          // S_ном.т, МВ·А
)

/**
 * Проміжні розрахункові значення опорів
 */
data class SCGPPCalculationIntermediate(
    val systemResistance: Double,       // X_c, Ом
    val transformerResistance: Double,  // X_T, Ом
    val totalResistance: Double         // X_Σ, Ом
)

/**
 * Результат розрахунку струмів КЗ на шинах ГПП
 */
data class SCGPPResult(
    val inputData: SCGPPInputData,
    val intermediate: SCGPPCalculationIntermediate,
    val shortCircuitCurrent: Double     // I_п0, кА
)
