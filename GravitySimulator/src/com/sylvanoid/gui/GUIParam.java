package com.sylvanoid.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfImpact;
import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.joblib.Parameters;

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
	private JFormattedTextField darkMatterXRatio;
	private JFormattedTextField darkMatterYRatio;
	private JFormattedTextField darkMatterZRatio;
	private JComboBox<String> typeOfImpact;
	private JFormattedTextField darkMatterMass;
	private JFormattedTextField darkMatterDensity;
	private JFormattedTextField collisionDistanceRatio;
	private JFormattedTextField matterViscosity;
	private JFormattedTextField gasViscosity;
	private JFormattedTextField viscoElasticity;
	private JFormattedTextField viscoElasticityNear;
	private JFormattedTextField pressureZero;
	private JCheckBox recoverFrictionEnergy;
	private JFormattedTextField recoverFrictionEnergyRatio;
	private JCheckBox staticDarkMatter;
	private JCheckBox expansionUnivers;
	private JFormattedTextField timeMultiplicator;
	private JCheckBox visousDarkMatter;

	private DecimalFormat dfsc = new DecimalFormat("0.####E0");
	private DecimalFormat fdpc = new DecimalFormat("0.####%");

	private Parameters parameters;

	public GUIParam(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		this.parameters = mother.getParameters();
		builder(mother);
		enableDisableParam();
	}

	public GUIProgram getMother() {
		return mother;
	}

	private void builder(GUIProgram mother) {
		setTitle("Parameters");
		setModal(true);
		int w = 1024;
		int h = 520;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(18, 4));
		add(new JLabel("Type of Univers:"));
		typeOfUnivers = new JComboBox<String>();
		for (TypeOfUnivers tou : TypeOfUnivers.values()) {
			typeOfUnivers.addItem(tou.getLabel());
		}
		typeOfUnivers.setSelectedItem(parameters.getTypeOfUnivers().getLabel());
		typeOfUnivers.addActionListener(chooseTypeOfUnivers());
		add(typeOfUnivers);

		add(new JLabel("Parallelization:"));
		parallelization = new JCheckBox();
		parallelization.setSelected(parameters.isParallelization());
		add(parallelization);

		add(new JLabel("Scala 1/x:"));
		scala = new JFormattedTextField(dfsc);
		scala.setValue(parameters.getScala());
		add(scala);

		add(new JLabel("Time factor:"));
		timeFactor = new JFormattedTextField(dfsc);
		timeFactor.setValue(parameters.getTimeFactor());
		add(timeFactor);

		add(new JLabel("Number of object:"));
		numberOfObjects = new JFormattedTextField(dfsc);
		numberOfObjects.setValue(parameters.getNumberOfObjects());
		add(numberOfObjects);

		add(new JLabel("Nebula radius:"));
		nebulaRadius = new JFormattedTextField(dfsc);
		nebulaRadius.setValue(parameters.getNebulaRadius());
		add(nebulaRadius);

		add(new JLabel("Density of objects:"));
		densiteMin = new JFormattedTextField(dfsc);
		densiteMin.setValue(parameters.getDensity());
		add(densiteMin);

		add(new JLabel("Mass object:"));
		JPanel massObject = new JPanel();
		massObject.setLayout(new GridLayout(1, 4));
		add(massObject);
		massObjectMin = new JFormattedTextField(dfsc);
		massObjectMin.setValue(parameters.getMassObjectMin());
		massObject.add(new JLabel("Min:"));
		massObject.add(massObjectMin);
		massObjectMax = new JFormattedTextField(dfsc);
		massObjectMax.setValue(parameters.getMassObjectMax());
		massObject.add(new JLabel("Max:"));
		massObject.add(massObjectMax);

		add(new JLabel("Matter distribution:"));
		matterDistribution = new JFormattedTextField(dfsc);
		matterDistribution.setValue(parameters.getMatterDistribution());
		add(matterDistribution);

		add(new JLabel("Nb galactic Arms:"));
		nbArms = new JFormattedTextField(dfsc);
		nbArms.setValue(parameters.getNbARms());
		add(nbArms);

		add(new JLabel("Ellipses eccentricity (]0-1[):"));
		ellipseRatio = new JFormattedTextField(fdpc);
		ellipseRatio.setValue(parameters.getEllipseRatio());
		add(ellipseRatio);

		add(new JLabel("Ellipses shift (ratio of nebula radius):"));
		ellipseShiftRatio = new JFormattedTextField(dfsc);
		ellipseShiftRatio.setValue(parameters.getEllipseShiftRatio());
		add(ellipseShiftRatio);

		add(new JLabel("Negligeable mass:"));
		negligeableMass = new JFormattedTextField(dfsc);
		negligeableMass.setValue(parameters.getNegligeableMass());
		add(negligeableMass);

		add(new JLabel("Num of gas particle:"));
		numOfLowMassParticule = new JFormattedTextField(dfsc);
		numOfLowMassParticule.setValue(parameters.getNumOfLowMassParticule());
		add(numOfLowMassParticule);

		add(new JLabel("Gas particles max mass:"));
		lowMassParticuleMass = new JFormattedTextField(dfsc);
		lowMassParticuleMass.setValue(parameters.getLowMassParticuleMass());
		add(lowMassParticuleMass);

		add(new JLabel("Gas particle density:"));
		lowMassDensity = new JFormattedTextField(dfsc);
		lowMassDensity.setValue(parameters.getLowMassDensity());
		add(lowMassDensity);

		add(new JLabel("Gas distribution:"));
		gasDistribution = new JFormattedTextField(dfsc);
		gasDistribution.setValue(parameters.getGasDistribution());
		add(gasDistribution);

		add(new JLabel("Dark Matter or central star Mass:"));
		darkMatterMass = new JFormattedTextField(dfsc);
		darkMatterMass.setValue(parameters.getDarkMatterMass());
		add(darkMatterMass);

		add(new JLabel("Dark Matter or central star Density:"));
		darkMatterDensity = new JFormattedTextField(dfsc);
		darkMatterDensity.setValue(parameters.getDarkMatterDensity());
		add(darkMatterDensity);

		add(new JLabel("Dark Matterdistribution:"));
		darkMatterDistribution = new JFormattedTextField(dfsc);
		darkMatterDistribution.setValue(parameters.getDarkMatterDistribution());
		add(darkMatterDistribution);

		add(new JLabel("Dark matter xyz ratio"));
		JPanel darkMatterRatio = new JPanel();
		add(darkMatterRatio);
		darkMatterRatio.setLayout(new GridLayout(1, 6));
		darkMatterXRatio = new JFormattedTextField(dfsc);
		darkMatterXRatio.setValue(parameters.getDarkMatterXYZRatio().x);
		darkMatterRatio.add(new JLabel("X:"));
		darkMatterRatio.add(darkMatterXRatio);

		darkMatterYRatio = new JFormattedTextField(dfsc);
		darkMatterYRatio.setValue(parameters.getDarkMatterXYZRatio().y);
		darkMatterRatio.add(new JLabel("Y:"));
		darkMatterRatio.add(darkMatterYRatio);

		darkMatterZRatio = new JFormattedTextField(dfsc);
		darkMatterZRatio.setValue(parameters.getDarkMatterXYZRatio().z);
		darkMatterRatio.add(new JLabel("Z:"));
		darkMatterRatio.add(darkMatterZRatio);

		add(new JLabel("Manage Impact:"));
		manageImpact = new JCheckBox();
		manageImpact.setSelected(parameters.isManageImpact());
		manageImpact.addActionListener(chooseManageImpact());
		add(manageImpact);

		add(new JLabel("Type Of Impact:"));
		typeOfImpact = new JComboBox<String>();
		for (TypeOfImpact toi : TypeOfImpact.values()) {
			typeOfImpact.addItem(toi.getLabel());
		}
		typeOfImpact.setSelectedItem(parameters.getTypeOfImpact().getLabel());
		add(typeOfImpact);

		add(new JLabel("Collision distance ratio rij:"));
		collisionDistanceRatio = new JFormattedTextField(dfsc);
		collisionDistanceRatio.setValue(parameters.getCollisionDistanceRatio());
		add(collisionDistanceRatio);

		add(new JLabel("Matter Viscosity ratio:"));
		matterViscosity = new JFormattedTextField(dfsc);
		matterViscosity.setValue(parameters.getMatterViscosity());
		add(matterViscosity);

		add(new JLabel("Gas Viscosity ratio:"));
		gasViscosity = new JFormattedTextField(dfsc);
		gasViscosity.setValue(parameters.getGasViscosity());
		add(gasViscosity);

		add(new JLabel("Visco elasticity:"));
		viscoElasticity = new JFormattedTextField(dfsc);
		viscoElasticity.setValue(parameters.getViscoElasticity());
		add(viscoElasticity);

		add(new JLabel("Visco elasticity Near:"));
		viscoElasticityNear = new JFormattedTextField(dfsc);
		viscoElasticityNear.setValue(parameters.getViscoElasticityNear());
		add(viscoElasticityNear);

		add(new JLabel("Rest of Presure:"));
		pressureZero = new JFormattedTextField(dfsc);
		pressureZero.setValue(parameters.getPressureZero());
		add(pressureZero);

		add(new JLabel("Recover Friction Energy (Experimental):"));
		recoverFrictionEnergy = new JCheckBox();
		recoverFrictionEnergy.setSelected(parameters.isRecoverFrictionEnegy());
		recoverFrictionEnergy.addActionListener(chooseRecoverEnergy());

		add(recoverFrictionEnergy);

		add(new JLabel("Recover Friction Energy Ratio:"));
		recoverFrictionEnergyRatio = new JFormattedTextField(dfsc);
		recoverFrictionEnergyRatio.setValue(parameters
				.getRecoverFrictionEnergyRatio());
		add(recoverFrictionEnergyRatio);

		add(new JLabel("Static Dark Matter:"));
		staticDarkMatter = new JCheckBox();
		staticDarkMatter.setSelected(parameters.isStaticDarkMatter());
		add(staticDarkMatter);

		add(new JLabel("Expansion Of Univers:"));
		expansionUnivers = new JCheckBox();
		expansionUnivers.setSelected(parameters.isExpansionUnivers());
		add(expansionUnivers);

		add(new JLabel("Time multiplicator:"));
		timeMultiplicator = new JFormattedTextField(dfsc);
		timeMultiplicator.setValue(parameters.getTimeMultiplicator());
		add(timeMultiplicator);

		add(new JLabel("Viscous Dark Matter:"));
		visousDarkMatter = new JCheckBox();
		visousDarkMatter.setSelected(parameters.isViscousDarkMatter());
		add(visousDarkMatter);


		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(cancelAction());
		add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(okAction());
		add(btnOK);
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
		collisionDistanceRatio.setEnabled(manageImpact.isSelected());
		matterViscosity.setEnabled(manageImpact.isSelected());
		gasViscosity.setEnabled(manageImpact.isSelected());
		viscoElasticity.setEnabled(manageImpact.isSelected());
		viscoElasticityNear.setEnabled(manageImpact.isSelected());
		pressureZero.setEnabled(manageImpact.isSelected());
		recoverFrictionEnergy.setEnabled(manageImpact.isSelected());
		recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected()
				&& recoverFrictionEnergy.isSelected());
		darkMatterMass.setEnabled(false);
		darkMatterDensity.setEnabled(false);
		darkMatterDistribution.setEnabled(false);
		ellipseRatio.setEnabled(false);
		ellipseShiftRatio.setEnabled(false);
		nbArms.setEnabled(false);
		staticDarkMatter.setEnabled(false);
		expansionUnivers.setEnabled(false);
		darkMatterXRatio.setEnabled(false);
		darkMatterYRatio.setEnabled(false);
		darkMatterZRatio.setEnabled(false);
		timeMultiplicator.setEnabled(false);
		visousDarkMatter.setEnabled(false);
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
			timeMultiplicator.setEnabled(true);
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
			timeMultiplicator.setEnabled(true);
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
			expansionUnivers.setEnabled(true);
			darkMatterXRatio.setEnabled(true);
			darkMatterYRatio.setEnabled(true);
			darkMatterZRatio.setEnabled(true);
			timeMultiplicator.setEnabled(true);
			visousDarkMatter.setEnabled(true);
			break;
		case 4:
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
			timeMultiplicator.setEnabled(true);
			break;
		case 5:
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
			expansionUnivers.setEnabled(true);
			darkMatterXRatio.setEnabled(true);
			darkMatterYRatio.setEnabled(true);
			darkMatterZRatio.setEnabled(true);
			timeMultiplicator.setEnabled(true);
			visousDarkMatter.setEnabled(true);
			break;
		case 6:
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
			expansionUnivers.setEnabled(true);
			darkMatterXRatio.setEnabled(true);
			darkMatterYRatio.setEnabled(true);
			darkMatterZRatio.setEnabled(true);
			timeMultiplicator.setEnabled(true);
			visousDarkMatter.setEnabled(true);
			break;
		}
	}

	private ActionListener chooseTypeOfUnivers() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (typeOfUnivers.getSelectedIndex()) {
				case 0:
					// TypeOfUnivers.Planetary;
					parameters.setTypeOfUnivers(TypeOfUnivers.Planetary);
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
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 1:
					// TypeOfUnivers.PlanetaryRandom;
					parameters.setTypeOfUnivers(TypeOfUnivers.PlanetaryRandom);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(1E-9);
					numberOfObjects.setValue(20);
					typeOfImpact.setSelectedIndex(0);
					densiteMin.setValue(1E-2);
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
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 2:
					parameters.setTypeOfUnivers(TypeOfUnivers.Random);
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
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 3:
					parameters
							.setTypeOfUnivers(TypeOfUnivers.RandomRotateUnivers);
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
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 4: // Planetary genesis
					parameters
							.setTypeOfUnivers(TypeOfUnivers.PlanetariesGenesis);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEDAY);
					scala.setValue(5E-10);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-3);
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
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 5:
					parameters
							.setTypeOfUnivers(TypeOfUnivers.RandomRotateUniverCircular);
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
					lowMassDensity.setValue(5E-24);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(1E40);
					darkMatterDensity.setValue(1E-24);
					darkMatterDistribution.setValue(5);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(true);
					expansionUnivers.setEnabled(false);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1E-11);
					viscoElasticityNear.setValue(1E-11);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					break;
				case 6:
					parameters
							.setTypeOfUnivers(TypeOfUnivers.RandomStaticSphericalUnivers);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-24);
					typeOfImpact.setSelectedIndex(1);
					numberOfObjects.setValue(0);
					densiteMin.setValue(1E-19);
					nebulaRadius.setValue(HelperVariable.PC * 5E9);
					massObjectMin
							.setValue(HelperVariable.MINIMALSTARMASS * 1E5);
					massObjectMax
							.setValue(HelperVariable.MAXIMALSTARMASS * 1E4);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass
							.setValue(HelperVariable.MINIMALSTARMASS * 2.5E20);
					lowMassDensity.setValue(1E-24);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(2.59E53);
					darkMatterDensity.setValue(1E-24);
					darkMatterDistribution.setValue(1);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(false);
					expansionUnivers.setSelected(true);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1);
					gasViscosity.setValue(1);
					viscoElasticity.setValue(1E-11);
					viscoElasticityNear.setValue(1E-11);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(0.2);
					darkMatterYRatio.setValue(0.2);
					darkMatterZRatio.setValue(0.2);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(true);
					break;
				}
				enableDisableParam();
			}
		};
	}

	private ActionListener chooseManageImpact() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeOfImpact.setEnabled(manageImpact.isSelected());
				collisionDistanceRatio.setEnabled(manageImpact.isSelected());
				matterViscosity.setEnabled(manageImpact.isSelected());
				gasViscosity.setEnabled(manageImpact.isSelected());
				viscoElasticity.setEnabled(manageImpact.isSelected());
				viscoElasticityNear.setEnabled(manageImpact.isSelected());
				pressureZero.setEnabled(manageImpact.isSelected());
				recoverFrictionEnergy.setEnabled(manageImpact.isSelected());
				recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected()
						&& recoverFrictionEnergy.isSelected());
			}
		};
	}

	private ActionListener chooseRecoverEnergy() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected()
						&& recoverFrictionEnergy.isSelected());
			}
		};
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
					parameters.setParallelization(me.parallelization
							.isSelected());
					parameters.setTimeFactor(Double.parseDouble(me.timeFactor
							.getValue().toString()));
					parameters.setScala(Double.parseDouble(me.scala.getValue()
							.toString()));
					parameters
							.setNumberOfObjects(Integer
									.parseInt(me.numberOfObjects.getValue()
											.toString()));
					parameters
							.setNebulaRadius(Double.parseDouble(me.nebulaRadius
									.getValue().toString()));
					parameters.setDensity(Double.parseDouble(me.densiteMin
							.getValue().toString()));
					parameters
							.setMassObjectMin(Double
									.parseDouble(me.massObjectMin.getValue()
											.toString()));
					parameters
							.setMassObjectMax(Double
									.parseDouble(me.massObjectMax.getValue()
											.toString()));
					parameters.setMatterDistribution(Double
							.parseDouble(me.matterDistribution.getValue()
									.toString()));
					parameters.setNbARms(Integer.parseInt(me.nbArms.getValue()
							.toString()));
					parameters
							.setEllipseRatio(Double.parseDouble(me.ellipseRatio
									.getValue().toString()));
					parameters.setEllipseShiftRatio(Double
							.parseDouble(me.ellipseShiftRatio.getValue()
									.toString()));
					parameters.setNegligeableMass(Double
							.parseDouble(me.negligeableMass.getValue()
									.toString()));
					parameters.setNumOfLowMassParticule(Integer
							.parseInt(me.numOfLowMassParticule.getValue()
									.toString()));
					parameters.setLowMassParticuleMass(Double
							.parseDouble(me.lowMassParticuleMass.getValue()
									.toString()));
					parameters.setLowMassDensity(Double
							.parseDouble(me.lowMassDensity.getValue()
									.toString()));
					parameters.setGasDistribution(Double
							.parseDouble(me.gasDistribution.getValue()
									.toString()));
					parameters.setManageImpact(me.manageImpact.isSelected());
					parameters
							.setTypeOfImpact(TypeOfImpact.values()[me.typeOfImpact
									.getSelectedIndex()]);
					parameters.setCollisionDistanceRatio(Double
							.parseDouble(me.collisionDistanceRatio.getValue()
									.toString()));
					parameters.setMatterViscosity(Double
							.parseDouble(me.matterViscosity.getValue()
									.toString()));
					parameters
							.setGasViscosity(Double.parseDouble(me.gasViscosity
									.getValue().toString()));
					parameters.setViscoElasticity(Double
							.parseDouble(me.viscoElasticity.getValue()
									.toString()));
					parameters.setViscoElasticityNear(Double
							.parseDouble(me.viscoElasticityNear.getValue()
									.toString()));
					parameters
							.setPressureZero(Double.parseDouble(me.pressureZero
									.getValue().toString()));
					parameters.setDarkMatterMass(Double
							.parseDouble(me.darkMatterMass.getValue()
									.toString()));
					parameters.setDarkMatterDensity(Double
							.parseDouble(me.darkMatterDensity.getValue()
									.toString()));
					parameters.setDarkMatterDistribution(Double
							.parseDouble(me.darkMatterDistribution.getValue()
									.toString()));
					parameters.setDarkMatterXYZRatio(new Vector3d(Double
							.parseDouble(me.darkMatterXRatio.getValue()
									.toString()), Double
							.parseDouble(me.darkMatterYRatio.getValue()
									.toString()), Double
							.parseDouble(me.darkMatterZRatio.getValue()
									.toString())));
					parameters.setStaticDarkMatter(me.staticDarkMatter
							.isSelected());
					parameters.setExpansionUnivers(me.expansionUnivers
							.isSelected());
					parameters.setRecoverFrictionEnegy(me.recoverFrictionEnergy
							.isSelected());
					parameters.setRecoverFrictionEnergyRatio(Double
							.parseDouble(me.recoverFrictionEnergyRatio
									.getValue().toString()));
					parameters.setTimeMultiplicator(Double
							.parseDouble(me.timeMultiplicator.getValue()
									.toString()));

					parameters.setViscousDarkMatter(me.visousDarkMatter
							.isSelected());

					parameters.setEyes(new Vector3d(0, 0, 900));
					parameters.setLookAt(new Vector3d(0, 0, -900));
					me.getMother().reset();
					me.setVisible(false);
					me.mother.setVisible(true);

				} catch (Exception em) {
					JDialog d = new JDialog();
					d.add(new JLabel("Error: " + em.getMessage()));
					d.setVisible(true);
					d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				}
			}
		};
	}
}
