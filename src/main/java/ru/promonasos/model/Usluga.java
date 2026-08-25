package ru.promonasos.model;

import java.util.List;

/**
 * Инжиниринговая услуга: подбор, разработка, шефмонтаж, обучение, ремонт.
 * Услуги — второй по важности источник доверия после каталога.
 */
public class Usluga {
    private final String slug;
    private final String nazvanie;
    private final String kratkoeOpisanie;
    private final List<String> opisanie;
    private final List<String> rezultat;
    private final List<String> etapy;
    private final String ikonka;
    private final String seoTitle;
    private final String seoDescription;

    public Usluga(String slug, String nazvanie, String kratkoeOpisanie, List<String> opisanie,
                  List<String> rezultat, List<String> etapy, String ikonka,
                  String seoTitle, String seoDescription) {
        this.slug = slug;
        this.nazvanie = nazvanie;
        this.kratkoeOpisanie = kratkoeOpisanie;
        this.opisanie = opisanie;
        this.rezultat = rezultat;
        this.etapy = etapy;
        this.ikonka = ikonka;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getKratkoeOpisanie() { return kratkoeOpisanie; }
    public List<String> getOpisanie() { return opisanie; }
    public List<String> getRezultat() { return rezultat; }
    public List<String> getEtapy() { return etapy; }
    public String getIkonka() { return ikonka; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
}
