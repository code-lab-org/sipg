package edu.mit.isos3;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos3.element.ElectElement;
import edu.mit.isos3.element.LocalPetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.hla.HLAobject;
import edu.mit.isos3.hla.ISOSambassador;
import edu.mit.isos3.hla.ISOSelement;

public class PetrolFederate extends DefaultFederate {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	private LocalPetrolElement e_o1;
	private LocalPetrolElement e_o2;
	private LocalPetrolElement e_o3;
	
	public static void main(String[] args) throws RTIexception, IOException {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		new PetrolFederate(4, 1, 1000).execute();
	}

	public PetrolFederate(int numIterations, int numReplications, long timeStep) {
		super("Petrol", numIterations, numReplications, timeStep);
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

	@Override
	public void postInitializeSetUp(ISOSambassador amb) {
		setUpPetrol(e_o1, amb.getElements());
		setUpPetrol(e_o2, amb.getElements());
		setUpPetrol(e_o3, amb.getElements());
	}
	
	private static void setUpPetrol(LocalPetrolElement petrol, Collection<ISOSelement> objects) {
		for(HLAobject object : objects) {
			if(object instanceof ElectElement) {
				ElectElement elect = (ElectElement) object;
				if(petrol.getLocation().equals(elect.getLocation())) {
					petrol.setElectSupplier(elect);
				}
			}
			if(object instanceof ElectElement) {
				ElectElement elect = (ElectElement) object;
				if(petrol.getLocation().equals(elect.getLocation())) {
					petrol.setCustomer(elect);
				}
			}
			if(object instanceof SocialElement) {
				SocialElement social = (SocialElement) object;
				if(petrol.getLocation().equals(social.getLocation())) {
					petrol.setCustomer(social);
				}
			}
		}
	}
}
