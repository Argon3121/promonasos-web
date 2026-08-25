package ru.promonasos.service;

import org.junit.jupiter.api.Test;
import ru.promonasos.model.Uplotnenie;
import ru.promonasos.model.ZaprosPodboraUplotneniya;
import ru.promonasos.repository.UplotneniyaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UplotneniyaServiceTest {

    private final UplotneniyaRepository repository = new UplotneniyaRepository();
    private final UplotneniyaService service = new UplotneniyaService(repository);

    @Test
    void bezZaprosaVozvrashchaetVseUplotneniya() {
        assertEquals(repository.getUplotneniya().size(), service.podobratUplotnenie(null).size());
    }

    @Test
    void diametrValaVneDiapazonaIsklyuchaetIzdelie() {
        Uplotnenie etalon = repository.getUplotneniya().get(0);
        // на 1 мм больше верхней границы вала — изделие не подходит
        var zapros = new ZaprosPodboraUplotneniya(null, etalon.getDiametrValaDo() + 1, null, null);

        boolean etalonEstVRezultate = service.podobratUplotnenie(zapros).stream()
                .anyMatch(u -> u.getSlug().equals(etalon.getSlug()));

        assertFalse(etalonEstVRezultate);
    }

    @Test
    void diametrValaVDiapazoneOstavlyaetIzdelie() {
        Uplotnenie etalon = repository.getUplotneniya().get(0);
        double seredinaDiapazona = (etalon.getDiametrValaOt() + etalon.getDiametrValaDo()) / 2;
        var zapros = new ZaprosPodboraUplotneniya(null, seredinaDiapazona, null, null);

        List<Uplotnenie> rezultaty = service.podobratUplotnenie(zapros);

        assertTrue(rezultaty.stream().anyMatch(u -> u.getSlug().equals(etalon.getSlug())));
    }

    @Test
    void davlenieVysheDopustimogoIsklyuchaetIzdelie() {
        Uplotnenie etalon = repository.getUplotneniya().get(0);
        var zapros = new ZaprosPodboraUplotneniya(null, null, etalon.getDavlenieDo() + 100, null);

        boolean etalonEstVRezultate = service.podobratUplotnenie(zapros).stream()
                .anyMatch(u -> u.getSlug().equals(etalon.getSlug()));

        assertFalse(etalonEstVRezultate);
    }

    @Test
    void filtrPoSredeOstavlyaetTolkoPodkhodyashchieIzdeliya() {
        Uplotnenie etalon = repository.getUplotneniya().stream()
                .filter(u -> !u.getSredyKody().isEmpty())
                .findFirst().orElseThrow();
        String sreda = etalon.getSredyKody().get(0);

        List<Uplotnenie> rezultaty = service.podobratUplotnenie(
                new ZaprosPodboraUplotneniya(sreda, null, null, null));

        assertFalse(rezultaty.isEmpty());
        assertTrue(rezultaty.stream().allMatch(u -> u.getSredyKody().contains(sreda)));
    }
}
