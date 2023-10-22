package org.jakub1221.herobrineai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jakub1221.herobrineai.support.WorldGuardHook;

public class Support {

	private boolean B_Residence = false;
	private boolean B_GriefPrevention = false;
	private boolean B_Towny = false;
	private boolean B_CustomItems = false;
	private boolean B_WorldGuard = false;
	private boolean B_PreciousStones = false;
	private boolean B_Factions = false;

	private WorldGuardHook WorldGuard = null;

	public Support() {
		WorldGuard = new WorldGuardHook();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HerobrineAI.getPluginCore(), new Runnable() {
			public void run() {
				CheckForPlugins();
			}
		}, 1 * 2L);
	}

	public boolean isPreciousStones() {
		return B_PreciousStones;
	}

	public boolean isWorldGuard() {
		return B_WorldGuard;
	}

	public boolean isResidence() {
		return B_Residence;
	}

	public boolean isGriefPrevention() {
		return B_GriefPrevention;
	}

	public boolean isTowny() {
		return B_Towny;
	}

	public boolean isFactions() {
		return B_Factions;
	}

	public void CheckForPlugins() {
		if (WorldGuard.Check()) {
			B_WorldGuard = true;
			HerobrineAI.log.info("[HerobrineAI] WorldGuard plugin detected!");
		}
	}

	public boolean isSecuredArea(Location loc) {		
		if (B_WorldGuard)
			return WorldGuard.isSecuredArea(loc);
		else
			return false;
	}

	public boolean checkBuild(final Location loc) {
		//return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Build || !isSecuredArea(loc);
		return HerobrineAI.getPluginCore().config.getBoolean("config.SecuredArea_Build") || !isSecuredArea(loc);
	}

	public boolean checkAttack(final Location loc) {
		return HerobrineAI.getPluginCore().config.getBoolean("config.SecuredArea_Attack") || !isSecuredArea(loc);
		//return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Attack || !isSecuredArea(loc);
	}

	public boolean checkHaunt(final Location loc) {
		//return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Haunt || !isSecuredArea(loc);		
		return HerobrineAI.getPluginCore().config.getBoolean("config.SecuredArea_Haunt") || !isSecuredArea(loc);
	}

	public boolean checkSigns(final Location loc) {
		//return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Signs || !isSecuredArea(loc);
		return HerobrineAI.getPluginCore().config.getBoolean("config.SecuredArea_Signs") || !isSecuredArea(loc);
	}

	public boolean checkBooks(final Location loc) {
		
		//return HerobrineAI.getPluginCore().getConfigDB().SecuredArea_Books || !isSecuredArea(loc);
		return HerobrineAI.getPluginCore().config.getBoolean("config.SecuredArea_Books") || !isSecuredArea(loc);
	}
	
	public boolean isCustomItems() {
		return B_CustomItems;
	}
}
