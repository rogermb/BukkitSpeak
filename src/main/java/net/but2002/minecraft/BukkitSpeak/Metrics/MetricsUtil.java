package net.but2002.minecraft.BukkitSpeak.Metrics;

import java.io.IOException;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class MetricsUtil {
	
	private static Metrics metrics;
	private static Logger logger;
	
	private MetricsUtil() {};
	
	public static Metrics getMetrics() {
		return metrics;
	}
	
	public static void setupMetrics() {
		try {
			metrics = new Metrics(BukkitSpeak.getInstance());
			logger = BukkitSpeak.log();
			
			/* Listeners stuff */
			Metrics.Graph teamspeakGraph = metrics.createGraph("Listeners active");
			teamspeakGraph.addPlotter(new SPlotter("Server events listener", BukkitSpeak.getStringManager().getUseServer()));
			teamspeakGraph.addPlotter(new SPlotter("Server text messages listener", BukkitSpeak.getStringManager().getUseTextServer()));
			teamspeakGraph.addPlotter(new SPlotter("Channel events listener", BukkitSpeak.getStringManager().getUseServer()));
			teamspeakGraph.addPlotter(new SPlotter("Channel text messages listener", BukkitSpeak.getStringManager().getUseServer()));
			teamspeakGraph.addPlotter(new SPlotter("Private messages listener", BukkitSpeak.getStringManager().getUseServer()));
			
			/* Text message sender */
			Metrics.Graph minecraftGraph = metrics.createGraph("Chat listener target");
			switch (BukkitSpeak.getStringManager().getTeamspeakTarget()) {
			case SERVER:
				minecraftGraph.addPlotter(new SPlotter("Server", 1));
				break;
			case CHANNEL:
				minecraftGraph.addPlotter(new SPlotter("Channel", 1));
				break;
			case NONE:
			default:
				minecraftGraph.addPlotter(new SPlotter("None", 0));
				break;
			}
			
			/* Clients on the TS3
			metrics.addCustomData(new Metrics.Plotter("Clients on TeamSpeak3 servers") {
				
				@Override
				public int getValue() {
					return clients.size();
				}
			}); */
			
			/* Factions stuff */
			metrics.addCustomData(new SPlotter("Factions", BukkitSpeak.hasFactions()));
			
			metrics.start();
		} catch (IOException e) {
			logger.info("Failed to connect to Metrics - " + e.getMessage());
		}
	}
}
