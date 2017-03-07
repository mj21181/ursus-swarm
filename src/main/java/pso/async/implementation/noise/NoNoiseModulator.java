/**
 * Copyright (C) 2016 Michael Johnson
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package pso.async.implementation.noise;

import pso.MisconfiguredBlockException;
import pso.async.interfaces.NoiseModulator;

/**
 * This class does not modulate any noise into the given input signal
 * 
 * @author Mike Johnson
 * 
 */
public class NoNoiseModulator implements NoiseModulator
{
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.NoiseModulator#transfer(int[])
	 */
	@Override
	public int[] transfer (int... input) throws MisconfiguredBlockException
	{
		return input;
	}
	
}
