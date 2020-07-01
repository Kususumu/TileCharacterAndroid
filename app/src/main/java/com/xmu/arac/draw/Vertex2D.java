package com.xmu.arac.draw;

import android.graphics.Point;

public class Vertex2D extends Vector2 implements Cloneable
{

	public int index;
	public boolean fixed;
	boolean corner;
	public double deep;

	public Vertex2D()
	{
	}

	public Vertex2D(double _x, double _y)
	{
		x = _x;
		y = _y;
	}

	public Vertex2D(Vertex2D v)
	{
		x = v.x;
		y = v.y;
	}

	public Vertex2D(Point p, int i)
	{
		x = p.x;
		y = p.y;
		index = i;
	}

	public Vertex2D(Point p)
	{
		x = p.x;
		y = p.y;
	}

	Vertex2D(Vector2 p)
	{
		x = p.x;
		y = p.y;
	}

	public String toString()
	{
		return (new StringBuilder("(")).append(x).append(",").append(y)
				.append(")").toString();
	}

	public Vertex2D copy()
	{
		Vertex2D v = new Vertex2D(x, y);
		return v;
	}

	public boolean samePosition(Vertex2D v)
	{
		return x == v.x && y == v.y;
	}

	public static Vertex2D midPoint(Vector2 a, Vector2 b)
	{
		return new Vertex2D((a.x + b.x) / 2D, (a.y + b.y) / 2D);
	}

	public Vertex2D translate(Vector2 v)
	{
		return new Vertex2D(x + v.x, y + v.y);
	}

	public void warp(Vertex2D v)
	{
		x = v.x;
		y = v.y;
	}

	public double distance(Vertex2D node)
	{
		return Math.sqrt((node.x - x) * (node.x - x) + (node.y - y)
				* (node.y - y));
	}

	public static double distance(Vertex2D n1, Vertex2D n2)
	{
		return Math.sqrt((n1.x - n2.x) * (n1.x - n2.x) + (n1.y - n2.y)
				* (n1.y - n2.y));
	}

	public static double distance(Vertex2D n1, Point n2)
	{
		return Math.sqrt((n1.x - (double) n2.x) * (n1.x - (double) n2.x)
				+ (n1.y - (double) n2.y) * (n1.y - (double) n2.y));
	}
	
	public static Vertex2D interporate(Vertex2D start, Vertex2D end, double t)
	{
		return new Vertex2D(start.x * (1.0D - t) + end.x * t, start.y
				* (1.0D - t) + end.y * t);
	}

	public static Vertex2D interpolate(Vertex2D start, Vertex2D end, double t)
	{
		return new Vertex2D(start.x * (1.0D - t) + end.x * t, start.y
				* (1.0D - t) + end.y * t);
	}
	
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

}
