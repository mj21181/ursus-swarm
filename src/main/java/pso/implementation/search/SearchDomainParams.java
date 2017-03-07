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
package pso.implementation.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mike Johnson
 * 
 */
public class SearchDomainParams
{
	protected List<Double>	axisMinimums	= new ArrayList<Double>();
	
	protected List<Double>	axisMaximums	= new ArrayList<Double>();
	
	protected List<Integer>	axisSize		= new ArrayList<Integer>();
	
	public SearchDomainParams ()
	{
		
	}
	
	public int getSearchDomainDimension ()
	{
		return axisSize.size();
	}
	
	public void addAxis (double min, double max, int size)
	{
		if (min > max) { throw new IllegalArgumentException(
				"Axis minimum value must be less than axis maximum value"); }
		
		if (size <= 0) { throw new IllegalArgumentException(
				"Axis size must be a strictly positive number (non zero, non negative)"); }
		
		axisMinimums.add(min);
		axisMaximums.add(max);
		axisSize.add(size);
	}
	
	public Double getAxisMax (int index)
	{
		if (index < 0 || index > axisSize.size()) { return Double.NaN; }
		
		return axisMaximums.get(index);
	}
	
	public Double getAxisMin (int index)
	{
		if (index < 0 || index > axisSize.size()) { return Double.NaN; }
		
		return axisMinimums.get(index);
	}
	
	public Integer getAxisSize (int index)
	{
		if (index < 0 || index > axisSize.size()) { return -1; }
		
		return axisSize.get(index);
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
		result =
				prime
						* result
						+ ( (axisMaximums == null) ? 0 : axisMaximums
								.hashCode());
		result =
				prime
						* result
						+ ( (axisMinimums == null) ? 0 : axisMinimums
								.hashCode());
		result =
				prime * result
						+ ( (axisSize == null) ? 0 : axisSize.hashCode());
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
		SearchDomainParams other = (SearchDomainParams) obj;
		if (axisMaximums == null)
		{
			if (other.axisMaximums != null) return false;
		}
		else if (!axisMaximums.equals(other.axisMaximums)) return false;
		if (axisMinimums == null)
		{
			if (other.axisMinimums != null) return false;
		}
		else if (!axisMinimums.equals(other.axisMinimums)) return false;
		if (axisSize == null)
		{
			if (other.axisSize != null) return false;
		}
		else if (!axisSize.equals(other.axisSize)) return false;
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
		return String
				.format("SearchDomainParams [axisMinimums=%s, axisMaximums=%s, axisSize=%s]",
						axisMinimums, axisMaximums, axisSize);
	}
}
