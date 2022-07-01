package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class EffectReverseDirection extends AbstractTMGEffect {
    public static final EffectReverseDirection INSTANCE = new EffectReverseDirection("reverse_direction", "Reverse Direction");

    public EffectReverseDirection(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        BlockHitResult reversedRayTraceResult = rayTraceResult
                .withPosition(rayTraceResult.isInside()
                        ? rayTraceResult.getBlockPos()
                        : rayTraceResult.getBlockPos().relative(rayTraceResult.getDirection()).relative(rayTraceResult.getDirection()))
                            // Relative adjustment of 2 required to get to the opposite side of the pivot block
                .withDirection(rayTraceResult.getDirection().getOpposite());
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell continuation = spellContext.getRemainingSpell();
        SpellContext newContext = spellContext.clone().withSpell(continuation);
        resolver.getNewResolver(newContext).onResolveEffect(world, reversedRayTraceResult);
    }

    @Override
    public SpellTier getTier() { return SpellTier.ONE; }

    @Override
    public int getDefaultManaCost() { return 0; }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }
}
