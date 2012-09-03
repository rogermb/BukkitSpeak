package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandSet extends BukkitSpeakCommand {
	
	private final static String[][] PROPERTIES = {
		{StringManager.TEAMSPEAK_NAME,
				"Any string",
				"This name will prefix every message in TeamSpeak. It's the nickname of the ServerQuery client."},
		{StringManager.TEAMSPEAK_CONSOLENAME,
				"Any string",
				"This name will be used if a message is sent by the console."},
		{StringManager.TEAMSPEAK_CHANNELID,
				"An integer greater than 0",
				"BukkitSpeak will try to move itself into the channel with the stated ID. Set ChannelPassword &lfirst&r!"},
		{StringManager.TEAMSPEAK_CHANNELPW,
				"Any string",
				"BukkitSpeak will try to use this password to connect to the channel with the cid of ChannelID."},
		{StringManager.TEAMSPEAK_SERVER,
				"true or false",
				"If this is set to true, BukkitSpeak will notice if somebody joins or leaves the TS3 server."},
		{StringManager.TEAMSPEAK_TEXTSERVER,
				"true or false",
				"If this is set to true, BukkitSpeak will notice if somebody writes a global message on TeamSpeak."},
		{StringManager.TEAMSPEAK_CHANNEL,
				"true or false",
				"If this is set to true, the Minecraft server will be notified when somebody joins or leaves the TS channel."},
		{StringManager.TEAMSPEAK_TEXTCHANNEL,
				"true or false",
				"If this is set to true, chat in the TeamSpeak channel will be sent to the Minecraft server."},
		{StringManager.TEAMSPEAK_PRIVATEMESSAGES,
				"true or false",
				"If this is set to true, people can send private messages to people on the TS3 and they can text back."},
		{StringManager.TEAMSPEAK_ALLOWLINKS,
				"true or false",
				"If set to false, any links in messages will be filtered out."},
		{StringManager.TEAMSPEAK_TARGET,
				"none, channel or server",
				"If set to channel, Minecraft chat will be sent to the channel, if set to server it will be broadcasted."},
		{StringManager.TEAMSPEAK_CONSOLE,
				"true or false",
				"If set to false, none of the actions will be logged in the console. Exceptions will still be logged as usual."},
		{StringManager.TEAMSPEAK_DEFAULTREASON,
				"Any string",
				"This will be the default if you don't add a reason to /tsa kick, channelkick or ban."},
		{StringManager.TEAMSPEAK_DEBUG,
				"true or false",
				"True sets the plugin to debug mode."}};
	String props;
	ConfigurationSection tsSection;
	
	public CommandSet() {
		super();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < PROPERTIES.length; j++) {
			if (sb.length() > 0) sb.append("&a, ");
			sb.append("&6");
			sb.append(PROPERTIES[j][0]);
		}
		props = sb.toString();
		tsSection = BukkitSpeak.getInstance().getConfig().getConfigurationSection(StringManager.TEAMSPEAK_SECTION);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!BukkitSpeak.getQuery().isConnected() || BukkitSpeak.getClients() == null) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		if (args.length == 1) {
			send(sender, Level.INFO, "&aUsage: &6/tsa set <property> <value>");
			send(sender, Level.INFO, "&aProperties you can set:");
			send(sender, Level.INFO, props);
		} else if (args.length == 2) {
			int i = getPropsIndex(args[1]);
			if (i == -1) {
				send(sender, Level.INFO, "&4This is not a valid property.");
				send(sender, Level.INFO, "&aProperties you can set:");
				send(sender, Level.INFO, props);
			} else {
				send(sender, Level.INFO, "&4You need to add a value to set.");
				send(sender, Level.INFO, "&aPossible values:");
				send(sender, Level.INFO, "&6" + PROPERTIES[i][1]);
				send(sender, Level.INFO, "&aDescription:");
				send(sender, Level.INFO, "&6" + PROPERTIES[i][2]);
			}
		} else if (args.length == 3) {
			int i = getPropsIndex(args[1]);
			switch (i) {
			case -1:
				send(sender, Level.INFO, "&4This is not a valid property.");
				send(sender, Level.INFO, "&aProperties you can set:");
				send(sender, Level.INFO, props);
				return;
			case 0:
				if (BukkitSpeak.getQuery().setDisplayName(args[2])) {
					tsSection.set(StringManager.TEAMSPEAK_NAME, args[2]);
					send(sender, Level.INFO, "&aThe display name was successfully set to " + args[2]);
				} else {
					send(sender, Level.WARNING, "&4The display name could not be set.");
					send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
					return;
				}
				break;
			case 1:
				tsSection.set(StringManager.TEAMSPEAK_CONSOLENAME, args[2]);
				send(sender, Level.INFO, "&aThe console name was successfully set to " + args[2]);
				break;
			case 2:
				if (!(BukkitSpeak.getStringManager().getUseChannel()) && !(BukkitSpeak.getStringManager().getUseTextChannel())) {
					send(sender, Level.WARNING, "&4Set " + StringManager.TEAMSPEAK_CHANNEL 
							+ " or " + StringManager.TEAMSPEAK_TEXTCHANNEL + " to true to use this feature.");
					return;
				}
				int cid = -1;
				try {
					cid = Integer.valueOf(args[2]);
				} catch (Exception e) {
					send(sender, Level.WARNING, "&4The value must be an Integer greater than 0.");
					return;
				}
				if (cid < 1) {
					send(sender, Level.WARNING, "&4The value must be an Integer greater than 0.");
					return;
				}
				int clid = BukkitSpeak.getQuery().getCurrentQueryClientID();
				String pw = BukkitSpeak.getStringManager().getChannelPass();
				if (BukkitSpeak.getQuery().moveClient(clid, cid, pw)) {
					tsSection.set(StringManager.TEAMSPEAK_CHANNELID, args[2]);
					send(sender, Level.INFO, "&aThe channel ID was successfully set to " + args[2]);
				} else {
					send(sender, Level.WARNING, "&4The channel ID could not be set.");
					send(sender, Level.WARNING, "&4Ensure that this ID is really assigned to a channel.");
					send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
					return;
				}
				break;
			case 3:
				if (!(BukkitSpeak.getStringManager().getUseChannel()) && !(BukkitSpeak.getStringManager().getUseTextChannel())) {
					send(sender, Level.WARNING, "&4Set " + StringManager.TEAMSPEAK_CHANNEL 
							+ " or " + StringManager.TEAMSPEAK_TEXTCHANNEL + " to true to use this feature.");
					return;
				}
				tsSection.set(StringManager.TEAMSPEAK_CHANNELPW, args[2]);
				send(sender, Level.INFO, "&aThe channel password was successfully set to " + args[2]);
				break;
			case 4:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_SERVER, true);
					send(sender, Level.INFO, "&aServer joins and quits will now be broadcasted in Minecraft.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_SERVER, false);
					send(sender, Level.INFO, "&aServer joins and quits won't be broadcasted in Minecraft anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				BukkitSpeak.getInstance().reloadStringManager();
				reloadListener();
				return;
			case 5:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_TEXTSERVER, true);
					send(sender, Level.INFO, "&aTeamSpeak broadcasts will now be sent to Minecraft.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_TEXTSERVER, false);
					send(sender, Level.INFO, "&aTeamSpeak broadcasts won't be sent to Minecraft anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				BukkitSpeak.getInstance().reloadStringManager();
				reloadListener();
				return;
			case 6:
				boolean o1 = ((BukkitSpeak.getStringManager().getUseChannel())
						|| (BukkitSpeak.getStringManager().getUseTextChannel()));
				boolean n1 = false;
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_CHANNEL, true);
					n1 = true;
					send(sender, Level.INFO, "&aChannel joins and quits will now be broadcasted in Minecraft.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_CHANNEL, false);
					send(sender, Level.INFO, "&aChannel joins and quits won't be broadcasted in Minecraft anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				BukkitSpeak.getInstance().reloadStringManager();
				reloadListener();
				if (!o1 && n1) connectChannel(sender);
				return;
			case 7:
				boolean o2 = ((BukkitSpeak.getStringManager().getUseChannel())
						|| (BukkitSpeak.getStringManager().getUseTextChannel()));
				boolean n2 = false;
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_TEXTCHANNEL, true);
					n2 = true;
					send(sender, Level.INFO, "&aChat messages from the TeamSpeak channel will now be broadcasted in Minecraft.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_TEXTCHANNEL, false);
					send(sender, Level.INFO, "&aChat messages from the TeamSpeak channel won't be broadcasted in Minecraft anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				BukkitSpeak.getInstance().reloadStringManager();
				reloadListener();
				if (!o2 && n2) connectChannel(sender);
				return;
			case 8:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_PRIVATEMESSAGES, true);
					send(sender, Level.INFO, "&aPrivate messages can now be sent and received.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_PRIVATEMESSAGES, false);
					send(sender, Level.INFO, "&aPrivate messages can't be sent or received anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				BukkitSpeak.getInstance().reloadStringManager();
				reloadListener();
				return;
			case 9:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_ALLOWLINKS, true);
					send(sender, Level.INFO, "&aLinks in messages are now allowed.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_ALLOWLINKS, false);
					send(sender, Level.INFO, "&aLinks in messages are now forbidden.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				break;
			case 10:
				if (args[2].equalsIgnoreCase("none") || args[2].equalsIgnoreCase("noting")) {
					tsSection.set(StringManager.TEAMSPEAK_TARGET, "none");
					send(sender, Level.INFO, "&aMinecraft chat won't be sent to the TeamSpeak server anymore.");
				} else if (args[2].equalsIgnoreCase("channel") || args[2].equalsIgnoreCase("chat")) {
					tsSection.set(StringManager.TEAMSPEAK_TARGET, "channel");
					send(sender, Level.INFO, "&aMinecraft chat will now be sent to the TeamSpeak channel.");
				} else if (args[2].equalsIgnoreCase("server") || args[2].equalsIgnoreCase("broadcast")) {
					tsSection.set(StringManager.TEAMSPEAK_TARGET, "server");
					send(sender, Level.INFO, "&aMinecraft chat will now be broadcasted on the TeamSpeak server.");
				} else {
					send(sender, Level.WARNING, "Only 'none', 'channel' or 'server' are accepted.");
					return;
				}
				break;
			case 11:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_CONSOLE, true);
					send(sender, Level.INFO, "&aTeamspeak actions will now be logged in the console.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_CONSOLE, false);
					send(sender, Level.INFO, "&aTeamspeak actions won't be logged in the console anymore.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				break;
			case 12:
				tsSection.set(StringManager.TEAMSPEAK_DEFAULTREASON, args[2]);
				send(sender, Level.INFO, "&aThe default reason was successfully set to " + args[2]);
				break;
			case 13:
				if (args[2].equalsIgnoreCase("true")) {
					tsSection.set(StringManager.TEAMSPEAK_DEBUG, true);
					send(sender, Level.INFO, "&aDebug mode was successfully enabled.");
				} else if (args[2].equalsIgnoreCase("false")) {
					tsSection.set(StringManager.TEAMSPEAK_DEBUG, false);
					send(sender, Level.INFO, "&aDebug mode was successfully disabled.");
				} else {
					send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
					return;
				}
				break;
			default:
				return;
			}
			BukkitSpeak.getInstance().reloadStringManager();
		}
	}
	
	private int getPropsIndex(String name) {
		for (int i = 0; i < PROPERTIES.length; i++) {
			if (PROPERTIES[i][0].equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}
	
	private void reloadListener() {
		
		BukkitSpeak.getQuery().removeAllEvents();
		
		if (BukkitSpeak.getStringManager().getUseServer()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		}
		if (BukkitSpeak.getStringManager().getUseTextServer()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		}
		if (BukkitSpeak.getStringManager().getUseChannel()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL,
					BukkitSpeak.getStringManager().getChannelID());
		}
		if (BukkitSpeak.getStringManager().getUseTextChannel()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL,
					BukkitSpeak.getStringManager().getChannelID());
		}
		if (BukkitSpeak.getStringManager().getUsePrivateMessages()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		}
	}
	
	private void connectChannel(CommandSender sender) {
		int cid = BukkitSpeak.getQuery().getCurrentQueryClientChannelID();
		int clid = BukkitSpeak.getQuery().getCurrentQueryClientID();
		String pw = BukkitSpeak.getStringManager().getChannelPass();
		if (!BukkitSpeak.getQuery().moveClient(clid, cid, pw)) {
			send(sender, Level.WARNING, "&4The channel ID could not be set.");
			send(sender, Level.WARNING, "&4Ensure that the ChannelID is really assigned to a valid channel.");
			send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
		}
	}
}
