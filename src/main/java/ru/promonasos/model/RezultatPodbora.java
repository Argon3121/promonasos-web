package ru.promonasos.model;

/** Результат подбора: модель плюс объяснение, почему она предложена. */
public class RezultatPodbora {
    private final ModelNasosa model;
    private final MarkaNasosa marka;
    private final double otklonenie;
    private final String verdikt;
    private final String obosnovanie;

    public RezultatPodbora(ModelNasosa model, MarkaNasosa marka, double otklonenie,
                            String verdikt, String obosnovanie) {
        this.model = model;
        this.marka = marka;
        this.otklonenie = otklonenie;
        this.verdikt = verdikt;
        this.obosnovanie = obosnovanie;
    }

    public ModelNasosa getModel() { return model; }
    public MarkaNasosa getMarka() { return marka; }
    public double getOtklonenie() { return otklonenie; }
    public String getVerdikt() { return verdikt; }
    public String getObosnovanie() { return obosnovanie; }
}
