package ru.promonasos.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// заявка с формы
public class Zayavka {

    @NotBlank(message = "Укажите, как к вам обращаться")
    @Size(max = 120, message = "Имя должно быть короче 120 символов")
    private String imya;

    @NotBlank(message = "Нужен телефон или почта для ответа")
    @Size(max = 254, message = "Контакт должен быть короче 254 символов")
    private String kontakt;

    @Size(max = 200, message = "Название организации должно быть короче 200 символов")
    private String organizatsiya;

    @Size(max = 3000, message = "Описание запроса должно быть короче 3000 символов")
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
