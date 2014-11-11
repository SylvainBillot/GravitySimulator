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
import javax.swing.JFormattedTextField;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfUnivers;

public class GUIParam extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUIParam me;
	private GUIProgram mother;
	private JComboBox typeOfUnivers;
	private JFormattedTextField timeFactor;
	private JFormattedTextField scala;
	private JFormattedTextField numberOfObjects;
	private JFormattedTextField nebulaRadius;
	private JFormattedTextField densiteMin;
	private JFormattedTextField densiteMax;
	private JFormattedTextField massObjectMin;
	private JFormattedTextField massObjectMax;
	private JFormattedTextField probFusion;
	private JFormattedTextField typeOfImpact;
	private JFormattedTextField darkMatterMass;
	private JFormattedTextField darkMatterDensity;

	public GUIParam(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		setTitle("Parameters");
		setModal(true);
		int w = 600;
		int h = 500;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(14, 2));
		add(new Label("Type of Univers:"));
		typeOfUnivers = new JComboBox();
		for (TypeOfUnivers tou : TypeOfUnivers.values()) {
			typeOfUnivers.addItem(tou.getLabel());
		}
		typeOfUnivers.setSelectedItem(HelperVariable.typeOfUnivers.getLabel());
		typeOfUnivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (typeOfUnivers.getSelectedIndex()) {
				case 0:
					HelperVariable.typeOfUnivers = TypeOfUnivers.Planetary;
					scala.setValue(1);
					timeFactor.setValue(100);
					break;
				case 1:
					HelperVariable.typeOfUnivers = TypeOfUnivers.PlanetaryRandom;
					scala.setValue(1);
					timeFactor.setValue(20);
					numberOfObjects.setValue(20);
					probFusion.setValue(1);
					densiteMin.setValue(30);
					densiteMax.setValue(30);
					nebulaRadius.setValue(500);
					massObjectMin.setValue(10000);
					massObjectMax.setValue(1000000);
					break;
				case 2:
					HelperVariable.typeOfUnivers = TypeOfUnivers.Random;
					scala.setValue(1);
					timeFactor.setValue(1);
					numberOfObjects.setValue(1000);
					probFusion.setValue(1);
					densiteMin.setValue(500);
					densiteMax.setValue(500);
					nebulaRadius.setValue(600);
					massObjectMin.setValue(100000000);
					massObjectMax.setValue(1000000000);
					break;
				case 3:
					HelperVariable.typeOfUnivers = TypeOfUnivers.RandomRotateUnivers;
					scala.setValue(1);
					timeFactor.setValue(1);
					numberOfObjects.setValue(1000);
					probFusion.setValue(1);
					densiteMin.setValue(50);
					densiteMax.setValue(50);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(10000);
					massObjectMax.setValue(100000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					break;
				case 4:
					HelperVariable.typeOfUnivers = TypeOfUnivers.GalaxiesCollision;
					scala.setValue(1);
					timeFactor.setValue(1);
					numberOfObjects.setValue(800);
					probFusion.setValue(1);
					densiteMin.setValue(800);
					densiteMax.setValue(800);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(10000000);
					massObjectMax.setValue(100000000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					break;
				case 5:
					HelperVariable.typeOfUnivers = TypeOfUnivers.PlanetariesGenesis;
					scala.setValue(1);
					timeFactor.setValue(10000);
					numberOfObjects.setValue(500);
					probFusion.setValue(1);
					densiteMin.setValue(50);
					densiteMax.setValue(50);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(1000);
					massObjectMax.setValue(100000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					break;
				}
			}
		});
		add(typeOfUnivers);
		add(new Label("Scala:"));
		scala = new JFormattedTextField(HelperVariable.scala);
		add(scala);
		add(new Label("Time factor:"));
		timeFactor = new JFormattedTextField(HelperVariable.timeFactor);
		add(timeFactor);
		add(new Label("Number of object:"));
		numberOfObjects = new JFormattedTextField(
				HelperVariable.numberOfObjects);
		add(numberOfObjects);
		add(new Label("Nebula radius:"));
		nebulaRadius = new JFormattedTextField(HelperVariable.nebulaRadius);
		add(nebulaRadius);
		add(new Label("Density min:"));
		densiteMin = new JFormattedTextField(HelperVariable.dentityMin);
		add(densiteMin);
		add(new Label("Dentity max:"));
		densiteMax = new JFormattedTextField(HelperVariable.densityMax);
		add(densiteMax);
		add(new Label("Mass object min:"));
		massObjectMin = new JFormattedTextField(HelperVariable.massObjectMin);
		add(massObjectMin);
		add(new Label("Mass object max:"));
		massObjectMax = new JFormattedTextField(HelperVariable.massObjectMax);
		add(massObjectMax);
		add(new Label("Fusion probability (1-0):"));
		probFusion = new JFormattedTextField(HelperVariable.probFusion);
		add(probFusion);
		add(new Label("Type of impact (1-elastic 0-inelastic):"));
		typeOfImpact = new JFormattedTextField(HelperVariable.typeOfImpact);
		add(typeOfImpact);
		add(new Label("Dark Matter Mass:"));
		darkMatterMass = new JFormattedTextField(HelperVariable.darkMatterMass);
		add(darkMatterMass);
		add(new Label("Darkk Matter Density:"));
		darkMatterDensity = new JFormattedTextField(
				HelperVariable.darkMatterDensity);
		add(darkMatterDensity);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				me.setVisible(false);
			}
		});
		add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					HelperVariable.scala = Double.parseDouble(me.scala
							.getValue().toString());
					HelperVariable.timeFactor = Double
							.parseDouble(me.timeFactor.getValue().toString());
					HelperVariable.numberOfObjects = Integer
							.parseInt(me.numberOfObjects.getValue().toString());
					HelperVariable.nebulaRadius = Double
							.parseDouble(me.nebulaRadius.getValue().toString());
					HelperVariable.dentityMin = Double
							.parseDouble(me.densiteMin.getValue().toString());
					HelperVariable.densityMax = Double
							.parseDouble(me.densiteMax.getValue().toString());
					HelperVariable.massObjectMin = Double
							.parseDouble(me.massObjectMin.getValue().toString());
					HelperVariable.massObjectMax = Double
							.parseDouble(me.massObjectMax.getValue().toString());
					HelperVariable.probFusion = Double
							.parseDouble(me.probFusion.getValue().toString());
					HelperVariable.typeOfImpact = Double
							.parseDouble(me.typeOfImpact.getValue().toString());
					HelperVariable.darkMatterMass = Double
							.parseDouble(me.darkMatterMass.getValue()
									.toString());
					HelperVariable.darkMatterDensity = Double
							.parseDouble(me.darkMatterDensity.getValue()
									.toString());
					me.getMother().reset();
					me.setVisible(false);

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

	public GUIProgram getMother() {
		return mother;
	}

}
