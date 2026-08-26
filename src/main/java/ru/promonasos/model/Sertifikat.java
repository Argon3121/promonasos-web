package ru.promonasos.model;

/**
 * Сертификат, декларация соответствия или свидетельство дилера — скан
 * реального документа. {@code fayl} — полный PDF для просмотра/скачивания;
 * для двух документов, которые изначально были просто картинкой без PDF,
 * {@code fayl} совпадает с {@code miniatyura}.
 */
public class Sertifikat {
    private final String nazvanie;
    private final String badge;
    private final String kategoriya;
    private final String miniatyura;
    private final String fayl;

    public Sertifikat(String nazvanie, String badge, String kategoriya, String miniatyura, String fayl) {
        this.nazvanie = nazvanie;
        this.badge = badge;
        this.kategoriya = kategoriya;
        this.miniatyura = miniatyura;
        this.fayl = fayl;
    }

    public String getNazvanie() { return nazvanie; }
    public String getBadge() { return badge; }
    public String getKategoriya() { return kategoriya; }
    public String getMiniatyura() { return miniatyura; }
    public String getFayl() { return fayl; }
}
