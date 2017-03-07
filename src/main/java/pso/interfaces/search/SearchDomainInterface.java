/*******************************************************************************
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
 *******************************************************************************/
package pso.interfaces.search;

import pso.implementation.search.SearchDomain;
import pso.implementation.search.SearchDomainParams;

/**
 * 
 * @author Mike Johnson
 *
 */
public interface SearchDomainInterface
{
	/**
	 * Gets the {@link SearchDomainParams} that determine how this
	 * {@link SearchDomain} is configured.
	 * 
	 * @return
	 */
	public SearchDomainParams getParams ();
	
	/**
	 * Gets the fitness value stored in the N-Dimensional array at the index
	 * provided
	 * 
	 * @param domainIndicies the indicies along each axis of the search domain
	 * @return the fitness value
	 */
	public double getValue (int... domainIndicies);
	
	/**
	 * Sets the value in the N-Dimensional array to the fitness value provided
	 * 
	 * @param value the fitness value
	 * @param domainIndicies the indicies along each axis of the search domain
	 */
	public void observe (double value, int... domainIndicies);
	
	/**
	 * Determines whether the fitness function has been observed at the given
	 * index provided
	 * 
	 * @param domainIndicies the indicies along each axis of the search domain
	 * @return true if the index has been observed, false otherwise
	 */
	public boolean isObserved (int... domainIndicies);
}
