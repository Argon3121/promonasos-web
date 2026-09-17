package ru.promonasos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// реквизиты сайта из application.yml, секция sait
@ConfigurationProperties(prefix = "sait")
public record SaitSvoystva(
        String nazvanie,
        String domen,
        String telefon,
        String telefonSsylka,
        String pochta,
        String adres,
        String grafikRaboty,
        String godOsnovaniya
) {
}
