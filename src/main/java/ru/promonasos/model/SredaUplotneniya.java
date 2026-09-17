package ru.promonasos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
// среда — с неё начинается подбор уплотнения, не с марки насоса
public class SredaUplotneniya {
    private final String kod;
    private final String slug;
    private final String nazvanie;
    private final String opisanie;
    private final String agressivnost;
    private final String abrazivnost;
    private final String diapazonTemperatur;
    private final String diapazonDavleniy;
    private final List<String> otrasli;
    private final List<String> chastyeOshibki;
    private final String seoTitle;
    private final String seoDescription;



    public String getKod() { return kod; }
    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getOpisanie() { return opisanie; }
    public String getAgressivnost() { return agressivnost; }
    public String getAbrazivnost() { return abrazivnost; }
    public String getDiapazonTemperatur() { return diapazonTemperatur; }
    public String getDiapazonDavleniy() { return diapazonDavleniy; }
    public List<String> getOtrasli() { return otrasli; }
    public List<String> getChastyeOshibki() { return chastyeOshibki; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
}
