package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.Simulator;

/**
 * The Class SimulationControlPanel.
 */
public class SimulationControlPane extends JPanel implements ConnectionListener, UpdateListener {
	private static final long serialVersionUID = -7014074954503228524L;

	private final Simulator simulator;
	private final JSlider timeSlider;
	private final AtomicBoolean working = new AtomicBoolean(false);

	private final Action toggleConnection = 
			new AbstractAction(null, Icons.DISCONNECTED) {
		private static final long serialVersionUID = 8065337353804878751L;

		@Override
		public void actionPerformed(ActionEvent e) {
			working.set(true);
			updateActions();
			
			new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() {
					try {
						if(simulator.getConnection().isConnected()) {
							simulator.getAmbassador().disconnect();
						} else {
							simulator.getAmbassador().connect();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(getParent(), 
								ex.getMessage(), "Error", 
								JOptionPane.ERROR_MESSAGE);
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
	};

	private final Action autoOptimizeDistribution = 
			new AbstractAction("Distribution") {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JCheckBox) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				simulator.setAutoOptimizeDistribution(selected);
				autoOptimizeProductionAndDistribution.setEnabled(selected);
			}
		}
	};

	private final Action autoOptimizeProductionAndDistribution = 
			new AbstractAction("Production") {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JCheckBox) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				simulator.setAutoOptimizeProductionAndDistribution(selected);
				autoOptimizeDistribution.setEnabled(!selected);
			}
		}
	};

	private final Action runOptimization = 
			new AbstractAction(null, Icons.RECALC) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			working.set(true);
			updateActions();
			
			new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() {
					try {
						simulator.runOptimization();
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
	};

	private final Action initializeSim = new AbstractAction(null, Icons.INITIALIZE) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fireSimulationInitialize();
		}
	};

	private final Action stepSim = new AbstractAction(null, Icons.STEP) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fireSimulationAdvance(1);
		}
	};

	private final Action advanceSim = new AbstractAction(null, Icons.ADVANCE) {
		private static final long serialVersionUID = 4589751151727368209L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fireSimulationAdvance(5);
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
	public SimulationControlPane(Simulator simulator) {
		if(simulator == null) {
			throw new IllegalArgumentException(
					"Simulator cannot be null.");
		}
		this.simulator = simulator;
		
		toggleConnection.putValue(Action.SHORT_DESCRIPTION, 
				"Toggle the network connection.");
		autoOptimizeDistribution.putValue(Action.SHORT_DESCRIPTION, 
				"Optimize resource distribution at each time step.");
		autoOptimizeProductionAndDistribution.putValue(Action.SHORT_DESCRIPTION, 
				"Optimize resource production and distribution at each time step.");
		runOptimization.putValue(Action.SHORT_DESCRIPTION, 
				"Run the optimization routine.");
		initializeSim.putValue(Action.SHORT_DESCRIPTION, 
				"Initialize the simulation.");
		stepSim.putValue(Action.SHORT_DESCRIPTION, 
				"Step the simulation by one year.");
		advanceSim.putValue(Action.SHORT_DESCRIPTION, 
				"Advance the simulation by five years.");
		endSim.putValue(Action.SHORT_DESCRIPTION, 
				"Run the simulation until the end.");
		
		setLayout(new BorderLayout());
		timeSlider = new JSlider();
		timeSlider.setPaintTrack(false);
		timeSlider.setEnabled(false);
		
		add(timeSlider, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(new JButton(toggleConnection));
		buttonPanel.add(new JButton(initializeSim));
		buttonPanel.add(new JButton(stepSim));
		buttonPanel.add(new JButton(advanceSim));
		buttonPanel.add(new JButton(endSim));

		add(buttonPanel, BorderLayout.CENTER);

		JPanel optimizationPanel = new JPanel();
		optimizationPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		optimizationPanel.add(new JLabel("Auto-optimize: "));
		c.gridx++;
		JCheckBox optimizeDistributionCheck = new JCheckBox(autoOptimizeDistribution);
		optimizeDistributionCheck.setSelected(simulator.isAutoOptimizeDistribution());
		optimizationPanel.add(optimizeDistributionCheck, c);
		c.gridy++;
		JCheckBox optimizeProductionCheck = new JCheckBox(autoOptimizeProductionAndDistribution);
		optimizeProductionCheck.setSelected(simulator.isAutoOptimizeProductionAndDistribution());
		optimizationPanel.add(optimizeProductionCheck, c);
		c.gridy--;
		c.gridx++;
		c.gridheight = 2;
		c.fill = GridBagConstraints.BOTH;
		optimizationPanel.add(new JButton(runOptimization), c);
		add(optimizationPanel, BorderLayout.SOUTH);
		
		updateActions();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.event.ConnectionListener#connectionEventOccurred(edu.mit.sips.gui.event.ConnectionEvent)
	 */
	@Override
	public void connectionEventOccurred(ConnectionEvent e) {
		if(e.getConnection().isConnected()) {
			toggleConnection.putValue(
					Action.SMALL_ICON, Icons.CONNECTED);
		} else {
			toggleConnection.putValue(
					Action.SMALL_ICON, Icons.DISCONNECTED);
		}
	}

	/**
	 * Fire simulation advance.
	 *
	 * @param duration the duration
	 */
	private void fireSimulationAdvance(final long duration) {
		final JPanel panel = this;
		working.set(true);
		updateActions();
		
		new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground() {
				try {
					simulator.advanceSimulation(
							new SimulationControlEvent.Advance(
									panel, duration));
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
	 *
	 */
	private void fireSimulationInitialize() {
		final InitializationInputPanel input = new InitializationInputPanel();
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
				getTopLevelAncestor(), input, 
				"Initialize Simulation", JOptionPane.OK_CANCEL_OPTION)) {
			final JPanel panel = this;
			working.set(true);
			updateActions();
			
			new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() {
					try {
						simulator.initializeSimulation(
								new SimulationControlEvent.Initialize(
										panel, input.getStartTime(), 
										input.getEndTime()));
					} catch(IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(getTopLevelAncestor(), 
								ex.getMessage(), "Error", 
								JOptionPane.ERROR_MESSAGE);
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
	}

	/**
	 * Update actions.
	 */
	private void updateActions() {
		toggleConnection.setEnabled(!working.get());
		autoOptimizeDistribution.setEnabled(!working.get()
				&& !simulator.isAutoOptimizeProductionAndDistribution());
		autoOptimizeProductionAndDistribution.setEnabled(!working.get()
				&& simulator.isAutoOptimizeDistribution());
		runOptimization.setEnabled(!working.get()
				&& simulator.isInitialized() 
				&& !simulator.isCompleted());
		initializeSim.setEnabled(!working.get());
		stepSim.setEnabled(!working.get()
				&& simulator.isInitialized() 
				&& !simulator.isCompleted());
		advanceSim.setEnabled(!working.get()
				&& simulator.isInitialized() 
				&& !simulator.isCompleted());
		endSim.setEnabled(!working.get()
				&& simulator.isInitialized() 
				&& !simulator.isCompleted());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		updateActions();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) { 
		timeSlider.setValue((int) simulator.getTime());
		updateActions();
	}
}
