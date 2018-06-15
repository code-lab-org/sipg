package edu.mit.sips.sim.util2;

/**
 * The Class ResourceType.
 */
public abstract class ResourceType {
	public static final Currency CURRENCY = new Currency();
	public static final Electricity ELECTRICITY = new Electricity();
	public static final Food FOOD = new Food();
	public static final Land LAND = new Land();
	public static final Oil OIL = new Oil();
	public static final People PEOPLE = new People();
	public static final Water WATER = new Water();
	
	/**
	 * The Class Currency.
	 */
	public static class Currency extends ResourceType {
		public static final ResourceStock<Currency> ZERO_STOCK = 
				new ResourceStock<Currency>(CurrencyUnits.sim, 0);
		public static final ResourceFlow<Currency> ZERO_FLOW = 
				new ResourceFlow<Currency>(CurrencyUnits.sim, TimeUnits.year, 0);
		public static final ResourceStock<Currency> UNIT_STOCK = 
				new ResourceStock<Currency>(CurrencyUnits.sim, 1);
		public static final ResourceFlow<Currency> UNIT_FLOW = 
				new ResourceFlow<Currency>(CurrencyUnits.sim, TimeUnits.year, 1);
		
		private Currency() { };
	}
	
	/**
	 * The Class Electricity.
	 */
	public static class Electricity extends ResourceType { 
		public static final ResourceStock<Electricity> ZERO_STOCK = 
				new ResourceStock<Electricity>(ElectricityUnits.Wh, 0);
		public static final ResourceFlow<Electricity> ZERO_FLOW = 
				new ResourceFlow<Electricity>(ElectricityUnits.Wh, TimeUnits.year, 0);
		public static final ResourceStock<Electricity> UNIT_STOCK = 
				new ResourceStock<Electricity>(ElectricityUnits.Wh, 1);
		public static final ResourceFlow<Electricity> UNIT_FLOW = 
				new ResourceFlow<Electricity>(ElectricityUnits.Wh, TimeUnits.year, 1);
		
		private Electricity() { };
	}
	
	/**
	 * The Class Food.
	 */
	public static class Food extends ResourceType { 
		public static final ResourceStock<Food> ZERO_STOCK = 
				new ResourceStock<Food>(FoodUnits.kcal, 0);
		public static final ResourceFlow<Food> ZERO_FLOW = 
				new ResourceFlow<Food>(FoodUnits.kcal, TimeUnits.year, 0);
		public static final ResourceStock<Food> UNIT_STOCK = 
				new ResourceStock<Food>(FoodUnits.kcal, 1);
		public static final ResourceFlow<Food> UNIT_FLOW = 
				new ResourceFlow<Food>(FoodUnits.kcal, TimeUnits.year, 1);
		
		private Food() { };
	}
	
	/**
	 * The Class Land.
	 */
	public static class Land extends ResourceType { 
		public static final ResourceStock<Land> ZERO_STOCK = 
				new ResourceStock<Land>(LandUnits.km2, 0);
		public static final ResourceFlow<Land> ZERO_FLOW = 
				new ResourceFlow<Land>(LandUnits.km2, TimeUnits.year, 0);
		public static final ResourceStock<Land> UNIT_STOCK = 
				new ResourceStock<Land>(LandUnits.km2, 1);
		public static final ResourceFlow<Land> UNIT_FLOW = 
				new ResourceFlow<Land>(LandUnits.km2, TimeUnits.year, 1);
		
		private Land() { };
	}
	
	/**
	 * The Class Oil.
	 */
	public static class Oil extends ResourceType { 
		public static final ResourceStock<Oil> ZERO_STOCK = 
				new ResourceStock<Oil>(OilUnits.toe, 0);
		public static final ResourceFlow<Oil> ZERO_FLOW = 
				new ResourceFlow<Oil>(OilUnits.toe, TimeUnits.year, 0);
		public static final ResourceStock<Oil> UNIT_STOCK = 
				new ResourceStock<Oil>(OilUnits.toe, 1);
		public static final ResourceFlow<Oil> UNIT_FLOW = 
				new ResourceFlow<Oil>(OilUnits.toe, TimeUnits.year, 1);
		
		private Oil() { };
	}
	
	/**
	 * The Class People.
	 */
	public static class People extends ResourceType { 
		public static final ResourceStock<People> ZERO_STOCK = 
				new ResourceStock<People>(PeopleUnits.p, 0);
		public static final ResourceFlow<People> ZERO_FLOW = 
				new ResourceFlow<People>(PeopleUnits.p, TimeUnits.year, 0);
		public static final ResourceStock<People> UNIT_STOCK = 
				new ResourceStock<People>(PeopleUnits.p, 1);
		public static final ResourceFlow<People> UNIT_FLOW = 
				new ResourceFlow<People>(PeopleUnits.p, TimeUnits.year, 1);
		
		private People() { };
	}
	
	/**
	 * The Class Time.
	 */
	public static class Time { 
		public static final TimePoint ZERO_TIME = 
				new TimePoint(TimeUnits.year, 0);
		public static final TimeDuration ZERO_DURATION = 
				new TimeDuration(TimeUnits.year, 0);
		public static final TimeDuration UNIT_DURATION = 
				new TimeDuration(TimeUnits.year, 1);
		
		private Time() { };
	}
	
	/**
	 * The Class Water.
	 */
	public static class Water extends ResourceType { 
		public static final ResourceStock<Water> ZERO_STOCK = 
				new ResourceStock<Water>(WaterUnits.m3, 0);
		public static final ResourceFlow<Water> ZERO_FLOW = 
				new ResourceFlow<Water>(WaterUnits.m3, TimeUnits.year, 0);
		public static final ResourceStock<Water> UNIT_STOCK = 
				new ResourceStock<Water>(WaterUnits.m3, 1);
		public static final ResourceFlow<Water> UNIT_FLOW = 
				new ResourceFlow<Water>(WaterUnits.m3, TimeUnits.year, 1);
		
		private Water() { };
	}
}
