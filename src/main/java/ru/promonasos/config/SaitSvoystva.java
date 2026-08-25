package ru.promonasos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Реквизиты и контакты сайта из application.yml (секция "sait").
 * Аналог секции "Sait" в appsettings.json оригинала.
 */
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
