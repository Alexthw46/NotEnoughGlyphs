package alexthw.not_enough_glyphs.init;

import alexthw.ars_elemental.common.glyphs.MethodArcProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.common.glyphs.PropagatorArc;
import alexthw.ars_elemental.common.glyphs.PropagatorHoming;
import alexthw.not_enough_glyphs.api.spell_style.MissileTimeline;
import alexthw.not_enough_glyphs.api.spell_style.RayMotion;
import alexthw.not_enough_glyphs.api.spell_style.RayTimeline;
import alexthw.not_enough_glyphs.api.spell_style.TrailTimeline;
import alexthw.not_enough_glyphs.common.glyphs.contingency.*;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectChaining;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectFlatten;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectMomentum;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectPlow;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectResize;
import alexthw.not_enough_glyphs.common.glyphs.effects.EffectReverseDirection;
import alexthw.not_enough_glyphs.common.glyphs.filters.*;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodArc;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodHoming;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodMissile;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodOverhead;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodRay;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodTrail;
import alexthw.not_enough_glyphs.common.glyphs.propagators.*;
import alexthw.not_enough_glyphs.common.spell.*;
import com.hollingsworth.arsnouveau.api.particle.configurations.IParticleMotionType;
import com.hollingsworth.arsnouveau.api.particle.configurations.SimpleParticleMotionType;
import com.hollingsworth.arsnouveau.api.particle.timelines.BurstTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.SimpleParticleTimelineType;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.ParticleMotionRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ITurretBehavior;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectReset;
import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hollingsworth.arsnouveau.api.registry.ParticleMotionRegistry.PARTICLE_CONFIG;
import static com.hollingsworth.arsnouveau.api.registry.ParticleTimelineRegistry.TIMELINE_DF;
import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret.ROT_TURRET_BEHAVIOR_MAP;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    static public boolean arsElemental, tooManyGlyphs, arsOmega, arsTrinkets;

    public static void registerGlyphs() {

        arsElemental = ModList.get().isLoaded("ars_elemental");
        tooManyGlyphs = ModList.get().isLoaded("toomanyglyphs");
        arsOmega = ModList.get().isLoaded("arsomega");
        arsTrinkets = ModList.get().isLoaded("ars_trinkets");

        //neg effects
        register(EffectPlow.INSTANCE);
        register(MethodTrail.INSTANCE);
        register(EffectMomentum.INSTANCE);

        //neg filters
        register(FilterLight.LIGHT);
        register(FilterDark.DARK);

        //contingencies
        register(FallContingency.INSTANCE);
        register(HealContingency.INSTANCE);
        register(HeroicsContingency.INSTANCE);
        register(DeathContingency.INSTANCE);
        register(FireContingency.INSTANCE);
        register(BlinkContingency.INSTANCE);
        register(ExpireContingency.INSTANCE);

        //tmg
        if (!tooManyGlyphs) {
            //tmg methods
            register(MethodRay.INSTANCE);

            //tmg effects
            register(EffectReverseDirection.INSTANCE);
            register(EffectChaining.INSTANCE);

            //filters
            register(FilterBlock.INSTANCE);
            register(FilterEntity.INSTANCE);
            register(FilterLiving.INSTANCE);
            register(FilterLivingNotMonster.INSTANCE);
            register(FilterLivingNotPlayer.INSTANCE);
            register(FilterMonster.INSTANCE);
            register(FilterPlayer.INSTANCE);
            register(FilterItem.INSTANCE);
            register(FilterAnimal.INSTANCE);
            register(FilterBaby.INSTANCE);
            register(FilterMature.INSTANCE);
        }

        // trinket self filter
        if (!arsTrinkets) {
            register(FilterSelf.SELF);
            register(FilterSelf.NOT_SELF);
        }

        //neg propagators
        register(PropagatePlane.INSTANCE);

        //omega
        if (!arsOmega) {
            register(EffectFlatten.INSTANCE);

            register(PropagateUnderfoot.INSTANCE);
            register(PropagateProjectile.INSTANCE);
            register(PropagateSelf.INSTANCE);

            register(MethodMissile.INSTANCE);
            register(MethodOverhead.INSTANCE);
            register(PropagateMissile.INSTANCE);
            register(PropagateOverhead.INSTANCE);
        }

        //elemental
        if (!arsElemental) {
            register(MethodArc.INSTANCE);
            register(MethodHoming.INSTANCE);

            register(PropagateArc.INSTANCE);
            register(PropagateHoming.INSTANCE);
        } else {

            registeredSpells.addAll(List.of(
                    MethodArcProjectile.INSTANCE, MethodHomingProjectile.INSTANCE,
                    PropagatorArc.INSTANCE, PropagatorHoming.INSTANCE)
            );
            PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_FIRE);
            PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_WATER);
            PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_EARTH);
            PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_AIR);
        }

        //ex scalaes
        register(EffectResize.INSTANCE);

        //perks
        PerkRegistry.registerPerk(FocusPerk.MANIPULATION);
        PerkRegistry.registerPerk(FocusPerk.SUMMONING);
        PerkRegistry.registerPerk(RandomPerk.INSTANCE);
        PerkRegistry.registerPerk(PacificThread.INSTANCE);
        PerkRegistry.registerPerk(BulldozeThread.INSTANCE);
        PerkRegistry.registerPerk(SharpThread.INSTANCE);
        PerkRegistry.registerPerk(PounchThread.INSTANCE);

        PerkRegistry.registerPerk(SpellCritChancePerk.INSTANCE);
        PerkRegistry.registerPerk(SpellCritDamagePerk.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart) {
        APIRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static final DeferredHolder<IParticleMotionType<?>, IParticleMotionType<RayMotion>> RAY_MOTION = PARTICLE_CONFIG.register("ray", () -> new SimpleParticleMotionType<>(RayMotion.CODEC, RayMotion.STREAM, RayMotion::new));
    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<ProjectileTimeline>> MISSILE_TIMELINE = TIMELINE_DF.register("missile_projectile", () -> new SimpleParticleTimelineType<>(MethodMissile.INSTANCE, ProjectileTimeline.CODEC, ProjectileTimeline.STREAM_CODEC, MissileTimeline::new));
    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<ProjectileTimeline>> TRAIL_TIMELINE = TIMELINE_DF.register("trail_projectile", () -> new SimpleParticleTimelineType<>(MethodTrail.INSTANCE, ProjectileTimeline.CODEC, ProjectileTimeline.STREAM_CODEC, TrailTimeline::new));
    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<RayTimeline>> RAY_TIMELINE = TIMELINE_DF.register("ray", () -> new SimpleParticleTimelineType<>(MethodRay.INSTANCE, RayTimeline.CODEC, RayTimeline.STREAM_CODEC, RayTimeline::new));
    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<BurstTimeline>> CHAIN_TIMELINE = TIMELINE_DF.register("chaining", () -> new SimpleParticleTimelineType<>(EffectChaining.INSTANCE, BurstTimeline.CODEC, BurstTimeline.STREAM_CODEC, BurstTimeline::new));

    public static void postInit() {
        SpellCasterRegistry.register(Registry.SPELL_BINDER.get(), (stack) -> stack.get(Registry.SPELL_BINDER_CASTER));
        PerkRegistry.registerPerkProvider(Registry.SPELL_BINDER.get(), List.of(List.of(PerkSlot.ONE, PerkSlot.TWO)));
        EffectReset.RESET_LIMITS.add(PropagatePlane.INSTANCE);
        EffectReset.RESET_LIMITS.add(EffectChaining.INSTANCE);

        registerSpellStyles();
    }

    public static void registerSpellStyles() {


        List<IParticleMotionType<?>> PROJECTILE_OPTIONS = Arrays.asList(
                ParticleMotionRegistry.TRAIL_TYPE.get(),
                ParticleMotionRegistry.SPIRAL_TYPE.get(),
                ParticleMotionRegistry.HELIX_TYPE.get(),
                ParticleMotionRegistry.WAVE_TYPE.get(),
                ParticleMotionRegistry.ZIGZAG_TYPE.get());

        List<IParticleMotionType<?>> RESOLVE_OPTIONS = Arrays.asList(ParticleMotionRegistry.BURST_TYPE.get(), RAY_MOTION.get());

        List<IParticleMotionType<?>> ON_SPAWN_OPTIONS = Arrays.asList(ParticleMotionRegistry.NONE_TYPE.get(),
                ParticleMotionRegistry.BURST_TYPE.get());
        List<IParticleMotionType<?>> FLAIR_OPTIONS = Arrays.asList(ParticleMotionRegistry.NONE_TYPE.get(),
                ParticleMotionRegistry.SPIRAL_TYPE.get(), ParticleMotionRegistry.TRAIL_TYPE.get(), ParticleMotionRegistry.HELIX_TYPE.get(),
                ParticleMotionRegistry.WAVE_TYPE.get(),
                ParticleMotionRegistry.ZIGZAG_TYPE.get());

        RayTimeline.RESOLVING_OPTIONS.addAll(RESOLVE_OPTIONS);

    }


    static {

        TURRET_BEHAVIOR_MAP.put(MethodTrail.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position iposition, Direction direction) {
                SpellStats stats = resolver.getCastStats();
                boolean gravity = stats.hasBuff(AugmentDampen.INSTANCE);
                TrailingProjectile spell = new TrailingProjectile(world, resolver);
                spell.setOwner(fakePlayer);
                spell.setPos(iposition.x(), iposition.y() - 0.25, iposition.z());
                spell.setAoe(stats.getAoeMultiplier());
                spell.setDelay((int) stats.getDurationMultiplier());
                spell.setGravity(gravity);
                float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), velocity, 0);
                } else {
                    spell.shoot(direction.getStepX(), ((float) direction.getStepY()), direction.getStepZ(), velocity, 0);
                }
                world.addFreshEntity(spell);

            }
        });
        ROT_TURRET_BEHAVIOR_MAP.put(MethodTrail.INSTANCE, TURRET_BEHAVIOR_MAP.get(MethodTrail.INSTANCE));

        TURRET_BEHAVIOR_MAP.put(MethodMissile.INSTANCE, new ITurretBehavior() {

            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position iposition, Direction direction) {
                SpellStats stats = resolver.getCastStats();
                boolean gravity = stats.hasBuff(AugmentDampen.INSTANCE);
                int duration = (int) Math.max(5, 30 + 7f * stats.getDurationMultiplier());

                MissileProjectile spell = new MissileProjectile(world, resolver, duration, true, (float) stats.getAoeMultiplier());
                spell.setOwner(fakePlayer);
                spell.setPos(iposition.x(), iposition.y() - 0.25, iposition.z());
                spell.setGravity(gravity);
                float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), velocity, 0);
                } else {
                    spell.shoot(direction.getStepX(), ((float) direction.getStepY()), direction.getStepZ(), velocity, 0);
                }
                world.addFreshEntity(spell);
            }
        });
        ROT_TURRET_BEHAVIOR_MAP.put(MethodMissile.INSTANCE, TURRET_BEHAVIOR_MAP.get(MethodMissile.INSTANCE));

        TURRET_BEHAVIOR_MAP.put(MethodRay.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel serverLevel, BlockPos pos, Player fakePlayer, Position dispensePosition, Direction direction) {
                Vec3 fromPoint = (Vec3) dispensePosition;
                Vec3 viewVector;
                if (serverLevel.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    viewVector = rotatingTurretTile.getShootAngle().normalize();
                } else
                    viewVector = pos.getCenter().vectorTo((Vec3) dispensePosition).normalize();
                MethodRay.INSTANCE.fireRay(serverLevel, fakePlayer, resolver.getCastStats(), resolver.spellContext, resolver, fromPoint, viewVector);
            }
        });
        ROT_TURRET_BEHAVIOR_MAP.put(MethodRay.INSTANCE, TURRET_BEHAVIOR_MAP.get(MethodRay.INSTANCE));

    }


}
