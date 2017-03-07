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
package task.tasks.value;

import java.util.Arrays;

import log.ApplicationLogger;

import pso.async.interfaces.FitnessInterface;
import pso.interfaces.StateInterface;
import event.EventDispatcher;
import event.EventListener;
import event.events.ValueEvent;
import task.Task;

/**
 * @author Mike Johnson
 * 
 */
public class ValueTask implements Task
{
	/**
	 * Used to calculate the fitness of a given location
	 */
	protected FitnessInterface	fitness		= null;
	
	/**
	 * Used to dispatch the {@link ValueEvent} to the {@link EventListener}s
	 */
	protected EventDispatcher	dispatcher	= null;
	
	/**
	 * A reference to the memory where the algorithm's state is stored
	 */
	protected StateInterface	state		= null;
	
	/**
	 * The id number of this robot to use in {@link ValueEvent}s
	 */
	protected int				id			= -1;
	
	public ValueTask (EventDispatcher ed, FitnessInterface fit,
			StateInterface s, int idNum)
	{
		dispatcher = ed;
		fitness = fit;
		state = s;
		id = idNum;
	}
	
	public ValueTask (ValueTask vt)
	{
		dispatcher = vt.dispatcher;
		fitness = vt.fitness;
		state = vt.state;
		id = vt.id;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#copy()
	 */
	@Override
	public ValueTask copy ()
	{
		return new ValueTask(this);
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
		
		// get the location we are trying to sample
		int[] location = state.getSampleLocation(id);
		
		ApplicationLogger.getInstance().logDebug(
				"SAMPLE LOCATION: " + Arrays.toString(location));
		
		// calculate the fitness value
		double value = fitness.calculateFitness(location);
		
		ApplicationLogger.getInstance()
				.logDebug("CALCULATED FITNESS: " + value);
		
		// dispatch value event
		dispatcher.dispatchEvent(new ValueEvent(id, value));
	}
	
}
