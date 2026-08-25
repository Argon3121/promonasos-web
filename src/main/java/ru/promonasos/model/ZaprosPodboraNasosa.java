package ru.promonasos.model;

/** Параметры подбора насоса по рабочей точке. */
public class ZaprosPodboraNasosa {
    private final String sreda;
    private final Double podacha;
    private final Double napor;

    public ZaprosPodboraNasosa(String sreda, Double podacha, Double napor) {
        this.sreda = sreda;
        this.podacha = podacha;
        this.napor = napor;
    }

    public String getSreda() { return sreda; }
    public Double getPodacha() { return podacha; }
    public Double getNapor() { return napor; }
}
