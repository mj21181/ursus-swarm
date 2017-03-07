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
package task.tasks;

import java.util.concurrent.ArrayBlockingQueue;

import task.Task;

import log.ApplicationLogger;
import event.Event;
import event.EventProcessor;

/**
 * @author Mike Johnson
 * 
 */
public class WaitForEventTask implements Task
{
	/**
	 * Shortcut reference for convenience
	 */
	protected ApplicationLogger			log			= ApplicationLogger
															.getInstance();
	
	/**
	 * The {@link EventProcessor} used to handle the {@link Event}s that are
	 * received.
	 */
	protected EventProcessor			processor	= null;
	
	/**
	 * The queue of received events from other robots in the swarm.
	 */
	protected ArrayBlockingQueue<Event>	queue		= null;
	
	/**
	 * 
	 */
	public WaitForEventTask (EventProcessor ep,
			ArrayBlockingQueue<Event> eventQueue)
	{
		processor = ep;
		queue = eventQueue;
	}
	
	public WaitForEventTask (WaitForEventTask wfet)
	{
		processor = wfet.processor;
		queue =
				new ArrayBlockingQueue<Event>(wfet.queue.size()
						+ wfet.queue.remainingCapacity());
		
		queue.addAll(wfet.queue);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#copy()
	 */
	@Override
	public WaitForEventTask copy ()
	{
		return new WaitForEventTask(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see task.Task#execute()
	 */
	@Override
	public void execute ()
	{
		boolean waiting = true;
		
		while (waiting)
		{
			try
			{
				log.logDebug("Waiting for Event");
				
				Event evt = queue.take();
				
				log.logDebug("Event Received. Checking type.");
				
				boolean correctType = processor.checkEventType(evt);
				
				log.logDebug("Correct Type: " + correctType);
				
				if (correctType)
				{
					// process it and determine if we are done waiting
					waiting = processor.processEvent(evt);
					
					log.logDebug("Event Processed. Still waiting: " + waiting);
				}
				else
				{
					// save it, might be from a future event
					queue.put(evt);
				}
			}
			catch (InterruptedException e)
			{
				log.logDebug(
						"STATE: WaitForEvents: Event %s Interrupted, stopping waiting",
						processor.getType());
				waiting = false;
				break;
			}
		}
	}
	
}
