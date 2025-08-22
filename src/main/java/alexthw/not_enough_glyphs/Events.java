package alexthw.not_enough_glyphs;

import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import org.jetbrains.annotations.NotNull;

public class Events {

    public static void registerListeners(IEventBus modbus, IEventBus forgebus) {
        modbus.addListener(Events::attachCaps);
    }

    @SubscribeEvent
    public static void attachCaps(final RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, DataComponents.CONTAINER, 25) {
            @Override
            protected void onContentsChanged(int slot, @NotNull ItemStack oldStack, @NotNull ItemStack newStack) {
                super.onContentsChanged(slot, oldStack, newStack);
                AbstractCaster<?> caster = SpellCasterRegistry.from(newStack);
                Spell spell = caster == null ? new Spell() : caster.getSpell();
                AbstractCaster<?> binderCaster = SpellCasterRegistry.from((ItemStack) this.parent);
                assert binderCaster != null;
                binderCaster.setSpell(spell, slot).saveToStack((ItemStack) this.parent);
            }
        }, Registry.SPELL_BINDER.get());
    }
}
