package org.jakub1221.herobrineai.entity;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.HerobrineAI;
import org.mcmonkey.sentinel.SentinelTrait;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.trait.SkinTrait;

public class EntityManager {

	private ArrayList<NPC> mobList = new ArrayList<>();
	
	private String texture_zombie = "ewogICJ0aW1lc3RhbXAiIDogMTY5NjYwMTc3NDU4MywKICAicHJvZmlsZUlkIiA6ICI4ZjE5NjJmYzE4NzY0MDU3ODYxMmIxMzNjODE4YmY5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaW9uXzkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFlZDI0MDlhNzkyYmQ0YzY4NDQzNDI1NWFkMjEzOGFhYjIxY2JhMzM2ZDg5MDhjMzdhY2I4OTFkYzYzMTVkNyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
	private String signature_zombie = "ydM0cFumuwjilCtN7Fkwlh7aeFyIV0vFOAD1B3gDmzYlHxiMHWOz8Nouwx9JG0TLVt6TQF8z+ytvFI3nl8JiRHvRpPJiMftWv23rm/ek8UPcZ5Pr+W79CkiCqC1Hb2ys3BKybT9laUzk54NrFmNs1Ton37rc9wFtX7vq9hq4PjOV4JfJDsaiEYLiBqiJnY7FrE7lx4KdltkpE1u4hbAuRXeioFHoQ7zPQ4UNWiTfhVMHdYOv+DfNtptrEpOHt69pP0UsjGyMj/mdXlS/jOrMW9D1UW0x5XkQEZZhaGQz/Yk72RQSR/aGmR0GrmhZrBO+PX15jDCHmL/mHevdXehWi2tWaLNsTbq1QoTHt3iiDAEeeVAo3eFrdCR/qFMj7aSSryb/p/X8hs8BOYVx2swrlCnb4rEB+Gmqt60BpOiuGHcEyMmZT91k9UFt3yD8sl3NiotTuKGL/zNMZP3fcYJg7MwIGfoCuLL13VsObB8heXYqk6XEIAiJycGL3BiR8O3KVBuVwuTMzAlsRqp89Z1WRyXoicAAM7YZqI+XuWteOQD/tz/Dmpt3dJ8BvDEAMMLCqIhvk8KP/bWPuF+iU5SkQ+6beU34ztA43MnfZ9hEZijyX8ehD2hOD+UKZHumpgqLq83XCO561VJxOU6pgTYd/mxTKMucxijkpojacH6rIF4=";

	private String texture_skeleton = "ewogICJ0aW1lc3RhbXAiIDogMTY5NjExMTExNTMwMCwKICAicHJvZmlsZUlkIiA6ICIyYWI1NWUyNjQ4MzM0N2U1YTZiMmE1MzYyODVlNjFjZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJYZmV6b3IiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc4NGZiNzhkMDliMWZmMzNmYzg5MzY0MjhhNzU5NzA5NmQzNmYxNDIzMzRlYTU1OWI1OTg2MWVlZmY1MDc4NSIKICAgIH0KICB9Cn0=";
	private String signature_skeleton = "HS73LNqK9k3ZgaoIWqZwzFguEPjAfNVVsSa6TU/harY9TE2f2iUk+ZaJen9kiHJlvv3wCO0HUugxLEShk8yQno7EwD6UZ+kU/OSzB8eUJMIg3llHoLKKLWMb2ZMJJrBxbz/QJzrZTT3m+t5d6eDc02DoMV2il+Wsx6d7Sg5o6L58HWp9SauNog0JbA1AFVpfIIzEY2MX/WtxvMfU7qrk0VTzp67B0v+LdCcwsyE53B+m57X3EIczt4E2xKjunXdZZmg8dPkJgmxkdFp1BQeUItvPn1avjuTTJDioHbeKlD6t6ITF0gd3wqfpxgHcr9Ub+voyWZtb9N830gfrncgbXrAcU2Z3ZOTnkPdgS/Yfp/3tVSMys70ujudSrGt4t1sZXOoYgWA3fikGBmgzpRPzEI1W4MknMiE/0TBkx3bMEfjGFmLTkdBwEyxO4YxzGQfyrbbrGdr0fezAX1Etl2WgaYj8QNQ465+5l9uulQVulfg2rEu/Tb2wxL0F0iVNMg28DXLEm8iAslmLjjJUToUul/dPvDQcPxs2WhDNduAEHGCRqJFcO8GgZBn90BpEyDk1HmTVn4yV+3K230AelftBqZEiZmzkcntXTU2Afa/25Ggyup94gkbMPMXzV0emuhIZiIdL9sxV+ljANGAVuri0pMIE1OTe5w4sFo78L4/Mmlg=";

