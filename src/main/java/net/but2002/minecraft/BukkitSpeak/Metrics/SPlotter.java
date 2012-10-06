package net.but2002.minecraft.BukkitSpeak.Metrics;

import net.but2002.minecraft.BukkitSpeak.Metrics.Metrics.Plotter;

public class SPlotter extends Plotter {
	
	private int v = 0;
	
	public SPlotter() {
		super();
	}
	
	public SPlotter(String name) {
		super(name);
	}
	
	public SPlotter(String name, int value) {
		super(name);
		v = value;
	}
	
	public SPlotter(String name, boolean value) {
		super(name);
		v = value ? 1 : 0;
	}
	
	@Override
	public int getValue() {
		return v;
	}
}
