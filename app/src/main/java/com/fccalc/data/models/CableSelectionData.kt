package com.fccalc.data.models

/**
 * Вхідні дані для розрахунку вибору кабелів
 */
data class CableSelectionData(
    val nominalVoltage: Double = 10.0,          // Номінальна напруга, кВ
    val transformerPower: Double = 1000.0,      // Потужність ТП, кВ·А
    val calculatedLoad: Double = 1300.0,        // Розрахункове навантаження, кВ·А
    val maxUsageTime: Double = 4000.0,          // Час використання максимуму, год
    val shortCircuitCurrent: Double = 2.5,      // Струм КЗ, кА
    val switchingTime: Double = 2.5             // Фіктивний час вимикання, с
)

/**
 * Проміжні розрахункові дані
 */
data class CableCalculationIntermediate(
    val calculatedCurrent: Double,              // Розрахунковий струм I_M, А
    val economicCurrentDensity: Double,         // Економічна густина струму j_ек, А/мм²
    val economicCrossSection: Double,           // Економічний переріз s_ек, мм²
    val thermalCoefficient: Double,             // Термічний коефіцієнт C_T, А·с^0.5/мм²
    val minCrossSection: Double                 // Мінімальний переріз s_min, мм²
)

/**
 * Результат розрахунку вибору кабелів
 */
data class CableSelectionResult(
    val inputData: CableSelectionData,
    val intermediate: CableCalculationIntermediate,
    val recommendedCable: String,               // Рекомендований кабель
    val explanation: String                     // Пояснення вибору
)
