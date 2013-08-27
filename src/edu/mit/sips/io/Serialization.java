package edu.mit.sips.io;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.LifecycleModel;
import edu.mit.sips.core.MutableSimpleLifecycleModel;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.MutableElectricityElement;
import edu.mit.sips.core.petroleum.MutablePetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.PopulationModel;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Serialization.
 */
public final class Serialization {
	private static final GsonBuilder gsonBuilder = new GsonBuilder();
	
	static {
		gsonBuilder.registerTypeAdapter(LifecycleModel.class, 
				new InterfaceAdapter<LifecycleModel>());
		gsonBuilder.registerTypeAdapter(PopulationModel.class, 
				new InterfaceAdapter<PopulationModel>());
		gsonBuilder.registerTypeAdapter(DemandModel.class, 
				new InterfaceAdapter<DemandModel>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.class, 
				new InterfaceAdapter<AgricultureSystem>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.Local.class, 
				new InterfaceAdapter<AgricultureSystem.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.Remote.class, 
				new InterfaceAdapter<AgricultureSystem.Remote>());
		gsonBuilder.registerTypeAdapter(AgricultureSoS.Local.class, 
				new InterfaceAdapter<AgricultureSoS.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSoS.class, 
				new InterfaceAdapter<AgricultureSoS>());
		gsonBuilder.registerTypeAdapter(WaterSystem.class, 
				new InterfaceAdapter<WaterSystem>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Local.class, 
				new InterfaceAdapter<WaterSystem.Local>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Remote.class, 
				new InterfaceAdapter<WaterSystem.Remote>());
		gsonBuilder.registerTypeAdapter(WaterSoS.Local.class, 
				new InterfaceAdapter<WaterSoS.Local>());
		gsonBuilder.registerTypeAdapter(WaterSoS.class, 
				new InterfaceAdapter<WaterSoS>());
		gsonBuilder.registerTypeAdapter(PetroleumSystem.class, 
				new InterfaceAdapter<PetroleumSystem>());
		gsonBuilder.registerTypeAdapter(PetroleumSoS.class, 
				new InterfaceAdapter<PetroleumSoS>());
		gsonBuilder.registerTypeAdapter(ElectricitySystem.class, 
				new InterfaceAdapter<ElectricitySystem>());
		gsonBuilder.registerTypeAdapter(ElectricitySoS.class, 
				new InterfaceAdapter<ElectricitySoS>());
		gsonBuilder.registerTypeAdapter(SocialSystem.class, 
				new InterfaceAdapter<SocialSystem>());
		gsonBuilder.registerTypeAdapter(SocialSystem.Local.class, 
				new InterfaceAdapter<SocialSystem.Local>());
		gsonBuilder.registerTypeAdapter(SocialSystem.Remote.class, 
				new InterfaceAdapter<SocialSystem.Remote>());
		gsonBuilder.registerTypeAdapter(SocialSoS.class, 
				new InterfaceAdapter<SocialSoS>());
		gsonBuilder.registerTypeAdapter(Society.class, 
				new InterfaceAdapter<Society>());
		gsonBuilder.registerTypeAdapter(AgricultureElement.class, 
				new InterfaceAdapter<AgricultureElement>());
		gsonBuilder.registerTypeAdapter(WaterElement.class, 
				new InterfaceAdapter<WaterElement>());
		gsonBuilder.registerTypeAdapter(PetroleumElement.class, 
				new InterfaceAdapter<PetroleumElement>());
		gsonBuilder.registerTypeAdapter(ElectricityElement.class, 
				new InterfaceAdapter<ElectricityElement>());
		gsonBuilder.registerTypeAdapter(PriceModel.class, 
				new InterfaceAdapter<PriceModel>());
		gsonBuilder.registerTypeAdapter(DomesticProductionModel.class, 
				new InterfaceAdapter<DomesticProductionModel>());
	}
	
	private static Gson gson = gsonBuilder.create();
	
	/**
	 * Serialize.
	 *
	 * @param country the country
	 * @return the string
	 */
	public static String serialize(Country country) {
		return getGson().toJson(country);
	}
	
