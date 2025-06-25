package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class FallContingency extends AbstractContingency {

    public static final FallContingency INSTANCE = new FallContingency();

    public FallContingency() {
        super("contingency_fall", "Contingency: Fall");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target is in free fall for enough time, casting the spell contained within. Base at 5 blocks and increases/decreases by 1 per Amplify/Dampen.";
    }

    @Override
    public ContingencyEffectInstance.TRIGGER getTrigger() {
        return ContingencyEffectInstance.TRIGGER.ON_FALL;
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAmplify.INSTANCE, "Increases fall before trigger by one block.");
        map.put(AugmentDampen.INSTANCE, "Decreases fall before trigger by one block.");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentDampen.INSTANCE, AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentSplit.INSTANCE);
    }

}
