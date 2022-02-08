package io.github.derringersmods.toomanyglyphs.init;

import io.github.derringersmods.toomanyglyphs.common.glyphs.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs() {
        register(EffectFilterBlock.INSTANCE);
        register(EffectFilterEntity.INSTANCE);
        register(EffectFilterLiving.INSTANCE);
        register(EffectFilterLivingNotMonster.INSTANCE);
        register(EffectFilterLivingNotPlayer.INSTANCE);
        register(EffectFilterMonster.INSTANCE);
        register(EffectFilterPlayer.INSTANCE);
        register(MethodLayOnHands.INSTANCE);
        register(EffectReverseDirection.INSTANCE);
        register(EffectChaining.INSTANCE);
        register(MethodRay.INSTANCE);
        register(EffectFilterItem.INSTANCE);
        register(EffectFilterAnimal.INSTANCE);
        register(EffectFilterIsBaby.INSTANCE);
        register(EffectFilterIsMature.INSTANCE);
        register(AugmentAmplifyTwo.INSTANCE);
        register(AugmentAmplifyThree.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart.tag, spellPart);
        registeredSpells.add(spellPart);
    }
}
