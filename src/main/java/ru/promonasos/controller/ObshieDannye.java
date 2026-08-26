package ru.promonasos.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.promonasos.config.SaitSvoystva;
import ru.promonasos.model.GruppaNasosov;
import ru.promonasos.model.MarkaNasosa;
import ru.promonasos.model.SredaUplotneniya;
import ru.promonasos.service.NasosyService;
import ru.promonasos.service.UplotneniyaService;

import java.util.List;
import java.util.Map;

/**
 * Данные, нужные почти на каждой странице: реквизиты сайта и списки для
 * шапки/подвала (мега-меню марок, среды). В оригинале на Razor каждый
 * _Header/_Footer сам делал @inject нужного репозитория — здесь то же самое
 * объявлено один раз для всех контроллеров.
 */
@ControllerAdvice
public class ObshieDannye {

    private final NasosyService nasosyService;
    private final UplotneniyaService uplotneniyaService;
    private final SaitSvoystva sait;

    public ObshieDannye(NasosyService nasosyService, UplotneniyaService uplotneniyaService, SaitSvoystva sait) {
        this.nasosyService = nasosyService;
        this.uplotneniyaService = uplotneniyaService;
        this.sait = sait;
    }

    @ModelAttribute("sait")
    public SaitSvoystva sait() {
        return sait;
    }

    @ModelAttribute("vseMarki")
    public List<MarkaNasosa> vseMarki() {
        return nasosyService.getMarkiNasosov();
    }

    @ModelAttribute("vseGruppy")
    public List<GruppaNasosov> vseGruppy() {
        return nasosyService.getGruppy();
    }

    /** Марки по группам — и мега-меню, и подвал, и главная показывают каталог сгруппированным, а не плоским списком из 45 марок. */
    @ModelAttribute("markiPoGruppam")
    public Map<String, List<MarkaNasosa>> markiPoGruppam() {
        return nasosyService.getMarkiPoGruppam();
    }

    /** Группа по slug — картинка и название группы там, где под рукой только марка (карточки, результаты подбора). */
    @ModelAttribute("gruppyPoSlug")
    public Map<String, GruppaNasosov> gruppyPoSlug() {
        return nasosyService.getGruppyPoSlug();
    }

    @ModelAttribute("vseSredy")
    public List<SredaUplotneniya> vseSredy() {
        return uplotneniyaService.getSredyUplotneniy();
    }

    /**
     * Текущий путь запроса — для подсветки активного пункта меню в шапке.
     * В Thymeleaf 3.1 (Spring Boot 4) объект #request в шаблонах больше не
     * доступен по умолчанию, поэтому путь передаётся обычным атрибутом модели.
     */
    @ModelAttribute("tekushchiyPut")
    public String tekushchiyPut(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
