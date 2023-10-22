package org.jakub1221.herobrineai.AI.cores;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.NPC.AI.Path;

public class RandomPosition extends Core {

	private int randomTicks = 0;
	private int randomMoveTicks = 0;
	private boolean RandomMoveIsPlayer = false;

	public RandomPosition() {
		super(CoreType.RANDOM_POSITION, AppearType.APPEAR, HerobrineAI.getPluginCore());
	}

	public int getRandomTicks() {
		return this.randomTicks;
	}

	public int getRandomMoveTicks() {
		return this.randomMoveTicks;
	}

	public void setRandomTicks(int i) {
		this.randomTicks = i;
	};

	public void setRandomMoveTicks(int i) {
		this.randomMoveTicks = i;
	};

	public CoreResult CallCore(Object[] data) {
		return setRandomPosition((World) data[0]);
	}

	public CoreResult setRandomPosition(World world) {
		if (HerobrineAI.getPluginCore().config.getBoolean("config.UseWalkingMode")) {
			if (randomTicks != 3) {
				randomTicks++;
				if (PluginCore.getAICore().getCoreTypeNow() != CoreType.RANDOM_POSITION && AICore.isTarget == false) {
					Location newloc = (Location) getRandomLocation(world);
					if (newloc != null) {

						//PluginCore.HerobrineNPC.moveTo(newloc);
						PluginCore.HerobrineNPC.teleport(newloc,TeleportCause.PLUGIN);
						newloc.setX(newloc.getX() + 2);
						newloc.setY(newloc.getY() + 1.5);
						//PluginCore.HerobrineNPC.lookAtPoint(newloc);
						PluginCore.HerobrineNPC.faceLocation(newloc);

						randomTicks = 0;
						AICore.log.info("[HerobrineAI] Herobrine is now in RandomLocation mode.");
						PluginCore.getAICore().Start_RM();
						PluginCore.getAICore().Start_RS();
						PluginCore.getAICore().Start_CG();
						RandomMoveIsPlayer = false;
						return new CoreResult(true, "Herobrine is now in WalkingMode.");
					} else {
						AICore.log.info("[HerobrineAI] RandomPosition Failed!");
						return setRandomPosition(world);
					}
				}
			} else {
				return new CoreResult(false, "WalkingMode - Find location failed!");
			}
		} else {
			return new CoreResult(false, "WalkingMode is disabled!");
		}
		return new CoreResult(false, "WalkingMode failed!");
	}

	public Location getRandomLocation(World world) {

		int i = 0;
		for (i = 0; i <= 100; i++) {

			int r_nxtX = HerobrineAI.getPluginCore().config.getInt("config.WalkingModeXRadius");
			int nxtX = r_nxtX;
			if (nxtX < 0) {
				nxtX = -nxtX;
			}
			int r_nxtZ = HerobrineAI.getPluginCore().config.getInt("config.WalkingModeZRadius");
			int nxtZ = r_nxtZ;
			if (nxtZ < 0) {
				nxtZ = -nxtZ;
			}
			int randx = Utils.getRandomGen().nextInt(nxtX);

			int randy = 0;

			int randz = Utils.getRandomGen().nextInt(nxtZ);

			int randxp = Utils.getRandomGen().nextInt(1);

			int randzp = Utils.getRandomGen().nextInt(1);

			if (randxp == 0 && randx != 0) {
				randx = -(randx);
			}
			if (randzp == 0 && randz != 0) {
				randz = -(randz);
			}

			randx = randx + HerobrineAI.getPluginCore().config.getInt("config.WalkingModeFromXRadius");
			randz = randz + HerobrineAI.getPluginCore().config.getInt("config.WalkingModeFromZRadius");

			if (world != null) {
				randy = world.getHighestBlockYAt(randx, randz);
			} else {
				return null;
			}

			if (world.getBlockAt(randx, randy, randz).getType() == Material.AIR
					&& world.getBlockAt(randx, randy + 1, randz).getType() == Material.AIR) {
				if (world.getBlockAt(randx, randy - 1, randz).getType() != Material.AIR
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.WATER
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.LAVA
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.GRASS
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.SNOW
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.OAK_LEAVES
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.WHEAT
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.TORCH
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.REDSTONE_TORCH
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.REDSTONE
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.WATER
						&& world.getBlockAt(randx, randy - 1, randz).getType() != Material.LAVA) {

					AICore.log.info("[HerobrineAI] RandomLocation "
							+ world.getBlockAt(randx, randy - 1, randz).getType().toString() + " is X:" + randx + " Y:"
							+ randy + " Z:" + randz);
					return new Location(world, (float) randx + 0.5, (float) randy, (float) randz);

				}
			}
		}

		return null;

	}

