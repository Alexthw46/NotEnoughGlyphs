package alexthw.not_enough_glyphs.common.glyphs;

import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;

public interface RepackedGlyph {

    default String getMaintainerModId() {
        return NotEnoughGlyphs.MODID;
    }
}
