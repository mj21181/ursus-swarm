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
package pso.async.implementation.fitness;

import pso.async.interfaces.FitnessInterface;
import pso.implementation.search.SearchDomain;
import pso.implementation.search.SearchDomainCoordinateConverter;
import pso.implementation.search.SearchDomainParams;

/**
 * @author Mike Johnson
 * 
 */
public class FitnessCalculator implements FitnessInterface
{
	/**
	 * Used to convert the coordinates of the {@link SearchDomain} into the
	 * domain of the fitness function
	 */
	protected SearchDomainCoordinateConverter	converter	= null;
	
	/**
	 * Used for fitness functions imported from a data file from the Mars
	 * Orbiter Laser Altimeter
	 */
	protected double[][]						molaData	= null;
	
	/**
	 * The fitness function type enum
	 */
	protected FitFunction						function	= null;
	
	/**
	 * Constructor for a given {@link SearchDomain} and {@link FitFunction}
	 * 
	 * @param params
	 * @param f
	 */
	public FitnessCalculator (SearchDomainParams params, FitFunction f)
	{
		function = f;
		
		converter = new SearchDomainCoordinateConverter(params);
	}
	
	/**
	 * Constructor for a given {@link SearchDomain} and MOLA Data file
	 * 
	 * @param params
	 * @param mola
	 */
	public FitnessCalculator (SearchDomainParams params, double[][] mola)
	{
		this(params, FitFunction.MOLA);
		
		molaData = mola;
	}
	
	/**
	 * Calculated the fitness score for a given location in the
	 * {@link SearchDomain} based on the currently selected Fitness Function
	 * Enum
	 * 
	 * @param m the location to calculate the fitness function at
	 * @return the fitness score
	 */
	public double calculateFitness (int... m)
	{
		double val = Double.NaN;
		
		switch (function)
		{
			case SPHERE:
				val = sphere(m);
				break;
			case ROSENBROCK:
				val = rosenbrock(m);
				break;
			case RASTRIGIN:
				val = rastrigin(m);
				break;
			case GRIEWANK:
				val = griewank(m);
				break;
			case ACKLEY:
				val = ackley(m);
				break;
			case MICHALEWICZ:
				val = michalewicz(m);
				break;
			case SCHWEFEL:
				val = schwefel(m);
				break;
			case FLAT:
				val = flat(m);
				break;
			case MOLA:
				val = mola(m);
				break;
			case CEL_NAV:
				val = celNav(m);
				break;
			default:
		}
		
		return val;
	}
	
