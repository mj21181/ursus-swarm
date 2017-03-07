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
package pso.async.interfaces;

import pso.MisconfiguredBlockException;

/**
 * 
 * @author Mike Johnson
 * 
 */
public interface FeedbackControlSystem
{
	/**
	 * Initializes the {@link FeedbackControlSystem} dynamics with the given initial state vector.
	 * 
	 * @param initialState
	 * @throws MisconfiguredBlockException
	 */
	public void intialize (int[] initialState)
			throws MisconfiguredBlockException;
	
	/**
	 * Returns true if the {@link FeedbackControlSystem} has been initialized
	 * 
	 * @return
	 */
	public boolean isInitialized ();
	
	/**
	 * Gets the current state vector of the system.
	 * 
	 * @return
	 */
	public int[] getState ();
	
	/**
	 * Tracks the desired state vector using {@link FeedbackControlSystem} and returns an
	 * output vector
	 * 
	 * @param desired
	 * @return
	 * @throws MisconfiguredBlockException
	 */
	public int[] transfer (int... desired) throws MisconfiguredBlockException;
}
