package org.jakub1221.herobrineai.NPC;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location; 
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jakub1221.herobrineai.HerobrineAI;


import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;


public class NPCCore {

	private ArrayList<NPC> npcs = new ArrayList<NPC>();
	private int taskid;
	public static JavaPlugin plugin;
	public boolean isInLoaded = false;

	public NPCCore(JavaPlugin plugin) {

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(), new Runnable() {
			@Override
			public void run() {
				final ArrayList<NPC> toRemove = new ArrayList<NPC>();
				for (final NPC humanNPC : npcs) {
					final Entity entity = humanNPC.getEntity();
					if (entity.isDead()) {
						toRemove.add(humanNPC);
					}
				}
				for (final NPC n : toRemove) {
					npcs.remove(n);
				}
			}
		}, 1L, 1L);
		
	}

	public void removeAll() {
		for (NPC humannpc : npcs) {
			if (humannpc != null) {
				humannpc.destroy();
			}
		}
		npcs.clear();
	}


	public void DisableTask() {
		Bukkit.getServer().getScheduler().cancelTask(taskid);
	}


	public NPC spawnHumanNPC(String name, Location l) {
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name); 
		SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
		trait.setSkinPersistent("Herobrine", "GvMJvqChiEY6d5TUcQj5IBwwCgomRTZBbtOwVieOJN8ULbL/QYZgwWnvbqGz+eZYWgyPy5zy0fK5fcDM5s3FVPlFtW60iazVE1UYfSSROp2lO++U/kEBjgV9RX8gDfDoC/CF/jsj4FAoHoYkK/qZ3kKAx4KCNc3tLwud6hVFbpxbE2qaUiXHsOATx0mLolk8C+65NgebbYuNVkF1fYGUfh5S7fTxtoIS3wy30MsqaZzdV6x8ioATjfBnO/DKc64bNl5Ii0u5rpivcIvpqytynKFeylvBpeT5uNhtRfbjM6Ezuyka2f/XCZ9d82DB5VfC9E6vNcYu45JHZqOb7tvOux51KtoMtkli7tmm9pfmp/krGkVKNgiJRjwXctKJ6WtLgn8P/Uii6UNvHADeApLITMFrfzlIzIbyk3stlsVwoZBXPn34JWgJzEMXm052xZc4spHyZI4a9/HuCeaPoWK/6+Wgd40eN1gCH30Pu2j+OdXUzConQPT4/xMuzEWlB5iaY9TIay43ny4l9Zuc6m1kOkjTKwBEN59wWfXTQkDC9xnZRs4YkQaY239d1tNRBS+5SfQls8IKBVEaaXTTY1DDre5ovUR69Hq+GZaDMawGdWutlRVzSdomiZb85Aa6hHYEw5WJyToM4wZPxunLLoDoj1S/SCeZyH9I/sQh7wBOK0w=", "ewogICJ0aW1lc3RhbXAiIDogMTY3MjE0NjkwNDc5MCwKICAicHJvZmlsZUlkIiA6ICJiMjdjMjlkZWZiNWU0OTEyYjFlYmQ5NDVkMmI2NzE0YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIRUtUMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMGZiMDVlNjBmNDZiZTUwMzI1MzllODNhOWU4Y2UwNjZkYmJhNWFmODU0ZGUxYjg2NDYzMGFjYmM4ODRlZjU2IgogICAgfQogIH0KfQ==");
		npc.spawn(l);

		return npc;
	}

	public NPC getHumanNPC(int id) {

		for (NPC n : npcs) {
			if (n.getId() == id) {
				return n;
			}
		}

		return null;
	}
}
