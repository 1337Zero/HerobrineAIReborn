package org.jakub1221.herobrineai.AI.extensions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.misc.StructureLoader;

public class GraveyardWorld {

	public static void Create() {

		Location loc = new Location(Bukkit.getServer().getWorld("world_herobrineai_graveyard"), 0, 3, 0);

		for (int x = -50; x <= 50; x++) {
			for (int z = -50; z <= 50; z++) {

				loc.getWorld().getBlockAt(x, 3, z).setType(Material.MYCELIUM);
			}

		}

		int MainX = -10;
		int MainY = 3;
		int MainZ = -10;
		
		//StructureLoader structLoader = new StructureLoader(HerobrineAI.getPluginCore().getInputStreamData("/res/graveyard_world.yml"));
		StructureLoader structLoader = new StructureLoader(HerobrineAI.pluginCore.schem_graveyardword);
		structLoader.Build(loc.getWorld(), MainX, MainY, MainZ);

	}

}
