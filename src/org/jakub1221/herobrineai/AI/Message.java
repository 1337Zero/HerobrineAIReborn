package org.jakub1221.herobrineai.AI;

import java.util.Random;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class Message {

	public static void SendMessage(Player player) {
		if (HerobrineAI.getPluginCore().config.getBoolean("config.SendMessages") == true) {

			int count = HerobrineAI.getPluginCore().config.getStringList("config.Messages").size();

			Random randgen = new Random();
			int randmsg = randgen.nextInt(count);

			player.sendMessage("<Herobrine> " + HerobrineAI.getPluginCore().config.getStringList("config.Messages").get(randmsg));

		}
	}

}
