package edu.mit.isos3;

import java.io.IOException;

import hla.rti1516e.exceptions.RTIexception;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FederateController {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.setLevel(Level.OFF);
		/*
		for(int itr : new int[]{4}) {
			new DemoScript(itr,20,1000).execute();
		}
		*/
		/*
		for(int itr : new int[]{100, 100, 100, 100, 100}) {
			new DemoScript(itr,20,1000).execute();
		}
		for(int itr : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100}) {
			new DemoScript(itr,50,1000).execute();
		}
		*/
		/*
		for(int itr : new int[]{100, 100, 100, 100, 100}) {
			new DemoScript(itr,20,1000).execute();
		}
		for(int stp : new int[]{100, 250, 500, 1000, 2500, 10000}) {
			new DemoScript(40,50,stp).execute();
		}
		*/
		
		final int itr = 1;
		final int rep = 1;
		final long stp = 1000;
		
		new Thread(new Runnable() {
			public void run() {
				try {
					new ElectFederate(itr, rep, stp).execute();
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					new PetrolFederate(itr, rep, stp).execute();
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					new SocialFederate(itr, rep, stp).execute();
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					new WaterFederate(itr, rep, stp).execute();
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		}).start();
		
	}
}