	public void spawnCustomZombie(Location loc, MobType mbt) {
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Zombie");
		SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
		trait.setSkinPersistent("Zombie", signature_zombie, texture_zombie);
		npc.spawn(loc);
		assignMobType(npc,mbt,loc);
		mobList.add(npc);
	}

	public void spawnCustomSkeleton(Location loc, MobType mbt) {
		/* 
		World world = loc.getWorld();
		net.minecraft.server.v1_12_R1.World mcWorld = ((org.bukkit.craftbukkit.v1_12_R1.CraftWorld) world).getHandle();
		CustomSkeleton zmb = new CustomSkeleton(mcWorld, loc, mbt);
		mcWorld.addEntity(zmb);
		mobList.put(new Integer(zmb.getBukkitEntity().getEntityId()), zmb);*/

		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Skeleton");
		SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
		trait.setSkinPersistent("Skeleton", signature_skeleton, texture_skeleton);
		npc.spawn(loc);
		assignMobType(npc,mbt,loc);
		mobList.add(npc);
	}
	private void assignMobType(NPC npc,MobType mbt,Location loc) {
		if (mbt.equals(MobType.ARTIFACT_GUARDIAN)) {
			SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);
			npc.getNavigator().getDefaultParameters().speedModifier(
					(float) HerobrineAI.getPluginCore().config.getDouble("npc.Guardian.Speed"));
			sentinelTrait.setHealth(HerobrineAI.getPluginCore().config.getInt("npc.Guardian.HP"));
			npc.setName("Artifact Guardian");
			Equipment eq = npc.getOrAddTrait(Equipment.class);

			eq.set(EquipmentSlot.HAND, new ItemStack(Material.GOLDEN_SWORD, 1));
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.GOLDEN_HELMET, 1));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.GOLDEN_CHESTPLATE, 1));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.GOLDEN_LEGGINGS, 1));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.GOLDEN_BOOTS, 1));

			npc.teleport(loc, TeleportCause.PLUGIN);
		} else if (mbt.equals(MobType.HEROBRINE_WARRIOR)) {
			SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);
			npc.getNavigator().getDefaultParameters().speedModifier(
					(float) HerobrineAI.getPluginCore().config.getDouble("npc.Guardian.Speed"));
			sentinelTrait.setHealth(HerobrineAI.getPluginCore().config.getDouble("npc.Guardian.HP"));
			npc.setName("Herobrine Warrior");

			Equipment eq = npc.getOrAddTrait(Equipment.class);

			eq.set(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD, 1));
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.IRON_HELMET, 1));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.IRON_CHESTPLATE, 1));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.IRON_LEGGINGS, 1));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.IRON_BOOTS, 1));

			npc.teleport(loc, TeleportCause.PLUGIN);
		} else if (mbt.equals(MobType.DEMON)) {
			SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);
			// this.getAttributeInstance(GenericAttributes.c).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getDouble("npc.Warrior.Speed"));
			npc.getNavigator().getDefaultParameters()
					.speedModifier((float) HerobrineAI.getPluginCore().config.getDouble("npc.Demon.Speed"));
			// this.getAttributeInstance(GenericAttributes.maxHealth).setValue(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));
			sentinelTrait.setHealth(HerobrineAI.getPluginCore().config.getInt("npc.Demon.HP"));
			// this.setHealth(HerobrineAI.getPluginCore().getConfigDB().npc.getInt("npc.Warrior.HP"));

			npc.setName("Herobrine Warrior");

			Equipment eq = npc.getOrAddTrait(Equipment.class);

			eq.set(EquipmentSlot.HAND, new ItemStack(Material.GOLDEN_APPLE, 1));
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET, 1));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS, 1));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS, 1));

			npc.teleport(loc, TeleportCause.PLUGIN);
		}
	}


	public boolean isCustomMob(int id) {
		for(NPC npc : mobList){
			if(npc.getEntity().getEntityId() == id){
				return true;
			}
		}
		return false;
	}
/*
	public CustomEntity getMobType(int id) {
		return mobList.get(new Integer(id));
	}
*/
	public void removeMob(int id) {
		for(NPC npc : mobList){
			if(npc.getEntity().getEntityId() == id){
				npc.destroy();
				mobList.remove(npc);
				return;
			}
		}
	}

	public void removeAllMobs() {
		mobList.clear();
	}

	public void killAllMobs() {
		for (NPC s : mobList) {
			s.destroy();
		}
		removeAllMobs();
	}

}
