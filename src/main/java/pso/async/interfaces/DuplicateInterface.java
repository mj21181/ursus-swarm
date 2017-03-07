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

import pso.async.implementation.duplicate.FullySampledException;
import pso.interfaces.search.SearchDomainInterface;

/**
 * This interface is used to specify an optional process to stop the search from
 * searching locations which have already been identified.
 * 
 * @author Mike Johnson
 * 
 */
public interface DuplicateInterface
{
	/**
	 * Returns true if the specified location has already been observed
	 * 
	 * @param location
	 * @return
	 */
	public boolean isAlreadyObserved (int[] location);
	
	/**
	 * Selects a location within the {@link SearchDomainInterface} which has not
	 * yet been observed and returns it.
	 * 
	 * @param location The location that has been observed that we are replacing
	 * @return An unobserved location if the one passed in has already been
	 *         searched before
	 */
	public int[] selectUnobservedLocation (int[] location)
			throws FullySampledException;
	
	/**
	 * Adds a location within the {@link SearchDomainInterface} which has not
	 * been observed
	 * 
	 * @param location
	 */
	public void addUnobservedLocation (int[] location);
}
