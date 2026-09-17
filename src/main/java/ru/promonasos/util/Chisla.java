package ru.promonasos.util;

// в Java Double.toString(20) даёт "20.0", а не "20" как в C# — tekst() убирает лишнее
public final class Chisla {

    private Chisla() {
    }

    public static String tekst(double znachenie) {
        if (znachenie == Math.rint(znachenie) && !Double.isInfinite(znachenie)) {
            return String.valueOf((long) znachenie);
        }
        // русская запись дробей — через запятую: 12,5 а не 12.5
        return String.valueOf(znachenie).replace('.', ',');
    }

    // склонение после числа: sklonenie(1, "типоразмер", "типоразмера", "типоразмеров") → "типоразмер"
    public static String sklonenie(int kolichestvo, String odin, String dva, String pyat) {
        int sotni = kolichestvo % 100;
        if (sotni >= 11 && sotni <= 14) {
            return pyat;
        }
        int edinicy = kolichestvo % 10;
        if (edinicy == 1) {
            return odin;
        }
        if (edinicy >= 2 && edinicy <= 4) {
            return dva;
        }
        return pyat;
    }
}
