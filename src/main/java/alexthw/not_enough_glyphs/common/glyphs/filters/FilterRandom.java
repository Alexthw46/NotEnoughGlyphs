package alexthw.not_enough_glyphs.common.glyphs.filters;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractFilter;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class FilterRandom extends AbstractFilter {
    public static final FilterRandom INSTANCE = new FilterRandom(CompatRL.controle("filter_random"), "Random Filter");

    static final double BASE_CHANCE = 0.5D;
    public double chance = BASE_CHANCE;

    public FilterRandom(ResourceLocation registryName, String name) {
        super(registryName, name);
    }

    public static double calculateChance(double amps) {
        return switch (Double.compare(amps, 0.0)) {
            case 1 -> 1.0 - BASE_CHANCE / Math.pow(2, amps);
            case -1 -> BASE_CHANCE / Math.pow(2, -amps);
            default -> BASE_CHANCE;
        };
    }

    @Override
    public String getBookDescription() {
        return "Has a base 50% chance of resolving. If amplified overall, chance will be (100% - 50% / (2 ^ Amplification)). If dampened overall, chance will be (50% / (2 ^ Dampening)).";
    }

    @Override
    public Integer getTypeIndex() {
        return 15;
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target, Level level) {
        return this.shouldResolve();
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level) {
        return this.shouldResolve();
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.chance = calculateChance(spellStats.getAmpMultiplier());
        super.onResolveEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.chance = calculateChance(spellStats.getAmpMultiplier());
        super.onResolveBlock(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    public boolean shouldResolve() {
        return ThreadLocalRandom.current().nextDouble() <= this.chance;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }

    @Override
    protected void addAugmentCostOverrides(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 0);
        defaults.put(AugmentDampen.INSTANCE.getRegistryName(), 0);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAmplify.INSTANCE, "Increases the chance of resolving");
        map.put(AugmentDampen.INSTANCE, "Decreases the chance of resolving");
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