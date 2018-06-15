package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
		
		Collection<Role> roles = new HashSet<Role>();
		for(String arg : args) {
			if(Role.valueOf(arg) != null) {
				roles.add(Role.valueOf(arg));
			}
		}
		multiThread(roles, 5, 2, 1000, 30.0);
		for(int stp : new int[]{250}) {
			multiThread(roles, 10, 20, stp, 30.0);
		}
		/*
		multiThread(roles, 5, 1, 1000, 30.0);
		for(int itr : new int[]{1, 2, 4, 10, 20, 50}) {
			multiThread(roles, itr, 20, 1000, 30.0);
		}
		for(int stp : new int[]{100, 250, 500, 2500, 10000}) {
			multiThread(roles, 10, 20, stp, 30.0);
		}
		multiThread(roles, 2, 20, 250, 30.0);
		*/
		/*
		singleThread(5, 100, 1000, 30.0);
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
		Map<Role, Boolean> running = Collections.synchronizedMap(new HashMap<Role, Boolean>());

		if(roles.contains(Role.SOCIAL)) {
			running.put(Role.SOCIAL, true);
			new Thread(new Runnable() {
				public void run() {
					try {
						new SocialFederate(itr, rep, stp).execute(dur);
						running.put(Role.SOCIAL, false);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			}).start();
		}

		if(roles.contains(Role.WATER)) {
			running.put(Role.WATER, true);
			new Thread(new Runnable() {
				public void run() {
					try {
						new WaterFederate(itr, rep, stp).execute(dur);
						running.put(Role.WATER, false);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		if(roles.contains(Role.ELECT)) {
			running.put(Role.ELECT, true);
			new Thread(new Runnable() {
				public void run() {
					try {
						new ElectFederate(itr, rep, stp).execute(dur);
						running.put(Role.ELECT, false);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			}).start();
		}

		if(roles.contains(Role.PETROL)) {
			running.put(Role.PETROL, true);
			new Thread(new Runnable() {
				public void run() {
					try {
						new PetrolFederate(itr, rep, stp).execute(dur);
						running.put(Role.PETROL, false);
					} catch (RTIexception | IOException e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		while((roles.contains(Role.SOCIAL) && running.get(Role.SOCIAL))
				|| (roles.contains(Role.WATER) && running.get(Role.WATER))
				|| (roles.contains(Role.ELECT) && running.get(Role.ELECT))
				|| (roles.contains(Role.PETROL) && running.get(Role.PETROL))) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
