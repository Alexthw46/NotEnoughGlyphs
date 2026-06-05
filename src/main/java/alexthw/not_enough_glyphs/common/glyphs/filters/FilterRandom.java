package alexthw.not_enough_glyphs.common.glyphs.filters;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class FilterRandom extends AbstractEffectFilter {

    public static final FilterRandom INSTANCE = new FilterRandom("filter_random", "Filter: Random");

    public FilterRandom(String tag, String description) {
        super(CompatRL.neg(tag), description);
    }

    @Override
    public String getBookDescription() {
        return "Has a 50% chance to stop the spell from resolving past this glyph.";
    }

    @Override
    String getDescriptionSegment() {
        return "";
    }

    @Override
    public boolean shouldAffect(HitResult rayTraceResult, Level level) {
        return level.random.nextFloat() < 0.5;
    }
}
