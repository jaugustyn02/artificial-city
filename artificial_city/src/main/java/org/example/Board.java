package org.example;

import org.example.cells.CellType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 10;
	public CellType editType = CellType.NOT_SPECIFIED;
	public FileHandler fileHandler = new FileHandler();


	public Board(int length, int height) {
		initialize(length, height);
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	public void iteration() {
		for (int x = 0; x < points.length; ++x)
			for (int lane = 0; lane < points[x].length; ++lane) {
				points[x][lane].iterate();
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

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[0].length; ++y) {
				points[x][y] = new Point();
			}
	}

	private void initializeNewPoints(int length, int height) {
		Point[][] new_points = new Point[length][height];

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
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;

		points[x][y] = editType.getObject();
		System.out.println(points[x][y].type);
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

		points[x][y] = editType.getObject();
		this.repaint();
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
//		int[] size = fileHandler.loadSize();
//		size[0] = Math.max(points.length, size[0]);
//		size[1] = Math.max(points[0].length, size[1]);
//		this.setSize(size[0]*this.size, size[1]*this.size);

		int[] size = new int[2];
		size[0] = points.length;
		size[1] = points[0].length;
		points = fileHandler.loadMap(size, fileName);
		this.repaint();
	}

}
