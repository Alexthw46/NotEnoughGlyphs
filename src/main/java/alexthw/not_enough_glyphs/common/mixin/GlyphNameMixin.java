package alexthw.not_enough_glyphs.common.mixin;

import alexthw.not_enough_glyphs.common.glyphs.RepackedGlyph;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Glyph.class)
public abstract class GlyphNameMixin {

    @Shadow
    public AbstractSpellPart spellPart;

    @Inject(method = "getCreatorModId", at = @At("HEAD"), cancellable = true)
    public void getAltCreatorModId(ItemStack itemStack, CallbackInfoReturnable<String> cir) {
        if (this.spellPart instanceof RepackedGlyph adopted) {
            cir.setReturnValue(adopted.getMaintainerModId());
        }
    }

}
