package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;
import org.bukkit.command.CommandSender;

public class CommandHelp extends BukkitSpeakCommand {

	public CommandHelp() {
		super("help");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, Messages.MC_COMMAND_HELP_USER_HEADER.get());
		sendUserCommandHelp(sender, "list", Messages.MC_COMMAND_LIST_DESCRIPTION.get());
		sendUserCommandHelp(sender, "mute", Messages.MC_COMMAND_MUTE_DESCRIPTION.get());
		if (Configuration.TS_ENABLE_SERVER_MESSAGES.getBoolean()) {
			sendUserCommandHelp(sender, "broadcast", Messages.MC_COMMAND_BROADCAST_DESCRIPTION.get());
		}
		if (Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean()) {
			sendUserCommandHelp(sender, "chat", Messages.MC_COMMAND_CHAT_DESCRIPTION.get());
		}
		if (Configuration.TS_ENABLE_PRIVATE_MESSAGES.getBoolean()) {
			sendUserCommandHelp(sender, "pm", Messages.MC_COMMAND_PM_DESCRIPTION.get());
			sendUserCommandHelp(sender, "reply", "r(eply)", Messages.MC_COMMAND_REPLY_DESCRIPTION.get());
		}
		sendUserCommandHelp(sender, "poke", Messages.MC_COMMAND_POKE_DESCRIPTION.get());
		sendUserCommandHelp(sender, "info", Messages.MC_COMMAND_INFO_DESCRIPTION.get());

		if (checkCommandPermission(sender, "admin")) {
			String help = Messages.MC_COMMAND_HELP_ADMIN.get();
			help = new Replacer().addCommandDescription(Messages.MC_COMMAND_HELP_ADMIN_COMMAND.get(),
					Messages.MC_COMMAND_HELP_ADMIN_DESCRIPTION.get()).replace(help);
			send(sender, Level.INFO, help);
		}
	}

	private void sendUserCommandHelp(CommandSender sender, String command, String description) {
		sendUserCommandHelp(sender, command, command, description);
	}

	private void sendUserCommandHelp(CommandSender sender, String permission, String command, String description) {
		if (!checkCommandPermission(sender, permission)) return;
		String help = Messages.MC_COMMAND_HELP_USER.get();
		help = new Replacer().addCommandDescription("/ts " + command, description).replace(help);
		send(sender, Level.INFO, help);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
