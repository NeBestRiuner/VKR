package org.example.project.Model.Enums

enum class RusMonth(val monthNumber: Int, val russianName: String) {
    JANUARY(1, "Январь"),
    FEBRUARY(2, "Февраль"),
    MARCH(3, "Март"),
    APRIL(4, "Апрель"),
    MAY(5, "Май"),
    JUNE(6, "Июнь"),
    JULY(7, "Июль"),
    AUGUST(8, "Август"),
    SEPTEMBER(9, "Сентябрь"),
    OCTOBER(10, "Октябрь"),
    NOVEMBER(11, "Ноябрь"),
    DECEMBER(12, "Декабрь");
    companion object {
        fun fromInt(monthNumber: Int): RusMonth {
            return values().firstOrNull { it.monthNumber == monthNumber }
                ?: throw IllegalArgumentException("Некорректный номер месяца: $monthNumber. Допустимые значения: 0-11")
        }

        fun getNameByNumber(monthNumber: Int): String {
            return fromInt(monthNumber).russianName
        }
    }
}