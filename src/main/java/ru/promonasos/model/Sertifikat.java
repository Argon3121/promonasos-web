package ru.promonasos.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
// скан документа; у двух штук PDF не было, только картинка — там fayl = miniatyura
public class Sertifikat {
    private final String nazvanie;
    private final String badge;
    private final String kategoriya;
    private final String miniatyura;
    private final String fayl;


    public String getNazvanie() { return nazvanie; }
    public String getBadge() { return badge; }
    public String getKategoriya() { return kategoriya; }
    public String getMiniatyura() { return miniatyura; }
    public String getFayl() { return fayl; }
}
