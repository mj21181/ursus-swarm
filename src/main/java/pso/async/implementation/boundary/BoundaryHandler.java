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
/**
 * 
 */
package pso.async.implementation.boundary;

import pso.MisconfiguredBlockException;
import pso.async.interfaces.BoundaryInterface;
import pso.implementation.search.SearchDomain;
import pso.interfaces.StateInterface;

/**
 * The Boundary Handling class defines an enum of different boundary handling
 * techniques. It is meant to be an single operation on an algorithm object that
 * does not maintain any state.
 * 
 * It contains a field of which enum is currently being used by the algorithm.
 * 
 * This class contains a getter and setter method for this technique. In
 * addition, there is a method that once called evaluates a switch statement
 * based on the enum and calls the specific method that implements that
 * technique.
 * 
 * There are currently four boundary handling types. They are:
 * 
 * -Changing the personal or global best -Repositioning infeasible particles
 * -Preventing infeasibility -Modifying the fitness function
 * 
 * List of Methods implementing each type
 * ========================================
 * ==========================================================
 * 
 * Changing the personal or global best - infinity - pulidoCoello
 * 
 * Repositioning infeasible particles -nearest -pBest -reflect -random
 * -nearestAbsorb -nearestDeterministicBack -nearestAdjust
 * 
 * Preventing infeasibility -hyperbolic -randomForth
 * 
 * Modifying the fitness function -boundedMirror -periodic
 * 
 * @author mike
 * 
 */
public class BoundaryHandler implements BoundaryInterface
{
	protected class VecPair
	{
		int[]	next	= null;
		int[]	cur		= null;
		
		public VecPair (int[] n, int[] c)
		{
			next = n;
			cur = c;
		}
	}
	
	protected BoundaryTechnique	technique	= null;
	
	protected StateInterface	state		= null;
	
	protected int				axisSize	= -1;
	
	protected int				id			= -1;
	
