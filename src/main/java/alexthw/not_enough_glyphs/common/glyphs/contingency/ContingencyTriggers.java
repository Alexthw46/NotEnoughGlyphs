package alexthw.not_enough_glyphs.common.glyphs.contingency;

import com.alexthw.sauce.util.ContingencyEffectInstance;
import com.alexthw.sauce.util.ContingencyEffectInstance.ContingencyTrigger;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ContingencyTriggers {

    public static final ContingencyTrigger ON_FALL = new ContingencyTrigger() {
        @Override
        public boolean onTick(ContingencyEffectInstance inst, LivingEntity entity) {
            if (entity.fallDistance > 5 + inst.getAmplifier()) {
                inst.triggerSpell(entity);
            }
            return true;
        }
    };
    public static final ContingencyTrigger ON_FIRE = new ContingencyTrigger() {
        @Override
        public void onMobHurt(ContingencyEffectInstance instance,
                              LivingEntity entity,
                              DamageSource source,
                              float amount) {

            if (source.is(DamageTypeTags.IS_FIRE)) {
                instance.triggerSpell(entity);
            }
        }
    };
    public static final ContingencyTrigger HEROICS = new ContingencyTrigger() {

        @Override
        public void onMobHurt(ContingencyEffectInstance instance,
                              LivingEntity entity,
                              DamageSource source,
                              float amount) {

            double threshold = entity.getMaxHealth()
                    * (2 + instance.getAmplifier()) / 10.0;

            if (entity.getHealth() <= threshold) {
                instance.triggerSpell(entity);
            }

        }

    };
    public static final ContingencyTrigger ON_HEAL = new ContingencyTrigger() {
    };
    public static final ContingencyTrigger BLINK = new ContingencyTrigger() {
    };
    public static final ContingencyTrigger DEATH = new ContingencyTrigger() {
    };

}
