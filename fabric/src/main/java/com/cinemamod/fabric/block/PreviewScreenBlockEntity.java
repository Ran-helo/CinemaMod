package com.cinemamod.fabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PreviewScreenBlockEntity extends BlockEntity {

    public static ResourceLocation IDENT;
    public static BlockEntityType<PreviewScreenBlockEntity> PREVIEW_SCREEN_BLOCK_ENTITY;

    public PreviewScreenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PreviewScreenBlockEntity(BlockPos pos, BlockState state) {
        super(PREVIEW_SCREEN_BLOCK_ENTITY, pos, state);
    }

    public static void register() {
        IDENT = new ResourceLocation("cinemamod", "preview_screen_block_entity");
        PREVIEW_SCREEN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
                .create(PreviewScreenBlockEntity::new, PreviewScreenBlock.PREVIEW_SCREEN_BLOCK)
                .build();
    }

}
