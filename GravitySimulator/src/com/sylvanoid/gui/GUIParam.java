package com.sylvanoid.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.sylvanoid.common.TypeOfUnivers;

public class GUIParam extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUIParam me;
	private GUIProgram mother;
	private JComboBox<String> typeOfUnivers;
	private JFormattedTextField timeFactor;
	private JCheckBox manageImpact;
	private JFormattedTextField numberOfObjects;
	private JFormattedTextField nebulaRadius;
	private JFormattedTextField densiteMin;
	private JFormattedTextField massObjectMin;
	private JFormattedTextField massObjectMax;
	private JFormattedTextField expensionOfUnivers;
	private JCheckBox fusion;
	private JSlider typeOfImpact;
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

		NumberFormat displayFormat = NumberFormat.getNumberInstance();
		displayFormat.setMinimumFractionDigits(0);
		displayFormat.setMaximumFractionDigits(20);
		DefaultFormatterFactory ddf = new DefaultFormatterFactory(
				new NumberFormatter(displayFormat), new NumberFormatter(
						displayFormat));

		add(new Label("Type of Univers:"));
		typeOfUnivers = new JComboBox<String>();
		for (TypeOfUnivers tou : TypeOfUnivers.values()) {
			typeOfUnivers.addItem(tou.getLabel());
		}
		typeOfUnivers.setSelectedItem(mother.getParameters().getTypeOfUnivers()
				.getLabel());
		typeOfUnivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (typeOfUnivers.getSelectedIndex()) {
				case 0:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.Planetary);
					manageImpact.setSelected(false);
					timeFactor.setValue(5);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					expensionOfUnivers.setValue(0);
					break;
				case 1:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetaryRandom);
					manageImpact.setSelected(true);
					timeFactor.setValue(10);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(20);
					fusion.setSelected(true);
					densiteMin.setValue(30);
					nebulaRadius.setValue(500);
					massObjectMin.setValue(1E4);
					massObjectMax.setValue(1E6);
					darkMatterMass.setValue(1E10);
					darkMatterDensity.setValue(200);
					expensionOfUnivers.setValue(0);
					break;
				case 2:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.Random);
					manageImpact.setSelected(true);
					timeFactor.setValue(2);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(500);
					nebulaRadius.setValue(600);
					massObjectMin.setValue(100000000);
					massObjectMax.setValue(1000000000);
					expensionOfUnivers.setValue(0);
					break;
				case 3:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUnivers);
					manageImpact.setSelected(false);
					timeFactor.setValue(1);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(5000);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(1E6);
					massObjectMax.setValue(1E7);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1000);
					expensionOfUnivers.setValue(0);
					break;
				case 4:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.GalaxiesCollision);
					manageImpact.setSelected(false);
					timeFactor.setValue(1);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(500);
					densiteMin.setValue(5000);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(1000000);
					massObjectMax.setValue(10000000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1000);
					expensionOfUnivers.setValue(0);
					break;
				case 5:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetariesGenesis);
					manageImpact.setSelected(true);
					timeFactor.setValue(50);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(50);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(10000);
					massObjectMax.setValue(100000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					darkMatterMass.setValue(1E10);
					darkMatterDensity.setValue(200);
					expensionOfUnivers.setValue(0);
					break;

				case 6:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.DoubleStars);
					manageImpact.setSelected(true);
					timeFactor.setValue(5);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(100);
					nebulaRadius.setValue(200);
					massObjectMin.setValue(100000000);
					massObjectMax.setValue(100000000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					expensionOfUnivers.setValue(0);
					break;
				case 7:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomInitialExpension);
					manageImpact.setSelected(true);
					timeFactor.setValue(2);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(200);
					nebulaRadius.setValue(600);
					massObjectMin.setValue(100000000);
					massObjectMax.setValue(1000000000);
					expensionOfUnivers.setValue(0.00028);
					break;
				}
				enableDisableParam();
			}
		});
		add(typeOfUnivers);

		add(new Label("Time factor:"));
		timeFactor = new JFormattedTextField(ddf);
		timeFactor.setValue(me.mother.getParameters().getTimeFactor());
		add(timeFactor);

		add(new Label("Number of object:"));
		numberOfObjects = new JFormattedTextField(ddf);
		numberOfObjects
				.setValue(me.mother.getParameters().getNumberOfObjects());
		add(numberOfObjects);

		add(new Label("Nebula radius:"));
		nebulaRadius = new JFormattedTextField(ddf);
		nebulaRadius.setValue(me.mother.getParameters().getNebulaRadius());
		add(nebulaRadius);

		add(new Label("Density of objects:"));
		densiteMin = new JFormattedTextField(ddf);
		densiteMin.setValue(me.mother.getParameters().getDensity());
		add(densiteMin);

		add(new Label("Mass object min:"));
		massObjectMin = new JFormattedTextField(ddf);
		massObjectMin.setValue(me.mother.getParameters().getMassObjectMin());
		add(massObjectMin);

		add(new Label("Mass object max:"));
		massObjectMax = new JFormattedTextField(ddf);
		massObjectMax.setValue(me.mother.getParameters().getMassObjectMax());
		add(massObjectMax);

		add(new Label("Dark Matter or central star Mass:"));
		darkMatterMass = new JFormattedTextField(ddf);
		darkMatterMass.setValue(me.mother.getParameters().getDarkMatterMass());
		add(darkMatterMass);

		add(new Label("Dark Matter or central star Density:"));
		darkMatterDensity = new JFormattedTextField(ddf);
		darkMatterDensity.setValue(me.mother.getParameters()
				.getDarkMatterDensity());
		add(darkMatterDensity);

		add(new Label("Manage Impact:"));
		manageImpact = new JCheckBox();
		manageImpact.setSelected(me.mother.getParameters().isManageImpact());
		add(manageImpact);

		add(new Label("Expension of univers :"));
		expensionOfUnivers = new JFormattedTextField(ddf);
		expensionOfUnivers.setValue(me.mother.getParameters()
				.getExpensionOfUnivers());
		add(expensionOfUnivers);

		add(new Label("Fusion (or impact) :"));
		fusion = new JCheckBox();
		fusion.setSelected(me.mother.getParameters().isFusion());
		add(fusion);

		add(new Label("Type of impact (1-elastic 0-inelastic):"));
		typeOfImpact = new JSlider(0, 100, (int) (me.mother.getParameters()
				.getTypeOfImpact() * 100));
		add(typeOfImpact);

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
					me.mother.getParameters().setTimeFactor(
							Double.parseDouble(me.timeFactor.getValue()
									.toString()));
					me.mother.getParameters().setNumberOfObjects(
							Integer.parseInt(me.numberOfObjects.getValue()
									.toString()));
					me.mother.getParameters().setNebulaRadius(
							Double.parseDouble(me.nebulaRadius.getValue()
									.toString()));
					me.mother.getParameters().setDensity(
							Double.parseDouble(me.densiteMin.getValue()
									.toString()));
					me.mother.getParameters().setMassObjectMin(
							Double.parseDouble(me.massObjectMin.getValue()
									.toString()));
					me.mother.getParameters().setMassObjectMax(
							Double.parseDouble(me.massObjectMax.getValue()
									.toString()));
					me.mother.getParameters().setManageImpact(
							me.manageImpact.isSelected());
					me.mother.getParameters().setFusion(me.fusion.isSelected());
					me.mother.getParameters().setTypeOfImpact(
							(double) me.typeOfImpact.getValue() / 100);
					me.mother.getParameters().setDarkMatterMass(
							Double.parseDouble(me.darkMatterMass.getValue()
									.toString()));
					me.mother.getParameters().setDarkMatterDensity(
							Double.parseDouble(me.darkMatterDensity.getValue()
									.toString()));
					me.mother.getParameters().setExpensionOfUnivers(
							Double.parseDouble(me.expensionOfUnivers.getValue()
									.toString()));
					me.getMother().reset();
					me.setVisible(false);
					me.mother.setVisible(true);

				} catch (Exception em) {
					JDialog d = new JDialog();
					d.add(new Label("Error: " + em.getMessage()));
					d.setVisible(true);
					d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				}
			}
		});
		add(btnOK);

		enableDisableParam();
	}

	public GUIProgram getMother() {
		return mother;
	}

	private void enableDisableParam() {
		timeFactor.setEnabled(false);
		manageImpact.setEnabled(false);
		numberOfObjects.setEnabled(false);
		nebulaRadius.setEnabled(false);
		densiteMin.setEnabled(false);
		massObjectMin.setEnabled(false);
		massObjectMax.setEnabled(false);
		fusion.setEnabled(false);
		typeOfImpact.setEnabled(false);
		darkMatterMass.setEnabled(false);
		darkMatterDensity.setEnabled(false);
		expensionOfUnivers.setEnabled(false);
		switch (typeOfUnivers.getSelectedIndex()) {
		case 0:
			// TypeOfUnivers.Planetary;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 1:
			// TypeOfUnivers.PlanetaryRandom;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			fusion.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 2:
			// TypeOfUnivers.Random;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 3:
			// TypeOfUnivers.RandomRotateUnivers;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 4:
			// TypeOfUnivers.GalaxiesCollision;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 5:
			// TypeOfUnivers.PlanetariesGenesis;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 6:
			// TypeOfUnivers.DoubleStars;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			densiteMin.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 7:
			// TypeOfUnivers.RandomInitialExpension;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		}
	}

}
