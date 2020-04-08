/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sipg.io;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Central location to load and make icons available to the application.
 * 
 * @author Paul T. Grogan
 */
public abstract class Icons {
	
	public static final Icon COUNTRY = loadIcon("resources/silk/world.png");
	public static final Icon REGION = loadIcon("resources/silk/map.png");
	public static final Icon CITY = loadIcon("resources/silk/building.png");

	public static final Icon ADD = loadIcon("resources/silk/add.png");
	public static final Icon ADD_WIZARD = loadIcon("resources/silk/wand.png");
	public static final Icon EDIT = loadIcon("resources/silk/pencil.png");
	public static final Icon DELETE = loadIcon("resources/silk/delete.png");

	public static final Icon INFRASTRUCTURE = loadIcon("resources/silk/chart_organisation.png");
	public static final Icon AGRICULTURE = loadIcon("resources/fugue/leaf.png");
	public static final Icon AGRICULTURE_PLANNED = loadIcon("resources/fugue/leaf--faded.png");
	public static final Icon WATER = loadIcon("resources/fugue/water.png");
	public static final Icon WATER_PLANNED  = loadIcon("resources/fugue/water--faded.png");
	public static final Icon ENERGY = loadIcon("resources/fugue/lightning.png");
	public static final Icon PETROLEUM = loadIcon("resources/fugue/petroleum.png");
	public static final Icon PETROLEUM_PLANNED  = loadIcon("resources/fugue/petroleum--faded.png");
	public static final Icon ELECTRICITY = loadIcon("resources/fugue/light-bulb.png");
	public static final Icon ELECTRICITY_PLANNED  = loadIcon("resources/fugue/light-bulb--faded.png");
	
	public static final Icon WATER_ELECTRICITY = loadIcon("resources/fugue/water-electricity.png");
	
	public static final Icon PRODUCT = loadIcon("resources/silk/cog.png");
	public static final Icon POPULATION = loadIcon("resources/silk/group.png");
	public static final Icon REVENUE = loadIcon("resources/fugue/money.png");
	public static final Icon INVESTMENT = loadIcon("resources/fugue/piggy-bank.png");
	public static final Icon FUNDS = loadIcon("resources/fugue/bank.png");
	public static final Icon CONFIGURATION = loadIcon("resources/silk/wrench.png");

	public static final Icon RENEWABLE = loadIcon("resources/silk/arrow_rotate_clockwise.png");
	public static final Icon CONSUMPTION = loadIcon("resources/silk/user_comment.png");
	public static final Icon LOCAL = loadIcon("resources/fugue/map-resize-actual.png");
	public static final Icon COST_PRODUCTION = loadIcon("resources/fugue/tag-export.png");
	public static final Icon COST_SUPPLY = loadIcon("resources/fugue/tag-import.png");
	public static final Icon ARABLE_LAND = loadIcon("resources/fugue/microformats.png");
	public static final Icon LABOR = loadIcon("resources/silk/group.png");
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
	public static final Icon METRICS = loadIcon("resources/silk/chart_curve.png");

	public static final Icon RECALC = loadIcon("resources/fugue/arrow-circle.png");
	
	public static final Icon CONNECTED_BULLET = loadIcon("resources/silk/bullet_green.png");
	public static final Icon NOT_CONNECTED_BULLET = loadIcon("resources/silk/bullet_red.png");
	public static final Icon CONNECTED = loadIcon("resources/silk/connect.png");
	public static final Icon DISCONNECTED = loadIcon("resources/silk/disconnect.png");
	
	public static final Icon LOADING = loadIcon("resources/loading.gif");
	public static final Icon LOADING_COMPLETE = loadIcon("resources/loading_complete.png");
	
	public static final Image SYSTEM_MONITOR = loadIcon(
			"resources/fugue/system-monitor.png").getImage();
	
	/**
	 * Load icon.
	 *
	 * @param path the path
	 * @return the image icon
	 */
	private static final ImageIcon loadIcon(String path) {
		return new ImageIcon(Icons.class.getClassLoader().getResource(path));
	}
}
