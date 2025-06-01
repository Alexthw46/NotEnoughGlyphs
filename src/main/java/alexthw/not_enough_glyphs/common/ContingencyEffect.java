package alexthw.not_enough_glyphs.common;

import alexthw.not_enough_glyphs.api.ContingencyEffectInstance;
import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.common.potions.PublicEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

public class ContingencyEffect extends PublicEffect {
    public ContingencyEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
        NeoForge.EVENT_BUS.addListener(ContingencyEffect::onHealTrigger);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, ContingencyEffect::onDeathEvent);
    }

    public static void onHealTrigger(LivingHealEvent event) {
        if (event.getEntity().hasEffect(Registry.CONTINGENCY)) {
            if (event.getEntity().getEffect(Registry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyEffectInstance.TRIGGER.ON_HEAL) {
                cei.triggerSpell(event.getEntity());
            }
        }
    }

    public static void onDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.getEffect(Registry.CONTINGENCY) instanceof ContingencyEffectInstance cei && cei.getTrigger() == ContingencyEffectInstance.TRIGGER.DEATH) {
            cei.triggerSpell(entity);
        }
    }
}
