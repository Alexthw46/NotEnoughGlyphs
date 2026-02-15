package alexthw.not_enough_glyphs.common.glyphs.contingency;


import com.alexthw.sauce.util.ContingencyEffectInstance;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class HeroicsContingency extends NEGAbstractContingency {

    public static final HeroicsContingency INSTANCE = new HeroicsContingency();

    public HeroicsContingency() {
        super("contingency_health", "Contingency: Health");
    }

    @Override
    public String getBookDescription() {
        return "The contingency will trigger after the target's health falls below a certain threshold, casting the spell contained within. Base at 20% and increases by 10% per Amplify.";
    }

    @Override
    public ContingencyEffectInstance.ContingencyTrigger getTrigger() {
        return ContingencyTriggers.HEROICS;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        super.addDefaultAugmentLimits(defaults);
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 3);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAmplify.INSTANCE, "Increases health threshold by 10%.");
    }

}
