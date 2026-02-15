package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;

public class FireContingency extends NEGAbstractContingency {

    public static final FireContingency INSTANCE = new FireContingency();

    public FireContingency() {
        super("contingency_fire", "Contingency: Fire");
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyTriggers.ON_FIRE;
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target is hurt from fire damage, casting the spell contained within.";
    }

}
