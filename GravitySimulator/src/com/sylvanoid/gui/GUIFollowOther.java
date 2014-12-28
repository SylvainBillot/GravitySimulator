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
		setTitle("Follow someyhing");
		setModal(true);
		int w = 600;
		int h = 100;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(2, 2));
		add(new Label("Follow this:"));
		matters = new JComboBox<Matter>();
		for (Matter m : mother.getUnivers().getListMatter().values()) {
			matters.addItem(m);
		}
		add(matters);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				me.setVisible(false);
				me.mother.setVisible(true);
				me.mother.getAnimator().start();
			}
		});
		add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					parameters.setFollowCentroid(false);
					parameters.setFollowMaxMass(false);
					parameters.setObjectToFollow((Matter) matters
							.getSelectedItem());
					me.setVisible(false);
					me.mother.setVisible(true);
					me.mother.getAnimator().start();
				} catch (Exception em) {
					JDialog d = new JDialog();
					d.add(new Label("Error: " + em.getMessage()));
					d.setVisible(true);
					d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				}
			}
		});
		add(btnOK);
	}

}
