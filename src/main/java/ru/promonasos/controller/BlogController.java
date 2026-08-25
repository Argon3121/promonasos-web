package ru.promonasos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.promonasos.model.StatyaBloga;
import ru.promonasos.service.BlogService;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.UplotneniyaService;

/** База знаний: информационный трафик и снятие возражений перед заявкой. */
@Controller
public class BlogController {

    private final BlogService blogService;
    private final NasosyService nasosyService;
    private final UplotneniyaService uplotneniyaService;

    public BlogController(BlogService blogService, NasosyService nasosyService,
                           UplotneniyaService uplotneniyaService) {
        this.blogService = blogService;
        this.nasosyService = nasosyService;
        this.uplotneniyaService = uplotneniyaService;
    }

    /** Список статей с фильтром по рубрике. /blog */
    @GetMapping("/blog")
    public String spisok(@RequestParam(required = false) String rubrika, Model model) {
        model.addAttribute("stati", blogService.getStatiPoRubrike(rubrika));
        model.addAttribute("rubriki", blogService.getRubriki());
        model.addAttribute("tekushchayaRubrika", rubrika);

        model.addAttribute("zagolovok", "База знаний: подбор насосов и торцовых уплотнений | ТД «Промоборудование»");
        model.addAttribute("opisanie", "Статьи инженеров о подборе насосов по рабочей точке, выборе торцовых уплотнений, парах трения, причинах отказов и импортозамещении.");
        model.addAttribute("canonical", "/blog");
        model.addAttribute("noindex", rubrika != null && !rubrika.isEmpty());
        return "blog/spisok";
    }

    /** Статья. /blog/{slug} */
    @GetMapping("/blog/{slug}")
    public String statya(@PathVariable String slug, Model model) {
        StatyaBloga statya = blogService.getStatyu(slug);
        if (statya == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("statya", statya);
        model.addAttribute("marki", nasosyService.getMarkiNasosov().stream()
                .filter(m -> statya.getSvyazannyeMarki().contains(m.getSlug())).toList());
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy().stream()
                .filter(s -> statya.getSvyazannyeSredy().contains(s.getSlug())).toList());
        model.addAttribute("drugie", blogService.getStati().stream()
                .filter(s -> !s.getSlug().equals(slug)).limit(3).toList());

        model.addAttribute("zagolovok", statya.getSeoTitle());
        model.addAttribute("opisanie", statya.getSeoDescription());
        model.addAttribute("canonical", "/blog/" + statya.getSlug());
        return "blog/statya";
    }
}
