package com.sylvanoid.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Parameters;

public class GUIFollowOther extends JDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GUIProgram mother;
	private GUIFollowOther me;
	private JComboBox<Matter> matters;
	private Parameters parameters;

	public GUIFollowOther(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		this.parameters = mother.getParameters();
		builder(mother);
	}

	private void builder(GUIProgram mother) {
		setTitle("Follow someyhing");
		setModal(true);
		setSize(new Dimension(600, 100));
		int w = getSize().width;
		int h = getSize().height;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setLayout(new GridLayout(0, 2));
		add(new Label("Follow this:"));
		matters = new JComboBox<Matter>();
		for (Matter m : mother.getUnivers().getListMatter()) {
			matters.addItem(m);
		}
		add(matters);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(cancelAction());
		add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(okAction());
		add(btnOK);
	}

	private ActionListener cancelAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				me.setVisible(false);
				me.mother.setVisible(true);
				me.mother.getAnimator().start();
			}
		};
	}

	private ActionListener okAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parameters.setFollowCentroid(false);
					parameters.setFollowMaxMass(false);
					parameters.setObjectToFollow((Matter) matters
							.getSelectedItem());
					me.setVisible(false);
					me.mother.setVisible(true);
					me.mother.getAnimator().start();
				} catch (Exception em) {
					em.printStackTrace();
				}
			}
		};
	}

}
