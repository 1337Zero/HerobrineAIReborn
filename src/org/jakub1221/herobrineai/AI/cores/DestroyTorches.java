package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class DestroyTorches extends Core {

	public DestroyTorches() {
		super(CoreType.DESTROY_TORCHES, AppearType.NORMAL, HerobrineAI.getPluginCore());
	}

	public CoreResult CallCore(Object[] data) {
		return destroyTorches((Location) data[0]);
	}

	public CoreResult destroyTorches(Location loc) {
		if (HerobrineAI.getPluginCore().config.getBoolean("config.DestroyTorches") == true) {

			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			World world = loc.getWorld();

			int i = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); // Y
			int ii = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); // X
			int iii = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); // Z

			for (i = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); i <= HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius"); i++) {
				for (ii = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); ii <= HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius"); ii++) {
					for (iii = -(HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius")); iii <= HerobrineAI.getPluginCore().config.getInt("config.DestroyTorchesRadius"); iii++) {
						if (world.getBlockAt(x + ii, y + i, z + iii).getType() == Material.TORCH) {
							world.getBlockAt(x + ii, y + i, z + iii).breakNaturally();
							return new CoreResult(true, "Torches destroyed!");
						}
					}
				}
			}

		}
		return new CoreResult(false, "Cannot destroy torches.");
	}

}
