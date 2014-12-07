package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jcodec.api.SequenceEncoder;

import com.jogamp.opengl.util.FPSAnimator;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Univers;

public class GUIProgram extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIProgram me;
	private Univers univers;
	private final FPSAnimator animator;
	private SequenceEncoder out;
	private GLU glu = new GLU();

	private float eyesX = 0;
	private float eyesY = 0;
	private float eyesZ = 900;

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
		InputHandler inputHandler = new InputHandler(this);
		addKeyListener(inputHandler);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					me.getOut().finish();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					animator.stop();
					System.exit(0);
				}
			}
		});
		univers = new Univers(HelperVariable.typeOfUnivers);

		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		JMenu menuFichier = new JMenu("File");
		menuBar.add(menuFichier);
		JMenuItem menuItemBaseParam = new JMenuItem("Params ...");
		menuItemBaseParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animator.stop();
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
				animator.start();
			}
		});
		JMenuItem menuItemStop = new JMenuItem("Stop");
		menuItemStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animator.stop();
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

		menuVisu.add(menuItemDisplayVectors);
		menuVisu.add(menuItemTrace);
		menuVisu.add(menuItemCentreEcran);
		menuVisu.add(menuItemplusMassif);
		menuVisu.add(menuItemBarycentre);

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

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLJPanel gljpanel = new GLJPanel(caps);
		gljpanel.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable drawable, int xstart,
					int ystart, int width, int height) {
				// TODO Auto-generated method stub
				final GL2 gl = drawable.getGL().getGL2();

				if (height <= 0) // avoid a divide by zero error!
					height = 1;
				final float h = (float) width / (float) height;
				gl.glViewport(0, 0, width, height);
				gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
				gl.glLoadIdentity();
				glu.gluPerspective(45, h, 1.0, 10000.0);
				glu.gluLookAt(eyesX, eyesY, eyesZ, 0, 0, 0, 0, 1, 0);
				gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
				gl.glLoadIdentity();
			}

			@Override
			public void init(GLAutoDrawable drawable) {
				// TODO Auto-generated method stub
				GL2 gl = drawable.getGL().getGL2();
				gl.glClearColor(0, 0, 0.1f, 0);
			}

			@Override
			public void dispose(GLAutoDrawable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void display(GLAutoDrawable drawable) {
				// TODO Auto-generated method stub
				render(drawable);
				update();
			}
		});
		me.add(gljpanel, BorderLayout.CENTER);
		// Create a animator that drives canvas' display() at the specified FPS.
		animator = new FPSAnimator(gljpanel, 60, true);

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

	public FPSAnimator getAnimator() {
		return animator;
	}

	public void setUnivers(Univers univers) {
		this.univers = univers;
	}

	public Univers getUnivers() {
		return univers;
	}

	public void reset() {
		animator.stop();
		HelperVariable.centerOnMassMax = false;
		HelperVariable.centerOnCentroid = false;
		HelperVariable.centerOnCentroid = false;
		univers = new Univers(HelperVariable.typeOfUnivers);
		animator.start();
	}

	public SequenceEncoder getOut() {
		return out;
	}

	public void setOut(SequenceEncoder out) {
		this.out = out;
	}

	public void eyesXAdd(float value) {
		eyesX += value;
	}

	public void eyesYAdd(float value) {
		eyesY += value;
	}

	public void eyesZAdd(float value) {
		eyesZ += value;
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 10000);
		glu.gluLookAt(eyesX, eyesY, eyesZ, 0, 0, 0, 0, 1, 0);
		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		if (!HelperVariable.traceCourbe) {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		}
		GLUquadric param = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(param, GLU.GLU_POINT);
		for (Matter m : univers.getListMatiere().values()) {
			if (!m.isDark()) {
				gl.glLoadIdentity();
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);
				gl.glPushMatrix();
				gl.glColor3d(1, 1, 1);
				glu.gluSphere(param, m.getRayon(), 6, 6);
				gl.glPopMatrix();
			}
		}
		gl.glEnd();
		gl.glFlush();
	}

	private void update() {
		univers.process();
	}

}