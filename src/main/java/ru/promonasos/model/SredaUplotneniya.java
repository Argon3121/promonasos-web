package ru.promonasos.model;

import java.util.List;

/**
 * Перекачиваемая среда — точка входа в каталог торцовых уплотнений.
 * Уплотнение подбирается именно по среде и её параметрам, а не по марке насоса.
 */
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

    public SredaUplotneniya(String kod, String slug, String nazvanie, String opisanie, String agressivnost,
                             String abrazivnost, String diapazonTemperatur, String diapazonDavleniy,
                             List<String> otrasli, List<String> chastyeOshibki,
                             String seoTitle, String seoDescription) {
        this.kod = kod;
        this.slug = slug;
        this.nazvanie = nazvanie;
        this.opisanie = opisanie;
        this.agressivnost = agressivnost;
        this.abrazivnost = abrazivnost;
        this.diapazonTemperatur = diapazonTemperatur;
        this.diapazonDavleniy = diapazonDavleniy;
        this.otrasli = otrasli;
        this.chastyeOshibki = chastyeOshibki;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

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
