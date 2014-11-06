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
	JComboBox typeOfUnivers;
	private JFormattedTextField nbrObject;
	private JFormattedTextField rayonDeLaNebuleusePrimitive;
	private JFormattedTextField densiteMin;
	private JFormattedTextField densiteMax;
	private JFormattedTextField massBaseObjetMin;
	private JFormattedTextField massBaseObjetMax;
	private JFormattedTextField probFusion;
	private JFormattedTextField typeChoc;

	public GUIParam(GUIProgram mother) {
		this.me = this;
		this.mother = mother;
		setTitle("Parameters");
		setModal(true);
		int w = 500;
		int h = 400;
		setLocation(new Point((mother.getWidth() - w) / 2,
				(mother.getHeight() - h) / 2));
		setSize(new Dimension(w, h));
		setLayout(new GridLayout(10, 2));
		add(new Label("Type of Univers:"));
		typeOfUnivers = new JComboBox();
		for(TypeOfUnivers tou:TypeOfUnivers.values()){
			typeOfUnivers.addItem(tou.getLabel());
		}
		typeOfUnivers.setSelectedItem(HelperVariable.typeOfUnivers.getLabel());
		add(typeOfUnivers);
		add(new Label("Number of object:"));
		nbrObject = new JFormattedTextField(HelperVariable.numberOfObjects);
		add(nbrObject);
		add(new Label("Nebula radius:"));
		rayonDeLaNebuleusePrimitive = new JFormattedTextField(
				HelperVariable.nebulaRadius);
		add(rayonDeLaNebuleusePrimitive);
		add(new Label("Density min:"));
		densiteMin = new JFormattedTextField(HelperVariable.dentityMin);
		add(densiteMin);
		add(new Label("Dentity max:"));
		densiteMax = new JFormattedTextField(HelperVariable.densityMax);
		add(densiteMax);
		add(new Label("Mass object min:"));
		massBaseObjetMin = new JFormattedTextField(HelperVariable.massObjectMin);
		add(massBaseObjetMin);
		add(new Label("Mass object max:"));
		massBaseObjetMax = new JFormattedTextField(HelperVariable.massObjectMax);
		add(massBaseObjetMax);
		add(new Label("Fusion probability (1-0):"));
		probFusion = new JFormattedTextField(HelperVariable.probFusion);
		add(probFusion);
		add(new Label("Type of impact (1-elastic 0-inelastic):"));
		typeChoc = new JFormattedTextField(HelperVariable.typeOfImpact);
		add(typeChoc);

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
					switch(typeOfUnivers.getSelectedIndex()){
					case 0:
						HelperVariable.typeOfUnivers = TypeOfUnivers.Planetary;
						break;
					case 1:
						HelperVariable.typeOfUnivers = TypeOfUnivers.Random;
						break;
					case 2:
						HelperVariable.typeOfUnivers = TypeOfUnivers.Galaxy;
						break;
					}
					HelperVariable.numberOfObjects = Integer
							.parseInt(me.nbrObject.getValue().toString());
					HelperVariable.nebulaRadius = Double
							.parseDouble(me.rayonDeLaNebuleusePrimitive
									.getValue().toString());
					HelperVariable.dentityMin = Double
							.parseDouble(me.densiteMin.getValue().toString());
					HelperVariable.densityMax = Double
							.parseDouble(me.densiteMax.getValue().toString());
					HelperVariable.massObjectMin = Double
							.parseDouble(me.massBaseObjetMin.getValue()
									.toString());
					HelperVariable.massObjectMax = Double
							.parseDouble(me.massBaseObjetMax.getValue()
									.toString());
					HelperVariable.probFusion = Double
							.parseDouble(me.probFusion.getValue().toString());
					HelperVariable.typeOfImpact = Double
							.parseDouble(me.typeChoc.getValue().toString());
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
