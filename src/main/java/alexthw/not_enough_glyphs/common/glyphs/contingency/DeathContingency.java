package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;

public class DeathContingency extends NEGAbstractContingency {

    public static final DeathContingency INSTANCE = new DeathContingency();

    public DeathContingency() {
        super("contingency_death", "Contingency: Death");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target's death. It will still trigger if the target survives with a totem.";
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyTriggers.DEATH;
    }


}
