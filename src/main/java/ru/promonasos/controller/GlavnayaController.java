package ru.promonasos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.promonasos.model.Zayavka;
import ru.promonasos.service.BlogService;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.UplotneniyaService;
import ru.promonasos.service.UslugiService;

import java.net.URI;

/** Главная страница, контакты, о компании, приём заявки, robots.txt и sitemap.xml. */
@Controller
public class GlavnayaController {

    private final NasosyService nasosyService;
    private final UplotneniyaService uplotneniyaService;
    private final UslugiService uslugiService;
    private final BlogService blogService;

    public GlavnayaController(NasosyService nasosyService, UplotneniyaService uplotneniyaService,
                               UslugiService uslugiService, BlogService blogService) {
        this.nasosyService = nasosyService;
        this.uplotneniyaService = uplotneniyaService;
        this.uslugiService = uslugiService;
        this.blogService = blogService;
    }

    /** Главная. Ключевой блок — марки насосов с техническим описанием. */
    @GetMapping("/")
    public String glavnaya(Model model) {
        model.addAttribute("marki", nasosyService.getMarkiNasosov());
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("uslugi", uslugiService.getUslugi());
        model.addAttribute("stati", blogService.getStati().stream().limit(3).toList());
        model.addAttribute("vsegoModeley", nasosyService.vsegoModeley());

        model.addAttribute("zagolovok", "Промышленные насосы и торцовые уплотнения — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Поставка промышленных насосов К, КМ, Д, ЦНС, Х, АХ, СМ, Ф, ТК, ВК и производство торцовых уплотнений с 2001 года. Подбор по рабочей точке, импортозамещение уплотнений, шефмонтаж. Москва.");
        model.addAttribute("canonical", "/");
        return "glavnaya";
    }

    /** Контакты и реквизиты. */
    @GetMapping("/kontakty")
    public String kontakty(Model model) {
        model.addAttribute("zagolovok", "Контакты — ТД «Промоборудование», Москва");
        model.addAttribute("opisanie", "Адрес, телефон и реквизиты ТД «Промоборудование»: 109544, Москва, ул. Рабочая, д. 93, стр. 2, офис 236. Телефон +7 (495) 925-05-03.");
        model.addAttribute("canonical", "/kontakty");
        return "kontakty";
    }

    /** Источники и лицензии фотографий — обязательное условие CC BY-SA/CC BY. */
    @GetMapping("/istochniki-izobrazheniy")
    public String istochnikiIzobrazheniy(Model model) {
        model.addAttribute("zagolovok", "Источники изображений — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Авторы и лицензии фотографий, использованных на сайте.");
        model.addAttribute("canonical", "/istochniki-izobrazheniy");
        model.addAttribute("noindex", true);
        return "istochniki-izobrazheniy";
    }

    /** О компании: опыт, производство, сертификаты. */
    @GetMapping("/o-kompanii")
    public String oKompanii(Model model) {
        model.addAttribute("zagolovok", "О компании — ТД «Промоборудование», с 2001 года");
        model.addAttribute("opisanie", "ТД «Промоборудование»: поставка промышленных насосов и собственное производство торцовых уплотнений с 2001 года. Более 250 предприятий-заказчиков.");
        model.addAttribute("canonical", "/o-kompanii");
        return "o-kompanii";
    }

