package com.cinemamod.fabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PreviewScreenBlock extends Block implements EntityBlock {

    public static ResourceLocation IDENT;
    public static PreviewScreenBlock PREVIEW_SCREEN_BLOCK;

    public PreviewScreenBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(-1.0f, 3600000.0F).noDrops().noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public static void register() {
        IDENT = new ResourceLocation("cinemamod", "preview_screen");
        PREVIEW_SCREEN_BLOCK = new PreviewScreenBlock();

        Registry.register(Registry.BLOCK, IDENT, PREVIEW_SCREEN_BLOCK);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PreviewScreenBlockEntity(PreviewScreenBlockEntity.PREVIEW_SCREEN_BLOCK_ENTITY, pos, state);
    }

}
