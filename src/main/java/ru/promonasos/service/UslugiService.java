package ru.promonasos.service;

import org.springframework.stereotype.Service;
import ru.promonasos.model.Usluga;
import ru.promonasos.repository.UslugiRepository;

import java.util.List;

@Service
public class UslugiService {

    private final UslugiRepository repository;

    public UslugiService(UslugiRepository repository) {
        this.repository = repository;
    }

    public List<Usluga> getUslugi() {
        return repository.getUslugi();
    }

    public Usluga getUslugu(String slug) {
        return repository.getUslugu(slug);
    }
}
