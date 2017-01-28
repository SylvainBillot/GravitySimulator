package com.sylvanoid.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.vecmath.Vector3d;

public class GUIAddUnivers extends JDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GUIProgram mother;
	private GUIAddUnivers me;
	private boolean ok = false;
	private Vector3d offset = new Vector3d();
	private Vector3d speed  = new Vector3d();

	private JFormattedTextField offsetX;
	private JFormattedTextField offsetY;
	private JFormattedTextField offsetZ;
	private JFormattedTextField speedX;
	private JFormattedTextField speedY;
	private JFormattedTextField speedZ;

	private DecimalFormat dfsc = new DecimalFormat("0.####E0");

	public GUIAddUnivers(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		builder(mother);
	}

	private void builder(GUIProgram mother) {
		setTitle("Set offsets");
		setModal(true);
		int w = 800;
		int h = 300;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(7, 2));

		add(new Label("Offset x:"));
		offsetX = new JFormattedTextField(dfsc);
		offsetX.setValue(offset.getX());
		add(offsetX);

		add(new Label("Offset y:"));
		offsetY = new JFormattedTextField(dfsc);
		offsetY.setValue(offset.getY());
		add(offsetY);

		add(new Label("Offset z:"));
		offsetZ = new JFormattedTextField(dfsc);
		offsetZ.setValue(offset.getZ());
		add(offsetZ);

		add(new Label("Speed x:"));
		speedX = new JFormattedTextField(dfsc);
		speedX.setValue(speed.getX());
		add(speedX);

		add(new Label("Speed y:"));
		speedY = new JFormattedTextField(dfsc);
		speedY.setValue(speed.getY());
		add(speedY);

		add(new Label("Speed z:"));
		speedZ = new JFormattedTextField(dfsc);
		speedZ.setValue(speed.getZ());
		add(speedZ);


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
				me.ok = false;
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
				me.ok = true;
				try {
					offset.set(
							Double.parseDouble(offsetX.getValue().toString()),
							Double.parseDouble(offsetY.getValue().toString()),
							Double.parseDouble(offsetZ.getValue().toString()));
					speed.set(
							Double.parseDouble(speedX.getValue().toString()),
							Double.parseDouble(speedY.getValue().toString()),
							Double.parseDouble(speedZ.getValue().toString()));
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
		};
	}

	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @param ok
	 *            the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}

	/**
	 * @return the offset
	 */
	public Vector3d getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Vector3d offset) {
		this.offset = offset;
	}

	/**
	 * @return the speed
	 */
	public Vector3d getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Vector3d speed) {
		this.speed = speed;
	}

}
