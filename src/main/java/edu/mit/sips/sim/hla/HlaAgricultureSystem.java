package edu.mit.sips.sim.hla;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

/**
 * The Class HLA water system.
 */
public class HlaAgricultureSystem extends HlaInfrastructureSystem implements AgricultureSystem {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;
	public static final String 
	CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.AgricultureSystem";
	
	public static final String 
	WATER_CONSUMPTION_ATTRIBUTE = "WaterConsumption",
	FOOD_DOMESTIC_PRICE_ATTRIBUTE = "FoodDomesticPrice",
	FOOD_IMPORT_PRICE_ATTRIBUTE = "FoodImportPrice",
	FOOD_EXPORT_PRICE_ATTRIBUTE = "FoodExportPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		SUSTAINABILITY_NUMERATOR_ATTRIBUTE,
		SUSTAINABILITY_DENOMINATOR_ATTRIBUTE,
		WATER_CONSUMPTION_ATTRIBUTE,
		FOOD_DOMESTIC_PRICE_ATTRIBUTE,
		FOOD_IMPORT_PRICE_ATTRIBUTE,
		FOOD_EXPORT_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local water system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param agricultureSystem the agriculture system
	 * @return the HLA water system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaAgricultureSystem createLocalAgricultureSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			AgricultureSystem.Local agricultureSystem) throws RTIexception {
		HlaAgricultureSystem hlaSystem = new HlaAgricultureSystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(agricultureSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote agriculture system.
	 *
	 * @param rtiAmbassador the RTI ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the HLA agriculture system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaAgricultureSystem createRemoteAgricultureSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HlaAgricultureSystem hlaSystem = new HlaAgricultureSystem(
				rtiAmbassador, encoderFactory, instanceName);
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
	
	private transient final HLAfloat64BE waterConsumption;
	private transient final HLAfloat64BE foodDomesticPrice;
	private transient final HLAfloat64BE foodImportPrice;
	private transient final HLAfloat64BE foodExportPrice;

	/**
	 * Instantiates a new HLA agriculture system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @param agricultureSystem the agriculture system
	 * @throws RTIexception the RTI exception
	 */
	protected HlaAgricultureSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		waterConsumption = encoderFactory.createHLAfloat64BE();
		foodDomesticPrice = encoderFactory.createHLAfloat64BE();
		foodImportPrice = encoderFactory.createHLAfloat64BE();
		foodExportPrice = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(WATER_CONSUMPTION_ATTRIBUTE), 
				waterConsumption);
		attributeValues.put(getAttributeHandle(FOOD_DOMESTIC_PRICE_ATTRIBUTE), 
				foodDomesticPrice);
		attributeValues.put(getAttributeHandle(FOOD_IMPORT_PRICE_ATTRIBUTE), 
				foodImportPrice);
		attributeValues.put(getAttributeHandle(FOOD_EXPORT_PRICE_ATTRIBUTE), 
				foodExportPrice);
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
	public double getFoodDomesticPrice() {
		return foodDomesticPrice.getValue();
	}

	@Override
	public double getFoodExportPrice() {
		return foodExportPrice.getValue();
	}

	@Override
	public double getFoodImportPrice() {
		return foodImportPrice.getValue();
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
		if(system instanceof AgricultureSystem) {
			AgricultureSystem agricultureSystem = (AgricultureSystem) system;
			waterConsumption.setValue(agricultureSystem.getWaterConsumption());
			foodDomesticPrice.setValue(agricultureSystem.getFoodDomesticPrice());
			foodImportPrice.setValue(agricultureSystem.getFoodImportPrice());
			foodExportPrice.setValue(agricultureSystem.getFoodExportPrice());
			sustainabilityMetricNumerator.setValue(agricultureSystem.getFoodProduction());
			sustainabilityMetricDenominator.setValue(agricultureSystem.getTotalFoodSupply());
		}
	}

	@Override
	public double getFoodSecurity() {
		return getTotalFoodSupply() == 0 ? 1 
				: (getFoodProduction() / getTotalFoodSupply());
	}

	@Override
	public double getFoodProduction() {
		return sustainabilityMetricNumerator.getValue();
	}

	@Override
	public double getTotalFoodSupply() {
		return sustainabilityMetricDenominator.getValue();
	}
}
