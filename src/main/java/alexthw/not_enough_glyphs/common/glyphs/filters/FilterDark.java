package alexthw.not_enough_glyphs.common.glyphs.filters;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FilterDark extends AbstractEffectFilter {

    public static final FilterDark DARK = new FilterDark("filter_dark", "Filter: Dark");

    public FilterDark(String tag, String description) {
        super(CompatRL.neg(tag), description);
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult blockHitResult, Level level) {
        return level.getBrightness(LightLayer.BLOCK, blockHitResult.getBlockPos().above()) < 8;
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult entity, Level level) {
        return level.getBrightness(LightLayer.BLOCK, entity.getEntity().getOnPos()) < 8;
    }

    @Override
    String getDescriptionSegment() {
        return "a dark area. The light threshold for this glyph is 8 and ignores sunlight.";
    }
}
