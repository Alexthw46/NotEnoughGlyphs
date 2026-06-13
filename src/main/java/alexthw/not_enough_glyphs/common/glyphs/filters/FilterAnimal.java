package alexthw.not_enough_glyphs.common.glyphs.filters;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class FilterAnimal extends FilterLiving {
    public static final FilterAnimal INSTANCE = new FilterAnimal("filter_animal", "Filter: Animal");

    public FilterAnimal(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return super.shouldResolveOnEntity(target, level, spellStats, spellContext, resolver) && target.getEntity() instanceof Animal;
    }

    @Override
    String getDescriptionSegment() {
        return "an Animal";
    }
}
