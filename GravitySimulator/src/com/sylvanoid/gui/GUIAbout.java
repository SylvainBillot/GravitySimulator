package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class GUIAbout extends JDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GUIAbout me;
	private GUIProgram mother;

	public GUIAbout(GUIProgram mother) {
		me = this;
		this.mother = mother;
		setTitle("About");
		setModal(true);
		int w = 400;
		int h = 300;
		setLocation(new Point((mother.getWidth() - w) / 2, (mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		getContentPane().setLayout(new BorderLayout());
		String text = "<html><body>Gravity Simulator<br/>" + "V0.9 Beta<br/>GNU GENERAL PUBLIC LICENSE<br/>"
				+ "Credit: Sylvain Billot<br/>" + "https://github.com/SylvainBillot/GravitySimulator<br/>"
				+ "Credit: jogl 2 library<br/>" + "Credit: gluegen 2 library<br/>" + "Credit: vecmath library<br/>"
				+ "Credit: jcodec library<br/>" + "Credit: jafama library<br/>" + "</body></html>";
		JLabel content = new JLabel(text);
		content.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		getContentPane().add(content, BorderLayout.CENTER);
		JButton btnCancel = new JButton("OK");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				me.setVisible(false);
				me.mother.setVisible(true);

			}
		});
		getContentPane().add(btnCancel, BorderLayout.SOUTH);

	}
}
