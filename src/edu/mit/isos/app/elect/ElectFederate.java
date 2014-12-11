package edu.mit.isos.app.elect;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos.app.DefaultFederate;
import edu.mit.isos.core.context.Scenario;

public class ElectFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger(ElectFederate.class);
	private LocalElectElement e_e1;
	private LocalElectElement e_e2;
	private LocalElectElement e_e3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		
		final int itr = 1;
		final int rep = 1;
		final long stp = 1000;
		final double dur = 5.0;
		
		new ElectFederate(itr, rep, stp).execute(dur);
	}

	public ElectFederate(int numIterations, int numReplications, long timeStep) {
		super("Elect", numIterations, numReplications, timeStep);
	}

	@Override
	public Scenario buildScenario(double stepsPerYear) {
		e_e1 = new LocalElectElement("e_E1", l_aa, 1.0/stepsPerYear, 0.25);
		e_e2 = new LocalElectElement("e_E2", l_bb, 0.5/stepsPerYear, 0.30);
		e_e3 = new LocalElectElement("e_E3", l_cc, 0.8/stepsPerYear, 0.25);
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_e1, e_e2, e_e3));
	}
}
