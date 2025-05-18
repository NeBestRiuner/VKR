package org.example.project.Model.Enums

enum class RusDay(val dayNumber: Int, val russianName: String, val shortName: String) {
    MONDAY(1, "Понедельник","Пн"),
    TUESDAY(2, "Вторник","Вт"),
    WEDNESDAY(3, "Среда","Ср"),
    THURSDAY(4, "Четверг","Чт"),
    FRIDAY(5, "Пятница","Пт"),
    SATURDAY(6, "Суббота","Сб"),
    SUNDAY(7, "Воскресенье", "Вс");
    companion object {
        fun fromInt(dayNumber: Int): RusDay {
            return values().firstOrNull { it.dayNumber == dayNumber }
                ?: throw IllegalArgumentException("Некорректный номер дня: $dayNumber. Допустимые значения: 1-7")
        }

        fun getNameByNumber(dayNumber: Int): String {
            return fromInt(dayNumber).russianName
        }
    }
}