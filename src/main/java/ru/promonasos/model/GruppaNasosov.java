package ru.promonasos.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor// конструктивная группа — 45 марок
public class GruppaNasosov {
    private final String slug;
    private final String nazvanie;
    private final String opisanie;

    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getOpisanie() { return opisanie; }
}
