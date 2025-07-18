package alexthw.not_enough_glyphs.common.glyphs.propagators;

import alexthw.not_enough_glyphs.api.IPropagator;
import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodArc;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropagateArc extends AbstractEffect implements IPropagator {

    public static final PropagateArc INSTANCE = new PropagateArc();

    public PropagateArc() {
        super(CompatRL.elemental("propagator_arc"), "Propagate Arc");
    }

    @Override
    public AbstractCastMethod getCastType() {
        return MethodArc.INSTANCE;
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }

    @Override
    public void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        Vec3 pos = hitResult.getLocation();
        ArrayList<EntityProjectileSpell> projectiles = new ArrayList<>();
        EntityProjectileSpell projectileSpell = new EntityProjectileSpell(world, resolver).setGravity(true);
        projectileSpell.setPos(pos.add(0, 1, 0));
        projectiles.add(projectileSpell);
        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        float sizeRatio = shooter.getEyeHeight() / Player.DEFAULT_EYE_HEIGHT;

        for (int i = 1; i < numSplits + 1; i++) {
            Direction offset = shooter.getDirection().getClockWise();
            if (i % 2 == 0) offset = offset.getOpposite();
            // Alternate sides
            BlockPos projPos = BlockPos.containing(pos).relative(offset, i).offset(0, (int) (1.5 * sizeRatio), 0);
            EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver).setGravity(true);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }


        float velocity = MethodArc.getProjectileSpeed(stats);
        Vec3 direction = IPropagator.getDirection(shooter, resolver, pos);
        for (EntityProjectileSpell proj : projectiles) {
            proj.setPos(proj.position().add(0, 0.25 * sizeRatio, 0));
            if (stats.hasBuff(AugmentExtract.INSTANCE) || shooter instanceof FakePlayer) {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            } else {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity, 0.3f);
            }
            world.addFreshEntity(proj);
        }
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 150;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        var extended = new HashSet<>(MethodArc.INSTANCE.getCompatibleAugments());
        extended.add(AugmentExtract.INSTANCE);
        return extended;
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentPierce.INSTANCE, "Projectiles will bounce on blocks or hit through enemies an additional time.");
        map.put(AugmentSplit.INSTANCE, "Creates multiple projectiles.");
        map.put(AugmentAccelerate.INSTANCE, "Projectiles will move faster.");
        map.put(AugmentDecelerate.INSTANCE, "Projectiles will move slower.");
        map.put(AugmentSensitive.INSTANCE, "Projectiles will hit plants and other materials that do not block motion.");
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
