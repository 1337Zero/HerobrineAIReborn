package org.jakub1221.herobrineai;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.NPC.AI.Path;
import org.jakub1221.herobrineai.NPC.AI.PathManager;
import org.jakub1221.herobrineai.NPC.NPCCore;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.AI.extensions.GraveyardWorld;
import org.jakub1221.herobrineai.commands.CmdExecutor;
import org.jakub1221.herobrineai.entity.EntityManager;
import org.jakub1221.herobrineai.listeners.BlockListener;
import org.jakub1221.herobrineai.listeners.EntityListener;
import org.jakub1221.herobrineai.listeners.InventoryListener;
import org.jakub1221.herobrineai.listeners.PlayerListener;
import org.jakub1221.herobrineai.listeners.WorldListener;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class HerobrineAI extends JavaPlugin implements Listener {

	public static HerobrineAI pluginCore;
	private AICore aicore;
	
	public FileConfiguration config;
	public FileConfiguration schem_graveyardword;
	public FileConfiguration schem_temple;

	private Support support;
	private EntityManager entMng;
	private PathManager pathMng;
	private NPCCore NPCman;
	public long HerobrineEntityID;
	public boolean isInitDone = false;
	private int pathUpdateINT = 0;
	public NPC HerobrineNPC;

	public static String versionStr = "UNDEFINED";
	public static boolean isNPCDisabled = false;
	public static String bukkit_ver_string = "1.12.2";
	public static int HerobrineHP = 200;
	public static int HerobrineMaxHP = 200;
	public static final boolean isDebugging = false;
	public static boolean AvailableWorld = false;

	public static List<Material> AllowedBlocks = new ArrayList<Material>();
	public Map<Player, Long> PlayerApple = new HashMap<Player, Long>();

	public static Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {

		PluginDescriptionFile pdf = this.getDescription();
		versionStr = pdf.getVersion();

		boolean errorCheck = true;

		/*try {
			Class.forName("net.minecraft.server.v1_12_R1.Entity");
		} catch (ClassNotFoundException e) {
			errorCheck = false;
			isInitDone = false;
		}*/
		if (errorCheck) {

			isInitDone = true;

			HerobrineAI.pluginCore = this;

			//this.configdb = new ConfigDB(log);
			config = getConfig();
			config.options().copyDefaults(true);
			saveConfig();

			this.NPCman = new NPCCore(this);

			getServer().getPluginManager().registerEvents(new EntityListener(this), this);
			getServer().getPluginManager().registerEvents(new BlockListener(), this);
			getServer().getPluginManager().registerEvents(new InventoryListener(), this);
			getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
			getServer().getPluginManager().registerEvents(new WorldListener(), this);


			// Initialize PathManager

			this.pathMng = new PathManager();

			// Initialize AICore

			this.aicore = new AICore();

			// Initialize EntityManager

			this.entMng = new EntityManager();

			// Custom Config loading

			schem_graveyardword = createCustomConfig("graveyard_world.yml");
			schem_temple = createCustomConfig("temple.yml");

			// Spawn Herobrine

			Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), (float) 0, (float) -20,
					(float) 0);
			nowloc.setYaw((float) 1);
			nowloc.setPitch((float) 1);
			HerobrineSpawn(nowloc);

			Equipment eq = HerobrineNPC.getOrAddTrait(Equipment.class);
			//eq.set(EquipmentSlot.HAND, configdb.ItemInHand.getItemStack());
			eq.set(EquipmentSlot.HAND, new ItemStack(Material.valueOf(config.getString("config.ItemInHand"))));
			

			// Graveyard World

			File file = new File(getServer().getWorldContainer(), "world_herobrineai_graveyard");
			//if (!file.exists() && configdb.UseGraveyardWorld) {
			if (!file.exists() && config.getBoolean("config.UseGraveyardWorld")) {
				// create new World
				log.info("[HerobrineAI] Creating Graveyard world...");

				WorldCreator wc = new WorldCreator("world_herobrineai_graveyard");
				wc.generateStructures(false);
				org.bukkit.WorldType type = org.bukkit.WorldType.FLAT;
				wc.type(type);
				wc.createWorld();
				Bukkit.getServer().createWorld(wc);
				GraveyardWorld.Create();
			} else if (file.exists() && config.getBoolean("config.UseGraveyardWorld")) {
				log.info("[HerobrineAI] loading Graveyard world...");
				Bukkit.getServer().createWorld(new WorldCreator("world_herobrineai_graveyard"));
			}

			log.info("[HerobrineAI] Plugin loaded! Version: ");

			// Init Block Types

			AllowedBlocks.add(Material.AIR);
			AllowedBlocks.add(Material.SNOW);
			// Dead Shrub
			AllowedBlocks.add(Material.DEAD_BUSH);
			AllowedBlocks.add(Material.RAIL);
			AllowedBlocks.add(Material.DANDELION);
			AllowedBlocks.add(Material.POPPY);
			AllowedBlocks.add(Material.STONE_PRESSURE_PLATE);
			AllowedBlocks.add(Material.OAK_PRESSURE_PLATE);
			AllowedBlocks.add(Material.VINE);
			AllowedBlocks.add(Material.TORCH);
			AllowedBlocks.add(Material.REDSTONE);
			AllowedBlocks.add(Material.LEVER);
			AllowedBlocks.add(Material.STONE_BUTTON);
			AllowedBlocks.add(Material.LADDER);

			/*
			 * Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this,
			 * new Runnable() { public void run() {
			 * 
			 * for (int i = 0;i<=configdb.useWorlds.size()-1;i++){ if
			 * (Bukkit.getServer().getWorlds().contains(Bukkit.getServer().
			 * getWorld(configdb.useWorlds.get(i)))){AvailableWorld=true;} } if
			 * (AvailableWorld==false){ log.warning(
			 * "**********************************************************");
			 * log.
			 * warning("[HerobrineAI] There are no available worlds for Herobrine!"
			 * ); log.warning(
			 * "**********************************************************");
			 * }else{ log.info(
			 * "**********************************************************");
			 * log.info("[HerobrineAI] No problems detected."); log.info(
			 * "**********************************************************"); }
			 * 
			 * 
			 * } }, 1 * 1L);
			 */

			pathUpdateINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					if (Utils.getRandomGen().nextInt(4) == 2 && HerobrineAI.getPluginCore().getAICore().getCoreTypeNow()
							.equals(CoreType.RANDOM_POSITION)) {
						pathMng.setPath(new Path(Utils.getRandomGen().nextInt(15) - 7f,
								Utils.getRandomGen().nextInt(15) - 7f, HerobrineAI.getPluginCore()));
					}
				}
			}, 1 * 200L, 1 * 200L);

			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					pathMng.update();
				}
			}, 1 * 5L, 1 * 5L);

			// Command Executors
			this.getCommand("hb").setExecutor((CommandExecutor) new CmdExecutor(this));
			this.getCommand("hb-ai").setExecutor((CommandExecutor) new CmdExecutor(this));

			// Support initialize
			this.support = new Support();
			/*
			 * Class[] argst = new Class[3];
			 * argst[0] = Class.class;
			 * argst[1] = String.class;
			 * argst[2] = int.class;
			 * 
			 * Class[] argst2 = new Class[4];
			 * argst2[0] = int.class;
			 * argst2[1] = String.class;
			 * argst2[2] = Class.class;
			 * argst2[3] = String.class;
			 * 
			 * //int,string,class,string
			 * 
			 * //ALT = class,string,int
			 * try {
			 * Method ab =
			 * net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredMethod("a",
			 * argst2);
			 * } catch (NoSuchMethodException e1) {
			 * isNPCDisabled = true;
			 * } catch (SecurityException e1) {
			 * isNPCDisabled = true;
			 * }
			 */
			if (!isNPCDisabled) {
				/*
				 * try {
				 * 
				 * @SuppressWarnings("rawtypes")
				 * Class[] args = new Class[3];
				 * args[0] = Class.class;
				 * args[1] = String.class;
				 * args[2] = int.class;
				 * 
				 * Class[] args2 = new Class[4];
				 * args2[0] = int.class;
				 * args2[1] = String.class;
				 * args2[2] = Class.class;
				 * args2[3] = String.class;
				 * 
				 * Method a =
				 * net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredMethod("a",
				 * args2);
				 * a.setAccessible(true);
				 * 
				 * //Old Version, look for the function which adds an Entity to the list
				 * //a.invoke(a, CustomZombie.class, "Zombie", 54);
				 * //a.invoke(a, CustomSkeleton.class, "Skeleton", 51);
				 * 
				 * a.invoke(a,54,"zombie",CustomZombie.class,"Zombie");
				 * a.invoke(a,54,"skeleton",CustomSkeleton.class,"Skeleton");
				 * 
				 * } catch (Exception e) {
				 * e.printStackTrace();
				 * this.setEnabled(false);
				 * }
				 */
			} else {
				log.warning("[HerobrineAI] Custom NPCs have been disabled. (Incompatibility error!)");
			}
		} else {
			log.warning("[HerobrineAI] ******************ERROR******************");
			log.warning("[HerobrineAI] This version is only compatible with bukkit version " + bukkit_ver_string);
			log.warning("[HerobrineAI] *****************************************");
			this.setEnabled(false);
		}
	}

	private FileConfiguration createCustomConfig(String file) {
		File customConfigFile = new File(HerobrineAI.pluginCore.getDataFolder(), file);
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			saveResource(file, false);
		}

		YamlConfiguration customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		/*
		 * User Edit:
		 * Instead of the above Try/Catch, you can also use
		 * YamlConfiguration.loadConfiguration(customConfigFile)
		 */
		return customConfig;
	}

	public void onDisable() {

		if (isInitDone) {
			this.entMng.killAllMobs();
			Bukkit.getServer().getScheduler().cancelTask(pathUpdateINT);
			NPCman.DisableTask();
			aicore.CancelTarget(CoreType.ANY);
			aicore.Stop_BD();
			aicore.Stop_CG();
			aicore.Stop_MAIN();
			aicore.Stop_RC();
			aicore.Stop_RM();
			aicore.Stop_RP();
			aicore.Stop_RS();
			aicore.disableAll();
			log.info("[HerobrineAI] Plugin disabled!");
		}

		NPCman.removeAll();

	}

	public java.io.InputStream getInputStreamData(String src) {
		return HerobrineAI.class.getResourceAsStream(src);
	}

	public AICore getAICore() {

		return this.aicore;

	}

	public EntityManager getEntityManager() {
		return this.entMng;
	}

	public static HerobrineAI getPluginCore() {

		return HerobrineAI.pluginCore;

	}

	public void HerobrineSpawn(Location loc) {
		HerobrineNPC = NPCman.spawnHumanNPC(ChatColor.WHITE + "Herobrine", loc);
	}

	public void HerobrineRemove() {

		HerobrineEntityID = 0;
		HerobrineNPC = null;
		NPCman.removeAll();

	}

	public String getVersionStr() {
		return versionStr;
	}

	public Support getSupport() {
		return this.support;
	}

	public PathManager getPathManager() {
		return this.pathMng;
	}

	public boolean canAttackPlayer(Player player, Player sender) {

		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;

		//if (!configdb.AttackOP && player.isOp()) {
		if (!config.getBoolean("config.AttackOP") && player.isOp()) {
			opCheck = false;
		}

		//if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
		if (!config.getBoolean("config.AttackCreative") && player.getGameMode() == GameMode.CREATIVE) {
			creativeCheck = false;
		}

		//if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
		if (config.getBoolean("config.UseIgnorePermission") && player.hasPermission("hb-ai.ignore")) {			
			ignoreCheck = false;
		}

		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		} else {

			if (sender == null) {
				if (!opCheck)
					log.info("[HerobrineAI] Player is an OP.");
				else if (!creativeCheck)
					log.info("[HerobrineAI] Player is in creative mode.");
				else if (!ignoreCheck)
					log.info("[HerobrineAI] Player has ignore permission.");
			} else {
				if (!opCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is an OP.");
				else if (!creativeCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in creative mode.");
				else if (!ignoreCheck)
					sender.sendMessage(ChatColor.RED + "[HerobrineAI] Player has ignore permission.");
			}

			return false;
		}

	}

	public boolean canAttackPlayerNoMSG(Player player) {

		boolean opCheck = true;
		boolean creativeCheck = true;
		boolean ignoreCheck = true;

		//if (!configdb.AttackOP && player.isOp()) {
		if (!config.getBoolean("config.AttackOP") && player.isOp()) {
			opCheck = false;
			System.out.println("1");
		}

		//if (!configdb.AttackCreative && player.getGameMode() == GameMode.CREATIVE) {
		if (!config.getBoolean("config.AttackCreative") && player.getGameMode() == GameMode.CREATIVE) {			
			creativeCheck = false;
			System.out.println("2");
		}

		//if (configdb.UseIgnorePermission && player.hasPermission("hb-ai.ignore")) {
		if (config.getBoolean("config.UseIgnorePermission") && player.hasPermission("hb-ai.ignore")) {				
			ignoreCheck = false;
			System.out.println("3");
		}

		if (opCheck && creativeCheck && ignoreCheck) {
			return true;
		} else {

			return false;
		}
	}

	public String getAvailableWorldString() {
		if (AvailableWorld) {
			return "Yes";
		} else {
			return "No";
		}
	}

}
