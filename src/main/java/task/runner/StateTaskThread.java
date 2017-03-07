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
package task.runner;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;

import pso.interfaces.StateInterface;

import log.ApplicationLogger;

import event.Event;
import event.EventListener;

/**
 * @author Mike Johnson
 * 
 */
public class StateTaskThread extends TaskThread implements EventListener
{
	/**
	 * Shortcut reference for convenience
	 */
	protected ApplicationLogger			log			= ApplicationLogger
															.getInstance();
	
	/**
	 * The queue of received events from other robots in the swarm.
	 */
	protected ArrayBlockingQueue<Event>	queue		= null;
	
	/**
	 * @param cb
	 */
	public StateTaskThread (int numRobots, CyclicBarrier cb, StateInterface s)
	{
		super(cb, s);
		
		queue = new ArrayBlockingQueue<Event>(2 * numRobots);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update (Observable arg0, Object arg1)
	{
		if (arg1 instanceof Event)
		{
			receiveEvent((Event) arg1);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * event.EventListener#receiveEvent(edu.unh.acl.app
	 * .model.event.Event)
	 */
	@Override
	public void receiveEvent (Event evt)
	{
		try
		{
			log.logDebug("WORKER %02d: Adding event to STATE queue",
					evt.getID());
			
			// queue.add(evt);
			queue.put(evt);
			
			log.logDebug("WORKER %02d: Added event to STATE queue", evt.getID());
		}
		catch (InterruptedException e)
		{
			if (running)
			{
				log.logException(e);
			}
		}
	}
}
