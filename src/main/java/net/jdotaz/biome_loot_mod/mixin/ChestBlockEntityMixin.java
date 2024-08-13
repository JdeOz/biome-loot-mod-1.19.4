package net.jdotaz.biome_loot_mod.mixin;

import net.jdotaz.biome_loot_mod.util.ConfigLoader;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin {

    @Unique
    private boolean used;


    @Inject(method = "readNbt", at = @At("TAIL"))
    public void injectReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("used")) {
            this.used = nbt.getBoolean("used");
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    protected void injectWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("used", this.used);
    }

    @Inject(method = "onOpen", at = @At("TAIL"))
    public void injectOnOpen(PlayerEntity player, CallbackInfo ci) {

        ChestBlockEntity chest = (ChestBlockEntity) (Object) this;
        BlockPos pos = chest.getPos();
        World world = chest.getWorld();

        if (world != null && !world.isClient) {
            NbtCompound nbt = chest.createNbt();
            boolean isUsed = nbt.contains("used") && nbt.getBoolean("used");

            if (!isUsed) {
                String biomeName = world.getBiome(pos).getKey().orElseThrow().getValue().toString();
                player.sendMessage(Text.literal(biomeName));

                String selectedLootTable = ConfigLoader.getRandomLootTableForBiome(biomeName);
                if (selectedLootTable != null) {
                    Identifier lootTableId = new Identifier(selectedLootTable);
                    player.sendMessage(Text.literal(selectedLootTable));

                    chest.clear();
                    chest.setLootTable(lootTableId, ConfigLoader.RANDOM.nextLong());

                }
                nbt.putBoolean("used", true);
                chest.readNbt(nbt);
                chest.markDirty();
            }
        }
    }
}