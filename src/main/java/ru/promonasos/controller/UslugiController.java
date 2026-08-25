package ru.promonasos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.promonasos.service.UslugiService;

/** Инжиниринговые услуги: подбор, импортозамещение, шефмонтаж, ремонт. */
@Controller
public class UslugiController {

    private final UslugiService uslugiService;

    public UslugiController(UslugiService uslugiService) {
        this.uslugiService = uslugiService;
    }

    /** Все услуги одной страницей с якорями на каждую. /uslugi */
    @GetMapping("/uslugi")
    public String spisok(Model model) {
        model.addAttribute("uslugi", uslugiService.getUslugi());

        model.addAttribute("zagolovok", "Инжиниринг и сервис: подбор, импортозамещение, шефмонтаж | ТД «Промоборудование»");
        model.addAttribute("opisanie", "Услуги ТД «Промоборудование»: инженерный подбор насоса и уплотнения, импортозамещение уплотнений John Crane и Burgmann, разработка под условия, шефмонтаж, ремонт узлов.");
        model.addAttribute("canonical", "/uslugi");
        return "uslugi/spisok";
    }
}
