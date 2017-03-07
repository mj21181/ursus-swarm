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

import java.util.Observable;
import java.util.Observer;

/**
 * This class extends the {@link Observable} class to facilitate easy
 * dispatching of {@link Event}s to {@link EventListener}s
 * 
 * @author Mike Johnson
 * 
 */
public class EventDispatcher extends Observable
{
	/**
	 * Dispatches a given {@link Event} to {@link EventListener} that have been
	 * added as {@link Observer}s
	 * 
	 * @param evt The {@link Event} to dispatch
	 */
	public void dispatchEvent (Event evt)
	{
		setChanged();
		notifyObservers(evt);
	}
}
