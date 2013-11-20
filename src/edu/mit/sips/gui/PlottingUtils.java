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
	private static final Color lightRed = new Color(255, 102, 102),
			lightOrange = new Color(255, 178, 102),
			lightGreen = new Color(102, 255, 102),
			lightBlue = new Color(102, 178, 255),
			lightPurple = new Color(178, 102, 255),
			gray = new Color(128, 128, 128),
			pink = new Color(255, 153, 204),
			darkGreen = new Color(0, 153, 0),
			purple = new Color(153, 51, 255),
			orange = new Color(255, 153, 51),
			seaGreen = new Color(102, 255, 178),
			limeGreen = new Color(153, 255, 51),
			yellow = new Color(255, 255, 51),
			teal = new Color(102, 255, 255),
			magenta = new Color(255, 102, 255),
			blue = new Color(102, 102, 255);
	
	public static Color getSystemColor(InfrastructureSystem system) {
		if(system instanceof AgricultureSystem) {
			return lightGreen;
		}
		if(system instanceof WaterSystem) {
			return lightBlue;
		}
		if(system instanceof PetroleumSystem) {
			return gray;
		}
		if(system instanceof ElectricitySystem) {
			return lightPurple;
		}
		if(system instanceof SocialSystem) {
			return pink;
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
			return darkGreen;
		}
		if(society.getName().contains("Industrial")) {
			return gray;
		}
		if(society.getName().contains("Urban")) {
			return lightBlue;
		}
		if(society.getName().contains("Rural")) {
			return lightGreen;
		}
		return null;
	}
	
	public static Color[] getResourceColors(Collection<String> names) {
		List<Color> colors = new ArrayList<Color>();
		for(String name : names) {
			colors.add(getResourceColor(name));
		}
		return colors.toArray(new Color[0]);
	}
	
	public static Color getResourceColor(String name) {
		// uses
		if(name.contains("Export")) {
			return purple;
		}
		if(name.contains("Losses")) {
			return orange;
		}
		if(name.contains("Wasted")) {
			return lightRed;
		}
		if(name.contains("Distribution")) {
			return seaGreen;
		}
		if(name.contains("Agriculture")) {
			return lightGreen;
		}
		if(name.contains("Water")) {
			return lightBlue;
		}
		if(name.contains("Petroleum")) {
			return gray;
		}
		if(name.contains("Electricity")) {
			return lightPurple;
		}
		if(name.contains("Social") || name.contains("Society")) {
			if(name.contains("Industrial")) {
				return gray;
			}
			if(name.contains("Urban")) {
				return lightBlue;
			}
			if(name.contains("Rural")) {
				return lightGreen;
			}
			return pink;
		}
		// sources
		if(name.contains("Import")) {
			return lightPurple;
		}
		if(name.contains("Private")) {
			return orange;
		} else if(name.contains("Production")) {
			if(name.contains("Industrial")) {
				return gray;
			}
			if(name.contains("Urban")) {
				return lightBlue;
			}
			if(name.contains("Rural")) {
				return lightGreen;
			}
			return limeGreen;
		} 
		if(name.contains("Distribution")) {
			return seaGreen;
		}
		return null;
	}
	
	public static Color getCashFlowColor(String name) {
		if(name.contains("Capital Expense")) {
			return lightRed;
		}
		if(name.contains("Operations Expense")) {
			return orange;
		}
		if(name.contains("Input Expense")) {
			return lightGreen;
		}
		if(name.contains("Decommission Expense")) {
			return yellow;
		}
		if(name.contains("Distribution Expense")) {
			return magenta;
		}
		if(name.contains("Import Expense")) {
			return lightPurple;
		}
		if(name.contains("Distribution Revenue")) {
			return blue;
		}
		if(name.contains("Export Revenue")) {
			return purple;
		}
		if(name.contains("Output Revenue")) {
			return lightGreen;
		}
		return null;
	}
	
	public static Color[] getCashFlowColors(Collection<String> names) {
		List<Color> colors = new ArrayList<Color>();
		for(String name : names) {
			colors.add(getCashFlowColor(name));
		}
		return colors.toArray(new Color[0]);
	}
	
	public static Color[] getSocietyColors(Collection<? extends Society> societies) {
		List<Color> colors = new ArrayList<Color>();
		for(Society society : societies) {
			colors.add(getSocietyColor(society));
		}
		return colors.toArray(new Color[0]);
	}
}