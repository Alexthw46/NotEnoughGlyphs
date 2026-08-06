package alexthw.not_enough_glyphs.common.glyphs.forms;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.CastResolveType;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MethodMounted extends AbstractCastMethod {

    public static final MethodMounted INSTANCE = new MethodMounted();

    public MethodMounted() {
        super(CompatRL.neg("mounted"), "mounted");
    }

    @Override
    public String getName() {
        return "Mounted";
    }

    @Override
    public String getBookDescription() {
        return "Targets what the rider is mounted on.";
    }

    public CastResolveType castMounted(Entity target, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver)
    {
        Level my_level = target.getCommandSenderWorld();
        Entity vehicle = target.getVehicle();
        double aeo = spellStats.getAoeMultiplier();
        int pierce = spellStats.getBuffCount(AugmentPierce.INSTANCE);
        int split = spellStats.getBuffCount(AugmentSplit.INSTANCE);

        if (vehicle != null)
        {
            if (aeo > 0)
            {
                resolver.onResolveEffect(my_level, new EntityHitResult(target)); // ensures the target is the first to be hit
                aeo -= 1;
                List<Entity> passengers = new LinkedList<Entity>();
                passengers.addAll(vehicle.getPassengers());
                while(!(aeo <= 0 || passengers.isEmpty()))
                {
                    if (passengers.get(0) != target)
                    {
                        resolver.onResolveEffect(my_level, new EntityHitResult(passengers.get(0)));
                        aeo -= 1;
                    }
                    passengers.remove(0);
                }
            }
            if (pierce > 0)
            {
                Entity sub_vehicle = vehicle.getVehicle();
                while(pierce > 0 && sub_vehicle != null)
                {
                    resolver.onResolveEffect(my_level, new EntityHitResult(sub_vehicle));
                    sub_vehicle = sub_vehicle.getVehicle();
                    pierce -= 1;
                }
            }
            if (split > 0) // this could get out of hand quickly... hopefully never...
            {
                List<Entity> sub_riders = new LinkedList<Entity>();
                sub_riders.addAll(target.getPassengers());
                List<Entity> sub_sub_riders = new LinkedList<Entity>();
                while(split > 0 && !sub_riders.isEmpty())
                {
                    for(Entity e : sub_riders)
                    {
                        resolver.onResolveEffect(my_level, new EntityHitResult(e));
                        sub_sub_riders.addAll(e.getPassengers());
                    }
                    sub_riders.clear();
                    sub_riders.addAll(sub_sub_riders);
                    sub_sub_riders.clear();
                    split -= 1;
                }
            }

            resolver.onResolveEffect(my_level, new EntityHitResult(vehicle));
        }

        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCast(@Nullable ItemStack stack, LivingEntity caster, Level world, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return castMounted(caster, spellStats, spellContext, resolver);
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        LivingEntity caster = context.getPlayer();
        return castMounted(caster, spellStats, spellContext, resolver);
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return castMounted(caster, spellStats, spellContext, resolver);
    }

    @Override
    public CastResolveType onCastOnEntity(@Nullable ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return castMounted(target, spellStats, spellContext, resolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 5;
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentPierce.INSTANCE, "Includes mounted of mounted");
        map.put(AugmentSplit.INSTANCE, "Includes rider/s of rider");
        map.put(AugmentAOE.INSTANCE, "Includes rider/s.");
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAOE.INSTANCE);
    }

    @Override
    public Glyph getGlyph() {
        if (glyphItem == null) {
            glyphItem = new Glyph(this) {
                @Override
                public @NotNull String getCreatorModId(@NotNull ItemStack itemStack) {
                    return NotEnoughGlyphs.MODNAME;
                }
            };
        }
        return this.glyphItem;
    }
}
