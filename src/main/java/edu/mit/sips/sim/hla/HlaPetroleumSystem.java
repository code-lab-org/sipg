package edu.mit.sips.sim.hla;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIexception;

import java.util.HashMap;
import java.util.Map;

import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class HLA petroleum system.
 */
public class HlaPetroleumSystem extends HlaInfrastructureSystem implements PetroleumSystem {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	public static final String CLASS_NAME = "HLAobjectRoot.InfrastructureSystem.PetroleumSystem";
	
	public static final String 
	ELECTRICITY_CONSUMPTION_ATTRIBUTE = "ElectricityConsumption",
	PETROLEUM_DOMESTIC_PRICE_ATTRIBUTE = "PetroleumDomesticPrice",
	PETROLEUM_IMPORT_PRICE_ATTRIBUTE = "PetroleumImportPrice",
	PETROLEUM_EXPORT_PRICE_ATTRIBUTE = "PetroleumExportPrice";
	
	public static final String[] ATTRIBUTES = new String[]{
		NAME_ATTRIBUTE,
		SOCIETY_NAME_ATTRIBUTE,
		NET_CASH_FLOW_ATTRIBUTE,
		CAPITAL_EXPENSE_ATTRIBUTE,
		SUSTAINABILITY_NUMERATOR_ATTRIBUTE,
		SUSTAINABILITY_DENOMINATOR_ATTRIBUTE,
		ELECTRICITY_CONSUMPTION_ATTRIBUTE, 
		PETROLEUM_DOMESTIC_PRICE_ATTRIBUTE, 
		PETROLEUM_IMPORT_PRICE_ATTRIBUTE,
		PETROLEUM_EXPORT_PRICE_ATTRIBUTE
	};

	/**
	 * Creates the local petroleum system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param petroleumSystem the petroleum system
	 * @return the HLA petroleum system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaPetroleumSystem createLocalPetroleumSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			PetroleumSystem.Local petroleumSystem) throws RTIexception {
		HlaPetroleumSystem hlaSystem = new HlaPetroleumSystem(
				rtiAmbassador, encoderFactory, null);
		hlaSystem.setAttributes(petroleumSystem);
		return hlaSystem;
	}
	
	/**
	 * Creates the remote petroleum system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @return the HLA petroleum system
	 * @throws RTIexception the RTI exception
	 */
	public static HlaPetroleumSystem createRemotePetroleumSystem(
			RTIambassador rtiAmbassador, EncoderFactory encoderFactory,
			String instanceName) throws RTIexception {
		HlaPetroleumSystem hlaSystem = new HlaPetroleumSystem(
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
	
	private transient final HLAfloat64BE electricityConsumption;
	private transient final HLAfloat64BE petroleumDomesticPrice;
	private transient final HLAfloat64BE petroleumImportPrice;
	private transient final HLAfloat64BE petroleumExportPrice;

	/**
	 * Instantiates a new HLA petroleum system.
	 *
	 * @param rtiAmbassador the rti ambassador
	 * @param encoderFactory the encoder factory
	 * @param instanceName the instance name
	 * @throws RTIexception the RTI exception
	 */
	protected HlaPetroleumSystem(RTIambassador rtiAmbassador, 
			EncoderFactory encoderFactory, String instanceName) throws RTIexception {
		super(rtiAmbassador, encoderFactory, instanceName);
		electricityConsumption = encoderFactory.createHLAfloat64BE();
		petroleumDomesticPrice = encoderFactory.createHLAfloat64BE();
		petroleumImportPrice = encoderFactory.createHLAfloat64BE();
		petroleumExportPrice = encoderFactory.createHLAfloat64BE();
		attributeValues.put(getAttributeHandle(ELECTRICITY_CONSUMPTION_ATTRIBUTE), 
				electricityConsumption);
		attributeValues.put(getAttributeHandle(PETROLEUM_DOMESTIC_PRICE_ATTRIBUTE), 
				petroleumDomesticPrice);
		attributeValues.put(getAttributeHandle(PETROLEUM_IMPORT_PRICE_ATTRIBUTE), 
				petroleumImportPrice);
		attributeValues.put(getAttributeHandle(PETROLEUM_EXPORT_PRICE_ATTRIBUTE), 
				petroleumExportPrice);
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
	public double getPetroleumDomesticPrice() {
		return petroleumDomesticPrice.getValue();
	}

	@Override
	public double getPetroleumExportPrice() {
		return petroleumExportPrice.getValue();
	}

	@Override
	public double getPetroleumImportPrice() {
		return petroleumImportPrice.getValue();
	}

	@Override
	public void setAttributes(InfrastructureSystem system) {
		super.setAttributes(system);
		if(system instanceof PetroleumSystem) {
			PetroleumSystem petroleumSystem = (PetroleumSystem) system;
			electricityConsumption.setValue(petroleumSystem.getElectricityConsumption());
			petroleumDomesticPrice.setValue(petroleumSystem.getPetroleumDomesticPrice());
			petroleumImportPrice.setValue(petroleumSystem.getPetroleumImportPrice());
			petroleumExportPrice.setValue(petroleumSystem.getPetroleumExportPrice());
			sustainabilityMetricNumerator.setValue(petroleumSystem.getReservoirVolume());
			sustainabilityMetricDenominator.setValue(petroleumSystem.getReservoirWithdrawals());
		}
	}

	@Override
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirVolume() {
		return sustainabilityMetricNumerator.getValue();
	}

	@Override
	public double getReservoirWithdrawals() {
		return sustainabilityMetricDenominator.getValue();
	}
}
