package net.but2002.minecraft.BukkitSpeak.Metrics;

import java.io.IOException;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
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
					return Configuration.TS_ENABLE_SERVER_EVENTS.getBoolean() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Server text messages listener") {
				
				@Override
				public int getValue() {
					return Configuration.TS_ENABLE_SERVER_MESSAGES.getBoolean() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Channel events listener") {
				
				@Override
				public int getValue() {
					return Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Channel text messages listener") {
				
				@Override
				public int getValue() {
					return Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean() ? 1 : 0;
				}
			});
			teamspeakGraph.addPlotter(new Plotter("Private messages listener") {
				
				@Override
				public int getValue() {
					return Configuration.TS_ENABLE_PRIVATE_MESSAGES.getBoolean() ? 1 : 0;
				}
			});
			
			/* Text message sender */
			Graph minecraftGraph = metrics.createGraph("Chat listener target");
			switch (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget()) {
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
			
			/* Teamspeak commands */
			Graph tsCommandGraph = metrics.createGraph("TeamSpeak commands enabled");
			tsCommandGraph.addPlotter(new Plotter("Yes") {
				
				@Override
				public int getValue() {
					return Configuration.TS_COMMANDS_ENABLED.getBoolean() ? 1 : 0;
				}
			});
			tsCommandGraph.addPlotter(new Plotter("No") {
				
				@Override
				public int getValue() {
					return Configuration.TS_COMMANDS_ENABLED.getBoolean() ? 0 : 1;
				}
			});
			
			/* Dependency stuff */
			Graph dependencyGraph = metrics.createGraph("Dependencies");
			dependencyGraph.addPlotter(new Plotter("Factions") {
				
				@Override
				public int getValue() {
					return (BukkitSpeak.hasFactions() && Configuration.PLUGINS_FACTIONS_PUBLIC_ONLY.getBoolean()) ? 1 : 0;
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
