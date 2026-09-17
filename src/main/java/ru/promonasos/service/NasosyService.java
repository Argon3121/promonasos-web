package ru.promonasos.service;

import org.springframework.stereotype.Service;
import ru.promonasos.model.*;
import ru.promonasos.repository.NasosyRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NasosyService {

    private final NasosyRepository repository;

    // Специализированные группы: их насосы физически способны качать чистую воду,
    // но выбирать их для водяной задачи без нужды не стоит. Поэтому при подборе
    // без указания среды (или по «воде») они идут после общепромышленных, а не
    // вперемешку — иначе на запрос «60 м³/ч, 30 м» первым выпадает песковый насос
    // просто потому, что его номинальная точка ближе.
    private static final Set<String> SPETSIALIZIROVANNYE_GRUPPY = Set.of(
            "khimicheskie", "neftyanye", "fekalnye-stochnye", "gruntovye", "peskovye",
            "skvazhinnye", "vintovye", "dozirovochnye", "burovye", "plunzhernye", "vakuumnye");

    public NasosyService(NasosyRepository repository) {
        this.repository = repository;
    }

    public List<GruppaNasosov> getGruppy() {
        return repository.getGruppy();
    }

    public List<MarkaNasosa> getMarkiNasosov() {
        return repository.getMarkiNasosov();
    }

    public List<MarkaNasosa> getMarkiPoGruppe(String gruppaSlug) {
        return repository.getMarkiPoGruppe(gruppaSlug);
    }

    // быстрый доступ к группе по slug, когда под рукой только марка
    public Map<String, GruppaNasosov> getGruppyPoSlug() {
        return repository.getGruppy().stream().collect(Collectors.toMap(GruppaNasosov::getSlug, g -> g));
    }

    // сгруппировано заранее — в Thymeleaf список так просто не отфильтруешь, как в Razor
    public Map<String, List<MarkaNasosa>> getMarkiPoGruppam() {
        return repository.getMarkiNasosov().stream()
                .collect(Collectors.groupingBy(MarkaNasosa::getGruppaSlug));
    }

    public MarkaNasosa getMarku(String slug) {
        return repository.getMarku(slug);
    }

    public List<ModelNasosa> getModeliPoMarke(String markaSlug) {
        return repository.getModeliPoMarke(markaSlug);
    }

    public ModelNasosa getModel(String markaSlug, String modelSlug) {
        return repository.getModel(markaSlug, modelSlug);
    }

    public int vsegoModeley() {
        return repository.vsegoModeley();
    }

    // у насоса кривая Q-H, а в каталоге одна точка — поэтому это направление для расчёта, не готовый ответ
    public List<RezultatPodbora> podobratNasos(ZaprosPodboraNasosa zapros) {
        if (zapros == null) {
            return List.of();
        }

        List<ModelNasosa> vyborka = repository.getVseModeli();

        if (zapros.getSreda() != null && !zapros.getSreda().isEmpty()) {
            Set<String> podkhodyashchie = repository.getMarkiNasosov().stream()
                    .filter(m -> m.getSredy().contains(zapros.getSreda()))
                    .map(MarkaNasosa::getSlug)
                    .collect(Collectors.toSet());
            vyborka = vyborka.stream().filter(m -> podkhodyashchie.contains(m.getMarkaSlug())).toList();
        }

        List<RezultatPodbora> rezultat = new ArrayList<>();

        for (ModelNasosa model : vyborka) {
            double otklonenie = 0;
            List<String> prichiny = new ArrayList<>();

            if (zapros.getPodacha() != null && zapros.getPodacha() > 0) {
                double otnoshenie = model.getPodacha() / zapros.getPodacha();
                if (otnoshenie < 0.6 || otnoshenie > 2.2) {
                    continue; // совсем другой масштаб
                }
                otklonenie += Math.abs(Math.log(otnoshenie)) * 2;
                int procent = (int) Math.round((otnoshenie - 1) * 100);
                if (procent == 0) {
                    prichiny.add("подача совпадает с запросом");
                } else if (procent > 0) {
                    prichiny.add("подача с запасом " + procent + " %");
                } else {
                    prichiny.add("подача ниже запроса на " + (-procent) + " %");
                }
            }

            if (zapros.getNapor() != null && zapros.getNapor() > 0) {
                double otnoshenie = model.getNapor() / zapros.getNapor();
                if (otnoshenie < 0.95 || otnoshenie > 3) {
                    continue; // не добивает или запас абсурдный
                }
                otklonenie += Math.abs(Math.log(otnoshenie)) * 1.6;
                int procent = (int) Math.round((otnoshenie - 1) * 100);
                prichiny.add(procent <= 2 ? "напор соответствует задаче" : "запас по напору " + procent + " %");
            }

            String verdikt;
            if (zapros.getPodacha() == null && zapros.getNapor() == null) {
                verdikt = null;
            } else if (otklonenie < 0.16) {
                verdikt = "tochno";
            } else if (otklonenie < 0.45) {
                verdikt = "podoydet";
            } else {
                verdikt = "napredele";
            }

            MarkaNasosa marka = repository.getMarku(model.getMarkaSlug());
            rezultat.add(new RezultatPodbora(model, marka, otklonenie, verdikt, String.join(", ", prichiny)));
        }

        boolean sredaNeZadana = zapros.getSreda() == null || zapros.getSreda().isEmpty()
                || zapros.getSreda().equals("voda");

        Comparator<RezultatPodbora> poBlizosti = Comparator
                .comparingDouble(RezultatPodbora::getOtklonenie)
                .thenComparingDouble(r -> r.getModel().getPodacha());

        Comparator<RezultatPodbora> poryadok = sredaNeZadana
                ? Comparator.<RezultatPodbora>comparingInt(r ->
                        SPETSIALIZIROVANNYE_GRUPPY.contains(r.getMarka().getGruppaSlug()) ? 1 : 0)
                    .thenComparing(poBlizosti)
                : poBlizosti;

        return rezultat.stream().sorted(poryadok).toList();
    }
}
