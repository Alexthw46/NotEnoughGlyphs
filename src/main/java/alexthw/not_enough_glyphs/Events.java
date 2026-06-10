package alexthw.not_enough_glyphs;

import alexthw.not_enough_glyphs.common.glyphs.contingency.ContingencyTriggers;
import alexthw.not_enough_glyphs.init.Registry;
import com.alexthw.sauce.registry.ModRegistry;
import com.alexthw.sauce.util.ContingencyEffectInstance;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class Events {

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

    @SubscribeEvent
    public static void onHealTrigger(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ModRegistry.CONTINGENCY)) {
            if (event.getEntity().getEffect(ModRegistry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyTriggers.ON_HEAL) {
                cei.triggerSpell(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.getEffect(ModRegistry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyTriggers.DEATH) {
            cei.triggerSpell(entity);
        }
    }

    @SubscribeEvent
    public static void onDeathTotemEvent(LivingUseTotemEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.getEffect(ModRegistry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyTriggers.DEATH) {
            cei.triggerSpell(entity);
        }
    }

    @SubscribeEvent
    public static void onBlinkTrigger(EntityTeleportEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.hasEffect(ModRegistry.CONTINGENCY)) {
            if (entity.getEffect(ModRegistry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyTriggers.BLINK) {
                cei.triggerSpell(entity);
            }
        }
    }

    @SubscribeEvent
    public static void onCrushStuff(SpellDamageEvent.Pre event) {
        if (event.damageSource.is(DamageTypesRegistry.CRUSH)) {
            if (event.target instanceof LivingEntity entity && entity.level() instanceof ServerLevel server) {
                var stuff = entity.getEffect(Registry.STUFFED_EFFECT);
                if (stuff != null) {
                    if (entity.getHealth() / entity.getMaxHealth() < 0.25F) {
                        entity.kill();
                        server.sendParticles(ParticleTypes.EXPLOSION, entity.position().x, entity.position().y + 0.5, entity.position().z, 5,
                                ParticleUtil.inRange(-0.5, 0.5), ParticleUtil.inRange(-0.5, 0.5), ParticleUtil.inRange(-0.5, 0.5), 0.3);
                        for (Entity e : server.getEntities(event.caster, new AABB(
                                entity.position().add(4, 4, 4), entity.position().subtract(4, 4, 4)))) {
                            if (!(e instanceof LivingEntity living && living.getHealth() <= 0 || e.isAlliedTo(event.caster)))
                                e.hurt(event.damageSource, event.damage * (1.25F + 0.25F * stuff.getAmplifier()));
                        }
                    } else {
                        event.damage = event.damage * (1.25F + 0.25F * stuff.getAmplifier());
                    }
                }
            }
        }
    }
}