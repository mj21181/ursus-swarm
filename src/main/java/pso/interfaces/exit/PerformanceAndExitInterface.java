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
package pso.interfaces.exit;

import java.util.List;

import pso.Sample;

/**
 * @author Mike Johnson
 * 
 */
public interface PerformanceAndExitInterface
{
	/**
	 * Updates any metrics being used to track performance based on a new batch
	 * of samples.
	 * 
	 * @param samples {@link List} of new samples
	 */
	public void updatePerformance (List<Sample> samples);
	
	/**
	 * Returns true if we have reached a point where we should exit the search
	 * algorithm
	 * 
	 * @return
	 */
	public boolean shouldExit ();
}
