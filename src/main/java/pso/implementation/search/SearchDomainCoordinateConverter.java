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
package pso.implementation.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.data.statistics.SimpleHistogramBin;

/**
 * This class is used to convert between coordinates of the search domain and
 * indicies for the N-dimensional array representing each of the discrete
 * values.
 * 
 * First, for each axis of the search domain (e.g. X axis and Y axis for a 2D
 * domain), a maximum, a minimum, and the number of discrete points along the
 * axis to be included in the discretization are selected by creating a
 * {@link SearchDomainParams} object and passing it into the
 * {@link SearchDomainCoordinateConverter} constructor.
 * 
 * This constructor calculates delta along the axis of the search domain between
 * each of the discrete points and uses these to convert between the coordinates
 * of the search domain and the index of the array along each axis.
 * 
 * @author Mike Johnson
 * 
 */
public class SearchDomainCoordinateConverter
{
	protected double[]									axisDeltas	= null;
	
	protected Map<Integer, List<SimpleHistogramBin>>	bins		=
																			new HashMap<Integer, List<SimpleHistogramBin>>();
	
	protected SearchDomainParams						params		= null;
	
	public SearchDomainCoordinateConverter (SearchDomainParams params)
	{
		this.params = params;
		
		int dim = params.getSearchDomainDimension();
		
		// determine how many values are in the discretization for each axis
		// and
		// what the delta is between each value in the domain
		axisDeltas = new double[dim];
		
		for (int i = 0; i < dim; i++ )
		{
			// System.out.println("max: " + params.getAxisMax(i));
			// System.out.println("min: " + params.getAxisMin(i));
			// System.out.println("size: " + params.getAxisSize(i));
			
			axisDeltas[i] =
					(params.getAxisMax(i) - params.getAxisMin(i))
							/ (params.getAxisSize(i));
			// System.out.println("delta: " + axisDeltas[i]);
			
			List<SimpleHistogramBin> axisBins =
					new LinkedList<SimpleHistogramBin>();
			
			// used so that we don't need to test for floating point equality
			// (is it the first iteration)
			boolean flag = true;
			for (double d = params.getAxisMin(i); d <= params.getAxisMax(i); d +=
					axisDeltas[i])
			{
				if (flag)
				{
					axisBins.add(new SimpleHistogramBin(d, d + axisDeltas[i],
							true, true));
					
					flag = false;
				}
				else
				{
					axisBins.add(new SimpleHistogramBin(d, d + axisDeltas[i],
							false, true));
				}
			}
			
			bins.put(i, axisBins);
			
			// SimpleHistogramBin lastBin = null;
			//
			// for (SimpleHistogramBin bin : bins)
			// {
			// System.out.println("Adding bin for axis: " + i + " lb: "
			// + bin.getLowerBound() + " ub: " + bin.getUpperBound());
			// if (lastBin != null)
			// {
			// if (bin.overlapsWith(lastBin))
			// {
			// System.out.println("Overlaps!");
			// }
			// }
			// lastBin = bin;
			// }
			
		}
	}
	
	public SearchDomainParams getParams ()
	{
		return params;
	}
	
	public int[] findDomainIndicies (double... coordinates)
	{
		for (int i = 0; i < coordinates.length; i++ )
		{
			if (coordinates[i] > params.getAxisMax(i)) { throw new IllegalArgumentException(
					"Input domain value must be less than domain max value"); }
			
			if (coordinates[i] < params.getAxisMin(i)) { throw new IllegalArgumentException(
					"Input domain value must be larger than domain min value"); }
		}
		
		// the index along each axis the double value lies
		int[] domainIndicies = new int[coordinates.length];
		
		for (int i = 0; i < coordinates.length; i++ )
		{
			// iterate along each axis to find which index the value lies at
			
			int index = 0;
			
			Iterator<SimpleHistogramBin> iterator = bins.get(i).iterator();
			while (iterator.hasNext())
			{
				SimpleHistogramBin bin = iterator.next();
				
				// System.out.println("BIN: lb: " + bin.getLowerBound() +
				// " ub: "
				// + bin.getUpperBound());
				
				if (bin.accepts(coordinates[i]))
				{
					// System.out.println("accepted: d: " + domain[i] +
					// " at "
					// + index);
					break;
				}
				else
				{
					// System.out.println("adding");
					index++ ;
				}
			}
			
			// System.out.println("index: " + index);
			
			domainIndicies[i] = index;
		}
		
		return domainIndicies;
	}
	
	public double[] findDomainCoordinates (int... indicies)
	{
		double[] coordinates = new double[indicies.length];
		
		for (int i = 0; i < indicies.length; i++ )
		{
			coordinates[i] = indicies[i] * axisDeltas[i] + params.getAxisMin(i);
		}
		
		return coordinates;
	}
	
	public double[] quantize (double... coordinates)
	{
		int[] domainIndicies = findDomainIndicies(coordinates);
		
		return findDomainCoordinates(domainIndicies);
	}
}
