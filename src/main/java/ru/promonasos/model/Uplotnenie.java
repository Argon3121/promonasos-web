package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
// торцовое уплотнение своего производства
public class Uplotnenie {
    private final String oboznachenie;
    private final String slug;
    private final String nazvanie;
    private final String tip;
    private final String opisanie;
    private final List<String> sredyKody;
    private final double diametrValaOt;
    private final double diametrValaDo;
    private final double davlenieDo;
    private final double temperaturaOt;
    private final double temperaturaDo;
    private final String vtorichnoeUplotnenie;
    private final List<String> paryTreniya;
    private final String metallicheskieDetali;
    private final String standarty;
    private final List<String> primenyaetsyaNaNasosakh;
    private final String seoTitle;
    private final String seoDescription;

    private String izobrazhenieOverride;


    public String getOboznachenie() { return oboznachenie; }
    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getTip() { return tip; }
    public String getOpisanie() { return opisanie; }
    public List<String> getSredyKody() { return sredyKody; }
    public double getDiametrValaOt() { return diametrValaOt; }
    public double getDiametrValaDo() { return diametrValaDo; }
    public double getDavlenieDo() { return davlenieDo; }
    // Для серий, объединяющих несколько исполнений, показываем пределы каждого исполнения.
    public String getDavlenieOpisanie() {
        return switch (slug) {
            case "251-211-311" -> "до 16 (251, 211); до 40 (311)";
            case "153-353-313" -> "до 8 (153); до 16 (353, 313)";
            case "153-d" -> "до 8";
            case "211c-361c" -> "до 20 (211.C); до 40 (361.C)";
            case "264-365" -> "до 15 (264); до 85 (365)";
            default -> null;
        };
    }
    public boolean podkhoditPoDavleniyu(double davlenie) {
        double limit = switch (slug) {
            case "251-211-311", "153-353-313" -> davlenie <= 16 ? 16 : (slug.equals("251-211-311") ? 40 : 16);
            case "211c-361c" -> davlenie <= 20 ? 20 : 40;
            case "264-365" -> davlenie <= 15 ? 15 : 85;
            default -> davlenieDo;
        };
        return limit >= davlenie;
    }
    public double getTemperaturaOt() { return temperaturaOt; }
    public double getTemperaturaDo() { return temperaturaDo; }
    public String getTemperaturaOpisanie() {
        return switch (slug) {
            case "112-212-n" -> "−20…+120 (112); до +160 (212.N)";
            case "351n-361n", "351t-n" -> "−30…+150 (до +200 по исполнению)";
            default -> null;
        };
    }
    public String getVtorichnoeUplotnenie() { return vtorichnoeUplotnenie; }
    public List<String> getParyTreniya() { return paryTreniya; }
    public String getMetallicheskieDetali() { return metallicheskieDetali; }
    public String getStandarty() { return standarty; }
    public List<String> getPrimenyaetsyaNaNasosakh() { return primenyaetsyaNaNasosakh; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
    public String getIzobrazhenieOverride() { return izobrazhenieOverride; }
    public void setIzobrazhenieOverride(String izobrazhenieOverride) { this.izobrazhenieOverride = izobrazhenieOverride; }
}
