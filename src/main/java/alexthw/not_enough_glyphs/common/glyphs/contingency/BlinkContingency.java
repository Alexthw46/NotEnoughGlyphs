package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class BlinkContingency extends AbstractContingency {

    public static final BlinkContingency INSTANCE = new BlinkContingency();

    public BlinkContingency() {
        super("contingency_blink", "Contingency: Blink");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target teleports.";
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.BLINK;
    }

}
