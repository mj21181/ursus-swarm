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
package task.tasks.location;

import java.nio.IntBuffer;
import java.util.Arrays;

import event.EventDispatcher;
import event.EventListener;
import event.events.LocationEvent;
import log.ApplicationLogger;
import pso.MisconfiguredBlockException;
import pso.async.implementation.boundary.BoundaryHandler;
import pso.async.implementation.duplicate.FullySampledException;
import pso.async.interfaces.BoundaryInterface;
import pso.async.interfaces.DuplicateInterface;
import pso.async.interfaces.FeedbackControlSystem;
import pso.interfaces.StateInterface;
import task.Task;

/**
 * @author Mike Johnson
 * 
 */
public class LocationTask implements Task
{
	/**
	 * Used to calculate the next search location
	 */
	protected FeedbackControlSystem			dynamics	= null;
	
	/**
	 * Used to alter the search location so that it is within the boundary of
	 * the search domain
	 */
	protected BoundaryInterface				boundary	= null;
	
	/**
	 * Used to dispatch the {@link LocationEvent} to the {@link EventListener}s
	 */
	protected EventDispatcher				dispatcher	= null;
	
	/**
	 * A reference to the memory where the algorithm's state is stored
	 */
	protected StateInterface				state		= null;
	
	/**
	 * Used to prevent duplicate observations of a location within the search
	 * domain if the fitness function is not time varying
	 */
	protected DuplicateInterface	duplicate	= null;
	
	/**
	 * The id number of this robot to use in {@link LocationEvent}s
	 */
	protected int							id			= -1;
	
	/**
	 * Constructor used to initialize this {@link LocationTask}
	 * 
	 * @param sys
	 * @param b
	 * @param ed
	 * @param s
	 * @param idNum
	 */
	public LocationTask (FeedbackControlSystem sys, BoundaryInterface b,
			EventDispatcher ed, StateInterface s,
			DuplicateInterface dup, int idNum)
	{
		id = idNum;
		dynamics = sys;
		boundary = b;
		dispatcher = ed;
		state = s;
		duplicate = dup;
		
		ApplicationLogger.getInstance()
				.logDebug("BT IN USE: " + ((BoundaryHandler) b).toString());
	}
	
	public LocationTask (LocationTask lt)
	{
		id = lt.id;
		
		dynamics = lt.dynamics;
		boundary = lt.boundary;
		dispatcher = lt.dispatcher;
		state = lt.state;
		duplicate = lt.duplicate;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#copy()
	 */
	@Override
	public LocationTask copy ()
	{
		return new LocationTask(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#execute()
	 */
	@Override
	public void execute ()
	{
		// check if we should exit
		if (state.shouldExit()) { return; }
		
		// get desired location
		int[] desired = state.getDesiredLocation(id);
		// System.out.println("Des: " + Arrays.toString(desired));
		try
		{
			// check whether the system has been initialized
			if (!dynamics.isInitialized())
			{
				// System.out.println("Not initialized");
				int[] initial = state.getInitialState(id);
				// System.out
				// .println("Initial State: " + Arrays.toString(initial));
				
				dynamics.intialize(initial);
				// System.out.println("Initialized");
			}
			
			IntBuffer inputBuffer = IntBuffer.allocate(desired.length * 2);
			
			inputBuffer.position(desired.length);
			inputBuffer.put(desired);
			
			// ApplicationLogger.getInstance().logDebug("INPUT: " +
			// Arrays.toString(inputBuffer.array()));
			
			// calculate Particle Dynamics to find next search location
			int[] location = dynamics.transfer(inputBuffer.array());
			
			ApplicationLogger.getInstance()
					.logDebug("OUTPUTD: " + Arrays.toString(location));
			
			// handle any boundary violations
			int[] nextState = boundary.handleBoundary(location);
			
			ApplicationLogger.getInstance()
					.logDebug("NEXT STATE: " + Arrays.toString(nextState));
			
			int[] nextLocation = new int[desired.length];
			System.arraycopy(nextState, nextLocation.length, nextLocation, 0,
					nextLocation.length);
			
			// check if the location has been observed
			if (duplicate.isAlreadyObserved(nextLocation))
			{
				ApplicationLogger.getInstance().logDebug("OBSERVED");
				
				try
				{
					nextLocation =
							duplicate.selectUnobservedLocation(nextLocation);
				}
				catch (FullySampledException e)
				{
					for (int i = 0; i < nextLocation.length; i++ )
					{
						nextLocation[i] = -1;
					}
				}
				
				ApplicationLogger.getInstance().logDebug(
						"ALTERED LOCATION: " + Arrays.toString(nextLocation));
				
				System.arraycopy(nextLocation, 0, nextState,
						nextLocation.length, nextLocation.length);
			}
			else
			{
				ApplicationLogger.getInstance().logDebug("NOT OBSERVED");
			}
			
			// take the modified next state and gets the
			int[] output = new int[desired.length];
			
			System.arraycopy(nextState, output.length, output, 0,
					output.length);
			
			ApplicationLogger.getInstance()
					.logDebug("OUTPUT: " + Arrays.toString(output));
			
			// dispatch location
			dispatcher.dispatchEvent(new LocationEvent(id, output));
		}
		catch (MisconfiguredBlockException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
}
