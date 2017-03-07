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
package pso.async.implementation.error;

import pso.MisconfiguredBlockException;
import pso.async.interfaces.ErrorCalculator;

/**
 * This class is used to calculate the error between some desired vector and a
 * measured vector.
 * 
 * This class does this using vector addition.
 * 
 * This is used by a {@link FeedbackControlSystem} to calculate the error
 * signal. Feedback can either be set to Positive or Negative using the
 * constructor.
 * 
 * @author Mike Johnson
 * 
 */
public class AdditiveErrorCalculator implements ErrorCalculator
{
	/**
	 * Whether to use negative feedback
	 */
	protected boolean	isNegativeFeedback	= false;
	
	/**
	 * Constructs the {@link AdditiveErrorCalculator}
	 * 
	 * @param negativeFeedback
	 */
	public AdditiveErrorCalculator (boolean negativeFeedback)
	{
		isNegativeFeedback = negativeFeedback;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.ErrorCalculator#transfer(int[], int[])
	 */
	@Override
	public int[] transfer (int[] desired, int[] measured)
			throws MisconfiguredBlockException
	{
		if (desired.length != measured.length) { throw new MisconfiguredBlockException(
				"Desired vector must have same length as Measured length."); }
		
		int[] output = new int[desired.length];
		
		// calculate error for the current position, not the last one
		for (int i = (int) Math.floor(output.length / 2.0); i < output.length; i++ )
		{
			if (isNegativeFeedback)
			{
				output[i] = desired[i] - measured[i];
			}
			else
			{
				output[i] = desired[i] + measured[i];
			}
		}
		
		return output;
	}
}
