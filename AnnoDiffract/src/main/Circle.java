package main;

import java.awt.Point;
import java.util.ArrayList;

public class Circle {
	private boolean dr=false;
	
	public final ArrayList<Point> ptCircle = new ArrayList<>();
	
	public boolean isDr() {
		return dr;
	}
	
	public void setDr() {
		this.dr = true;
	}
	
	public Circle(){
		
	}
}
