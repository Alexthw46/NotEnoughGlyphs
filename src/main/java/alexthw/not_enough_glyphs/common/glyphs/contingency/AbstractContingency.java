package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;
import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class AbstractContingency extends AbstractEffect implements IPotionEffect {

    public AbstractContingency(String effectName, String description) {
        super(CompatRL.neg(effectName), description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity livingEntity) {
            // create delayed resolver
            SpellContext newContext = spellContext.makeChildContext();
            SpellResolver newResolver = resolver.getNewResolver(newContext);
            spellContext.setCanceled(true);
            // make sure to remove existing contingencies
            if (livingEntity.hasEffect(Registry.CONTINGENCY)) livingEntity.removeEffectNoUpdate(Registry.CONTINGENCY);
            int ticks = getBaseDuration() * 20 + getExtendTimeDuration() * spellStats.getDurationInTicks();
            livingEntity.addEffect(new ContingencyEffectInstance(newResolver, getTrigger(), ticks, spellStats.getAmpMultiplier()));
        }
    }

    @Override
    public Integer getTypeIndex() {
        return 6;
    }

    public abstract ContingencyEffectInstance.TRIGGER getTrigger();

    @Override
    protected int getDefaultManaCost() {
        return 100;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getSummonAugments();
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ABJURATION);
    }

}
