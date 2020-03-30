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
package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;

import edu.mit.sips.gui.event.SimulationControlEvent;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.gui.event.UpdateListener;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.Simulator;

/**
 * A panel that provides simulation control.
 * 
 * @author Paul T. Grogan
 */
public class SimulationControlPanel extends JPanel implements UpdateListener {
	private static final long serialVersionUID = -7014074954503228524L;

	private final Simulator simulator;
	private final JSlider timeSlider;
	private final AtomicBoolean working = new AtomicBoolean(false);

	private final Action initializeSim = new AbstractAction(null, Icons.INITIALIZE) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fireSimulationInitialize();
		}
	};

	private final Action endSim = new AbstractAction(null, Icons.ADVANCE_TO_END) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fireSimulationAdvanceToEnd();
		}
	};

	/**
	 * Instantiates a new simulation control panel.
	 *
	 * @param simulator the simulator
	 */
	public SimulationControlPanel(Simulator simulator) {
		if(simulator == null) {
			throw new IllegalArgumentException(
					"Simulator cannot be null.");
		}
		this.simulator = simulator;
		
		initializeSim.putValue(Action.SHORT_DESCRIPTION, 
				"Initialize the simulation.");
		endSim.putValue(Action.SHORT_DESCRIPTION, 
				"Run the simulation until the end.");
		
		setLayout(new BorderLayout());
		timeSlider = new JSlider();
		timeSlider.setPaintTrack(false);
		timeSlider.setEnabled(false);
		add(timeSlider, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton(initializeSim));
		buttonPanel.add(new JButton(endSim));
		add(buttonPanel, BorderLayout.CENTER);
		
		setMinimumSize(new Dimension(300,1));
		
		updateActions();
	}

	/**
	 * Fire simulation advance to end.
	 */
	private void fireSimulationAdvanceToEnd() {
		final JPanel panel = this;
		working.set(true);
		updateActions();
		
		new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground() {
				try {
					simulator.advanceSimulationToEnd(
							new SimulationControlEvent.AdvanceToEnd(panel));
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(getTopLevelAncestor(), 
							ex.getMessage(), "Error", 
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void done() {
				working.set(false);
				updateActions();
			}
		}.execute();
	}

	/**
	 * Fire simulation initialize.
	 */
	private void fireSimulationInitialize() {
		final JPanel panel = this;
		working.set(true);
		updateActions();
		
		new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground() {
				try {
					simulator.initializeSimulation(
							new SimulationControlEvent.Initialize(
									panel, simulator.getScenario().getStartTime(), 
									simulator.getScenario().getEndTime()));
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(getTopLevelAncestor(), 
							ex.getMessage(), "Error", 
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
					fireSimulationInitialize();
				}
				return null;
			}
			
			@Override
			protected void done() {
				working.set(false);
				updateActions();
			}
		}.execute();
	}

	/**
	 * Update actions.
	 */
	private void updateActions() {
		initializeSim.setEnabled(!working.get()
				&& (!simulator.isInitialized() || simulator.isCompleted()));
		endSim.setEnabled(!working.get()
				&& simulator.isInitialized() 
				&& !simulator.isCompleted());
	}

	@Override
	public void simulationCompleted(UpdateEvent event) {
		updateActions();
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		timeSlider.setMinimum((int) simulator.getStartTime());
		timeSlider.setMaximum((int) simulator.getEndTime());
		timeSlider.setLabelTable(timeSlider.createStandardLabels(
				10, timeSlider.getMinimum()));
		timeSlider.setMinorTickSpacing(1);
		timeSlider.setMajorTickSpacing(10);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		timeSlider.setValue((int) simulator.getTime());
		updateActions();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) { 
		timeSlider.setValue((int) simulator.getTime());
		updateActions();
	}
}
