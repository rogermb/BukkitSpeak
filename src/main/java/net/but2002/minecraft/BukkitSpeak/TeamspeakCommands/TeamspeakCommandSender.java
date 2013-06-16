package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class TeamspeakCommandSender implements CommandSender {
	
	private final boolean operator;
	private final PermissibleBase permissions;
	private final Map<String, String> client;
	private final String name;
	private final List<String> outBuffer;
	
	private BufferSender outSender;
	
	public TeamspeakCommandSender(Map<String, String> clientInfo, boolean op, Map<String, Boolean> perms) {
		client = clientInfo;
		name = replaceValues(BukkitSpeak.getStringManager().getMessage("TeamspeakCommandSenderName"), client, true);
		outBuffer = Collections.synchronizedList(new LinkedList<String>());
		operator = op;
		
		permissions = new PermissibleBase(null);
		for (Map.Entry<String, Boolean> e : perms.entrySet()) {
			permissions.addAttachment(BukkitSpeak.getInstance(), e.getKey(), e.getValue());
		}
	}
	
	@Override
	public boolean isPermissionSet(String perm) {
		return permissions.isPermissionSet(perm);
	}
	
	@Override
	public boolean isPermissionSet(Permission perm) {
		return permissions.isPermissionSet(perm);
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return permissions.hasPermission(perm);
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		return permissions.hasPermission(perm);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String perm, boolean value) {
		return permissions.addAttachment(plugin, perm, value);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return permissions.addAttachment(plugin);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String perm, boolean value, int ticks) {
		return permissions.addAttachment(plugin, perm, value, ticks);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return permissions.addAttachment(plugin, ticks);
	}
	
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		permissions.removeAttachment(attachment);
	}
	
	@Override
	public void recalculatePermissions() {
		permissions.recalculatePermissions();
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return permissions.getEffectivePermissions();
	}
	
	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}
	
	@Override
	public boolean isOp() {
		return operator;
	}
	
	@Override
	public void setOp(boolean op) {
		throw new UnsupportedOperationException("A TeamspeakCommandSender's OP status cannot be changed.");
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void sendMessage(String message) {
		if (message == null) return;
		if (!BukkitSpeak.getQuery().isConnected()) return;
		
		outBuffer.add(format(message));
		startBuffer();
	}
	
	@Override
	public void sendMessage(String[] messages) {
		if (messages == null || messages.length == 0) return;
		
		for (int i = 0; i < messages.length; i++) {
			outBuffer.add(format(messages[i]));
		}
		
		startBuffer();
	}
	
	private String format(String s) {
		// TODO: Format message?
		return BukkitSpeakCommand.convertToTeamspeak(s, false, true);
	}
	
	private void startBuffer() {
		if (outSender == null || outSender.isDone()) {
			outSender = new BufferSender(outBuffer, client);
			Executors.newSingleThreadScheduledExecutor().schedule(outSender,
					BukkitSpeak.getStringManager().getTeamspeakCommandSenderBuffer(), TimeUnit.MILLISECONDS);
		}
	}
	
	public static String replaceValues(String input, Map<String, String> repl, boolean color) {
		String output = input;
		output = Matcher.quoteReplacement(output);
		if (color) {
			output = output.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
		} else {
			output = output.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
		}
		
		for (String key : repl.keySet()) {
			if ((key != null) && (repl.get(key) != null)) {
				output = output.replace("%" + key + "%", repl.get(key));
			}
		}
		
		return output;
	}
}

class BufferSender implements Runnable {
	
	private static final int MSG_MAXLENGTH = 1024;
	
	private final List<String> buffer;
	private final int clid;
	
	private boolean done;
	
	public BufferSender(List<String> outBuffer, Map<String, String> client) {
		buffer = outBuffer;
		clid = Integer.valueOf(client.get("clid"));
	}
	
	public void run() {
		if (buffer.isEmpty()) return;
		StringBuilder sb = new StringBuilder(buffer.remove(0));
		
		for (String message : buffer) {
			if (sb.length() + message.length() + 2 < MSG_MAXLENGTH) {
				sb.append("\n").append(message);
			} else {
				sendToTeamspeak(sb.toString());
				sb = new StringBuilder(message);
			}
		}
		sendToTeamspeak(sb.toString());
		
		setDone();
	}
	
	public boolean isDone() {
		return done;
	}
	
	private void sendToTeamspeak(String message) {
		QuerySender qs = new QuerySender(clid, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, message);
		Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
	}
	
	private void setDone() {
		done = true;
	}
}
