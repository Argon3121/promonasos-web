package ru.promonasos.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

/**
 * Заявка с сайта. В отличие от остальных моделей — изменяемый JavaBean:
 * Spring биндит в него поля формы по именам сеттеров.
 */
public class Zayavka {

    @NotBlank(message = "Укажите, как к вам обращаться")
    private String imya;

    @NotBlank(message = "Нужен телефон или почта для ответа")
    private String kontakt;

    private String organizatsiya;
    private String zadacha;

    @AssertTrue(message = "Без согласия мы не можем принять заявку")
    private boolean soglasie;

    public String getImya() { return imya; }
    public void setImya(String imya) { this.imya = imya; }

    public String getKontakt() { return kontakt; }
    public void setKontakt(String kontakt) { this.kontakt = kontakt; }

    public String getOrganizatsiya() { return organizatsiya; }
    public void setOrganizatsiya(String organizatsiya) { this.organizatsiya = organizatsiya; }

    public String getZadacha() { return zadacha; }
    public void setZadacha(String zadacha) { this.zadacha = zadacha; }

    public boolean isSoglasie() { return soglasie; }
    public void setSoglasie(boolean soglasie) { this.soglasie = soglasie; }
}
