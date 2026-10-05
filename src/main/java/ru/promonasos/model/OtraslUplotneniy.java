package ru.promonasos.model;

import java.util.List;

public record OtraslUplotneniy(
        String slug,
        String nazvanie,
        String seoTitle,
        String seoDescription,
        List<String> slugiUplotneniy
) {
}
