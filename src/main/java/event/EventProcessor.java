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
package event;

import java.util.concurrent.CyclicBarrier;

/**
 * 
 * @author Mike Johnson
 * 
 */
public interface EventProcessor
{
	/**
	 * Processes the received {@link Event} and determines whether the thread
	 * should continue waiting for additional {@link Event}s or not.
	 * 
	 * @param evt The received Event
	 * @return true if the thread should continue waiting
	 */
	public boolean processEvent (Event evt);
	
	/**
	 * Returns a {@link String} containing the name of the class of
	 * {@link EventProcessor} in use.
	 * 
	 * @return The Simple Name of the class
	 */
	public String getType ();
	
	/**
	 * Returns the {@link CyclicBarrier} that the waiting thread should call
	 * await() on once {@link EventProcessor#processEvent(Event)} returns false
	 * 
	 * @return {@link CyclicBarrier}
	 */
//	public CyclicBarrier getBarrier ();
	
	/**
	 * Checks whether the {@link Event} we are receiving is the correct type.
	 * 
	 * @param evt
	 * @return
	 */
	public boolean checkEventType (Event evt);
}
