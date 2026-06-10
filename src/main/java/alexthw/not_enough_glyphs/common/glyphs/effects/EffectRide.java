package alexthw.not_enough_glyphs.common.glyphs.effects;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static alexthw.not_enough_glyphs.common.glyphs.CompatRL.neg;

public class EffectRide extends AbstractEffect {

    public static final EffectRide INSTANCE = new EffectRide(neg("ride"), "Ride");

    public EffectRide(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Makes the caster mount on the target entity, but does not grant steering control over it. Doesn't work on most hostile mobs.";
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (isNotFakePlayer(shooter) && shooter != rayTraceResult.getEntity()) {
            if (rayTraceResult.getEntity() instanceof Enemy)
                return;
            shooter.startRiding(rayTraceResult.getEntity());
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 20;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.MANIPULATION);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of();
    }
}
