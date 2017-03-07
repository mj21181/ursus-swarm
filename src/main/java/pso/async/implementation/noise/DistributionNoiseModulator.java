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

import java.util.Arrays;

import log.ApplicationLogger;

import pso.MisconfiguredBlockException;
import pso.async.interfaces.NoiseModulator;
import pso.interfaces.StateInterface;

/**
 * @author Mike Johnson
 * 
 */
public class DistributionNoiseModulator implements NoiseModulator
{
	protected StateInterface	state		= null;
	
	protected double			gain		= Double.NaN;
	
	protected int				axisSize	= -1;
	
	/**
	 * 
	 */
	public DistributionNoiseModulator (double gain, StateInterface s)
	{
		state = s;
		this.gain = gain;
		
		axisSize = state.getConfiguration().getFitnessState().getAxisSize();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.NoiseModulator#transfer(int[])
	 */
	@Override
	public int[] transfer (int... input) throws MisconfiguredBlockException
	{
		// if (input.length != state.) { throw new MisconfiguredBlockException(
		// "Input vector must have same size as RNG array"); }
		//
		int[] noise = state.generateRandomVectorPrimary();
		
		for (int i = 0; i < noise.length; i++ )
		{
			noise[i] = noise[i] - (int) Math.floor(axisSize / 2.0);
		}
		
		for (int i = 0; i < noise.length; i++ )
		{
			noise[i] = (int) Math.floor((gain * noise[i]));
		}
		
		ApplicationLogger.getInstance().logDebug(
				"Noise: " + Arrays.toString(noise));
		ApplicationLogger.getInstance().logDebug(
				"Input: " + Arrays.toString(input));
		
		int count = 0;
		for (int i = (int) Math.floor(input.length / 2.0); i < input.length; i++ )
		{
			input[i] = input[i] + noise[count];
			count++ ;
		}
		
		return input;
	}
}
