package edu.mit.sips.sim.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.encoding.HLAinteger64BE;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.FoodUnits;
import edu.mit.sips.units.OilUnits;
import edu.mit.sips.units.TimeUnits;
import edu.mit.sips.units.WaterUnits;

/**
 * The Class HLA social system.
 */
public class HlaSocialSystem extends HlaInfrastructureSystem implements SocialSystem {
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.SocialSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	FOOD_CONSUMPTION_ATTRIBUTE = "FoodConsumption",
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption",
	PETROLEUM_CONSUMPTION_ATTRIBUTE = "PetroleumConsumption",
	POPULATION_ATTRIBUTE = "Population",
	DOMESTIC_PRODUCT_ATTRIBUTE = "DomesticProduct";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE,
		FOOD_CONSUMPTION_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE,
		PETROLEUM_CONSUMPTION_ATTRIBUTE,
		POPULATION_ATTRIBUTE,
		DOMESTIC_PRODUCT_ATTRIBUTE
	};
	
	/**
	 * Creates the local social system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param socialSystem the social system
	 * @return the hL asocial system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaSocialSystem createLocalSocialSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			SocialSystem.Local socialSystem) throws RTIexception {
		HlaSocialSystem hlaSystem = new HlaSocialSystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(socialSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote social system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the hL asocial system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaSocialSystem createRemoteSocialSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HlaSocialSystem hlaSystem = new HlaSocialSystem(rtiAmbassador, 
				encoderFactory, instanceName);
		//hlaSystem.requestAttributeValueUpdate();
		return hlaSystem;
	}

	/**
	 * Publish all.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void publishAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet attributeHandleSet = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			attributeHandleSet.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.publishObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
				attributeHandleSet);
	}
	
	/**
	 * Subscribe all.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @throws RTIexception the RTI exception
	 */
	public static void subscribeAll(RTIambassador rtiAmbassador) 
			throws RTIexception {
		AttributeHandleSet attributeHandleSet = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attributeName : ATTRIBUTES) {
			attributeHandleSet.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
					attributeName));
		}
		rtiAmbassador.subscribeObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME), 
				attributeHandleSet);
	}

	private transient final HLAfloat64BE electricityConsumption;
	private transient final HLAfloat64BE foodConsumption;
	private transient final HLAfloat64BE waterConsumption;
	private transient final HLAfloat64BE petroleumConsumption;
	private transient final HLAfloat64BE domesticProduct; // TODO unused -- now calculated locally
	private transient final HLAinteger64BE population;
	
	/**
	 * Instantiates a new hL asocial system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	protected HlaSocialSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		electricityConsumption = encoderFactory.createHLAfloat64BE();
		foodConsumption = encoderFactory.createHLAfloat64BE();
		waterConsumption = encoderFactory.createHLAfloat64BE();
		petroleumConsumption = encoderFactory.createHLAfloat64BE();
		domesticProduct = encoderFactory.createHLAfloat64BE();
		population = encoderFactory.createHLAinteger64BE();
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(FOOD_CONSUMPTION_ATTRIBUTE), 
				foodConsumption);
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(PETROLEUM_CONSUMPTION_ATTRIBUTE), 
				petroleumConsumption);
		attributeValues.put(getAttributeHandle(DOMESTIC_PRODUCT_ATTRIBUTE), 
				domesticProduct);
		attributeValues.put(getAttributeHandle(POPULATION_ATTRIBUTE), 
				population);
	}
	
	@Override
	public String[] getAttributeNames() {
		return ATTRIBUTES;
	}
	
	@Override
	public Map<AttributeHandle, DataElement> getAttributeValues() {
		return new HashMap<AttributeHandle,DataElement>(attributeValues);
	}

	@Override
	public double getElectricityConsumption() {
		return electricityConsumption.getValue();
	}

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public double getFoodConsumption() {
		return foodConsumption.getValue();
	}

	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	@Override
	public String getObjectClassName() {
		return CLASS_NAME;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public double getPetroleumConsumption() {
		return petroleumConsumption.getValue();
	}

	@Override
	public long getPopulation() {
		return population.getValue();
	}

	@Override
	public double getWaterConsumption() {
		return waterConsumption.getValue();
	}

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	@Override
	public void setAttributes(InfrastructureSystem system) {
		super.setAttributes(system);
		if(system instanceof SocialSystem) {
			SocialSystem socialSystem = (SocialSystem) system;
			electricityConsumption.setValue(socialSystem.getElectricityConsumption());
			foodConsumption.setValue(socialSystem.getFoodConsumption());
			waterConsumption.setValue(socialSystem.getWaterConsumption());
			petroleumConsumption.setValue(socialSystem.getPetroleumConsumption());
			population.setValue(socialSystem.getPopulation());
		}
	}
}
