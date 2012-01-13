package com.ryan.squareballs;

import android.graphics.RectF;

public class Vector2f {
  
	public float x;
	public float y;
	
	// Constructors
    public Vector2f() {
    	x = y = 0F;
    }
    public Vector2f( float p ) {
    	x = y = p;
    }
    public Vector2f( Vector2f v ) {
    	x = v.x;
    	y = v.y;
    }
    public Vector2f( float x, float y ) {
    	this.x = x;
    	this.y = y;
    }
    
    public String toString() {
    	return "x:"+x+" -- y:"+y;
    }

    // Magnitude
    public float getMagnitude() {
    	return (float) Math.sqrt(getSquaredMagnitude());
    }
    public float getSquaredMagnitude() {
    	return x*x + y*y;
    }
    
    // Distance between two vectors
    public float getDistance( Vector2f p2 ) {
    	return (float) Math.sqrt(getSquaredDistance(p2));
    }
    public static float getDistance( Vector2f p1, Vector2f p2 ) {
    	return (float) Math.sqrt(getSquaredDistance(p1, p2));
    }
    public float getSquaredDistance( Vector2f p2 ) {
    	return Vector2f.subtract(p2, this).getSquaredMagnitude();
    }
    public static float getSquaredDistance( Vector2f p1, Vector2f p2 ) {
    	return Vector2f.subtract(p2,  p1).getSquaredMagnitude();
    }

    // Normals
    public Vector2f unit() {
    	float mag = getMagnitude();
    	this.divide(mag);
    	return this;
    }
    public Vector2f getUnit() {
    	Vector2f unit = new Vector2f(this);
    	float mag = unit.getMagnitude();
    	return unit.divide(mag);
    }
    public static Vector2f normal( Vector2f v1, Vector2f v2 ) {
    	float dx = v2.x - v1.x;
    	float dy = v2.y - v1.y;	  
    	return new Vector2f( -dy, dx ).unit();
    }

    // Dot
    public float dot( Vector2f other ) {
    	return (x * other.x + y * other.y);
    }
    
    // Cross
    public float cross( Vector2f other ) {
    	return (x * other.y - y * other.x);
    }

    // Reflect
    public static Vector2f reflect( Vector2f a, Vector2f b ) {
		float dotProduct = -a.x*b.x - a.y*b.y;
		return new Vector2f(a.x + 2 * b.x * dotProduct,
							a.y + 2 * b.y * dotProduct);
    }
    public Vector2f reflect( Vector2f other ) {
    	float dotProduct = -x*other.x - y*other.y;
		x = x + 2 * other.x * dotProduct;
		y = y + 2 * other.y * dotProduct;
		return this;
    }

    // Angle Radians
    public float getAngleRadians() {
    	float angle = (float) Math.atan2( y, x );

    	if ( angle < 0 ) {
    		angle += 2 * Math.PI;
    	}

    	return angle;
    }
    
    // Angle Degrees
    public float getAngleDegrees() {
    	return getAngleRadians() * 57.29578F;
    }

    // Set
    public void set( Vector2f rhs ) {
    	this.x = rhs.x;
    	this.y = rhs.y;
    }
    public void set( float val ) {
    	this.x = val;
    	this.y = val;
    }
    public void set( float x, float y ) {
    	this.x = x;
    	this.y = y;
    }
    
    // Equals
    public boolean equals( Vector2f rhs ) {
    	return this.x == rhs.x && this.y == rhs.y;
    }
    
    // Not Equals
    public boolean notEquals( Vector2f rhs ) {
    	return ! this.equals(rhs);
    }

    // Basic Math
    public Vector2f add( Vector2f rhs ) {
    	this.x += rhs.x;
    	this.y += rhs.y;
    	return this;
    }
    public Vector2f subtract( Vector2f rhs ) {
    	this.x -= rhs.x;
    	this.y -= rhs.y;
    	return this;
    }
    public Vector2f multiply( Vector2f rhs ) {
    	this.x *= rhs.x;
    	this.y *= rhs.y;
    	return this;
    }
    public Vector2f multiply( float rhs ) {
    	this.x *= rhs;
    	this.y *= rhs;
    	return this;
    }
    public Vector2f divide( Vector2f rhs ) {
    	this.x /= rhs.x;
    	this.y /= rhs.y;
    	return this;
    }
    public Vector2f divide( float rhs ) {
    	this.x /= rhs;
    	this.y /= rhs;
    	return this;
    }
    