	public BoundaryHandler (int id, int axisSize, BoundaryTechnique bt,
			StateInterface s)
	{
		technique = bt;
		state = s;
		this.id = id;
		this.axisSize = axisSize;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.BoundaryInterface#handleBoundary(int[])
	 */
	@Override
	public int[] handleBoundary (int[] nextState)
			throws MisconfiguredBlockException
	{
		if (nextState.length % 2 != 0) { throw new MisconfiguredBlockException(
				"State Vector length must be even"); }
		
		int[] next = new int[nextState.length / 2];
		int[] cur = new int[nextState.length / 2];
		
		System.arraycopy(nextState, 0, cur, 0, cur.length);
		System.arraycopy(nextState, cur.length, next, 0, next.length);
		
		VecPair vp = null;
		
		switch (technique)
		{
			case NONE:
				break;
			case P_BEST:
				vp = pBest(next, cur);
				break;
			case RANDOM:
				vp = random(next, cur);
				break;
			case REFLECT_UNMODIFIED:
				vp = reflectUnmodified(next, cur);
				break;
			case REFLECT_ABSORB:
				vp = reflectAbsorb(next, cur);
				break;
			case REFLECT_ADJUST:
				vp = reflectAdjust(next, cur);
				break;
			case NEAREST_UNMODIFIED:
				vp = nearestUnmodified(next, cur);
				break;
			case NEAREST_ABSORB:
				vp = nearestAbsorb(next, cur);
				break;
			case NEAREST_DETERMINISTIC_BACK:
				vp = nearestDeterministicBack(next, cur);
				break;
			case NEAREST_RANDOM_BACK:
				vp = nearestRandomBack(next, cur);
				break;
			case NEAREST_ADJUST:
				vp = nearestAdjust(next, cur);
				break;
			case HYPERBOLIC:
				vp = hyperbolic(next, cur);
				break;
			case INF:
				break;
			case PULIDO_COELLO:
				break;
			case RANDOM_FORTH:
				break;
			case BOUNDED_MIRROR:
				break;
			case PERIODIC:
				break;
			default:
				break;
		}
		
		if (vp != null)
		{
			next = vp.next;
			cur = vp.cur;
		}
		
		System.arraycopy(cur, 0, nextState, 0, cur.length);
		System.arraycopy(next, 0, nextState, cur.length, next.length);
		
		return nextState;
	}
	
	/**
	 * 
	 * This method implements the P Best technique. This technique repositions
	 * infeasible particles that lie outside the {@link SearchDomain}.
	 * 
	 * This technique repositions an infeasible particle onto their personal
	 * best position
	 * 
	 * @param next the particle's next location
	 * @param cur the particle's current location
	 */
	protected VecPair pBest (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			if (next[i] < lb)
			{
				next = state.getBestLocation(id);
				break;
			}
			else if (next[i] >= ub)
			{
				next = state.getBestLocation(id);
				break;
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Random technique. This technique repositions
	 * infeasible particles that lie outside the {@link SearchDomain}.
	 * 
	 * This technique takes the infeasible particle and sets its position to a
	 * random location inside the bounds of the search space.
	 * 
	 * @param next the particle's next location
	 * @param cur the particle's current location
	 */
	protected VecPair random (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			if (next[i] < lb)
			{
				next = state.generateRandomVectorPrimary();
				break;
			}
			else if (next[i] >= ub)
			{
				next = state.generateRandomVectorPrimary();
				break;
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Reflect technique. This technique repositions
	 * infeasible particles that lie outside the {@link SearchDomain}.
	 * 
	 * This technique uses the boundary as a mirror and reflects the vector
	 * between the particle's infeasible position and its last feasible position
	 * back into the feasible search space.
	 * 
	 * @param next the particle's next location
	 * @param cur the particle's current location
	 */
	protected VecPair reflectUnmodified (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			int oldnext = next[i];
			
			if (next[i] < lb)
			{
				next[i] = lb + (lb - next[i]);
				cur[i] = next[i] - (oldnext - cur[i]);
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - (next[i] - (ub - 1));
				cur[i] = next[i] - (oldnext - cur[i]);
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Reflect Absorb technique. This technique
	 * repositions infeasible particles that lie outside the
	 * {@link SearchDomain}.
	 * 
	 * This technique uses the Reflect Technique in addition to the Absorb
	 * Velocity technique
	 * 
	 * @param next the particle's next location
	 * @param cur the particle's current location
	 */
	protected VecPair reflectAbsorb (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		boolean modified = false;
		
		for (int i = 0; i < next.length; i++ )
		{
			if (next[i] < lb)
			{
				next[i] = lb + (lb - next[i]);
				modified = true;
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - (next[i] - (ub - 1));
				modified = true;
			}
		}
		
		if (modified)
		{
			cur = next;
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * This method implements the Reflect Adjust technique. This technique
	 * repositions infeasible particles.
	 * 
	 * This technique uses the Reflect Technique in addition to the Adjust
	 * velocity technique
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair reflectAdjust (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		// System.out.println(String.format("NextOld: %s",
		// Arrays.toString(next)));
		// System.out.println("LB: " + lb);
		// System.out.println("UB: " + ub);
		
		for (int i = 0; i < next.length; i++ )
		{
			// System.out.println("i: " + i + ", next: " + next[i]);
			if (next[i] < lb)
			{
				next[i] = lb + (lb - next[i]);
				// System.out.println("lbbranch: " + next[i]);
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - (next[i] - (ub - 1));
				// System.out.println("ubbranch: " + next[i]);
			}
			
			// NOTE: for some reason this method was still causing a sample to
			// be outside the search space, perhaps the magnitude of the
			// reflection was larger than the width of the search space? for
			// now, adding a hard clamp if that happens
			if (next[i] >= ub)
			{
				next[i] = (ub - 1);
			}
			else if (next[i] < lb)
			{
				next[i] = lb;
			}
		}
		
		// System.out.println(String.format("NextNew: %s",
		// Arrays.toString(next)));
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Nearest technique. This technique repositions
	 * infeasible particles.
	 * 
	 * This method simply sets each particle back onto the boundary if it
	 * exceeds the boundary in each dimension.
	 * 
	 * If x_{i,t+1} > x_{i,max} then x_{i,t+1}' = x_{i,max} If x_{i,t+1} <
	 * x_{i,min} then x_{i,t+1}' = x_{i,min}
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair nearestUnmodified (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			int oldnext = next[i];
			
			if (next[i] < lb)
			{
				next[i] = lb;
				cur[i] = next[i] - (oldnext - cur[i]);
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - 1; // index from zero
				cur[i] = next[i] - (oldnext - cur[i]);
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Nearest Absorb technique. This technique
	 * repositions infeasible particles.
	 * 
	 * This method uses the nearest method but then also sets the velocity to
	 * zero.
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair nearestAbsorb (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		boolean modified = false;
		
		for (int i = 0; i < next.length; i++ )
		{
			if (next[i] < lb)
			{
				next[i] = lb;
				modified = true;
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - 1;
				modified = true;
			}
		}
		
		if (modified)
		{
			cur = next;
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * This method implements the Nearest Deterministic Back technique. This
	 * technique repositions infeasible particles.
	 * 
	 * This method uses the nearest method and then also reverses the velocity
	 * to force the particle back into the feasible space in the next iteration
	 * 
	 * v_{i,t+1}' = -\lamda*v_{i,t+1}
	 * 
	 * in some papers \lamda is 0.5, in others it is set according to distance
	 * to the boundary.
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair nearestDeterministicBack (int[] next, int[] cur)
	{
		double lamda = 0.5;
		int lb = 0;
		int ub = axisSize;
		boolean modified = false;
		int[] oldnext = new int[next.length];
		
		for (int i = 0; i < next.length; i++ )
		{
			oldnext[i] = next[i];
			
			if (next[i] < lb)
			{
				next[i] = lb;
				modified = true;
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - 1;
				modified = true;
			}
		}
		
		if (modified)
		{
			for (int i = 0; i < next.length; i++ )
			{
				cur[i] = (int) (next[i] + lamda * (oldnext[i] - cur[i]));
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Nearest Random Back technique. This technique
	 * repositions infeasible particles.
	 * 
	 * This method is identical to the deterministic back technique, but instead
	 * \lamda is a uniformly random value [0,1]
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 * @param r
	 */
	protected VecPair nearestRandomBack (int[] next, int[] cur)
	{
		double lamda = state.generateRandomBackScalar();
		
		int lb = 0;
		int ub = axisSize;
		boolean modified = false;
		int[] oldnext = new int[next.length];
		
		for (int i = 0; i < next.length; i++ )
		{
			oldnext[i] = next[i];
			
			if (next[i] < lb)
			{
				next[i] = lb;
				modified = true;
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - 1;
				modified = true;
			}
		}
		
		if (modified)
		{
			for (int i = 0; i < next.length; i++ )
			{
				cur[i] = (int) (next[i] + lamda * (oldnext[i] - cur[i]));
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Nearest Adjust technique. This technique
	 * repositions infeasible particles.
	 * 
	 * This method uses the nearest method and then sets the particle's velocity
	 * to v_{i,t+1} = x_{i,t+1}' - x_{i,t}
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair nearestAdjust (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			if (next[i] < lb)
			{
				next[i] = lb;
			}
			else if (next[i] >= ub)
			{
				next[i] = ub - 1;
			}
		}
		
		return new VecPair(next, cur);
	}
	
	/**
	 * 
	 * This method implements the Hyperbolic technique. This technique prevents
	 * infeasiblity of the particles.
	 * 
	 * This method normalizes the velocity based on distance to the boundary. If
	 * v_{i,t+1} > 0, v_{i,t+1} = \frac{v_{i,t+1}}{1 +
	 * abs(\frac{v_{i,t+1}}{x_{i,t} - x_{i,min}}}
	 * 
	 * @param numParticles
	 * @param f
	 * @param part
	 */
	protected VecPair hyperbolic (int[] next, int[] cur)
	{
		int lb = 0;
		int ub = axisSize;
		
		for (int i = 0; i < next.length; i++ )
		{
			if ( (next[i] - cur[i]) > 0)
			{
				next[i] = cur[i] + ( (next[i] - cur[i]) / (1 + (int) Math
						.abs( (next[i] - cur[i]) / ( (ub - 1) - cur[i]))));
			}
			else
			{
				next[i] = cur[i] + ( (next[i] - cur[i]) / (1
						+ (int) Math.abs( (next[i] - cur[i]) / (cur[i] - lb))));
			}
		}
		
		return new VecPair(next, cur);
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
				"BoundaryHandler [technique=%s, axisSize=%s, id=%s]", technique,
				axisSize, id);
	}
	
}
