package ru.promonasos.service;

import org.junit.jupiter.api.Test;
import ru.promonasos.model.MarkaNasosa;
import ru.promonasos.model.ModelNasosa;
import ru.promonasos.model.RezultatPodbora;
import ru.promonasos.model.ZaprosPodboraNasosa;
import ru.promonasos.repository.NasosyRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NasosyServiceTest {

    private final NasosyRepository repository = new NasosyRepository();
    private final NasosyService service = new NasosyService(repository);

    @Test
    void bezZaprosaVozvrashchaetPustoiSpisok() {
        assertTrue(service.podobratNasos(null).isEmpty());
    }

    @Test
    void tochnoeSovpadenieDaetVerdiktTochno() {
        ModelNasosa etalon = repository.getVseModeli().get(0);
        var zapros = new ZaprosPodboraNasosa(null, etalon.getPodacha(), etalon.getNapor());

        List<RezultatPodbora> rezultaty = service.podobratNasos(zapros);

        RezultatPodbora naidenny = rezultaty.stream()
                .filter(r -> r.getModel().getSlug().equals(etalon.getSlug())
                        && r.getModel().getMarkaSlug().equals(etalon.getMarkaSlug()))
                .findFirst().orElseThrow(() -> new AssertionError("Эталонная модель не попала в подбор"));

        assertEquals("tochno", naidenny.getVerdikt());
        assertEquals(0.0, naidenny.getOtklonenie(), 0.0001);
    }

    @Test
    void slishkomDalyokayaPodachaOtsekaetsya() {
        ModelNasosa etalon = repository.getVseModeli().get(0);
        // в 10 раз больше запрошенной подачи — за пределами допустимого диапазона (0.6–2.2)
        var zapros = new ZaprosPodboraNasosa(null, etalon.getPodacha() * 10, null);

        boolean etalonEstVRezultate = service.podobratNasos(zapros).stream()
                .anyMatch(r -> r.getModel().getSlug().equals(etalon.getSlug())
                        && r.getModel().getMarkaSlug().equals(etalon.getMarkaSlug()));

        assertFalse(etalonEstVRezultate);
    }

    @Test
    void filtrPoSredeOstavlyaetTolkoPodkhodyashchieMarki() {
        MarkaNasosa markaSoSredoy = repository.getMarkiNasosov().stream()
                .filter(m -> !m.getSredy().isEmpty())
                .findFirst().orElseThrow();
        String sreda = markaSoSredoy.getSredy().get(0);

        var zapros = new ZaprosPodboraNasosa(sreda, null, null);
        List<RezultatPodbora> rezultaty = service.podobratNasos(zapros);

        assertFalse(rezultaty.isEmpty());
        assertTrue(rezultaty.stream().allMatch(r -> r.getMarka().getSredy().contains(sreda)));
    }
}
