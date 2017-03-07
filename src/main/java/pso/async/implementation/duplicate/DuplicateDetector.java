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
package pso.async.implementation.duplicate;

import log.ApplicationLogger;
import pso.DuplicateDetectionMode;
import pso.async.interfaces.DuplicateInterface;
import pso.implementation.search.UnitIntervalMapper.Mapping;
import pso.interfaces.StateInterface;
import pso.interfaces.search.SearchDomainInterface;

/**
 * The {@link DuplicateDetector} is an optional process which could be executed
 * when the search has been running too long. The only implemented mode picks
 * randomly from the list of locations which have not been searched and
 * reinitializes the robot to the unsampled location. This randomness follows a
 * uniform distribution.
 * 
 * There is an enum {@link DuplicateDetectionMode} and a switch statement where
 * other more complex methods could be added in the future.
 * 
 * For example, some nonlinear regression technique like Gaussian Process
 * Regression could be used for this. This technique gives confidence values for
 * unsampled locations in the search space. When the confidence predictions (to
 * a configured certainty) fall below (or rise above) the optimum value already
 * found, the search can stop. Whether the value is rising above or falling
 * below depends on whether the function is being minimized or maximized. This
 * process allows for the optimum to be found to a configured certainty without
 * having to search the rest of the search space.
 * 
 * @author Mike Johnson
 * 
 */
public class DuplicateDetector implements DuplicateInterface
{
	/**
	 * The mode being used to check if a given location has been searched
	 * already
	 */
	protected DuplicateDetectionMode	mode	= null;
	
	/**
	 * A reference to the state object which stores all of the information about
	 * which locations have been sampled
	 */
	protected StateInterface			state	= null;
	
	/**
	 * Creates this object according to a certain mode and a given state object
	 * with sample information
	 */
	public DuplicateDetector (DuplicateDetectionMode m, StateInterface s)
	{
		mode = m;
		state = s;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pso.async.interfaces.DuplicateInterface#isAlreadyObserved(int[])
	 */
	@Override
	public boolean isAlreadyObserved (int[] location)
	{
		if (mode == DuplicateDetectionMode.NONE)
		{
			return false;
		}
		else
		{
			ApplicationLogger.getInstance().logDebug("CHECKING IF OBSERVED");
			
			SearchDomainInterface sd = state.getSearchDomain();
			
			return sd.isObserved(location);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pso.async.interfaces.DuplicateInterface#selectUnobservedLocation
	 * (int[])
	 */
	@Override
	public int[] selectUnobservedLocation (int[] location)
			throws FullySampledException
	{
		switch (mode)
		{
			case NONE:
				break;
			case UNIFORM_UNOBSERVED:
				
				double selectionValue =
						state.generateRandomDuplicateRemovalValue();
				
				Mapping m = state.selectAlternateLocation(selectionValue);
				// System.out.println("Remaining: " + uim.getSize());
				
				// check if all of the locations in the SearchDomain have been
				// observed
				if (m == null) { throw new FullySampledException(
						"Search Domain Fully Sampled"); }
				
				return m.indicies;
			default:
				break;
		}
		
		// default - do nothing
		return location;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pso.async.interfaces.DuplicateInterface#addUnobservedLocation(int
	 * [])
	 */
	@Override
	public void addUnobservedLocation (int[] location)
	{
		state.addAlternateLocation(location);
	}
	
}
