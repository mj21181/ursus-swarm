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
package pso.async.implementation;

import java.util.Arrays;

import log.ApplicationLogger;

import org.apache.commons.math3.linear.RealMatrix;

import pso.MisconfiguredBlockException;
import pso.StateRepository;
import pso.async.implementation.controllers.LinearController;
import pso.async.implementation.error.AdditiveErrorCalculator;
import pso.async.implementation.noise.DistributionNoiseModulator;
import pso.async.implementation.noise.NoNoiseModulator;
import pso.async.implementation.plants.LinearPlantParameters;
import pso.async.implementation.plants.StateSpace;
import pso.async.interfaces.Controller;
import pso.async.interfaces.ErrorCalculator;
import pso.async.interfaces.FeedbackControlSystem;
import pso.async.interfaces.NoiseModulator;
import pso.async.interfaces.Plant;

/**
 * @author Mike Johnson
 * 
 */
public class PSOPositionTracker implements FeedbackControlSystem
{
	/**
	 * The measured value of the output search position at the last timestep
	 */
	protected int[]				measured		= null;
	
	/**
	 * Used to calculate error between the output search position signal and the
	 * desired search position
	 */
	protected ErrorCalculator	errCalc			= null;
	
	/**
	 * The Control law used to cause the output signal track some desired search
	 * position
	 */
	protected Controller		control			= null;
	
	/**
	 * Used to modulate noise into the next sampling position signal. This
	 * interface can be used to add some stochastic or periodic component into
	 * the next search position calculation
	 */
	protected NoiseModulator	processNoise	= null;
	
	/**
	 * The Open Loop model of the particle dynamics we are using
	 */
	protected Plant				plant			= null;
	
	/**
	 * 
	 */
	public PSOPositionTracker (RealMatrix K, LinearPlantParameters params)
	{
		// initialize the plant and controller
		try
		{
			plant = new StateSpace(params);
			
			control = new LinearController(K, params.getInputVectorSize(),
					params.getStateVectorSize());
		}
		catch (MisconfiguredBlockException e)
		{
			throw new IllegalArgumentException(e.getMessage());
		}
		
		// initialize the error calculator
		errCalc = new AdditiveErrorCalculator(true);
		
		// initialize the noise generator
		if (!Double.isNaN(params.getNoiseGain()))
		{
			processNoise = new DistributionNoiseModulator(params.getNoiseGain(),
					StateRepository.getInstance());
		}
		else
		{
			processNoise = new NoNoiseModulator();
		}
		
		// initialize the measured output vector
		measured = new int[params.getInputVectorSize()];
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.FeedbackControlSystem#intialize(int[])
	 */
	@Override
	public void intialize (int[] initialState)
			throws MisconfiguredBlockException
	{
		plant.setInitialState(initialState);
		measured = Arrays.copyOf(measured, measured.length);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.FeedbackControlSystem#isInitialized()
	 */
	@Override
	public boolean isInitialized ()
	{
		return plant.isInitialized();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.FeedbackControlSystem#getState()
	 */
	@Override
	public int[] getState ()
	{
		return plant.getState();
	}
	
	protected void updateFeedbackSignal (int[] output)
	{
		measured = Arrays.copyOf(output, output.length);
		// System.out.println("Meas: " + measured.toString());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.Plant#transfer(int[])
	 */
	@Override
	public int[] transfer (int... input) throws MisconfiguredBlockException
	{
		ApplicationLogger.getInstance()
				.logDebug("Desired Location: " + Arrays.toString(input));
		ApplicationLogger.getInstance()
				.logDebug("Measured Location: " + Arrays.toString(measured));
		
		if (input.length != measured.length) { throw new MisconfiguredBlockException(
				"Input vector must have length equal to measured vector length."); }
		
		// calculate the error
		int[] error = errCalc.transfer(input, measured);
		
		ApplicationLogger.getInstance()
				.logDebug("ERR: " + Arrays.toString(error));
		
		// perform the control correction on the signal
		error = control.transfer(error);
		
		ApplicationLogger.getInstance()
				.logDebug("CONTROL: " + Arrays.toString(error));
		
		// modulate in some noise
		error = processNoise.transfer(error);
		
		ApplicationLogger.getInstance()
				.logDebug("ACT: " + Arrays.toString(error));
		
		// calculate the output
		int[] output = plant.transfer(error);
		
		// ApplicationLogger.getInstance().logDebug("Out: " +
		// output.toString());
		
		// update our feedback vector
		updateFeedbackSignal(output);
		
		return output;
	}
	
}
