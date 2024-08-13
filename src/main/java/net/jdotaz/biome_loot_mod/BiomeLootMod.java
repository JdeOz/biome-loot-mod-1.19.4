package net.jdotaz.biome_loot_mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.jdotaz.biome_loot_mod.command.ChestLootCommand;
import net.jdotaz.biome_loot_mod.util.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiomeLootMod implements ModInitializer {

    public static final String MOD_ID = "biome_loot_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigLoader.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ChestLootCommand.register(dispatcher, environment.dedicated);
        });
    }


}

