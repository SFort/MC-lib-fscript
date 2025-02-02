package tf.ssf.sfort.script;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import tf.ssf.sfort.script.extended.mixin.MixinExtendedFishingBobberEntityScript;
import tf.ssf.sfort.script.extended.mixin.MixinExtendedLivingEntityScript;
import tf.ssf.sfort.script.extended.mixin.MixinExtendedServerPlayerEntityScript;
import tf.ssf.sfort.script.extended.trinkets.TrinketExtendedLivingEntityScript;
import tf.ssf.sfort.script.instance.BiomeScript;
import tf.ssf.sfort.script.instance.ChunkScript;
import tf.ssf.sfort.script.instance.DamageSourceScript;
import tf.ssf.sfort.script.instance.DimensionTypeScript;
import tf.ssf.sfort.script.instance.EnchantmentLevelEntryScript;
import tf.ssf.sfort.script.instance.EnchantmentScript;
import tf.ssf.sfort.script.instance.EnderChestInventoryScript;
import tf.ssf.sfort.script.instance.EntityScript;
import tf.ssf.sfort.script.instance.FishingBobberEntityScript;
import tf.ssf.sfort.script.instance.GameModeScript;
import tf.ssf.sfort.script.instance.InventoryScript;
import tf.ssf.sfort.script.instance.ItemScript;
import tf.ssf.sfort.script.instance.ItemStackScript;
import tf.ssf.sfort.script.instance.LivingEntityScript;
import tf.ssf.sfort.script.instance.NbtElementScript;
import tf.ssf.sfort.script.instance.PlayerEntityScript;
import tf.ssf.sfort.script.instance.PlayerInventoryScript;
import tf.ssf.sfort.script.instance.ProjectileEntityScript;
import tf.ssf.sfort.script.instance.ServerPlayerEntityScript;
import tf.ssf.sfort.script.instance.ThrownEntityScript;
import tf.ssf.sfort.script.instance.ThrownItemEntityScript;
import tf.ssf.sfort.script.instance.WorldScript;

import java.util.HashMap;
import java.util.Map;

public class Default {
    public static final Parameters PARAMETERS = new Parameters();

    public static final EntityScript<Entity> ENTITY = new EntityScript<>();
    public static final ProjectileEntityScript<ProjectileEntity> PROJECTILE_ENTITY = new ProjectileEntityScript<>();
    public static final ThrownEntityScript THROWN_ENTITY = new ThrownEntityScript();
    public static final ThrownItemEntityScript THROWN_ITEM_ENTITY = new ThrownItemEntityScript();
    public static final LivingEntityScript<LivingEntity> LIVING_ENTITY = new LivingEntityScript<>();
    public static final PlayerEntityScript<PlayerEntity> PLAYER_ENTITY = new PlayerEntityScript<>();
    public static final ServerPlayerEntityScript<ServerPlayerEntity> SERVER_PLAYER_ENTITY = new ServerPlayerEntityScript<>();
    public static final DimensionTypeScript DIMENSION_TYPE = new DimensionTypeScript();
    public static final ChunkScript CHUNK = new ChunkScript();
    public static final WorldScript WORLD = new WorldScript();
    public static final BiomeScript BIOME = new BiomeScript();
    public static final ItemScript ITEM = new ItemScript();
    public static final InventoryScript<Inventory> INVENTORY = new InventoryScript<>();
    public static final PlayerInventoryScript PLAYER_INVENTORY = new PlayerInventoryScript();
    public static final EnderChestInventoryScript ENDERCHEST_INVENTORY = new EnderChestInventoryScript();
    public static final ItemStackScript ITEM_STACK = new ItemStackScript();
    public static final EnchantmentScript ENCHANTMENT = new EnchantmentScript();
    public static final EnchantmentLevelEntryScript ENCHANTMENT_LEVEL_ENTRY = new EnchantmentLevelEntryScript();
    public static final GameModeScript GAME_MODE = new GameModeScript();
    public static final FishingBobberEntityScript FISHING_BOBBER_ENTITY = new FishingBobberEntityScript();
    public static final DamageSourceScript DAMAGE_SOURCE = new DamageSourceScript();
    public static final NbtElementScript NBT_ELEMENT = new NbtElementScript();

