package alexthw.not_enough_glyphs.init;

import alexthw.not_enough_glyphs.ClientStuff;
import com.alexthw.sauce.Sauce;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(NotEnoughGlyphs.MODID)
public class NotEnoughGlyphs {

    public static final String MODID = "not_enough_glyphs";
    public static final String MODNAME = "Not Enough Glyphs";

    public NotEnoughGlyphs(IEventBus modEventBus, ModContainer modContainer) {
        Sauce.ENABLE_SPELL_CRIT = true;
        Registry.init(modEventBus);
        ArsNouveauRegistry.registerGlyphs();
        modEventBus.addListener(Networking::register);
        modEventBus.addListener(this::registerItems);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        //NeoForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(ClientStuff.class);
        }

    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void registerItems(final RegisterEvent event) {
        // Perk-items are registered here (deterministic, single-threaded) instead of via Ars Nouveau's perk-map sweep,
        // which was being populated from the parallel mod constructor. See ArsNouveauRegistry#registerPerks.
        event.register(Registries.ITEM, ArsNouveauRegistry::registerPerks);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ArsNouveauRegistry::postInit);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

}
