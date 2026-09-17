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

    document.addEventListener('DOMContentLoaded', function () {
        initMenu();
        initForms();
        initYakorya();
    });
})();
