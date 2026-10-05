package ru.promonasos.model;

// Категория каталога; это не допуск изделия к работе с конкретной жидкостью.
public record SredaUplotneniya(
        String kod,
        String slug,
        String nazvanie,
        String opisanie,
        String seoTitle,
        String seoDescription
) {
    // Bean-style accessors are used by existing controllers, templates and SEO helpers.
    public String getKod() { return kod; }
    public String getSlug() { return slug; }
    public String getNazvanie() { return nazvanie; }
    public String getOpisanie() { return opisanie; }
    public String getSeoTitle() { return seoTitle; }
    public String getSeoDescription() { return seoDescription; }
}
