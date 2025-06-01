package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class FireContingency extends AbstractContingency {

    public static final FireContingency INSTANCE = new FireContingency();

    public FireContingency() {
        super("contingency_fire", "Contingency: Fire");
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.ON_FIRE;
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target is hurt from fire damage, casting the spell contained within.";
    }

}
