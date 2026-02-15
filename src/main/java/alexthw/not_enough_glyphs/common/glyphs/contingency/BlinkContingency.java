package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;

public class BlinkContingency extends NEGAbstractContingency {

    public static final BlinkContingency INSTANCE = new BlinkContingency();

    public BlinkContingency() {
        super("contingency_blink", "Contingency: Blink");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target teleports.";
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyTriggers.BLINK;
    }

}
