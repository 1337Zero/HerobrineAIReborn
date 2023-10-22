package org.jakub1221.herobrineai.support;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WorldGuardHook {
	public boolean Check() {

		return (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null);
	}

	public boolean isSecuredArea(Location loc) {

		//WorldGuardPlugin worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

		Map<String, ProtectedRegion> rm = container.get(BukkitAdapter.adapt(loc.getWorld())).getRegions();

		if (rm != null) {
			for (Entry<String, ProtectedRegion> s : rm.entrySet()) {
				if (s.getValue().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
					return true;
				}
			}

		}

		return false;
	}
}
