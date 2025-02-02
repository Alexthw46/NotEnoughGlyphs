package alexthw.not_enough_glyphs.datagen;

import alexthw.not_enough_glyphs.common.spell.BookPerk;
import alexthw.not_enough_glyphs.init.NotEnoughGlyphs;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NEGItemModels extends ItemModelProvider {
    public NEGItemModels(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator.getPackOutput(), NotEnoughGlyphs.MODID, fileHelper);
    }

    @Override
    protected void registerModels() {
        PerkRegistry.getPerkItemMap().values().forEach(perk -> {
            if (!(perk.perk instanceof BookPerk)) return;
            doubleLayer(perk);
            justIcon(perk);
        });
    }

    private static final ResourceLocation GENERATED = ResourceLocation.withDefaultNamespace("item/generated");

    private ItemModelBuilder justIcon(Item i) {
        String name = BuiltInRegistries.ITEM.getKey(i).getPath();
        return withExistingParent(name + "_icon", GENERATED).texture("layer0", ArsNouveau.prefix("item/" + name));
    }

    private ItemModelBuilder doubleLayer(Item i) {
        String name = BuiltInRegistries.ITEM.getKey(i).getPath();
        return withExistingParent(name, GENERATED).texture("layer0", ArsNouveau.prefix("item/blank_book_thread")).texture("layer1", ArsNouveau.prefix("item/" + name));
    }

}
