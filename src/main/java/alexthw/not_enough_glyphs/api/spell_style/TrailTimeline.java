package alexthw.not_enough_glyphs.api.spell_style;

import alexthw.not_enough_glyphs.init.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;

public class TrailTimeline extends ProjectileTimeline {

    @Override
    public IParticleTimelineType<ProjectileTimeline> getType() {
        return ArsNouveauRegistry.TRAIL_TIMELINE.get();
    }
}
