package alexthw.not_enough_glyphs.init;

import alexthw.ars_elemental.common.glyphs.MethodArcProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.common.glyphs.PropagatorArc;
import alexthw.ars_elemental.common.glyphs.PropagatorHoming;
import alexthw.not_enough_glyphs.common.spell.FocusPerk;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;

import java.util.List;

/**
 * Compat class that is only loaded when Ars Elemental is present.
 * Keeps Ars Elemental class references out of ArsNouveauRegistry to avoid
 * NoClassDefFoundError when Ars Elemental is not installed.
 */
public class ArsElementalCompatRegistry {

    public static void register() {
        ArsNouveauRegistry.registeredSpells.addAll(List.of(
                MethodArcProjectile.INSTANCE, MethodHomingProjectile.INSTANCE,
                PropagatorArc.INSTANCE, PropagatorHoming.INSTANCE)
        );
        PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_FIRE);
        PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_WATER);
        PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_EARTH);
        PerkRegistry.registerPerk(FocusPerk.ELEMENTAL_AIR);
    }
}
