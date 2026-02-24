package alexthw.not_enough_glyphs.common.spellbinder;

import alexthw.not_enough_glyphs.api.ThreadwiseSpellResolver;
import alexthw.not_enough_glyphs.init.Registry;
import com.google.common.collect.ImmutableMap;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BinderCasterData extends AbstractCaster<BinderCasterData> {

    public BinderCasterData(int maxSlots) {
        this(0, "", false, "", maxSlots);
    }

    public static final MapCodec<BinderCasterData> CODEC = SpellCaster.createCodec(BinderCasterData::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, BinderCasterData> STREAM_CODEC = createStream(BinderCasterData::new);


    @Override
    public MapCodec<BinderCasterData> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BinderCasterData> streamCodec() {
        return STREAM_CODEC;
    }

    public BinderCasterData(Integer slot, String flavorText, Boolean isHidden, String hiddenText, int maxSlots) {
        this(slot, flavorText, isHidden, hiddenText, maxSlots, new SpellSlotMap(ImmutableMap.of()));
    }

    public BinderCasterData(Integer slot, String flavorText, Boolean isHidden, String hiddenText, int maxSlots, SpellSlotMap spells) {
        super(slot, flavorText, isHidden, hiddenText, maxSlots, spells);
    }

    public static BinderCasterData create(int slot, String flavorText, Boolean isHidden, String hiddenText, int maxSlots) {
        return new BinderCasterData(slot, flavorText, isHidden, hiddenText, maxSlots);
    }

    public DataComponentType<BinderCasterData> getComponentType() {
        return Registry.SPELL_BINDER_CASTER.get();
    }

    @Override
    protected BinderCasterData build(int slot, String flavorText, Boolean isHidden, String hiddenText, int maxSlots, SpellSlotMap spells) {
        return new BinderCasterData(slot, flavorText, isHidden, hiddenText, maxSlots, spells);
    }

    @Override
    public SpellResolver getSpellResolver(SpellContext context, Level worldIn, LivingEntity playerIn, InteractionHand handIn) {
        return new ThreadwiseSpellResolver(context);
    }

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext pContext, @NotNull Consumer<Component> pTooltipAdder, @NotNull TooltipFlag pTooltipFlag) {
        if (getSpell().isEmpty()){
            pTooltipAdder.accept(Component.translatable("not_enough_glyphs.spell_binder.empty").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            return;
        }
        if (!getSpellName().isEmpty()) {
            pTooltipAdder.accept(Component.literal(getSpellName()));
        }
        if (isSpellHidden()) {
            pTooltipAdder.accept(Component.literal(getHiddenRecipe()).withStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("minecraft", "alt")).withColor(ChatFormatting.GOLD)));
        } else {
            Spell spell = getSpell();
            pTooltipAdder.accept(Component.literal(spell.getDisplayString()));
        }
        if (!getFlavorText().isEmpty())
            pTooltipAdder.accept(Component.literal(getFlavorText()).withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.BLUE)));
    }
}
