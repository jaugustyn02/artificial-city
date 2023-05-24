package org.example;

import org.example.cells.CellType;
import org.example.cells.Entrance;
import org.example.cells.Exit;
import org.example.iterable.CarEntrance;
import org.example.iterable.CarExit;
import org.example.iterable.IterablePoint;
import org.example.moving.BoardDirection;
import org.example.moving.Direction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	@Serial
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private MovingObject[][] movingObjects;
	private IterablePoint[][] iterablePoints;
	final private int size = 10;
	public CellType editType = CellType.NOT_SPECIFIED;
	public FileHandler fileHandler = new FileHandler(this);

	public BoardDirection editDirection = BoardDirection.RIGHT;

	private int length;
	private int height;

	public Board(int length, int height) {
		initialize(length, height);
		this.length = length;
		this.height = height;
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	public void iteration() {
		for(IterablePoint point : getIterablePoints1d()){
			if (point instanceof Exit)
				point.iterate();
		}

		for(MovingObject obj : getMovingObjects1d()){
			obj.iterate(points, movingObjects);
		}
		MovingObject[][] newMovingObjects = new MovingObject[length][height];
		for(MovingObject obj : getMovingObjects1d()){
			obj.move();
			newMovingObjects[obj.getX()][obj.getY()] = obj;
		}
		movingObjects = newMovingObjects;

		for(IterablePoint point : getIterablePoints1d()){
			if (point instanceof Entrance)
				point.iterate();
		}

		this.repaint();
	}

	public void clear() {
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[0].length; ++y) {
				points[x][y] = CellType.NOT_SPECIFIED.getObject();
			}
		}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Point[length][height];
		movingObjects = new MovingObject[length][height];
		iterablePoints = new IterablePoint[length][height];

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[0].length; ++y) {
				points[x][y] = new Point();
			}
	}

	private void initializeNewPoints(int length, int height) {
		Point[][] new_points = new Point[length][height];
		this.length = length;
		this.height = height;

		for (int x = 0; x < length; ++x)
			for (int y = 0; y < height; ++y) {
				if (x < points.length && y < points[0].length){
					new_points[x][y] = points[x][y];
				}
				else{
					new_points[x][y] = new Point();
				}
			}

		points = new_points;
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		g.setColor(new Color(201, 201, 201));
		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				Point point = points[x][y];
				g.setColor(point.type.getColor());
				g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
			}
		}

		for(MovingObject obj : getMovingObjects1d()){
			g.setColor(obj.type.getColor());
			g.fillRect((obj.x * size) + 1, (obj.y * size) + 1, (size - 1), (size - 1));
		}
	}

	public void mouseClicked(MouseEvent e){
		int x = e.getX() / size;
		int y = e.getY() / size;
		setCell(x, y);
	}

	public void setCell(int x, int y) {

		if(editType == CellType.CAR){
			MovingObject obj = editType.getMovingObject(editDirection);
			obj.setPosition(x,y);
			movingObjects[x][y] = obj;
		} else if (editType == CellType.PEDESTRIAN) {
			MovingObject obj = editType.getMovingObject();
			obj.setPosition(x,y);
			movingObjects[x][y] = obj;
		} else {
			points[x][y] = editType.getObject();
		}

		if (points[x][y] instanceof IterablePoint point){
			point.setPosition(x, y);
			point.setBoard(this);
			iterablePoints[x][y] = point;
		}

		this.repaint();
	}

	public void componentResized(ComponentEvent e) {
		int length = (this.getWidth() / size) + 1;
		int height = (this.getHeight() / size) + 1;
//		initialize(dlugosc, wysokosc);
		initializeNewPoints(length, height);
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		setCell(x, y);
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void save(String filePath){
		fileHandler.saveMap(points, filePath);
	}

	public void load(String fileName){
		int[] size = new int[2];
		size[0] = points.length;
		size[1] = points[0].length;
		fileHandler.loadMap(size, fileName);
		this.repaint();
	}
	private MovingObject[] getMovingObjects1d(){
		return Arrays.stream(movingObjects).flatMap(Stream::of).filter(Objects::nonNull).toArray(MovingObject[]::new);
	}

	private IterablePoint[] getIterablePoints1d(){
		return Arrays.stream(iterablePoints).flatMap(Stream::of).filter(Objects::nonNull).toArray(IterablePoint[]::new);
	}

	public Point getPointAt(int x, int y){
		return points[x][y];
	}

	public MovingObject getMovingObjectAt(int x, int y){
		return movingObjects[x][y];
	}

	public void removeMovingObjectsAt(int x, int y){
		movingObjects[x][y] = null;
	}

}
