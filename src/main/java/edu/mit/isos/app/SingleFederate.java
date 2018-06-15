package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.util.Arrays;

import org.apache.log4j.Logger;

import edu.mit.isos.app.elect.LocalElectElement;
import edu.mit.isos.app.hla.ISOSnullAmbassador;
import edu.mit.isos.app.petrol.LocalPetrolElement;
import edu.mit.isos.app.social.LocalSocialElement;
import edu.mit.isos.app.water.LocalWaterElement;
import edu.mit.isos.app.water.WaterController;
import edu.mit.isos.app.water.WaterPipeline;
import edu.mit.isos.app.water.WaterPlant;
import edu.mit.isos.core.context.Scenario;
import edu.mit.isos.core.hla.ISOSambassador;

public class SingleFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger(SingleFederate.class);
	
	private LocalSocialElement e_s1;
	private LocalSocialElement e_s2;
	private LocalSocialElement e_s3;
	private LocalWaterElement e_w1;
	private LocalWaterElement e_w2;
	private LocalWaterElement e_w3;
	private LocalElectElement e_e1;
	private LocalElectElement e_e2;
	private LocalElectElement e_e3;
	private LocalPetrolElement e_o1;
	private LocalPetrolElement e_o2;
	private LocalPetrolElement e_o3;

	public SingleFederate(int numIterations, int numReplications, long timeStep) {
		super("Single", numIterations, numReplications, timeStep);
	}
	
	public Scenario buildScenario(double stepsPerYear) {
		e_s1 = new LocalSocialElement("e_S1", l_aa, 0.065/stepsPerYear, 4.0/stepsPerYear, 1.0/stepsPerYear, 3.0, 0.07/stepsPerYear);
		e_s2 = new LocalSocialElement("e_S2", l_bb, 0.050/stepsPerYear, 3.0/stepsPerYear, 1.2/stepsPerYear, 1.0, 0.05/stepsPerYear);
		e_s3 = new LocalSocialElement("e_S3", l_cc, 0.060/stepsPerYear, 3.5/stepsPerYear, 1.0/stepsPerYear, 6.0, 0.06/stepsPerYear);
		
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
		
		e_o1 = new LocalPetrolElement("e_P1", l_aa, 0.5, 1.0, 5000);
		e_o2 = new LocalPetrolElement("e_P2", l_bb, 0.8, 1.0, 1000);
		e_o3 = new LocalPetrolElement("e_P3", l_cc, 0.6, 1.0, 4000);
		
		e_e1 = new LocalElectElement("e_E1", l_aa, 1.0/stepsPerYear, 0.25);
		e_e2 = new LocalElectElement("e_E2", l_bb, 0.5/stepsPerYear, 0.30);
		e_e3 = new LocalElectElement("e_E3", l_cc, 0.8/stepsPerYear, 0.25);
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3, 
						e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8, e_w9, 
						e_o1, e_o2, e_o3, 
						e_e1, e_e2, e_e3));
	}

	@Override
	public ISOSambassador getAmbassador() throws RTIexception {
		return new ISOSnullAmbassador();
	}

}
