package edu.mit.sips.io;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.DomesticProductionModel;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSoS;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.core.electricity.DefaultElectricitySystem;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySoS;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.electricity.MutableElectricityElement;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.core.lifecycle.MutableSimpleLifecycleModel;
import edu.mit.sips.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sips.core.petroleum.MutablePetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSoS;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.social.demand.DemandModel;
import edu.mit.sips.core.social.population.PopulationModel;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSoS;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.scenario.ElementTemplate;
import edu.mit.sips.scenario.Scenario;
import edu.mit.sips.sim.hla.HlaAgricultureSystem;
import edu.mit.sips.sim.hla.HlaElectricitySystem;
import edu.mit.sips.sim.hla.HlaPetroleumSystem;
import edu.mit.sips.sim.hla.HlaSocialSystem;
import edu.mit.sips.sim.hla.HlaWaterSystem;

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
		gsonBuilder.registerTypeAdapter(AgricultureSoS.Local.class, 
				new InterfaceAdapter<AgricultureSoS.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSoS.class, 
				new InterfaceAdapter<AgricultureSoS>());
		gsonBuilder.registerTypeAdapter(WaterSystem.class, 
				new InterfaceAdapter<WaterSystem>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Local.class, 
				new InterfaceAdapter<WaterSystem.Local>());
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
		gsonBuilder.registerTypeAdapter(Scenario.class, 
				new InterfaceAdapter<Scenario>());
		gsonBuilder.registerTypeAdapter(ElementTemplate.class, 
				new InterfaceAdapter<ElementTemplate>());
	}

	private static Gson gson = gsonBuilder.create();

	/**
	 * Serialize.
	 *
	 * @param scenario the scenario
	 * @return the string
	 */
	public static String serialize(Scenario scenario) {
		return getGson().toJson(new ScenarioWrapper(scenario));
	}

	/**
	 * Deserialize.
	 *
	 * @param json the json
	 * @return the country
	 */
	public static Scenario deserialize(String json) {
		Scenario scenario = getGson().fromJson(json, ScenarioWrapper.class).scenario;
		Country country = scenario.getCountry();
		recursiveReplaceCircularReferences(country);
		// replace HLA datatypes; they will need to be re-created
		// to get the proper data members
		for(City city : country.getCities()) {
			if(city.getAgricultureSystem() instanceof HlaAgricultureSystem) {
				city.setAgricultureSystem(new DefaultAgricultureSystem());
			}
			if(city.getWaterSystem() instanceof HlaWaterSystem) {
				city.setWaterSystem(new DefaultWaterSystem());
			}
			if(city.getPetroleumSystem() instanceof HlaPetroleumSystem) {
				city.setPetroleumSystem(new DefaultPetroleumSystem());
			}
			if(city.getElectricitySystem() instanceof HlaElectricitySystem) {
				city.setElectricitySystem(new DefaultElectricitySystem());
			}
			if(city.getSocialSystem() instanceof HlaSocialSystem) {
				city.setSocialSystem(new DefaultSocialSystem());
			}
		}
		for(City city : country.getCities()) {
			if(city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				List<AgricultureElement> elements = new ArrayList<AgricultureElement>(
						((AgricultureSystem.Local)city.getAgricultureSystem()).getInternalElements());
				for(AgricultureElement element : elements) {
					((AgricultureSystem.Local)city.getAgricultureSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					MutableAgricultureElement mutable = (MutableAgricultureElement) element.getMutableElement();
					MutableAgricultureElement template = (MutableAgricultureElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
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
					mutable.setCostIntensityOfLandUsed(template.getCostIntensityOfLandUsed());
					mutable.setLaborIntensityOfLandUsed(template.getLaborIntensityOfLandUsed());
					mutable.setFoodIntensityOfLandUsed(template.getFoodIntensityOfLandUsed());
					mutable.setWaterIntensityOfLandUsed(template.getWaterIntensityOfLandUsed());
					mutable.setVariableOperationsCostOfFoodDistribution(template.getVariableOperationsCostOfFoodDistribution());
					((AgricultureSystem.Local)city.getAgricultureSystem()).addElement(mutable.createElement());
				}

			}
			if(city.getWaterSystem() instanceof WaterSystem.Local){
				List<WaterElement> elements = new ArrayList<WaterElement>(
						((WaterSystem.Local)city.getWaterSystem()).getInternalElements());
				for(WaterElement element : elements) {
					((WaterSystem.Local)city.getWaterSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					MutableWaterElement mutable = (MutableWaterElement) element.getMutableElement();
					MutableWaterElement template = (MutableWaterElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
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
					mutable.setVariableOperationsCostOfWaterProduction(template.getVariableOperationsCostOfWaterProduction());
					((WaterSystem.Local)city.getWaterSystem()).addElement(mutable.createElement());
				}
			}
			if(city.getElectricitySystem() instanceof ElectricitySystem.Local) {
				List<ElectricityElement> elements = new ArrayList<ElectricityElement>(
						((ElectricitySystem.Local) city.getElectricitySystem()).getInternalElements());
				for(ElectricityElement element : elements) {
					((ElectricitySystem.Local)city.getElectricitySystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					MutableElectricityElement mutable = (MutableElectricityElement) element.getMutableElement();
					MutableElectricityElement template = (MutableElectricityElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
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
					((ElectricitySystem.Local)city.getElectricitySystem()).addElement(mutable.createElement());
				}
			}
			if(city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				List<PetroleumElement> elements = new ArrayList<PetroleumElement>(
						((PetroleumSystem.Local) city.getPetroleumSystem()).getInternalElements());
				for(PetroleumElement element : elements) {
					((PetroleumSystem.Local)city.getPetroleumSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					MutablePetroleumElement mutable = (MutablePetroleumElement) element.getMutableElement();
					MutablePetroleumElement template = (MutablePetroleumElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
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
					mutable.setVariableOperationsCostOfPetroleumProduction(template.getVariableOperationsCostOfPetroleumProduction());
					((PetroleumSystem.Local)city.getPetroleumSystem()).addElement(mutable.createElement());
				}
			}
		}
		return scenario;
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
