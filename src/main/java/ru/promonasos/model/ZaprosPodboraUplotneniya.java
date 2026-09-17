package ru.promonasos.model;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// параметры формы подбора уплотнения
public class ZaprosPodboraUplotneniya {
    private final String sreda;
    private final Double diametrVala;
    private final Double davlenie;
    private final Double temperatura;


    public String getSreda() { return sreda; }
    public Double getDiametrVala() { return diametrVala; }
    public Double getDavlenie() { return davlenie; }
    public Double getTemperatura() { return temperatura; }
}
