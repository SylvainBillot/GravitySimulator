package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.joblib.SimpleThread;
import com.sylvanoid.joblib.Univers;

public class GUIProgram extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIProgram me;
	private Univers univers;
	private GraphicZone p;
	private Thread t;
	private Label status;

	public GUIProgram() {
		this.me = this;
		setTitle("Gravity Simulator");
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setSize((int) dimension.getWidth(), (int) dimension.getHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		univers = new Univers();
		p = new GraphicZone(me);
		me.add(p, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		JMenu menuFichier = new JMenu("File");
		menuBar.add(menuFichier);
		JMenuItem menuItemBaseParam = new JMenuItem("Params ...");
		menuItemBaseParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUIParam guiParam = new GUIParam(me);
				guiParam.setVisible(true);
			}
		});
		JMenuItem menuItemQuitter = new JMenuItem("Quit");
		menuItemQuitter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(me.getThread()!=null){
					me.getThread().interrupt();
				}
				me.dispose();
			}
		});
		menuFichier.add(menuItemBaseParam);
		menuFichier.add(menuItemQuitter);

		JMenu menuProcess = new JMenu("Process");
		menuBar.add(menuProcess);
		JMenuItem menuItemStart = new JMenuItem("Start");
		menuItemStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (t == null) {
					t = new Thread(new SimpleThread(me));
				}
				if (!t.isAlive()) {
					t.start();
				}
			}
		});
		JMenuItem menuItemStop = new JMenuItem("Stop");
		menuItemStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (t == null) {
					t = new Thread(new SimpleThread(me));
				}
				t.interrupt();
				t = new Thread(new SimpleThread(me));
			}
		});
		JMenuItem menuItemReset = new JMenuItem("Reset");
		menuItemReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				me.reset();
			}
		});
		JMenuItem menuItemByStep = new JMenuItem("Step By Step");
		menuItemByStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (t == null) {
					t = new Thread(new SimpleThread(me));
				}
				if (!t.isAlive()) {
					t.start();
				}
				HelperVariable.stepByStep = !HelperVariable.stepByStep;
			}
		});		
		menuProcess.add(menuItemStart);
		menuProcess.add(menuItemStop);
		menuProcess.add(menuItemReset);
		menuProcess.add(menuItemByStep);

		JMenu menuTime = new JMenu("Time factor");
		menuBar.add(menuTime);
		menuTime.setVisible(false);
		JMenuItem menuItemTempsXDix = new JMenuItem("x 2");
		menuItemTempsXDix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.timeFactor *= 2;
				HelperVariable.timeFactorChangeX10 = true;
			}
		});
		JMenuItem menuItemTempsDivDix = new JMenuItem("/ 2");
		menuItemTempsDivDix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.timeFactor /= 2;
				HelperVariable.timeFactorChangeDiv10 = true;
			}
		});
		menuTime.add(menuItemTempsXDix);
		menuTime.add(menuItemTempsDivDix);

		JMenu menuVisu = new JMenu("View");
		menuBar.add(menuVisu);
		JMenuItem menuItemTraceOn = new JMenuItem("Trace On");
		menuItemTraceOn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.traceCourbe = true;
			}
		});
		JMenuItem menuItemTraceOff = new JMenuItem("Trace Off");
		menuItemTraceOff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.traceCourbe = false;
			}
		});
		JMenuItem menuItemCentreEcran = new JMenuItem("All univers on screen");
		menuItemCentreEcran.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.centerOnScreen = true;
				HelperVariable.centerOnCentroid = false;
				HelperVariable.centerOnMassMax = false;
				me.getUnivers().computeLimits();
				HelperVariable.scala = ((double) me.getSize().height
						/ (me.getUnivers().getMaxY() - me.getUnivers()
								.getMinY()));
			}
		});
		JMenuItem menuItemplusMassif = new JMenuItem("Center on maximum mass");
		menuItemplusMassif.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.centerOnScreen = false;
				HelperVariable.centerOnCentroid = false;
				HelperVariable.centerOnMassMax = true;
			}
		});

		JMenuItem menuItemBarycentre = new JMenuItem("Center on centroid");
		menuItemBarycentre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.centerOnScreen = false;
				HelperVariable.centerOnMassMax = false;
				HelperVariable.centerOnCentroid = true;
				;
			}
		});

		JMenuItem menuItemEchelleMoins = new JMenuItem("Zoom -");
		menuItemEchelleMoins.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala /= 2;
				p.updateUI();
			}
		});

		JMenuItem menuItemEchellePlus = new JMenuItem("Zoom +");
		menuItemEchellePlus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala *= 2;
				p.updateUI();
			}
		});
		JMenuItem menuItemEchelleUnQuart = new JMenuItem("Zoom 25%");
		menuItemEchelleUnQuart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala = 0.25;
				p.updateUI();
			}
		});
		JMenuItem menuItemEchelleUnDemi = new JMenuItem("Zoom 50%");
		menuItemEchelleUnDemi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala = 0.5;
				p.updateUI();
			}
		});
		JMenuItem menuItemEchelleUn = new JMenuItem("Zoom 100%");
		menuItemEchelleUn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala = 1;
				p.updateUI();
			}
		});
		JMenuItem menuItemEchelleDeux = new JMenuItem("Zoom 200%");
		menuItemEchelleDeux.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala = 2;
				p.updateUI();
			}
		});
		JMenuItem menuItemEchelleQuatre = new JMenuItem("Zoom 400%");
		menuItemEchelleQuatre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.scala = 4;
				p.updateUI();
			}
		});
		menuVisu.add(menuItemTraceOn);
		menuVisu.add(menuItemTraceOff);
		menuVisu.add(menuItemCentreEcran);
		menuVisu.add(menuItemplusMassif);
		menuVisu.add(menuItemBarycentre);
		menuVisu.add(menuItemEchelleMoins);
		menuVisu.add(menuItemEchellePlus);
		menuVisu.add(menuItemEchelleUnQuart);
		menuVisu.add(menuItemEchelleUnDemi);
		menuVisu.add(menuItemEchelleUn);
		menuVisu.add(menuItemEchelleDeux);
		menuVisu.add(menuItemEchelleQuatre);

		JPanel outPanel = new JPanel(new BorderLayout());
		add(outPanel, BorderLayout.SOUTH);
		status = new Label("");
		outPanel.add(status);

	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GUIProgram ex = new GUIProgram();
				ex.setVisible(true);
			}
		});
	}

	public Label getStatus() {
		return status;
	}

	public void setGraphicZone(GraphicZone p) {
		this.p = p;
	}

	public GraphicZone getGraphicZone() {
		return p;
	}

	public void setUnivers(Univers univers) {
		this.univers = univers;
	}

	public Univers getUnivers() {
		return univers;
	}

	public Thread getThread() {
		return t;
	}

	public void reset() {
		if (t == null) {
			t = new Thread(new SimpleThread(this));
		}
		t.interrupt();
		HelperVariable.centerOnMassMax = false;
		HelperVariable.centerOnCentroid = false;
		HelperVariable.centerOnCentroid = false;
		HelperVariable.traceCourbe = false;
		HelperVariable.scala = 1;
		HelperVariable.timeFactor = 1;
		univers = new Univers();
		p.repaint();
		t = new Thread(new SimpleThread(this));
	}
}