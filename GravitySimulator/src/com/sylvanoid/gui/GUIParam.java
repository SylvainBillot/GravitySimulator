package com.sylvanoid.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;

import com.sylvanoid.common.HelperVariable;
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
	private JFormattedTextField scala;
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
	
	DecimalFormat dfsc = new DecimalFormat("0.####E0");
	
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
		setLayout(new GridLayout(15, 2));

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
					timeFactor.setValue(6000);
					scala.setValue(1E-9);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 1:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetaryRandom);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(1E-9);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(20);
					fusion.setSelected(true);
					densiteMin.setValue(1);
					nebulaRadius.setValue(HelperVariable.UA*5);
					massObjectMin.setValue(HelperVariable.M/1000);
					massObjectMax.setValue(HelperVariable.M/100);
					darkMatterMass.setValue(HelperVariable.M);
					darkMatterDensity.setValue(1);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 2:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.Random);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY/3);
					scala.setValue(5E-10);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(0.1);
					nebulaRadius.setValue(HelperVariable.UA*5);
					massObjectMin.setValue(HelperVariable.M/1000);
					massObjectMax.setValue(HelperVariable.M/100);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 3:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUnivers);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEDAY*365*1000000);
					scala.setValue(4E-19);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1E-21);
					nebulaRadius.setValue(HelperVariable.PC*30000);
					massObjectMin.setValue(HelperVariable.MINIMALSTARMASS*1000);
					massObjectMax.setValue(HelperVariable.MAXIMALSTARMASS*1000);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-20);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 4:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.GalaxiesCollision);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEDAY*365*1000000);
					scala.setValue(1E-19);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(500);
					densiteMin.setValue(1E-21);
					nebulaRadius.setValue(HelperVariable.PC*30000);
					massObjectMin.setValue(HelperVariable.MINIMALSTARMASS*1000);
					massObjectMax.setValue(HelperVariable.MAXIMALSTARMASS*1000);
					darkMatterMass.setValue(5E42);
					darkMatterDensity.setValue(1E-20);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 5:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetariesGenesis);
					manageImpact.setSelected(true);
					timeFactor.setValue(50);
					scala.setValue(1);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1E5);
					nebulaRadius.setValue(300);
					massObjectMin.setValue(10000);
					massObjectMax.setValue(100000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					darkMatterMass.setValue(1E10);
					darkMatterDensity.setValue(1E7);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;

				case 6:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.DoubleStars);
					manageImpact.setSelected(true);
					timeFactor.setValue(5);
					scala.setValue(1);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1E6);
					nebulaRadius.setValue(200);
					massObjectMin.setValue(100000000);
					massObjectMax.setValue(100000000);
					darkMatterMass.setValue(1E12);
					darkMatterDensity.setValue(1E10);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				case 7:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomInitialExpension);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY*1E9);
					scala.setValue(1E-20);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1E-22);
					nebulaRadius.setValue(HelperVariable.PC*1E6);
					massObjectMin.setValue(HelperVariable.M*1E2);
					massObjectMax.setValue(HelperVariable.M*1E10);
					expensionOfUnivers.setValue(HelperVariable.H0);
					break;
				}
				enableDisableParam();
			}
		});
		add(typeOfUnivers);

		add(new Label("Scala 1/x:"));
		scala = new JFormattedTextField(dfsc);
		scala.setValue(me.mother.getParameters().getScala());
		add(scala);
		
		add(new Label("Time factor:"));
		timeFactor = new JFormattedTextField(dfsc);
		timeFactor.setValue(me.mother.getParameters().getTimeFactor());
		add(timeFactor);

		add(new Label("Number of object:"));
		numberOfObjects = new JFormattedTextField(dfsc);
		numberOfObjects
				.setValue(me.mother.getParameters().getNumberOfObjects());
		add(numberOfObjects);

		add(new Label("Nebula radius:"));
		nebulaRadius = new JFormattedTextField(dfsc);
		nebulaRadius.setValue(me.mother.getParameters().getNebulaRadius());
		add(nebulaRadius);

		add(new Label("Density of objects:"));
		densiteMin = new JFormattedTextField(dfsc);
		densiteMin.setValue(me.mother.getParameters().getDensity());
		add(densiteMin);

		add(new Label("Mass object min:"));
		massObjectMin = new JFormattedTextField(dfsc);
		massObjectMin.setValue(me.mother.getParameters().getMassObjectMin());
		add(massObjectMin);

		add(new Label("Mass object max:"));
		massObjectMax = new JFormattedTextField(dfsc);
		massObjectMax.setValue(me.mother.getParameters().getMassObjectMax());
		add(massObjectMax);

		add(new Label("Dark Matter or central star Mass:"));
		darkMatterMass = new JFormattedTextField(dfsc);
		darkMatterMass.setValue(me.mother.getParameters().getDarkMatterMass());
		add(darkMatterMass);

		add(new Label("Dark Matter or central star Density:"));
		darkMatterDensity = new JFormattedTextField(dfsc);
		darkMatterDensity.setValue(me.mother.getParameters()
				.getDarkMatterDensity());
		add(darkMatterDensity);

		add(new Label("Manage Impact:"));
		manageImpact = new JCheckBox();
		manageImpact.setSelected(me.mother.getParameters().isManageImpact());
		add(manageImpact);

		add(new Label("Expension of univers :"));
		expensionOfUnivers = new JFormattedTextField(dfsc);
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
					me.mother.getParameters().setScala(
							Double.parseDouble(me.scala.getValue()
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
		scala.setEnabled(false);
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
			scala.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 1:
			// TypeOfUnivers.PlanetaryRandom;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
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
			scala.setEnabled(true);
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
			scala.setEnabled(true);
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
			scala.setEnabled(true);
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
			scala.setEnabled(true);
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
			scala.setEnabled(true);
			fusion.setEnabled(true);
			typeOfImpact.setEnabled(true);
			densiteMin.setEnabled(true);
			expensionOfUnivers.setEnabled(true);
			break;
		case 7:
			// TypeOfUnivers.RandomInitialExpension;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
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
