package alexthw.not_enough_glyphs.common.spell;

import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import static alexthw.not_enough_glyphs.init.NotEnoughGlyphs.prefix;

public class SpellCritChancePerk extends BookPerk {
    public static final SpellCritChancePerk INSTANCE = new SpellCritChancePerk(ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "thread_scritchance"));

    public SpellCritChancePerk(ResourceLocation key) {
        super(key);
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return modifiers.withModifierAdded(ModRegistry.SPELL_CRIT, new AttributeModifier(prefix("scritchance_perk"), 10 * slotValue, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
    }
}
