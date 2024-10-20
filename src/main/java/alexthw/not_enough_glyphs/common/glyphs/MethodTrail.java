package alexthw.not_enough_glyphs.common.glyphs;

import alexthw.not_enough_glyphs.common.spell.TrailingProjectile;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBurst;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MethodTrail extends AbstractCastMethod {

    public static final MethodTrail INSTANCE = new MethodTrail();

    public MethodTrail() {
        super(CompatRL.neg("trail"), "Trail");
    }

    @Override
    protected void addDefaultInvalidCombos(Set<ResourceLocation> defaults) {
        defaults.addAll(Stream.of(EffectLinger.INSTANCE, EffectWall.INSTANCE, EffectBurst.INSTANCE).map(AbstractSpellPart::getRegistryName).toList());
    }

    public void summonProjectiles(Level world, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        List<TrailingProjectile> projectiles = new ArrayList<>();
        for (int i = 0; i < numSplits; i++) {
            TrailingProjectile spell = new TrailingProjectile(world, resolver);
            spell.setAoe(stats.getAoeMultiplier());
            spell.setDelay((int) stats.getDurationMultiplier());
            projectiles.add(spell);
        }

        float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
        int opposite = -1;
        int counter = 0;

        for (TrailingProjectile proj : projectiles) {
            proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + Math.round(counter / 2.0) * 10 * opposite, 0.0F, velocity, 0.8f);
            opposite = opposite * -1;
            counter++;
            world.addFreshEntity(proj);
        }
    }

    // Summons the projectiles directly above the block, facing downwards. Legacy Splits
    public void summonProjectiles(Level world, BlockPos pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        ArrayList<EntityProjectileSpell> projectiles = new ArrayList<>();
        EntityProjectileSpell projectileSpell = new EntityProjectileSpell(world, resolver);
        projectileSpell.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
        projectiles.add(projectileSpell);

        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        for (int i = 1; i < numSplits + 1; i++) {
            Direction offset = shooter.getDirection().getClockWise();
            if (i % 2 == 0) offset = offset.getOpposite();
            // Alternate sides
            BlockPos projPos = pos.relative(offset, i);
            projPos = projPos.offset(0, 2, 0);
            EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }
        for (EntityProjectileSpell proj : projectiles) {
            proj.setDeltaMovement(new Vec3(0, -0.1, 0));
            world.addFreshEntity(proj);
        }
    }

    @Override
    public CastResolveType onCast(ItemStack stack, LivingEntity shooter, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        summonProjectiles(world, shooter, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Level world = context.getLevel();
        Player shooter = context.getPlayer();
        summonProjectiles(world, shooter, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        caster.lookAt(EntityAnchorArgument.Anchor.EYES, blockRayTraceResult.getLocation().add(0, 0, 0));
        summonProjectiles(caster.getCommandSenderWorld(), blockRayTraceResult.getBlockPos(), caster, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCastOnEntity(ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        summonProjectiles(caster.getCommandSenderWorld(), caster, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    /**
     * The default cost generated for the config.
     * This should not be used directly for calculations, but as a helper for a recommended value.
     */
    @Override
    protected int getDefaultManaCost() {
        return 200;
    }

    /**
     * Returns the set of augments that this spell part can be enhanced by.
     * Mods should use {@link AbstractSpellPart#compatibleAugments} for addon-supported augments.
     *
     * @see AbstractSpellPart#augmentSetOf(AbstractAugment...) for easy syntax to make the Set.
     * This should not be accessed directly, but can be overridden.
     */
    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentSensitive.INSTANCE, AugmentAccelerate.INSTANCE, AugmentDecelerate.INSTANCE, AugmentAOE.INSTANCE, AugmentSplit.INSTANCE, AugmentPierce.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE);
    }

    public ForgeConfigSpec.IntValue PROJECTILE_TTL;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        PROJECTILE_TTL = builder.comment("Max lifespan of the projectile, in seconds.").defineInRange("max_lifespan", 60, 0, Integer.MAX_VALUE);
    }

    public int getProjectileLifespan() {
        return PROJECTILE_TTL != null ? PROJECTILE_TTL.get() : 60;
    }
}
