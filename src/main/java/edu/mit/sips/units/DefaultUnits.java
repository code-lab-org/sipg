/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.units;

/**
 * A default implementation of the units interface with sub-classes 
 * for numerator and denominator units.
 * 
 * @author Paul T. Grogan
 */
public abstract class DefaultUnits implements Units {
	/**
	 * Subclass that specifies units used in the denominator of fractions.
	 * 
	 * @author Paul T. Grogan
	 */
	public static abstract class DenominatorUnits extends DefaultUnits {
		/**
		 * Instantiates a new denominator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		protected DenominatorUnits(String name, String abbreviation, double scale) {
			super(name, abbreviation, scale);
		}
	}
	
	/**
	 * Subclass that specifies units used in the numerator of fractions.
	 * 
	 * @author Paul T. Grogan
	 */
	public static abstract class NumeratorUnits extends DefaultUnits {
		
		/**
		 * Instantiates a new numerator units.
		 *
		 * @param name the name
		 * @param abbreviation the abbreviation
		 * @param scale the scale
		 */
		protected NumeratorUnits(String name, String abbreviation, double scale) {
			super(name, abbreviation, scale);
		}
	}
	
	/**
	 * Convert.
	 *
	 * @param value the value
	 * @param sourceNumeratorUnits the source numerator units
	 * @param sourceDenominatorUnits the source denominator units
	 * @param targetNumeratorUnits the target numerator units
	 * @param targetDenominatorUnits the target denominator units
	 * @return the double
	 */
	public static double convert(double value, 
			DefaultUnits sourceNumeratorUnits, 
			DefaultUnits sourceDenominatorUnits, 
			DefaultUnits targetNumeratorUnits, 
			DefaultUnits targetDenominatorUnits) {
		return value / sourceNumeratorUnits.getScale() 
				* sourceDenominatorUnits.getScale() 
				* targetNumeratorUnits.getScale()
				/ targetDenominatorUnits.getScale();
	}
	
	/**
	 * Convert flow.
	 *
	 * @param value the value
	 * @param sourceUnits the source units
	 * @param sourceTimeUnits the source time units
	 * @param targetUnits the target units
	 * @param targetTimeUnits the target time units
	 * @return the double
	 */
	public static double convertFlow(double value, 
			DefaultUnits sourceUnits, 
			TimeUnits sourceTimeUnits, 
			DefaultUnits targetUnits, 
			TimeUnits targetTimeUnits) {
		return convertStock(value, sourceUnits, targetUnits) 
				/ convertStock(1, sourceTimeUnits, targetTimeUnits);
	}
	
	/**
	 * Convert stock.
	 *
	 * @param value the value
	 * @param sourceUnits the source units
	 * @param targetUnits the target units
	 * @return the double
	 */
	public static double convertStock(double value, 
			DefaultUnits sourceUnits, 
			DefaultUnits targetUnits) {
		return value / sourceUnits.getScale() 
				* targetUnits.getScale();
	}
	
	private final String name;
	private final String abbreviation;
	private final double scale;
	
	/**
	 * Instantiates a new denominator units.
	 *
	 * @param name the name
	 * @param abbreviation the abbreviation
	 * @param scale the scale
	 */
	protected DefaultUnits(String name, String abbreviation, double scale) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.scale = scale;
	}
	
	@Override
	public String getAbbreviation() { 
		return abbreviation; 
	}

	@Override
	public String getName() { 
		return name; 
	}

	@Override
	public double getScale() { 
		return scale; 
	}

	@Override
	public String toString() { 
		return abbreviation; 
	}
}
