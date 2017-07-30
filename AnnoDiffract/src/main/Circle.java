package main;

import java.awt.Point;
import java.util.ArrayList;

public class Circle {
	/*Permet de savoir si il faut dessiner le cercle*/
	private boolean dr=false;
	
	/*Il contiendra les 3 points du cercle*/
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
