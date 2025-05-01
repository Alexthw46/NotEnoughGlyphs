package alexthw.not_enough_glyphs.api;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public interface IPropagator {

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        SpellContext newContext = spellContext.makeChildContext();
        newContext.withSpell(newContext.getSpell().mutable().add(0, DUMMY).immutable());
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        spellContext.setCanceled(true);
        AbstractCastMethod newCastType = getCastType();
        if (newCastType != null)
            newResolver.castType = newCastType;
        propagate(world, rayTraceResult, shooter, stats, newResolver);
    }

    AbstractAugment DUMMY = AugmentDampen.INSTANCE;

    void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

    /**
     * @return the new cast method for the propagated resolver
     */
    @Nullable
    default AbstractCastMethod getCastType() {
        return null;
    }

}
