package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
// статья блога — трафик из поиска + снимает возражения перед заявкой
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
