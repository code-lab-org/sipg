package edu.mit.isos.app.petrol;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos.app.DefaultFederate;
import edu.mit.isos.app.hla.ISOSfedAmbassador;
import edu.mit.isos.core.context.Scenario;

public class PetrolFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger(PetrolFederate.class);
	private LocalPetrolElement e_o1;
	private LocalPetrolElement e_o2;
	private LocalPetrolElement e_o3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		
		final int itr = 1;
		final int rep = 1;
		final long stp = 1000;
		final double dur = 5.0;
		
		new PetrolFederate(itr, rep, stp).execute(dur);
	}

	public PetrolFederate(int numIterations, int numReplications, long timeStep) throws RTIexception {
		super("Petrol", numIterations, numReplications, timeStep, new ISOSfedAmbassador());
	}

	@Override
	public Scenario buildScenario(double stepsPerYear) {
		e_o1 = new LocalPetrolElement("e_P1", l_aa, 0.5, 1.0, 5000);
		e_o2 = new LocalPetrolElement("e_P2", l_bb, 0.8, 1.0, 1000);
		e_o3 = new LocalPetrolElement("e_P3", l_cc, 0.6, 1.0, 4000);
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_o1, e_o2, e_o3));
	}
}
