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
package event.events;

import java.util.Arrays;

import pso.async.interfaces.FeedbackControlSystem;
import event.Event;
import event.eventprocessors.InitializationEventProcessor;

/**
 * This event is broadcast by each robot on the network to inform the other
 * robots of its initial state. The {@link InitializationEventProcessor} on each
 * of the other robots will wait for all of these events to arrive. When they do, it the {@link InitializationEventProcessor} records the state before allowing the robot and 
 * 
 * @author Mike Johnson
 * 
 */
public class InitializationEvent extends Event
{
	/**
	 * The initial state for the {@link FeedbackControlSystem} dynamics
	 */
	protected int[] initial = null;
	
	/**
	 * @param id
	 */
	public InitializationEvent (int id, int[] state)
	{
		super(id);
		
		initial = state;
	}
	
	/**
	 * @return the initial
	 */
	public int[] getInitial ()
	{
		return initial;
	}
	
	/**
	 * @param initial the initial to set
	 */
	public void setInitial (int[] initial)
	{
		this.initial = initial;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(initial);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		InitializationEvent other = (InitializationEvent) obj;
		if (!Arrays.equals(initial, other.initial)) return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format("InitializationEvent [initial=%s]",
				Arrays.toString(initial));
	}
	
}
