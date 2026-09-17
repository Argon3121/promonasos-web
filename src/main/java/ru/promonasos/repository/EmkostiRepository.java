package ru.promonasos.repository;

import org.springframework.stereotype.Repository;
import ru.promonasos.model.EmkostEP;

import java.util.List;

// подземные ёмкости ЕП, данные с hermetica.su
@Repository
public class EmkostiRepository {

    // обозначение, объём (м³), диаметр (мм), длина (мм), масса (кг)
    private final List<EmkostEP> emkosti = List.of(
            new EmkostEP("ЕП-4", 4, 1600, 2250, 1400),
            new EmkostEP("ЕП-5", 5, 1600, 2755, 1430),
            new EmkostEP("ЕП-8", 8, 2000, 2880, 2850),
            new EmkostEP("ЕП-12,5", 12.5, 2000, 4280, 2920),
            new EmkostEP("ЕП-16", 16, 2000, 5280, 3430),
            new EmkostEP("ЕП-20", 20, 2400, 4826, 3650),
            new EmkostEP("ЕП-25", 25, 2400, 5826, 4300),
            new EmkostEP("ЕП-40", 40, 2400, 9026, 6270),
            new EmkostEP("ЕП-63", 63, 3000, 9244, 8990),
            new EmkostEP("ЕП-100", 100, 3000, 13910, 10190)
    );

    public List<EmkostEP> getEmkosti() {
        return emkosti;
    }
}
