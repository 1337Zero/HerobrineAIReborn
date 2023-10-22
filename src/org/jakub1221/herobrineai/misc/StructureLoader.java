package org.jakub1221.herobrineai.misc;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class StructureLoader {

	private int current = 0;
	private int length = 0;
	private FileConfiguration file;

	public StructureLoader(FileConfiguration config) {
		file = config;
	}

	public void Build(World world, int MainX, int MainY, int MainZ) {

		length = file.getInt("DATA.LENGTH") - 1;
		for (current = 0; current <= length; current++) {
			try {
				world.getBlockAt(MainX + file.getInt("DATA." + current + ".X"),
						MainY + file.getInt("DATA." + current + ".Y"), MainZ + file.getInt("DATA." + current + ".Z"))
						.setType(Material.valueOf(file.getString("DATA." + current + ".ID")));
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				System.out.println("Error processing: DATA." + current);
			}

		}
	}
}
