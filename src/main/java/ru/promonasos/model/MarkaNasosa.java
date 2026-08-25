package ru.promonasos.model;

import java.util.List;

/**
 * Марка (серия) насоса — основная посадочная страница каталога:
 * пользователь чаще всего приходит из поиска, уже зная марку.
 */
public class MarkaNasosa {
    private final String oboznachenie;
    private final String slug;
    private final String nazvanie;
    private final String rasshifrovka;
    private final String kratkoeOpisanie;
    private final List<String> opisanie;
    private final String gruppaSlug;
    private final List<String> sredy;
    private final List<String> primenenie;
    private final List<String> preimushchestva;
    private final List<String> sovmestimyeUplotneniya;
    private final String seoTitle;
    private final String seoDescription;
    // Не final: по умолчанию марка показывает общее фото группы (gruppa.izobrazhenie),
    // это поле — точечное исключение для марок, у которых нашлось второе настоящее
    // фото на Wikimedia Commons, чтобы внутри одной группы не все карточки были одинаковые.
    private String izobrazhenieOverride;

    public MarkaNasosa(String oboznachenie, String slug, String nazvanie, String rasshifrovka,
                        String kratkoeOpisanie, List<String> opisanie, String gruppaSlug,
                        List<String> sredy, List<String> primenenie, List<String> preimushchestva,
                        List<String> sovmestimyeUplotneniya, String seoTitle, String seoDescription) {
        this.oboznachenie = oboznachenie;
        this.slug = slug;
        this.nazvanie = nazvanie;
        this.rasshifrovka = rasshifrovka;
        this.kratkoeOpisanie = kratkoeOpisanie;
        this.opisanie = opisanie;
        this.gruppaSlug = gruppaSlug;
        this.sredy = sredy;
        this.primenenie = primenenie;
        this.preimushchestva = preimushchestva;
        this.sovmestimyeUplotneniya = sovmestimyeUplotneniya;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
    }

    public String getOboznachenie() { return oboznachenie; }
    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getRasshifrovka() { return rasshifrovka; }
    public String getKratkoeOpisanie() { return kratkoeOpisanie; }
    public List<String> getOpisanie() { return opisanie; }
    public String getGruppaSlug() { return gruppaSlug; }
    public List<String> getSredy() { return sredy; }
    public List<String> getPrimenenie() { return primenenie; }
    public List<String> getPreimushchestva() { return preimushchestva; }
    public List<String> getSovmestimyeUplotneniya() { return sovmestimyeUplotneniya; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
    public String getIzobrazhenieOverride() { return izobrazhenieOverride; }
    public void setIzobrazhenieOverride(String izobrazhenieOverride) { this.izobrazhenieOverride = izobrazhenieOverride; }
}
