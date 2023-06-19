package org.example;

import org.example.cells.*;
import org.example.helpers.Vector2D;
import org.example.iterable.DrivingPathChances;
import org.example.iterable.IterablePoint;
import org.example.iterable.PedestrianExit;
import org.example.moving.BoardDirection;
import org.example.moving.Pedestrian;
import org.example.moving.PedestrianGroup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.*;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	@Serial
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private MovingObject[][] movingObjects;
	private IterablePoint[][] iterablePoints;
	private final List<PedestrianExit> pedestrianExits = new ArrayList<>();
	public List<Lights> lights = new ArrayList<>();
	public List<LightsCrossingController> lightsCrossingControllers = new ArrayList<>();
	final public int size = 10;
	public CellType editType = CellType.SELECT;
	public FileHandler fileHandler = new FileHandler(this);
	public BoardDirection editDirection = BoardDirection.RIGHT;
	public DrivingPathChances editChances = new DrivingPathChances();
	private int length;
	private int height;
	public boolean resizingActive = true;

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
			if (obj instanceof PedestrianGroup pedestrianGroup){
				for (Pedestrian pedestrian: pedestrianGroup.getPedestrians()) {
					pedestrian.move();
					if (!(newMovingObjects[pedestrian.getX()][pedestrian.getY()] instanceof PedestrianGroup))
						newMovingObjects[pedestrian.getX()][pedestrian.getY()] = new PedestrianGroup();
					PedestrianGroup newPedestrianGroup = (PedestrianGroup)newMovingObjects[pedestrian.getX()][pedestrian.getY()];
					newPedestrianGroup.addPedestrian(pedestrian);
				}
			}
			else {
				obj.move();
				newMovingObjects[obj.getX()][obj.getY()] = obj;
			}
		}
		movingObjects = newMovingObjects;

		for(IterablePoint point : getIterablePoints1d()){
			if (point instanceof Entrance)
				point.iterate();
		}

		for(LightsCrossingController controller: lightsCrossingControllers){
			controller.iterate();
		}

		this.repaint();
	}

	public void clear() {
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[0].length; ++y) {
				points[x][y] = CellType.NOT_SPECIFIED.getObject();
				removeMovingObjectsAt(x, y);
			}
		}
		this.repaint();
	}

	public void clearMovingObjects(){
		for (int x = 0; x < getPointsLength(); ++x){
			for (int y = 0; y < getPointsHeight(); y++){
				removeMovingObjectsAt(x, y);
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
		MovingObject[][] new_movingObjects = new MovingObject[length][height];
		IterablePoint[][] new_iterablePoints = new IterablePoint[length][height];
		this.length = length;
		this.height = height;

		for(int x = 0; x < length; ++x) {
			for(int y = 0; y < height; ++y) {
				if (x < this.points.length && y < this.points[0].length) {
					new_points[x][y] = this.points[x][y];
					new_movingObjects[x][y] = this.movingObjects[x][y];
					new_iterablePoints[x][y] = this.iterablePoints[x][y];
				} else {
					new_points[x][y] = new Point();
				}
			}
		}

		this.points = new_points;
		this.movingObjects = new_movingObjects;
		this.iterablePoints = new_iterablePoints;
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

		for(Lights l: lights){
			g.setColor(l.getColor());
			g.fillOval((l.x * size), (l.y * size), size, size);
		}
	}

	public void mouseClicked(MouseEvent e){
		int x = e.getX() / size;
		int y = e.getY() / size;
		if (editType == CellType.SELECT){
			System.out.println("[POINT SELECTED] - {position: ("+x+", "+y+"), "+getCellAt(x, y).getInfo()+"}");
		}
		else {
			setCell(x, y);
		}
	}

	public void setCell(int x, int y) {
		if(editType == CellType.CAR){
			MovingObject obj = editType.getMovingObject(editDirection);
			obj.setPosition(x,y);
			movingObjects[x][y] = obj;
		} else if (editType == CellType.PEDESTRIAN) {
			MovingObject obj = editType.getMovingObject();
			obj.setPosition(x,y);
			((Pedestrian)obj).setTargetExit(getRandomPedestrianExit());
			movingObjects[x][y] = obj;
		} else {
			points[x][y] = editType.getObject();

			if (points[x][y] instanceof IterablePoint point){
				point.setPosition(x, y);
				point.setBoard(this);
				iterablePoints[x][y] = point;
			}

			if (points[x][y] instanceof PedestrianExit pedestrianExit){
				pedestrianExit.setBoard(this);
			}

			if (points[x][y] instanceof Drivable drivable){
				drivable.setDrivingPathChances(editChances);
			}

			if (points[x][y] instanceof WalkablePoint walkable){
				walkable.setPosition(new Vector2D(x, y));
				for (int i=-1; i < 2; i++){
					for (int j=-1; j < 2; j++){
						int curr_x = x+i;
						int curr_y = y+j;
						if (curr_x < 0 || curr_y < 0 || curr_x >= getPointsLength() || curr_y >= getPointsHeight())
							continue;
						if ((i != 0 || j != 0) && points[curr_x][curr_y] instanceof WalkablePoint neighbour) {
							walkable.addWalkableNeighbour(neighbour);
							neighbour.addWalkableNeighbour(walkable);
							if (i * j != 0){
								neighbour.setNeighbourIsOnDiagonal(walkable);
								walkable.setNeighbourIsOnDiagonal(neighbour);
							}
						}
					}
				}
			}

			if (points[x][y] instanceof PedestrianExit exit){
				pedestrianExits.add(exit);
			}

			System.out.println("[POINT PLACED] - {position: ("+x+", "+y+"), "+getCellAt(x, y).getInfo()+"}");
		}

		this.repaint();
	}

	public void componentResized(ComponentEvent e) {
		if (resizingActive) {
			System.out.println("Resized: "+getWidth()+" "+getHeight());
			int length = (this.getWidth() / size) + 1;
			int height = (this.getHeight() / size) + 1;
			initializeNewPoints(length, height);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (editType == CellType.SELECT)
			return;
		int x = e.getX() / size;
		int y = e.getY() / size;
		setCell(x, y);
	}

	public void save(String filePath){
		fileHandler.saveMap(points, filePath);
	}

	public void load(String fileName){
		fileHandler.loadMap(fileName);
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

	public Point getCellAt(int x, int y){
		if (getMovingObjectAt(x, y) != null)
			return getMovingObjectAt(x, y);
		for (Lights light: lights){
			if (light.x == x && light.y == y){
				return light;
			}
		}
		return getPointAt(x, y);
	}

	public MovingObject getMovingObjectAt(int x, int y){
		return movingObjects[x][y];
	}

	public void removeMovingObjectsAt(int x, int y){
		movingObjects[x][y] = null;
	}

	public int getPointsLength(){
		return points.length;
	}

	public int getPointsHeight(){
		if (points.length > 0)
			return points[0].length;
		return 0;
	}

	public void calcStaticField(){
		for(PedestrianExit exit: pedestrianExits){
			calcExitStaticField(exit);
		}
	}

	private void calcExitStaticField(PedestrianExit exit){
		ArrayList<WalkablePoint> toCheckField = new ArrayList<>();
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				if (points[x][y] instanceof PedestrianExit exit2){
					exit2.setStaticField(exit, 0);
					toCheckField.addAll(exit2.getWalkableNeighbours());
				}
			}
		}

		while(!toCheckField.isEmpty()){
			WalkablePoint currPoint = toCheckField.get(0);
			if (currPoint.calcStaticField(exit)){
				toCheckField.addAll(currPoint.getWalkableNeighbours());
			}
			toCheckField.remove(currPoint);
		}
	}

	public PedestrianExit getRandomPedestrianExit(){
		Random rand = new Random();
		int randomIndex = rand.nextInt(pedestrianExits.size());
		return pedestrianExits.get(randomIndex);
	}

// ------------------------------------------------------------------------------
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void mousePressed(MouseEvent e) {}
}
