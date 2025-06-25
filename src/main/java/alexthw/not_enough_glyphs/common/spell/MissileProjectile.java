package alexthw.not_enough_glyphs.common.spell;

import alexthw.not_enough_glyphs.init.ArsNouveauRegistry;
import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.block.IPrismaticBlock;
import com.hollingsworth.arsnouveau.api.event.SpellProjectileHitEvent;
import com.hollingsworth.arsnouveau.api.particle.ParticleEmitter;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineEntryData;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineMap;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.client.ClientInfo;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MissileProjectile extends EntityProjectileSpell {

    public float aoe;
    public boolean activateOnEmpty;
    int maxAge = 200;

    public Set<BlockPos> hitList = new HashSet<>();

    public MissileProjectile(EntityType<? extends MissileProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public MissileProjectile(Level world, SpellResolver resolver) {
        this(world, resolver, 200, true, resolver.spell.getBuffsAtIndex(0, resolver.spellContext.getUnwrappedCaster(), AugmentAOE.INSTANCE));
    }

    public MissileProjectile(Level world, SpellResolver resolver, int maxAge, boolean activate, float aoe) {
        super(Registry.MISSILE_PROJECTILE.get(), world, resolver);
        this.aoe = aoe;
        this.maxAge = maxAge;
        this.activateOnEmpty = activate;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.maxAge) {
            ExplodeMissile();
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void buildEmitters() {
        TimelineMap timelineMap = this.resolver().spell.particleTimeline();
        ProjectileTimeline projectileTimeline = timelineMap.get(ArsNouveauRegistry.MISSILE_TIMELINE.get());
        TimelineEntryData trailConfig = projectileTimeline.trailEffect;
        TimelineEntryData resolveConfig = projectileTimeline.onResolvingEffect;
        TimelineEntryData spawnConfig = projectileTimeline.onSpawnEffect;
        TimelineEntryData flairConfig = projectileTimeline.flairEffect;

        this.tickEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, trailConfig);
        this.resolveEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, resolveConfig);
        this.onSpawnEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, spawnConfig);
        this.flairEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, flairConfig);
        this.castSound = projectileTimeline.castSound.sound;
        this.resolveSound = projectileTimeline.resolveSound.sound;
    }

    @Override
    protected void onHit(HitResult result) {
        result = transformHitResult(result);

        if (!level().isClientSide) {

            SpellProjectileHitEvent event = new SpellProjectileHitEvent(this, result);
            NeoForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return;
            }

            if (result instanceof EntityHitResult entityHitResult) {
                if (entityHitResult.getEntity().equals(this.getOwner())) return;
                if (this.resolver() != null) {
                    activateSpellAtPos(entityHitResult.getEntity().position());
                    sendResolveParticles();
                    attemptRemoval();
                }
            }

            if (result instanceof BlockHitResult blockraytraceresult && !this.isRemoved() && !hitList.contains(blockraytraceresult.getBlockPos())) {

                BlockState state = level().getBlockState(blockraytraceresult.getBlockPos());

                if (state.getBlock() instanceof IPrismaticBlock prismaticBlock) {
                    prismaticBlock.onHit((ServerLevel) level(), blockraytraceresult.getBlockPos(), this);
                    return;
                }

                if (state.is(BlockTags.PORTALS)) {
                    state.entityInside(level(), blockraytraceresult.getBlockPos(), this);
                    return;
                }

                if (state.getBlock() instanceof TargetBlock) {
                    this.onHitBlock(blockraytraceresult);
                }

//                if (canBounce()) {
//                    bounce(blockraytraceresult);
//                    if (numSensitive > 1) {
//                        pierceLeft--; //to replace with bounce field eventually, reduce here since we're not calling attemptRemoval
//                        return;
//                    }
//                }

                if (this.resolver() != null) {
                    this.hitList.add(blockraytraceresult.getBlockPos());
                    activateSpellAtPos(blockraytraceresult.getLocation());
                }
                sendResolveParticles();
                attemptRemoval();
            }
        }
    }

    protected void activateSpellAtPos(Vec3 pos) {
        if (!this.level().isClientSide() && this.resolver() != null) {
            float sideOffset = 5f + 1.3f * aoe;
            float upOffset = 2f + aoe;
            Vec3 offset = new Vec3(sideOffset, upOffset, sideOffset);
            AABB axis = new AABB(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, pos.x - offset.x, pos.y - offset.y, pos.z - offset.z);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, axis, entity -> entity != resolver().spellContext.getUnwrappedCaster());
            if (!entities.isEmpty()) {
                for (LivingEntity entity : entities) {
                    this.resolver().onResolveEffect(this.level(), new EntityHitResult(entity));
                }
            } else if (activateOnEmpty) {
                Vec3 vector3d2 = this.position();
                Vec3 dist;
                if (this.getOwner() == null) {
                    dist = new Vec3(0, 1, 0);
                } else {
                    dist = this.position().subtract(this.getOwner().position());
                }
                this.resolver().onResolveEffect(this.level(), new BlockHitResult(vector3d2, Direction.getNearest(dist.x, dist.y, dist.z), BlockPos.containing(vector3d2), true));
            }
        }
    }

    protected void ExplodeMissile() {
        this.activateSpellAtPos(this.position());
        sendResolveParticles();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("maxAge")) {
            this.maxAge = tag.getInt("maxAge");
        }
        if (tag.contains("aoe")) {
            this.aoe = tag.getFloat("aoe");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("maxAge", this.maxAge);
        tag.putFloat("aoe", this.aoe);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return Registry.MISSILE_PROJECTILE.get();
    }
}