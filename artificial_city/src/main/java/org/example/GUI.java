package org.example;

import org.example.cells.CellType;
import org.example.moving.BoardDirection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Board board;
	private JButton start, clear, load, save, edit, file, saveChances;
	private JComboBox<CellType> drawType;
	private JSlider pred;
	private JTextField[][] pathChoices;
	private JTextField fileName;
	private JFrame frame;
	private JComboBox<String> filesToLoad;
	private JPanel editPanel, filePanel;
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

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.addActionListener(this);

		saveChances = new JButton("Save chances");
		saveChances.setActionCommand("save chances");
		saveChances.addActionListener(this);

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

		editPanel = new JPanel();
		editPanel.setLayout(layout);
		editPanel.setPreferredSize(new Dimension(200, container.getHeight()));
		editPanel.setVisible(false);
		editPanel.add(type);
		editPanel.add(drawType);
		editPanel.add(road);
		editPanel.add(from);
		for (JLabel label: fromDirections) editPanel.add(label);
		for (JLabel label: toDirections) editPanel.add(label);
		for (int i=0; i < BoardDirection.values().length; i++){
			for (int j=0; j < BoardDirection.values().length; j++){
				editPanel.add(pathChoices[i][j]);
			}
		}
		editPanel.add(saveChances);
		editPanel.add(clear);

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

		layout.putConstraint(SpringLayout.NORTH, saveChances, 30, SpringLayout.NORTH, pathChoices[3][3]);
		layout.putConstraint(SpringLayout.EAST, saveChances, -20, SpringLayout.EAST, editPanel);

		layout.putConstraint(SpringLayout.SOUTH, clear, -10, SpringLayout.SOUTH, editPanel);
		layout.putConstraint(SpringLayout.WEST, clear, 10, SpringLayout.WEST, editPanel);

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
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			iterNum++;
			frame.setTitle("Artificial City simulation (" + iterNum + " iteration)");
			board.iteration();
		} else {
			String command = e.getActionCommand();
			switch (command) {
				case "Start":
					if (!running) {
						timer.start();
						start.setText("Pause");
					} else {
						timer.stop();
						start.setText("Start");
						CellType newType = (CellType) drawType.getSelectedItem();
						System.out.println("Edit type: "+newType);
						board.editType = newType;
					}
					running = !running;
					clear.setEnabled(true);
					break;
				case "clear":
					iterNum = 0;
					timer.stop();
					start.setEnabled(true);
					board.clear();
					frame.setTitle("Artificial City simulation");
					break;
				case "drawType":
					CellType newType = (CellType) drawType.getSelectedItem();
					System.out.println("Edit type: "+newType);
					board.editType = newType;
					break;
				case "save":
					if (!running) {
						board.save(fileName.getText());
						frame.setTitle("Artificial City simulation - saved to: " + fileName.getText());
					}
					break;
				case "load":
					if (!running) {
						iterNum = 0;
						timer.stop();
						start.setEnabled(true);
						String loadFileName = (String) filesToLoad.getSelectedItem();
						board.clear();
						board.load(loadFileName);
						fileName.setText(loadFileName);
						frame.setTitle("Artificial City simulation - loaded: " + loadFileName);
					}
					break;
				case "save chances":
					for (int i = 0; i < pathChoices.length; i++) {
						for (int j = 0; j < pathChoices[0].length; j++) {
							int chance = Integer.parseInt(pathChoices[i][j].getText());
							chance = Math.max(0, chance);
							chance = Math.min(chance, 100);
							BoardDirection from = BoardDirection.values()[i];
							BoardDirection to = BoardDirection.values()[j];
							board.editChances.setChanceFromTo(from, to, chance);
						}
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
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}
}
