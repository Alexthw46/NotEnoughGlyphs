package alexthw.not_enough_glyphs.common.glyphs.propagators;

import alexthw.not_enough_glyphs.common.glyphs.RepackedGlyph;
import com.alexthw.sauce.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static alexthw.not_enough_glyphs.common.glyphs.CompatRL.omega;

public class PropagateSelf extends AbstractEffect implements IPropagator, RepackedGlyph {

    public static final PropagateSelf INSTANCE = new PropagateSelf();

    public PropagateSelf() {
        super(omega("propagate_self"), "Propagate Self");
    }

    @Override
    public String getBookDescription() {
        return "Resolves the remainder of the spell on the caster.";
    }

    @Override
    public void propagate(Level world, HitResult result, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        if (resolver.spellContext != null && resolver.spellContext.castingTile != null) {
            BlockPos blockPos = resolver.spellContext.castingTile.getBlockPos();
            resolver.onResolveEffect(world, new BlockHitResult(blockPos.getCenter(), Direction.DOWN, blockPos, true));
        } else resolver.onResolveEffect(world, new EntityHitResult(shooter));
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return MethodSelf.INSTANCE.getCompatibleAugments();
    }


}
