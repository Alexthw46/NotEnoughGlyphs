package alexthw.not_enough_glyphs.common.spell;

import com.alexthw.sauce.registry.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import static alexthw.not_enough_glyphs.init.NotEnoughGlyphs.prefix;

public class SpellCritDamagePerk extends BookPerk {

    public static final SpellCritDamagePerk INSTANCE = new SpellCritDamagePerk(prefix("thread_scritdamage"));

    public SpellCritDamagePerk(ResourceLocation key) {
        super(key);
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers.withModifierAdded(ModRegistry.SPELL_CRIT_DAMAGE, new AttributeModifier(prefix("scritdamage_perk"), 0.25 * slotValue, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
    }

}

