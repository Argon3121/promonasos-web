package ru.promonasos.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Статья блога (базы знаний). Блог решает две задачи:
 * собирает информационный трафик и снимает возражения перед заявкой.
 */
public class StatyaBloga {
    private final String slug;
    private final String zagolovok;
    private final String anons;
    private final List<String> tekst;
    private final String rubrika;
    private final LocalDate data;
    private final int vremyaChteniya;
    private final List<String> svyazannyeMarki;
    private final List<String> svyazannyeSredy;
    private final String seoTitle;
    private final String seoDescription;

    public StatyaBloga(String slug, String zagolovok, String anons, List<String> tekst, String rubrika,
                        LocalDate data, int vremyaChteniya, List<String> svyazannyeMarki,
                        List<String> svyazannyeSredy, String seoTitle, String seoDescription) {
        this.slug = slug;
        this.zagolovok = zagolovok;
        this.anons = anons;
        this.tekst = tekst;
        this.rubrika = rubrika;
        this.data = data;
        this.vremyaChteniya = vremyaChteniya;
        this.svyazannyeMarki = svyazannyeMarki;
        this.svyazannyeSredy = svyazannyeSredy;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

    public String getSlug() { return slug; }
    public String getZagolovok() { return zagolovok; }
    public String getAnons() { return anons; }
    public List<String> getTekst() { return tekst; }
    public String getRubrika() { return rubrika; }
    public LocalDate getData() { return data; }
    public int getVremyaChteniya() { return vremyaChteniya; }
    public List<String> getSvyazannyeMarki() { return svyazannyeMarki; }
    public List<String> getSvyazannyeSredy() { return svyazannyeSredy; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
}
