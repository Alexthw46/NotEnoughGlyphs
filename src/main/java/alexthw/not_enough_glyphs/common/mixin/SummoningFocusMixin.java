package alexthw.not_enough_glyphs.common.mixin;

import alexthw.not_enough_glyphs.common.spell.FocusPerk;
import alexthw.not_enough_glyphs.common.spellbinder.SpellBinder;
import com.hollingsworth.arsnouveau.common.items.curios.SummoningFocus;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SummoningFocus.class)
public class SummoningFocusMixin {

    @ModifyReturnValue(method = "containsThis", at = @At("TAIL"))
    private static boolean containsThis(boolean original, Level world, Entity entity) {
        if (!original && entity instanceof LivingEntity caster) {
            return SpellBinder.getPerkInstanceFromHands(FocusPerk.SUMMONING, caster) != null;
        }
        return original;
    }

}
