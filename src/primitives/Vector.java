package primitives;

import static java.lang.System.out;

public class Vector extends Point{

    public Vector(Double3 xyz) {
        super(xyz);

        if( this.equals(Point.ZERO)){
            throw new IllegalArgumentException(" vector zero is illegal");
        }
    }


    public Vector(double x, double y, double z){
        super(x,y,z);
        if(this.equals(Point.ZERO)){
            throw new IllegalArgumentException(" vector zero is illegal");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) &&
                this.xyz.equals(other.xyz);
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
        if(this.equals(other.scale(-1))) {
            throw new IllegalArgumentException("other vector equals -self is illegal");
        }

        return new Vector(this.xyz.add(other.xyz));
    }

    public Vector scale(double scale){
        return new Vector(this.xyz.scale(scale));
    }
    public double dotProduct(Vector other){
        return this.xyz.d1()*other.xyz.d1() +
                this.xyz.d2()*other.xyz.d2() +
                this.xyz.d3()*other.xyz.d3();
    }
    public Vector crossProduct(Vector other){
        return new Vector(
                this.xyz.d2() * other.xyz.d3() - this.xyz.d3() * other.xyz.d2(),
                this.xyz.d3() * other.xyz.d1() - this.xyz.d1() * other.xyz.d3(),
                this.xyz.d1() * other.xyz.d2() - this.xyz.d2() * other.xyz.d1()
        );
    }

    public Vector normalize(){
        double len = this.length();
        return new Vector(this.xyz.d1() / len, this.xyz.d2() / len, this.xyz.d3() / len);
    }

}