    // Static Math methods
    public static Vector2f add( Vector2f a, Vector2f b ) {
    	return new Vector2f(a).add(b);
    }
    public static Vector2f subtract( Vector2f a, Vector2f b ) {
    	return new Vector2f(a).subtract(b);
    }
    public static Vector2f multiply( Vector2f a, Vector2f b ) {
    	return new Vector2f(a).multiply(b);
    }
    public static Vector2f multiply( Vector2f a, float val ) {
    	return new Vector2f(a).multiply(val);
    }
    public static Vector2f divide( Vector2f a, Vector2f b ) {
    	return new Vector2f(a).divide(b);
    }
    public static Vector2f divide( Vector2f a, float val ) {
    	return new Vector2f(a).divide(val);
    }
    
    ////////////////////////////////////////////////
    // 0 == no intersection
    //
    //         2
    //      ________
    //     |        |
    //  1  |        |   3
    //     |        |
    //     |________|
    //       
    //          4
    /////////////////////////////////////////////////
    public static int rectLineIntersection(RectF rect, Vector2f p1, Vector2f p2) {
    	Vector2f result;
    	Vector2f p3 = new Vector2f();
    	Vector2f p4 = new Vector2f();
    	
    	// Left line
    	p3.set(rect.left, rect.bottom);
    	p4.set(rect.left, rect.top);
    	result = lineLineIntersection(p1, p2, p3, p4);
    	if (null != result) return 1;
    	
    	// Top line
    	p3.set(rect.left, rect.top);
    	p4.set(rect.right, rect.top);
    	result = lineLineIntersection(p1, p2, p3, p4);
    	if (null != result) return 2;
    	
    	// Right line
    	p3.set(rect.right, rect.top);
    	p4.set(rect.right, rect.bottom);
    	result = lineLineIntersection(p1, p2, p3, p4);
    	if (null != result) return 3;
    	
    	// Bottom line
    	p3.set(rect.left, rect.bottom);
    	p4.set(rect.right, rect.bottom);
    	result = lineLineIntersection(p1, p2, p3, p4);
    	if (null != result) return 4;
    	
    	return 0;
    }
    
    public static Vector2f lineLineIntersection(Vector2f p1, Vector2f p2,  
    		Vector2f p3, Vector2f p4) {  
	  float xD1,yD1,xD2,yD2,xD3,yD3;  
	  float dot,deg,len1,len2;  
	  float segmentLen1,segmentLen2;  
	  float ua,ub,div;  
	  
	  // calculate differences  
	  xD1=p2.x-p1.x;  
	  xD2=p4.x-p3.x;  
	  yD1=p2.y-p1.y;  
	  yD2=p4.y-p3.y;  
	  xD3=p1.x-p3.x;  
	  yD3=p1.y-p3.y;    
	  
	  // calculate the lengths of the two lines  
	  len1=(float) Math.sqrt(xD1*xD1+yD1*yD1);  
	  len2=(float) Math.sqrt(xD2*xD2+yD2*yD2);  
	  
	  // calculate angle between the two lines.  
	  dot=(xD1*xD2+yD1*yD2); // dot product  
	  deg=dot/(len1*len2);  
	  
	  // if abs(angle)==1 then the lines are parallell,  
	  // so no intersection is possible  
	  if(Math.abs(deg)==1) return null;  
	  
	  // find intersection Pt between two lines  
	  Vector2f pt = new Vector2f(0,0);  
	  div=yD2*xD1-xD2*yD1;  
	  ua=(xD2*yD3-yD2*xD3)/div;  
	  ub=(xD1*yD3-yD1*xD3)/div;  
	  pt.x=p1.x+ua*xD1;  
	  pt.y=p1.y+ua*yD1;  
	  
	  // calculate the combined length of the two segments  
	  // between Pt-p1 and Pt-p2  
	  xD1=pt.x-p1.x;  
	  xD2=pt.x-p2.x;  
	  yD1=pt.y-p1.y;  
	  yD2=pt.y-p2.y;  
	  segmentLen1=(float) (Math.sqrt(xD1*xD1+yD1*yD1)+Math.sqrt(xD2*xD2+yD2*yD2));  
	  
	  // calculate the combined length of the two segments  
	  // between Pt-p3 and Pt-p4  
	  xD1=pt.x-p3.x;  
	  xD2=pt.x-p4.x;  
	  yD1=pt.y-p3.y;  
	  yD2=pt.y-p4.y;  
	  segmentLen2=(float) (Math.sqrt(xD1*xD1+yD1*yD1)+Math.sqrt(xD2*xD2+yD2*yD2));  
	  
	  // if the lengths of both sets of segments are the same as  
	  // the lenghts of the two lines the point is actually  
	  // on the line segment.  
	  
	  // if the point isn't on the line, return null  
	  if(Math.abs(len1-segmentLen1)>0.01 || Math.abs(len2-segmentLen2)>0.01)  
	    return null;  
	  
	  // return the valid intersection  
	  return pt;  
	}
}