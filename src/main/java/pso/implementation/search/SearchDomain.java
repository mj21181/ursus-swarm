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

import java.util.Arrays;

import log.ApplicationLogger;

import pso.interfaces.search.SearchDomainInterface;

/**
 * This class exists to manage the discretization of the search domain.
 * 
 * First, for each axis of the search domain (e.g. X axis and Y axis for a 2D
 * domain), a maximum, a minimum, and the number of discrete points along the
 * axis to be included in the discretization are selected by creating a
 * {@link SearchDomainParams} object and passing it into the
 * {@link SearchDomain} constructor.
 * 
 * An array of doubles is used to represent the value of the fitness function at
 * each discrete point in the discretization. For a 2D domain, this would be
 * each point in a grid.
 * 
 * This array of doubles can be thought of as an N-dimensional array, depending
 * on the dimension of the search domain. In reality, this is a 1D array that is
 * indexes as follows:<br>
 * <br>
 * 1D: index = i<br>
 * 2D: index = j * rowsize_i + i<br>
 * 3D: index = k * rowsize_i * rowsize_j + j * rowsize_i + i<br>
 * 
 * etc. Where i, j, and k are indexes along each axis respectively.
 * 
 * The {@link SearchDomain} is used to store the value of the fitness function
 * observed at each discrete point. To store a fitness value observed at some
 * coordinates in the search domain, use a
 * {@link SearchDomainCoordinateConverter} to convert the coordinates into the
 * axis indicies, which are then converted into the final array index by the
 * {@link SearchDomain} object.
 * 
 * All methods of the {@link SearchDomain} class use the axis indicies as input.
 * 
 * For example:<br>
 * 
 * SearchDomain sd = new SearchDomain(params);
 * 
 * 1-D Search Space: sd.isObserved(5);<br>
 * 2-D Search Space: sd.isObserved(5, 3); <br>
 * 3-D Search Space: sd.isObserved(5, 3, 7); <br>
 * 
 * etc. Each new integer added as an argument indexes into the next dimension.
 * In the 3-D example, the X index is 5, the Y index is 3, and the Z index is 7.
 * 
 * @author Mike Johnson
 * 
 */
public class SearchDomain implements SearchDomainInterface
{
	/**
	 * The N-dimensional array for each point in the Search Domain
	 */
	protected double[]				domain	= null;
	
	/**
	 * The parameters for this search domain
	 */
	protected SearchDomainParams	params	= null;
	
	/**
	 * Constructs a new {@link SearchDomain} object, allocating the
	 * N-Dimensional array according to the {@link SearchDomainParams} object
	 * provided.
	 * 
	 * Since the fitness value at each discrete point is unknown, the
	 * N-Dimensional array is initialized with each value set as Double.NaN.
	 * 
	 * @param params
	 */
	public SearchDomain (SearchDomainParams params)
	{
		this.params = params;
		
		int dim = params.getSearchDomainDimension();
		
		// calculate how many values will be in the search domain
		int product = 1;
		
		for (int i = 0; i < dim; i++ )
		{
			product *= params.getAxisSize(i);
		}
		
		// create the array and initialize it to an unknown fitness value
		domain = new double[product];
		
		for (int i = 0; i < product; i++ )
		{
			domain[i] = Double.NaN;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.SearchDomainInterface#observe(double,
	 * int[])
	 */
	@Override
	public void observe (double value, int... domainIndicies)
	{
		int val = calculateArrayIndex(domainIndicies);
		
//		 System.out.println(val);
//		 System.out.println("len: " + domain.length);
		domain[val] = value;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.SearchDomainInterface#getValue(int[])
	 */
	@Override
	public double getValue (int... domainIndicies)
	{
		return domain[calculateArrayIndex(domainIndicies)];
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.SearchDomainInterface#getParams()
	 */
	@Override
	public SearchDomainParams getParams ()
	{
		return params;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.SearchDomainInterface#isObserved(int[])
	 */
	@Override
	public boolean isObserved (int... domainIndicies)
	{
		ApplicationLogger.getInstance().logDebug("Calculating Array Index for location: " + Arrays.toString(domainIndicies));
		int val = calculateArrayIndex(domainIndicies);
		
		ApplicationLogger.getInstance().logDebug("INDEX: " + val);
		ApplicationLogger.getInstance().logDebug("LIMIT: " + domain.length);
		
		if (Double.isNaN(domain[val]))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	// //////////// Helper methods
	
	/**
	 * Checks the format of the domain index provided against the specified
	 * {@link SearchDomainParams}, and then converts the N-Dimensional index
	 * into the index of the 1-D array.
	 * 
	 * @param domainIndicies index in the N-D array
	 * @return index in the 1-D array
	 */
	protected int calculateArrayIndex (int... domainIndicies)
	{
		if (domainIndicies.length != params.axisSize.size()) { throw new IllegalArgumentException(
				"Domain index vector must be same length as number of dimensions in the Search Domain"); }
		
		for (int i = 0; i < domainIndicies.length; i++ )
		{
			if (domainIndicies[i] < 0) { throw new IllegalArgumentException(
					"Domain index must be greater than zero"); }
			
			if (domainIndicies[i] > params.axisSize.get(i)) { throw new IllegalArgumentException(
					"Domain index must be less than the size of the domain axis"); }
		}
		
		return calculateIndexWeightRecursive(domainIndicies.length - 1,
				domainIndicies);
	}
	
	/**
	 * Recursively calculates the weight of a given axis' index and adds it to
	 * the weight of the indicies of the other axes, eventually returning the
	 * 1-D index
	 * 
	 * @param index current index
	 * @param domainIndicies
	 * @return
	 */
	protected int calculateIndexWeightRecursive (int index,
			int... domainIndicies)
	{
		// base case
		if (index == 0)
		{
			// System.out.println("DimSize: 1");
			// System.out.println("index: " + domainIndicies[0]);
			return domainIndicies[0];
		}
		else
		{
			// calculate the number of points along this dimension, multiply it
			// by the number of points along the dimensions of lower ordinal
			int dimSize = 1;
			
			for (int j = index; j > 0; j-- )
			{
				dimSize *= params.getAxisSize(j - 1);
			}
			
			// multiply it by the current dimension's index to get this
			// dimension's weight
			int dimIndex = domainIndicies[index] * dimSize;
			
			// add and recurse
			return dimIndex
					+ calculateIndexWeightRecursive(index - 1, domainIndicies);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		StringBuilder sb = new StringBuilder();
		
		// just because we are using this one so much
		if (params.getSearchDomainDimension() == 2)
		{
			for (int i = 0; i < params.getAxisSize(1); i++ )
			{
				for (int j = 0; j < params.getAxisSize(0); j++ )
				{
					sb.append(domain[calculateArrayIndex(j, i)]);
					sb.append(", ");
				}
				sb.append(System.lineSeparator());
			}
		}
		else
		{
			sb.append(Arrays.toString(domain));
		}
		
		return String.format(
				"SearchDomain [params=%s, " + System.lineSeparator()
						+ "domain=" + System.lineSeparator() + "%s]", params,
				sb.toString());
	}
}
