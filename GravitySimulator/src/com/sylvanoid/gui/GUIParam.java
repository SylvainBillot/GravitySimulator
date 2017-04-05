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
import javax.swing.SwingConstants;
import java.awt.Font;

public class GUIParam extends JDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GUIParam me;
	private GUIProgram mother;
	private JCheckBox parallelization;
	private JCheckBox barnessHut;
	private JComboBox<String> typeOfUnivers;
	private JFormattedTextField timeFactor;
	private JFormattedTextField scala;
	private JFormattedTextField matterRendererExtender;
	private JFormattedTextField gasRendererExtender;
	private JFormattedTextField darkMatterRendererExtender;
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
	private JFormattedTextField darkMatterViscosity;
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
		getContentPane().setFont(new Font("Dialog", Font.PLAIN, 10));
		setFont(new Font("Dialog", Font.PLAIN, 10));
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
		setSize(new Dimension(1024, 600));
		int w = getSize().width;
		int h = getSize().height;
		setLocation(new Point((mother.getWidth() - w) / 2, (mother.getHeight() - h) / 2));

		getContentPane().setLayout(new GridLayout(0, 4));
		JLabel label_TypeOfUnivers = new JLabel("Type of Univers:");
		label_TypeOfUnivers.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_TypeOfUnivers);
		typeOfUnivers = new JComboBox<String>();
		typeOfUnivers.setFont(new Font("Dialog", Font.BOLD, 10));
		for (TypeOfUnivers tou : TypeOfUnivers.values()) {
			typeOfUnivers.addItem(tou.getLabel());
		}
		typeOfUnivers.setSelectedItem(parameters.getTypeOfUnivers().getLabel());
		typeOfUnivers.addActionListener(chooseTypeOfUnivers());
		getContentPane().add(typeOfUnivers);

		JLabel label_Parallelization = new JLabel("Parallelization:");
		label_Parallelization.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_Parallelization);
		parallelization = new JCheckBox();
		parallelization.setSelected(parameters.isParallelization());
		getContentPane().add(parallelization);

		JLabel label_BarnesHut = new JLabel("BarneHut/NN:");
		label_BarnesHut.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_BarnesHut);
		barnessHut = new JCheckBox();
		barnessHut.setSelected(parameters.isBarnesHut());
		getContentPane().add(barnessHut);

		JLabel label_Scala = new JLabel("Scala 1/x:");
		label_Scala.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_Scala);
		scala = new JFormattedTextField(dfsc);
		scala.setFont(new Font("Dialog", Font.PLAIN, 10));
		scala.setHorizontalAlignment(SwingConstants.RIGHT);
		scala.setValue(parameters.getScala());
		getContentPane().add(scala);

		JLabel label_TimeFactor = new JLabel("Time factor:");
		label_TimeFactor.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_TimeFactor);
		timeFactor = new JFormattedTextField(dfsc);
		timeFactor.setFont(new Font("Dialog", Font.PLAIN, 10));
		timeFactor.setHorizontalAlignment(SwingConstants.RIGHT);
		timeFactor.setValue(parameters.getTimeFactor());
		getContentPane().add(timeFactor);

		JLabel label_MatterRendererExtender = new JLabel("Matter Renderer Extender:");
		label_MatterRendererExtender.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_MatterRendererExtender);
		matterRendererExtender = new JFormattedTextField(dfsc);
		matterRendererExtender.setFont(new Font("Dialog", Font.PLAIN, 10));
		matterRendererExtender.setHorizontalAlignment(SwingConstants.RIGHT);
		matterRendererExtender.setValue(parameters.getMatterRendererExtender());
		getContentPane().add(matterRendererExtender);

		JLabel label_GasRendererExtender = new JLabel("Gas Renderer Extender:");
		label_GasRendererExtender.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_GasRendererExtender);
		gasRendererExtender = new JFormattedTextField(dfsc);
		gasRendererExtender.setFont(new Font("Dialog", Font.PLAIN, 10));
		gasRendererExtender.setHorizontalAlignment(SwingConstants.RIGHT);
		gasRendererExtender.setValue(parameters.getGasRendererExtender());
		getContentPane().add(gasRendererExtender);

		JLabel label_DarkMatterRendererExtender = new JLabel("Dark Matter Renderer Extender:");
		label_DarkMatterRendererExtender.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_DarkMatterRendererExtender);
		darkMatterRendererExtender = new JFormattedTextField(dfsc);
		darkMatterRendererExtender.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterRendererExtender.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterRendererExtender.setValue(parameters.getDarkMatterRendererExtender());
		getContentPane().add(darkMatterRendererExtender);

		JLabel label_NumOfObject = new JLabel("Number of object:");
		label_NumOfObject.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_NumOfObject);
		numberOfObjects = new JFormattedTextField(dfsc);
		numberOfObjects.setFont(new Font("Dialog", Font.PLAIN, 10));
		numberOfObjects.setHorizontalAlignment(SwingConstants.RIGHT);
		numberOfObjects.setValue(parameters.getNumberOfObjects());
		getContentPane().add(numberOfObjects);

		JLabel label_Radius = new JLabel("Nebula radius:");
		label_Radius.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_Radius);
		nebulaRadius = new JFormattedTextField(dfsc);
		nebulaRadius.setFont(new Font("Dialog", Font.PLAIN, 10));
		nebulaRadius.setHorizontalAlignment(SwingConstants.RIGHT);
		nebulaRadius.setValue(parameters.getNebulaRadius());
		getContentPane().add(nebulaRadius);

		JLabel label_DensityObject = new JLabel("Density of objects:");
		label_DensityObject.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_DensityObject);
		densiteMin = new JFormattedTextField(dfsc);
		densiteMin.setFont(new Font("Dialog", Font.PLAIN, 10));
		densiteMin.setHorizontalAlignment(SwingConstants.RIGHT);
		densiteMin.setValue(parameters.getDensity());
		getContentPane().add(densiteMin);

		JLabel label_MassObject = new JLabel("Mass object:");
		label_MassObject.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_MassObject);
		JPanel massObject = new JPanel();
		getContentPane().add(massObject);
		massObjectMin = new JFormattedTextField(dfsc);
		massObjectMin.setFont(new Font("Dialog", Font.PLAIN, 10));
		massObjectMin.setHorizontalAlignment(SwingConstants.RIGHT);
		massObjectMin.setBounds(36, 0, 80, 29);
		massObjectMin.setValue(parameters.getMassObjectMin());
		massObject.setLayout(null);
		JLabel label_min = new JLabel("Min:");
		label_min.setFont(new Font("Dialog", Font.BOLD, 10));
		label_min.setBounds(0, 0, 46, 29);
		massObject.add(label_min);
		massObject.add(massObjectMin);
		massObjectMax = new JFormattedTextField(dfsc);
		massObjectMax.setFont(new Font("Dialog", Font.PLAIN, 10));
		massObjectMax.setHorizontalAlignment(SwingConstants.RIGHT);
		massObjectMax.setBounds(175, 0, 80, 29);
		massObjectMax.setValue(parameters.getMassObjectMax());
		JLabel label_max = new JLabel("Max:");
		label_max.setFont(new Font("Dialog", Font.BOLD, 10));
		label_max.setBounds(138, 0, 39, 29);
		massObject.add(label_max);
		massObject.add(massObjectMax);

		JLabel label_MatterDist = new JLabel("Matter distribution:");
		label_MatterDist.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_MatterDist);
		matterDistribution = new JFormattedTextField(dfsc);
		matterDistribution.setFont(new Font("Dialog", Font.PLAIN, 10));
		matterDistribution.setHorizontalAlignment(SwingConstants.RIGHT);
		matterDistribution.setValue(parameters.getMatterDistribution());
		getContentPane().add(matterDistribution);

		JLabel label_NbGalArms = new JLabel("Nb galactic Arms:");
		label_NbGalArms.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_NbGalArms);
		nbArms = new JFormattedTextField(dfsc);
		nbArms.setFont(new Font("Dialog", Font.PLAIN, 10));
		nbArms.setHorizontalAlignment(SwingConstants.RIGHT);
		nbArms.setValue(parameters.getNbARms());
		getContentPane().add(nbArms);

		JLabel label_elipse_ecc = new JLabel("Ellipses eccentricity (]0-1[):");
		label_elipse_ecc.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_elipse_ecc);
		ellipseRatio = new JFormattedTextField(fdpc);
		ellipseRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		ellipseRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		ellipseRatio.setValue(parameters.getEllipseRatio());
		getContentPane().add(ellipseRatio);

		JLabel label_elipse_shit = new JLabel("Ellipses shift (ratio of nebula radius):");
		label_elipse_shit.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_elipse_shit);
		ellipseShiftRatio = new JFormattedTextField(dfsc);
		ellipseShiftRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		ellipseShiftRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		ellipseShiftRatio.setValue(parameters.getEllipseShiftRatio());
		getContentPane().add(ellipseShiftRatio);

		JLabel label_35 = new JLabel("Negligeable mass:");
		label_35.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_35);
		negligeableMass = new JFormattedTextField(dfsc);
		negligeableMass.setFont(new Font("Dialog", Font.PLAIN, 10));
		negligeableMass.setHorizontalAlignment(SwingConstants.RIGHT);
		negligeableMass.setValue(parameters.getNegligeableMass());
		getContentPane().add(negligeableMass);

		JLabel label_12 = new JLabel("Num of gas particle:");
		label_12.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_12);
		numOfLowMassParticule = new JFormattedTextField(dfsc);
		numOfLowMassParticule.setFont(new Font("Dialog", Font.PLAIN, 10));
		numOfLowMassParticule.setHorizontalAlignment(SwingConstants.RIGHT);
		numOfLowMassParticule.setValue(parameters.getNumOfLowMassParticule());
		getContentPane().add(numOfLowMassParticule);

		JLabel label_34 = new JLabel("Gas particles max mass:");
		label_34.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_34);
		lowMassParticuleMass = new JFormattedTextField(dfsc);
		lowMassParticuleMass.setFont(new Font("Dialog", Font.PLAIN, 10));
		lowMassParticuleMass.setHorizontalAlignment(SwingConstants.RIGHT);
		lowMassParticuleMass.setValue(parameters.getLowMassParticuleMass());
		getContentPane().add(lowMassParticuleMass);

		JLabel label_13 = new JLabel("Gas particle density:");
		label_13.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_13);
		lowMassDensity = new JFormattedTextField(dfsc);
		lowMassDensity.setFont(new Font("Dialog", Font.PLAIN, 10));
		lowMassDensity.setHorizontalAlignment(SwingConstants.RIGHT);
		lowMassDensity.setValue(parameters.getLowMassDensity());
		getContentPane().add(lowMassDensity);

		JLabel label_33 = new JLabel("Gas distribution:");
		label_33.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_33);
		gasDistribution = new JFormattedTextField(dfsc);
		gasDistribution.setFont(new Font("Dialog", Font.PLAIN, 10));
		gasDistribution.setHorizontalAlignment(SwingConstants.RIGHT);
		gasDistribution.setValue(parameters.getGasDistribution());
		getContentPane().add(gasDistribution);

		JLabel label_14 = new JLabel("Dark Matter or central star Mass:");
		label_14.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_14);
		darkMatterMass = new JFormattedTextField(dfsc);
		darkMatterMass.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterMass.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterMass.setValue(parameters.getDarkMatterMass());
		getContentPane().add(darkMatterMass);

		JLabel label_32 = new JLabel("Dark Matter or central star Density:");
		label_32.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_32);
		darkMatterDensity = new JFormattedTextField(dfsc);
		darkMatterDensity.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterDensity.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterDensity.setValue(parameters.getDarkMatterDensity());
		getContentPane().add(darkMatterDensity);

		JLabel label_15 = new JLabel("Dark Matterdistribution:");
		label_15.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_15);
		darkMatterDistribution = new JFormattedTextField(dfsc);
		darkMatterDistribution.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterDistribution.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterDistribution.setValue(parameters.getDarkMatterDistribution());
		getContentPane().add(darkMatterDistribution);

		JLabel label_31 = new JLabel("Dark matter xyz ratio");
		label_31.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_31);
		JPanel darkMatterRatio = new JPanel();
		getContentPane().add(darkMatterRatio);
		darkMatterXRatio = new JFormattedTextField(dfsc);
		darkMatterXRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterXRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterXRatio.setBounds(23, 0, 50, 29);
		darkMatterXRatio.setValue(parameters.getDarkMatterXYZRatio().x);
		darkMatterRatio.setLayout(null);
		JLabel label_2 = new JLabel("X:");
		label_2.setFont(new Font("Dialog", Font.BOLD, 10));
		label_2.setBounds(2, 0, 22, 29);
		darkMatterRatio.add(label_2);
		darkMatterRatio.add(darkMatterXRatio);

		darkMatterYRatio = new JFormattedTextField(dfsc);
		darkMatterYRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterYRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterYRatio.setBounds(113, 0, 50, 29);
		darkMatterYRatio.setValue(parameters.getDarkMatterXYZRatio().y);
		JLabel label_3 = new JLabel("Y:");
		label_3.setFont(new Font("Dialog", Font.BOLD, 10));
		label_3.setBounds(93, 0, 22, 29);
		darkMatterRatio.add(label_3);
		darkMatterRatio.add(darkMatterYRatio);

		darkMatterZRatio = new JFormattedTextField(dfsc);
		darkMatterZRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterZRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterZRatio.setBounds(205, 0, 50, 29);
		darkMatterZRatio.setValue(parameters.getDarkMatterXYZRatio().z);
		JLabel label_4 = new JLabel("Z:");
		label_4.setFont(new Font("Dialog", Font.BOLD, 10));
		label_4.setBounds(181, 0, 22, 29);
		darkMatterRatio.add(label_4);
		darkMatterRatio.add(darkMatterZRatio);

		JLabel label_16 = new JLabel("Manage Impact:");
		label_16.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_16);
		manageImpact = new JCheckBox();
		manageImpact.setSelected(parameters.isManageImpact());
		manageImpact.addActionListener(chooseManageImpact());
		getContentPane().add(manageImpact);

		JLabel label_30 = new JLabel("Type Of Impact:");
		label_30.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_30);
		typeOfImpact = new JComboBox<String>();
		typeOfImpact.setFont(new Font("Dialog", Font.BOLD, 10));
		for (TypeOfImpact toi : TypeOfImpact.values()) {
			typeOfImpact.addItem(toi.getLabel());
		}
		typeOfImpact.setSelectedItem(parameters.getTypeOfImpact().getLabel());
		getContentPane().add(typeOfImpact);

		JLabel label_17 = new JLabel("Collision distance ratio rij:");
		label_17.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_17);
		collisionDistanceRatio = new JFormattedTextField(dfsc);
		collisionDistanceRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		collisionDistanceRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		collisionDistanceRatio.setValue(parameters.getCollisionDistanceRatio());
		getContentPane().add(collisionDistanceRatio);

		JLabel label_29 = new JLabel("Matter Viscosity ratio:");
		label_29.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_29);
		matterViscosity = new JFormattedTextField(dfsc);
		matterViscosity.setFont(new Font("Dialog", Font.PLAIN, 10));
		matterViscosity.setHorizontalAlignment(SwingConstants.RIGHT);
		matterViscosity.setValue(parameters.getMatterViscosity());
		getContentPane().add(matterViscosity);

		JLabel label_18 = new JLabel("Gas Viscosity ratio:");
		label_18.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_18);
		gasViscosity = new JFormattedTextField(dfsc);
		gasViscosity.setFont(new Font("Dialog", Font.PLAIN, 10));
		gasViscosity.setHorizontalAlignment(SwingConstants.RIGHT);
		gasViscosity.setValue(parameters.getGasViscosity());
		getContentPane().add(gasViscosity);

		JLabel label_28 = new JLabel("Dark Matter Viscosity ratio:");
		label_28.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_28);
		darkMatterViscosity = new JFormattedTextField(dfsc);
		darkMatterViscosity.setFont(new Font("Dialog", Font.PLAIN, 10));
		darkMatterViscosity.setHorizontalAlignment(SwingConstants.RIGHT);
		darkMatterViscosity.setValue(parameters.getDarkMatterViscosity());
		getContentPane().add(darkMatterViscosity);

		JLabel label_19 = new JLabel("Visco elasticity:");
		label_19.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_19);
		viscoElasticity = new JFormattedTextField(dfsc);
		viscoElasticity.setFont(new Font("Dialog", Font.PLAIN, 10));
		viscoElasticity.setHorizontalAlignment(SwingConstants.RIGHT);
		viscoElasticity.setValue(parameters.getViscoElasticity());
		getContentPane().add(viscoElasticity);

		JLabel label_27 = new JLabel("Visco elasticity Near:");
		label_27.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_27);
		viscoElasticityNear = new JFormattedTextField(dfsc);
		viscoElasticityNear.setFont(new Font("Dialog", Font.PLAIN, 10));
		viscoElasticityNear.setHorizontalAlignment(SwingConstants.RIGHT);
		viscoElasticityNear.setValue(parameters.getViscoElasticityNear());
		getContentPane().add(viscoElasticityNear);

		JLabel label_20 = new JLabel("Rest of Presure:");
		label_20.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_20);
		pressureZero = new JFormattedTextField(dfsc);
		pressureZero.setFont(new Font("Dialog", Font.PLAIN, 10));
		pressureZero.setHorizontalAlignment(SwingConstants.RIGHT);
		pressureZero.setValue(parameters.getPressureZero());
		getContentPane().add(pressureZero);

		JLabel label_26 = new JLabel("Recover Friction Energy (Experimental):");
		label_26.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_26);
		recoverFrictionEnergy = new JCheckBox();
		recoverFrictionEnergy.setSelected(parameters.isRecoverFrictionEnegy());
		recoverFrictionEnergy.addActionListener(chooseRecoverEnergy());

		getContentPane().add(recoverFrictionEnergy);

		JLabel label_21 = new JLabel("Recover Friction Energy Ratio:");
		label_21.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_21);
		recoverFrictionEnergyRatio = new JFormattedTextField(dfsc);
		recoverFrictionEnergyRatio.setFont(new Font("Dialog", Font.PLAIN, 10));
		recoverFrictionEnergyRatio.setHorizontalAlignment(SwingConstants.RIGHT);
		recoverFrictionEnergyRatio.setValue(parameters.getRecoverFrictionEnergyRatio());
		getContentPane().add(recoverFrictionEnergyRatio);

		JLabel label_25 = new JLabel("Static Dark Matter:");
		label_25.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_25);
		staticDarkMatter = new JCheckBox();
		staticDarkMatter.setSelected(parameters.isStaticDarkMatter());
		getContentPane().add(staticDarkMatter);

		JLabel label_22 = new JLabel("Expansion Of Univers:");
		label_22.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_22);
		expansionUnivers = new JCheckBox();
		expansionUnivers.setSelected(parameters.isExpansionUnivers());
		getContentPane().add(expansionUnivers);

		JLabel label_24 = new JLabel("Time multiplicator:");
		label_24.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_24);
		timeMultiplicator = new JFormattedTextField(dfsc);
		timeMultiplicator.setFont(new Font("Dialog", Font.PLAIN, 10));
		timeMultiplicator.setHorizontalAlignment(SwingConstants.RIGHT);
		timeMultiplicator.setValue(parameters.getTimeMultiplicator());
		getContentPane().add(timeMultiplicator);

		JLabel label_23 = new JLabel("Viscous Dark Matter:");
		label_23.setFont(new Font("Dialog", Font.BOLD, 10));
		getContentPane().add(label_23);
		visousDarkMatter = new JCheckBox();
		visousDarkMatter.setSelected(parameters.isViscousDarkMatter());
		getContentPane().add(visousDarkMatter);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Dialog", Font.BOLD, 10));
		btnCancel.addActionListener(cancelAction());
		getContentPane().add(btnCancel);
		JButton btnOK = new JButton("OK");
		btnOK.setFont(new Font("Dialog", Font.BOLD, 10));
		btnOK.addActionListener(okAction());
		getContentPane().add(btnOK);
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
		darkMatterViscosity.setEnabled(manageImpact.isSelected());
		viscoElasticity.setEnabled(manageImpact.isSelected());
		viscoElasticityNear.setEnabled(manageImpact.isSelected());
		pressureZero.setEnabled(manageImpact.isSelected());
		recoverFrictionEnergy.setEnabled(manageImpact.isSelected());
		recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected() && recoverFrictionEnergy.isSelected());
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
		matterRendererExtender.setEnabled(false);
		gasRendererExtender.setEnabled(false);
		darkMatterRendererExtender.setEnabled(false);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
			matterRendererExtender.setEnabled(true);
			gasRendererExtender.setEnabled(true);
			darkMatterRendererExtender.setEnabled(true);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(10);
					gasRendererExtender.setValue(10);
					darkMatterRendererExtender.setValue(20);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(3);
					gasRendererExtender.setValue(3);
					darkMatterRendererExtender.setValue(5);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(1);
					gasRendererExtender.setValue(1);
					darkMatterRendererExtender.setValue(1);
					break;
				case 3:
					parameters.setTypeOfUnivers(TypeOfUnivers.RandomRotateUnivers);
					manageImpact.setSelected(false);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					typeOfImpact.setSelectedIndex(0);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-20);
					nebulaRadius.setValue(HelperVariable.PC * 3E4);
					massObjectMin.setValue(HelperVariable.MINIMALSTARMASS * 1E3);
					massObjectMax.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(HelperVariable.MINIMALSTARMASS * 1E2);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(1);
					gasRendererExtender.setValue(1);
					darkMatterRendererExtender.setValue(1);
					break;
				case 4: // Planetary genesis
					parameters.setTypeOfUnivers(TypeOfUnivers.PlanetariesGenesis);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1);
					viscoElasticityNear.setValue(1);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(1);
					gasRendererExtender.setValue(1);
					darkMatterRendererExtender.setValue(1);
					break;
				case 5:
					parameters.setTypeOfUnivers(TypeOfUnivers.RandomRotateUniverCircular);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(3E-19);
					typeOfImpact.setSelectedIndex(1);
					numberOfObjects.setValue(10000);
					densiteMin.setValue(1E-19);
					nebulaRadius.setValue(HelperVariable.PC * 4E4);
					massObjectMin.setValue(HelperVariable.MINIMALSTARMASS * 1E4);
					massObjectMax.setValue(HelperVariable.MAXIMALSTARMASS * 1E3);
					matterDistribution.setValue(5);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(10000);
					lowMassParticuleMass.setValue(HelperVariable.MINIMALSTARMASS * 1E6);
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
					darkMatterViscosity.setValue(1);
					viscoElasticity.setValue(1E-11);
					viscoElasticityNear.setValue(1E-11);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(5);
					darkMatterYRatio.setValue(5);
					darkMatterZRatio.setValue(5);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(1);
					gasRendererExtender.setValue(3);
					darkMatterRendererExtender.setValue(1);
					break;
				case 6:
					parameters.setTypeOfUnivers(TypeOfUnivers.RandomExpensionUnivers);
					manageImpact.setSelected(true);
					timeFactor.setValue(HelperVariable.ONEYEAR * 1E7);
					scala.setValue(5E-24);
					typeOfImpact.setSelectedIndex(1);
					numberOfObjects.setValue(8000);
					densiteMin.setValue(1E-22);
					nebulaRadius.setValue(HelperVariable.PC * 2E9);
					massObjectMin.setValue(HelperVariable.MINIMALSTARMASS * 1E18);
					massObjectMax.setValue(HelperVariable.MINIMALSTARMASS * 1E19);
					matterDistribution.setValue(1);
					negligeableMass.setValue(0);
					numOfLowMassParticule.setValue(0);
					lowMassParticuleMass.setValue(1);
					lowMassDensity.setValue(1E-23);
					gasDistribution.setValue(1);
					darkMatterMass.setValue(HelperVariable.MINIMALSTARMASS * 1E23);
					darkMatterDensity.setValue(1E-25);
					darkMatterDistribution.setValue(1);
					ellipseRatio.setValue(0.15);
					ellipseShiftRatio.setValue(1);
					nbArms.setValue(3);
					staticDarkMatter.setSelected(false);
					expansionUnivers.setSelected(true);
					collisionDistanceRatio.setValue(1);
					matterViscosity.setValue(1E-3);
					gasViscosity.setValue(1);
					darkMatterViscosity.setValue(1E-10);
					viscoElasticity.setValue(1E-10);
					viscoElasticityNear.setValue(1E-10);
					pressureZero.setValue(0);
					darkMatterXRatio.setValue(1);
					darkMatterYRatio.setValue(1);
					darkMatterZRatio.setValue(1);
					recoverFrictionEnergy.setSelected(false);
					recoverFrictionEnergyRatio.setValue(1);
					timeMultiplicator.setValue(1);
					visousDarkMatter.setSelected(false);
					matterRendererExtender.setValue(1);
					gasRendererExtender.setValue(3);
					darkMatterRendererExtender.setValue(3);
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
				recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected() && recoverFrictionEnergy.isSelected());
			}
		};
	}

	private ActionListener chooseRecoverEnergy() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recoverFrictionEnergyRatio.setEnabled(manageImpact.isSelected() && recoverFrictionEnergy.isSelected());
			}
		};
	}

	private ActionListener cancelAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				me.setVisible(false);
				me.mother.setVisible(true);
				me.mother.getAnimator().start();
			}
		};
	}

	private ActionListener okAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parameters.setParallelization(me.parallelization.isSelected());
					parameters.setBarnesHut(me.barnessHut.isSelected());
					parameters.setTimeFactor(Double.parseDouble(me.timeFactor.getValue().toString()));
					parameters.setScala(Double.parseDouble(me.scala.getValue().toString()));
					parameters.setNumberOfObjects(Integer.parseInt(me.numberOfObjects.getValue().toString()));
					parameters.setNebulaRadius(Double.parseDouble(me.nebulaRadius.getValue().toString()));
					parameters.setDensity(Double.parseDouble(me.densiteMin.getValue().toString()));
					parameters.setMassObjectMin(Double.parseDouble(me.massObjectMin.getValue().toString()));
					parameters.setMassObjectMax(Double.parseDouble(me.massObjectMax.getValue().toString()));
					parameters.setMatterDistribution(Double.parseDouble(me.matterDistribution.getValue().toString()));
					parameters.setNbARms(Integer.parseInt(me.nbArms.getValue().toString()));
					parameters.setEllipseRatio(Double.parseDouble(me.ellipseRatio.getValue().toString()));
					parameters.setEllipseShiftRatio(Double.parseDouble(me.ellipseShiftRatio.getValue().toString()));
					parameters.setNegligeableMass(Double.parseDouble(me.negligeableMass.getValue().toString()));
					parameters
							.setNumOfLowMassParticule(Integer.parseInt(me.numOfLowMassParticule.getValue().toString()));
					parameters
							.setLowMassParticuleMass(Double.parseDouble(me.lowMassParticuleMass.getValue().toString()));
					parameters.setLowMassDensity(Double.parseDouble(me.lowMassDensity.getValue().toString()));
					parameters.setGasDistribution(Double.parseDouble(me.gasDistribution.getValue().toString()));
					parameters.setManageImpact(me.manageImpact.isSelected());
					parameters.setTypeOfImpact(TypeOfImpact.values()[me.typeOfImpact.getSelectedIndex()]);
					parameters.setCollisionDistanceRatio(
							Double.parseDouble(me.collisionDistanceRatio.getValue().toString()));
					parameters.setMatterViscosity(Double.parseDouble(me.matterViscosity.getValue().toString()));
					parameters.setGasViscosity(Double.parseDouble(me.gasViscosity.getValue().toString()));
					parameters.setDarkMatterViscosity(Double.parseDouble(me.darkMatterViscosity.getValue().toString()));
					parameters.setViscoElasticity(Double.parseDouble(me.viscoElasticity.getValue().toString()));
					parameters.setViscoElasticityNear(Double.parseDouble(me.viscoElasticityNear.getValue().toString()));
					parameters.setPressureZero(Double.parseDouble(me.pressureZero.getValue().toString()));
					parameters.setDarkMatterMass(Double.parseDouble(me.darkMatterMass.getValue().toString()));
					parameters.setDarkMatterDensity(Double.parseDouble(me.darkMatterDensity.getValue().toString()));
					parameters.setDarkMatterDistribution(
							Double.parseDouble(me.darkMatterDistribution.getValue().toString()));
					parameters.setDarkMatterXYZRatio(
							new Vector3d(Double.parseDouble(me.darkMatterXRatio.getValue().toString()),
									Double.parseDouble(me.darkMatterYRatio.getValue().toString()),
									Double.parseDouble(me.darkMatterZRatio.getValue().toString())));
					parameters.setStaticDarkMatter(me.staticDarkMatter.isSelected());
					parameters.setExpansionUnivers(me.expansionUnivers.isSelected());
					parameters.setRecoverFrictionEnegy(me.recoverFrictionEnergy.isSelected());
					parameters.setRecoverFrictionEnergyRatio(
							Double.parseDouble(me.recoverFrictionEnergyRatio.getValue().toString()));
					parameters.setTimeMultiplicator(Double.parseDouble(me.timeMultiplicator.getValue().toString()));
					parameters.setViscousDarkMatter(me.visousDarkMatter.isSelected());
					parameters.setMatterRendererExtender(
							Double.parseDouble(me.matterRendererExtender.getValue().toString()));
					parameters.setGasRendererExtender(Double.parseDouble(me.gasRendererExtender.getValue().toString()));
					parameters.setDarkMatterRendererExtender(
							Double.parseDouble(me.darkMatterRendererExtender.getValue().toString()));

					parameters.setEyes(new Vector3d(0, 0, 900));
					parameters.setLookAt(new Vector3d(0, 0, -900));
					me.getMother().reset();
					me.setVisible(false);
					me.mother.setVisible(true);

				} catch (Exception em) {
					em.printStackTrace();
				}
			}
		};
	}
}
