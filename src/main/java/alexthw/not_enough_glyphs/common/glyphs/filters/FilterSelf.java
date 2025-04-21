package alexthw.not_enough_glyphs.common.glyphs.filters;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class FilterSelf extends FilterEntity {

    public static final FilterSelf SELF = new FilterSelf("filter_self", "Filter: Self");
    public static final AbstractEffectFilter NOT_SELF = new FilterSelf("filter_not_self", "Filter: Not Self").inverted();


    public FilterSelf(String tag, String description) {
        super(CompatRL.trinkets(tag), description);
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // we might be resolving on caster, check if we are in filter:not_self case
        if (rayTraceResult instanceof EntityHitResult entityHitResult) {
            // if we are in filter:not_self case, we want to cancel the spell if we're resolving on the caster
            if (entityHitResult.getEntity() == shooter && inverted) {
                spellContext.setCanceled(true);
            }
        }
        // we are not resolving on the caster, so we want to cancel the spell if we're in filter:self case
        else if (!inverted) {
            spellContext.setCanceled(true);
        }
    }

    @Override
    public String getBookDescription() {
        return "Stops the spell from resolving " + (inverted ? "if" : "unless") + " it targets the caster.";
    }

    @Override
    public Glyph getGlyph() {
        return super.getGlyph();
    }
}
