package ru.promonasos.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
// результат подбора: модель + почему её предложили
public class RezultatPodbora {
    private final ModelNasosa model;
    private final MarkaNasosa marka;
    private final double otklonenie;
    private final String verdikt;
    private final String obosnovanie;

    public ModelNasosa getModel() { return model; }
    public MarkaNasosa getMarka() { return marka; }
    public double getOtklonenie() { return otklonenie; }
    public String getVerdikt() { return verdikt; }
    public String getObosnovanie() { return obosnovanie; }
}
