package alexthw.not_enough_glyphs.api.spell_style;

import alexthw.not_enough_glyphs.init.ArsNouveauRegistry;
import com.google.common.collect.ImmutableList;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleOptions;
import com.hollingsworth.arsnouveau.api.particle.configurations.IParticleMotionType;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.BaseProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.MotionProperty;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.SoundProperty;
import com.hollingsworth.arsnouveau.api.particle.timelines.BaseTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineEntryData;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineOption;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RayTimeline extends BaseTimeline<RayTimeline> {
    public static final MapCodec<RayTimeline> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TimelineEntryData.CODEC.fieldOf("onResolvingEffect").forGetter(i -> i.onResolvingEffect),
            SoundProperty.CODEC.fieldOf("resolveSound").forGetter(i -> i.resolveSound)
    ).apply(instance, RayTimeline::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RayTimeline> STREAM_CODEC = StreamCodec.composite(
            TimelineEntryData.STREAM,
            RayTimeline::onResolvingEffect,
            SoundProperty.STREAM_CODEC,
            i -> i.resolveSound,
            RayTimeline::new);

    public static final List<IParticleMotionType<?>> RESOLVING_OPTIONS = new CopyOnWriteArrayList<>();

    public TimelineEntryData onResolvingEffect;
    public SoundProperty resolveSound = new SoundProperty();

    public RayTimeline() {
        this(new TimelineEntryData(new RayMotion(), new PropertyParticleOptions()), new SoundProperty());
    }

    public RayTimeline(TimelineEntryData onResolvingEffect, SoundProperty soundProperty) {
        this.onResolvingEffect = onResolvingEffect;
        this.resolveSound = soundProperty;
    }

    public TimelineEntryData onResolvingEffect() {
        return onResolvingEffect;
    }

    @Override
    public IParticleTimelineType<RayTimeline> getType() {
        return ArsNouveauRegistry.RAY_TIMELINE.get();
    }

    @Override
    public List<BaseProperty<?>> getProperties() {
        return List.of(new MotionProperty(new TimelineOption(ArsNouveau.prefix("impact"), this.onResolvingEffect, ImmutableList.copyOf(RESOLVING_OPTIONS)), List.of(resolveSound)));
    }


}
