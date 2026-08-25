package ru.promonasos.model;

/**
 * Конкретный типоразмер насоса внутри марки: К 20/30, ЦНСг 60-132.
 * Рабочая точка (подача + напор) — главный параметр подбора.
 */
public class ModelNasosa {
    private final String oboznachenie;
    private final String slug;
    private final String markaSlug;
    private final double podacha;
    private final double napor;
    private final double moshchnost;
    private final int oboroty;
    private final double diametrVala;
    private final double maxTemperatura;
    private final String materialProtochnoyChasti;
    private final String nalichie;
    private final String gabarity;
    private final String seoTitle;
    private final String seoDescription;

    public ModelNasosa(String oboznachenie, String slug, String markaSlug, double podacha, double napor,
                        double moshchnost, int oboroty, double diametrVala, double maxTemperatura,
                        String materialProtochnoyChasti, String nalichie, String gabarity,
                        String seoTitle, String seoDescription) {
        this.oboznachenie = oboznachenie;
        this.slug = slug;
        this.markaSlug = markaSlug;
        this.podacha = podacha;
        this.napor = napor;
        this.moshchnost = moshchnost;
        this.oboroty = oboroty;
        this.diametrVala = diametrVala;
        this.maxTemperatura = maxTemperatura;
        this.materialProtochnoyChasti = materialProtochnoyChasti;
        this.nalichie = nalichie;
        this.gabarity = gabarity;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

    public String getOboznachenie() { return oboznachenie; }
    public String getSlug() { return slug; }
    public String getMarkaSlug() { return markaSlug; }
    public double getPodacha() { return podacha; }
    public double getNapor() { return napor; }
    public double getMoshchnost() { return moshchnost; }
    public int getOboroty() { return oboroty; }
    public double getDiametrVala() { return diametrVala; }
    public double getMaxTemperatura() { return maxTemperatura; }
    public String getMaterialProtochnoyChasti() { return materialProtochnoyChasti; }
    public String getNalichie() { return nalichie; }
    public String getGabarity() { return gabarity; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
}
