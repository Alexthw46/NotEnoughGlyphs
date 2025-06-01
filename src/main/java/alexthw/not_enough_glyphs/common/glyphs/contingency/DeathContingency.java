package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;

public class DeathContingency extends AbstractContingency {

    public static final DeathContingency INSTANCE = new DeathContingency();

    public DeathContingency() {
        super("contingency_death", "Contingency: Death");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target's death. It will still trigger if the target survives with a totem.";
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.DEATH;
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
