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
import javax.vecmath.Vector3d;

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
	private JFormattedTextField negligeableMass;
	private JFormattedTextField numOfLowMassParticule;
	private JFormattedTextField nbArms;
	private JFormattedTextField ellipseRatio;
	private JFormattedTextField ellipseShiftRatio;
	private JFormattedTextField lowMassParticuleMass;
	private JFormattedTextField lowMassDensity;
	private JCheckBox fusion;
	private JSlider typeOfImpact;
	private JFormattedTextField darkMatterMass;
	private JFormattedTextField darkMatterDensity;
	private JFormattedTextField demiDistanceBetweenGalaxiesX;
	private JFormattedTextField demiDistanceBetweenGalaxiesY;
	private JFormattedTextField demiDistanceBetweenGalaxiesZ;

	private DecimalFormat dfsc = new DecimalFormat("0.####E0");
	private DecimalFormat fdpc = new DecimalFormat("0.####%");

	public GUIParam(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		setTitle("Parameters");
		setModal(true);
		int w = 1024;
		int h = 400;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(12, 4));

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
					timeFactor.setValue(6E3);
					scala.setValue(1E-9);
					fusion.setSelected(true);
					nebulaRadius.setValue(HelperVariable.UA * 10);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(0);
					lowMassDensity.setValue(0);
					typeOfImpact.setValue(100);
					ellipseRatio.setValue(0.5);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
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
					nebulaRadius.setValue(HelperVariable.UA * 5);
					massObjectMin.setValue(HelperVariable.M / 2E3);
					massObjectMax.setValue(HelperVariable.M / 1E3);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					darkMatterMass.setValue(HelperVariable.M);
					darkMatterDensity.setValue(1);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
					break;
				case 2:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.Random);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY / 3);
					scala.setValue(5E-10);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(0.1);
					nebulaRadius.setValue(HelperVariable.UA * 5);
					massObjectMin.setValue(HelperVariable.M / 1E3);
					massObjectMax.setValue(HelperVariable.M / 1E2);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					darkMatterMass.setValue(0);
					darkMatterDensity.setValue(1E-12);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
					break;
				case 3:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUnivers);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-20);
					nebulaRadius.setValue(HelperVariable.PC * 3E4);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E2);
					lowMassDensity.setValue(1E-27);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-23);
					ellipseRatio.setValue(0.25);
					ellipseShiftRatio.setValue(-1.25);
					nbArms.setValue(2);
					break;
				case 4:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.GalaxiesCollision);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 5E6);
					scala.setValue(4E-20);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(500);
					densiteMin.setValue(1E-23);
					nebulaRadius.setValue(HelperVariable.PC * 2E5);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					negligeableMass.setValue(1E39);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E2);
					lowMassDensity.setValue(5E-30);
					darkMatterMass.setValue(1E42);
					darkMatterDensity.setValue(1E-20);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					demiDistanceBetweenGalaxiesX
							.setValue(200000 * HelperVariable.PC);
					demiDistanceBetweenGalaxiesY.setValue(0);
					demiDistanceBetweenGalaxiesZ.setValue(0);
					nbArms.setValue(3);
					break;
				case 5:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetariesGenesis);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(5E-10);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1);
					nebulaRadius.setValue(HelperVariable.UA * 3);
					massObjectMin.setValue(HelperVariable.M / 1000000);
					massObjectMax.setValue(HelperVariable.M / 100000);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					darkMatterMass.setValue(HelperVariable.M);
					darkMatterDensity.setValue(1);
					ellipseRatio.setValue(0.95);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					break;
				case 6:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUniverCircular);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					fusion.setSelected(true);
					typeOfImpact.setValue(100);
					numberOfObjects.setValue(0);
					densiteMin.setValue(1E-19);
					nebulaRadius.setValue(HelperVariable.PC * 4E4);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E2);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E6);
					lowMassDensity.setValue(1E-24);
					darkMatterMass.setValue(5E39);
					darkMatterDensity.setValue(1E-23);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
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

		add(new Label("Nb galactic Arms:"));
		nbArms = new JFormattedTextField(dfsc);
		nbArms.setValue(me.mother.getParameters().getNbARms());
		add(nbArms);

		add(new Label("Ellipses eccentricity (]0-1[):"));
		ellipseRatio = new JFormattedTextField(fdpc);
		ellipseRatio.setValue(me.mother.getParameters().getEllipseRatio());
		add(ellipseRatio);

		add(new Label("Ellipses shift (ratio of nebula radius):"));
		ellipseShiftRatio = new JFormattedTextField(dfsc);
		ellipseShiftRatio.setValue(me.mother.getParameters()
				.getEllipseShiftRatio());
		add(ellipseShiftRatio);

		add(new Label("Negligeable mass:"));
		negligeableMass = new JFormattedTextField(dfsc);
		negligeableMass
				.setValue(me.mother.getParameters().getNegligeableMass());
		add(negligeableMass);

		add(new Label("Num of gas particle:"));
		numOfLowMassParticule = new JFormattedTextField(dfsc);
		numOfLowMassParticule.setValue(me.mother.getParameters()
				.getNumOfLowMassParticule());
		add(numOfLowMassParticule);

		add(new Label("Gas particles max mass:"));
		lowMassParticuleMass = new JFormattedTextField(dfsc);
		lowMassParticuleMass.setValue(me.mother.getParameters()
				.getLowMassParticuleMass());
		add(lowMassParticuleMass);

		add(new Label("Gas particle density:"));
		lowMassDensity = new JFormattedTextField(dfsc);
		lowMassDensity.setValue(me.mother.getParameters().getLowMassDensity());
		add(lowMassDensity);

		add(new Label("Dark Matter or central star Mass:"));
		darkMatterMass = new JFormattedTextField(dfsc);
		darkMatterMass.setValue(me.mother.getParameters().getDarkMatterMass());
		add(darkMatterMass);

		add(new Label("Dark Matter or central star Density:"));
		darkMatterDensity = new JFormattedTextField(dfsc);
		darkMatterDensity.setValue(me.mother.getParameters()
				.getDarkMatterDensity());
		add(darkMatterDensity);

		add(new Label("Demi Distance between Galaxies X :"));
		demiDistanceBetweenGalaxiesX = new JFormattedTextField(dfsc);
		demiDistanceBetweenGalaxiesX.setValue(me.mother.getParameters()
				.getDemiDistanceBetweenGalaxies().x);
		add(demiDistanceBetweenGalaxiesX);

		add(new Label("Demi Distance between Galaxies Y :"));
		demiDistanceBetweenGalaxiesY = new JFormattedTextField(dfsc);
		demiDistanceBetweenGalaxiesY.setValue(me.mother.getParameters()
				.getDemiDistanceBetweenGalaxies().y);
		add(demiDistanceBetweenGalaxiesY);

		add(new Label("Demi Distance between Galaxies Z :"));
		demiDistanceBetweenGalaxiesZ = new JFormattedTextField(dfsc);
		demiDistanceBetweenGalaxiesZ.setValue(me.mother.getParameters()
				.getDemiDistanceBetweenGalaxies().z);
		add(demiDistanceBetweenGalaxiesZ);

		add(new Label("Manage Impact:"));
		manageImpact = new JCheckBox();
		manageImpact.setSelected(me.mother.getParameters().isManageImpact());
		add(manageImpact);

		add(new Label("Fusion (or friction) :"));
		fusion = new JCheckBox();
		fusion.setSelected(me.mother.getParameters().isFusion());
		add(fusion);

		add(new Label("Friction coeff :"));
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
							Double.parseDouble(me.scala.getValue().toString()));
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
					me.mother.getParameters().setNbARms(
							Integer.parseInt(me.nbArms.getValue().toString()));
					me.mother.getParameters().setEllipseRatio(
							Double.parseDouble(me.ellipseRatio.getValue()
									.toString()));
					me.mother.getParameters().setEllipseShiftRatio(
							Double.parseDouble(me.ellipseShiftRatio.getValue()
									.toString()));
					me.mother.getParameters().setNegligeableMass(
							Double.parseDouble(me.negligeableMass.getValue()
									.toString()));
					me.mother.getParameters().setNumOfLowMassParticule(
							Integer.parseInt(me.numOfLowMassParticule
									.getValue().toString()));
					me.mother.getParameters().setLowMassParticuleMass(
							Double.parseDouble(me.lowMassParticuleMass
									.getValue().toString()));
					me.mother.getParameters().setLowMassDensity(
							Double.parseDouble(me.lowMassDensity.getValue()
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
					me.mother
							.getParameters()
							.setDemiDistanceBetweenGalaxies(
									new Vector3d(
											Double.parseDouble(me.demiDistanceBetweenGalaxiesX
													.getValue().toString()),
											Double.parseDouble(me.demiDistanceBetweenGalaxiesY
													.getValue().toString()),
											Double.parseDouble(me.demiDistanceBetweenGalaxiesZ
													.getValue().toString())));

					switch (typeOfUnivers.getSelectedIndex()) {
					case 0:
						// TypeOfUnivers.Planetary;
						me.mother.getParameters().setStaticDarkMatter(false);
						break;
					case 1:
						// TypeOfUnivers.PlanetaryRandom;
						me.mother.getParameters().setStaticDarkMatter(false);
						break;
					case 2:
						// TypeOfUnivers.Random;
						me.mother.getParameters().setStaticDarkMatter(true);
						break;
					case 3:
						// TypeOfUnivers.RandomRotateUnivers;
						me.mother.getParameters().setStaticDarkMatter(true);
						break;
					case 4:
						// TypeOfUnivers.GalaxiesCollision;
						me.mother.getParameters().setStaticDarkMatter(false);
						break;
					case 5:
						// TypeOfUnivers.PlanetariesGenesis;
						me.mother.getParameters().setStaticDarkMatter(false);
						break;
					case 6:
						// TypeOfUnivers.RandomRotateUniversWithoutCentralMass;
						me.mother.getParameters().setStaticDarkMatter(true);
						break;
					}

					me.mother.getParameters().setEyes(new Vector3d(0, 0, 900));
					me.mother.getParameters().setLookAt(
							new Vector3d(0, 0, -900));
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
		negligeableMass.setEnabled(false);
		numOfLowMassParticule.setEnabled(false);
		lowMassParticuleMass.setEnabled(false);
		lowMassDensity.setEnabled(false);
		fusion.setEnabled(false);
		typeOfImpact.setEnabled(false);
		darkMatterMass.setEnabled(false);
		darkMatterDensity.setEnabled(false);
		ellipseRatio.setEnabled(false);
		ellipseShiftRatio.setEnabled(false);
		demiDistanceBetweenGalaxiesX.setEnabled(false);
		demiDistanceBetweenGalaxiesY.setEnabled(false);
		demiDistanceBetweenGalaxiesZ.setEnabled(false);
		nbArms.setEnabled(false);
		switch (typeOfUnivers.getSelectedIndex()) {
		case 0:
			// TypeOfUnivers.Planetary;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			break;
		case 1:
			// TypeOfUnivers.PlanetaryRandom;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			fusion.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			break;
		case 2:
			// TypeOfUnivers.Random;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			break;
		case 3:
			// TypeOfUnivers.RandomRotateUnivers;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			ellipseRatio.setEnabled(true);
			ellipseShiftRatio.setEnabled(true);
			nbArms.setEnabled(true);
			break;
		case 4:
			// TypeOfUnivers.GalaxiesCollision;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			demiDistanceBetweenGalaxiesX.setEnabled(true);
			demiDistanceBetweenGalaxiesY.setEnabled(true);
			demiDistanceBetweenGalaxiesZ.setEnabled(true);

			break;
		case 5:
			// TypeOfUnivers.PlanetariesGenesis;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			break;
		case 6:
			// TypeOfUnivers.RandomRotateUniversWithoutCentralMass;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			fusion.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			darkMatterMass.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			ellipseRatio.setEnabled(false);
			ellipseShiftRatio.setEnabled(false);
			nbArms.setEnabled(false);
			break;
		}
	}

}
