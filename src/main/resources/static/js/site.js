(function () {
    'use strict';

    //Мобильное меню
    function initMenu() {
        var burger = document.querySelector('[data-burger]');
        var nav = document.querySelector('.nav');
        if (!burger || !nav) { return; }

        burger.addEventListener('click', function () {
            var otkryto = nav.classList.toggle('is-open');
            burger.setAttribute('aria-expanded', String(otkryto));
        });

        // После перехода по ссылке меню должно закрываться само
        nav.addEventListener('click', function (e) {
            if (e.target.closest('a')) {
                nav.classList.remove('is-open');
                burger.setAttribute('aria-expanded', 'false');
            }
        });
    }

    //Валидация форм в реальном времени
    function initForms() {
        var formy = document.querySelectorAll('form[data-validate]');

        Array.prototype.forEach.call(formy, function (form) {
            var polya = form.querySelectorAll('input[required], textarea[required]');

            function proverit(el) {
                var obertka = el.closest('.field');
                var korrektno = el.checkValidity();
                if (obertka) {
                    obertka.classList.toggle('is-invalid', !korrektno);
                    el.setAttribute('aria-invalid', String(!korrektno));
                }
                return korrektno;
            }

            Array.prototype.forEach.call(polya, function (el) {
                el.addEventListener('blur', function () { proverit(el); });
                el.addEventListener('input', function () {
                    var obertka = el.closest('.field');
                    if (obertka && obertka.classList.contains('is-invalid')) { proverit(el); }
                });
            });

            form.addEventListener('submit', function (e) {
                var vsjoKorrektno = true;
                Array.prototype.forEach.call(polya, function (el) {
                    if (!proverit(el)) { vsjoKorrektno = false; }
                });

                if (!vsjoKorrektno) {
                    e.preventDefault();
                    var pervoe = form.querySelector('.field.is-invalid input, .field.is-invalid textarea');
                    if (pervoe) { pervoe.focus(); }
                }
            });
        });
    }

    //Плавный переход к якорям
    function initYakorya() {
        document.addEventListener('click', function (e) {
            var a = e.target.closest('a[href^="#"]');
            if (!a) { return; }

            var id = a.getAttribute('href').slice(1);
            if (!id) { return; }

            var cel = document.getElementById(id);
            if (!cel) { return; }

            e.preventDefault();
            cel.scrollIntoView({ behavior: 'smooth', block: 'start' });
            cel.setAttribute('tabindex', '-1');
            cel.focus({ preventScroll: true });

            // Первое поле формы заявки получает фокус: это экономит пользователю клик
            var pervoePole = cel.querySelector('input, textarea');
            if (pervoePole) {
                setTimeout(function () { pervoePole.focus({ preventScroll: true }); }, 350);
            }
        });
    }

    // Увеличение фотографий насосов и уплотнений.
    function initImageZoom() {
        // Каталоги и результаты подбора не создают lightbox и не получают
        // обработчики изображений: увеличение включено только на карточках позиций.
        if (!document.querySelector('[data-image-zoom]')) { return; }

        var dialog = document.createElement('div');
        dialog.className = 'photo-lightbox';
        dialog.setAttribute('role', 'dialog');
        dialog.setAttribute('aria-modal', 'true');
        dialog.setAttribute('aria-label', 'Просмотр фотографии');
        dialog.hidden = true;

        var close = document.createElement('button');
        close.type = 'button';
        close.className = 'photo-lightbox__close';
        close.setAttribute('aria-label', 'Закрыть фото');
        close.textContent = '×';

        var image = document.createElement('img');
        image.className = 'photo-lightbox__image';
        var caption = document.createElement('p');
        caption.className = 'photo-lightbox__caption';

        dialog.appendChild(close);
        dialog.appendChild(image);
        dialog.appendChild(caption);
        document.body.appendChild(dialog);

        var lastTrigger = null;
        var previousBodyOverflow = '';
        function showPhoto(trigger) {
            var sourceImage = trigger.matches('img') ? trigger : trigger.querySelector('img');
            if (!sourceImage) { return; }
            lastTrigger = trigger;
            image.src = sourceImage.currentSrc || sourceImage.src;
            image.alt = sourceImage.alt || '';
            caption.textContent = sourceImage.alt || '';
            image.classList.toggle('photo-lightbox__image--square', sourceImage.classList.contains('tovar__foto--seal'));
            previousBodyOverflow = document.body.style.overflow;
            document.body.style.overflow = 'hidden';
            trigger.setAttribute('aria-expanded', 'true');
            dialog.hidden = false;
            close.focus();
        }

        function hidePhoto() {
            dialog.hidden = true;
            document.body.style.overflow = previousBodyOverflow;
            image.removeAttribute('src');
            if (lastTrigger) {
                lastTrigger.setAttribute('aria-expanded', 'false');
                lastTrigger.focus();
            }
        }

        close.addEventListener('click', hidePhoto);
        dialog.addEventListener('click', function (event) {
            if (event.target === dialog) { hidePhoto(); }
        });
        document.addEventListener('click', function (event) {
            var trigger = event.target.closest('[data-image-zoom]');
            if (trigger) {
                showPhoto(trigger);
                return;
            }
        });
        document.addEventListener('keydown', function (event) {
            if (!dialog.hidden && event.key === 'Tab') {
                // В окне просмотра доступна одна кнопка управления — закрытие.
                event.preventDefault();
                close.focus();
                return;
            }
            if (!dialog.hidden && event.key === 'Escape') {
                event.preventDefault();
                hidePhoto();
                return;
            }
            var trigger = event.target.closest && event.target.closest('[data-image-zoom]');
            if (trigger && (event.key === 'Enter' || event.key === ' ')) {
                event.preventDefault();
                showPhoto(trigger);
            }
        });
    }

    // Листинговые карточки ведут на позицию, но фото в них не увеличиваются.
    function initCardLinks() {
        document.addEventListener('click', function (event) {
            var card = event.target.closest('[data-card-link]');
            if (card && !event.target.closest('a, button, input, select, textarea, summary, [role="button"]')) {
                window.location.assign(card.getAttribute('data-card-link'));
            }
        });
    }

    // ---------- Умный поиск по каталогам ----------
    // Находит марки насосов и уплотнения по обозначению, названию, описанию и типоразмеру.
    // Понимает: регистр, «ё/е», латинские буквы вместо русских («K» = «К»), забытую смену
    // раскладки («r» = «к»), лишние слова («Х насос»), несколько слов подряд.
    // Короткий запрос-обозначение («К», «Х», «АХ») сначала показывает именно эту марку,
    // а похожие обозначения предлагает кнопками. Без JavaScript каталог остаётся полным.
    var LOOKALIKE = { a: 'а', b: 'в', c: 'с', e: 'е', h: 'н', k: 'к', m: 'м', o: 'о', p: 'р', t: 'т', x: 'х', y: 'у' };
    var EN_TO_RU = {
        q: 'й', w: 'ц', e: 'у', r: 'к', t: 'е', y: 'н', u: 'г', i: 'ш', o: 'щ', p: 'з',
        a: 'ф', s: 'ы', d: 'в', f: 'а', g: 'п', h: 'р', j: 'о', k: 'л', l: 'д',
        z: 'я', x: 'ч', c: 'с', v: 'м', b: 'и', n: 'т', m: 'ь'
    };
    var RU_TO_EN = {};
    Object.keys(EN_TO_RU).forEach(function (key) { RU_TO_EN[EN_TO_RU[key]] = key; });
    var STOP_WORDS = ['насос', 'насоса', 'насосы', 'насосов', 'насосу', 'марка', 'марки', 'марку', 'серия', 'серии',
        'модель', 'модели', 'типоразмер', 'типоразмеры', 'уплотнение', 'уплотнения', 'торцовое', 'торцовые', 'для', 'по'];

    function canon(value) {
        return (value || '').toLocaleLowerCase('ru').replace(/ё/g, 'е')
            .replace(/[a-z]/g, function (ch) { return LOOKALIKE[ch] || ch; })
            .replace(/\s+/g, ' ').trim();
    }

    function compact(value) {
        return canon(value).replace(/[^a-zа-я0-9]/g, '');
    }

    function swapLayout(value, map) {
        return (value || '').toLocaleLowerCase('ru').replace(/[a-zа-яё]/g, function (ch) { return map[ch] || ch; });
    }

    function parseQuery(raw) {
        var tokens = canon(raw).split(' ').filter(function (word) {
            return word && STOP_WORDS.indexOf(word) === -1;
        });
        return { tokens: tokens, compact: compact(tokens.join('')) };
    }

    function tokenInText(text, token) {
        if (token.length <= 2) { return (' ' + text).indexOf(' ' + token) !== -1; }
        if (text.indexOf(token) !== -1) { return true; }
        // грубое отсечение окончания: «грунтовые» находит «грунтовый»
        return token.length >= 6 && text.indexOf(token.slice(0, token.length - 2)) !== -1;
    }

    // Уровни совпадения: 3 — точное обозначение марки или типоразмера, 2 — начало обозначения,
    // 1 — слова запроса найдены в описании, 0 — нет совпадения.
    function evaluateItem(entry, query) {
        var c = query.compact;
        var result = { tier: 0, models: [] };
        if (c) {
            if (entry.codes.indexOf(c) !== -1) { result.tier = 3; }
            var exactModels = entry.models.filter(function (m) { return m.compact === c; });
            if (exactModels.length) { result.tier = 3; result.models = exactModels; }
            if (result.tier < 3) {
                var prefixModels = entry.models.filter(function (m) {
                    return m.compact.indexOf(c) === 0 || (c.length >= 3 && m.compact.indexOf(c) !== -1);
                });
                var prefixCode = entry.codes.some(function (code) { return code.indexOf(c) === 0; });
                if (prefixCode) { result.tier = 2; }
                if (prefixModels.length) { result.tier = 2; result.models = prefixModels; }
            }
        }
        if (result.tier === 0 && query.tokens.length) {
            var haystack = entry.text + ' ' + entry.modelsText;
            var all = query.tokens.every(function (token) { return tokenInText(haystack, token); });
            if (all) { result.tier = 1; }
        }
        return result;
    }

    function initCatalogSearch() {
        var catalogs = document.querySelectorAll('[data-catalog-search]');
        Array.prototype.forEach.call(catalogs, function (catalog) {
            var controls = catalog.querySelector('[data-catalog-search-controls]');
            var input = catalog.querySelector('[data-catalog-search-input]');
            var status = catalog.querySelector('[data-catalog-search-status]');
            var clearButton = catalog.querySelector('[data-catalog-search-clear]');
            var suggestBox = catalog.querySelector('[data-catalog-search-suggest]');
            var emptyBox = catalog.querySelector('[data-catalog-search-empty]');
            var chips = catalog.querySelector('.chips');
            var elements = Array.prototype.slice.call(catalog.querySelectorAll('[data-search-item]'));
            if (!controls || !input || !status || elements.length === 0) { return; }

            var index = {};
            var indexNode = document.querySelector('[data-search-index]');
            if (indexNode) {
                try { index = JSON.parse(indexNode.textContent) || {}; } catch (error) { index = {}; }
            }

            var entries = elements.map(function (el) {
                var base = el.getAttribute('data-card-link') || '';
                var models = (index[el.getAttribute('data-search-slug') || ''] || []).map(function (m) {
                    return { name: m[0], href: base + '/' + m[1], compact: compact(m[0]), canon: canon(m[0]) };
                });
                return {
                    el: el,
                    href: base,
                    codes: (el.getAttribute('data-search-code') || '').split(',').map(compact).filter(Boolean),
                    label: (el.getAttribute('data-search-code') || '').split(',')[0].trim(),
                    text: canon(el.getAttribute('data-search-text') || el.textContent),
                    models: models,
                    modelsText: models.map(function (m) { return m.canon; }).join(' ')
                };
            });

            var currentTarget = null;

            function removeFoundNotes() {
                Array.prototype.forEach.call(catalog.querySelectorAll('[data-search-found]'), function (node) {
                    node.parentNode.removeChild(node);
                });
            }

            function addFoundNote(entry, models) {
                if (!models.length) { return; }
                var note = document.createElement('p');
                note.className = 'marka-card__found';
                note.setAttribute('data-search-found', '');
                note.appendChild(document.createTextNode(models.length === 1 ? 'Найден типоразмер: ' : 'Найденные типоразмеры: '));
                models.slice(0, 4).forEach(function (m, i) {
                    if (i > 0) { note.appendChild(document.createTextNode(', ')); }
                    var link = document.createElement('a');
                    link.href = m.href;
                    link.textContent = m.name;
                    note.appendChild(link);
                });
                if (models.length > 4) {
                    note.appendChild(document.createTextNode(' и ещё ' + (models.length - 4)));
                }
                var foot = entry.el.querySelector('.marka-card__foot');
                entry.el.insertBefore(note, foot);
            }

            function renderSuggestions(list) {
                if (!suggestBox) { return; }
                suggestBox.textContent = '';
                if (!list.length) { suggestBox.hidden = true; return; }
                suggestBox.appendChild(document.createTextNode('Похожие обозначения: '));
                list.slice(0, 8).forEach(function (entry) {
                    var button = document.createElement('button');
                    button.type = 'button';
                    button.className = 'catalog-search__chip';
                    button.textContent = entry.label;
                    button.addEventListener('click', function () {
                        input.value = entry.label;
                        run();
                        input.focus();
                    });
                    suggestBox.appendChild(button);
                });
                suggestBox.hidden = false;
            }

            // Возвращает результат для запроса; если ничего нет, пробует исправить раскладку.
            function search(raw) {
                var variants = [{ text: raw, note: '' }];
                if (/[a-z]/i.test(raw)) { variants.push({ text: swapLayout(raw, EN_TO_RU), note: 'раскладка' }); }
                if (/[а-яё]/i.test(raw)) { variants.push({ text: swapLayout(raw, RU_TO_EN), note: 'раскладка' }); }
                for (var i = 0; i < variants.length; i++) {
                    var query = parseQuery(variants[i].text);
                    if (!query.tokens.length) { continue; }
                    var results = entries.map(function (entry) { return evaluateItem(entry, query); });
                    var top = results.reduce(function (max, r) { return Math.max(max, r.tier); }, 0);
                    if (top > 0) {
                        return { results: results, top: top, shownAs: variants[i].note ? variants[i].text.trim() : '' };
                    }
                }
                return { results: [], top: 0, shownAs: '' };
            }

            function run() {
                var raw = input.value || '';
                var hasQuery = canon(raw) !== '' && parseQuery(raw).tokens.length > 0;
                var visible = 0;
                var onlyEntry = null;
                var onlyModels = [];
                var found = { results: [], top: 0, shownAs: '' };
                removeFoundNotes();
                currentTarget = null;
                if (clearButton) { clearButton.hidden = raw === ''; }

                if (hasQuery) { found = search(raw); }

                entries.forEach(function (entry, i) {
                    var res = hasQuery ? found.results[i] : null;
                    var show = !hasQuery || (res && res.tier > 0 && res.tier === found.top);
                    entry.el.hidden = !show;
                    if (show) {
                        visible += 1;
                        onlyEntry = entry;
                        if (hasQuery && res) {
                            onlyModels = res.models;
                            addFoundNote(entry, res.models);
                        }
                    }
                });

                Array.prototype.forEach.call(catalog.querySelectorAll('[data-search-group]'), function (group) {
                    group.hidden = !group.querySelector('[data-search-item]:not([hidden])');
                });
                if (chips) { chips.hidden = hasQuery; }

                if (suggestBox) {
                    var similar = [];
                    if (hasQuery && found.top === 3) {
                        var q = parseQuery(raw);
                        similar = entries.filter(function (entry, i) {
                            return found.results[i].tier === 2 && q.compact && entry.codes.some(function (code) {
                                return code.indexOf(q.compact) === 0;
                            });
                        });
                    }
                    renderSuggestions(similar);
                }

                if (!hasQuery) {
                    status.textContent = 'Показано: ' + visible;
                } else if (visible === 0) {
                    status.textContent = 'Ничего не найдено. Проверьте обозначение или очистите запрос.';
                } else {
                    status.textContent = 'Найдено: ' + visible + (found.shownAs ? '. Запрос прочитан как «' + found.shownAs + '».' : '');
                }
                if (emptyBox) { emptyBox.hidden = !(hasQuery && visible === 0); }

                if (visible === 1 && onlyEntry) {
                    currentTarget = onlyModels.length === 1 ? onlyModels[0].href : onlyEntry.href;
                }

                if (window.history && window.history.replaceState) {
                    var url = new URL(window.location.href);
                    if (raw.trim()) { url.searchParams.set('q', raw.trim()); } else { url.searchParams.delete('q'); }
                    window.history.replaceState(null, '', url.pathname + url.search + url.hash);
                }
            }

            controls.hidden = false;
            input.addEventListener('input', run);
            input.addEventListener('keydown', function (event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    // единственный результат открывается сразу
                    if (currentTarget) { window.location.assign(currentTarget); }
                } else if (event.key === 'Escape' && input.value) {
                    input.value = '';
                    run();
                }
            });
            if (clearButton) {
                clearButton.addEventListener('click', function () {
                    input.value = '';
                    run();
                    input.focus();
                });
            }

            try {
                var initial = new URLSearchParams(window.location.search).get('q');
                if (initial) { input.value = initial; }
            } catch (error) { /* старый браузер: поиск работает без адресной строки */ }
            run();
        });
    }

    // Внешняя карта подключается только после отдельного действия посетителя.
    function initYandexMap() {
        var blocks = document.querySelectorAll('[data-yandex-map]');
        Array.prototype.forEach.call(blocks, function (block) {
            var consent = block.querySelector('[data-yandex-map-consent]');
            var button = block.querySelector('[data-yandex-map-load]');
            if (!consent || !button) { return; }

            consent.addEventListener('change', function () {
                button.disabled = !consent.checked;
            });

            button.addEventListener('click', function () {
                if (!consent.checked) { return; }
                var frame = document.createElement('iframe');
                frame.src = block.getAttribute('data-map-src');
                frame.title = 'Карта: адрес компании';
                frame.loading = 'lazy';
                frame.referrerPolicy = 'no-referrer-when-downgrade';
                block.replaceChildren(frame);
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        initMenu();
        initForms();
        initYakorya();
        initCardLinks();
        initImageZoom();
        initYandexMap();
        initCatalogSearch();
    });
})();
