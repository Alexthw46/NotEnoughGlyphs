package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class FallContingency extends AbstractContingency {

    public static final FallContingency INSTANCE = new FallContingency();

    public FallContingency() {
        super("contingency_fall", "Contingency: Fall");
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.ON_FALL;
    }

    @Override
    public int getBaseDuration() {
        return 100;
    }

    @Override
    public int getExtendTimeDuration() {
        return 100;
    }
}
