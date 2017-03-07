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
package event;

import java.util.Observer;

/**
 * This interface extends the {@link Observer} interface to facilitate easy
 * reception of {@link Event}s from {@link EventDispatcher}s
 * 
 * @author Mike Johnson
 * 
 */
public interface EventListener extends Observer
{
	/**
	 * Receives an {@link Event} from an {@link EventDispatcher}. The class
	 * which implements this method must be added as an {@link Observer} of the
	 * {@link EventDispatcher}
	 * 
	 * @param evt The {@link Event} being received
	 */
	public void receiveEvent (Event evt);
}
