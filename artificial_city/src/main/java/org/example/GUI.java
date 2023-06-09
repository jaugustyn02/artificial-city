package org.example;

import org.example.cells.CellType;
import org.example.moving.BoardDirection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class GUI extends JPanel implements ActionListener, ChangeListener {
	private final static String title = "Artificial City Simulation";
	@Serial private static final long serialVersionUID = 1L;
	private final Timer timer;
	private Board board;
	private JButton start, clear, load, save, edit, file, saveChances, resetChances, clearChances, clearObjects;
	private JComboBox<CellType> drawType;
	private JSlider pred;
	private JTextField[][] pathChoices;
	private JTextField fileName;
	private final JFrame frame;
	private JComboBox<String> filesToLoad;
	private JPanel editPanel, filePanel;
	private final int maxDelay = 500;
	int initDelay = 100;
	private boolean running = false;
	private static final int numOfDirections = BoardDirection.values().length;
	private JTextField speedLimitTextField;
	private final Clock clock = new Clock(12, 0, 0);
	public GUI(JFrame jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	public void initialize(Container container) {
		container.setLayout(new BorderLayout());

//----------------------------------------------buttonPanel----------------------------------------------

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);

		pred = new JSlider();
		pred.setMinimum(0);
		pred.setMaximum(maxDelay);
		pred.addChangeListener(this);
		pred.setValue(maxDelay - timer.getDelay());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(start);
		buttonPanel.add(pred);

//----------------------------------------------toolBar----------------------------------------------

		file = new JButton("File");
		file.setActionCommand("file");
		file.addActionListener(this);

		edit = new JButton("Edit");
		edit.setActionCommand("edit");
		edit.addActionListener(this);

		JToolBar toolBar = new JToolBar();
		toolBar.add(file);
		toolBar.add(edit);

//----------------------------------------------editPanel----------------------------------------------

		drawType = new JComboBox<>(CellType.values());
		drawType.addActionListener(this);
		drawType.setActionCommand("drawType");

		clear = new JButton("Clear All");
		clear.setActionCommand("clear");
		clear.addActionListener(this);
		clear.setBorder(BorderFactory.createCompoundBorder(
				clear.getBorder(),
				BorderFactory.createEmptyBorder(0, -5, 0, -5)));

		saveChances = new JButton("Save chances");
		saveChances.setActionCommand("save chances");
		saveChances.addActionListener(this);

		saveChances = new JButton("Save chances");
		saveChances.setActionCommand("save chances");
		saveChances.addActionListener(this);

		resetChances = new JButton("Reset");
		resetChances.setActionCommand("reset chances");
		resetChances.addActionListener(this);

		clearChances = new JButton("Clear");
		clearChances.setActionCommand("clear chances");
		clearChances.addActionListener(this);

		clearObjects = new JButton("Clear Objects");
		clearObjects.setActionCommand("clear objects");
		clearObjects.addActionListener(this);
		clearObjects.setBorder(BorderFactory.createCompoundBorder(
				clearObjects.getBorder(),
				BorderFactory.createEmptyBorder(0, -5, 0, -5)));

		pathChoices = new JTextField[BoardDirection.values().length][BoardDirection.values().length];
		for (int i=0; i < BoardDirection.values().length; i++){
			for (int j=0; j < BoardDirection.values().length; j++){
				pathChoices[i][j] = new JTextField("0");
				pathChoices[i][j].setPreferredSize(new Dimension(26, 22));
			}
		}

		SpringLayout layout = new SpringLayout();

		JLabel type = new JLabel("Edit Type:");
		JLabel road = new JLabel("Road driving path chances:");
		JLabel from = new JLabel("From\\To");
		JLabel[] fromDirections = {new JLabel("\uD83E\uDC81"), new JLabel("\uD83E\uDC7A"), new JLabel("\uD83E\uDC7B"), new JLabel("\uD83E\uDC78")};
		JLabel[] toDirections = {new JLabel("\uD83E\uDC81"), new JLabel("\uD83E\uDC7A"), new JLabel("\uD83E\uDC7B"), new JLabel("\uD83E\uDC78")};
		JLabel speedLimitLabel = new JLabel("Speed limit: ");
		speedLimitTextField = new JTextField("5");
		speedLimitTextField.setPreferredSize(new Dimension(40, 20));
		JButton speedLimitSaveButton = new JButton("Save");
		speedLimitSaveButton.setActionCommand("save speed limit");
		speedLimitSaveButton.setPreferredSize(new Dimension(55,20));
		speedLimitSaveButton.addActionListener(this);
		speedLimitSaveButton.setBorder(BorderFactory.createCompoundBorder(
				speedLimitSaveButton.getBorder(),
				BorderFactory.createEmptyBorder(0, -5, 0, -5)));

		editPanel = new JPanel();
		editPanel.setLayout(layout);
		editPanel.setPreferredSize(new Dimension(200, container.getHeight()));
		editPanel.setVisible(false);
		editPanel.add(type);
		editPanel.add(drawType);
		editPanel.add(speedLimitLabel);
		editPanel.add(speedLimitTextField);
		editPanel.add(road);
		editPanel.add(from);
		for (JLabel label: fromDirections) editPanel.add(label);
		for (JLabel label: toDirections) editPanel.add(label);
		for (int i=0; i < BoardDirection.values().length; i++){
			for (int j=0; j < BoardDirection.values().length; j++){
				editPanel.add(pathChoices[i][j]);
			}
		}
		editPanel.add(resetChances);
		editPanel.add(clearChances);
		editPanel.add(saveChances);
		editPanel.add(clear);
		editPanel.add(clearObjects);
		editPanel.add(speedLimitSaveButton);

		layout.putConstraint(SpringLayout.NORTH, type, 10, SpringLayout.NORTH, editPanel);
		layout.putConstraint(SpringLayout.WEST, type, 10, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.NORTH,drawType, 25, SpringLayout.NORTH, type);
		layout.putConstraint(SpringLayout.WEST, drawType, 10, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.NORTH, road, 50, SpringLayout.NORTH, drawType);
		layout.putConstraint(SpringLayout.WEST, road, 10, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.NORTH, from, 10, SpringLayout.SOUTH, road);
		layout.putConstraint(SpringLayout.WEST, from, 10, SpringLayout.WEST, editPanel);

		for (int i=0; i<toDirections.length; i++){
			layout.putConstraint(SpringLayout.NORTH, toDirections[i], -16, SpringLayout.SOUTH, from);
			layout.putConstraint(SpringLayout.WEST, toDirections[i], 30*i + 58, SpringLayout.WEST, from);
		}

		layout.putConstraint(SpringLayout.NORTH, fromDirections[0], 10, SpringLayout.SOUTH, from);
		layout.putConstraint(SpringLayout.WEST, fromDirections[0], 20, SpringLayout.WEST, editPanel);
		for (int i=1; i<fromDirections.length; i++){
			layout.putConstraint(SpringLayout.NORTH, fromDirections[i], 10, SpringLayout.SOUTH, fromDirections[i-1]);
			layout.putConstraint(SpringLayout.WEST, fromDirections[i], 20, SpringLayout.WEST, editPanel);
		}
		for (int i=0; i < BoardDirection.values().length; i++){
			for (int j=0; j < BoardDirection.values().length; j++){
				layout.putConstraint(SpringLayout.NORTH, pathChoices[i][j], 25*i + 10, SpringLayout.SOUTH, from);
				layout.putConstraint(SpringLayout.WEST, pathChoices[i][j], 30*j + 63, SpringLayout.WEST, editPanel);
			}
		}

		layout.putConstraint(SpringLayout.NORTH, speedLimitLabel, 30, SpringLayout.NORTH, drawType);
		layout.putConstraint(SpringLayout.WEST, speedLimitLabel, 10, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.NORTH, speedLimitTextField, 30, SpringLayout.NORTH, drawType);
		layout.putConstraint(SpringLayout.WEST, speedLimitTextField, 71, SpringLayout.WEST, speedLimitLabel);

		layout.putConstraint(SpringLayout.NORTH, speedLimitSaveButton, 30, SpringLayout.NORTH, drawType);
		layout.putConstraint(SpringLayout.WEST, speedLimitSaveButton, 43, SpringLayout.WEST, speedLimitTextField);

		layout.putConstraint(SpringLayout.NORTH, resetChances, 30, SpringLayout.NORTH, pathChoices[3][3]);
		layout.putConstraint(SpringLayout.WEST, resetChances, 46, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.NORTH, clearChances, 30, SpringLayout.NORTH, pathChoices[3][3]);
		layout.putConstraint(SpringLayout.WEST, clearChances, 70, SpringLayout.WEST, resetChances);

		layout.putConstraint(SpringLayout.NORTH, saveChances, 35, SpringLayout.NORTH, resetChances);
		layout.putConstraint(SpringLayout.EAST, saveChances, -20, SpringLayout.EAST, editPanel);

		layout.putConstraint(SpringLayout.SOUTH, clear, -10, SpringLayout.SOUTH, editPanel);
		layout.putConstraint(SpringLayout.WEST, clear, 10, SpringLayout.WEST, editPanel);

		layout.putConstraint(SpringLayout.SOUTH, clearObjects, -10, SpringLayout.SOUTH, editPanel);
		layout.putConstraint(SpringLayout.WEST, clearObjects, 80, SpringLayout.WEST, clear);

//----------------------------------------------filePanel----------------------------------------------

		load = new JButton("Load");
		load.setActionCommand("load");
		load.addActionListener(this);

		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);

		fileName = new JTextField("");
		fileName.setPreferredSize(new Dimension(150, 30));
		fileName.setFont(new Font("Siema", Font.PLAIN, 12));
		fileName.setBorder(BorderFactory.createCompoundBorder(
				fileName.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		filesToLoad = new JComboBox<>(FileHandler.getFilenamesToLoad());
		filesToLoad.setSelectedIndex(0);

		SpringLayout layout2 = new SpringLayout();

		filePanel = new JPanel();
		filePanel.setLayout(layout2);
		filePanel.setPreferredSize(new Dimension(200, container.getHeight()));
		filePanel.setVisible(false);
		filePanel.add(filesToLoad);
		filePanel.add(load);
		filePanel.add(fileName);
		filePanel.add(save);

		layout2.putConstraint(SpringLayout.NORTH, filesToLoad, 10, SpringLayout.NORTH, filePanel);
		layout2.putConstraint(SpringLayout.WEST, filesToLoad, 10, SpringLayout.WEST, filePanel);

		layout2.putConstraint(SpringLayout.NORTH, load, 10, SpringLayout.SOUTH, filesToLoad);
		layout2.putConstraint(SpringLayout.WEST, load, 10, SpringLayout.WEST, filePanel);

		layout2.putConstraint(SpringLayout.NORTH, fileName, 10, SpringLayout.SOUTH, load);
		layout2.putConstraint(SpringLayout.WEST, fileName, 10, SpringLayout.WEST, filePanel);

		layout2.putConstraint(SpringLayout.NORTH, save, 10, SpringLayout.SOUTH, fileName);
		layout2.putConstraint(SpringLayout.WEST, save, 10, SpringLayout.WEST, filePanel);

//----------------------------------------------container----------------------------------------------

		board = new Board(0, 0);
		container.add(board, BorderLayout.CENTER);
		container.add(toolBar, BorderLayout.NORTH);
		container.add(editPanel, BorderLayout.EAST);
		container.add(filePanel, BorderLayout.WEST);
		container.add(buttonPanel, BorderLayout.SOUTH);

//----------------------------------------------rest---------------------------------------------------
		saveChances.doClick(); // initialize chances
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			frame.setTitle(title + " - Time: " + clock.getTime());
			board.iteration();
			clock.iterate();
		} else {
			String command = e.getActionCommand();
			switch (command) {
				case "Start":
					if (!running) {
						timer.start();
						board.calcStaticField();
						start.setText("Pause");
					} else {
						timer.stop();
						start.setText("Start");
						board.editType = (CellType) drawType.getSelectedItem();
					}
					running = !running;
					clear.setEnabled(true);
					break;
				case "clear":
					clock.reset();
					timer.stop();
					start.setEnabled(true);
					board.clear();
					frame.setTitle(title);
					break;
				case "drawType":
					CellType newType = (CellType) drawType.getSelectedItem();
					System.out.println("Edit type: "+newType);
					board.editType = newType;
					if (board.editType != CellType.SELECT){
						board.selectedPoint = null;
					}
					break;
				case "save":
					if (!running) {
						board.save(fileName.getText());
						frame.setTitle(title + " - saved:" + fileName.getText());
					}
					break;
				case "load":
					if (!running) {
						clock.reset();
						timer.stop();
						start.setEnabled(true);
						String loadFileName = (String) filesToLoad.getSelectedItem();
						board.clear();
						board.load(loadFileName);
						fileName.setText(loadFileName);
						frame.setTitle(title + " - loaded: " + loadFileName);
					}
					break;
				case "save chances":
					for (int row = 0; row < numOfDirections; row++) {
						if (!isValidChanceRow(row))
							resetChancesRow(row);

						for (int col = 0; col < numOfDirections; col++) {
							int chance = Integer.parseInt(pathChoices[row][col].getText());
							BoardDirection from = BoardDirection.values()[row];
							BoardDirection to = BoardDirection.values()[col];
							board.editChances.setChanceFromTo(from, to, chance);
						}
						board.setRoadChances();
					}
					break;
				case "edit":
					if(!filePanel.isVisible())
						board.resizingActive = !board.resizingActive;
					editPanel.setVisible(!editPanel.isVisible());
					break;
				case "file":
					if(!editPanel.isVisible())
						board.resizingActive = !board.resizingActive;
					filePanel.setVisible(!filePanel.isVisible());
					break;
				case "reset chances":
					resetChances();
					break;
				case "clear chances":
					clearChances();
					break;
				case "clear objects":
					board.clearMovingObjects();
					break;
				case "save speed limit":
					String limitS = speedLimitTextField.getText();
					if(isNumeric(limitS)){
						board.editSpeedLimit = Integer.parseInt(limitS);
						board.setSpeedLimit();
					} else {
						speedLimitTextField.setText("");
					}
					break;
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}

	private static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private static boolean isValidChance(int num){
		return num >= 0 && num <= 100;
	}

	private boolean isValidChanceRow(int row){
		int sumOfPercents = 0;
		for(int col = 0; col < numOfDirections; col++){
			String text = pathChoices[row][col].getText();
			if (!isNumeric(text))
				return false;
			int chance = Integer.parseInt(text);
			if (!isValidChance(chance))
				return false;
			sumOfPercents += chance;
		}
		return (sumOfPercents == 100);
	}

	private void resetChancesRow(int row){
		for (int col = 0; col < numOfDirections; col++){
			if (col == row)
				pathChoices[row][col].setText("100");
			else
				pathChoices[row][col].setText("0");
		}
	}

	private void resetChances(){
		for(int row=0; row < numOfDirections; row++){
			resetChancesRow(row);
		}
	}

	private void clearChances(){
		for(int row=0; row < numOfDirections; row++) {
			for (int col = 0; col < numOfDirections; col++) {
				pathChoices[row][col].setText("0");
			}
		}
	}
}