	/**
	 * This function is one of the easiest optimization functions. A.k.a the
	 * first deJong function. There is only one minimum located at the bottom of
	 * the parabola.
	 * 
	 * Global Minimum: x[i] = 0 from 1 to n, fitness = 0
	 * 
	 * http://www.pg.gda.pl/~mkwies/dyd/geadocu/fcnfun1.html
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double sphere (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double sum = 0;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum = sum + Math.pow(x[i], 2);
		}
		
		return (sum);
	}
	
	/**
	 * This function is a classic optimization test function. A.k.a Banana
	 * Function, A.k.a second function of deJong The global minimum lies inside
	 * a long, narrow, and parabolic shaped flat valley. It is easy to find the
	 * valley but hard to find the minimum.
	 * 
	 * Global Minimum: x[i] = 1 from 1 to n, fitness = 0
	 * 
	 * https://en.wikipedia.org/wiki/Rosenbrock_function
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double rosenbrock (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double sum = 0;
		
		for (int i = 0; i < (x.length - 1); i++ )
		{
			sum =
					sum
							+ (100 * Math.pow(x[i + 1] - Math.pow(x[i], 2), 2) + Math
									.pow( (1 - x[i]), 2));
		}
		
		return sum;
	}
	
	/**
	 * This function is based on the deJong function but also has cosine terms
	 * modulating it so that there are multiple minima.
	 * 
	 * Global Minimum: x[i] = 0 from 1 to n, fitness = 0
	 * 
	 * https://en.wikipedia.org/wiki/Rastrigin_function
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double rastrigin (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double sum = 0;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum = sum + (Math.pow(x[i], 2) - 10 * Math.cos(2 * Math.PI * x[i]));
		}
		
		return (10 * x.length + sum);
	}
	
	/**
	 * This function is similar to the Rastrigin function, having many
	 * widespread minima that are regularly distributed around the search space.
	 * At a large scale, it looks like the deJong function, but when you look at
	 * the details there are many local minima.
	 * 
	 * Global Minimum: x[i] = 0 from 1 to n, fitness = 0
	 * 
	 * http://www.zsd.ict.pwr.wroc.pl/files/docs/functions.pdf
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double griewank (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
//		System.out.println("x: " + x[0]);
//		System.out.println("y: " + x[1]);
		
		double sum = 0;
		double product = 1;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum = sum + Math.pow(x[i], 2);
//			System.out.println("Sum: " + sum);
			product = product * Math.cos(x[i] / Math.sqrt(i + 1));
//			System.out.println("product: " + product);
		}
		
		return (sum / 4000.0 - product + 1);
	}
	
	/**
	 * The ackley function is a widely used multimodal function (multiple
	 * optima)
	 * 
	 * Global Minimum: x[i] = 0 from 1 to n, fitness = 0
	 * 
	 * http://www.zsd.ict.pwr.wroc.pl/files/docs/functions.pdf
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double ackley (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double sum = 0;
		double sum2 = 0;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum = sum + Math.pow(x[i], 2);
			sum2 = sum2 + Math.cos(2 * Math.PI * x[i]);
		}
		
		return (-20
				* Math.exp(-0.2 * Math.sqrt( (1 / (double) x.length) * sum))
				- Math.exp( (1 / (double) x.length) * sum2) + 20 + Math.E);
	}
	
	/**
	 * This function is a multimodal function (multiple optima) with n! optima.
	 * The m value declared controls the steepness of the slope. Larger m is
	 * harder. A common m value is 10.
	 * 
	 * Global Minimum n = 2, fitness = -1.8013, x = 2.2, y = 1.57
	 * 
	 * http://www.zsd.ict.pwr.wroc.pl/files/docs/functions.pdf
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double michalewicz (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		int slope = 10;
		
		double sum = 0;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum =
					sum
							+ (Math.sin(x[i]) * Math.pow(
									Math.sin( (i + 1) * Math.pow(x[i], 2)
											/ Math.PI), 2 * slope));
		}
		
		return -sum;
	}
	
	/**
	 * This function is designed to make the global minimum distant from the
	 * next best local minimum so that the algorithm is more likely to converge
	 * to the wrong location.
	 * 
	 * Global Minimum x[i] = 420.9687 from 1 to n, fitness = -n*418.9829
	 * 
	 * http://www.zsd.ict.pwr.wroc.pl/files/docs/functions.pdf
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double schwefel (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double sum = 0;
		
		for (int i = 0; i < x.length; i++ )
		{
			sum = sum + (x[i] * Math.sin(Math.sqrt(Math.abs(x[i]))));
		}
		
		return 418.9829 * x.length - sum;
	}
	
	protected double celNav (int[] m)
	{
		double[] x = converter.findDomainCoordinates(m);
		
		double factor = 3600 / 24.2;
		
		double xi = 929.1361;
		double roll = -0.54;
		double yi = 93.5697;
		double pitch = 62.62 * factor;
		double desired = 58.0483 * factor;
		
		double rollOff = x[0];
		double pitchOff = x[1];
		
		double sinRoll =
				Math.sin(Math.toRadians(roll) + Math.toRadians(rollOff));
		double cosRoll =
				Math.cos(Math.toRadians(roll) + Math.toRadians(rollOff));
		
		double val =
				-xi * sinRoll + (yi + (pitch + pitchOff * factor)) * cosRoll
						- desired;
		
		val = Math.pow(val, 2);
		
		return val;
	}
	
	/**
	 * This function is designed to always return a fitness score of 1
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double flat (int[] m)
	{
		return 1.0;
	}
	
	/**
	 * This function returns a value based on a PNG greyscale heightmap.
	 * 
	 * @param m the vector of a location to calculate the fitness value for
	 */
	protected double mola (int[] m)
	{
		if (molaData == null) { throw new IllegalStateException(
				"No MOLA Data set"); }
		
		int i = m[0];
		int j = m[1];
		
		if (i > (molaData.length - 1) || i < 0)
		{
			return Double.NaN;
		}
		
		if (j > (molaData[i].length - 1) || j < 0)
		{
			return Double.NaN;
		}
		
		double val = molaData[i][j];
		
		return val;
	}
}
