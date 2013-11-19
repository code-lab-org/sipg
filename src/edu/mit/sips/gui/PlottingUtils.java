package edu.mit.sips.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

public abstract class PlottingUtils {
	public static Color getSystemColor(InfrastructureSystem system) {
		if(system instanceof AgricultureSystem) {
			return new Color(178, 255, 102);
		}
		if(system instanceof WaterSystem) {
			return new Color(102, 178, 255);
		}
		if(system instanceof PetroleumSystem) {
			return new Color(128, 128, 128);
		}
		if(system instanceof ElectricitySystem) {
			return new Color(178, 102, 255);
		}
		if(system instanceof SocialSystem) {
			return new Color(255, 153, 204);
		}
		return null;
	}
	
	public static Color[] getSystemColors(Collection<? extends InfrastructureSystem> systems) {
		List<Color> colors = new ArrayList<Color>();
		for(InfrastructureSystem system : systems) {
			colors.add(getSystemColor(system));
		}
		return colors.toArray(new Color[0]);
	}
	
	public static Color getSocietyColor(Society society) {
		if(society instanceof Country) {
			return new Color(0, 153, 0);
		}
		if(society.getName().contains("Industrial")) {
			return new Color(128, 128, 128);
		}
		if(society.getName().contains("Urban")) {
			return new Color(102, 178, 255);
		}
		if(society.getName().contains("Rural")) {
			return new Color(178, 255, 102);
		}
		return null;
	}
	
	public static Color getCashFlowColor(String name) {
		if(name.contains("Capital Ex")) {
			return new Color(255, 102, 102);
		}
		if(name.contains("Operations Ex")) {
			return new Color(255, 153, 51);
		}
		if(name.contains("Decommission Ex")) {
			return new Color(255, 255, 102);
		}
		if(name.contains("Distribution Ex")) {
			return new Color(255, 102, 255);
		}
		if(name.contains("Import Ex")) {
			return new Color(178, 102, 255);
		}
		if(name.contains("Distribution Rev")) {
			return new Color(102, 102, 255);
		}
		if(name.contains("Export Rev")) {
			return new Color(51, 153, 255);
		}
		if(name.contains("Output Rev")) {
			return new Color(102, 255, 102);
		}
		return null;
	}
	
	public static Color[] getSocietyColors(Collection<? extends Society> societies) {
		List<Color> colors = new ArrayList<Color>();
		for(Society society : societies) {
			colors.add(getSocietyColor(society));
		}
		return colors.toArray(new Color[0]);
	}
}