package ru.promonasos.service;

import org.springframework.stereotype.Service;
import ru.promonasos.model.Sertifikat;
import ru.promonasos.repository.SertifikatyRepository;

import java.util.List;

@Service
public class SertifikatyService {

    private final SertifikatyRepository repository;

    public SertifikatyService(SertifikatyRepository repository) {
        this.repository = repository;
    }

    public List<Sertifikat> getDilerskie() {
        return repository.getDilerskie();
    }

    public List<Sertifikat> getSootvetstviya() {
        return repository.getSootvetstviya();
    }
}
