package org.jakub1221.herobrineai.NPC.Network;

import org.jakub1221.herobrineai.NPC.NPCCore;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class NetworkHandler extends PlayerConnection {

	public NetworkHandler(final NPCCore npcCore, final EntityPlayer entityPlayer) {
		super(npcCore.getServer().getMCServer(), npcCore.getNetworkCore(), entityPlayer);
	}

	@Override
	public void a(final double d0, final double d1, final double d2, final float f, final float f1) {
		
	}

	@Override
	public void sendPacket(final Packet packet) {
		//super.sendPacket(packet);
		/*if(packet instanceof PacketPlayOutNamedEntitySpawn){
			super.sendPacket(packet);
		}else if(packet instanceof PacketPlayOutPlayerInfo){
			super.sendPacket(packet);
		}else{
			System.out.println(packet);
		}*/
		
	}

}