package alexthw.not_enough_glyphs.api;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class FilterUtil {
    public static IFilter getTargetFilter(SpellContext spellContext, IFilter defaultFilter) {
        return getTargetFilter(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().getSpellSize()), defaultFilter);
    }

    public static IFilter getTargetFilter(Spell spell, IFilter defaultFilter) {
        return getTargetFilter(spell.recipe, defaultFilter);
    }

    public static IFilter getTargetFilter(List<AbstractSpellPart> recipe, IFilter defaultFilter) {
        for (AbstractSpellPart part : recipe) {
            if (part instanceof IFilter) return (IFilter) part;
            if (part instanceof AbstractEffect) break;
        }
        return defaultFilter;
    }

    /**
     * Returns true if the given entity is affected by the given filters.
     */
    public static boolean checkIgnoreFilters(Entity e, Set<IFilter> filters) {
        boolean flag = true;
        if (filters == null) return true;
        for (IFilter spellPart : filters) {
            flag &= spellPart.shouldAffect(new EntityHitResult(e), e.level());
        }
        return !flag;
    }

    /**
     * Returns a set of all filters in the spell, starting at the given index.
     */
    public static Set<IFilter> getFilters(List<AbstractSpellPart> recipe, int index) {
        Set<IFilter> list = new HashSet<>();
        for (AbstractSpellPart glyph : recipe.subList(index, recipe.size())) {
            if (glyph instanceof AbstractCastMethod) continue;
            if (glyph instanceof IFilter filter) {
                list.add(filter);
            } else if (glyph instanceof AbstractEffect) break;
        }
        return list;
    }

    /**
     * Returns a predicate that checks if an entity is affected by the given spell.
     */
    public static Predicate<Entity>getFilterPredicate(Spell spell, Predicate<Entity> defaultFilter) {
        Set<IFilter> set = getFilters(spell.recipe, 0);
        if (set.isEmpty()) return defaultFilter;
        return (entity -> !checkIgnoreFilters(entity, set));
    }
}
