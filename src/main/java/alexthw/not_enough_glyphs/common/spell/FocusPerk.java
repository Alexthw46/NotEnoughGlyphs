package alexthw.not_enough_glyphs.common.spell;

import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.resources.ResourceLocation;

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
    public void onEffectPreResolve(EffectResolveEvent.Pre event, PerkInstance perkInstance) {
        if (perkInstance.getSlot().value() > 0 && school.isPartOfSchool(event.resolveEffect)) {
            event.spellStats.setDamageModifier(event.spellStats.getDamageModifier() + 2 * perkInstance.getSlot().value());
        }
    }
}
