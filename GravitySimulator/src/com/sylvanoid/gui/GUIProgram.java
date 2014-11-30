package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jcodec.api.SequenceEncoder;

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
	private SequenceEncoder out;

	public GUIProgram() {
		this.me = this;
		File directory = new File(System.getProperty("user.home"));
		try {
			out = new SequenceEncoder(new File(directory.getPath()
					+ "/out.mpeg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setTitle("Gravity Simulator");
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setSize((int) dimension.getWidth(), (int) dimension.getHeight());
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					me.getOut().finish();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		univers = new Univers(HelperVariable.typeOfUnivers, this);
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
		menuFichier.add(menuItemBaseParam);

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

		JCheckBoxMenuItem menuItemByStep = new JCheckBoxMenuItem(
				"Step By Step", HelperVariable.stepByStep);
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

		JMenu menuVisu = new JMenu("View");
		menuBar.add(menuVisu);
		JCheckBoxMenuItem menuItemDisplayVectors = new JCheckBoxMenuItem(
				"Display Vectors", HelperVariable.displayVectors);
		menuItemDisplayVectors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.displayVectors = !HelperVariable.displayVectors;
			}
		});
		JCheckBoxMenuItem menuItemTrace = new JCheckBoxMenuItem("Trace",
				HelperVariable.traceCourbe);
		menuItemTrace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.traceCourbe = !HelperVariable.traceCourbe;
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
		menuVisu.add(menuItemDisplayVectors);
		menuVisu.add(menuItemTrace);
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

		JMenu menuVideo = new JMenu("Video");
		JCheckBoxMenuItem menuItemExportVideo = new JCheckBoxMenuItem(
				"Record to [home directory/out.mpeg]",
				HelperVariable.exportToVideo);
		menuItemExportVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HelperVariable.exportToVideo = !HelperVariable.exportToVideo;
			}
		});
		menuBar.add(menuVideo);
		menuVideo.add(menuItemExportVideo);

		JMenu menuAbout = new JMenu("?");
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuBar.add(menuAbout);
		menuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUIAbout guiAbout = new GUIAbout(me);
				guiAbout.setVisible(true);
			}
		});
		menuAbout.add(menuItemAbout);

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
		HelperVariable.scala = 1;
		univers = new Univers(HelperVariable.typeOfUnivers, this);
		p.repaint();
		t = new Thread(new SimpleThread(this));
	}

	public SequenceEncoder getOut() {
		return out;
	}

	public void setOut(SequenceEncoder out) {
		this.out = out;
	}
}