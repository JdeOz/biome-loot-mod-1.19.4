package net.jdotaz.biome_loot_mod.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.jdotaz.biome_loot_mod.BiomeLootMod;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigLoader {
    private static final Gson gson = new Gson();
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, List<String>>>() {}.getType();
    private static final Path CONFIG_PATH = Paths.get("config/biome_to_loot_table.json");

    public static Map<String, List<String>> BIOME_LOOT_TABLE;
    public static Random RANDOM = new Random();


    public static void loadConfig() {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(CONFIG_PATH))) {
            BIOME_LOOT_TABLE = gson.fromJson(reader, CONFIG_TYPE);
        } catch (Exception e) {
            BiomeLootMod.LOGGER.error(String.valueOf(e));
            BIOME_LOOT_TABLE = new HashMap<>();
        }
    }

    public static String getRandomLootTableForBiome(String biome) {
        List<String> lootTables = BIOME_LOOT_TABLE.get(biome);
        if (lootTables != null && !lootTables.isEmpty()) {
            return lootTables.get(RANDOM.nextInt(lootTables.size()));
        }
        return null;
    }

    public static void init(){
        loadConfig();
        BiomeLootMod.LOGGER.info("Config Loader for " + BiomeLootMod.MOD_ID);
    }
}