package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetTextChannelListener extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_TEXTCHANNEL;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If this is set to true, chat in the TeamSpeak channel will be sent to the Minecraft server.";
	private static final String[] TAB_SUGGESTIONS = {"true", "false"};
	
	@Override
	public String getProperty() {
		return PROPERTY;
	}
	
	@Override
	public String getAllowedInput() {
		return ALLOWED_INPUT;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg) {
		boolean o2 = (BukkitSpeak.getStringManager().getUseChannel())
				|| (BukkitSpeak.getStringManager().getUseTextChannel());
		boolean n2 = false;
		if (arg.equalsIgnoreCase("true")) {
			tsSection.set(StringManager.TEAMSPEAK_TEXTCHANNEL, true);
			n2 = true;
			send(sender, Level.INFO, "&aChat messages from the channel will now be broadcasted in Minecraft.");
		} else if (arg.equalsIgnoreCase("false")) {
			tsSection.set(StringManager.TEAMSPEAK_TEXTCHANNEL, false);
			send(sender, Level.INFO, "&aChat messages from the channel won't be broadcasted in Minecraft anymore.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		BukkitSpeak.getInstance().saveConfig();
		BukkitSpeak.getInstance().reloadStringManager();
		reloadListener();
		if (!o2 && n2) connectChannel(sender);
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length != 3) return null;
		List<String> al = new ArrayList<String>();
		for (String s : TAB_SUGGESTIONS) {
			if (s.startsWith(args[3])) {
				al.add(s);
			}
		}
		return al;
	}
}
