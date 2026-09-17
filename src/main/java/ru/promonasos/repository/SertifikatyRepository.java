package ru.promonasos.repository;

import org.springframework.stereotype.Repository;
import ru.promonasos.model.Sertifikat;

import java.util.List;

// сканы документов: свидетельства дилера + сертификаты ТР ТС
@Repository
public class SertifikatyRepository {

    private static final String KAT_DILER = "diler";
    private static final String KAT_SOOTVETSTVIE = "sootvetstvie";

    private final List<Sertifikat> sertifikaty = List.of(
            // --- Свидетельства дилера и партнёрства ---
            diler("Официальный дилер — Адекта", "adekta"),
            diler("Официальный дилер — КНЗ", "knz"),
            diler("Официальный партнёр — Энергия", "energiya"),
            diler("Официальный дилер — СМЗ (Средневолжский машиностроительный завод)", "smz"),
            diler("Официальный дилер — ТМЗ (Талнахский механический завод)", "tmz"),
            diler("Официальный авторизованный агент — Норнат", "nornat"),
            diler("Официальный представитель — УралГидроПром", "uralgidroprom"),
            diler("Официальный дилер — Солтек Новомосковский механический завод", "soltek"),
            diler("Сертификат дилера — Тулагидромаш", "tulagidromash"),
            diler("Официальный дилер — Центрнасоссервис", "centrnasosservis"),
            diler("Официальный дилер — Насосэлектропром", "nasoselektroprom"),
            diler("Официальный дилер — НХН (Невьянские химические насосы)", "nhn"),
            diler("Официальный представитель — ПромЭнергомаш-ЕК (ПЭМ)", "promenergomash-ek"),
            diler("Официальный дилер — ОЕМ", "oem"),
            diler("Свидетельство дилера — РММС", "rmms-dealer"),
            diler("Официальный дилер — Альфа", "alfa-dealer"),
            diler("Официальный представитель — Пинский ОМЗ", "pinskiy-omz"),
            diler("Сертификат дилера — Свердмаш", "sverdmash"),
            new Sertifikat("Опыт поставок 2020–2025", "Опыт поставок", KAT_DILER,
                    "/images/sertifikaty/opyt-postavok.jpg", "/documents/sertifikaty/opyt-postavok.pdf"),

            // --- Декларации и сертификаты соответствия ТР ТС ---
            sootvetstvie("Декларация соответствия — насосы ЦНС (Пинский ОМЗ)", "deklaratsiya-tsns-pinsk"),
            sootvetstvie("Декларация ТР ТС 010 — насосы Х, ХМ, АХ и другие (Альфа)", "deklaratsiya-alfa-2021"),
            sootvetstvie("Декларация соответствия — насосы 5Д, СМ, СД, СДВ, КМ, ЛМ, КМЛ", "deklaratsiya-5d-sm"),
            sootvetstvie("Декларация соответствия — КНЗ", "deklaratsiya-knz"),
            sootvetstvie("Декларация соответствия — Энергия", "deklaratsiya-energiya"),
            sootvetstvie("Декларация ЕАЭС — насосы ОВ, ВО, ОПВ, ДПВ", "deklaratsiya-eaes-ov"),
            sootvetstvie("Декларация ТР ТС 010 — МК Энерго", "mk-energo-010"),
            sootvetstvie("Сертификат ТР ТС 012 — МК Энерго", "mk-energo-012"),
            sootvetstvie("Декларация ТР ТС 010 — РММС", "rmms-010"),
            sootvetstvie("Сертификат ТР ТС 012 — РММС", "rmms-012"),
            sootvetstvie("Сертификат — пищевое исполнение", "pishchevye"),
            sootvetstvie("Сертификат ТР ТС — насосы НД, НДГ", "nd-ndg"),
            sootvetstvie("Сертификат ТР ТС 004/010/020 (2025)", "trts-004-010-020"),
            new Sertifikat("Сертификат ТР ТС 012", "Соответствие", KAT_SOOTVETSTVIE,
                    "/images/sertifikaty/trts012.jpg", "/images/sertifikaty/trts012.jpg"),
            new Sertifikat("Сертификат ТР ТС 010", "Соответствие", KAT_SOOTVETSTVIE,
                    "/images/sertifikaty/trts010.jpg", "/images/sertifikaty/trts010.jpg")
    );

    private static Sertifikat diler(String nazvanie, String slug) {
        return new Sertifikat(nazvanie, "Дилер", KAT_DILER,
                "/images/sertifikaty/" + slug + ".jpg", "/documents/sertifikaty/" + slug + ".pdf");
    }

    private static Sertifikat sootvetstvie(String nazvanie, String slug) {
        return new Sertifikat(nazvanie, "Соответствие", KAT_SOOTVETSTVIE,
                "/images/sertifikaty/" + slug + ".jpg", "/documents/sertifikaty/" + slug + ".pdf");
    }

    public List<Sertifikat> getDilerskie() {
        return sertifikaty.stream().filter(s -> s.getKategoriya().equals(KAT_DILER)).toList();
    }

    public List<Sertifikat> getSootvetstviya() {
        return sertifikaty.stream().filter(s -> s.getKategoriya().equals(KAT_SOOTVETSTVIE)).toList();
    }
}
