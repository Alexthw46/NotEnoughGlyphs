package alexthw.not_enough_glyphs.client;


import alexthw.not_enough_glyphs.common.spellbinder.SpellBinder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpellBinderRenderer extends GeoItemRenderer<SpellBinder> {
    public GeoModel<SpellBinder> closedModel;

    public SpellBinderRenderer() {
        super(new SpellBinderModel(SpellBinderModel.OPEN));
        this.closedModel = new SpellBinderModel(SpellBinderModel.CLOSED);
        addRenderLayer(new BinderThreadsLayer(this));
    }

    @Override
    public GeoModel<SpellBinder> getGeoModel() {
        if (renderPerspective == ItemDisplayContext.GUI)
            return closedModel;
        return super.getGeoModel();
    }

    public ResourceLocation getTextureLocation(SpellBinder o) {
        String base = "textures/item/spell_binder_";
        var dyeColor = currentItemStack.get(DataComponents.BASE_COLOR);
        String color = dyeColor == null ? "purple" : dyeColor.getName();
        return ResourceLocation.fromNamespaceAndPath("not_enough_glyphs", base + color + ".png");
    }

}
