package alexthw.not_enough_glyphs.datagen;

import alexthw.not_enough_glyphs.init.Registry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static alexthw.not_enough_glyphs.init.NotEnoughGlyphs.MODID;


public class NEGEntityTagProvider extends EntityTypeTagsProvider {
    public NEGEntityTagProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator.getPackOutput(), provider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(Registry.RIDE_BLACKLIST);
    }
}
