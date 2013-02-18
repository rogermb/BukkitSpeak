package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetChannel extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_CHANNELID;
	private static final String ALLOWED_INPUT = "Channel name or ID";
	private static final String DESCRIPTION = "BukkitSpeak will try to move itself into the channel with the stated ID. Set ChannelPassword &lfirst&r&6!";
	
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
		if (!(BukkitSpeak.getStringManager().getUseChannel()) && !(BukkitSpeak.getStringManager().getUseTextChannel())) {
			send(sender, Level.WARNING, "&4Set " + StringManager.TEAMSPEAK_CHANNEL 
					+ " or " + StringManager.TEAMSPEAK_TEXTCHANNEL + " to true to use this feature.");
			return false;
		}
		if (arg.contains(" ")) {
			send(sender, Level.WARNING, "&4The value must be an Integer greater than 0 or the name of a channel.");
			return false;
		}
		
		HashMap<String, String> channel;
		try {
			channel = BukkitSpeak.getChannelList().getByPartialName(arg);
		} catch (IllegalArgumentException e) {
			channel = null;
		}
		
		int cid = -1;
		if (channel == null) {
			cid = getIntFromString(arg);
		} else {
			cid = Integer.valueOf(channel.get("cid"));
		}
		
		if (cid < 1) {
			send(sender, Level.WARNING, "&4The value must be an Integer greater than 0.");
			return false;
		}
		
		int clid = BukkitSpeak.getQuery().getCurrentQueryClientID();
		String pw = BukkitSpeak.getStringManager().getChannelPass();
		
		if (BukkitSpeak.getQuery().moveClient(clid, cid, pw)) {
			tsSection.set(StringManager.TEAMSPEAK_CHANNELID, cid);
			BukkitSpeak.getInstance().saveConfig();
			BukkitSpeak.getInstance().reloadStringManager();
			reloadListener();
			send(sender, Level.INFO, "&aThe channel ID was successfully set to " + arg);
			sendChannelChangeMessage(sender);
		} else {
			send(sender, Level.WARNING, "&4The channel ID could not be set.");
			send(sender, Level.WARNING, "&4Ensure that this ID is really assigned to a channel.");
			send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length != 3) return null;
		List<String> al = new ArrayList<String>();
		for (String s : BukkitSpeak.getChannelList().getChannelNames()) {
			if (s.startsWith(args[3])) {
				al.add(s);
			}
		}
		return al;
	}
	
	private int getIntFromString(String s) {
		int ret;
		try {
			ret = Integer.valueOf(s);
			return ret;
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}
}
