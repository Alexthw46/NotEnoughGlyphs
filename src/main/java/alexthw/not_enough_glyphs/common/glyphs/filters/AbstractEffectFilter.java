package alexthw.not_enough_glyphs.common.glyphs.filters;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractEffectFilter extends AbstractFilter {

    public AbstractEffectFilter(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public AbstractEffectFilter inverted() {
        this.inverted = !inverted;
        return this;
    }

    public boolean shouldAffect(HitResult rayTraceResult) {
        return inverted != super.shouldAffect(rayTraceResult);
    }

    private boolean inverted = false;

    @Override
    public Integer getTypeIndex() {
        return 15;
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!shouldResolveOnEntity(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!shouldResolveOnBlock(rayTraceResult)) spellContext.setCanceled(true);
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target) {
        return false;
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target) {
        return false;
    }

}