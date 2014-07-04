package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.Arrays;
import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.ServerGroup;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandSender;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

public class TeamspeakCommandEvent extends TeamspeakEvent {

	private HashMap<String, String> info;

	public TeamspeakCommandEvent(HashMap<String, String> infoMap) {
		setUser(Integer.parseInt(infoMap.get("invokerid")));
		info = infoMap;

		if (getUser() == null) return;
		performAction();
	}

	@Override
	protected void performAction() {
		String cmd = info.get("msg");
		cmd = cmd.substring(Configuration.TS_COMMANDS_PREFIX.getString().length());
		String[] split = cmd.split(" ");

		String commandName = split[0].toLowerCase();
		String[] args = Arrays.copyOfRange(split, 1, split.length);

		ServerGroup sg = BukkitSpeak.getPermissionsHelper().getServerGroup(getUser().get("client_servergroups"));
		if (sg == null) {
			BukkitSpeak.log().warning("Could not resolve server group(s) for user \""
					+ getUser().get("client_nickname") + "\".");
			BukkitSpeak.log().warning("Server groups: " + String.valueOf(getUser().get("client_servergroups")));
			return;
		}
		if (sg.isBlocked()) return;

		TeamspeakCommandSender tscs = new TeamspeakCommandSender(getUser(), sg.isOp(), sg.getPermissions());
		// Check for internal commands.
		if (BukkitSpeak.getTeamspeakCommandExecutor().execute(tscs, commandName, args)) {
			// Command successfully executed --> Log and return
			if (Configuration.TS_LOGGING.getBoolean()) {
				BukkitSpeak.log().info("TS client \"" + getClientName() + "\" executed internal command \"" + cmd
						+ "\".");
			}
			return;
		}

		// Not an internal command, execute as a Bukkit command.
		PluginCommand pc = Bukkit.getPluginCommand(commandName);

		// Vanilla and Bukkit commands don't need to be on the whitelist
		if (pc != null && !(sg.getPluginWhitelist().contains(pc.getPlugin().getName()))) {
			String m = Messages.TS_COMMAND_NOT_WHITELISTED.get();
			if (m.isEmpty()) return;
			getUser().put("command_name", pc.getName());
			getUser().put("command_description", pc.getDescription());
			getUser().put("command_plugin", pc.getPlugin().getName());

			Replacer r = new Replacer().addClient(getUser());
			tscs.sendMessage(r.replace(m));
			if (Configuration.TS_COMMANDS_LOGGING.getBoolean()) {
				BukkitSpeak.log().info("TS client \"" + getClientName() + "\" tried executing command \"" + cmd + "\","
						+ " but the plugin \"" + pc.getPlugin().getName() + "\" was not whitelisted.");
			}
			return;
		}
		if (sg.getCommandBlacklist().contains(commandName)) {
			String m = Messages.TS_COMMAND_BLACKLISTED.get();
			if (m.isEmpty()) return;
			getUser().put("command_name", pc.getName());
			getUser().put("command_description", pc.getDescription());
			getUser().put("command_plugin", pc.getPlugin().getName());

			Replacer r = new Replacer().addClient(getUser());
			tscs.sendMessage(r.replace(m));
			if (Configuration.TS_COMMANDS_LOGGING.getBoolean()) {
				BukkitSpeak.log().info("TS client \"" + getClientName() + "\" tried executing command \"" + cmd + "\","
						+ " but the command was blacklisted.");
			}
			return;
		}

		if (Configuration.TS_COMMANDS_LOGGING.getBoolean()) {
			BukkitSpeak.log().info("TS client \"" + getClientName() + "\" executed command \"" + cmd + "\".");
		}
		Bukkit.dispatchCommand(tscs, cmd);
	}
}
