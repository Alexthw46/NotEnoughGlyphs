package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class HealContingency extends AbstractContingency {

    public static final HealContingency INSTANCE = new HealContingency();

    public HealContingency() {
        super("contingency_heal", "Contingency: Healing");
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.ON_HEAL;
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
