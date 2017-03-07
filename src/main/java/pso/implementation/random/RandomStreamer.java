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
package pso.implementation.random;

import java.util.ArrayList;

import pso.Sample;
import pso.async.implementation.boundary.BoundaryHandler;
import pso.async.implementation.duplicate.DuplicateDetector;
import pso.implementation.optimization.SampleOptimizer;
import pso.implementation.search.SearchDomain;

/**
 * This class is responsible for initializing and sampling the PRNGs used in the
 * PSO algorithm.
 * 
 * The seeds in the simulation need to be selected so that the samples generated
 * are statistically independent both from each other and from the PRNGs being
 * used on other robots (particles) participating in the simulation.
 * 
 * @author Mike Johnson
 * 
 */
public class RandomStreamer
{
	/**
	 * Used to generate a uniform random value for the global optimizer - to
	 * resolve ties in fitness values between samples
	 */
	protected Random			globlaTieBreakGen	= null;
	
	/**
	 * Scalar RNG used to generate lamda for the random back boundary technique
	 */
	protected Random			randBackGen			= null;
	
	/**
	 * Used to prevent duplicate locations in the search domain from being
	 * sampled
	 */
	protected Random			duplicateRemovalGen	= null;
	
	/**
	 * Vector RNG used to generate a uniform random value for each local
	 * optimizer - to resolve ties in fitness values between samples
	 */
	protected ArrayList<Random>	localTieBreakGens	= null;
	
	/**
	 * Vector RNG used to generate a random current position for initialization
	 * and for the noise generator used for particle dynamics
	 * 
	 */
	protected ArrayList<Random>	primaryFitnessGen	= null;
	
	/**
	 * Vector RNG used to generate a random last position for initialization and
	 * for the random boundary technique
	 */
	protected ArrayList<Random>	secondaryFitnessGen	= null;
	
	/**
	 * The number of dimensions in the fitness function
	 */
	protected int				dim					= 0;
	
	/**
	 * Initializes the {@link RandomStreamer} for a given {@link SearchDomain}
	 * axis size, and {@link SeedSet}
	 * 
	 * @param axisSize
	 * @param s
	 */
	public RandomStreamer (int axisSize, SeedSet s)
	{
		int numRobots = s.getLocalTieSeeds().size();
		int numDimensions = s.getPrimaryFitnessSeeds().size();
		
		// initialize the non-vector RNGs, 0 - 2, not including 2 so generates 0
		// or 1
		globlaTieBreakGen = new Random(s.getGlobalTieSeed(), 0, 2);
		randBackGen = new Random(s.getRandBackSeed(), 0, 1);
		duplicateRemovalGen = new Random(s.getDuplicateSeed(), 0, 1);
		
		// initialize the RNGs used to generate tie breaking values for the
		// SampleOptimizers
		localTieBreakGens = new ArrayList<Random>(numRobots);
		
		for (Integer seed : s.getLocalTieSeeds())
		{
			// 0 - 2, not including 2 so generates 0 or 1
			localTieBreakGens.add(new Random(seed, 0, 2));
		}
		
		// initialize the RNGs used to generate random vectors within the
		// SearchDomain
		primaryFitnessGen = new ArrayList<Random>(numDimensions);
		secondaryFitnessGen = new ArrayList<Random>(numDimensions);
		
		for (Integer seed : s.getPrimaryFitnessSeeds())
		{
			primaryFitnessGen.add(new Random(seed, 0, axisSize));
		}
		
		for (Integer seed : s.getSecondaryFitnessSeeds())
		{
			secondaryFitnessGen.add(new Random(seed, 0, axisSize));
		}
		
		// set the number of dimensions
		dim = numDimensions;
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} by allocating a new
	 * int[], generating the components, and returning the int[]
	 * 
	 * @param list
	 * @return
	 */
	protected int[] generateRandomSearchDomainVector (ArrayList<Random> list)
	{
		int[] rand = new int[dim];
		
		for (int i = 0; i < dim; i++ )
		{
			rand[i] = list.get(i).randomInt();
		}
		
		return rand;
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} by using an
	 * existing int[] int[], generating the components, and returning the int[]
	 * 
	 * @param list
	 * @return
	 */
	protected int[] generateRandomSearchDomainVector (ArrayList<Random> list,
			int[] rand)
	{
		for (int i = 0; i < dim; i++ )
		{
			rand[i] = list.get(i).randomInt();
		}
		
		return rand;
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} using the primary
	 * source of randomness, allocating a new int[]
	 * 
	 * @return
	 */
	public int[] generateVectorPrimary ()
	{
		return generateRandomSearchDomainVector(primaryFitnessGen);
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} using the primary
	 * source of randomness, using an existing int[]
	 * 
	 * @return
	 */
	public int[] generateVectorPrimary (int[] vec)
	{
		return generateRandomSearchDomainVector(primaryFitnessGen, vec);
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} using the secondary
	 * source of randomness, allocating a new int[]
	 * 
	 * @return
	 */
	public int[] generateVectorSecondary ()
	{
		return generateRandomSearchDomainVector(secondaryFitnessGen);
	}
	
	/**
	 * Generates a random vector in the {@link SearchDomain} using the secondary
	 * source of randomness, using an existing int[]
	 * 
	 * @return
	 */
	public int[] generateVectorSecondary (int[] vec)
	{
		return generateRandomSearchDomainVector(secondaryFitnessGen, vec);
	}
	
	/**
	 * Generates a value for use by the Random Back boundary technique used by
	 * the {@link BoundaryHandler}
	 * 
	 * @return
	 */
	public double generateRandomBackScalar ()
	{
		return randBackGen.randomDouble();
	}
	
	/**
	 * Generates a value for use by the {@link DuplicateDetector} to remove
	 * duplicate locations
	 * 
	 * @return
	 */
	public double generateRandomDuplicateRemovalValue ()
	{
		return duplicateRemovalGen.randomDouble();
	}
	
	/**
	 * Generates a value used to break ties in fitness value between two
	 * {@link Sample}s being checked by one of the Local {@link SampleOptimizer}
	 * 
	 * @param id
	 * @return
	 */
	public int generateRandomTieBreaker (int id)
	{
		return localTieBreakGens.get(id).randomInt();
	}
	
	/**
	 * Generates a value used to break ties in fitness value between two
	 * {@link Sample}s being checked by the Global {@link SampleOptimizer}
	 * 
	 * @param id
	 * @return
	 */
	public int generateGlobalTieBreaker ()
	{
		return globlaTieBreakGen.randomInt();
	}
}
