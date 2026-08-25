package ru.promonasos.model;

/**
 * Группа каталога по назначению: центробежные общепромышленные, химические,
 * питательные и сетевые и т. д. Нужна, чтобы 45 марок не висели плоским списком.
 */
public class GruppaNasosov {
    private final String slug;
    private final String nazvanie;
    private final String opisanie;
    private final String izobrazhenie;

    public GruppaNasosov(String slug, String nazvanie, String opisanie, String izobrazhenie) {
        this.slug = slug;
        this.nazvanie = nazvanie;
        this.opisanie = opisanie;
        this.izobrazhenie = izobrazhenie;
    }

    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getOpisanie() { return opisanie; }
    public String getIzobrazhenie() { return izobrazhenie; }
}
