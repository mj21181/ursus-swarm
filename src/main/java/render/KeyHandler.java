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
package render;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
	public boolean	left	= false;
	public boolean	right	= false;
	public boolean	up		= false;
	public boolean	down	= false;
	public boolean	q		= false;
	public boolean  e		= false;
	public boolean	s		= false;
	
	@Override
	public void keyPressed (KeyEvent arg0)
	{
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
		{
			left = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			right = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
		{
			down = true;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
		{
			up = true;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_Q)
		{
			q = true;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_E)
		{
			e = true;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_S)
		{
			s = true;
		}
	}
	
	@Override
	public void keyReleased (KeyEvent arg0)
	{
		
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
		{
			left = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
		{
			down = false;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
		{
			up = false;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_Q)
		{
			q = false;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_E)
		{
			e = false;
		}
		if(arg0.getKeyCode() == KeyEvent.VK_S)
		{
			s = false;
		}
	}
	
	@Override
	public void keyTyped (KeyEvent arg0)
	{
		
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
	}
	
}
