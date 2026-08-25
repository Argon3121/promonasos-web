package ru.promonasos.model;

/** Параметры подбора торцового уплотнения. */
public class ZaprosPodboraUplotneniya {
    private final String sreda;
    private final Double diametrVala;
    private final Double davlenie;
    private final Double temperatura;

    public ZaprosPodboraUplotneniya(String sreda, Double diametrVala, Double davlenie, Double temperatura) {
        this.sreda = sreda;
        this.diametrVala = diametrVala;
        this.davlenie = davlenie;
        this.temperatura = temperatura;
    }

    public String getSreda() { return sreda; }
    public Double getDiametrVala() { return diametrVala; }
    public Double getDavlenie() { return davlenie; }
    public Double getTemperatura() { return temperatura; }
}
