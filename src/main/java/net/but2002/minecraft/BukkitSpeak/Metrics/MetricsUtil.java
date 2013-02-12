package net.but2002.minecraft.BukkitSpeak.Metrics;

import java.io.IOException;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Metrics.Metrics.*;

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
			Graph teamspeakGraph = metrics.createGraph("Listeners active");
			teamspeakGraph.addPlotter(new Plotter("Server events listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseServer() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Server text messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseTextServer() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Channel events listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseChannel() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Channel text messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUseTextChannel() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Private messages listener") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.getStringManager().getUsePrivateMessages() ? 1 : 0;
				}
			});
			
			/* Text message sender */
			Graph minecraftGraph = metrics.createGraph("Chat listener target");
			switch (BukkitSpeak.getStringManager().getTeamspeakTarget()) {
			case SERVER:
				minecraftGraph.addPlotter(new Plotter("Server") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			case CHANNEL:
				minecraftGraph.addPlotter(new Plotter("Channel") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			case NONE:
			default:
				minecraftGraph.addPlotter(new Plotter("None") {
					
					@Override
					public int getValue() {
						return 1;
					}
				});
				break;
			}
			
			/* Clients on the TS3 */
			Graph clientsGraph = metrics.createGraph("Clients on TeamSpeak3 servers");
			clientsGraph.addPlotter(new Plotter("Clients") {
				
				@Override
				public int getValue() {
					if (BukkitSpeak.getClientList() != null) {
						return BukkitSpeak.getClientList().size();
					} else {
						return 0;
					}
				}
			});
			
			/* Dependency stuff */
			Graph dependencyGraph = metrics.createGraph("Dependencies");
			dependencyGraph.addPlotter(new Plotter("Factions") {
				
				@Override
				public int getValue() {
					return (BukkitSpeak.hasFactions() && BukkitSpeak.getStringManager().getFactionsPublicOnly()) ? 1 : 0;
				}
			});
			dependencyGraph.addPlotter(new Plotter("Herochat") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.useHerochat() ? 1 : 0;
				}
			});
			dependencyGraph.addPlotter(new Plotter("McMMO") {
				
				@Override
				public int getValue() {
					return BukkitSpeak.hasMcMMO() ? 1 : 0;
				}
			});
			
			metrics.start();
			logger.info("Connected to mcstats.org.");
		} catch (IOException e) {
			logger.info("Failed to connect to mcstats.org - " + e.getMessage());
		}
	}
}
