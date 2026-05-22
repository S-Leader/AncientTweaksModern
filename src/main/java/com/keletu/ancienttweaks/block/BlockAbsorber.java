package com.keletu.ancienttweaks.block;

import com.keletu.ancienttweaks.baubles.ItemTheAbsorber;
import com.keletu.ancienttweaks.init.ATRecipes;
import com.keletu.ancienttweaks.recipe.AbsorberRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Optional;

public class BlockAbsorber extends Block {
    public static final int MAX_DURATION = 3;
    public static final IntegerProperty DURATION = IntegerProperty.create("duration", 0, 3);

    public BlockAbsorber(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DURATION, MAX_DURATION));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DURATION);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (heldStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        int duration = state.getValue(DURATION);

        if (duration <= 0) {
            level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            level.destroyBlock(pos, true);
            return InteractionResult.SUCCESS;
        }

        Container container = new SimpleContainer(heldStack);

        Optional<AbsorberRecipe> recipeOptional = level.getRecipeManager().getRecipeFor(ATRecipes.ABSORBER_RECIPE_TYPE, container, level);

        if (recipeOptional.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        AbsorberRecipe recipe = recipeOptional.get();

        ItemStack resultStack = recipe.assemble(container, level.registryAccess()).copy();

        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        level.setBlock(pos, state.setValue(DURATION, duration - 1), 3);

        if (!player.addItem(resultStack)) {
            player.drop(resultStack, false);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0.0D, 0.0d, 0.0d, 16.0D, 4.0D + pState.getValue(DURATION) * 4, 16.0D);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();

        int duration = BlockAbsorber.MAX_DURATION;

        if (stack.hasTag() && stack.getTag().contains("uses")) {
            duration = stack.getTag().getInt("uses");
        }

        duration = net.minecraft.util.Mth.clamp(duration, 0, BlockAbsorber.MAX_DURATION);

        return this.defaultBlockState().setValue(DURATION, duration);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);

        int duration = state.getValue(DURATION);

        if (duration == 0) {
            return List.of(ItemStack.EMPTY);
        }

        for (ItemStack stack : drops) {
            if (stack.getItem() instanceof ItemTheAbsorber) {
                ItemTheAbsorber.setDuration(stack, duration);
            }
        }

        return drops;
    }
}