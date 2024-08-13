package net.jdotaz.biome_loot_mod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {

    @Inject(method = "onPlaced",at = @At("TAIL"))
    private void injectOnPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci){
        if (!world.isClient) {
            if (placer instanceof PlayerEntity player && !player.isCreative()) {
                if (world.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                    NbtCompound nbt = chest.createNbt();
                    nbt.putBoolean("used", true);
                    chest.readNbt(nbt);
                    chest.markDirty();
                }
            }
        }
    }
}
