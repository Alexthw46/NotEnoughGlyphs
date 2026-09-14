package alexthw.not_enough_glyphs.common.glyphs.propagators;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.common.glyphs.forms.MethodMounted;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.alexthw.sauce.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.Map;

public class PropagateMounted extends AbstractEffect implements IPropagator {

    public static final PropagateMounted INSTANCE = new PropagateMounted();

    public PropagateMounted() {
        super(CompatRL.neg("propagate_mounted"), "Propagate Mounted");
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public String getBookDescription() {
        return "Takes the remainder of the spell and casts it on what the target is mounted on.";
    }

    @Override
    public void propagate(Level world, HitResult result, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        if (result instanceof EntityHitResult entityHitResult)
            MethodMounted.INSTANCE.castMounted(entityHitResult.getEntity(), stats, null, resolver);
        else
            MethodMounted.INSTANCE.castMounted(shooter, stats, null, resolver);
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentPierce.INSTANCE, "Includes mounted of mounted");
        map.put(AugmentSplit.INSTANCE, "Includes rider/s of rider");
        map.put(AugmentAOE.INSTANCE, "Includes rider/s.");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return MethodMounted.INSTANCE.getCompatibleAugments();
    }

    @Override
    public Glyph getGlyph() {
        if (glyphItem == null) {
            glyphItem = new Glyph(this) {
                @Override
                public @NotNull String getCreatorModId(@NotNull ItemStack itemStack) {
                    return NotEnoughGlyphs.MODNAME;
                }
            };
        }
        return this.glyphItem;
    }
}
