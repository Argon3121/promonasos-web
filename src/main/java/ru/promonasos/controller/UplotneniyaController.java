package ru.promonasos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.promonasos.model.SredaUplotneniya;
import ru.promonasos.model.Uplotnenie;
import ru.promonasos.model.ZaprosPodboraUplotneniya;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.UplotneniyaService;

/**
 * Каталог торцовых уплотнений. Точка входа — перекачиваемая среда,
 * а не марка насоса: именно среда определяет материалы и конструкцию.
 */
@Controller
public class UplotneniyaController {

    private final UplotneniyaService uplotneniyaService;
    private final NasosyService nasosyService;

    public UplotneniyaController(UplotneniyaService uplotneniyaService, NasosyService nasosyService) {
        this.uplotneniyaService = uplotneniyaService;
        this.nasosyService = nasosyService;
    }

    /** Список сред. /tortsevye-uplotneniya */
    @GetMapping("/tortsevye-uplotneniya")
    public String katalog(Model model) {
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniya());

        model.addAttribute("zagolovok", "Торцовые уплотнения — каталог по средам | ТД «Промоборудование»");
        model.addAttribute("opisanie", "Торцовые уплотнения собственного производства: подбор по перекачиваемой среде, давлению, температуре и диаметру вала. Вода, нефтепродукты, кислоты, пульпа, пищевые среды.");
        model.addAttribute("canonical", "/tortsevye-uplotneniya");
        return "uplotneniya-katalog/katalog";
    }

    /** Подбор по параметрам среды. /tortsevye-uplotneniya/podbor */
    @GetMapping("/tortsevye-uplotneniya/podbor")
    public String podbor(@RequestParam(required = false) String sreda,
                          @RequestParam(required = false) Double diametrVala,
                          @RequestParam(required = false) Double davlenie,
                          @RequestParam(required = false) Double temperatura,
                          Model model) {
        ZaprosPodboraUplotneniya zapros = new ZaprosPodboraUplotneniya(sreda, diametrVala, davlenie, temperatura);
        boolean estZapros = (sreda != null && !sreda.isEmpty())
                || diametrVala != null || davlenie != null || temperatura != null;

        model.addAttribute("zapros", zapros);
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("estZapros", estZapros);
        model.addAttribute("rezultaty", uplotneniyaService.podobratUplotnenie(zapros));

        model.addAttribute("zagolovok", "Подбор торцового уплотнения по среде и параметрам — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Подбор торцового уплотнения: перекачиваемая среда, диаметр вала, рабочее давление и температура. Показываем изделия, которые перекрывают ваши условия.");
        model.addAttribute("canonical", "/tortsevye-uplotneniya/podbor");
        model.addAttribute("noindex", estZapros);
        return "uplotneniya-podbor/podbor";
    }

    /** Страница среды. /tortsevye-uplotneniya/{sreda} */
    @GetMapping("/tortsevye-uplotneniya/{sreda}")
    public String sreda(@PathVariable String sreda, Model model) {
        SredaUplotneniya sredaObj = uplotneniyaService.getSredu(sreda);
        if (sredaObj == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("sreda", sredaObj);
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniyaPoSrede(sredaObj.getKod()));
        model.addAttribute("drugieSredy", uplotneniyaService.getSredyUplotneniy().stream()
                .filter(s -> !s.getSlug().equals(sreda)).toList());
        // Марки насосов, работающие на этой среде, — блок перелинковки каталог ↔ уплотнения
        model.addAttribute("marki", nasosyService.getMarkiNasosov().stream()
                .filter(m -> m.getSredy().contains(sredaObj.getKod())).toList());

        model.addAttribute("zagolovok", sredaObj.getSeoTitle());
        model.addAttribute("opisanie", sredaObj.getSeoDescription());
        model.addAttribute("canonical", "/tortsevye-uplotneniya/" + sredaObj.getSlug());
        model.addAttribute("ogImage", "/images/uplotnenie.jpg");
        return "uplotneniya-sreda/sreda";
    }

    /**
     * Страница конкретного изделия. /tortsevye-uplotneniya/izdelie/{slug}
     * Отдельный URL под точное обозначение — люди ищут по номеру изделия
     * ("уплотнение 338 характеристики"), а не только по среде.
     */
    @GetMapping("/tortsevye-uplotneniya/izdelie/{slug}")
    public String izdelie(@PathVariable String slug, Model model) {
        Uplotnenie izdelie = uplotneniyaService.getUplotnenie(slug);
        if (izdelie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("izdelie", izdelie);
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy().stream()
                .filter(s -> izdelie.getSredyKody().contains(s.getKod())).toList());
        model.addAttribute("marki", nasosyService.getMarkiNasosov().stream()
                .filter(m -> izdelie.getPrimenyaetsyaNaNasosakh().contains(m.getOboznachenie())).toList());
        model.addAttribute("pohozhie", uplotneniyaService.getUplotneniya().stream()
                .filter(u -> !u.getSlug().equals(slug))
                .filter(u -> u.getSredyKody().stream().anyMatch(k -> izdelie.getSredyKody().contains(k)))
                .limit(4).toList());

        model.addAttribute("zagolovok", izdelie.getSeoTitle());
        model.addAttribute("opisanie", izdelie.getSeoDescription());
        model.addAttribute("canonical", "/tortsevye-uplotneniya/izdelie/" + izdelie.getSlug());
        model.addAttribute("ogImage", "/images/uplotnenie.jpg");
        return "uplotneniya-izdelie/izdelie";
    }
}
