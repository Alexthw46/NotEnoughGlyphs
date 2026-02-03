package alexthw.not_enough_glyphs.api;

import alexthw.not_enough_glyphs.init.Registry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ContingencyEffectInstance extends MobEffectInstance {

    protected final TRIGGER trigger;
    SpellResolver spell;
    double amplifier;
    int activations, max_activations;

    public ContingencyEffectInstance(SpellResolver spell, TRIGGER trigger, int duration, double amplifier, int max_activations) {
        super(Registry.CONTINGENCY, duration, 0, false, false);
        this.spell = spell;
        this.trigger = trigger;
        this.amplifier = amplifier;
        this.activations = 0;
        this.max_activations = max_activations; // Set the maximum activations, first not counted
    }

    public TRIGGER getTrigger() {
        return trigger;
    }

    public void triggerSpell(LivingEntity entity) {
        if (activations <= max_activations) {
            spell.onResolveEffect(entity.level(), new EntityHitResult(entity));
            activations++;
        }
    }


    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource, float amount) {
        if (trigger == TRIGGER.HEROICS && (livingEntity.getHealth() <= livingEntity.getMaxHealth() * (2 + amplifier) / 10) || trigger == TRIGGER.ON_FIRE && damageSource.is(DamageTypeTags.IS_FIRE)) {
            triggerSpell(livingEntity);
        }
    }

    @Override
    public boolean update(@NotNull MobEffectInstance other) {
        return true;
    }

    @Override
    public boolean tick(@NotNull LivingEntity entity, @NotNull Runnable onExpirationRunnable) {
        if (entity.level().isClientSide) return super.tick(entity, onExpirationRunnable);
        if (trigger == TRIGGER.ON_FALL) {
            if (entity.fallDistance > 5 + amplifier) {
                triggerSpell(entity);
            }
        }

        // chain the runnable with an additional method to trigger the spell if the trigger is EXPIRE
        Runnable newExpirationRunnable = () -> {
            if (trigger == TRIGGER.EXPIRE) {
                triggerSpell(entity);
                if (activations > max_activations) {
                    onExpirationRunnable.run();
                }
            } else onExpirationRunnable.run();
        };

        // Remove the effect if max activations reached by returning false
        return super.tick(entity, newExpirationRunnable) && activations <= max_activations;
    }

    public enum TRIGGER {
        ON_FALL,
        ON_FIRE,
        ON_HEAL,
        HEROICS,
        BLINK,
        DEATH,
        EXPIRE
    }

}
