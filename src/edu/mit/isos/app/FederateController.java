package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
		
		Collection<Role> roles = Arrays.asList(Role.WATER);

		multiThread(roles, 5, 1, 1000, 30.0);
		for(int itr : new int[]{1, 2, 4, 10}) {
			multiThread(roles, itr, 20, 1000, 30.0);
		}
		/*
		for(int itr : new int[]{1, 2, 4, 10, 20, 50}) {
			multiThread(roles, itr, 20, 1000, 30.0);
		}
		for(int stp : new int[]{100, 250, 500, 2500, 10000}) {
			multiThread(roles, 10, 20, stp, 30.0);
		}
		multiThread(roles, 2, 20, 250, 30.0);
		
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
		Map<Role, Thread> federates = new HashMap<Role, Thread>();

		if(roles.contains(Role.SOCIAL)) {
			Thread social = new Thread(new Runnable() {
				public void run() {
					try {
						new SocialFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			federates.put(Role.SOCIAL, social);
			social.start();
			while(!social.isAlive()) {
				Thread.yield();
			}
		}

		if(roles.contains(Role.WATER)) {
			Thread water = new Thread(new Runnable() {
				public void run() {
					try {
						new WaterFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			federates.put(Role.WATER, water);
			water.start();
			while(!water.isAlive()) {
				Thread.yield();
			}
		}
		
		if(roles.contains(Role.ELECT)) {
			Thread elect = new Thread(new Runnable() {
				public void run() {
					try {
						new ElectFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			federates.put(Role.ELECT, elect);
			elect.start();
			while(!elect.isAlive()) {
				Thread.yield();
			}
		}

		if(roles.contains(Role.PETROL)) {
			Thread petrol = new Thread(new Runnable() {
				public void run() {
					try {
						new PetrolFederate(itr, rep, stp).execute(dur);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			});
			federates.put(Role.PETROL, petrol);
			petrol.start();
			while(!petrol.isAlive()) {
				Thread.yield();
			}
		}
		
		while((roles.contains(Role.SOCIAL) && federates.get(Role.SOCIAL).isAlive()) 
				&& (roles.contains(Role.WATER) && federates.get(Role.WATER).isAlive()) 
				&& (roles.contains(Role.ELECT) && federates.get(Role.ELECT).isAlive()) 
				&& (roles.contains(Role.PETROL) && federates.get(Role.PETROL).isAlive()) ) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
