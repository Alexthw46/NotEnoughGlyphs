package alexthw.not_enough_glyphs.common.spell;

import com.alexthw.sauce.event.AttributeEventHandler;
import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import static com.hollingsworth.arsnouveau.ArsNouveau.prefix;

public class FocusPerk extends BookPerk implements IEffectResolvePerk {
    public static final FocusPerk MANIPULATION = new FocusPerk(prefix("thread_shaper_focus"), SpellSchools.MANIPULATION);
    public static final FocusPerk SUMMONING = new FocusPerk(prefix("thread_summon_focus"), SpellSchools.CONJURATION);

    public static final FocusPerk ELEMENTAL_FIRE = new FocusPerk(prefix("thread_fire_focus"), SpellSchools.ELEMENTAL_FIRE);
    public static final FocusPerk ELEMENTAL_WATER = new FocusPerk(prefix("thread_water_focus"), SpellSchools.ELEMENTAL_WATER);
    public static final FocusPerk ELEMENTAL_EARTH = new FocusPerk(prefix("thread_earth_focus"), SpellSchools.ELEMENTAL_EARTH);
    public static final FocusPerk ELEMENTAL_AIR = new FocusPerk(prefix("thread_air_focus"), SpellSchools.ELEMENTAL_AIR);


    public final SpellSchool school;

    public FocusPerk(ResourceLocation key, SpellSchool school) {
        super(key);
        this.school = school;
    }

    @Override
    public @NotNull ItemAttributeModifiers applyAttributeModifiers(ItemAttributeModifiers modifiers, ItemStack stack, int slotValue, EquipmentSlotGroup equipmentSlotGroup) {
        return super.applyAttributeModifiers(modifiers, stack, slotValue, equipmentSlotGroup).withModifierAdded(AttributeEventHandler.schoolToPowerAttribute.get(this.school), new AttributeModifier(this.getRegistryName(), 2 * slotValue, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
    }

}
