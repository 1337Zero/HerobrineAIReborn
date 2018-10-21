package org.jakub1221.herobrineai.NPC.Entity;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_12_R1.PacketPlayOutCamera;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HumanNPC {

	private EntityPlayer entity;
	private final int id;

	public HumanNPC(HumanEntity humanEntity, int id) {
		this.entity = humanEntity;
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public EntityPlayer getEntity() {
		return this.entity;
	}

	public void ArmSwingAnimation() {
		((WorldServer) entity.world).tracker.a(entity, new PacketPlayInArmAnimation());
	}

	public void HurtAnimation() {
		LivingEntity ent = (LivingEntity) entity.getBukkitEntity();

		double healthBefore = ent.getHealth();

		ent.damage(0.5D);
		ent.setHealth(healthBefore);
	}

	public void setItemInHand(ItemStack item) {
		if (item != null) {
			((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).setItemInHand(item);
		}
	}

	public String getName() {
		return ((HumanEntity) getEntity()).getName();
	}

	public void setPitch(float pitch) {
		((HumanEntity) getEntity()).pitch = pitch;
	}

	public void moveTo(Location loc) {
		Teleport(loc);
	}

	public void Teleport(Location loc) {
		getEntity().getBukkitEntity().teleport(loc);
	}

	public PlayerInventory getInventory() {
		return ((org.bukkit.entity.HumanEntity) getEntity().getBukkitEntity()).getInventory();
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setYaw(float yaw) {
		((EntityPlayer) getEntity()).yaw = yaw;
		((EntityPlayer) getEntity()).aA = yaw;
	}

	public void lookAtPoint(Location point) {

		if (getEntity().getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}

		Location npcLoc = ((LivingEntity) getEntity().getBukkitEntity()).getEyeLocation();
		double xDiff = point.getX() - npcLoc.getX();
		double yDiff = point.getY() - npcLoc.getY();
		double zDiff = point.getZ() - npcLoc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);

		double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
		double newPitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;

		if (zDiff < 0.0) {
			newYaw = newYaw + Math.abs(180 - newYaw) * 2.0D;
		}

		if (newYaw > 0.0D || newYaw < 180.0D) {
			entity.yaw = (float) (newYaw - 90.0);
			entity.pitch = (float) newPitch;
			entity.aM = (float) (newYaw - 90.0);
			entity.aK = (float) (newYaw - 90.0);
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			/*if(ent instanceof EntityPlayer){
				EntityPlayer pl = (EntityPlayer)ent;				
				PlayerConnection con = ((EntityPlayer) ent).getBukkitEntity().getHandle().playerConnection;
				//con.sendPacket(new PacketPlayOutCamera(getEntity()));
				//con.sendPacket(new PacketPlayOutEntityHeadRotation(getEntity(), (byte)entity.yaw));
				con.sendPacket(new PacketPlayOutEntityLook(getEntity().getId(),(byte)entity.yaw,(byte)entity.pitch,true));
				//new PacketPlayOutEntityLook();
				
			}*/
			byte yaw = (byte)(int)(entity.yaw * 256.0F / 360.0F);
            byte pitch = (byte)(int)(entity.pitch * 256.0F / 360.0F);
            
			PlayerConnection con = ((CraftPlayer)p).getHandle().playerConnection;
			con.sendPacket(new PacketPlayOutEntityHeadRotation(entity, yaw));
			con.sendPacket(new PacketPlayOutEntityLook(entity.getId(), Byte.valueOf(yaw), Byte.valueOf(pitch), Boolean.valueOf(true)));
			//con.sendPacket(new PacketPlayOutEntityLook(getEntity().getId(),(byte)entity.yaw,(byte)entity.pitch,true));
			
		}
		
	}

	public void setYawA(float yaw) {
		((EntityPlayer) getEntity()).aP = yaw;
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}
}
