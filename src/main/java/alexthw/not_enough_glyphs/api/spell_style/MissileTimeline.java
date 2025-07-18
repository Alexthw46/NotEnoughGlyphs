package alexthw.not_enough_glyphs.api.spell_style;

import alexthw.not_enough_glyphs.init.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;

public class MissileTimeline extends ProjectileTimeline {

    @Override
    public IParticleTimelineType<ProjectileTimeline> getType() {
        return ArsNouveauRegistry.MISSILE_TIMELINE.get();
    }
}
