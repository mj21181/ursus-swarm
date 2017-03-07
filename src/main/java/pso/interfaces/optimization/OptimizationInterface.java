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
package pso.interfaces.optimization;

import java.util.List;

import pso.Sample;

/**
 * 
 * @author Mike Johnson
 *
 */
public interface OptimizationInterface
{
	/**
	 * Gets the optimum value of the fitness function being searched
	 * 
	 * @return
	 */
	public double getOptimumValue ();
	
	/**
	 * Gets the indicies of the location the optimum value was discovered. The
	 * number of indicies in the array are equal to the number of dimensions in
	 * the fitness function.
	 * 
	 * @return
	 */
	public int[] getOptimumLocation ();
	
	/**
	 * Gets the optimum location/value in {@link Sample} object form.
	 * 
	 * @return
	 */
	public Sample getOptimumSample ();
	
	/**
	 * Returns true if the optimization is searching for the maximum value,
	 * false if it is the minimum
	 * 
	 * @return
	 */
	public boolean isMaximization ();
	
	/**
	 * Takes a {@link List} of {@link Sample}s and compares them to the optimum.
	 * If a given {@link Sample} has a better value, it becomes the optimum
	 * {@link Sample}.
	 * 
	 * @param samples
	 * @return true if the optimum was updated
	 */
	public boolean updateOptimum (List<Sample> samples);
}
