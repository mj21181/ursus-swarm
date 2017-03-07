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
package task.tasks.move;

import pso.async.interfaces.MovementInterface;
import pso.implementation.search.SearchDomain;
import pso.interfaces.StateInterface;
import event.EventDispatcher;
import event.EventListener;
import event.events.LocationEvent;
import event.events.MoveEvent;
import task.Task;

/**
 * @author Mike Johnson
 * 
 */
public class MoveTask implements Task
{
	/**
	 * Used to move the robot to a specific location within the
	 * {@link SearchDomain}
	 */
	protected MovementInterface	mover		= null;
	
	/**
	 * Used to dispatch the {@link LocationEvent} to the {@link EventListener}s
	 */
	protected EventDispatcher	dispatcher	= null;
	
	/**
	 * A reference to the memory where the algorithm's state is stored
	 */
	protected StateInterface	state		= null;
	
	/**
	 * The id number of this robot to use in {@link LocationEvent}s
	 */
	protected int				id			= -1;
	
	/**
	 * 
	 */
	public MoveTask (int id, EventDispatcher ed, StateInterface s,
			MovementInterface move)
	{
		this.id = id;
		state = s;
		dispatcher = ed;
		mover = move;
	}
	
	public MoveTask (MoveTask mt)
	{
		id = mt.id;
		state = mt.state;
		dispatcher = mt.dispatcher;
		mover = mt.mover;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#copy()
	 */
	@Override
	public MoveTask copy ()
	{
		return new MoveTask(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#execute()
	 */
	@Override
	public void execute ()
	{
		// get the location we are moving to
		int[] location = state.getSampleLocation(id);
		
		// perform move
		boolean atLocation = mover.performMove(location);
		
		if (atLocation)
		{
			// dispatch event so other robots know
			dispatcher.dispatchEvent(new MoveEvent(id));
		}
		else
		{
			throw new IllegalStateException(
					"Lower layer did not succeed moving robot to desired location");
		}
	}
	
}
