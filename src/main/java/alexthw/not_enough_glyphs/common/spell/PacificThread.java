package alexthw.not_enough_glyphs.common.spell;

import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import static alexthw.not_enough_glyphs.init.NotEnoughGlyphs.prefix;

public class PacificThread extends BookPerk {
    public PacificThread(ResourceLocation key) {
        super(key);
    }

    public static final PacificThread INSTANCE = new PacificThread(prefix("thread_cheap_damage"));


    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers
                .withModifierAdded(ModRegistry.MANA_DISCOUNT, new AttributeModifier(prefix("pacific_perk"), 50 * slotValue, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup)
                .withModifierAdded(PerkAttributes.SPELL_DAMAGE_BONUS, new AttributeModifier(prefix("pacific_perk"), -3 * slotValue, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
    }

}
