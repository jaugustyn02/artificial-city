package org.example;

import org.example.cells.CellType;

public class Point {
	public CellType type;

	public Point(){
		this.type = CellType.NOT_SPECIFIED;
	}
	public Point(CellType type) {
		this.type = type;
	}
	public void clicked() {
	}
	
	public void clear() {
	}



}