package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands;

import java.util.ArrayList;
import java.util.List;

import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.internal.*;

public class TeamspeakCommandExecutor {
	
	private List<TeamspeakCommand> commands;
	
	public TeamspeakCommandExecutor() {
		commands = new ArrayList<TeamspeakCommand>();
		commands.add(new CommandList());
		commands.add(new CommandPm());
	}
	
	public boolean execute(TeamspeakCommandSender sender, String command, String[] args) {
		for (TeamspeakCommand tsc : commands) {
			for (String name : tsc.getNames()) {
				if (name.equalsIgnoreCase(command)) {
					tsc.execute(sender, args);
					return true;
				}
			}
		}
		
		return false;
	}
}
