package alexthw.not_enough_glyphs.common.glyphs.filters;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class FilterLivingNotMonster extends FilterLiving {
    public static final FilterLivingNotMonster INSTANCE = new FilterLivingNotMonster("filter_living_not_monster", "Filter: Not Monster");

    public FilterLivingNotMonster(String tag, String description) {
        super(tag, description);
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return super.shouldResolveOnEntity(target, level, spellStats, spellContext, resolver) && target.getEntity().getClassification(false) != MobCategory.MONSTER;
    }


    @Override
    String getDescriptionSegment() {
        return super.getDescriptionSegment() + " but not a Monster";
    }

}
