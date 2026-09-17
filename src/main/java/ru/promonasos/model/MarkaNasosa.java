package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
// марка насоса — сюда обычно и заходят из поиска, уже зная марку
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
    private final String seoTitle;
    private final String seoDescription;
    // Не final: по умолчанию марка показывает общее фото группы (gruppa.izobrazhenie),
    // это поле — точечное исключение для марок, у которых нашлось второе настоящее
    private String izobrazhenieOverride;
    // КПД и буквенные исполнения проставляются постобработкой только там, где эти
    // данные есть на hermetica.su (см. NasosyRepository.primenitDannyeIspolneniy).
    private String kpd;
    private List<Ispolnenie> ispolneniya = List.of();

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
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
    public String getIzobrazhenieOverride() { return izobrazhenieOverride; }
    public void setIzobrazhenieOverride(String izobrazhenieOverride) { this.izobrazhenieOverride = izobrazhenieOverride; }
    public String getKpd() { return kpd; }
    public void setKpd(String kpd) { this.kpd = kpd; }
    public List<Ispolnenie> getIspolneniya() { return ispolneniya; }
    public void setIspolneniya(List<Ispolnenie> ispolneniya) { this.ispolneniya = ispolneniya; }

    // буквенное/типовое исполнение серии: код («К», «ЦМГ-Х», «0») и что оно значит
    public record Ispolnenie(String kod, String opisanie) {}
}
