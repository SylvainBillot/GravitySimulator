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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.vecmath.Vector3d;

import org.jcodec.api.SequenceEncoder;

import com.jogamp.opengl.util.FPSAnimator;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Univers;

import com.sylvanoid.common.TextureReader;

public class GUIProgram extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIProgram me;
	private GLJPanel gljpanel;
	private Univers univers;
	private final FPSAnimator animator;
	private SequenceEncoder out;
	private GLU glu = new GLU();

	private int textures[] = new int[1]; // Storage For One textures

	private Vector3d eyes = new Vector3d(0, 0, 900);

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GUIProgram ex = new GUIProgram();
				ex.setVisible(true);
			}
		});
	}

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
		gljpanel = new GLJPanel(caps);
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
				glu.gluLookAt(eyes.x, eyes.y, eyes.z, 0, 0, 0, 0, 1, 0);
				gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
				gl.glLoadIdentity();
			}

			@Override
			public void init(GLAutoDrawable drawable) {
				// TODO Auto-generated method stub
				GL2 gl = drawable.getGL().getGL2();
				gl.glEnable(GL2.GL_TEXTURE_2D); // Enable Texture Mapping
				gl.glShadeModel(GL2.GL_SMOOTH); // Enable Smooth Shading
				gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
				gl.glClearDepth(1.0f); // Depth Buffer Setup
				gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really
																				// Nice
																				// Perspective
																				// Calculations
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE); // Set The Blending
															// Function For
															// Translucency
				gl.glEnable(GL.GL_BLEND);

				LoadGLTextures(gl);
			}

			@Override
			public void dispose(GLAutoDrawable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void display(GLAutoDrawable drawable) {
				// TODO Auto-generated method stub
				render(drawable);
				// if (HelperVariable.exportToVideo) {
				// BufferedImage img = new BufferedImage(drawable.getWidth(),
				// drawable.getHeight(),
				// BufferedImage.TYPE_USHORT_555_RGB);
				// drawable.paint(img.getGraphics());
				// GL2 gl = drawable.getGL().getGL2();
				// BufferedImage img =
				// gl.glReadBuffer(GL2.GL_READ_BUFFER).readPixelsToBufferedImage(drawable.getGL(),
				// true);
				// try {
				// me.getOut().encodeImage(img);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				update();
			}
		});
		me.add(gljpanel, BorderLayout.CENTER);
		// Create a animator that drives canvas' display() at the specified FPS.
		animator = new FPSAnimator(gljpanel, 60, true);

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

	public GLJPanel getGljpanel() {
		return gljpanel;
	}

	public void setGljpanel(GLJPanel gljpanel) {
		this.gljpanel = gljpanel;
	}

	public void setEyes(Vector3d eyes) {
		this.eyes = eyes;
	}

	public Vector3d getEyes() {
		return eyes;
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

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if (!HelperVariable.traceCourbe) {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]); // Select Our Texture
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 10000);
		glu.gluLookAt(eyes.x, eyes.y, eyes.z, 0, 0, 0, 0, 1, 0);
		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		for (Matter m : univers.getListMatiere().values()) {
			if (!m.isDark()) {
				double theta = Math.atan2(m.getPoint().y - eyes.y,
						m.getPoint().x - eyes.x);
				double phi = Math.atan2(Math.pow(
						Math.pow(m.getPoint().x - eyes.x, 2)
								+ Math.pow(m.getPoint().y - eyes.y, 2), 0.5),
						(m.getPoint().z - eyes.z));
				double dx = Math.cos(theta) * Math.sin(phi);
				double dy = Math.sin(theta) * Math.sin(phi);
				double dz = Math.cos(phi);
				double r = m.getRayon() < 1 ? 1 : m.getRayon();
				gl.glLoadIdentity();
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);
				gl.glColor3d(m.getColor().x, m.getColor().y, m.getColor().z);
				gl.glBegin(GL2.GL_QUADS);
				gl.glTexCoord2d(0, 0);
				gl.glVertex3d(-r, -r, -r);
				gl.glTexCoord2d(1, 0);
				gl.glVertex3d(r, -r, -r);
				gl.glTexCoord2d(1, 1);
				gl.glVertex3d(r, r, r);
				gl.glTexCoord2d(0, 1);
				gl.glVertex3d(-r, r, r);
				gl.glEnd();
			}
		}
		gl.glEnd();
		gl.glFlush();
	}

	private void update() {
		univers.process();
	}

	private void LoadGLTextures(GL gl) { // Load Bitmaps And Convert To Textures
		com.sylvanoid.common.TextureReader.Texture texture = null;// Create
																	// Storage
																	// Space For
																	// The
																	// Texture
		try {
			texture = TextureReader.readTexture("resources/images/Star.bmp");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		gl.glGenTextures(1, textures, 0); // Create One Texture

		// Create Linear Filtered Texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(),
				texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture.getPixels());
	}

}