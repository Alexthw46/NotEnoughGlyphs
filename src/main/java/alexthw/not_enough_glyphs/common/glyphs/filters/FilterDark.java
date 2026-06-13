package alexthw.not_enough_glyphs.common.glyphs.filters;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FilterDark extends AbstractEffectFilter {

    public static final FilterDark DARK = new FilterDark("filter_dark", "Filter: Dark");

    public FilterDark(String tag, String description) {
        super(CompatRL.neg(tag), description);
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult blockHitResult, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return level.getBrightness(LightLayer.BLOCK, blockHitResult.getBlockPos().above()) < 8 + spellStats.getAmpMultiplier();
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult entity, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return level.getBrightness(LightLayer.BLOCK, entity.getEntity().getOnPos()) < 8 + spellStats.getAmpMultiplier();
    }

    @Override
    String getDescriptionSegment() {
        return "a dark area. The light threshold for this glyph is 8 and ignores sunlight, can be adjusted with amplify/dampen.";
    }

    @Override
    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }

}
