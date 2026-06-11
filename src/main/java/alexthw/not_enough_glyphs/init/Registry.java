package alexthw.not_enough_glyphs.init;

import alexthw.not_enough_glyphs.common.spell.MissileProjectile;
import alexthw.not_enough_glyphs.common.spell.TrailingProjectile;
import alexthw.not_enough_glyphs.common.spellbinder.BinderCasterData;
import alexthw.not_enough_glyphs.common.spellbinder.SpellBinder;
import alexthw.not_enough_glyphs.common.spellbinder.SpellBinderContainer;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.hollingsworth.arsnouveau.common.potions.PublicEffect;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static alexthw.not_enough_glyphs.init.NotEnoughGlyphs.prefix;


public class Registry {

    public static TagKey<EntityType<?>> RIDE_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, prefix("ride_blacklist"));

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NotEnoughGlyphs.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NotEnoughGlyphs.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, NotEnoughGlyphs.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, NotEnoughGlyphs.MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, NotEnoughGlyphs.MODID);
    public static final Supplier<EntityType<TrailingProjectile>> TRAILING_PROJECTILE;
    public static final Supplier<EntityType<MissileProjectile>> MISSILE_PROJECTILE;

    public static final Supplier<MenuType<SpellBinderContainer>> SPELL_HOLDER;
    public static final Supplier<Item> SPELL_BINDER;

    public static final DeferredHolder<MobEffect, MobEffect> GROWING_EFFECT = EFFECTS.register("grow", () -> new PublicEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.SCALE, prefix("effects.grow"), 0.5D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> SHRINKING_EFFECT = EFFECTS.register("shrink", () -> new PublicEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.SCALE, prefix("effects.shrink"), -0.2D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> STUFFED_EFFECT = EFFECTS.register("stuffed", () -> new PublicEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.SCALE, prefix("effects.stuffed"), 0.025D, AttributeModifier.Operation.ADD_VALUE).addAttributeModifier(PerkAttributes.WHIRLIESPRIG, prefix("effects.stuffed"), 0.05, AttributeModifier.Operation.ADD_VALUE));

    static {
        TRAILING_PROJECTILE = addEntity("trail", 0.5F, 0.5F, true, true, TrailingProjectile::new, MobCategory.MISC);
        MISSILE_PROJECTILE = addEntity("missile", 0.75F, 0.75F, true, true, MissileProjectile::new, MobCategory.MISC);

        SPELL_BINDER = ITEMS.register("spell_binder", () -> new SpellBinder(new Item.Properties().stacksTo(1)));

        SPELL_HOLDER = CONTAINERS.register("spell_holder", () -> IMenuTypeExtension.create((id, inv, extraData) -> {
            boolean mainHand = extraData.readBoolean();
            ItemStack stack = inv.player.getItemInHand(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            return new SpellBinderContainer(id, inv, stack);
        }));
    }

    static <T extends Entity> Supplier<EntityType<T>> addEntity(String name, float width, float height, boolean fire, boolean noSave, EntityType.EntityFactory<T> factory, MobCategory kind) {
        return ENTITIES.register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, kind)
                    .setTrackingRange(32)
                    .sized(width, height);
            if (noSave) {
                builder.noSave();
            }
            if (fire) {
                builder.fireImmune();
            }
            return builder.build(NotEnoughGlyphs.MODID + ":" + name);
        });
    }

    public static void init(IEventBus modbus) {
        ITEMS.register(modbus);
        ENTITIES.register(modbus);
        CONTAINERS.register(modbus);
        ATTRIBUTES.register(modbus);
        EFFECTS.register(modbus);
        DATA.register(modbus);
        modbus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTab() == CreativeTabRegistry.BLOCKS.get())
                for (var item : ITEMS.getEntries())
                    event.accept(item.get());
        });
    }

    public static final DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, NotEnoughGlyphs.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BinderCasterData>> SPELL_BINDER_CASTER = DATA.register("spell_binder_caster", () -> DataComponentType.<BinderCasterData>builder().persistent(BinderCasterData.CODEC.codec()).networkSynchronized(BinderCasterData.STREAM_CODEC).build());

}
