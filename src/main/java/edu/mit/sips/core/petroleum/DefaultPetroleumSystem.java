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
package edu.mit.sips.core.petroleum;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The default implementation of the petroleum system interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultPetroleumSystem extends DefaultInfrastructureSystem implements PetroleumSystem {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	
	@Override
	public double getElectricityConsumption() {
		return 0;
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
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}
	
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}
	
	@Override
	public double getPetroleumDomesticPrice() {
		return 0;
	}
	
	@Override
	public double getPetroleumExportPrice() {
		return 0;
	}
	
	@Override
	public double getPetroleumImportPrice() {
		return 0;
	}

	@Override
	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirVolume() {
		return 0;
	}

	@Override
	public double getReservoirWithdrawals() {
		return 0;
	}
}
