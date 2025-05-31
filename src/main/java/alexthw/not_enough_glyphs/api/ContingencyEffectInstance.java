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

    public ContingencyEffectInstance(SpellResolver spell, TRIGGER trigger, int duration) {
        super(Registry.CONTINGENCY, duration, 0, false, false);
        this.spell = spell;
        this.trigger = trigger;
    }

    public TRIGGER getTrigger() {
        return trigger;
    }

    public void triggerSpell(LivingEntity entity) {
        spell.onResolveEffect(entity.level(), new EntityHitResult(entity));
        entity.removeEffect(Registry.CONTINGENCY);
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource, float amount) {
        if (trigger == TRIGGER.HEROICS && (livingEntity.getHealth() <= livingEntity.getMaxHealth() * 0.25) || trigger == TRIGGER.ON_FIRE && damageSource.is(DamageTypeTags.IS_FIRE) || trigger == TRIGGER.ON_FALL && damageSource.is(DamageTypeTags.IS_FALL)) {
            triggerSpell(livingEntity);
        }
    }

    @Override
    public boolean update(@NotNull MobEffectInstance other) {
        return true;
    }

    @Override
    public boolean tick(@NotNull LivingEntity entity, @NotNull Runnable onExpirationRunnable) {
        if (trigger == TRIGGER.ON_FALL) {
            if (entity.fallDistance > 10) {
                triggerSpell(entity);
            }
        }
        return super.tick(entity, onExpirationRunnable);
    }

    public enum TRIGGER {
        ON_FALL,
        ON_FIRE,
        ON_HEAL,
        HEROICS,
        DEATH
    }

}
