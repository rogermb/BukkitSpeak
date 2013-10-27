package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.internal;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandSender;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

public class CommandPm extends TeamspeakCommand {
	
	public CommandPm() {
		super("pm", "tell");
	}
	
	@Override
	public void execute(TeamspeakCommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Usage: !pm user (message)");
			return;
		}
		String mcUser = args[0];
		Player p = BukkitSpeak.getInstance().getServer().getPlayer(mcUser);
		if (p == null) {
			sender.sendMessage(ChatColor.RED + "No Minecraft player by the name of " + mcUser + ".");
			return;
		}
		
		if (args.length > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			
			String m = Messages.TS_EVENT_PRIVATE_MESSAGE.get();
			if (m.isEmpty()) return;
			m = new Replacer().addClient(sender.getClientInfo()).addMessage(sb.toString()).replace(m);
			m = MessageUtil.toMinecraft(m, true, true);
			
			if (!BukkitSpeak.getMuted(p) && p.hasPermission("bukkitspeak.messages.pm")) {
				p.sendMessage(m);
			}
		}
		sender.sendMessage("Started conversation with player " + p.getName()
				+ ". You can now chat directly without typing "
				+ Configuration.TS_COMMANDS_PREFIX.getString() + "pm");
		BukkitSpeak.registerRecipient(p.getName(), sender.getClientID());
	}
}