    /** Приём заявки. В прототипе только валидация и подтверждение. */
    @PostMapping("/zayavka")
    public String zayavka(@Valid Zayavka zayavka, BindingResult bindingResult,
                           HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Referer приходит абсолютным адресом (https://хост/nasosy/k), поэтому
        // берём путь из Referer и только если хост совпадает с нашим.
        String vernutsya = "/";
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            try {
                URI refererUri = URI.create(referer);
                if (refererUri.getHost() != null
                        && refererUri.getHost().equalsIgnoreCase(request.getServerName())) {
                    String zapros = refererUri.getRawQuery();
                    vernutsya = refererUri.getRawPath() + (zapros != null ? "?" + zapros : "");
                }
            } catch (IllegalArgumentException ignored) {
                // некорректный Referer — остаёмся с "/"
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("oshibka", "Проверьте заполнение формы: не хватает обязательных полей.");
            return "redirect:" + vernutsya;
        }

        // TODO: отправка на почту и в CRM.
        redirectAttributes.addFlashAttribute("uspekh", "Заявка принята. Инженер ответит в течение одного рабочего дня.");
        return "redirect:" + vernutsya;
    }

    /** robots.txt отдаётся кодом, чтобы домен не пришлось править в двух местах. */
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String robots(HttpServletRequest request) {
        String domen = request.getScheme() + "://" + request.getHeader("Host");
        StringBuilder sb = new StringBuilder();
        sb.append("User-agent: *\n");
        sb.append("Disallow: /zayavka\n");
        sb.append("Clean-param: utm_source&utm_medium&utm_campaign&utm_term&utm_content\n");
        sb.append("\n");
        sb.append("Sitemap: ").append(domen).append("/sitemap.xml\n");
        return sb.toString();
    }

    /** Карта сайта строится из каталога: новые марки попадают в неё автоматически. */
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String sitemap(HttpServletRequest request) {
        String domen = request.getScheme() + "://" + request.getHeader("Host");
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        dobavitUrl(sb, domen, "/", "1.0", "weekly");
        dobavitUrl(sb, domen, "/nasosy", "0.9", "weekly");
        dobavitUrl(sb, domen, "/nasosy/podbor", "0.9", "monthly");
        dobavitUrl(sb, domen, "/tortsevye-uplotneniya", "0.9", "weekly");
        dobavitUrl(sb, domen, "/tortsevye-uplotneniya/podbor", "0.9", "monthly");
        dobavitUrl(sb, domen, "/uslugi", "0.8", "monthly");
        dobavitUrl(sb, domen, "/blog", "0.7", "weekly");
        dobavitUrl(sb, domen, "/o-kompanii", "0.5", "yearly");
        dobavitUrl(sb, domen, "/kontakty", "0.6", "yearly");

        nasosyService.getMarkiNasosov().forEach(marka -> {
            dobavitUrl(sb, domen, "/nasosy/" + marka.getSlug(), "0.9", "weekly");
            nasosyService.getModeliPoMarke(marka.getSlug()).forEach(model ->
                    dobavitUrl(sb, domen, "/nasosy/" + marka.getSlug() + "/" + model.getSlug(), "0.7", "monthly"));
        });

        uplotneniyaService.getSredyUplotneniy().forEach(sreda ->
                dobavitUrl(sb, domen, "/tortsevye-uplotneniya/" + sreda.getSlug(), "0.8", "monthly"));

        uplotneniyaService.getUplotneniya().forEach(izdelie ->
                dobavitUrl(sb, domen, "/tortsevye-uplotneniya/izdelie/" + izdelie.getSlug(), "0.7", "monthly"));

        // У статей есть настоящая дата публикации — добавляем lastmod, чтобы
        // поисковик знал, что страницу не нужно перепроверять на изменения.
        blogService.getStati().forEach(statya ->
                dobavitUrl(sb, domen, "/blog/" + statya.getSlug(), "0.6", "monthly", statya.getData()));

        sb.append("</urlset>\n");
        return sb.toString();
    }

    private void dobavitUrl(StringBuilder sb, String domen, String put, String prioritet, String chastota) {
        dobavitUrl(sb, domen, put, prioritet, chastota, null);
    }

    private void dobavitUrl(StringBuilder sb, String domen, String put, String prioritet, String chastota,
                             java.time.LocalDate lastmod) {
        sb.append("  <url>\n");
        sb.append("    <loc>").append(domen).append(put).append("</loc>\n");
        if (lastmod != null) {
            sb.append("    <lastmod>").append(lastmod).append("</lastmod>\n");
        }
        sb.append("    <changefreq>").append(chastota).append("</changefreq>\n");
        sb.append("    <priority>").append(prioritet).append("</priority>\n");
        sb.append("  </url>\n");
    }
}
