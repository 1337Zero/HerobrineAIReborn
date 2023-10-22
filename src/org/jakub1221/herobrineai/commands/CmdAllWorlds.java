package org.jakub1221.herobrineai.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdAllWorlds extends SubCommand {

	public CmdAllWorlds(HerobrineAI plugin, Logger log) {
		super(plugin, log);
	}

	@Override
	public boolean execute(Player player, String[] args) {

		addAllWorlds();
		sendMessage(player, ChatColor.GREEN + "[HerobrineAI] All worlds have been added to config.");
		sendMessage(player, ChatColor.YELLOW + "[HerobrineAI] Note: Worlds with blank spaces can cause problems!");

		return true;
	}

	@Override
	public String help() {
		return ChatColor.GREEN + "/hb-ai allworlds";
	}

	public void addAllWorlds() {
		ArrayList<String> allWorlds = new ArrayList<String>();
		List<World> worlds_ = Bukkit.getWorlds();
		for (int i = 0; i <= worlds_.size() - 1; i++) {
			if (!worlds_.get(i).getName().equalsIgnoreCase("world_herobrineai_graveyard")) {
				allWorlds.add(worlds_.get(i).getName());
			}
		}
		HerobrineAI.pluginCore.config.set("config.Worlds", allWorlds);
		HerobrineAI.pluginCore.saveConfig();

	}

}
