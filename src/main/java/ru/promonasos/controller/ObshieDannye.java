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

// общее для каждой страницы: реквизиты, меню, подвал — чтобы не тащить это в каждый контроллер
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

    // марки по группам — иначе меню и главная это 45 марок плоским списком
    @ModelAttribute("markiPoGruppam")
    public Map<String, List<MarkaNasosa>> markiPoGruppam() {
        return nasosyService.getMarkiPoGruppam();
    }

    // группа по slug, когда под рукой только марка — карточки, подбор
    @ModelAttribute("gruppyPoSlug")
    public Map<String, GruppaNasosov> gruppyPoSlug() {
        return nasosyService.getGruppyPoSlug();
    }

    @ModelAttribute("vseSredy")
    public List<SredaUplotneniya> vseSredy() {
        return uplotneniyaService.getSredyUplotneniy();
    }

    // текущий путь — подсветить пункт меню; #request в шаблоне больше не доступен, пришлось так
    @ModelAttribute("tekushchiyPut")
    public String tekushchiyPut(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
