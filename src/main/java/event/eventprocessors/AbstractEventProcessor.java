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
package event.eventprocessors;

import java.util.ArrayList;
import java.util.Collections;

import log.ApplicationLogger;

import event.Event;
import event.EventProcessor;

/**
 * This class is the abstract implementation of the {@link EventProcessor}
 * interface.
 * 
 * It provides the logic common for all child {@link EventProcessor}s.
 * 
 * @author Mike Johnson
 * 
 */
public abstract class AbstractEventProcessor implements EventProcessor
{
	/**
	 * The number of other robots in this simulation
	 */
	protected int				numRobots		= 0;
	
	/**
	 * The {@link ArrayList} of received {@link Event}s from the other robots
	 */
	protected ArrayList<Event>	receivedEvts	= null;
	
	/**
	 * Shortcut reference to the logger so that we don't need to keep typing it
	 */
	protected ApplicationLogger	logger			= ApplicationLogger
														.getInstance();
	
	/**
	 * Super constructor for all of the implementing child classes.
	 * 
	 * @param id
	 * @param numRobots
	 * @param barrier
	 */
	public AbstractEventProcessor (int numRobots)
	{
		this.numRobots = numRobots;
		
		receivedEvts = new ArrayList<Event>(numRobots);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see event.EventProcessor#checkEventType(event.Event)
	 */
	@Override
	public abstract boolean checkEventType (Event evt);
	
	/**
	 * Runs the barrier action method because this {@link EventProcessor} has
	 * received enough {@link Event}s. Usually this will dispatch an
	 * {@link Event} to the waiting Threads.
	 */
	public abstract void runBarrierAction ();
	
	/**
	 * Decides whether the {@link AbstractEventProcessor#runBarrierAction()}
	 * method should run after receiving the {@link Event} currently being
	 * processed
	 * 
	 * @return true if the method should run
	 */
	public abstract boolean shouldRunAction ();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mapp.EventProcessor#processEvent(edu.unh.acl.app
	 * .model.event.Event)
	 */
	@Override
	public boolean processEvent (Event evt)
	{
		if (checkEventType(evt))
		{
			// logger.logDebug("STATE  %02d: Received %s event from %02d", id,
			// ProgressionEvent.class.getSimpleName(), evt.getID());
			
			// check that the id is valid
			int id = evt.getID();
			
			if (id < 0 || id >= numRobots) { return true; }
			
			// ignore duplicates
			if (receivedEvts.contains(evt)) { return true; }
			
			// add the event to the List
			receivedEvts.add(evt);
			
			logger.logDebug("STATE  %02d: received %02d Events", id,
					receivedEvts.size());
			
			// check whether we need to trip the barrier
			if (shouldRunAction())
			{
				Collections.sort(receivedEvts);
				
				logger.logDebug("STATE  %02d: Running barrier action", id);
				
				runBarrierAction();
				
				// create a new object so that this thread can no longer write
				// to the old object
				receivedEvts = new ArrayList<Event>(numRobots);
				
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mapp.EventProcessor#getType()
	 */
	@Override
	public abstract String getType ();
	
	/**
	 * Gets the number of {@link Event}s we are waiting for (1 event for each
	 * robot)
	 * 
	 * @return
	 */
	public int getNumberOfRobots ()
	{
		return numRobots;
	}
	
	/**
	 * Gets the {@link ArrayList} of received {@link Event}s
	 * 
	 * @return
	 */
	public ArrayList<Event> getReceivedEvents ()
	{
		return receivedEvts;
	}
}