	public void RandomMove() {
		if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION && AICore.isTarget == false
				&& RandomMoveIsPlayer == false) {
			HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;

			if (Utils.getRandomGen().nextInt(5) == 3) {
				Location loc = PluginCore.HerobrineNPC.getEntity().getLocation();
				Path path = new Path((float) loc.getX() + Utils.getRandomGen().nextInt(30) - 15,
									 (float) loc.getZ() + Utils.getRandomGen().nextInt(30) - 15,
									 PluginCore);
				PluginCore.getPathManager().setPath(path);
			}

		}

	}

	public void CheckGravity() {

		if (PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION && AICore.isTarget == false) {

			Location hbloc = (Location) PluginCore.HerobrineNPC.getEntity().getLocation();
			World w = (World) hbloc.getWorld();
			
			if (hbloc.getBlockX() < HerobrineAI.getPluginCore().config.getInt("config.WalkingModeXRadius") + HerobrineAI.getPluginCore().config.getInt("config.WalkingModeFromXRadius")
				&& hbloc.getBlockX() > (-HerobrineAI.getPluginCore().config.getInt("config.WalkingModeXRadius")) + HerobrineAI.getPluginCore().config.getInt("config.WalkingModeXRadius")
				&& hbloc.getBlockZ() < HerobrineAI.getPluginCore().config.getInt(".WalkingModeZRadius") + HerobrineAI.getPluginCore().config.getInt(".WalkingModeFromZRadius")
				&& hbloc.getBlockZ() > (-HerobrineAI.getPluginCore().config.getInt("config.WalkingModeZRadius")) + HerobrineAI.getPluginCore().config.getInt("config.WalkingModeFromZRadius")) {
			
				if (!w.getBlockAt(hbloc.getBlockX(), hbloc.getBlockY() - 1, hbloc.getBlockZ()).getType().isSolid()) {

					hbloc.setY(hbloc.getY() - 1);

					//PluginCore.HerobrineNPC.moveTo(hbloc);
					PluginCore.HerobrineNPC.teleport(hbloc, TeleportCause.PLUGIN);
				}
			} else {
				PluginCore.getAICore().CancelTarget(CoreType.RANDOM_POSITION);
			}
		}
	}

	public void CheckPlayerPosition() {
		boolean isThere = false;
		Location loc = (Location) PluginCore.HerobrineNPC.getEntity().getLocation();
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		
		if (Bukkit.getServer().getOnlinePlayers().size() > 0) {

			for (Player player : onlinePlayers) {
				
				if (PluginCore.HerobrineEntityID != player.getEntityId()) {
					Location ploc = (Location) player.getLocation();
					
					if (ploc.getWorld() == loc.getWorld() 
						&& ploc.getX() + 7 > loc.getX()
						&& ploc.getX() - 7 < loc.getX() 
						&& ploc.getZ() + 7 > loc.getZ()
						&& ploc.getZ() - 7 < loc.getZ() 
						&& ploc.getY() + 7 > loc.getY()
						&& ploc.getY() - 7 < loc.getY()) {
						
						loc.setY(-20);
						//PluginCore.HerobrineNPC.moveTo(loc);
						PluginCore.HerobrineNPC.teleport(loc, TeleportCause.PLUGIN);
						PluginCore.getAICore().CancelTarget(CoreType.RANDOM_POSITION);
						RandomMoveIsPlayer = false;
						PluginCore.getAICore().setAttackTarget(player);
						break;
					} else {
						
						if (ploc.getWorld() == loc.getWorld() 
							&& ploc.getX() + 15 > loc.getX()
							&& ploc.getX() - 15 < loc.getX()
							&& ploc.getZ() + 15 > loc.getZ()
							&& ploc.getZ() - 15 < loc.getZ() 
							&& ploc.getY() + 15 > loc.getY()
							&& ploc.getY() - 15 < loc.getY()) {
							
							ploc.setY(ploc.getY() + 1.5);
							//PluginCore.HerobrineNPC.lookAtPoint(ploc);
							PluginCore.HerobrineNPC.faceLocation(ploc);
							PluginCore.getPathManager().setPath(null);
							isThere = true;
							break;
						}
					}
				}
			}
		}

		if (isThere) {
			RandomMoveIsPlayer = true;
		} else {
			RandomMoveIsPlayer = false;
		}

	}

}
