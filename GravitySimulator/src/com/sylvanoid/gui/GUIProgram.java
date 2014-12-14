package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jcodec.api.SequenceEncoder;

import com.jogamp.opengl.util.FPSAnimator;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
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

	private int textures[] = new int[2]; // Storage For One textures

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
		JMenuItem menuItemExport = new JMenuItem("Export univer ...");
		menuItemExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animator.stop();
				try {
					JAXBContext context = JAXBContext.newInstance(Univers.class);
					Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					m.marshal(univers, System.out);
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				animator.start();
			}
		});
		JMenuItem menuItemImport = new JMenuItem("Import univers ...");
		menuItemImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animator.stop();
				animator.start();
			}
		});
		menuFichier.add(menuItemBaseParam);
		menuFichier.add(menuItemExport);
		menuFichier.add(menuItemImport);

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
		menuProcess.add(menuItemStart);
		menuProcess.add(menuItemStop);
		menuProcess.add(menuItemReset);

		JMenu menuVisu = new JMenu("View");
		menuBar.add(menuVisu);
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
				gl.glEnable(GL2.GL_TEXTURE_2D);
				gl.glShadeModel(GL2.GL_SMOOTH);
				gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
				gl.glClearDepth(1.0f);
				gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
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
				if (HelperVariable.exportToVideo) {
					GL2 gl = drawable.getGL().getGL2();
					BufferedImage img = toImage(gl, drawable.getSurfaceWidth(),
							drawable.getSurfaceHeight());
					try {
						me.getOut().encodeImage(img);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				update();
			}
		});
		InputHandler inputHandler = new InputHandler(this);
		gljpanel.addKeyListener(inputHandler);
		me.add(gljpanel, BorderLayout.CENTER);
		animator = new FPSAnimator(gljpanel, 60, true);
		animator.start();
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

	public FPSAnimator getAminator() {
		return animator;
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
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL2.GL_BLEND);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 10000);
		glu.gluLookAt(eyes.x, eyes.y, eyes.z, 0, 0, 0, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		double phi01 = new Vector3d(0, 0, 1).angle(eyes) * -Math.signum(eyes.y);
		Vector3d afterRotateX = HelperVector.rotate(new Vector3d(0, 0, 1),
				new Vector3d(1, 0, 0), phi01);
		double phi02 = afterRotateX.angle(eyes) * Math.signum(eyes.x);
		for (Matter m : univers.getListMatiere().values()) {
			if (!m.isDark()) {
				gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]); // Select Our
																	// Texture
				double r = (0.5 * Math.random() + 4.5)
						* (m.getRayon() < 1 ? 1 : m.getRayon());
				Vector3d[] pts = new Vector3d[4];
				pts[0] = new Vector3d(-r, -r, 0); // BL
				pts[1] = new Vector3d(r, -r, 0); // BR
				pts[2] = new Vector3d(r, r, 0); // TR
				pts[3] = new Vector3d(-r, r, 0); // TL
				gl.glLoadIdentity();
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);
				gl.glRotated(phi01 * 180 / Math.PI, 1, 0, 0);
				gl.glRotated(phi02 * 180 / Math.PI, 0, 1, 0);
				gl.glRotated(Math.random() * 360, 0, 0, 1);
				gl.glColor3d(m.getColor().x, m.getColor().y, m.getColor().z);
				gl.glBegin(GL2.GL_QUADS);
				gl.glTexCoord2d(0, 0);
				gl.glVertex3d(pts[0].x, pts[0].y, pts[0].z);
				gl.glTexCoord2d(1, 0);
				gl.glVertex3d(pts[1].x, pts[1].y, pts[1].z);
				gl.glTexCoord2d(1, 1);
				gl.glVertex3d(pts[2].x, pts[2].y, pts[2].z);
				gl.glTexCoord2d(0, 1);
				gl.glVertex3d(pts[3].x, pts[3].y, pts[3].z);
				gl.glEnd();
			} else {
				// If you want show dark mass, code here
				gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]); // Select Our
																	// Texture
				double r = 5 * m.getRayon();
				Vector3d[] pts = new Vector3d[4];
				pts[0] = new Vector3d(-r, -r, 0); // BL
				pts[1] = new Vector3d(r, -r, 0); // BR
				pts[2] = new Vector3d(r, r, 0); // TR
				pts[3] = new Vector3d(-r, r, 0); // TL
				gl.glLoadIdentity();
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);
				gl.glRotated(phi01 * 180 / Math.PI, 1, 0, 0);
				gl.glRotated(phi02 * 180 / Math.PI, 0, 1, 0);
				gl.glRotated(Math.random() * 360, 0, 0, 1);
				gl.glColor3d(0.15, 0.15, 0.15);
				gl.glBegin(GL2.GL_QUADS);
				gl.glTexCoord2d(0, 0);
				gl.glVertex3d(pts[0].x, pts[0].y, pts[0].z);
				gl.glTexCoord2d(1, 0);
				gl.glVertex3d(pts[1].x, pts[1].y, pts[1].z);
				gl.glTexCoord2d(1, 1);
				gl.glVertex3d(pts[2].x, pts[2].y, pts[2].z);
				gl.glTexCoord2d(0, 1);
				gl.glVertex3d(pts[3].x, pts[3].y, pts[3].z);
				gl.glEnd();
			}
		}
		gl.glDisable(GL2.GL_BLEND);
	}

	private void update() {
		univers.process();
	}

	private void LoadGLTextures(GL gl) {
		com.sylvanoid.common.TextureReader.Texture texture01 = null;
		com.sylvanoid.common.TextureReader.Texture texture02 = null;
		try {
			texture01 = TextureReader.readTexture("resources/images/Star.bmp");
			texture02 = TextureReader
					.readTexture("resources/images/Particle.png");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture01.getWidth(),
				texture01.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture01.getPixels());
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture02.getWidth(),
				texture02.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture02.getPixels());

	}

	private BufferedImage toImage(GL2 gl, int w, int h) {
		gl.glReadBuffer(GL.GL_FRONT); // or GL.GL_BACK
		ByteBuffer glBB = ByteBuffer.allocate(3 * w * h);
		gl.glReadPixels(0, 0, w, h, GL2.GL_BGR, GL2.GL_BYTE, glBB);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		int[] bd = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int b = 2 * glBB.get();
				int g = 2 * glBB.get();
				int r = 2 * glBB.get();
				bd[(h - y - 1) * w + x] = (r << 16) | (g << 8) | b | 0xFF000000;
			}
		}
		return bi;
	}
}