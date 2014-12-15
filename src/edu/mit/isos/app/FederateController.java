package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos.app.elect.ElectFederate;
import edu.mit.isos.app.petrol.PetrolFederate;
import edu.mit.isos.app.social.SocialFederate;
import edu.mit.isos.app.water.WaterFederate;

public class FederateController {
	protected static Logger logger = Logger.getLogger("edu.mit.isos");
	private static enum Role {SOCIAL, WATER, ELECT, PETROL};
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.setLevel(Level.INFO);
		
		Collection<Role> roles = Arrays.asList(Role.SOCIAL, Role.WATER, Role.ELECT, Role.PETROL);

		multiThread(roles, 5, 5, 1000, 30.0);
		for(int itr : new int[]{1, 2, 4, 10, 20, 50}) {
			multiThread(roles, itr, 20, 1000, 30.0);
		}
		for(int stp : new int[]{100, 250, 500, 2500, 10000}) {
			multiThread(roles, 10, 20, stp, 30.0);
		}
		multiThread(roles, 2, 20, 250, 30.0);
		
		/*
		singleThread(5, 5, 1000, 30.0);
		for(int itr : new int[]{1, 2, 4, 10, 20, 50}) {
			singleThread(itr, 20, 1000, 30.0);
		}
		for(int stp : new int[]{100, 250, 500, 2500, 10000}) {
			singleThread(10, 20, stp, 30.0);
		}
		singleThread(2, 20, 250, 30.0);
		
		*/
	}
	
	public static void singleThread(int itr, final int rep, final long stp, final double dur) {
		try {
			new SingleFederate(itr, rep, stp).execute(dur);
		} catch (RTIexception | IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public static void multiThread(Collection<Role> roles, int itr, final int rep, final long stp, final double dur) {
		Thread social = null, water = null, elect = null, petrol = null;

		if(roles.contains(Role.SOCIAL)) {
			social = new Thread(new Runnable() {
				public void run() {
					try {
						new SocialFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			social.start();
		}

		if(roles.contains(Role.WATER)) {
			water = new Thread(new Runnable() {
				public void run() {
					try {
						new WaterFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			water.start();
		}
		
		if(roles.contains(Role.ELECT)) {
			elect = new Thread(new Runnable() {
				public void run() {
					try {
						new ElectFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			elect.start();
		}

		if(roles.contains(Role.PETROL)) {
			petrol = new Thread(new Runnable() {
				public void run() {
					try {
						new PetrolFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			petrol.start();
		}
		
		while((social != null && social.isAlive()) 
				&& (water != null && water.isAlive())
				&& (elect != null && elect.isAlive())
				&& (petrol != null && petrol.isAlive())) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
