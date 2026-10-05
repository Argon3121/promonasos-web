package ru.promonasos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.promonasos.model.SredaUplotneniya;
import ru.promonasos.model.Uplotnenie;
import ru.promonasos.model.OtraslUplotneniy;
import ru.promonasos.model.ZaprosPodboraUplotneniya;
import ru.promonasos.service.UplotneniyaService;

import java.util.Map;

// каталог уплотнений — заходят по среде, не по марке насоса
@Controller
public class UplotneniyaController {

    private static final Map<String, String> STARYE_SLUGI_IZDELIY = Map.ofEntries(
            Map.entry("tortsevye-uplotneniya-2c-vtorichnoe-uplotnenie-rezina-251-d", "251-d"),
            Map.entry("tortsevye-uplotneniya-2c-vtorichnoe-uplotnenie-rezina-211r-l", "211r-l"),
            Map.entry("tortsevye-uplotneniya-2c-vtorichnoe-uplotnenie-ftoroplast-153-d", "153-d"),
            Map.entry("tortsevye-uplotneniya-s-metallicheskim-silfonom-338", "338"),
            Map.entry("tortsevye-uplotneniya-2c-vtorichnoe-uplotnenie-rezina-251-2c-211-2c-311", "251-211-311"),
            Map.entry("tortsevye-uplotneniya-dlya-uplotnitelnykh-kompleksov-351-n-2c-361-n", "351n-361n"),
            Map.entry("tortsevye-uplotneniya-2c-vtorichnoe-uplotnenie-ftoroplast-153-2c-353-2c-313", "153-353-313"),
            Map.entry("tortsevye-uplotneniya-dlya-uplotnitelnykh-kompleksov-338-n", "338-n"),
            Map.entry("tortsevye-uplotneniya-dlya-uplotnitelnykh-kompleksov-351-t-n", "351t-n"),
            Map.entry("tortsevye-uplotneniya-dlya-uplotnitelnykh-kompleksov-338-t-n", "338-t-n"),
            Map.entry("tortsevye-uplotneniya-s-rezinovym-silfonom-112-2c-212-n", "112-212-n"),
            Map.entry("tortsevye-uplotneniya-s-rezinovym-silfonom-212-n4", "212-n4"),
            Map.entry("tortsevye-uplotneniya-modulnogo-tipa-211-c-2c-361-c", "211c-361c"),
            Map.entry("tortsevye-uplotneniya-modulnogo-tipa-264-2c-365", "264-365")
    );

    private final UplotneniyaService uplotneniyaService;
    public UplotneniyaController(UplotneniyaService uplotneniyaService) {
        this.uplotneniyaService = uplotneniyaService;
    }

    // список сред
    @GetMapping("/tortsevye-uplotneniya")
    public String katalog(Model model) {
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniya());

        model.addAttribute("zagolovok", "Торцовые уплотнения — каталог по средам | ТД «Промоборудование»");
        model.addAttribute("opisanie", "Каталог торцовых уплотнений с параметрами для сравнения. Совместимость проверяется по документации и условиям конкретного процесса.");
        model.addAttribute("canonical", "/tortsevye-uplotneniya");
        return "uplotneniya-katalog/katalog";
    }

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
        model.addAttribute("opisanie", "Предварительный фильтр торцовых уплотнений по среде, диаметру вала, давлению и температуре. Результат необходимо сверить по паспорту.");
        model.addAttribute("canonical", "/tortsevye-uplotneniya/podbor");
        model.addAttribute("noindex", estZapros);
        return "uplotneniya-podbor/podbor";
    }

    @GetMapping("/tortsevye-uplotneniya/{sreda}")
    public Object sreda(@PathVariable String sreda, Model model) {
        String slugIzdeliya = STARYE_SLUGI_IZDELIY.get(sreda);
        if (slugIzdeliya != null) {
            RedirectView redirect = new RedirectView("/tortsevye-uplotneniya/izdelie/" + slugIzdeliya, true, false);
            redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirect;
        }

        SredaUplotneniya sredaObj = uplotneniyaService.getSredu(sreda);
        if (sredaObj != null) {
            model.addAttribute("sreda", sredaObj);
            model.addAttribute("industryPage", false);
            model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniyaPoSrede(sredaObj.getKod()));
            model.addAttribute("drugieSredy", uplotneniyaService.getSredyUplotneniy().stream()
                    .filter(s -> !s.getSlug().equals(sreda)).toList());
            model.addAttribute("zagolovok", sredaObj.getSeoTitle());
            model.addAttribute("opisanie", sredaObj.getSeoDescription());
            model.addAttribute("canonical", "/tortsevye-uplotneniya/" + sredaObj.getSlug());
            model.addAttribute("ogImage", "/images/uplotnenie.jpg");
            return "uplotneniya-sreda/sreda";
        }

        OtraslUplotneniy otrasl = uplotneniyaService.getOtrasl(sreda);
        if (otrasl == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        model.addAttribute("otrasl", otrasl);
        model.addAttribute("uplotneniya", uplotneniyaService.getUplotneniyaPoOtrasli(sreda));
        model.addAttribute("industryPage", true);
        model.addAttribute("drugieSredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("zagolovok", otrasl.seoTitle());
        model.addAttribute("opisanie", otrasl.seoDescription());
        model.addAttribute("canonical", "/tortsevye-uplotneniya/" + otrasl.slug());
        model.addAttribute("ogImage", "/images/uplotnenie.jpg");
        return "uplotneniya-otrasl/otrasl";
    }

    // люди гуглят номер изделия, не только среду
    @GetMapping("/tortsevye-uplotneniya/izdelie/{slug}")
    public String izdelie(@PathVariable String slug, Model model) {
        Uplotnenie izdelie = uplotneniyaService.getUplotnenie(slug);
        if (izdelie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("izdelie", izdelie);
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy().stream()
                .filter(s -> izdelie.getSredyKody().contains(s.getKod())).toList());
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
