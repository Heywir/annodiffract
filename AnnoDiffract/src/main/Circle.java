package main;

import java.awt.Point;
import java.util.ArrayList;

public class Circle {
	private boolean dr=false;
	
	public ArrayList<Point> ptCircle = new ArrayList<Point>();
	
	public boolean isDr() {
		return dr;
	}
	
	public void setDr(boolean dr) {
		this.dr = dr;
	}
	
	public Circle(){
		
	}
}
