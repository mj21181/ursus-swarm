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
package path.elements;

import path.elements.vertices.ConstraintTreeNode;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This class is used to indicate the wrong child class of {@link GraphVertex}
 * was used in an operation. For example if a method expected
 * {@link LocationVertex} and was passed a {@link ConstraintTreeNode}
 * 
 * @author Mike Johnson
 *
 */
public class VertexTypeException extends Exception
{
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 1870942685915372439L;
	
	public VertexTypeException (String str)
	{
		super(str);
	}
}
