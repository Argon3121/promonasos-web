package ru.promonasos.model;

import java.util.List;

/**
 * Торцовое уплотнение собственного производства.
 * Характеристики взяты со страниц каталога hermetica.su.
 */
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

    public Uplotnenie(String oboznachenie, String slug, String nazvanie, String tip, String opisanie,
                       List<String> sredyKody, double diametrValaOt, double diametrValaDo, double davlenieDo,
                       double temperaturaOt, double temperaturaDo, String vtorichnoeUplotnenie,
                       List<String> paryTreniya, String metallicheskieDetali, String standarty,
                       List<String> primenyaetsyaNaNasosakh, String seoTitle, String seoDescription) {
        this.oboznachenie = oboznachenie;
        this.slug = slug;
        this.nazvanie = nazvanie;
        this.tip = tip;
        this.opisanie = opisanie;
        this.sredyKody = sredyKody;
        this.diametrValaOt = diametrValaOt;
        this.diametrValaDo = diametrValaDo;
        this.davlenieDo = davlenieDo;
        this.temperaturaOt = temperaturaOt;
        this.temperaturaDo = temperaturaDo;
        this.vtorichnoeUplotnenie = vtorichnoeUplotnenie;
        this.paryTreniya = paryTreniya;
        this.metallicheskieDetali = metallicheskieDetali;
        this.standarty = standarty;
        this.primenyaetsyaNaNasosakh = primenyaetsyaNaNasosakh;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

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
}
