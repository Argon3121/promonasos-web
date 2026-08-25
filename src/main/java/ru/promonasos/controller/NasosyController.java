package ru.promonasos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ru.promonasos.model.MarkaNasosa;
import ru.promonasos.model.ModelNasosa;
import ru.promonasos.model.ZaprosPodboraNasosa;
import ru.promonasos.service.BlogService;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.UplotneniyaService;

import java.util.List;

/**
 * Каталог насосов: список марок, страница марки с модельным рядом,
 * страница типоразмера и подбор по рабочей точке.
 */
@Controller
public class NasosyController {

    private final NasosyService nasosyService;
    private final UplotneniyaService uplotneniyaService;
    private final BlogService blogService;

    public NasosyController(NasosyService nasosyService, UplotneniyaService uplotneniyaService,
                             BlogService blogService) {
        this.nasosyService = nasosyService;
        this.uplotneniyaService = uplotneniyaService;
        this.blogService = blogService;
    }

    /** Все марки, сгруппированные по назначению. /nasosy */
    @GetMapping("/nasosy")
    public String katalog(Model model) {
        model.addAttribute("marki", nasosyService.getMarkiNasosov());
        model.addAttribute("vsegoModeley", nasosyService.vsegoModeley());

        model.addAttribute("zagolovok", "Промышленные насосы — каталог марок и моделей | ТД «Промоборудование»");
        model.addAttribute("opisanie", "Каталог промышленных насосов: К, КМ, Д, ЦНС, Х, АХ, СМ, Ф, ТК, ВК, ПЭ, СЭ. Технические характеристики, модельные ряды, подбор по подаче и напору. Москва, с 2001 года.");
        model.addAttribute("canonical", "/nasosy");
        return "nasosy/katalog";
    }

    /**
     * Подбор по рабочей точке. /nasosy/podbor
     * Литеральный путь "/nasosy/podbor" Spring всегда предпочитает шаблону
     * "/nasosy/{marka}" независимо от порядка объявления методов — как и в ASP.NET Core.
     */
    @GetMapping("/nasosy/podbor")
    public String podbor(@RequestParam(required = false) String sreda,
                          @RequestParam(required = false) Double podacha,
                          @RequestParam(required = false) Double napor,
                          Model model) {
        ZaprosPodboraNasosa zapros = new ZaprosPodboraNasosa(sreda, podacha, napor);
        boolean estZapros = podacha != null || napor != null || (sreda != null && !sreda.isEmpty());

        model.addAttribute("zapros", zapros);
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("estZapros", estZapros);
        model.addAttribute("rezultaty", estZapros ? nasosyService.podobratNasos(zapros) : List.of());

        model.addAttribute("zagolovok", "Подбор насоса по подаче и напору — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Подбор промышленного насоса по рабочей точке: укажите среду, подачу в м³/ч и напор в метрах. Показываем подходящие типоразмеры с обоснованием.");
        model.addAttribute("canonical", "/nasosy/podbor");
        model.addAttribute("noindex", estZapros); // страницы с параметрами в индекс не пускаем
        return "nasosy/podbor";
    }

    /** Страница марки с модельным рядом. /nasosy/{marka} */
    @GetMapping("/nasosy/{marka}")
    public String marka(@PathVariable String marka, Model model) {
        MarkaNasosa markaObj = nasosyService.getMarku(marka);
        if (markaObj == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<ModelNasosa> modeli = nasosyService.getModeliPoMarke(marka);

        model.addAttribute("marka", markaObj);
        model.addAttribute("modeli", modeli);
        model.addAttribute("podachaOt", modeli.stream().mapToDouble(ModelNasosa::getPodacha).min().orElse(0));
        model.addAttribute("podachaDo", modeli.stream().mapToDouble(ModelNasosa::getPodacha).max().orElse(0));
        model.addAttribute("naporOt", modeli.stream().mapToDouble(ModelNasosa::getNapor).min().orElse(0));
        model.addAttribute("naporDo", modeli.stream().mapToDouble(ModelNasosa::getNapor).max().orElse(0));
        model.addAttribute("gruppa", nasosyService.getGruppy().stream()
                .filter(g -> g.getSlug().equals(markaObj.getGruppaSlug())).findFirst().orElse(null));
        model.addAttribute("sosedniMarki", nasosyService.getMarkiPoGruppe(markaObj.getGruppaSlug()).stream()
                .filter(m -> !m.getSlug().equals(marka)).toList());
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniyaPoMarke(markaObj.getOboznachenie()));
        model.addAttribute("stati", blogService.getStatiPoMarke(marka));

        model.addAttribute("zagolovok", markaObj.getSeoTitle());
        model.addAttribute("opisanie", markaObj.getSeoDescription());
        model.addAttribute("canonical", "/nasosy/" + markaObj.getSlug());
        return "nasosy/marka";
    }

    /** Страница типоразмера. /nasosy/{marka}/{modelSlug} */
    @GetMapping("/nasosy/{marka}/{modelSlug}")
    public String model(@PathVariable String marka, @PathVariable String modelSlug, Model model) {
        MarkaNasosa markaObj = nasosyService.getMarku(marka);
        if (markaObj == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ModelNasosa modelObj = nasosyService.getModel(marka, modelSlug);
        if (modelObj == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<ModelNasosa> vse = nasosyService.getModeliPoMarke(marka);

        model.addAttribute("model", modelObj);
        model.addAttribute("marka", markaObj);
        model.addAttribute("drugieModeli", vse.stream()
                .filter(m -> !m.getSlug().equals(modelSlug)).limit(8).toList());
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniyaPoMarke(markaObj.getOboznachenie()));

        model.addAttribute("zagolovok", modelObj.getSeoTitle());
        model.addAttribute("opisanie", modelObj.getSeoDescription());
        model.addAttribute("canonical", "/nasosy/" + marka + "/" + modelSlug);
        return "nasosy/model";
    }
}
