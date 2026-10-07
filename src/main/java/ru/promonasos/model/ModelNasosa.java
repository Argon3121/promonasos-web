package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
// типоразмер внутри марки, например К 20/30 — подача и напор тут главное
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

    private String izobrazhenieOverride;

    // параметры в виде «название → значение» для моделей, у которых на оригинальном сайте нет подачи/напора/мощности (например BT)
    private Map<String, String> dopParametry = Map.of();

    // дополнительные строки к обычной таблице характеристик (например, давление у насосов ПТ)
    private Map<String, String> dopStroki = Map.of();


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
    public Map<String, String> getDopStroki() { return dopStroki; }
    public void setDopStroki(Map<String, String> dopStroki) { this.dopStroki = dopStroki; }
    public Map<String, String> getDopParametry() { return dopParametry; }
    public void setDopParametry(Map<String, String> dopParametry) { this.dopParametry = dopParametry; }
    public String getIzobrazhenieOverride() { return izobrazhenieOverride; }
    public void setIzobrazhenieOverride(String izobrazhenieOverride) { this.izobrazhenieOverride = izobrazhenieOverride; }
}
