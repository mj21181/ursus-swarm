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
package task.tasks.init;

import log.ApplicationLogger;
import event.EventDispatcher;
import event.EventListener;
import event.events.InitializationEvent;

import pso.interfaces.StateInterface;

import task.Task;

/**
 * @author Mike Johnson
 * 
 */
public class ParticleInitializationTask implements Task
{
	/**
	 * Used to dispatch the {@link InitializationEvent} to the
	 * {@link EventListener}s
	 */
	protected EventDispatcher	dispatcher	= null;
	
	/**
	 * A reference to the memory where the algorithm's state is stored
	 */
	protected StateInterface	state		= null;
	
	/**
	 * The id number of this robot to use in {@link InitializationEvent}
	 */
	protected int				id			= -1;
	
	/**
	 * Constructor for the initialization task
	 * 
	 * @param ed
	 * @param s
	 * @param idNum
	 */
	public ParticleInitializationTask (EventDispatcher ed, StateInterface s,
			int idNum)
	{
		dispatcher = ed;
		state = s;
		id = idNum;
	}
	
	public ParticleInitializationTask (ParticleInitializationTask pit)
	{
		dispatcher = pit.dispatcher;
		state = pit.state;
		id = pit.id;
	}
	
	/**
	 * Calculates an initial change in velocity based on the Half-Diff
	 * methodology
	 * 
	 * @param initial
	 * @param last
	 * @return
	 */
	protected int[] calculatePositionDelta (int[] initial, int[] last)
	{
		// subtract to create the change in position
		int[] change = new int[initial.length];
		
		for (int i = 0; i < change.length; i++ )
		{
			change[i] = initial[i] - last[i];
		}
		
		// divide change in position by two, floor round to require that two
		// bins of the input histogram before the division map to one bin of the
		// resulting histogram
		for (int i = 0; i < change.length; i++ )
		{
			change[i] = (int) Math.floor(change[i] / 2.0);
		}
		
		return change;
	}
	
	/**
	 * Calculates what the last position should be based on the generated change
	 * in position
	 * 
	 * @param initial the initial position
	 * @param change the change in position
	 * @return the last position
	 */
	protected int[] updateLastPosition (int[] initial, int[] change)
	{
		int[] last = new int[change.length];
		
		for (int i = 0; i < change.length; i++ )
		{
			last[i] = initial[i] - change[i];
		}
		
		return last;
	}
	
	/**
	 * Takes the initial current position and the adjusted last position and
	 * concatenates them into the initial state vector
	 * 
	 * @param last
	 * @param current
	 * @return
	 */
	protected int[] createInitialStateVector (int[] last, int[] current)
	{
		int[] initial = new int[current.length + last.length];
		
		System.arraycopy(last, 0, initial, 0, last.length);
		System.arraycopy(current, 0, initial, last.length, current.length);
		
		return initial;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#copy()
	 */
	@Override
	public ParticleInitializationTask copy ()
	{
		return new ParticleInitializationTask(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#execute()
	 */
	@Override
	public void execute ()
	{
		ApplicationLogger.getInstance().logDebug("Initializing");
		
		// create a position randomly
		int[] initialCurrent = state.generateRandomVectorPrimary();
		
		// generate a last position randomly
		
		int[] initialLast = state.generateRandomVectorSecondary();
		
		// calculate the initial change in position
		int[] change = calculatePositionDelta(initialCurrent, initialLast);
		
		// update the last position to reflect this change
		initialLast = updateLastPosition(initialCurrent, change);
		
		// create the initial state vector
		int[] initial = createInitialStateVector(initialLast, initialCurrent);
		
		// dispatch the event
		dispatcher.dispatchEvent(new InitializationEvent(id, initial));
	}
	
}
