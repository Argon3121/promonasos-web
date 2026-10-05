package ru.promonasos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Locale;

@Controller
public class LegacyRedirectController {

    private static final Map<String, String> LEGACY_PUMPS = Map.ofEntries(
            Map.entry("nasosy-nvn", "nvn"),
            Map.entry("nasosy-nv-d", "nvd"),
            Map.entry("nasosy-khgn", "khgn"),
            Map.entry("nasosy-khvn", "khvn"),
            Map.entry("nasosy-tsns", "tsns"),
            Map.entry("nasosy-tsn", "tsn"),
            Map.entry("nasosy-ks-2cksv", "ks-ksv"),
            Map.entry("nasosy-pe", "pe"),
            Map.entry("nasosy-se", "se"),
            Map.entry("nasosy-vvn", "vvn"),
            Map.entry("nasosy-tsmg", "tsmg"),
            Map.entry("nasosy-nd", "nd"),
            Map.entry("nasosy-pt", "pt"),
            Map.entry("nasosy-kh", "kh"),
            Map.entry("nasosy-akh", "akh"),
            Map.entry("nasosy-khp", "khp"),
            Map.entry("nasosy-akhp", "akhp"),
            Map.entry("nasosy-tkhi", "tkhi"),
            Map.entry("nasosy-k", "k"),
            Map.entry("nasosy-km", "km"),
            Map.entry("nasosy-sm", "sm"),
            Map.entry("nasosy-v", "v"),
            Map.entry("nasosy-ov", "ov"),
            Map.entry("nasosy-opv", "opv"),
            Map.entry("nasosy-dv-dpv", "dv-dpv"),
            Map.entry("nasosy-d", "d"),
            Map.entry("nasosy-opg", "opg"),
            Map.entry("nasosy-sdv", "sdv"),
            Map.entry("nasosy-okhg", "okhg"),
            Map.entry("nasosy-npv", "npv"),
            Map.entry("nasosy-unbt", "unbt"),
            Map.entry("nasosy-ant", "ant"),
            Map.entry("nasosy-nb", "nb"),
            Map.entry("asosy-pr-pk-pb", "pr-pk-pb"),
            Map.entry("nasosy-pr-pk-pb", "pr-pk-pb"),
            Map.entry("nasosy-peskovye-vertikalnye-tipa-pvp-2c-prvp-2c-pkvp", "pvp-prvp-pkvp"),
            Map.entry("nasosy-grat-grak", "grat-grak"),
            Map.entry("nasosy-grt-grk", "grt-grk"),
            Map.entry("nasosy-pvp", "ppv"),
            Map.entry("nasosy-vintovye-skvazhinnye-etsv", "etsv"),
            Map.entry("nasosy-bn", "bn"),
            Map.entry("nasosy-bv", "bv"),
            Map.entry("nasosy-bm", "bm"),
            Map.entry("nasosy-bt", "bt"),
            Map.entry("nasosy-kmkh", "kmkh"),
            Map.entry("kmkh", "kmkh"),
            Map.entry("vintovye-nasosy-kmkh", "kmkh"),
            Map.entry("odnovintovye-nasosy-ohb", "onv")
    );

    @GetMapping({"/product-category/promyshlennye-nasosy", "/product-category/promyshlennye-nasosy/"})
    public RedirectView starayaKategoriyaNasosov() {
        return permanent("/nasosy");
    }

    @GetMapping({"/product-category/tortsevye-uplotneniya", "/product-category/tortsevye-uplotneniya/"})
    public RedirectView starayaKategoriyaUplotneniy() {
        return permanent("/tortsevye-uplotneniya");
    }

    @GetMapping({"/promyshlennye-nasosy", "/promyshlennye-nasosy/", "/vintovye-nasosy", "/vintovye-nasosy/"})
    public RedirectView katalog() {
        return permanent("/nasosy");
    }

    @GetMapping({"/promyshlennye-nasosy/{legacySlug}", "/vintovye-nasosy/{legacySlug}"})
    public RedirectView marka(@PathVariable String legacySlug) {
        if (legacySlug.equalsIgnoreCase("emkosti-ep")) {
            return permanent("/emkosti-ep");
        }
        String target = LEGACY_PUMPS.get(legacySlug.toLowerCase(Locale.ROOT));
        if (target == null) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return permanent("/nasosy/" + target);
    }

    @GetMapping("/vintovye-nasosy-kmkh")
    public RedirectView kmkh() {
        return permanent("/nasosy/kmkh");
    }

    private RedirectView permanent(String location) {
        RedirectView view = new RedirectView(location, true, false);
        view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return view;
    }
}
