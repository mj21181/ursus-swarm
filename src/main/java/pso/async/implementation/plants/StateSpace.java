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
package pso.async.implementation.plants;

import java.util.Arrays;

import log.ApplicationLogger;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import pso.MisconfiguredBlockException;
import pso.async.interfaces.Plant;

/**
 * This class is a {@link StateSpace} representation of a Linear, time invariant
 * (LTI) dynamic system.
 * 
 * @author Mike Johnson
 * 
 */
public class StateSpace implements Plant
{
	/**
	 * The A Matrix
	 */
	protected RealMatrix	A				= null;
	
	/**
	 * The B Matrix
	 */
	protected RealMatrix	B				= null;
	
	/**
	 * The C Matrix
	 */
	protected RealMatrix	C				= null;
	
	/**
	 * The D Matrix
	 */
	protected RealMatrix	D				= null;
	
	/**
	 * The time index
	 */
	protected long			k				= 0;
	
	/**
	 * The initial state vector
	 */
	protected int[]			initialState	= null;
	
	/**
	 * The next state vector
	 */
	protected int[]			nextState		= null;
	
	/**
	 * The current state vector
	 */
	protected int[]			state			= null;
	
	/**
	 * Constructs a {@link StateSpace} system from a set of
	 * {@link LinearPlantParameters}
	 * 
	 * @throws MisconfiguredBlockException if the parameters are not valid
	 * 
	 */
	public StateSpace (LinearPlantParameters params)
			throws MisconfiguredBlockException
	{
		// initialize our values
		k = 0;
		
		int ovs = params.getOutputVectorSize();
		int ivs = params.getInputVectorSize();
		int svs = params.getStateVectorSize();
		
		state = new int[svs];
		nextState = new int[svs];
		
		A = params.getA();
		B = params.getB();
		C = params.getC();
		D = params.getD();
		
		// check if the model is misconfigured
		if (ivs != B.getColumnDimension()) { throw new MisconfiguredBlockException(
				"B Matrix must have same number of Columns as size of input vector."); }
		
		if (svs != B.getRowDimension()) { throw new MisconfiguredBlockException(
				"B Matrix must have same number of Rows as size of state vector."); }
		
		if (svs != A.getColumnDimension()) { throw new MisconfiguredBlockException(
				"A Matrix must have same number of Columns as size of state vector."); }
		
		if (svs != A.getRowDimension()) { throw new MisconfiguredBlockException(
				"A Matrix must have same number of Rows as size of state vector."); }
		
		if (ovs != C.getRowDimension()) { throw new MisconfiguredBlockException(
				"C Matrix must have same number of Rows as size of output vector."); }
		
		if (svs != C.getColumnDimension()) { throw new MisconfiguredBlockException(
				"C Matrix must have same number of Columns as size of state vector."); }
		
		if (ivs != D.getColumnDimension()) { throw new MisconfiguredBlockException(
				"D Matrix must have same number of Columns as size of input vector."); }
		
		if (ovs != D.getRowDimension()) { throw new MisconfiguredBlockException(
				"D Matrix must have same number of Rows as size of output vector."); }
	}
	
	/**
	 * Resets the {@link StateSpace} model back to the initial timestep and sets
	 * the current state to the initial state
	 */
	public void reset ()
	{
		k = 0;
		state = Arrays.copyOf(initialState, initialState.length);
	}
	
	/**
	 * @return the a
	 */
	public RealMatrix getA ()
	{
		return A;
	}
	
	/**
	 * @param a the a to set
	 */
	public void setA (RealMatrix a)
	{
		A = a;
	}
	
	/**
	 * @return the b
	 */
	public RealMatrix getB ()
	{
		return B;
	}
	
	/**
	 * @param b the b to set
	 */
	public void setB (RealMatrix b)
	{
		B = b;
	}
	
	/**
	 * @return the c
	 */
	public RealMatrix getC ()
	{
		return C;
	}
	
	/**
	 * @param c the c to set
	 */
	public void setC (RealMatrix c)
	{
		C = c;
	}
	
	/**
	 * @return the d
	 */
	public RealMatrix getD ()
	{
		return D;
	}
	
	/**
	 * @param d the d to set
	 */
	public void setD (RealMatrix d)
	{
		D = d;
	}
	
	/**
	 * @return the k
	 */
	public long getK ()
	{
		return k;
	}
	
	/**
	 * @param k the k to set
	 */
	public void setK (long k)
	{
		this.k = k;
	}
	
	/**
	 * @return the state
	 */
	public int[] getState ()
	{
		return Arrays.copyOf(state, state.length);
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState (int... state)
	{
		this.state = state;
	}
	
	/**
	 * @return the initialState
	 */
	public int[] getInitialState ()
	{
		return Arrays.copyOf(initialState, initialState.length);
	}
	
	/**
	 * Sets the initial state for the dynamics.
	 * 
	 * @param initial
	 * @throws MisconfiguredBlockException
	 */
	public void setInitialState (int... initial)
			throws MisconfiguredBlockException
	{
		ApplicationLogger.getInstance().logDebug("PLANT: initializing");
		
		if (initialState == null)
		{
			initialState = new int[state.length];
		}
		
		if (initial.length != initialState.length) { throw new MisconfiguredBlockException(
				"Initial State must have dimension of " + initialState.length); }
		
		initialState = initial;
		
		state = Arrays.copyOf(initialState, initialState.length);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.Plant#isInitialized()
	 */
	@Override
	public boolean isInitialized ()
	{
		if (initialState == null) { return false; }
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.async.interfaces.Plant#transfer(int[])
	 */
	@Override
	public int[] transfer (int... input) throws MisconfiguredBlockException
	{
		if (input.length != B.getColumnDimension()) { throw new MisconfiguredBlockException(
				"Input vector must have dimension equal to B Matrix number of Columns."); }
		
		// System.out.println("Input!: " + input.toString());
		
		RealMatrix inputMatrix = new Array2DRowRealMatrix(input.length, 1);
		
		for (int i = 0; i < input.length; i++ )
		{
			inputMatrix.setEntry(i, 0, input[i]);
		}
		
		RealMatrix stateMatrix = new Array2DRowRealMatrix(state.length, 1);
		
		for (int i = 0; i < state.length; i++ )
		{
			stateMatrix.setEntry(i, 0, state[i]);
		}
		
		RealMatrix nextStateMatrix =
				A.multiply(stateMatrix).add(B.multiply(inputMatrix));
		RealMatrix output =
				C.multiply(nextStateMatrix).add(D.multiply(inputMatrix));
		
		for (int i = 0; i < state.length; i++ )
		{
			nextState[i] = (int) Math.floor(nextStateMatrix.getEntry(i, 0));
		}
		
		propagateState();
		
		int[] outputIndicies = new int[output.getRowDimension()];
		
		for (int i = 0; i < outputIndicies.length; i++ )
		{
			outputIndicies[i] = (int) Math.floor(output.getEntry(i, 0));
		}
		
		return outputIndicies;
	}
	
	/**
	 * Increments one timestep in the signal.
	 * 
	 * Delays the next state so that it becomes the current state.
	 * 
	 * This allows the solution to the finite difference equations to be
	 * calculated
	 */
	protected void propagateState ()
	{
		k++ ;
		
		for (int i = 0; i < state.length; i++ )
		{
			state[i] = nextState[i];
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
		return String
				.format("StateSpace [A=%s, B=%s, C=%s, D=%s, k=%s, initialState=%s, nextState=%s, state=%s]",
						A, B, C, D, k, Arrays.toString(initialState), Arrays.toString(nextState), Arrays.toString(state));
	}
}
