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
        function showPhoto(trigger) {
            lastTrigger = trigger;
            image.src = trigger.currentSrc || trigger.src;
            image.alt = trigger.alt || '';
            caption.textContent = trigger.alt || '';
            image.classList.toggle('photo-lightbox__image--square', trigger.classList.contains('tovar__foto--seal'));
            dialog.hidden = false;
            close.focus();
        }

        function hidePhoto() {
            dialog.hidden = true;
            image.removeAttribute('src');
            if (lastTrigger) { lastTrigger.focus(); }
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

    document.addEventListener('DOMContentLoaded', function () {
        initMenu();
        initForms();
        initYakorya();
        initCardLinks();
        initImageZoom();
    });
})();
