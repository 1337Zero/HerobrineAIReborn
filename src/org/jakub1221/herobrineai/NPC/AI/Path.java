package org.jakub1221.herobrineai.NPC.AI;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;

public class Path {

	private float x;
	private float z;
	private boolean xNegative;
	private boolean zNegative;
	private boolean canContinue = true;
	private boolean isCompleted = false;
	private int stepNow = 0;
	private int maxSteps = Utils.getRandomGen().nextInt(3) + 3;
	
	protected HerobrineAI PluginCore;

	public Path(float _x, float _z, HerobrineAI plugin) {

		PluginCore = plugin;
		
		x = _x;
		z = _z;
		if ((x - PluginCore.HerobrineNPC.getEntity().getLocation().getX()) < 0) {
			xNegative = true;
		} else {
			xNegative = false;
		}
		if ((z - PluginCore.HerobrineNPC.getEntity().getLocation().getZ()) < 0) {
			zNegative = true;
		} else {
			zNegative = false;
		}
	}

	public void update() {
		if (stepNow <= maxSteps) {
			if (!isCompleted) {

				if ((x - PluginCore.HerobrineNPC.getEntity().getLocation().getX()) < 0) {
					xNegative = true;
				} else {
					xNegative = false;
				}
				if ((z - PluginCore.HerobrineNPC.getEntity().getLocation().getZ()) < 0) {
					zNegative = true;
				} else {
					zNegative = false;
				}
				
				Location loc = PluginCore.HerobrineNPC.getEntity().getLocation();
				World world = loc.getWorld();
				
				if (loc.getBlockX() > (int) x - 1 && loc.getBlockX() < (int) x + 1 && loc.getBlockZ() > (int) z - 1
						&& loc.getBlockZ() < (int) z + 1) {
					isCompleted = true;
					return;
				}
				float nX = (float) loc.getX();
				float nY = (float) loc.getY();
				float nZ = (float) loc.getZ();

				float pre_finalX = 0.3f;
				float pre_finalZ = 0.3f;
				if (xNegative) {
					pre_finalX = -0.3f;
				}
				if (zNegative) {
					pre_finalZ = -0.3f;
				}

				boolean canGoX = true;
				boolean canGoZ = true;

				if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) nZ) > nY + 1) {
					canGoX = false;
				}
				if (world.getHighestBlockYAt((int) nX, (int) (nZ + pre_finalZ)) > nY + 1) {
					canGoZ = false;
				}
				if (canGoX && canGoZ) {
					if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) (nZ + pre_finalZ)) > nY + 1) {
						canGoX = false;
						canGoZ = false;
					} else if (world.getHighestBlockYAt((int) (nX + pre_finalX), (int) (nZ + pre_finalZ)) < nY - 2) {
						canGoX = false;
						canGoZ = false;
					}
				}

				Location newloc = PluginCore.HerobrineNPC.getEntity().getLocation();

				if (canGoX) {
					newloc.setX(newloc.getX() + pre_finalX);
				}
				if (canGoZ) {
					newloc.setZ(newloc.getZ() + pre_finalZ);
				}

				if (canGoX && canGoZ) {
					if (xNegative) {

					}
				}

				newloc.setY(world.getHighestBlockYAt(newloc) - 1);

				if (world.getBlockAt(newloc).getType().isSolid()) {

					newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc) + 1.5f);
					PluginCore.HerobrineNPC.faceLocation(newloc);
					newloc.setY(newloc.getWorld().getHighestBlockYAt(newloc));
					//PluginCore.HerobrineNPC.moveTo(newloc);
					PluginCore.HerobrineNPC.teleport(newloc,TeleportCause.PLUGIN);
				}
				
				stepNow++;

			}
		} else {
			if (new Random().nextInt(7) == 3) {

				int yaw = Utils.getRandomGen().nextInt(360);
				PluginCore.HerobrineNPC.getEntity().getLocation().setYaw(yaw);
				//PluginCore.HerobrineNPC.setYaw(yaw);
				//PluginCore.HerobrineNPC.setYawA(yaw);
				PluginCore.HerobrineNPC.getEntity().getLocation().setYaw(yaw);
				//PluginCore.HerobrineNPC.setPitch(0);
				PluginCore.HerobrineNPC.getEntity().getLocation().setPitch(0);

			}
		}
	}

	public boolean canContinue() {
		return this.canContinue;
	}

}
