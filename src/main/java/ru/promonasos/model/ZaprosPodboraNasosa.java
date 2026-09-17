package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// параметры формы подбора насоса
public class ZaprosPodboraNasosa {
    private final String sreda;
    private final Double podacha;
    private final Double napor;


    public String getSreda() { return sreda; }
    public Double getPodacha() { return podacha; }
    public Double getNapor() { return napor; }
}
