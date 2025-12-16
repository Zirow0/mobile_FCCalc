package com.fccalc.data.models

/**
 * Вхідні дані для розрахунку струмів КЗ підстанції
 */
data class SubstationInputData(
    val transformerPower: Double = 6300.0,       // S_ном.т, кВ·А
    val voltageHigh: Double = 115.0,             // U_в.н, кВ
    val voltageLow: Double = 11.0,               // U_н.н, кВ
    val voltageShortCircuit: Double = 11.1,      // U_к.max, %

    // Нормальний режим
    val resistanceNormal: Double = 0.15,         // R_с.н, Ом
    val reactanceNormal: Double = 0.6,           // X_с.н, Ом

    // Мінімальний режим
    val resistanceMin: Double = 0.15,            // R_с.min, Ом
    val reactanceMin: Double = 1.8               // X_с.min, Ом
)

/**
 * Результати розрахунку для одного режиму роботи
 */
data class SubstationModeResult(
    val resistance: Double,             // R_III, Ом
    val reactance: Double,              // X_III, Ом
    val impedance: Double,              // Z_III, Ом
    val current3Phase: Double,          // I³, А
    val current2Phase: Double           // I², А
)

/**
 * Повний результат розрахунку струмів КЗ підстанції
 */
data class SubstationResult(
    val inputData: SubstationInputData,
    val transformerResistance: Double,  // X_T, Ом
    val reductionCoefficient: Double,   // k_пр
    val normalMode: SubstationModeResult,
    val minMode: SubstationModeResult
)
