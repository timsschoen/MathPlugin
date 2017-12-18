/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathplugin;

import eu.mihosoft.vrl.annotation.ComponentInfo;

/**
 *
 * @author tim
 */
@ComponentInfo(name="Vector", category="Math")
public class Vector {
    public int length;
    public double[] data;
    
    public Vector() {
        this.length = 0;
        this.data = new double[0];
    }
    
    public Vector(Vector A) {
        this.length = A.length;
        this.data = new double[length];
        for(int i = 0; i < length; i++) {
            this.data[i] = A.data[i];
        }
    }
    
    public Vector(int length) {
        this.length = length;
        this.data = new double[length];
    }
    
    public Vector(double[] v){
        this.length = v.length;
        this.data = new double[length];
        for(int i = 0; i < length; i++) {
            this.data[i] = v[i];
        }
    }
    
    public Vector(Double[] v){
        this.length = v.length;
        this.data = new double[length];
        for(int i = 0; i < length; i++) {
            this.data[i] = v[i];
        }
    }
    
    public void subtract(Vector v) {
        if(v.length != length) {
            throw new RuntimeException("vectors dont have the same length");
        }
        
        for(int i = 0; i < length; i++) {
            this.data[i] -= v.data[i];
        }            
    }
    
    public void scale(double factor) {
        for(int i = 0; i < length; i++) {
            this.data[i] *= factor;
        }
    }
    
    public void add(Vector v) {
        if(v.length != length) {
            throw new RuntimeException("vectors dont have the same length");
        }
        
        for(int i = 0; i < length; i++) {
            this.data[i] += v.data[i];
        }
    }
    
    public static Vector scale(Vector A, double factor) {
        Vector C = new Vector(A);
        C.scale(factor);
        return C;
    }
    
    public static Vector add(Vector A, Vector B) {
        Vector C = new Vector(A);
        C.add(B);
        return C;
    }
    
    public static Vector subtract(Vector A, Vector B) {
        Vector C = new Vector(A);
        C.subtract(B);
        return C;
    }
    
    public double GetL2Norm(){
        double norm = 0;
        for(int i = 0; i < length; i++) {
            norm += Math.pow(data[i], 2);
        }
        return Math.sqrt(norm);
    }
    
    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < length; i++) {
            result += "v" + i + " = " + data[i] + ", ";
        }
        return result;
    }
}