	/**
	 * Deserialize.
	 *
	 * @param json the json
	 * @return the country
	 */
	public static Country deserialize(String json) {
		Country country = getGson().fromJson(json, Country.class);
		recursiveReplaceCircularReferences(country);
		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local){
			List<? extends AgricultureElement> elements = ((AgricultureSystem.Local)country.getAgricultureSystem()).getInternalElements();
			for(AgricultureElement element : elements) {
				((AgricultureSystem.Local)country.getAgricultureSystem()).removeElement(element);
				MutableAgricultureElement mutable = (MutableAgricultureElement) element.getMutableElement();
				MutableAgricultureElement template = (MutableAgricultureElement) 
						element.getTemplate().createElement(
								((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeInitialized(), 
								element.getOrigin(), element.getDestination()).getMutableElement();
				((MutableSimpleLifecycleModel)template.getLifecycleModel()).setOperationsDuration(
						((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationsDuration());
				mutable.setDistributionEfficiency(template.getDistributionEfficiency());
				//mutable.setInitialFoodInput(template.getInitialFoodInput());
				//mutable.setInitialLandArea(template.getInitialLandArea());
				mutable.setLifecycleModel(template.getLifecycleModel());
				mutable.setMaxFoodInput(template.getMaxFoodInput());
				mutable.setMaxLandArea(template.getMaxLandArea());
				mutable.setProduct(template.getProduct());
				mutable.setVariableOperationsCostOfFoodDistribution(template.getVariableOperationsCostOfFoodDistribution());
				((AgricultureSystem.Local)country.getAgricultureSystem()).addElement(mutable.createElement());
			}
		}
		if(country.getWaterSystem() instanceof WaterSystem.Local){
			List<? extends WaterElement> elements = ((WaterSystem.Local)country.getWaterSystem()).getInternalElements();
			for(WaterElement element : elements) {
				((WaterSystem.Local)country.getWaterSystem()).removeElement(element);
				MutableWaterElement mutable = (MutableWaterElement) element.getMutableElement();
				MutableWaterElement template = (MutableWaterElement) 
						element.getTemplate().createElement(
								((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeInitialized(), 
								element.getOrigin(), element.getDestination()).getMutableElement();
				((MutableSimpleLifecycleModel)template.getLifecycleModel()).setOperationsDuration(
						((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationsDuration());
				mutable.setDistributionEfficiency(template.getDistributionEfficiency());
				mutable.setElectricalIntensityOfWaterDistribution(template.getElectricalIntensityOfWaterDistribution());
				mutable.setElectricalIntensityOfWaterProduction(template.getElectricalIntensityOfWaterProduction());
				//mutable.setInitialWaterInput(template.getInitialWaterInput());
				//mutable.setInitialWaterProduction(template.getInitialWaterProduction());
				mutable.setLifecycleModel(template.getLifecycleModel());
				mutable.setMaxWaterInput(template.getMaxWaterInput());
				mutable.setMaxWaterProduction(template.getMaxWaterProduction());
				mutable.setReservoirIntensityOfWaterProduction(template.getReservoirIntensityOfWaterProduction());
				mutable.setVariableOperationsCostOfWaterDistribution(template.getVariableOperationsCostOfWaterDistribution());
				mutable.setVariableOperationsCostOfWaterProduction(template.getVariableOperationsCostOfWaterDistribution());
				((WaterSystem.Local)country.getWaterSystem()).addElement(mutable.createElement());
			}
		}
		if(country.getElectricitySystem() instanceof ElectricitySystem.Local) {
			List<? extends ElectricityElement> elements = ((ElectricitySystem.Local)
					country.getElectricitySystem()).getInternalElements();
			for(ElectricityElement element : elements) {
				((ElectricitySystem.Local)country.getElectricitySystem()).removeElement(element);

				MutableElectricityElement mutable = (MutableElectricityElement) element.getMutableElement();
				MutableElectricityElement template = (MutableElectricityElement) 
						element.getTemplate().createElement(
								((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeInitialized(), 
								element.getOrigin(), element.getDestination()).getMutableElement();
				((MutableSimpleLifecycleModel)template.getLifecycleModel()).setOperationsDuration(
						((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationsDuration());
				mutable.setDistributionEfficiency(template.getDistributionEfficiency());
				//mutable.setInitialElectricityInput(template.getInitialElectricityInput());
				//mutable.setInitialElectricityProduction(template.getInitialElectricityProduction());
				mutable.setLifecycleModel(template.getLifecycleModel());
				mutable.setMaxElectricityInput(template.getMaxElectricityInput());
				mutable.setMaxElectricityProduction(template.getMaxElectricityProduction());
				mutable.setPetroleumIntensityOfElectricityProduction(template.getPetroleumIntensityOfElectricityProduction());
				mutable.setVariableOperationsCostOfElectricityDistribution(template.getVariableOperationsCostOfElectricityDistribution());
				mutable.setVariableOperationsCostOfElectricityProduction(template.getVariableOperationsCostOfElectricityDistribution());
				mutable.setWaterIntensityOfElectricityProduction(template.getWaterIntensityOfElectricityProduction());
				((ElectricitySystem.Local)country.getElectricitySystem()).addElement(mutable.createElement());
			}
		}
		if(country.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			List<? extends PetroleumElement> elements = ((PetroleumSystem.Local)
					country.getPetroleumSystem()).getInternalElements();
			for(PetroleumElement element : elements) {
				((PetroleumSystem.Local)country.getPetroleumSystem()).removeElement(element);
				MutablePetroleumElement mutable = (MutablePetroleumElement) element.getMutableElement();
				MutablePetroleumElement template = (MutablePetroleumElement) 
						element.getTemplate().createElement(
								((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeInitialized(), 
								element.getOrigin(), element.getDestination()).getMutableElement();
				((MutableSimpleLifecycleModel)template.getLifecycleModel()).setOperationsDuration(
						((MutableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationsDuration());
				mutable.setDistributionEfficiency(template.getDistributionEfficiency());
				mutable.setElectricalIntensityOfPetroleumDistribution(template.getElectricalIntensityOfPetroleumDistribution());
				//mutable.setInitialPetroleumInput(template.getInitialPetroleumInput());
				//mutable.setInitialPetroleumProduction(template.getInitialPetroleumProduction());
				mutable.setLifecycleModel(template.getLifecycleModel());
				mutable.setMaxPetroleumInput(template.getMaxPetroleumInput());
				mutable.setMaxPetroleumProduction(template.getMaxPetroleumProduction());
				mutable.setReservoirIntensityOfPetroleumProduction(template.getReservoirIntensityOfPetroleumProduction());
				mutable.setVariableOperationsCostOfPetroleumDistribution(template.getVariableOperationsCostOfPetroleumDistribution());
				mutable.setVariableOperationsCostOfPetroleumProduction(template.getVariableOperationsCostOfPetroleumDistribution());
				((PetroleumSystem.Local)country.getPetroleumSystem()).addElement(mutable.createElement());
			}
		}
		return country;
	}
	
	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */
	private static Gson getGson() {
		if(gson == null) {
			gson = gsonBuilder.create();
		}
		return gson;
	}
	
	/**
	 * Recursive replace circular references.
	 *
	 * @param society the society
	 */
	private static void recursiveReplaceCircularReferences(Society society) {
		for(InfrastructureSystem system : society.getInfrastructureSystems()) {
			system.setSociety(society);
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			nestedSociety.setSociety(society);
			recursiveReplaceCircularReferences(nestedSociety);
		}
	}
	
	/**
	 * Instantiates a new serialization.
	 */
	private Serialization() {
		
	}
	
	/**
	 * The Class InterfaceAdapter.
	 * 
	 * Written by Maciek Makowski and published on StackOverflow.
	 * http://stackoverflow.com/questions/4795349/how-to-serialize-a-class-with-an-interface
	 * 
	 *
	 * @param <T> the generic type
	 */
	private static class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	    
	    /* (non-Javadoc)
	     * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	     */
	    public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
	        final JsonObject wrapper = new JsonObject();
	        wrapper.addProperty("type", object.getClass().getName());
	        wrapper.add("data", context.serialize(object));
	        return wrapper;
	    }

	    /* (non-Javadoc)
	     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	     */
	    public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
	        final JsonObject wrapper = (JsonObject) elem;
	        final JsonElement typeName = get(wrapper, "type");
	        final JsonElement data = get(wrapper, "data");
	        final Type actualType = typeForName(typeName); 
	        return context.deserialize(data, actualType);
	    }

	    /**
	     * Type for name.
	     *
	     * @param typeElem the type elem
	     * @return the type
	     */
	    private Type typeForName(final JsonElement typeElem) {
	        try {
	            return Class.forName(typeElem.getAsString());
	        } catch (ClassNotFoundException e) {
	            throw new JsonParseException(e);
	        }
	    }

	    /**
	     * Gets the.
	     *
	     * @param wrapper the wrapper
	     * @param memberName the member name
	     * @return the json element
	     */
	    private JsonElement get(final JsonObject wrapper, String memberName) {
	        final JsonElement elem = wrapper.get(memberName);
	        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
	        return elem;
	    }
	}
}
