package alexthw.not_enough_glyphs.common.glyphs.propagators;

import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.not_enough_glyphs.api.IPropagator;
import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodHoming;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityHomingProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

import static alexthw.not_enough_glyphs.common.glyphs.forms.MethodHoming.getProjectileSpeed;

public class PropagateHoming extends AbstractEffect implements IPropagator {

    public static final PropagateHoming INSTANCE = new PropagateHoming();

    @Override
    public AbstractCastMethod getCastType() {
        return MethodHoming.INSTANCE;
    }

    public PropagateHoming() {
        super(CompatRL.elemental("propagator_homing"), "Propagate Homing");
    }

    @Override
    public void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        Vec3 pos = hitResult.getLocation();
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        List<EntityHomingProjectileSpell> projectiles = new ArrayList<>();
        // Create the projectiles
        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new EntityHomingProjectileSpell(world, resolver));
        }
        float velocity = getProjectileSpeed(stats);
        int opposite = -1;
        int counter = 0;

        // Adjust the direction of the projectiles
        Vec3 direction = IPropagator.getDirection(shooter, resolver, pos);

        // Set the position and shoot the projectiles in the correct direction
        for (EntityHomingProjectileSpell proj : projectiles) {
            proj.setPos(pos.add(0, 1, 0));
            proj.setIgnored(MethodHoming.basicIgnores(shooter, stats.hasBuff(AugmentSensitive.INSTANCE), resolver.spell));
            if (stats.hasBuff(AugmentDampen.INSTANCE)) proj.setGravity(true);
            if (stats.hasBuff(AugmentExtract.INSTANCE) || (shooter instanceof FakePlayer)) {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            } else {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + Math.round(counter / 2.0) * 5 * opposite, 0.0F, velocity, 0.8f);
            }
            opposite = opposite * -1;
            counter++;
            world.addFreshEntity(proj);
        }

    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 400;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        var extended = new HashSet<>(MethodHomingProjectile.INSTANCE.getCompatibleAugments());
        extended.add(AugmentExtract.INSTANCE);
        return extended;
    }

    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentPierce.INSTANCE, "Projectiles will pierce through enemies and blocks an additional time.");
        map.put(AugmentSplit.INSTANCE, "Creates multiple projectiles.");
        map.put(AugmentAccelerate.INSTANCE, "Projectiles will move faster.");
        map.put(AugmentDecelerate.INSTANCE, "Projectiles will move slower.");
        map.put(AugmentSensitive.INSTANCE, "Projectiles will also target players.");
        map.put(AugmentDampen.INSTANCE, "Projectiles will be affected by gravity.");
        map.put(AugmentExtract.INSTANCE, "Projectile direction will be relative to caster position.");
    }

    @Override
    public Glyph getGlyph() {
        if (glyphItem == null) {
            glyphItem = new Glyph(this) {
                @Override
                public @NotNull String getCreatorModId(@NotNull ItemStack itemStack) {
                    return NotEnoughGlyphs.MODID;
                }
            };
        }
        return this.glyphItem;
    }
}