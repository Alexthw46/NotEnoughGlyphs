package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;

public class HealContingency extends NEGAbstractContingency {

    public static final HealContingency INSTANCE = new HealContingency();

    public HealContingency() {
        super("contingency_heal", "Contingency: Healing");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target heals, casting the spell contained within.";
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyTriggers.ON_HEAL;
    }


}
