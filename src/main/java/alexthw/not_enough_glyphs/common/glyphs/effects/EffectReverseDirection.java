package alexthw.not_enough_glyphs.common.glyphs.effects;

import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.alexthw.sauce.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

import static alexthw.not_enough_glyphs.common.glyphs.CompatRL.tmg;

public class EffectReverseDirection extends AbstractEffect implements IPropagator {
    public static final EffectReverseDirection INSTANCE = new EffectReverseDirection("reverse_direction", "Redirect Placement");

    public EffectReverseDirection(String tag, String description) {
        super(tmg(tag), description);
    }

    @Override
    public String getBookDescription() {
        return "Reverses the direction of the spell, making it resolve as it was cast from the opposite side of the block. Especially useful with block-placing spells, as reversing the direction of pierce will point towards the caster instead of away.";
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.ONE;
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE, AugmentSensitive.INSTANCE);
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

    @Override
    public void propagate(Level world, HitResult result, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        HitResult reversedRayTraceResult;
        if (result instanceof BlockHitResult blockHitResult) {
            Direction direction = blockHitResult.getDirection();
            if (stats.isSensitive()) {
                // If sensitive, change axis
                direction = switch (direction) {
                    case UP, DOWN -> Direction.NORTH;
                    case NORTH, SOUTH, EAST, WEST -> Direction.UP;
                };
            }
            // Only works to cycle between the NSWE directions, as the vertical directions don't have a clockwise/counter-clockwise, and it makes no sense to double-negate
            if (direction.getAxis().isHorizontal()) {

                int ampMod = (int) stats.getAmpMultiplier();
                boolean counterClockwise = ampMod < 0;

                for (int i = 0; i < (counterClockwise ? 0 : 1) + Math.abs(ampMod); i++) {
                    direction = counterClockwise ? direction.getCounterClockWise() : direction.getClockWise();
                }
            }

            reversedRayTraceResult = blockHitResult
                    .withPosition(blockHitResult.isInside()
                            ? blockHitResult.getBlockPos()
                            : blockHitResult.getBlockPos().relative(direction).relative(direction))
                    // Relative adjustment of 2 required to get to the opposite side of the pivot block
                    .withDirection(direction.getOpposite());
        } else return; // Not supported

        resolver.onResolveEffect(world, reversedRayTraceResult);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentSensitive.INSTANCE, "Rotates the placement on a different axis.");
        map.put(AugmentDampen.INSTANCE, "Increases rotations counter-clockwise.");
        map.put(AugmentAmplify.INSTANCE, "Increases rotations clockwise.");
    }

    @Override
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
        defaults.put(AugmentDampen.INSTANCE.getRegistryName(), 2);
        defaults.put(AugmentSensitive.INSTANCE.getRegistryName(), 1);
        return super.getDefaultAugmentLimits(defaults);
    }
}
