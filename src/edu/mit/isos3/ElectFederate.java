package edu.mit.isos3;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos3.element.LocalElectElement;
import edu.mit.isos3.element.PetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.element.WaterElement;
import edu.mit.isos3.hla.HLAobject;
import edu.mit.isos3.hla.ISOSambassador;
import edu.mit.isos3.hla.ISOSelement;

public class ElectFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	private LocalElectElement e_e1;
	private LocalElectElement e_e2;
	private LocalElectElement e_e3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		new ElectFederate(4, 1, 1000).execute();
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

	@Override
	public void postInitializeSetUp(ISOSambassador amb) {
		setUpElect(e_e1, amb.getElements());
		setUpElect(e_e2, amb.getElements());
		setUpElect(e_e3, amb.getElements());
	}
	
	private static void setUpElect(LocalElectElement elect, Collection<ISOSelement> objects) {
		for(HLAobject object : objects) {
			if(object instanceof PetrolElement) {
				PetrolElement petrol = (PetrolElement) object;
				if(elect.getLocation().equals(petrol.getLocation())) {
					elect.setPetrolSupplier(petrol);
				}
			}
			if(object instanceof PetrolElement) {
				PetrolElement petrol = (PetrolElement) object;
				if(elect.getLocation().equals(petrol.getLocation())) {
					elect.setCustomer(petrol);
				}
			}
			if(object instanceof SocialElement) {
				SocialElement social = (SocialElement) object;
				if(elect.getLocation().equals(social.getLocation())) {
					elect.setCustomer(social);
				}
			}
			if(object instanceof WaterElement) {
				WaterElement water = (WaterElement) object;
				if(elect.getLocation().equals(water.getLocation())) {
					elect.setCustomer(water);
				}
			}
		}
	}
}
