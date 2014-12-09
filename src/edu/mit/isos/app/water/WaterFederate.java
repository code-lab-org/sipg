package edu.mit.isos.app.water;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos.app.DefaultFederate;
import edu.mit.isos.core.context.Scenario;

public class WaterFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	private LocalWaterElement e_w1;
	private LocalWaterElement e_w2;
	private LocalWaterElement e_w3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		
		final int itr = 1;
		final int rep = 1;
		final long stp = 1000;
		final double dur = 5.0;
		
		new WaterFederate(itr, rep, stp).execute(dur);
	}

	public WaterFederate(int numIterations, int numReplications, long timeStep) {
		super("Water", numIterations, numReplications, timeStep);
	}

	@Override
	public Scenario buildScenario(double stepsPerYear) {
		e_w1 = new LocalWaterElement("e_W1", l_aa, 1.0, 0.9, 200);
		e_w2 = new LocalWaterElement("e_W2", l_bb, 1.0, 0.9, 150);
		e_w3 = new LocalWaterElement("e_W3", l_cc, 1.0, 0.9, 250);
		WaterPlant e_w4 = new WaterPlant("e_W4", l_aa, (long)(2014000+0*stepsPerYear), 0.5/stepsPerYear, 4.5);
		WaterPlant e_w5 = new WaterPlant("e_W5", l_cc, (long)(2014000+0*stepsPerYear), 0.4/stepsPerYear, 4.5);
		WaterPlant e_w6 = new WaterPlant("e_W6", l_cc, (long)(2014000+5*stepsPerYear), 0.6/stepsPerYear, 4.5);
		WaterPipeline e_w7 = new WaterPipeline("e_W7", l_ab, 0.02/stepsPerYear, 0.9, 2.5);
		WaterPipeline e_w8 = new WaterPipeline("e_W8", l_cb, 0.02/stepsPerYear, 0.9, 2.0);
		WaterController e_w9 = new WaterController("e_W9", l_aa, 
				Arrays.asList(e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8));
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8, e_w9));
	}
}
