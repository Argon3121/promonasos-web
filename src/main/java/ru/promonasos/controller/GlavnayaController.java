package ru.promonasos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.promonasos.model.Zayavka;
import ru.promonasos.service.BlogService;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.SertifikatyService;
import ru.promonasos.service.UplotneniyaService;
import ru.promonasos.service.ZayavkaEmailService;

import java.net.URI;

// Главная страница
@Controller
public class GlavnayaController {

    private static final Logger log = LoggerFactory.getLogger(GlavnayaController.class);

    private final NasosyService nasosyService;
    private final UplotneniyaService uplotneniyaService;
    private final BlogService blogService;
    private final SertifikatyService sertifikatyService;
    private final ZayavkaEmailService zayavkaEmailService;

    public GlavnayaController(NasosyService nasosyService, UplotneniyaService uplotneniyaService,
                               BlogService blogService, SertifikatyService sertifikatyService,
                               ZayavkaEmailService zayavkaEmailService) {
        this.nasosyService = nasosyService;
        this.uplotneniyaService = uplotneniyaService;
        this.blogService = blogService;
        this.sertifikatyService = sertifikatyService;
        this.zayavkaEmailService = zayavkaEmailService;
    }

    // Марки насосов
    @GetMapping("/")
    public String glavnaya(Model model) {
        model.addAttribute("marki", nasosyService.getMarkiNasosov());
        model.addAttribute("sredy", uplotneniyaService.getSredyUplotneniy());
        model.addAttribute("stati", blogService.getStati().stream().limit(3).toList());
        model.addAttribute("vsegoModeley", nasosyService.vsegoModeley());

        model.addAttribute("zagolovok", "Промышленные насосы и торцовые уплотнения — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Поставка промышленных насосов К, КМ, Д, ЦНС, Х, АХ, СМ, Ф, ТК, ВК и производство торцовых уплотнений с 2001 года. Подбор по рабочей точке, импортозамещение уплотнений. Москва.");
        model.addAttribute("canonical", "/");
        return "glavnaya/glavnaya";
    }

    // Контакты
    @GetMapping("/kontakty")
    public String kontakty(Model model) {
        model.addAttribute("zagolovok", "Контакты — ТД «Промоборудование», Москва");
        model.addAttribute("opisanie", "Адрес, телефон и реквизиты ТД «Промоборудование»: 109544, Москва, ул. Рабочая, д. 93, стр. 2, офис 236. Телефон +7 (495) 925-05-03.");
        model.addAttribute("canonical", "/kontakty");
        return "kontakty/kontakty";
    }

    // условия использования информационного сайта
    @GetMapping("/polzovatelskoe-soglashenie")
    public String polzovatelskoeSoglashenie(Model model) {
        model.addAttribute("zagolovok", "Пользовательское соглашение — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Условия использования каталога насосов и торцовых уплотнений ТД «Промоборудование».");
        model.addAttribute("canonical", "/polzovatelskoe-soglashenie");
        return "polzovatelskoe-soglashenie/soglashenie";
    }

    @GetMapping("/politika-personalnyh-dannyh")
    public String politikaPersonalnyhDannykh(Model model) {
        model.addAttribute("zagolovok", "Политика обработки персональных данных — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Политика обработки персональных данных ООО ТД «Промоборудование» при использовании сайта и отправке заявки.");
        model.addAttribute("canonical", "/politika-personalnyh-dannyh");
        return "personalnye-dannye/politika";
    }

    @GetMapping("/soglasie-na-obrabotku-personalnyh-dannyh")
    public String soglasieNaObrabotkuPersonalnyhDannykh(Model model) {
        model.addAttribute("zagolovok", "Согласие на обработку персональных данных — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Согласие на обработку данных, отправляемых через форму заявки на сайте hermetica.su.");
        model.addAttribute("canonical", "/soglasie-na-obrabotku-personalnyh-dannyh");
        model.addAttribute("noindex", true);
        return "personalnye-dannye/soglasie";
    }

    // Сертификаты и свидетельства
    @GetMapping("/sertifikaty")
    public String sertifikaty(Model model) {
        model.addAttribute("dilerskie", sertifikatyService.getDilerskie());
        model.addAttribute("sootvetstviya", sertifikatyService.getSootvetstviya());

        model.addAttribute("zagolovok", "Свидетельства официального дилера — ТД «Промоборудование»");
        model.addAttribute("opisanie", "Лицензии, разрешения и сертификаты официального дилера ТД «Промоборудование» на поставку промышленных насосов ведущих производителей.");
        model.addAttribute("canonical", "/sertifikaty");
        return "sertifikaty/sertifikaty";
    }

    // Принимает заявку и отправляет её на корпоративную почту.
    @PostMapping("/zayavka")
    public String zayavka(@Valid Zayavka zayavka, BindingResult bindingResult,
                           HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Referer приходит абсолютным адресом (https://хост/nasosy/k)
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
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("oshibka", "Проверьте заполнение формы: не хватает обязательных полей.");
            return "redirect:" + vernutsya;
        }

        try {
            zayavkaEmailService.otpravit(zayavka, request.getRequestURI());
            redirectAttributes.addFlashAttribute("uspekh", "Заявка отправлена. Мы свяжемся с вами по указанным контактам.");
        } catch (MailException e) {
            log.warn("Не удалось отправить заявку: ошибка почтового сервера ({})", e.getClass().getSimpleName());
            redirectAttributes.addFlashAttribute("oshibka", "Не удалось отправить заявку. Позвоните нам по телефону +7 (495) 925-05-03 или напишите на info@hermetica.su.");
        }
        return "redirect:" + vernutsya;
    }

    // отдаём кодом, а не файлом — домен не хардкодить в двух местах
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

    // собираем из каталога — новая марка попадёт сюда сама
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
        dobavitUrl(sb, domen, "/blog", "0.7", "weekly");
        dobavitUrl(sb, domen, "/sertifikaty", "0.5", "yearly");
        dobavitUrl(sb, domen, "/kontakty", "0.6", "yearly");
        dobavitUrl(sb, domen, "/polzovatelskoe-soglashenie", "0.3", "yearly");
        dobavitUrl(sb, domen, "/politika-personalnyh-dannyh", "0.4", "yearly");

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
