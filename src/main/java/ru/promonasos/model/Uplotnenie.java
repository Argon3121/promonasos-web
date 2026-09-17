package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
// торцовое уплотнение своего производства, данные с hermetica.su
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
    // не final: по умолчанию изделие показывает общее фото уплотнения (uplotnenie.jpg),
    // сюда позже проставится реальное фото конкретного изделия, когда оно появится
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
    public double getTemperaturaOt() { return temperaturaOt; }
    public double getTemperaturaDo() { return temperaturaDo; }
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
