package org.jakub1221.herobrineai.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core.CoreType;
import org.jakub1221.herobrineai.misc.ItemName;

public class PlayerListener implements Listener {

	private ArrayList<String> equalsLoreS = new ArrayList<String>();
	private ArrayList<String> equalsLoreA = new ArrayList<String>();
	private ArrayList<LivingEntity> LivingEntities = new ArrayList<LivingEntity>();
	private Location le_loc = null;
	private Location p_loc = null;
	private long timestamp = 0;
	private boolean canUse = false;
	private HerobrineAI PluginCore = null;

	public PlayerListener(HerobrineAI plugin) {
		equalsLoreS.add("Herobrine artifact");
		equalsLoreS.add("Sword of Lighting");
		equalsLoreA.add("Herobrine artifact");
		equalsLoreA.add("Apple of Death");
		PluginCore = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() != null && event.getPlayer().getInventory().getItemInMainHand() != null) {

				ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
				if (event.getPlayer().getInventory().getItemInMainHand().getType() != null) {

					if (itemInHand.getType() == Material.DIAMOND_SWORD
							|| itemInHand.getType() == Material.GOLDEN_APPLE) {

						if (ItemName.getLore(itemInHand) != null) {

							if (ItemName.getLore(itemInHand).containsAll(equalsLoreS)
								&& PluginCore.config.getBoolean("config.UseArtifact.Sword")) {

								if (Utils.getRandomGen().nextBoolean()) {
									event.getPlayer().getLocation().getWorld()
											.strikeLightning(event.getClickedBlock().getLocation());
								}

							} else if (ItemName.getLore(itemInHand).containsAll(equalsLoreA)
									   && PluginCore.config.getBoolean("config.UseArtifact.Apple")) {
								
								timestamp = System.currentTimeMillis() / 1000;
								canUse = false;
								
								if (PluginCore.PlayerApple.containsKey(event.getPlayer())) {
									if (PluginCore.PlayerApple.get(event.getPlayer()) < timestamp) {
										PluginCore.PlayerApple.remove(event.getPlayer());
										canUse = true;
									} else {
										canUse = false;
									}
								} else {
									canUse = true;
								}

								if (canUse == true) {

									event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0F);
									LivingEntities = (ArrayList<LivingEntity>) event.getPlayer().getLocation()
											.getWorld().getLivingEntities();
									PluginCore.PlayerApple.put(event.getPlayer(), timestamp + 60);

									for (int i = 0; i <= LivingEntities.size() - 1; i++) {

										if (LivingEntities.get(i) instanceof Player || LivingEntities.get(i)
												.getEntityId() == PluginCore.HerobrineEntityID) {
										} else {
											le_loc = LivingEntities.get(i).getLocation();
											p_loc = event.getPlayer().getLocation();

											if (le_loc.getBlockX() < p_loc.getBlockX() + 20
												&& le_loc.getBlockX() > p_loc.getBlockX() - 20) {

												if (le_loc.getBlockY() < p_loc.getBlockY() + 10
													&& le_loc.getBlockY() > p_loc.getBlockY() - 10) {

													if (le_loc.getBlockZ() < p_loc.getBlockZ() + 20
														&& le_loc.getBlockZ() > p_loc.getBlockZ() - 20) {

														event.getPlayer().getWorld().createExplosion(LivingEntities.get(i).getLocation(), 0F);
														LivingEntities.get(i).damage(10000);
													}
												}
											}
										}
									}
								} else {
									event.getPlayer().sendMessage(ChatColor.RED + "Apple of Death is recharging!");
								}
							}
						}
					}
				}
			}
		}

		if (event.getClickedBlock() != null) {
			if (event.getPlayer().getInventory().getItemInMainHand() != null) {
				if (event.getClickedBlock().getType() == Material.JUKEBOX) {

					ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
					Jukebox block = (Jukebox) event.getClickedBlock().getState();

					if (!block.isPlaying()) {
						if (item.getType() == Material.MUSIC_DISC_11) {

							PluginCore.getAICore();

							if (!AICore.isDiscCalled) {

								final Player player = event.getPlayer();
								PluginCore.getAICore();
								AICore.isDiscCalled = true;
								PluginCore.getAICore().CancelTarget(CoreType.ANY);

								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin,
										new Runnable() {
											public void run() {
												PluginCore.getAICore().callByDisc(player);
											}
										}, 1 * 50L);
							}
						}
					}
				}
			}
		}

	}

	@EventHandler
	public void onPlayerEnterBed(PlayerBedEnterEvent event) {
		if (Utils.getRandomGen().nextInt(100) > 75) {
			Player player = event.getPlayer();
			//((CraftPlayer) player).getHandle().a(true, false, false);
			PluginCore.getAICore().playerBedEnter(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)

	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event.getPlayer().getEntityId() != PluginCore.HerobrineEntityID) {
			if (AICore.PlayerTarget == event.getPlayer()
					&& PluginCore.getAICore().getCoreTypeNow() == CoreType.GRAVEYARD
					&& event.getPlayer().getLocation().getWorld() == Bukkit.getServer()
							.getWorld("world_herobrineai_graveyard")
					&& AICore.isTarget) {

				if (Utils.getRandomGen().nextBoolean()) {
					event.getPlayer()
							.teleport(PluginCore.getAICore().getGraveyard().getSavedLocation());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getPlayer().getEntityId() == PluginCore.HerobrineEntityID) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getPlayer().getEntityId() == PluginCore.HerobrineEntityID) {
			if (event.getFrom().getWorld() != event.getTo().getWorld()) {
				PluginCore.HerobrineRemove();
				PluginCore.HerobrineSpawn(event.getTo());
				event.setCancelled(true);
				return;
			}
			if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
				Location herobrineLocation = PluginCore.HerobrineNPC.getEntity().getLocation();
				if (herobrineLocation.getBlockX() > PluginCore.config.getInt("config.WalkingModeXRadius")
					&& herobrineLocation.getBlockX() < -PluginCore.config.getInt("config.WalkingModeXRadius")
					&& herobrineLocation.getBlockZ() > PluginCore.config.getInt("config.WalkingModeZRadius")
					&& herobrineLocation.getBlockZ() < -PluginCore.config.getInt("config.WalkingModeZRadius")) {
					
					PluginCore.getAICore().CancelTarget(CoreType.RANDOM_POSITION);
					PluginCore.HerobrineNPC.teleport(new Location(Bukkit.getServer().getWorlds().get(0), 0, -20, 0),TeleportCause.PLUGIN);

				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (event.getEntity().getEntityId() == PluginCore.HerobrineEntityID) {
			event.setDeathMessage("");

			PluginCore.HerobrineRemove();

			Location nowloc = new Location((World) Bukkit.getServer().getWorlds().get(0), 0, -20.f, 0);
			nowloc.setYaw(1.f);
			nowloc.setPitch(1.f);
			PluginCore.HerobrineSpawn(nowloc);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (event.getPlayer().getEntityId() != PluginCore.HerobrineEntityID) {
			if(!event.getPlayer().hasPermission("herobrineai.movegraveyard")){
				if (event.getPlayer().getWorld() == Bukkit.getServer().getWorld("world_herobrineai_graveyard")) {
					Player player = (Player) event.getPlayer();
					player.teleport(new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), -2.49f, 4.f,
							10.69f, -179.85f, 0.44999f));
				}
			}			
		}
	}
}
