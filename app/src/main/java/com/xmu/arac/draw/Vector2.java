package com.xmu.arac.draw;
import android.graphics.Point;

public class Vector2
{

	public double x;
	public double y;

	public Vector2(Point start, Point end)
	{
		x = end.x - start.x;
		y = end.y - start.y;
	}

	public Vector2(Vector2 start, Vector2 end)
	{
		x = end.x - start.x;
		y = end.y - start.y;
	}

	public Vector2(Point p)
	{
		x = p.x;
		y = p.y;
	}

	public Vector2()
	{
	}

	public Vector2(double _x, double _y)
	{
		x = _x;
		y = _y;
	}

	public Vector2 flipYAxis()
	{
		return new Vector2(-x, y);
	}

	public Vector2 rotate(double degree)
	{
		if (degree == 90D)
			return new Vector2(-y, x);
		if (degree == 180D)
			return new Vector2(-x, -y);
		if (degree == 270D)
		{
			return new Vector2(y, -x);
		}
		else
		{
			double radian = (degree * 3.1415926535897931D) / 180D;
			double cos = Math.cos(radian);
			double sin = Math.sin(radian);
			return new Vector2(x * cos - y * sin, x * sin + y * cos);
		}
	}

	public Vector2 add(Vector2 v)
	{
		return new Vector2(x + v.x, y + v.y);
	}

	public Vector2 normalize()
	{
		double length = length();
		if (length == 0.0D)
			length = 1.0D;
		return new Vector2(x / length, y / length);
	}

	public Vector2 getNormalized()
	{
		double length = length();
		if (length == 0.0D)
			length = 1.0D;
		return new Vector2(x / length, y / length);
	}

	public Vector2 scale(double scale)
	{
		return new Vector2(x * scale, y * scale);
	}

	public Vector2 multiple(double m)
	{
		return new Vector2(x * m, y * m);
	}

	public void multipleSelf(double m)
	{
		x *= m;
		y *= m;
	}

	public void addSelf(Vector2 v)
	{
		x += v.x;
		y += v.y;
	}

	public void rotate90Self()
	{
		double xx = x;
		double yy = y;
		x = -y;
		y = x;
	}

	public Vector2 rotate90()
	{
		return new Vector2(-y, x);
	}

	public void normalize_self()
	{
		double length = length();
		if (length == 0.0D)
			length = 1.0D;
		x /= length;
		y /= length;
	}

	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}

	public double innerProduct(Vector2 v)
	{
		return x * v.x + y * v.y;
	}

	public double outerProduct(Vector2 v)
	{
		return x * v.y - y * v.x;
	}

	public double dotProduct(Vector2 v)
	{
		return x * v.x + y * v.y;
	}

	public double crossProduct(Vector2 v)
	{
		return x * v.y - y * v.x;
	}

	public static double dotProduct(Vector2 u, Vector2 v)
	{
		return u.x * v.x + u.y * v.y;
	}

	public static double crossProduct(Vector2 u, Vector2 v)
	{
		return u.x * v.y - u.y * v.x;
	}

	public double getCos(Vector2 v)
	{
		double length = Math.sqrt((x * x + y * y) * (v.x * v.x + v.y * v.y));
		if (length > 0.0D)
			return innerProduct(v) / length;
		else
			return 0.0D;
	}

	public double getSin(Vector2 v)
	{
		double length = Math.sqrt((x * x + y * y) * (v.x * v.x + v.y * v.y));
		if (length > 0.0D)
			return crossProduct(v) / length;
		else
			return 0.0D;
	}

	public double sin(Vector2 v)
	{
		return getSin(v);
	}

	public double cos(Vector2 v)
	{
		return getCos(v);
	}

	public static double cos(Vector2 u, Vector2 v)
	{
		double length = Math.sqrt((u.x * u.x + u.y * u.y)
				* (v.x * v.x + v.y * v.y));
		if (length > 0.0D)
			return dotProduct(u, v) / length;
		else
			return 0.0D;
	}

	public static double sin(Vector2 u, Vector2 v)
	{
		double length = Math.sqrt((u.x * u.x + u.y * u.y)
				* (v.x * v.x + v.y * v.y));
		if (length > 0.0D)
			return crossProduct(u, v) / length;
		else
			return 0.0D;
	}

	public double getRelativeAngle(Vector2 v)
	{
		double cosine = cos(v);
		if (cosine <= -1D)
			return 3.1415926535897931D;
		if (cosine >= 1.0D)
			return 0.0D;
		else
			return Math.acos(cos(v));
	}

	public double getAngle(Vector2 v)
	{
		double cos = getCos(v);
		double sin = getSin(v);
		if (cos == 0.0D)
			return sin <= 0.0D ? 270D : 90D;
		if (sin == 0.0D)
			return cos <= 0.0D ? 180D : 0.0D;
		double angle = (180D * Math.atan(sin / cos)) / 3.1415926535897931D;
		if (cos < 0.0D)
			angle += 180D;
		if (angle < 0.0D)
			angle += 360D;
		return angle;
	}

	public static double getAngle360(Vector2 u, Vector2 v)
	{
		double cos = cos(u, v);
		double sin = sin(u, v);
		if (cos == 0.0D)
			return sin <= 0.0D ? 270D : 90D;
		if (sin == 0.0D)
			return cos <= 0.0D ? 180D : 0.0D;
		double angle = (180D * Math.atan(sin / cos)) / 3.1415926535897931D;
		if (cos < 0.0D)
			angle += 180D;
		if (angle < 0.0D)
			angle += 360D;
		return angle;
	}

	public static double distance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public static double distance(Point start, Point end)
	{
		return distance(start.x, start.y, end.x, end.y);
	}

	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public static Vector2 interporate(Point start, Point end, double t)
	{
		return new Vector2((double) start.x * (1.0D - t) + (double) end.x * t,
				(double) start.y * (1.0D - t) + (double) end.y * t);
	}

	public Point point()
	{
		return new Point((int) x, (int) y);
	}

	public static double distance(Vertex2D start, Vertex2D end)
	{
		return distance(start.x, start.y, end.x, end.y);
	}

	public Vertex2D vertex2D()
	{
		return new Vertex2D(x, y);
	}

	public static Vector2 add(Vector2 u, Vector2 v)
	{
		return new Vector2(u.x + v.x, u.y + v.y);
	}

	public static Vector2 subtract(Vector2 u, Vector2 v)
	{
		return new Vector2(u.x - v.x, u.y - v.y);
	}

	public void subtract(Vector2 v)
	{
		x -= v.x;
		y -= v.y;
	}

	public static Vector2 multiply(Vector2 v, double b)
	{
		return new Vector2(v.x * b, v.y * b);
	}
}
