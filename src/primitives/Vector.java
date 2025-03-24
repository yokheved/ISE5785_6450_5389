package primitives;

public class Vector extends Point{
    public Vector(Double3 xyz) {
        super(xyz);
    }
    public Vector(double x, double y, double z){
        super(x,y,z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) &&
         super.equals(other);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "xyz=" + xyz +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public double lengthSquared(){
        return this.distanceSquared(Point.ZERO);
    }

    public double length(){
        return Math.sqrt(lengthSquared());
    }

    public Vector add(Vector other){
        return new Vector(this.xyz.add(other.xyz));
    }

    public Vector scale(double scale){
        return new Vector(this.xyz.scale(scale));
    }
    public double dotProduct(Vector other){
        return this.xyz.d1()*other.xyz.d1() +
                this.xyz.d2()*this.xyz.d2() +
                this.xyz.d3()*this.xyz.d3();
    }
    public Vector crossProduct(Vector other){
        return new Vector(
                this.xyz.d2() * other.xyz.d3() - this.xyz.d3() * other.xyz.d2(),
                this.xyz.d3() * other.xyz.d1() - this.xyz.d1() * other.xyz.d3(),
                this.xyz.d1() * other.xyz.d2() - this.xyz.d2() * other.xyz.d1()
        );
    }
}
