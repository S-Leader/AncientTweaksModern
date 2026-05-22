package com.keletu.ancienttweaks.event;

import com.keletu.ancienttweaks.init.ATItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;

public class DrownedJellyLootModifier extends LootModifier {

    public static final Codec<DrownedJellyLootModifier> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, DrownedJellyLootModifier::new));

    protected DrownedJellyLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        if (!(entity instanceof Drowned)) {
            return generatedLoot;
        }

        if (context.getRandom().nextFloat() < 0.03F) {
            Item selectedItem = switch (context.getRandom().nextInt(3)) {
                case 0 -> ATItems.blueJelly.get();
                case 1 -> ATItems.pinkJelly.get();
                default -> ATItems.greenJelly.get();
            };

            generatedLoot.add(new ItemStack(selectedItem));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends LootModifier> codec() {
        return CODEC;
    }
}
