package alexthw.not_enough_glyphs.api.spell_style;

import alexthw.not_enough_glyphs.init.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleOptions;
import com.hollingsworth.arsnouveau.api.particle.configurations.IParticleMotionType;
import com.hollingsworth.arsnouveau.api.particle.configurations.ParticleMotion;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.BaseProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleDensityProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleTypeProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.PropMap;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RayMotion extends ParticleMotion {

    public static MapCodec<RayMotion> CODEC = buildPropCodec(RayMotion::new);

    public static StreamCodec<RegistryFriendlyByteBuf, RayMotion> STREAM = buildStreamCodec(RayMotion::new);

    public RayMotion() {
        this(new PropMap());
    }

    public RayMotion(PropMap propertyMap) {
        super(propertyMap);
    }

    @Override
    public IParticleMotionType<?> getType() {
        return ArsNouveauRegistry.RAY_MOTION.get();
    }

    @Override
    public void tick(PropertyParticleOptions particleOptions, Level level, double x, double y, double z, double prevX, double prevY, double prevZ) {
        ParticleDensityProperty density = getDensity(particleOptions, 5, 0.1f);
        Vec3 adjustedVec = getMotionScaled(new Vec3(x, y, z), density.radius(), density.spawnType().orElse(SpawnType.SPHERE));
        for (int i = 0; i < density.density(); i++) {
            level.addAlwaysVisibleParticle(particleOptions, true, adjustedVec.x, adjustedVec.y, adjustedVec.z,
                    ParticleUtil.inRange(-0.025, 0.025),
                    ParticleUtil.inRange(-0.015, 0.015),
                    ParticleUtil.inRange(-0.025, 0.025));
        }
    }

    @Override
    public List<BaseProperty<?>> getProperties(PropMap propMap) {
        return List.of(propMap.createIfMissing(new ParticleTypeProperty()), propMap.createIfMissing(new ParticleDensityProperty(5, 0.1f, SpawnType.SPHERE)
                .minDensity(1)
                .densityStepSize(5)
                .maxDensity(20)
                .supportsShapes(true)));
    }
}
