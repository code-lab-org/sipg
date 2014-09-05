package edu.mit.isos3;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos3.element.LocalSocialElement;

public class SocialFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	private LocalSocialElement e_s1;
	private LocalSocialElement e_s2;
	private LocalSocialElement e_s3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		new SocialFederate(4, 1, 1000).execute();
	}

	public SocialFederate(int numIterations, int numReplications, long timeStep) {
		super("Social", numIterations, numReplications, timeStep);
	}

	@Override
	public Scenario buildScenario(double stepsPerYear) {
		e_s1 = new LocalSocialElement("e_S1", l_aa, 0.065/stepsPerYear, 4.0/stepsPerYear, 1.0/stepsPerYear, 3.0, 0.07/stepsPerYear);
		e_s2 = new LocalSocialElement("e_S2", l_bb, 0.050/stepsPerYear, 3.0/stepsPerYear, 1.2/stepsPerYear, 1.0, 0.05/stepsPerYear);
		e_s3 = new LocalSocialElement("e_S3", l_cc, 0.060/stepsPerYear, 3.5/stepsPerYear, 1.0/stepsPerYear, 6.0, 0.06/stepsPerYear);
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3));
	}
}
