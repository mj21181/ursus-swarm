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
package event.eventprocessors;

import pso.interfaces.StateInterface;
import event.Event;
import event.events.InitializationEvent;

/**
 * @author Mike Johnson
 *
 */
public class InitializationEventProcessor extends AbstractEventProcessor
{
	/**
	 * A reference to the state so that we can update it when events are
	 * received
	 */
	protected StateInterface	state	= null;
	
	/**
	 * @param id
	 * @param numRobots
	 */
	public InitializationEventProcessor (int numRobots, StateInterface s)
	{
		super(numRobots);
		
		state = s;
	}
	
	/* (non-Javadoc)
	 * @see event.eventprocessors.AbstractEventProcessor#checkEventType(event.Event)
	 */
	@Override
	public boolean checkEventType (Event evt)
	{
		if (evt instanceof InitializationEvent)
		{
			logger.logDebug("STATE: Received %s from %02d",
					InitializationEvent.class.getSimpleName(), evt.getID());
			
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see event.eventprocessors.AbstractEventProcessor#runBarrierAction()
	 */
	@Override
	public void runBarrierAction ()
	{
		state.updateInitialValues(receivedEvts);
	}
	
	/* (non-Javadoc)
	 * @see event.eventprocessors.AbstractEventProcessor#shouldRunAction()
	 */
	@Override
	public boolean shouldRunAction ()
	{
		if (receivedEvts.size() == numRobots) { return true; }
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see event.eventprocessors.AbstractEventProcessor#getType()
	 */
	@Override
	public String getType ()
	{
		return InitializationEventProcessor.class.getSimpleName();
	}
	
}
