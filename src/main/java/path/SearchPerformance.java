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
package path;

/**
 * This data structure is used to store all of the performance metrics
 * associated with a given graph search. This class is used both in the single
 * agent case and the multiple agent case. In the multiple agent case, all stats
 * are the cumulative set of the single agent searches performed.
 * 
 * @author Mike Johnson
 *
 */
public class SearchPerformance
{
	/**
	 * The time in milliseconds it took for the search to run
	 */
	protected long		timeToRunInMilliseconds	= -1;
	
	/**
	 * The number of nodes expanded (added to open list) during the search.
	 */
	protected long		numberOfNodesExpanded	= -1;
	
	/**
	 * The length of the solution (path) found by the search
	 */
	protected long		solutionLength			= -1;
	
	/**
	 * Whether these performance stats are for a high level search
	 */
	protected boolean	highLevel				= false;
	
	/**
	 * Constructs a {@link SearchPerformance} data structure with its fields
	 * initialized to -1;
	 */
	public SearchPerformance ()
	{
	}
	
	/**
	 * Gets the amount of time it took the search to run in milliseconds
	 * 
	 * @return the timeToRunInMilliseconds
	 */
	public long getTimeToRunInMilliseconds ()
	{
		return timeToRunInMilliseconds;
	}
	
	/**
	 * Sets the amount of time it took the search to run in milliseconds
	 * 
	 * @param timeToRunInMilliseconds the timeToRunInMilliseconds to set
	 */
	public void setTimeToRunInMilliseconds (long timeToRunInMilliseconds)
	{
		this.timeToRunInMilliseconds = timeToRunInMilliseconds;
	}
	
	/**
	 * Gets the number of nodes expanded during the search
	 * 
	 * @return the numberOfNodesExpanded
	 */
	public long getNumberOfNodesExpanded ()
	{
		return numberOfNodesExpanded;
	}
	
	/**
	 * Sets the number of nodes expanded during the search
	 * 
	 * @param numberOfNodesExpanded the numberOfNodesExpanded to set
	 */
	public void setNumberOfNodesExpanded (long numberOfNodesExpanded)
	{
		this.numberOfNodesExpanded = numberOfNodesExpanded;
	}
	
	/**
	 * Gets the length of the solution
	 * 
	 * @return the solutionLength
	 */
	public long getSolutionLength ()
	{
		return solutionLength;
	}
	
	/**
	 * Sets the length of the solution
	 * 
	 * @param solutionLength the solutionLength to set
	 */
	public void setSolutionLength (long solutionLength)
	{
		this.solutionLength = solutionLength;
	}
	
	/**
	 * Returns true if these stats are for a high level search
	 * 
	 * @return the highLevel
	 */
	public boolean isHighLevel ()
	{
		return highLevel;
	}
	
	/**
	 * Sets the flag of whether these stats are for a high level search
	 * 
	 * @param highLevel the highLevel to set
	 */
	public void setHighLevel (boolean highLevel)
	{
		this.highLevel = highLevel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (highLevel ? 1231 : 1237);
		result = prime * result + (int) (numberOfNodesExpanded
				^ (numberOfNodesExpanded >>> 32));
		result = prime * result
				+ (int) (solutionLength ^ (solutionLength >>> 32));
		result = prime * result + (int) (timeToRunInMilliseconds
				^ (timeToRunInMilliseconds >>> 32));
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SearchPerformance other = (SearchPerformance) obj;
		if (highLevel != other.highLevel) return false;
		if (numberOfNodesExpanded != other.numberOfNodesExpanded) return false;
		if (solutionLength != other.solutionLength) return false;
		if (timeToRunInMilliseconds != other.timeToRunInMilliseconds)
			return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format(
				"SearchPerformance [timeToRunInMilliseconds=%s, "
						+ "numberOfNodesExpanded=%s, solutionLength=%s, highLevel=%s]",
				timeToRunInMilliseconds, numberOfNodesExpanded, solutionLength,
				highLevel);
	}
	
}
