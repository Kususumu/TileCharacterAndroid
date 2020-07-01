package com.xmu.arac.draw;

public class Vector3
{
    public double x;
    public double y;
    public double z;

    public Vector3(){}

    public Vector3(Vertex3D v0, Vertex3D v1)
    {
        x = v1.x - v0.x;
        y = v1.y - v0.y;
        z = v1.z - v0.z;
    }

    public Vector3(double _x, double _y, double _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }

    Vector3(Vector3 v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }


    public Vector3 copyVector3()
    {
        return new Vector3(x, y, z);
    }

    public Vertex3D vertex()
    {
        return new Vertex3D(x, y, z);
    }

    public String toString()
    {
        return (new StringBuilder("(")).append(x).append(",").append(y)
                .append(",").append(z).append(")").toString();
    }

    public Vector3 getNormalized()
    {
        double l = length();
        return new Vector3(x / l, y / l, z / l);
    }

    public void normalize()
    {
        double l = length();
        if (l == 0.0D)
        {
            return;
        }
        else
        {
            x /= l;
            y /= l;
            z /= l;
            return;
        }
    }

    public void normalizeSelf()
    {
        normalize();
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 multiple(double m)
    {
        return new Vector3(x * m, y * m, z * m);
    }

    public Vector3 add(Vector3 v)
    {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 subtract(Vector3 v)
    {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public void multiply(double m)
    {
        x *= m;
        y *= m;
        z *= m;
    }

    public static Vector3 multiply(Vector3 v, double m)
    {
        return new Vector3(v.x * m, v.y * m, v.z * m);
    }

    public static Vector3 add(Vector3 u, Vector3 v)
    {
        return new Vector3(u.x + v.x, u.y + v.y, u.z + v.z);
    }

    public static Vector3 subtract(Vector3 u, Vector3 v)
    {
        return new Vector3(u.x - v.x, u.y - v.y, u.z - v.z);
    }

    public void multipleSelf(double m)
    {
        x *= m;
        y *= m;
        z *= m;
    }

    public void addSelf(Vector3 v)
    {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public static Vector3 add(Vector3 u, Vector3 v, Vector3 w)
    {
        return new Vector3(u.x + v.x + w.x, u.y + v.y + w.y, u.z + v.z + w.z);
    }

    public static double dotProduct(Vector3 u, Vector3 v)
    {
        return u.x * v.x + u.y * v.y + u.z * v.z;
    }

    public static Vector3 crossProduct(Vector3 u, Vector3 v)
    {
        return u.crossProduct(v);
    }

    public Vector3 crossProduct(Vector3 v)
    {
        double _x = y * v.z - z * v.y;
        double _y = z * v.x - x * v.z;
        double _z = x * v.y - y * v.x;
        return new Vector3(_x, _y, _z);
    }


    public static double cos(Vector3 u, Vector3 v)
    {
        double length = Math.sqrt((u.x * u.x + u.y * u.y + u.z * u.z)
                * (v.x * v.x + v.y * v.y + v.z * v.z));
        if (length > 0.0D)
            return dotProduct(u, v) / length;
        else
            return 0.0D;
    }

    public static double sin(Vector3 u, Vector3 v)
    {
        double length = Math.sqrt((u.x * u.x + u.y * u.y + u.z * u.z)
                * (v.x * v.x + v.y * v.y + v.z * v.z));
        if (length > 0.0D)
            return crossProduct(u, v).length() / length;
        else
            return 0.0D;
    }

    public double dotProduct(Vector3 v)
    {
        return x * v.x + y * v.y + z * v.z;
    }

    public double getCos(Vector3 v)
    {
        return cos(v);
    }

    public double getSin(Vector3 v)
    {
        return sin(v);
    }

    public double cos(Vector3 v)
    {
        double length = Math.sqrt((x * x + y * y + z * z)
                * (v.x * v.x + v.y * v.y + v.z * v.z));
        if (length > 0.0D)
            return dotProduct(v) / length;
        else
            return 0.0D;
    }


    public double sin(Vector3 v)
    {
        double length = Math.sqrt((x * x + y * y + z * z)
                * (v.x * v.x + v.y * v.y + v.z * v.z));
        if (length > 0.0D)
            return crossProduct(v).length() / length;
        else
            return 0.0D;
    }

    public boolean parallel(Vector3 v)
    {
        double sine = sin(v);
        return sine < 0.01D && sine > -0.01D;
    }

    public double getRelativeAngle(Vector3 v)
    {
        double cosine = cos(v);
        if (cosine <= -1D)
            return 3.1415926535897931D;
        if (cosine >= 1.0D)
            return 0.0D;
        else
            return Math.acos(cosine);
    }

    public static double getRelativeAngle(Vector3 u, Vector3 v)
    {
        double cosine = u.cos(v);
        if (cosine <= -1D)
            return 3.1415926535897931D;
        if (cosine >= 1.0D)
            return 0.0D;
        else
            return Math.acos(cosine);
    }

    public Vector3 reverse()
    {
        return new Vector3(-x, -y, -z);
    }

    static Vector3 negate(Vector3 v)
    {
        return new Vector3(-v.x, -v.y, -v.z);
    }

    public static Vector3 average(Vector3 a, Vector3 b)
    {
        return new Vector3((a.x + b.x) / 2D, (a.y + b.y) / 2D, (a.z + b.z) / 2D);
    }

    public static double distance(double x1, double y1, double z1, double x2,
                                  double y2, double z2)
    {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)
                + (z2 - z1) * (z2 - z1));
    }

    public static double distance(Vertex3D start, Vertex3D end)
    {
        return distance(start.x, start.y, start.z, end.x, end.y, end.z);
    }

    public double getAngle(Vector3 v, Vector3 normal)
    {
        double cos = getCos(v);
        double sin = getSin(v);
        if (crossProduct(v).dotProduct(normal) < 0.0D)
            sin *= -1D;
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

    public double getLength()
    {
        return getLength(x, y, z);
    }

    public static double getLength(double x, double y, double z)
    {
        double lengthSquared = getLengthSquared(x, y, z);
        if (lengthSquared == 1.0D)
            return 1.0D;
        else
            return Math.sqrt(lengthSquared);
    }

    public static double getLengthSquared(double x, double y, double z)
    {
        return x * x + y * y + z * z;
    }

    public static Vector3 normalizeV(Vector3 v)
    {
        Vector3 nv = new Vector3(v.x, v.y, v.z);
        nv.normalize();
        return nv;
    }
}
