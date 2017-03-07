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
package pso.async.implementation.controllers;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import pso.MisconfiguredBlockException;
import pso.async.implementation.plants.StateSpace;
import pso.async.interfaces.Controller;

/**
 * This class implements a Linear Control system.
 * 
 * @author Mike Johnson
 * 
 */
public class LinearController implements Controller
{
	/**
	 * The feedback gain
	 */
	protected RealMatrix	K			= null;
	
	protected RealMatrix	inputMatrix	= null;
	
	/**
	 * Constructs a {@link LinearController} for the given Feedback gain and
	 * uses the given {@link StateSpace} to validate the gain is correctly
	 * configured.
	 * 
	 * @throws MisconfiguredBlockException if misconfigured
	 */
	public LinearController (RealMatrix gain, int numInputs, int numStates)
			throws MisconfiguredBlockException
	{
		if (gain.getRowDimension() != numInputs) { throw new MisconfiguredBlockException(
				"Feedback Gain must have same number of Rows as B Matrix has columns."); }
		
		if (gain.getColumnDimension() != numStates) { throw new MisconfiguredBlockException(
				"Feedback Gain must have same number of Columns as B Matrix has rows."); }
		
		K = gain;
		
		inputMatrix = new Array2DRowRealMatrix(K.getColumnDimension(), 1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.Controller#transfer(int[])
	 */
	@Override
	public int[] transfer (int... error) throws MisconfiguredBlockException
	{
		if (K.getColumnDimension() != error.length) { throw new MisconfiguredBlockException(
				"Input vector must be K Column dimension in size."); }
		
		for (int i = 0; i < error.length; i++ )
		{
			inputMatrix.setEntry(i, 0, error[i]);
		}
		
		inputMatrix = K.multiply(inputMatrix);
		
		for (int i = 0; i < error.length; i++ )
		{
			error[i] = (int) Math.floor(inputMatrix.getEntry(i, 0));
		}
		
		return error;
	}
}
