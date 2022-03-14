package io.github.derringersmods.toomanyglyphs.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.phys.EntityHitResult;


public class EffectFilterIsBaby extends EffectFilterEntity {

    public static final EffectFilterIsBaby INSTANCE = new EffectFilterIsBaby("filter_is_baby", "Filter: Baby");

    public EffectFilterIsBaby(String tag, String description) {
        super(tag, description);
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Override
    public boolean matches(EntityHitResult target) {
        if (!(target.getEntity() instanceof AgeableMob)) return false;
        return ((AgeableMob)target.getEntity()).isBaby();
    }
}
