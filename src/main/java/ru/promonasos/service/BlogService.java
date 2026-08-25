package ru.promonasos.service;

import org.springframework.stereotype.Service;
import ru.promonasos.model.StatyaBloga;
import ru.promonasos.repository.BlogRepository;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository repository;

    public BlogService(BlogRepository repository) {
        this.repository = repository;
    }

    public List<StatyaBloga> getStati() {
        return repository.getStati();
    }

    public List<StatyaBloga> getStatiPoRubrike(String rubrika) {
        return repository.getStatiPoRubrike(rubrika);
    }

    public StatyaBloga getStatyu(String slug) {
        return repository.getStatyu(slug);
    }

    public List<StatyaBloga> getStatiPoMarke(String markaSlug) {
        return repository.getStatiPoMarke(markaSlug);
    }

    public List<String> getRubriki() {
        return repository.getRubriki();
    }
}
