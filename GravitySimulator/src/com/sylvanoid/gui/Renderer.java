package com.sylvanoid.gui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3d;

import org.jcodec.api.SequenceEncoder;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TextureReader;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Parameters;
import com.sylvanoid.joblib.Univers;

public class Renderer implements GLEventListener {

	private int textSize = 10;

	private GUIProgram guiProgram;
	private Parameters parameters;
	private Univers univers;
	private SequenceEncoder out;
	private GLU glu = new GLU();
	private int textures[] = new int[3]; // Storage For One textures

	private TextRenderer textRenderer;

	public Renderer(GUIProgram guiProgram) {
		reload(guiProgram);
	}

	public void reload(GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
		this.univers = guiProgram.getUnivers();
		this.parameters = guiProgram.getParameters();
		this.out = guiProgram.getOut();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		render(drawable);
		if (parameters.isExportToVideo()) {
			GL2 gl = drawable.getGL().getGL2();
			BufferedImage img = toImage(gl, drawable.getSurfaceWidth(),
					drawable.getSurfaceHeight());
			try {
				out.encodeImage(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		univers.process();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_COLOR);
		LoadGLTextures(gl);
		textRenderer = new TextRenderer(new java.awt.Font("SansSerif",
				java.awt.Font.PLAIN, textSize));
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int xstart, int ystart,
			int width, int height) {
		// TODO Auto-generated method stub
		final GL2 gl = drawable.getGL().getGL2();

		if (height <= 0) // avoid a divide by zero error!
			height = 1;
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45, h, 1.0, 1E5);
		Vector3d centerOfVision = new Vector3d(parameters.getEyes());
		centerOfVision.add(parameters.getLookAt());
		glu.gluLookAt(parameters.getEyes().x, parameters.getEyes().y,
				parameters.getEyes().z, centerOfVision.x, centerOfVision.y,
				centerOfVision.z, 0, 1, 0);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	private void render(GLAutoDrawable drawable) {
		parameters.setElapsedTime(parameters.getElapsedTime()+parameters.getTimeFactor());
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float) guiProgram.getWidth()
				/ (float) guiProgram.getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1E5);
		Vector3d centerOfVision = new Vector3d(parameters.getEyes());
		centerOfVision.add(parameters.getLookAt());
		glu.gluLookAt(parameters.getEyes().x, parameters.getEyes().y,
				parameters.getEyes().z, centerOfVision.x, centerOfVision.y,
				centerOfVision.z, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslated(centerOfVision.x, centerOfVision.y, centerOfVision.z);
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(0.3, 0.0, 0.0);
		gl.glVertex3d(0.0, 0, -1E5);
		gl.glVertex3d(0.0, 0, 1E5);
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(0, 0.3, 0.0);
		gl.glVertex3d(0.0, -1E5, 0);
		gl.glVertex3d(0.0, 1E5, 0);
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(0, 0, 0.3);
		gl.glVertex3d(-1E5, 0, 0);
		gl.glVertex3d(1E5, 0, 0);
		gl.glEnd();

		textRenderer.beginRendering(drawable.getSurfaceWidth(),
				drawable.getSurfaceHeight());
		textRenderer.setColor(0.7f, 0.7f, 0.7f, 1f);
		textRenderer.draw("Elapsed time: " + parameters.getElapsedTime(), 10,
				drawable.getSurfaceHeight() - textSize*1);
		textRenderer.draw("Time Speed: " + parameters.getTimeFactor(), 10,
				drawable.getSurfaceHeight() - textSize*2);
		textRenderer.endRendering();

		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glPushMatrix();
		for (Matter m : univers.getListMatter().values()) {
			if (!m.isDark()) {
				gl.glLoadIdentity();
				if(m.getTypeOfObject()==TypeOfObject.Star){
					gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
				}
				if(m.getTypeOfObject()==TypeOfObject.Planetary){
					gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
				}
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);

				double phi01 = new Vector3d(0, 0, 1).angle(parameters
						.getLookAt()) * -Math.signum(parameters.getLookAt().y);
				Vector3d afterRotateX = HelperVector.rotate(new Vector3d(0, 0,
						1), new Vector3d(1, 0, 0), phi01);
				double phi02 = afterRotateX.angle(parameters.getLookAt())
						* Math.signum(parameters.getLookAt().x);
				gl.glMultMatrixd(HelperVector
						.make3DTransformMatrix(new Vector3d(-phi01, -phi02,
								Math.random() * 2 * Math.PI)));

				double r = (Math.random() * 0.5 + 4.5)
						* (m.getRayon() < 1 ? 1 : m.getRayon());
				Vector3d[] pts = new Vector3d[4];
				pts[0] = new Vector3d(-r, -r, 0); // BL
				pts[1] = new Vector3d(r, -r, 0); // BR
				pts[2] = new Vector3d(r, r, 0); // TR
				pts[3] = new Vector3d(-r, r, 0); // TL
				gl.glColor3d(m.getColor().x, m.getColor().y, m.getColor().z);
				gl.glBegin(GL2.GL_TRIANGLE_FAN);
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
				gl.glLoadIdentity();
				gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
				gl.glTranslated(m.getPoint().x, m.getPoint().y, m.getPoint().z);

				double phi01 = new Vector3d(0, 0, 1).angle(parameters
						.getLookAt()) * -Math.signum(parameters.getLookAt().y);
				Vector3d afterRotateX = HelperVector.rotate(new Vector3d(0, 0,
						1), new Vector3d(1, 0, 0), phi01);
				double phi02 = afterRotateX.angle(parameters.getLookAt())
						* Math.signum(parameters.getLookAt().x);
				gl.glMultMatrixd(HelperVector
						.make3DTransformMatrix(new Vector3d(-phi01, -phi02, 0)));
				double r = 5 * m.getRayon();
				Vector3d[] pts = new Vector3d[4];
				pts[0] = new Vector3d(-r, -r, 0); // BL
				pts[1] = new Vector3d(r, -r, 0); // BR
				pts[2] = new Vector3d(r, r, 0); // TR
				pts[3] = new Vector3d(-r, r, 0); // TL
				gl.glColor3d(m.getColor().x, m.getColor().y, m.getColor().z);
				gl.glBegin(GL2.GL_TRIANGLE_FAN);
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
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}

	private void LoadGLTextures(GL gl) {
		com.sylvanoid.common.TextureReader.Texture texture00 = null;
		com.sylvanoid.common.TextureReader.Texture texture01 = null;
		com.sylvanoid.common.TextureReader.Texture texture02 = null;
		try {
			texture00 = TextureReader
					.readTexture("resources/images/Dark.png");
			texture01 = TextureReader.readTexture("resources/images/Star.bmp");
			texture02 = TextureReader
					.readTexture("resources/images/Planetary.png");

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		gl.glGenTextures(2, textures, 0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture00.getWidth(),
				texture00.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture00.getPixels());

		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture01.getWidth(),
				texture01.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE,
				texture01.getPixels());

		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
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
