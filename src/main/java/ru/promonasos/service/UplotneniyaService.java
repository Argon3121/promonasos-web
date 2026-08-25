package ru.promonasos.service;

import org.springframework.stereotype.Service;
import ru.promonasos.model.SredaUplotneniya;
import ru.promonasos.model.Uplotnenie;
import ru.promonasos.model.ZaprosPodboraUplotneniya;
import ru.promonasos.repository.UplotneniyaRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class UplotneniyaService {

    private final UplotneniyaRepository repository;

    public UplotneniyaService(UplotneniyaRepository repository) {
        this.repository = repository;
    }

    public List<SredaUplotneniya> getSredyUplotneniy() {
        return repository.getSredyUplotneniy();
    }

    public SredaUplotneniya getSredu(String slug) {
        return repository.getSredu(slug);
    }

    public List<Uplotnenie> getUplotneniya() {
        return repository.getUplotneniya();
    }

    public Uplotnenie getUplotnenie(String slug) {
        return repository.getUplotnenie(slug);
    }

    public List<Uplotnenie> getUplotneniyaPoSrede(String sredaKod) {
        return repository.getUplotneniyaPoSrede(sredaKod);
    }

    public List<Uplotnenie> getUplotneniyaPoMarke(String markaOboznachenie) {
        return repository.getUplotneniyaPoMarke(markaOboznachenie);
    }

    /**
     * Подбор по среде, валу, давлению и температуре. В отличие от насоса, здесь
     * параметры дискретные (диапазон вала, предельные давление и температура),
     * поэтому фильтрация строгая: если изделие не покрывает условие — оно не
     * показывается.
     */
    public List<Uplotnenie> podobratUplotnenie(ZaprosPodboraUplotneniya zapros) {
        if (zapros == null) {
            return repository.getUplotneniya();
        }

        List<Uplotnenie> vyborka = repository.getUplotneniya();

        if (zapros.getSreda() != null && !zapros.getSreda().isEmpty()) {
            vyborka = vyborka.stream().filter(u -> u.getSredyKody().contains(zapros.getSreda())).toList();
        }

        if (zapros.getDiametrVala() != null && zapros.getDiametrVala() > 0) {
            double d = zapros.getDiametrVala();
            vyborka = vyborka.stream()
                    .filter(u -> d >= u.getDiametrValaOt() && d <= u.getDiametrValaDo())
                    .toList();
        }

        if (zapros.getDavlenie() != null && zapros.getDavlenie() > 0) {
            vyborka = vyborka.stream().filter(u -> u.getDavlenieDo() >= zapros.getDavlenie()).toList();
        }

        if (zapros.getTemperatura() != null) {
            double t = zapros.getTemperatura();
            vyborka = vyborka.stream()
                    .filter(u -> t >= u.getTemperaturaOt() && t <= u.getTemperaturaDo())
                    .toList();
        }

        return vyborka.stream().sorted(Comparator.comparing(Uplotnenie::getOboznachenie)).toList();
    }
}
