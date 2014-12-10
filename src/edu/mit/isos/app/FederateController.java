package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.io.IOException;

import net.sf.ohla.rti.RTI;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.mit.isos.app.elect.ElectFederate;
import edu.mit.isos.app.petrol.PetrolFederate;
import edu.mit.isos.app.social.SocialFederate;
import edu.mit.isos.app.water.WaterFederate;

public class FederateController {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.setLevel(Level.WARN);
		
		ohlaRTI();
		multiThread(5, 5, 1000, 30.0);
		
		/*
		for(int itr : new int[]{1, 2, 4, 10, 20, 50}) {
			multiThread(itr, 20, 1000, 30.0);
		}
		for(int stp : new int[]{100, 250, 500, 2500, 10000}) {
			multiThread(10, 20, stp, 30.0);
		}
		*/
		
		/*
		multiThread(2, 50, 250, 30.0);
		multiThread(8, 50, 250, 30.0);
		*/
	}
	
	public static void ohlaRTI() {
		new Thread(new Runnable() {
			public void run() {
				new RTI();
			}
		}).start();
	}
	
	public static void singleThread(int itr, final int rep, final long stp, final double dur) {
		try {
			new SingleFederate(itr, rep, stp).execute(dur);
		} catch (RTIexception | IOException e) {
			logger.error(e);
		}
	}
	
	public static void multiThread(int itr, final int rep, final long stp, final double dur) {
		Thread elect = new Thread(new Runnable() {
			public void run() {
				try {
					new ElectFederate(itr, rep, stp).execute(dur);
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		});
		
		Thread petrol = new Thread(new Runnable() {
			public void run() {
				try {
					new PetrolFederate(itr, rep, stp).execute(dur);
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		});
		
		Thread social = new Thread(new Runnable() {
			public void run() {
				try {
					new SocialFederate(itr, rep, stp).execute(dur);
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		});
		
		Thread water = new Thread(new Runnable() {
			public void run() {
				try {
					new WaterFederate(itr, rep, stp).execute(dur);
				} catch (RTIexception | IOException e) {
					logger.error(e);
				}
			}
		});
		
		elect.start();
		petrol.start();
		social.start();
		water.start();
		
		while(elect.isAlive() && petrol.isAlive() && social.isAlive() && water.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
	}
}
