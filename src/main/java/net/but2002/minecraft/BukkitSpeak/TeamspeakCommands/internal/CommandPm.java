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
			String noUser = Messages.TS_COMMAND_PM_NO_PLAYER_BY_THIS_NAME.get();
			noUser = new Replacer().addClient(sender.getClientInfo()).addInput(mcUser).replace(noUser);
			if (!noUser.isEmpty()) sender.sendMessage(noUser);
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
			m = new Replacer().addClient(sender.getClientInfo()).addMessage(sb.toString()).replace(m);
			m = MessageUtil.toMinecraft(m, true, Configuration.TS_ALLOW_LINKS.getBoolean());
			
			if (!m.isEmpty()) {
				if (!BukkitSpeak.getMuted(p) && p.hasPermission("bukkitspeak.messages.pm")) {
					p.sendMessage(m);
					
					String tsMsg = Messages.TS_COMMAND_PM.get();
					Replacer r = new Replacer().addClient(sender.getClientInfo()).addMessage(sb.toString()).addPlayer(p);
					tsMsg = r.replace(tsMsg);
					if (!tsMsg.isEmpty()) {
						sender.sendMessage(tsMsg);
					}
				} else {
					String userMuted = Messages.TS_COMMAND_PM_RECIPIENT_MUTED.get();
					userMuted = new Replacer().addClient(sender.getClientInfo()).addPlayer(p).replace(userMuted);
					if (!userMuted.isEmpty()) sender.sendMessage(userMuted);
				}
			}
		}
		
		String convStarted = Messages.TS_COMMAND_PM_CONVERSATION_STARTED.get();
		convStarted = new Replacer().addClient(sender.getClientInfo()).addPlayer(p).replace(convStarted);
		if (!convStarted.isEmpty()) sender.sendMessage(convStarted);
		
		BukkitSpeak.registerRecipient(p.getName(), sender.getClientID());
	}
}
