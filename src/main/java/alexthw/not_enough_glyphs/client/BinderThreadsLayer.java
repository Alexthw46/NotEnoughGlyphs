package alexthw.not_enough_glyphs.client;

import alexthw.not_enough_glyphs.common.spellbinder.SpellBinder;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.items.PerkItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;

public class BinderThreadsLayer extends GeoRenderLayer<SpellBinder> {
    public BinderThreadsLayer(SpellBinderRenderer renderer) {
        super(renderer);
    }


    @Override
    public void render(PoseStack poseStack, SpellBinder animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        bakedModel.getBone("left_cover_3").ifPresent((b) -> {
            poseStack.pushPose();
            poseStack.scale(0.3F, 0.3F, 0.3F);
            poseStack.translate(0.65f, 2.1, 0.58F);
            poseStack.rotateAround(new Quaternionf().add(0F, 0.975f, 0, 0), 0, 0, 0);
            List<PerkItem> perks = PerkUtil.getPerksAsItems(((SpellBinderRenderer) renderer).getCurrentItemStack());
            for (int i = 0; i < perks.size(); i++) {
                poseStack.pushPose();
                // Adjust perk placemen
                poseStack.translate(0, i == 1 ? -0.8 : 0, 0);
                poseStack.rotateAround(new Quaternionf().add(0, 0, -0.025F, 0), 0, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderStatic(perks.get(i).getDefaultInstance(), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, ArsNouveau.proxy.getClientWorld(), 0);
                poseStack.popPose();
            }

            poseStack.popPose();
        });

        bakedModel.getBone("left_cover3").ifPresent((b) -> {
                    var mat = b.getLocalSpaceMatrix();
                    poseStack.pushPose();
                    poseStack.mulPose(mat);
                    List<PerkItem> perks = PerkUtil.getPerksAsItems(((SpellBinderRenderer) renderer).getCurrentItemStack());
                    for (int i = 0, perksSize = perks.size(); i < perksSize; i++) {
                        PerkItem perk = perks.get(i);
                        poseStack.pushPose();
                        poseStack.translate(-0.265, i == 1 ? -0.3 : 0.1, -0.31);
                        poseStack.scale(.3f, .3f, .3f);
                        poseStack.rotateAround(new Quaternionf().add(0, .225F, 0, 0), 0, 0, 0);
                        Minecraft.getInstance().getItemRenderer().renderStatic(perk.getDefaultInstance(), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, ArsNouveau.proxy.getClientWorld(), 0);
                        poseStack.popPose();
                    }
                    poseStack.popPose();
                }
        );
    }
}
