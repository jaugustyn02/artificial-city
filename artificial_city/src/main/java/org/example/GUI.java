package org.example;

import org.example.cells.CellType;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Board board;
	private JButton start;
	private JButton clear;
	private JButton load;
	private JButton save;
	private JComboBox<CellType> drawType;
	private JSlider pred;
	private JTextField fileName;
	private JFrame frame;
	private JComboBox<String> filesToLoad;
	private int iterNum = 0;
	private final int maxDelay = 500;
	private final int initDelay = 100;
	private boolean running = false;

	public GUI(JFrame jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.addActionListener(this);

		load = new JButton("Load");
		load.setActionCommand("load");
		load.addActionListener(this);

		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);

		pred = new JSlider();
		pred.setMinimum(0);
		pred.setMaximum(maxDelay);
		pred.addChangeListener(this);
		pred.setValue(maxDelay - timer.getDelay());
		
		drawType = new JComboBox<CellType>(CellType.values());
		drawType.addActionListener(this);
		drawType.setActionCommand("drawType");

		fileName = new JTextField("");
		fileName.setPreferredSize(new Dimension(100, 25));
		fileName.setBorder(BorderFactory.createCompoundBorder(
				fileName.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		filesToLoad = new JComboBox<>(FileHandler.getFilenamesToLoad());


		buttonPanel.add(start);
		buttonPanel.add(clear);
		buttonPanel.add(drawType);
		buttonPanel.add(pred);
		buttonPanel.add(load);
		buttonPanel.add(filesToLoad);
		buttonPanel.add(save);
		buttonPanel.add(fileName);

		board = new Board(1024, 768 - buttonPanel.getHeight());
		container.add(board, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			iterNum++;
			frame.setTitle("Artificial City simulation (" + Integer.toString(iterNum) + " iteration)");
			board.iteration();
		} else {
			String command = e.getActionCommand();
			if (command.equals("Start")) {
				if (!running) {
					timer.start();
					start.setText("Pause");
				} else {
					timer.stop();
					start.setText("Start");
				}
				running = !running;
				clear.setEnabled(true);

			} else if (command.equals("clear")) {
				iterNum = 0;
				timer.stop();
				start.setEnabled(true);
				board.clear();
				frame.setTitle("Artificial City simulation");
			}
			else if (command.equals("drawType")){
				CellType newType = (CellType)drawType.getSelectedItem();
				System.out.println(newType);
				board.editType = newType;
			} else if (command.equals("save")) {
				if (!running) {
					board.save(fileName.getText());
					frame.setTitle("Artificial City simulation - saved to: " + fileName.getText());
				}
			} else if (command.equals("load")) {
				if (!running) {
					iterNum = 0;
					timer.stop();
					start.setEnabled(true);
					String loadFileName = (String)filesToLoad.getSelectedItem();
					board.clear();
					board.load(loadFileName);
					frame.setTitle("Artificial City simulation - loaded: " + loadFileName);
				}
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}

	public int getIterNum(){
		return iterNum;
	}
}
