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
import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfImpact;
import com.sylvanoid.common.TypeOfUnivers;

public class GUIParam extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUIParam me;
	private GUIProgram mother;
	private JCheckBox parallelization;
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
	private JFormattedTextField matterDistribution;
	private JFormattedTextField numOfLowMassParticule;
	private JFormattedTextField nbArms;
	private JFormattedTextField ellipseRatio;
	private JFormattedTextField ellipseShiftRatio;
	private JFormattedTextField lowMassParticuleMass;
	private JFormattedTextField lowMassDensity;
	private JFormattedTextField gasDistribution;
	private JFormattedTextField darkMatterDistribution;
	private JComboBox<String> typeOfImpact;
	private JFormattedTextField darkMatterMass;
	private JFormattedTextField darkMatterDensity;
	private JFormattedTextField demiDistanceBetweenGalaxiesX;
	private JFormattedTextField demiDistanceBetweenGalaxiesY;
	private JFormattedTextField demiDistanceBetweenGalaxiesZ;
	private JCheckBox staticDarkMatter;
	private JCheckBox appliViscosity;
	private JFormattedTextField nebulaRadiusRatioForVolumicMass;
	private JFormattedTextField viscosityCoeff;

	private DecimalFormat dfsc = new DecimalFormat("0.####E0");
	private DecimalFormat fdpc = new DecimalFormat("0.####%");

	public GUIParam(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		setTitle("Parameters");
		setModal(true);
		int w = 1024;
		int h = 500;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(16, 4));
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
					typeOfImpact.setSelectedIndex(0);
					nebulaRadius.setValue(HelperVariable.UA * 10);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(0);
					lowMassDensity.setValue(0);
					gasDistribution.setValue(1);
					ellipseRatio.setValue(0.5);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 1:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetaryRandom);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(1E-9);
					numberOfObjects.setValue(20);
					typeOfImpact.setSelectedIndex(0);
					densiteMin.setValue(1);
					nebulaRadius.setValue(HelperVariable.UA * 5);
					massObjectMin.setValue(HelperVariable.M / 2E3);
					massObjectMax.setValue(HelperVariable.M / 1E3);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(HelperVariable.M);
					darkMatterDensity.setValue(1);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 2:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.Random);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY / 3);
					scala.setValue(5E-10);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(0.1);
					nebulaRadius.setValue(HelperVariable.UA * 5);
					massObjectMin.setValue(HelperVariable.M / 1E3);
					massObjectMax.setValue(HelperVariable.M / 1E2);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(0);
					darkMatterDensity.setValue(1E-12);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(-0.25);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 3:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUnivers);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-20);
					nebulaRadius.setValue(HelperVariable.PC * 3E4);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E2);
					lowMassDensity.setValue(1E-27);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-23);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.25);
					ellipseShiftRatio.setValue(-1.25);
					nbArms.setValue(2);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 4:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.GalaxiesCollision);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 5E6);
					scala.setValue(4E-20);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(500);
					densiteMin.setValue(1E-23);
					nebulaRadius.setValue(HelperVariable.PC * 2E5);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(5);
					negligeableMass.setValue(1E39);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E2);
					lowMassDensity.setValue(5E-30);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(1E42);
					darkMatterDensity.setValue(1E-20);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					demiDistanceBetweenGalaxiesX
							.setValue(200000 * HelperVariable.PC);
					demiDistanceBetweenGalaxiesY.setValue(0);
					demiDistanceBetweenGalaxiesZ.setValue(0);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(false);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 5:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.PlanetariesGenesis);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(5E-10);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(1000);
					densiteMin.setValue(1);
					nebulaRadius.setValue(HelperVariable.UA * 3);
					massObjectMin.setValue(HelperVariable.M / 1000000);
					massObjectMax.setValue(HelperVariable.M / 100000);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(HelperVariable.M);
					darkMatterDensity.setValue(1);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.95);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 6:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomRotateUniverCircular);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					typeOfImpact.setSelectedIndex(1);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-19);
					nebulaRadius.setValue(HelperVariable.PC * 4E4);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E4);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(5);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E6);
					lowMassDensity.setValue(1E-23);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-23);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				case 7:
					me.mother.getParameters().setTypeOfUnivers(
							TypeOfUnivers.RandomStaticSphericalUnivers);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					typeOfImpact.setSelectedIndex(1);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-19);
					nebulaRadius.setValue(HelperVariable.PC * 4E4);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E4);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 1E6);
					lowMassDensity.setValue(1E-23);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-23);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					appliViscosity.setSelected(false);
					nebulaRadiusRatioForVolumicMass.setValue(1.5);
					viscosityCoeff.setValue(1);
					break;
				}
				enableDisableParam();
			}
		});
		add(typeOfUnivers);

		add(new Label("Parallelization:"));
		parallelization = new JCheckBox();
		parallelization.setSelected(me.mother.getParameters()
				.isParallelization());
		add(parallelization);

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

		add(new Label("Matter distribution:"));
		matterDistribution = new JFormattedTextField(dfsc);
		matterDistribution.setValue(me.mother.getParameters()
				.getMatterDistribution());
		add(matterDistribution);

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

		add(new Label("Gas distribution:"));
		gasDistribution = new JFormattedTextField(dfsc);
		gasDistribution
				.setValue(me.mother.getParameters().getGasDistribution());
		add(gasDistribution);

		add(new Label("Dark Matter or central star Mass:"));
		darkMatterMass = new JFormattedTextField(dfsc);
		darkMatterMass.setValue(me.mother.getParameters().getDarkMatterMass());
		add(darkMatterMass);

		add(new Label("Dark Matter or central star Density:"));
		darkMatterDensity = new JFormattedTextField(dfsc);
		darkMatterDensity.setValue(me.mother.getParameters()
				.getDarkMatterDensity());
		add(darkMatterDensity);

		add(new Label("Dark Matterdistribution:"));
		darkMatterDistribution = new JFormattedTextField(dfsc);
		darkMatterDistribution.setValue(me.mother.getParameters()
				.getDarkMatterDistribution());
		add(darkMatterDistribution);

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
		manageImpact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeOfImpact.setEnabled(manageImpact.isSelected());
			}
		});
		add(manageImpact);

		add(new Label("Type Of Impact:"));
		typeOfImpact = new JComboBox<String>();
		for (TypeOfImpact toi : TypeOfImpact.values()) {
			typeOfImpact.addItem(toi.getLabel());
		}
		typeOfImpact.setSelectedItem(mother.getParameters().getTypeOfImpact()
				.getLabel());
		add(typeOfImpact);

		add(new Label("Static Dark Matter:"));
		staticDarkMatter = new JCheckBox();
		staticDarkMatter.setSelected(me.mother.getParameters()
				.isStaticDarkMatter());
		add(staticDarkMatter);

		add(new Label("Apply viscosity:"));
		appliViscosity = new JCheckBox();
		appliViscosity
				.setSelected(me.mother.getParameters().isAppliViscosity());
		appliViscosity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nebulaRadiusRatioForVolumicMass.setEnabled(appliViscosity.isSelected());
				viscosityCoeff.setEnabled(appliViscosity.isSelected());
			}
		});
		add(appliViscosity);
		
		add(new Label("Radius factor for search neighbors:"));
		nebulaRadiusRatioForVolumicMass = new JFormattedTextField(dfsc);
		nebulaRadiusRatioForVolumicMass.setValue(me.mother.getParameters()
				.getNebulaRadiusRatioForVolumicMass());
		add(nebulaRadiusRatioForVolumicMass);

		add(new Label("Viscosity Coeff:"));
		viscosityCoeff = new JFormattedTextField(dfsc);
		viscosityCoeff.setValue(me.mother.getParameters().getViscosityCoeff());
		add(viscosityCoeff);

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
					me.mother.getParameters().setParallelization(
							me.parallelization.isSelected());
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
					me.mother.getParameters().setMatterDistribution(
							Double.parseDouble(me.matterDistribution.getValue()
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
					me.mother.getParameters().setGasDistribution(
							Double.parseDouble(me.gasDistribution.getValue()
									.toString()));
					me.mother.getParameters().setManageImpact(
							me.manageImpact.isSelected());
					me.mother.getParameters().setTypeOfImpact(
							TypeOfImpact.values()[me.typeOfImpact
									.getSelectedIndex()]);
					me.mother.getParameters().setDarkMatterMass(
							Double.parseDouble(me.darkMatterMass.getValue()
									.toString()));
					me.mother.getParameters().setDarkMatterDensity(
							Double.parseDouble(me.darkMatterDensity.getValue()
									.toString()));
					me.mother.getParameters().setDarkMatterDistribution(
							Double.parseDouble(me.darkMatterDistribution
									.getValue().toString()));
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
					me.mother.getParameters().setStaticDarkMatter(
							me.staticDarkMatter.isSelected());
					me.mother.getParameters().setAppliViscosity(
							me.appliViscosity.isSelected());
					me.mother
							.getParameters()
							.setNebulaRadiusRatioForVolumicMass(
									Double.parseDouble(me.nebulaRadiusRatioForVolumicMass
											.getValue().toString()));
					me.mother.getParameters().setViscosityCoeff(
							Double.parseDouble(me.viscosityCoeff.getValue()
									.toString()));

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
		matterDistribution.setEnabled(false);
		negligeableMass.setEnabled(false);
		numOfLowMassParticule.setEnabled(false);
		lowMassParticuleMass.setEnabled(false);
		lowMassDensity.setEnabled(false);
		gasDistribution.setEnabled(false);
		typeOfImpact.setEnabled(manageImpact.isSelected());
		darkMatterMass.setEnabled(false);
		darkMatterDensity.setEnabled(false);
		darkMatterDistribution.setEnabled(false);
		ellipseRatio.setEnabled(false);
		ellipseShiftRatio.setEnabled(false);
		demiDistanceBetweenGalaxiesX.setEnabled(false);
		demiDistanceBetweenGalaxiesY.setEnabled(false);
		demiDistanceBetweenGalaxiesZ.setEnabled(false);
		nbArms.setEnabled(false);
		staticDarkMatter.setEnabled(false);
		appliViscosity.setEnabled(false);
		nebulaRadiusRatioForVolumicMass.setEnabled(appliViscosity.isSelected());
		viscosityCoeff.setEnabled(appliViscosity.isSelected());
		switch (typeOfUnivers.getSelectedIndex()) {
		case 0:
			// TypeOfUnivers.Planetary;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			break;
		case 1:
			// TypeOfUnivers.PlanetaryRandom;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			break;
		case 2:
			// TypeOfUnivers.Random;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		case 3:
			// TypeOfUnivers.RandomRotateUnivers;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterDistribution.setEnabled(true);
			ellipseRatio.setEnabled(true);
			ellipseShiftRatio.setEnabled(true);
			nbArms.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		case 4:
			// TypeOfUnivers.GalaxiesCollision;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			demiDistanceBetweenGalaxiesX.setEnabled(true);
			demiDistanceBetweenGalaxiesY.setEnabled(true);
			demiDistanceBetweenGalaxiesZ.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		case 5:
			// TypeOfUnivers.PlanetariesGenesis;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterMass.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		case 6:
			// TypeOfUnivers.RandomRotateUniversWithoutCentralMass;
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterDistribution.setEnabled(true);
			ellipseRatio.setEnabled(false);
			ellipseShiftRatio.setEnabled(false);
			nbArms.setEnabled(false);
			staticDarkMatter.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		case 7:
			// TypeOfUnivers.RandomStaticSphericalUnivers
			manageImpact.setEnabled(true);
			timeFactor.setEnabled(true);
			scala.setEnabled(true);
			numberOfObjects.setEnabled(true);
			densiteMin.setEnabled(true);
			nebulaRadius.setEnabled(true);
			massObjectMin.setEnabled(true);
			massObjectMax.setEnabled(true);
			matterDistribution.setEnabled(true);
			darkMatterMass.setEnabled(true);
			negligeableMass.setEnabled(true);
			numOfLowMassParticule.setEnabled(true);
			lowMassParticuleMass.setEnabled(true);
			lowMassDensity.setEnabled(true);
			gasDistribution.setEnabled(true);
			darkMatterDensity.setEnabled(true);
			darkMatterDistribution.setEnabled(true);
			ellipseRatio.setEnabled(false);
			ellipseShiftRatio.setEnabled(false);
			nbArms.setEnabled(false);
			staticDarkMatter.setEnabled(true);
			appliViscosity.setEnabled(true);
			break;
		}
	}
}
