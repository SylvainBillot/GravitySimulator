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
	private Vector3d speed = new Vector3d();
	private Vector3d rotate = new Vector3d();

	private JFormattedTextField offsetX;
	private JFormattedTextField offsetY;
	private JFormattedTextField offsetZ;
	private JFormattedTextField speedX;
	private JFormattedTextField speedY;
	private JFormattedTextField speedZ;
	private JFormattedTextField rotateX;
	private JFormattedTextField rotateY;
	private JFormattedTextField rotateZ;

	private DecimalFormat dfsc = new DecimalFormat("0.####E0");

	public GUIAddUnivers(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		this.builder(mother);
	}

	private void builder(GUIProgram mother) {
		setTitle("Set offsets");
		setModal(true);
		setSize(new Dimension(800, 150));
		int w = getSize().width;
		int h = getSize().height;
		setLocation(new Point((mother.getWidth() - w) / 2, (mother.getHeight() - h) / 2));
		getContentPane().setLayout(new GridLayout(0, 6));

		getContentPane().add(new Label("Offset x:"));
		offsetX = new JFormattedTextField(dfsc);
		offsetX.setValue(offset.getX());
		getContentPane().add(offsetX);

		getContentPane().add(new Label("Offset y:"));
		offsetY = new JFormattedTextField(dfsc);
		offsetY.setValue(offset.getY());
		getContentPane().add(offsetY);

		getContentPane().add(new Label("Offset z:"));
		offsetZ = new JFormattedTextField(dfsc);
		offsetZ.setValue(offset.getZ());
		getContentPane().add(offsetZ);

		getContentPane().add(new Label("Speed x:"));
		speedX = new JFormattedTextField(dfsc);
		speedX.setValue(speed.getX());
		getContentPane().add(speedX);

		getContentPane().add(new Label("Speed y:"));
		speedY = new JFormattedTextField(dfsc);
		speedY.setValue(speed.getY());
		getContentPane().add(speedY);

		getContentPane().add(new Label("Speed z:"));
		speedZ = new JFormattedTextField(dfsc);
		speedZ.setValue(speed.getZ());
		getContentPane().add(speedZ);

		getContentPane().add(new Label("Rotate x:"));
		rotateX = new JFormattedTextField(dfsc);
		rotateX.setValue(rotate.getX());
		getContentPane().add(rotateX);

		getContentPane().add(new Label("Rotate y:"));
		rotateY = new JFormattedTextField(dfsc);
		rotateY.setValue(rotate.getY());
		getContentPane().add(rotateY);

		getContentPane().add(new Label("Rotate z:"));
		rotateZ = new JFormattedTextField(dfsc);
		rotateZ.setValue(rotate.getZ());
		getContentPane().add(rotateZ);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(cancelAction());
		getContentPane().add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(okAction());
		getContentPane().add(btnOK);
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
			public void actionPerformed(ActionEvent e) {
				me.ok = true;
				try {
					offset.set(Double.parseDouble(offsetX.getValue().toString()),
							Double.parseDouble(offsetY.getValue().toString()),
							Double.parseDouble(offsetZ.getValue().toString()));
					speed.set(Double.parseDouble(speedX.getValue().toString()),
							Double.parseDouble(speedY.getValue().toString()),
							Double.parseDouble(speedZ.getValue().toString()));
					rotate.set(Double.parseDouble(rotateX.getValue().toString()),
							Double.parseDouble(rotateY.getValue().toString()),
							Double.parseDouble(rotateZ.getValue().toString()));
					me.setVisible(false);
					me.mother.setVisible(true);
					me.mother.getAnimator().start();
				} catch (Exception em) {
					em.printStackTrace();
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
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(Vector3d speed) {
		this.speed = speed;
	}

	/**
	 * @return the rotate
	 */
	public Vector3d getRotate() {
		return rotate;
	}

	/**
	 * @param rotate
	 *            the rotate to set
	 */
	public void setRotate(Vector3d rotate) {
		this.rotate = rotate;
	}

}
