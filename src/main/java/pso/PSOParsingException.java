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
package pso;

/**
 * Used to indicate if the parser discovers PSO log file is malformed. 
 * 
 * @author Mike Johnson
 *
 */
public class PSOParsingException extends Exception
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4638360930128891756L;

	public PSOParsingException (String msg)
	{
		super(msg);
	}
	
}
