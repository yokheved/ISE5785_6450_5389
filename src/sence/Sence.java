package sence;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Sence {

    String name;
    Color background = new Color(java.awt.Color.BLACK);
    AmbientLight ambientLight = AmbientLight.NONE;
    Geometries geometries = new Geometries();

    public Sence(String name) {
        this.name = name;
    }

    public Sence setBackround(Color background) {
        this.background = background;
        return this;
    }

    public Sence setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Sence setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }



}
