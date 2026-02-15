package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ExpireContingency extends NEGAbstractContingency {
    public static final ExpireContingency INSTANCE = new ExpireContingency();

    public ExpireContingency() {
        super("contingency_time", "Contingency: Expire");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after a certain amount of time, casting the spell contained within. Won't trigger if forcefully dispelled.";
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyEffectInstance.EXPIRE;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE);
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 60);
        addExtendTimeConfig(builder, 30);
        addDurationDownConfig(builder, 10 * 20);
    }

}
