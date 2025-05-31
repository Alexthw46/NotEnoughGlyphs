package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class HurtContingency extends AbstractContingency {

    public static final HurtContingency INSTANCE = new HurtContingency();

    public HurtContingency() {
        super("contingency_hurt", "Contingency: Hurt");
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.HEROICS;
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
