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
package pso.implementation.optimization;

import java.util.List;

import pso.Sample;
import pso.interfaces.StateInterface;
import pso.interfaces.optimization.OptimizationInterface;

/**
 * @author Mike Johnson
 * 
 */
public class SampleOptimizer implements OptimizationInterface
{
	protected static final double	TOLERANCE	= 1e-4;
	
	protected Sample				optimum		= null;
	
	protected boolean				maximizing	= false;
	
	protected StateInterface		state		= null;
	
	protected int					id			= -1;
	
	/**
	 * 
	 */
	public SampleOptimizer (int id, boolean maximize, StateInterface s)
	{
		maximizing = maximize;
		state = s;
		this.id = id;
		
		if (maximizing)
		{
			optimum = new Sample(Double.MIN_VALUE, null);
		}
		else
		{
			optimum = new Sample(Double.MAX_VALUE, null);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.OptimizationInterface#getOptimumValue()
	 */
	@Override
	public double getOptimumValue ()
	{
		
		return optimum.getSampleValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.OptimizationInterface#getOptimumLocation()
	 */
	@Override
	public int[] getOptimumLocation ()
	{
		
		return optimum.getSampleLocation();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.OptimizationInterface#getOptimumSample()
	 */
	@Override
	public Sample getOptimumSample ()
	{
		
		return optimum;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.OptimizationInterface#isMaximization()
	 */
	@Override
	public boolean isMaximization ()
	{
		
		return maximizing;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pso.OptimizationInterface#updateOptimum(java.util
	 * .List)
	 */
	@Override
	public boolean updateOptimum (List<Sample> samples)
	{
		boolean updated = false;
		
		for (Sample s : samples)
		{
			// first sample
			if (optimum.getSampleLocation() == null)
			{
				optimum = s;
				
				updated = true;
			}
			
			// for flat landscapes
			if (Math.abs(s.getSampleValue() - optimum.getSampleValue()) < TOLERANCE)
			{
				int tieBreaker = 0;
				
				// check if this is the SampleOptimizer for the global optimum,
				// all of the optimizers have different sources of randomness
				if (id == -1)
				{
					tieBreaker = state.generateGlobalTieBreaker();
				}
				else
				{
					// it's a local optimizer
					
					tieBreaker = state.generateTieBreaker(id);
				}
				
				if (tieBreaker > 0)
				{
					optimum = s;
					updated = true;
				}
				
				continue;
			}
			
			// for other landscapes
			if (maximizing)
			{
				if (s.getSampleValue() > optimum.getSampleValue())
				{
					optimum = s;
					updated = true;
				}
			}
			else
			{
				// minimizing
				if (s.getSampleValue() < optimum.getSampleValue())
				{
					optimum = s;
					updated = true;
				}
			}
		}
		
		return updated;
	}
	
}
