package com.cinemamod.fabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ScreenBlockEntity extends BlockEntity {

    public static ResourceLocation IDENT;
    public static BlockEntityType<ScreenBlockEntity> SCREEN_BLOCK_ENTITY;

    public ScreenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ScreenBlockEntity(BlockPos pos, BlockState state) {
        super(SCREEN_BLOCK_ENTITY, pos, state);
    }

    public static void register() {
        IDENT = new ResourceLocation("cinemamod", "screen_block_entity");
        SCREEN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
                .create(ScreenBlockEntity::new, ScreenBlock.SCREEN_BLOCK)
                .build();
    }

}
