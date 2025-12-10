package alexthw.not_enough_glyphs.init;

import alexthw.not_enough_glyphs.common.spell.BulldozeThread;
import alexthw.not_enough_glyphs.common.spell.FocusPerk;
import alexthw.not_enough_glyphs.common.spell.PacificThread;
import alexthw.not_enough_glyphs.common.spell.PounchThread;
import alexthw.not_enough_glyphs.common.spell.RandomPerk;
import alexthw.not_enough_glyphs.common.spell.SharpThread;
import com.hollingsworth.arsnouveau.api.documentation.ReloadDocumentationEvent;
import com.hollingsworth.arsnouveau.api.documentation.builder.DocEntryBuilder;
import com.hollingsworth.arsnouveau.api.documentation.entry.DocEntry;
import com.hollingsworth.arsnouveau.api.documentation.entry.TextEntry;
import com.hollingsworth.arsnouveau.api.registry.DocumentationRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;

import static com.hollingsworth.arsnouveau.setup.registry.Documentation.addBasicItem;

@EventBusSubscriber
public class NEGDocumentation {

    private static DocEntry addPage(DocEntryBuilder builder) {
        return DocumentationRegistry.registerEntry(builder.category, builder.build());
    }

    @SubscribeEvent
    public static void addDocumentation(ReloadDocumentationEvent.AddEntries event) {

        var book_threads = addPage(new DocEntryBuilder(DocumentationRegistry.SPELL_CASTING, "book_threads")
                .withIcon(PerkRegistry.getPerkItemMap().get(RandomPerk.INSTANCE.getRegistryName()))
                .withTextPage("not_enough_glyphs.page.book_threads.desc")
                .withCraftingPages(ItemsRegistry.BLANK_THREAD)
                .withPage(TextEntry.create(PacificThread.INSTANCE.getDescriptionKey(), PacificThread.INSTANCE.getName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(PacificThread.INSTANCE.getRegistryName()))
                .withPage(TextEntry.create(BulldozeThread.INSTANCE.getDescriptionKey(), BulldozeThread.INSTANCE.getName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(BulldozeThread.INSTANCE.getRegistryName()))
                .withPage(TextEntry.create(SharpThread.INSTANCE.getDescriptionKey(), SharpThread.INSTANCE.getName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(SharpThread.INSTANCE.getRegistryName()))
                .withPage(TextEntry.create(PounchThread.INSTANCE.getDescriptionKey(), PounchThread.INSTANCE.getName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(PounchThread.INSTANCE.getRegistryName()))
                .withPage(TextEntry.create(RandomPerk.INSTANCE.getDescriptionKey(), RandomPerk.INSTANCE.getName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(RandomPerk.INSTANCE.getRegistryName()))
                .withSortNum(101));

        DocEntry focus_threads;
        if (ModList.get().isLoaded("ars_elemental"))
            focus_threads = addPage(new DocEntryBuilder(DocumentationRegistry.SPELL_CASTING, "focus_threads")
                    .withIcon(PerkRegistry.getPerkItemMap().get(FocusPerk.MANIPULATION.getRegistryName()))
                    .withTextPage("ars_nouveau.page.focus_threads.desc")
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.SUMMONING.getRegistryName()))
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.MANIPULATION.getRegistryName()))
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.ELEMENTAL_FIRE.getRegistryName()))
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.ELEMENTAL_AIR.getRegistryName()))
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.ELEMENTAL_EARTH.getRegistryName()))
                    .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.ELEMENTAL_WATER.getRegistryName()))
                    .withSortNum(101));
        else focus_threads = addPage(new DocEntryBuilder(DocumentationRegistry.SPELL_CASTING, "focus_threads")
                .withIcon(PerkRegistry.getPerkItemMap().get(FocusPerk.MANIPULATION.getRegistryName()))
                .withTextPage("ars_nouveau.page.focus_threads.desc")
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.SUMMONING.getRegistryName()))
                .withCraftingPages(PerkRegistry.getPerkItemMap().get(FocusPerk.MANIPULATION.getRegistryName()))
                .withSortNum(101));

        addBasicItem(Registry.SPELL_BINDER.get(), DocumentationRegistry.SPELL_CASTING, 95).withRelations(book_threads, focus_threads);

    }

}
