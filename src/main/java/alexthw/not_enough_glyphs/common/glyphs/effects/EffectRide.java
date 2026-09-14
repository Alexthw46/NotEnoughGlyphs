package alexthw.not_enough_glyphs.common.glyphs.effects;

import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtract;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Map;

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
        Entity result = rayTraceResult.getEntity();
        if (spellStats.getAugments().contains(AugmentExtract.INSTANCE))
        {
            result.stopRiding();
        }
        else if (isNotFakePlayer(shooter) && shooter != result) {
            if (result instanceof Enemy || result.getType().is(Registry.RIDE_BLACKLIST))
                return;
            shooter.startRiding(result);
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
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentExtract.INSTANCE, "Dismounts target entity instead.");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentExtract.INSTANCE);
    }
}
