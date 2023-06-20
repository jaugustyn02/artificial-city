package org.example;

import javax.swing.JFrame;
import java.io.Serial;

public class Program extends JFrame {
	@Serial
	private static final long serialVersionUID = 1L;
	private final GUI gof;

	public Program() {
		setTitle("Artificial City Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gof = new GUI(this);
		gof.initialize(this.getContentPane());
		this.setSize(1440, 720);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Program();
	}
}
