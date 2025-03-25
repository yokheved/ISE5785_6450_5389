package primitives;

import java.util.Objects;

public class Point {
    public final static Point ZERO =  new Point(Double3.ZERO);
    protected final Double3 xyz;
    public Point(Double3 xyz) {

        this.xyz = xyz;

    }
    public Point(double x, double y, double z) {

        this.xyz = new Double3(x,y,z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz) ;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    public Point add(Vector other) {
        return new Point(xyz.add(other.xyz));
    }

    public Vector subtract(Point other) {
        if (this.equals(other)){
            throw new IllegalArgumentException("other point equals self is illegal");
        }
        return new Vector(xyz.subtract(other.xyz));
    }

    public double distanceSquared(Point other) {
       return (this.xyz.d1()-other.xyz.d1())*(this.xyz.d1()-other.xyz.d1()) +
               (this.xyz.d2()-other.xyz.d2())*(this.xyz.d2()-other.xyz.d2()) +
                (this.xyz.d3()-other.xyz.d3())*(this.xyz.d3()-other.xyz.d3());
    }

    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }
}
