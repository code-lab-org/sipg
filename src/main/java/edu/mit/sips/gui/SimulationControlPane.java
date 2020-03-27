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

import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.Simulator;

/**
 * The Class SimulationControlPanel.
 */
public class SimulationControlPane extends JPanel implements UpdateListener {
	private static final long serialVersionUID = -7014074954503228524L;

	private final DataFrame frame;
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
	public SimulationControlPane(DataFrame frame, Simulator simulator) {
		if(simulator == null) {
			throw new IllegalArgumentException(
					"Simulator cannot be null.");
		}
		this.simulator = simulator;
		this.frame = frame;
		
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
	 *
	 */
	private void fireSimulationInitialize() {
		frame.autoSave();
		final InitializationInputPanel input = new InitializationInputPanel();
		if(true /* JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
				getTopLevelAncestor(), input, 
				"Initialize Simulation", JOptionPane.OK_CANCEL_OPTION)*/) {
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