    protected static final Map<String, PredicateProvider<?>> defaults = new HashMap<>();
    public static Map<String, PredicateProvider<?>> getDefaultMap(){
        return defaults;
    }
    static {
        ENCHANTMENT_LEVEL_ENTRY.addProvider(ENCHANTMENT, enchant -> set -> enchant.test(set.getKey()), 3000);
        ENTITY.addProvider(WORLD, world -> entity -> world.test(entity.getWorld()), 3002);
        ENTITY.addProvider(BIOME, biom -> entity -> biom.test(entity.getWorld().getBiome(entity.getBlockPos()).value()), 3001);
        ENTITY.addProvider(CHUNK, chunk -> entity -> chunk.test(entity.getWorld().getWorldChunk(entity.getBlockPos())), 3000);
        FISHING_BOBBER_ENTITY.addProvider(ENTITY, entity -> entity::test, 3000);
        ITEM_STACK.addProvider(ITEM, item -> stack -> item.test(stack.getItem()), 3000);
        PROJECTILE_ENTITY.addProvider(ENTITY, entity -> entity::test, 3000);
        THROWN_ENTITY.addProvider(PROJECTILE_ENTITY, entity -> entity::test, 3000);
        THROWN_ITEM_ENTITY.addProvider(THROWN_ENTITY, entity -> entity::test, 3000);
        LIVING_ENTITY.addProvider(ENTITY, entity -> entity::test, 3000);
        PLAYER_ENTITY.addProvider(LIVING_ENTITY, entity -> entity::test, 3001);
        PLAYER_ENTITY.addProvider(FISHING_BOBBER_ENTITY, fis -> player -> fis.test(player.fishHook), 3000);
        SERVER_PLAYER_ENTITY.addProvider(PLAYER_ENTITY, entity -> entity::test, 3001);
        SERVER_PLAYER_ENTITY.addProvider(GAME_MODE, mode -> player -> mode.test(player.interactionManager.getGameMode()), 3000);
        WORLD.addProvider(DIMENSION_TYPE, dim -> world -> dim.test(world.getDimension()), 3000);
        PLAYER_INVENTORY.addProvider(INVENTORY, inv -> inv::test, 3000);
        ENDERCHEST_INVENTORY.addProvider(INVENTORY, inv -> inv::test, 3000);

        //Mixin
        SERVER_PLAYER_ENTITY.addProvider(new MixinExtendedServerPlayerEntityScript(), 1000);
        LIVING_ENTITY.addProvider(new MixinExtendedLivingEntityScript(), 1000);
        FISHING_BOBBER_ENTITY.addProvider(new MixinExtendedFishingBobberEntityScript(), 1000);

        //Mod Compat
        if (FabricLoader.getInstance().isModLoaded("trinkets") && TrinketExtendedLivingEntityScript.success)
            LIVING_ENTITY.addProvider(new TrinketExtendedLivingEntityScript());

        defaults.put("ENTITY", ENTITY);
        defaults.put("PROJECTILE_ENTITY", PROJECTILE_ENTITY);
        defaults.put("THROWN_ENTITY", THROWN_ENTITY) ;
        defaults.put("THROWN_ITEM_ENTITY", THROWN_ITEM_ENTITY) ;
        defaults.put("LIVING_ENTITY", LIVING_ENTITY);
        defaults.put("PLAYER_ENTITY", PLAYER_ENTITY);
        defaults.put("SERVER_PLAYER_ENTITY", SERVER_PLAYER_ENTITY);
        defaults.put("DIMENSION_TYPE", DIMENSION_TYPE);
        defaults.put("CHUNK", CHUNK);
        defaults.put("WORLD", WORLD);
        defaults.put("BIOME", BIOME);
        defaults.put("ITEM", ITEM);
        defaults.put("INVENTORY", INVENTORY);
        defaults.put("PLAYER_INVENTORY", PLAYER_INVENTORY);
        defaults.put("ENDERCHEST_INVENTORY", ENDERCHEST_INVENTORY);
        defaults.put("ITEM_STACK", ITEM_STACK);
        defaults.put("ENCHANTMENT", ENCHANTMENT);
        defaults.put("ENCHANTMENT_LEVEL_ENTRY", ENCHANTMENT_LEVEL_ENTRY);
        defaults.put("GAME_MODE", GAME_MODE);
        defaults.put("FISHING_BOBBER_ENTITY", FISHING_BOBBER_ENTITY);
        defaults.put("DAMAGE_SOURCE", DAMAGE_SOURCE);
        defaults.put("NBT_ELEMENT", NBT_ELEMENT);

        FabricLoader.getInstance().getEntrypoints("fscript", Object.class).forEach( o -> {
            if (o instanceof Runnable) {
                ((Runnable)o).run();
            } else if (o instanceof ModInitializer) {
                ((ModInitializer)o).onInitialize();
            }
        });
    }
}
