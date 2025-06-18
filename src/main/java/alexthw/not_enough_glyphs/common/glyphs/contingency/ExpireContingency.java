package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ExpireContingency extends AbstractContingency {
    public static final ExpireContingency INSTANCE = new ExpireContingency();

    public ExpireContingency() {
        super("contingency_time", "Contingency: Expire");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after a certain amount of time, casting the spell contained within. Won't trigger if forcefully dispelled.";
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.EXPIRE;
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 60);
        addExtendTimeConfig(builder, 30);
        addDurationDownConfig(builder, 10 * 20);
    }

}
