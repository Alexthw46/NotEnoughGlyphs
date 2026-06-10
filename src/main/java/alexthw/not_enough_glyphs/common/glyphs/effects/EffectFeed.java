package alexthw.not_enough_glyphs.common.glyphs.effects;

import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.item.inv.InventoryManager;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentRandomize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static alexthw.not_enough_glyphs.common.glyphs.CompatRL.neg;

public class EffectFeed extends AbstractEffect {

    public static final EffectFeed INSTANCE = new EffectFeed(neg("feed"), "Feed");

    public EffectFeed(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Force feeds the target, consuming a food item from the caster's inventory. Doesn't work on undead. The use of this glyph will cause the Stuffed effect, making the eater more vulnerable to crush and eventually explode if crushed at low health.";
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // Blacklist constructs ?
        if (rayTraceResult.getEntity() instanceof LivingEntity toFeed && (!toFeed.getType().is(EntityTypeTags.UNDEAD))) {
            InventoryManager manager = spellContext.getCaster().getInvManager();

            //Find a food item in the inventory
            var food = spellStats.isRandomized() ? manager.extractRandomItem(i -> !i.isEmpty() && i.getFoodProperties(toFeed) != null, 1) : manager.extractItem(i -> i.getFoodProperties(toFeed) != null, 1);
            MobEffectInstance stuffedEffect = toFeed.getEffect(Registry.STUFFED_EFFECT);
            int foodXplosionLevel = stuffedEffect != null ? stuffedEffect.getAmplifier() : 0;
            var foodData = food.getStack().getFoodProperties(toFeed);
            if (foodData == null) return;
            if (toFeed instanceof Player player) {
                // if it's a player, check the hunger situation first
                // boost the stuffing if they're already full
                var hunger = player.getFoodData().getFoodLevel();
                if (hunger > 18 || hunger + foodData.nutrition() >= 20) {
                    foodXplosionLevel += Math.ceilDiv(foodData.nutrition(), 5);
                }
            } else {
                foodXplosionLevel += Math.ceilDiv(foodData.nutrition(), 10);
            }
            toFeed.eat(world, food.getStack());
            toFeed.addEffect(new MobEffectInstance(Registry.STUFFED_EFFECT, 400, Math.min(foodXplosionLevel, 9)));
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 30;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.CONJURATION);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentRandomize.INSTANCE);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentRandomize.INSTANCE, "Randomizes the food selection instead of picking the first valid.");
    }
}
