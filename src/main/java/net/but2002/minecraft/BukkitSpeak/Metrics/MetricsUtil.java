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
			teamspeakGraph.addPlotter(new Metrics.Plotter("Server events listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseServer() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Metrics.Plotter("Server text messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseTextServer() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Metrics.Plotter("Channel events listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseChannel() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Metrics.Plotter("Channel text messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseTextChannel() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Metrics.Plotter("Private messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUsePrivateMessages() ? 1 : 0;
				}
			});
			
			/* Text message sender */
			Metrics.Graph minecraftGraph = metrics.createGraph("Chat listener target");
			switch (BukkitSpeak.getStringManager().getTeamspeakTarget()) {
			case SERVER:
				minecraftGraph.addPlotter(new Metrics.Plotter("Server") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			case CHANNEL:
				minecraftGraph.addPlotter(new Metrics.Plotter("Channel") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			case NONE:
			default:
				minecraftGraph.addPlotter(new Metrics.Plotter("None") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			}
			
			/* Clients on the TS3 */
			metrics.addCustomData(new Metrics.Plotter("Clients on TeamSpeak3 servers") {
				
				@Override
				public int getValue() {
					if (BukkitSpeak.getClients() != null) {
						return BukkitSpeak.getClients().size();
					} else {
						return 0;
					}
				}
			});
			
			/* Dependency stuff */
			Metrics.Graph dependencyGraph = metrics.createGraph("Dependencies");
			dependencyGraph.addPlotter(new Metrics.Plotter("Factions") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.hasFactions() ? 1 : 0;
				}
			});
			
			metrics.start();
		} catch (IOException e) {
			logger.info("Failed to connect to Metrics - " + e.getMessage());
		}
	}
}
