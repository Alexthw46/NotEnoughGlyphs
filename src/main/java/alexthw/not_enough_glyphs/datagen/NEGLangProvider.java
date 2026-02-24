package alexthw.not_enough_glyphs.datagen;


import alexthw.ars_elemental.ArsElemental;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.alexthw.sauce.Sauce;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.hollingsworth.arsnouveau.common.items.PerkItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class NEGLangProvider extends LanguageProvider {

    public NEGLangProvider(PackOutput output, String mod_id, String locale) {
        super(output, mod_id, locale);
    }

    @Override
    protected void addTranslations() {

        add("item.not_enough_glyphs.spell_binder", "Spell Binder");
        add("ars_nouveau.spell_binder.empty", "Add spell parchments and caster tomes in the binder by opening its inventory.");
        add("ars_nouveau.spell_binder.open", "Press %s to open the inventory");
        add("ars_nouveau.book_thread", "Book Thread : %s");
        add("ars_nouveau.book_slot", "Book Thread Slots");
        add("item.ars_nouveau.thread_summon_focus", "Summoning Focus");
        add("ars_nouveau.perk_desc.thread_summon_focus", "Thread for the spellbinder. Will enable glyph combos as if the summoning focus was equipped.");
        add("item.ars_nouveau.thread_shaper_focus", "BlockShaping Focus");
        add("ars_nouveau.perk_desc.thread_shaper_focus", "Thread for the spellbinder. Will enable glyph combos as if the block shaping focus was equipped.");
        add("item.ars_nouveau.thread_fire_focus", "Fire Focus");
        add("ars_nouveau.perk_desc.thread_fire_focus", "Thread for the spellbinder. Will enable glyph combos as if the fire focus was equipped.");
        add("item.ars_nouveau.thread_earth_focus", "Earth Focus");
        add("ars_nouveau.perk_desc.thread_earth_focus", "Thread for the spellbinder. Will enable glyph combos as if the earth focus was equipped.");
        add("item.ars_nouveau.thread_water_focus", "Water Focus");
        add("ars_nouveau.perk_desc.thread_water_focus", "Thread for the spellbinder. Will enable glyph combos as if the water focus was equipped.");
        add("item.ars_nouveau.thread_air_focus", "Air Focus");
        add("ars_nouveau.perk_desc.thread_air_focus", "Thread for the spellbinder. Will enable glyph combos as if the air focus was equipped.");
        add("item.ars_nouveau.thread_wild_magic", "Wheel of Fortune");
        add("ars_nouveau.perk_desc.thread_wild_magic", "Thread for the spellbinder. Will randomly add positive augments to effects of your spells when equipped.");
        add("item.ars_nouveau.thread_cheap_damage", "Cheap Damage");
        add("ars_nouveau.perk_desc.thread_cheap_damage", "Thread for the spellbinder. Will greatly discount the spells cast from the equipped book but heavily reduce their damage.");
        add("item.ars_nouveau.thread_slow_power", "Slow Power");
        add("ars_nouveau.perk_desc.thread_slow_power", "Thread for the spellbinder. Will increase the damage of the spells cast from the equipped book but heavily reduce their speed.");
        add("item.ars_nouveau.thread_sharp_paper", "Sharp Pages");
        add("ars_nouveau.perk_desc.thread_sharp_paper", "Thread for the spellbinder. Will increase the melee damage of the spellbinder if used as a weapon.");
        add("item.ars_nouveau.thread_knockback", "Heavy Cover");
        add("ars_nouveau.perk_desc.thread_knockback", "Thread for the spellbinder. Will increase the knockback of the spellbinder if used as a melee weapon.");
        add("item.ars_nouveau.thread_scritchance", "Lucky Cover");
        add("ars_nouveau.perk_desc.thread_scritchance", "Thread for the spellbinder. Will increase the critical chance of spells.");
        add("item.ars_nouveau.thread_scritdamage", "Keen Cover");
        add("ars_nouveau.perk_desc.thread_scritdamage", "Thread for the spellbinder. Will increase the critical damage of spells.");


        add("not_enough_glyphs.perk.mana_discount", "Mana Discount");
        add("not_enough_glyphs.perk.mana_discount.desc", "Reduces the mana cost of the spell by %s.");
        add("ars_nouveau.page.focus_threads", "Book Thread: Focus");
        add("ars_nouveau.page.focus_threads.desc", "Having these threads on the spellbinder will allow to unlock the glyph combos as if the corresponding focus was equipped. In a level 2 slot, they will give a small damage bonus to the spells from the matching school.");
        add("not_enough_glyphs.page.spell_binder", "An alternative to the traditional spellcasting, the spellbinder is a tool that allows to cast spells from the spell parchemnts and caster tomes put inside it. The left side will hold the 10 spells for the radial menu (V), while the right side allow to keep additional 15 spells to switch when needed.");
        add("ars_nouveau.page.book_threads", "Book Threads");
        add("not_enough_glyphs.page.book_threads.desc", "The book threads are at the core of the spellbinder. They allow to give your binder special properties and effects, similar to how Armor Threads work. You can use an Alteration Table to slot up to two threads into the Spell Binder.");

        add("effect.not_enough_glyphs.shrink", "Shrinking");
        add("effect.not_enough_glyphs.shrink.desc", "Shrinks down the entity.");
        add("effect.not_enough_glyphs.grow", "Growing");
        add("effect.not_enough_glyphs.grow.desc", "Increases the entity's size.");

        add("ars_nouveau.particle_config.ray", "Ray");

        for (Supplier<Glyph> supplier : GlyphRegistry.getGlyphItemMap().values()) {
            Glyph glyph = supplier.get();
            AbstractSpellPart spellPart = glyph.spellPart;
            ResourceLocation registryName = glyph.spellPart.getRegistryName();
            if (!registryName.getNamespace().equals(ArsNouveau.MODID) && !registryName.getNamespace().equals(Sauce.MODID)) {

                if (registryName.getNamespace().equals(ArsElemental.MODID)) {
                    // if the glyph is not one of the projectile we provide, we skip it
                    if (!registryName.getPath().contains("projectile") && !registryName.getPath().contains("propagator")) {
                        continue;
                    }
                }

                add(registryName.getNamespace() + ".glyph_desc." + registryName.getPath(), spellPart.getBookDescription());
                add(registryName.getNamespace() + ".glyph_name." + registryName.getPath(), spellPart.getName());

                Map<AbstractAugment, String> augmentDescriptions = new HashMap<>();
                spellPart.addAugmentDescriptions(augmentDescriptions);

                for (AbstractAugment augment : augmentDescriptions.keySet()) {
                    add("ars_nouveau.augment_desc." + registryName.getPath() + "_" + augment.getRegistryName().getPath(), augmentDescriptions.get(augment));
                }
            }
        }

        for (PerkItem i : PerkRegistry.getPerkItemMap().values()) {
            if (i.perk.getRegistryName().getNamespace().equals(NotEnoughGlyphs.MODID) && !i.perk.getRegistryName().getPath().equals("blank_thread")) {
                add("not_enough_glyphs.perk_desc." + i.perk.getRegistryName().getPath(), i.perk.getLangDescription());
                add("item.not_enough_glyphs." + i.perk.getRegistryName().getPath(), i.perk.getLangName());
            }
        }
    }
}