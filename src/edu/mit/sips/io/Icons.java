package edu.mit.sips.io;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class Icons {
	
	public static final Icon COUNTRY = loadIcon("resources/silk/world.png");
	public static final Icon REGION = loadIcon("resources/silk/map.png");
	public static final Icon CITY = loadIcon("resources/silk/building.png");

	public static final Icon ADD = loadIcon("resources/silk/add.png");
	public static final Icon EDIT = loadIcon("resources/silk/pencil.png");
	public static final Icon DELETE = loadIcon("resources/silk/delete.png");

	public static final Icon INFRASTRUCTURE = loadIcon("resources/silk/chart_organisation.png");
			//loadIcon("resources/fugue/node.png");
	public static final Icon AGRICULTURE = loadIcon("resources/fugue/leaf.png");
	public static final Icon WATER = loadIcon("resources/fugue/water.png");
	public static final Icon ENERGY = loadIcon("resources/fugue/lightning.png");
	public static final Icon PETROLEUM = loadIcon("resources/fugue/petroleum.png");
	public static final Icon ELECTRICITY = loadIcon("resources/fugue/light-bulb.png");
	
	public static final Icon PRODUCT = loadIcon("resources/silk/cog.png");
	public static final Icon POPULATION = loadIcon("resources/silk/group.png");
	public static final Icon REVENUE = loadIcon("resources/fugue/money.png");
	public static final Icon FUNDS = loadIcon("resources/fugue/bank.png");
	public static final Icon CONFIGURATION = loadIcon("resources/silk/wrench.png");

	public static final Icon RENEWABLE = loadIcon("resources/silk/arrow_rotate_clockwise.png");
	public static final Icon CONSUMPTION = loadIcon("resources/silk/user_comment.png");
	public static final Icon LOCAL = loadIcon("resources/fugue/map-resize-actual.png");
	public static final Icon COST_PRODUCTION = loadIcon("resources/fugue/tag-export.png");
	public static final Icon COST_SUPPLY = loadIcon("resources/fugue/tag-import.png");
	public static final Icon ARABLE_LAND = loadIcon("resources/fugue/microformats.png");
	public static final Icon WATER_RESERVOIR = loadIcon("resources/fugue/water-reservoir.png");
	public static final Icon PETROLEUM_RESERVOIR = loadIcon("resources/fugue/petroleum-reservoir.png");

	public static final Icon AGRICULTURE_SOURCE = loadIcon("resources/fugue/leaf--plus.png");
	public static final Icon AGRICULTURE_USE = loadIcon("resources/fugue/leaf--minus.png");
	public static final Icon WATER_SOURCE = loadIcon("resources/fugue/water--plus.png");
	public static final Icon WATER_USE = loadIcon("resources/fugue/water--minus.png");
	public static final Icon PETROLEUM_SOURCE = loadIcon("resources/fugue/petroleum--plus.png");
	public static final Icon PETROLEUM_USE = loadIcon("resources/fugue/petroleum--minus.png");
	public static final Icon ELECTRICITY_SOURCE = loadIcon("resources/fugue/light-bulb--plus.png");
	public static final Icon ELECTRICITY_USE = loadIcon("resources/fugue/light-bulb--minus.png");
	
	public static final Icon INITIALIZE = loadIcon("resources/silk/control_repeat_blue.png");
	public static final Icon STEP = loadIcon("resources/silk/control_play_blue.png");
	public static final Icon ADVANCE = loadIcon("resources/silk/control_fastforward_blue.png");
	public static final Icon ADVANCE_TO_END = loadIcon("resources/silk/control_end_blue.png");
	public static final Icon RESET = loadIcon("resources/silk/control_start_blue.png");

	public static final Icon NETWORK = loadIcon("resources/fugue/node-select-all.png");
	public static final Icon INDICATORS = loadIcon("resources/silk/chart_bar.png");

	public static final Icon RECALC = loadIcon("resources/fugue/arrow-circle.png");
	
	
	public static final Image SYSTEM_MONITOR = loadIcon(
			"resources/fugue/system-monitor.png").getImage();
	
	private static final ImageIcon loadIcon(String path) {
		return new ImageIcon(Icons.class.getClassLoader().getResource(path));
	}
}
