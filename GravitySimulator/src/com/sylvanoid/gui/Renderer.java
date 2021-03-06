package com.sylvanoid.gui;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import javax.vecmath.Vector3d;

import org.jcodec.api.awt.SequenceEncoder;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TextureReader;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.MatterPair;
import com.sylvanoid.joblib.Parameters;
import com.sylvanoid.joblib.Univers;

public class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private int textSize = 10;
    private double theta = net.jafama.FastMath.PI / 180;

    private GUIProgram guiProgram;
    private Parameters parameters;
    private Univers univers;
    private LinkedList<List<Vector3d[]>> forTrace = new LinkedList<List<Vector3d[]>>();
    private SequenceEncoder out;
    private GLU glu = new GLU();
    private int textures[] = new int[4]; // Storage For One textures
    private TextRenderer textRenderer;
    private java.awt.Point mousePoint = new Point(-1, -1);

    private double elapsedTimeOld = 0;

    private Thread thread;

    public Renderer(GUIProgram guiProgram) {
	reload(guiProgram);
    }

    private void restartUnivers() {
	stopUnivers();
	startUnivers();
    }

    private void stopUnivers() {
	try {
	    thread.interrupt();
	    while (thread.isAlive()) {
		// nothing
	    }
	} catch (Exception e) {
	    // nothing
	}
    }

    private void startUnivers() {
	thread = new Thread(univers);
	thread.start();
    }

    public void reload(GUIProgram guiProgram) {
	this.guiProgram = guiProgram;
	this.univers = guiProgram.getUnivers();
	this.forTrace = guiProgram.getForTrace();
	this.parameters = guiProgram.getParameters();
	this.out = guiProgram.getOut();

	restartUnivers();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
	// univers.process();
	if (parameters.isShowTrace()) {
	    List<Vector3d[]> tmpList = new ArrayList<Vector3d[]>();
	    for (Matter m : univers.getListMatter()) {
		Vector3d[] myLine = new Vector3d[2];
		myLine[0] = new Vector3d(m.getPointBefore().x, m.getPointBefore().y, m.getPointBefore().z);
		myLine[1] = new Vector3d(m.getPoint().x, m.getPoint().y, m.getPoint().z);
		tmpList.add(myLine);
	    }
	    forTrace.add(tmpList);
	    if (forTrace.size() > 1000) {
		forTrace.pollFirst();
	    }
	}

	render(drawable);

	if (parameters.isExportToVideo()) {
	    try {
		if (parameters.getElapsedTime() >= elapsedTimeOld
			+ parameters.getTimeFactor() * (1.0 / parameters.getVideoPicEveryCycle())) {
		    out.encodeImage((new Robot()).createScreenCapture(guiProgram.getBounds()));
		    elapsedTimeOld = parameters.getElapsedTime();
		}
	    } catch (IOException | AWTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else if (elapsedTimeOld != 0) {
	    elapsedTimeOld = 0;
	}
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
	GL2 gl = drawable.getGL().getGL2();
	gl.glShadeModel(GL2.GL_SMOOTH);
	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	gl.glClearDepth(0.0f);
	gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

	// gl.glBlendFunc (GL2.GL_ONE, GL2.GL_ONE);

	gl.glBlendEquationSeparate(GL2.GL_FUNC_ADD, GL2.GL_FUNC_ADD);
	gl.glBlendFuncSeparate(GL2.GL_ONE, GL2.GL_ONE, GL2.GL_ONE, GL2.GL_ONE);

	LoadGLTextures(gl);
	textRenderer = new TextRenderer(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, textSize));
	drawable.getAnimator().setUpdateFPSFrames(10, null);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int xstart, int ystart, int width, int height) {
    }

    private void render(GLAutoDrawable drawable) {
	if (parameters.isFollowCentroid()) {
	    Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
	    diffLookAt.negate();
	    Vector3d tmpvecScala = new Vector3d(univers.getGPoint().x * parameters.getScala(),
		    univers.getGPoint().y * parameters.getScala(), univers.getGPoint().z * parameters.getScala());
	    diffLookAt.add(tmpvecScala);
	    parameters.setEyes(diffLookAt);
	}
	if (parameters.isFollowMaxMass()) {

	    Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
	    diffLookAt.negate();
	    Vector3d tmpvecScala = new Vector3d(univers.getMaxMassElement().getPoint());
	    tmpvecScala = new Vector3d(tmpvecScala.x * parameters.getScala(), tmpvecScala.y * parameters.getScala(),
		    tmpvecScala.z * parameters.getScala());
	    diffLookAt.add(tmpvecScala);

	    parameters.setEyes(diffLookAt);

	}
	if (parameters.getObjectToFollow() != null) {
	    Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
	    diffLookAt.negate();
	    Vector3d tmpvecScala = new Vector3d(parameters.getObjectToFollow().getPoint());
	    tmpvecScala = new Vector3d(tmpvecScala.x * parameters.getScala(), tmpvecScala.y * parameters.getScala(),
		    tmpvecScala.z * parameters.getScala());
	    diffLookAt.add(tmpvecScala);
	    parameters.setEyes(diffLookAt);
	}
	if (parameters.isPermanentRotationy()) {
	    Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
	    parameters.setLookAt(
		    HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 1, 0), -net.jafama.FastMath.PI / 1000));
	    diffLookAt.sub(parameters.getLookAt());
	    parameters.getEyes().add(diffLookAt);
	}

	GL2 gl = drawable.getGL().getGL2();
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	// Perspective.
	float widthHeightRatio = (float) guiProgram.getWidth() / (float) guiProgram.getHeight();
	glu.gluPerspective(45, widthHeightRatio, 1, parameters.getNebulaRadius() * 1000);
	Vector3d centerOfVision = new Vector3d(parameters.getEyes());
	centerOfVision.add(parameters.getLookAt());
	glu.gluLookAt(parameters.getEyes().x, parameters.getEyes().y, parameters.getEyes().z, centerOfVision.x,
		centerOfVision.y, centerOfVision.z, 0, 1, 0);

	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadIdentity();
	gl.glTranslated(centerOfVision.x, centerOfVision.y, centerOfVision.z);

	if (parameters.isShowgrid()) {
	    // Show grid
	    drawGrid(gl);
	}

	if (parameters.isShowAxis()) {
	    // Show Axis
	    drawAxis(gl);
	}
	if (parameters.isShowInfo()) {
	    // Show info
	    drawInfo(drawable);
	}

	if (parameters.isShowTrace()) {
	    /* Show trace */
	    drawTrace(gl);
	}

	if (parameters.isShowViscosityRelationShip()) {
	    /* Show trace */
	    drawViscosityRelationShip(gl);

	}

	// drawGravitationalFields(gl);

	/* Show current univers */
	if (!parameters.isViewSimplePointOnly()) {
	    drawUnivers(gl);
	} else {
	    drawUniversSimplePoint(gl);
	}
	// drawUniversSimpleSphere(gl, glu);

    }

    private void LoadGLTextures(GL2 gl) {
	com.sylvanoid.common.TextureReader.Texture texture00 = null;
	com.sylvanoid.common.TextureReader.Texture texture01 = null;
	com.sylvanoid.common.TextureReader.Texture texture02 = null;
	com.sylvanoid.common.TextureReader.Texture texture03 = null;
	try {
	    texture00 = TextureReader.readTexture("images/Dark.png");
	    texture01 = TextureReader.readTexture("images/Star.bmp");
	    texture02 = TextureReader.readTexture("images/Planetary.png");
	    texture03 = TextureReader.readTexture("images/Galaxy.png");

	} catch (IOException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
	gl.glGenTextures(3, textures, 0);

	gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 3, texture00.getWidth(), texture00.getHeight(), 0, GL2.GL_RGB,
		GL2.GL_UNSIGNED_BYTE, texture00.getPixels());

	gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1]);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 3, texture01.getWidth(), texture01.getHeight(), 0, GL2.GL_RGB,
		GL2.GL_UNSIGNED_BYTE, texture01.getPixels());

	gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[2]);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 3, texture02.getWidth(), texture02.getHeight(), 0, GL2.GL_RGB,
		GL2.GL_UNSIGNED_BYTE, texture02.getPixels());

	gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[3]);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 3, texture03.getWidth(), texture03.getHeight(), 0, GL2.GL_RGB,
		GL2.GL_UNSIGNED_BYTE, texture03.getPixels());

    }

    private void drawGrid(GL2 gl) {
	double gridRadius = parameters.getNebulaRadius() * parameters.getScala() * 2;
	double gridStep = gridRadius / 10;
	Vector3d color = new Vector3d(0.08, 0.08, 0.08);
	for (double i = 0; i <= gridRadius; i += gridStep) {
	    for (double j = 0; j <= gridRadius; j += gridStep) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, j, -gridRadius);
		gl.glVertex3d(i, j, gridRadius);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, -gridRadius, j);
		gl.glVertex3d(i, gridRadius, j);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(-gridRadius, i, j);
		gl.glVertex3d(gridRadius, i, j);
		gl.glEnd();

	    }
	    for (double j = 0; j >= -gridRadius; j -= gridStep) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, j, -gridRadius);
		gl.glVertex3d(i, j, gridRadius);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, -gridRadius, j);
		gl.glVertex3d(i, gridRadius, j);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(-gridRadius, i, j);
		gl.glVertex3d(gridRadius, i, j);
		gl.glEnd();
	    }
	}
	for (double i = 0; i >= -gridRadius; i -= gridStep) {
	    for (double j = 0; j <= gridRadius; j += gridStep) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, j, -gridRadius);
		gl.glVertex3d(i, j, gridRadius);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, -gridRadius, j);
		gl.glVertex3d(i, gridRadius, j);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(-gridRadius, i, j);
		gl.glVertex3d(gridRadius, i, j);
		gl.glEnd();

	    }
	    for (double j = 0; j >= -gridRadius; j -= gridStep) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, j, -gridRadius);
		gl.glVertex3d(i, j, gridRadius);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(i, -gridRadius, j);
		gl.glVertex3d(i, gridRadius, j);
		gl.glEnd();

		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(-gridRadius, i, j);
		gl.glVertex3d(gridRadius, i, j);
		gl.glEnd();
	    }
	}
    }

    @SuppressWarnings("unused")
    private void drawGravitationalFields(GL2 gl) {
	double gridRadius = parameters.getNebulaRadius() * parameters.getScala() * 2;
	double gridStep = gridRadius / 10;
	Vector3d color = new Vector3d(0.5, 0.5, 0.5);
	for (double i = -gridRadius; i <= gridRadius; i += gridStep) {
	    for (double j = -gridRadius; j <= gridRadius; j += gridStep) {
		for (double k = -gridRadius; k <= gridRadius; k += gridStep) {
		    gl.glBegin(GL2.GL_POINTS);
		    gl.glColor3d(color.x, color.y, color.z);
		    gl.glVertex3d(i, j, k);
		    gl.glEnd();
		}
	    }
	}
    }

    private void drawAxis(GL2 gl) {
	double gridRadius = parameters.getNebulaRadius() * parameters.getScala();
	gl.glBegin(GL2.GL_LINES);
	gl.glColor3d(0.3, 0, 0);
	gl.glVertex3d(0, 0, -gridRadius);
	gl.glVertex3d(0, 0, gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glColor3d(0, 0.3, 0);
	gl.glVertex3d(0, -gridRadius, 0);
	gl.glVertex3d(0, gridRadius, 0);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glColor3d(0, 0, 0.3);
	gl.glVertex3d(-gridRadius, 0, 0);
	gl.glVertex3d(gridRadius, 0, 0);
	gl.glEnd();

	gl.glColor3d(0.1, 0.1, 0.1);
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, -gridRadius, -gridRadius);
	gl.glVertex3d(+gridRadius, -gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, -gridRadius, -gridRadius);
	gl.glVertex3d(+gridRadius, +gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, +gridRadius, -gridRadius);
	gl.glVertex3d(-gridRadius, +gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, +gridRadius, -gridRadius);
	gl.glVertex3d(-gridRadius, -gridRadius, -gridRadius);
	gl.glEnd();

	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, -gridRadius, +gridRadius);
	gl.glVertex3d(+gridRadius, -gridRadius, +gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, -gridRadius, +gridRadius);
	gl.glVertex3d(+gridRadius, +gridRadius, +gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, +gridRadius, +gridRadius);
	gl.glVertex3d(-gridRadius, +gridRadius, +gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, +gridRadius, +gridRadius);
	gl.glVertex3d(-gridRadius, -gridRadius, +gridRadius);
	gl.glEnd();

	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, -gridRadius, +gridRadius);
	gl.glVertex3d(-gridRadius, -gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, -gridRadius, +gridRadius);
	gl.glVertex3d(+gridRadius, -gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(+gridRadius, +gridRadius, +gridRadius);
	gl.glVertex3d(+gridRadius, +gridRadius, -gridRadius);
	gl.glEnd();
	gl.glBegin(GL2.GL_LINES);
	gl.glVertex3d(-gridRadius, +gridRadius, +gridRadius);
	gl.glVertex3d(-gridRadius, +gridRadius, -gridRadius);
	gl.glEnd();

    }

    private void drawInfo(GLAutoDrawable drawable) {
	DecimalFormat df2d = new DecimalFormat("0.0000");
	DecimalFormat dfsc = new DecimalFormat("0.####E0");
	textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
	textRenderer.setColor(0.7f, 0.7f, 0.7f, 1f);
	textRenderer.draw("Scala: 1/" + dfsc.format(1 / parameters.getScala()), 10,
		drawable.getSurfaceHeight() - textSize * 1);

	if (parameters.getElapsedTime() < HelperVariable.ONEHOUR) {
	    textRenderer.draw("Elapsed time (M): " + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEMINUTE),
		    10, drawable.getSurfaceHeight() - textSize * 2);
	} else if (parameters.getElapsedTime() < HelperVariable.ONEDAY) {
	    textRenderer.draw("Elapsed time (H): " + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEHOUR),
		    10, drawable.getSurfaceHeight() - textSize * 2);
    	} else if (parameters.getElapsedTime() < HelperVariable.ONEYEAR) {
	    textRenderer.draw("Elapsed time (day): " + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEDAY),
		    10, drawable.getSurfaceHeight() - textSize * 2);
	} else if (parameters.getElapsedTime() < HelperVariable.ONEYEAR * 1E6) {
	    textRenderer.draw(
		    "Elapsed time (year): " + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEYEAR), 10,
		    drawable.getSurfaceHeight() - textSize * 2);
	} else if (parameters.getElapsedTime() < HelperVariable.ONEYEAR * 1E9) {
	    textRenderer.draw(
		    "Elapsed time (millions of year): "
			    + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEYEAR / 1E6),
		    10, drawable.getSurfaceHeight() - textSize * 2);
	} else {
	    textRenderer.draw(
		    "Elapsed time (billions of year): "
			    + df2d.format(parameters.getElapsedTime() / HelperVariable.ONEYEAR / 1E9),
		    10, drawable.getSurfaceHeight() - textSize * 2);

	}
	if (parameters.getTimeFactor() < HelperVariable.ONEHOUR) {
	    textRenderer.draw("Time Step (M): " + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEMINUTE), 10,
		    drawable.getSurfaceHeight() - textSize * 3);
	} else if (parameters.getTimeFactor() < HelperVariable.ONEDAY) {
	    textRenderer.draw("Time Step (H): " + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEHOUR), 10,
		    drawable.getSurfaceHeight() - textSize * 3);
	} else if (parameters.getTimeFactor() < HelperVariable.ONEYEAR) {
	    textRenderer.draw("Time Step (day): " + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEDAY), 10,
		    drawable.getSurfaceHeight() - textSize * 3);
	} else if (parameters.getTimeFactor() < HelperVariable.ONEYEAR * 1E6) {
	    textRenderer.draw("Time Step (year): " + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEYEAR),
		    10, drawable.getSurfaceHeight() - textSize * 3);
	} else if (parameters.getTimeFactor() < HelperVariable.ONEYEAR * 1E9) {
	    textRenderer.draw(
		    "Time Step (millions of year): "
			    + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEYEAR / 1E6),
		    10, drawable.getSurfaceHeight() - textSize * 3);

	} else {
	    textRenderer.draw(
		    "Time Step (billions of year): "
			    + df2d.format(parameters.getTimeFactor() / HelperVariable.ONEYEAR / 1E9),
		    10, drawable.getSurfaceHeight() - textSize * 3);
	}
	textRenderer.draw("Num of Object: " + univers.getListMatter().size(), 10,
		drawable.getSurfaceHeight() - textSize * 4);
	textRenderer.draw(
		"Maximum Mass Object (M): " + dfsc.format(univers.getMaxMassElement().getMass() / HelperVariable.M), 10,
		drawable.getSurfaceHeight() - textSize * 5);
	textRenderer.draw("Univers visible mass (M): " + dfsc.format(univers.getVisibleMass() / HelperVariable.M), 10,
		drawable.getSurfaceHeight() - textSize * 6);
	textRenderer.draw("Univers dark mass (M): " + dfsc.format(univers.getDarkMass() / HelperVariable.M), 10,
		drawable.getSurfaceHeight() - textSize * 7);
	textRenderer.draw("Univers total mass (M): " + dfsc.format(univers.getMass() / HelperVariable.M), 10,
		drawable.getSurfaceHeight() - textSize * 8);

	textRenderer.draw("Num of recursive Barnes Hut computed: " + parameters.getNumOfCompute(), 10,
		drawable.getSurfaceHeight() - textSize * 10);
	textRenderer.draw("Num of acceleration computed: " + parameters.getNumOfAccelCompute(), 10,
		drawable.getSurfaceHeight() - textSize * 11);
	textRenderer.draw("Cycle compute time (ms): " + parameters.getCycleComputeTime(), 10,
		drawable.getSurfaceHeight() - textSize * 12);
	textRenderer.draw("Limit compute time (ms): " + parameters.getLimitComputeTime(), 10,
		drawable.getSurfaceHeight() - textSize * 13);
	textRenderer.draw("Barnes Hut compute time (ms): " + parameters.getBarnesHuttComputeTime(), 10,
		drawable.getSurfaceHeight() - textSize * 14);
	textRenderer.draw("Move compute time (ms): " + parameters.getMoveComputeTime(), 10,
		drawable.getSurfaceHeight() - textSize * 15);

	textRenderer.draw("K (j): " + dfsc.format(parameters.getKlength()), 10,
		drawable.getSurfaceHeight() - textSize * 17);
	textRenderer.draw("P: " + dfsc.format(parameters.getPlength()), 10,
		drawable.getSurfaceHeight() - textSize * 18);

	textRenderer.draw("FPS: " + df2d.format(drawable.getAnimator().getLastFPS()), 10, 10);

	textRenderer.draw("https://github.com/SylvainBillot/GravitySimulator", drawable.getSurfaceWidth() - 275, 10);
	textRenderer.endRendering();
    }

    private void drawTrace(GL2 gl) {
	gl.glPushMatrix();
	gl.glColor3d(0.20, 0.20, 0.20);
	for (List<Vector3d[]> tmpList : forTrace) {
	    for (Vector3d[] p : tmpList) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(parameters.getScala() * p[0].x, parameters.getScala() * p[0].y,
			parameters.getScala() * p[0].z);
		gl.glVertex3d(parameters.getScala() * p[1].x, parameters.getScala() * p[1].y,
			parameters.getScala() * p[1].z);
		gl.glEnd();
	    }
	}
	gl.glPopMatrix();
    }

    private void drawViscosityRelationShip(GL2 gl) {
	gl.glPushMatrix();
	Vector3d color = new Vector3d(0, 0.4, 0);

	for (MatterPair mp : univers.getCollisionPairsRenderer()) {
	    gl.glBegin(GL2.GL_LINES);
	    gl.glColor3d(color.x, color.y, color.z);
	    gl.glVertex3d(parameters.getScala() * mp.getM1().getPoint().x,
		    parameters.getScala() * mp.getM1().getPoint().y, parameters.getScala() * mp.getM1().getPoint().z);
	    gl.glVertex3d(parameters.getScala() * mp.getM2().getPoint().x,
		    parameters.getScala() * mp.getM2().getPoint().y, parameters.getScala() * mp.getM2().getPoint().z);
	    gl.glEnd();
	}
	gl.glPopMatrix();

    }

    private void drawUniversSimplePoint(GL2 gl) {
	gl.glPushMatrix();
	for (Matter m : univers.getListMatter()) {
	    if (m.getTypeOfObject().equals(TypeOfObject.Matter) && parameters.isShowMatter()
		    || m.getTypeOfObject().equals(TypeOfObject.Gas) && parameters.isShowGas()
		    || m.getTypeOfObject().equals(TypeOfObject.Dark) && parameters.isShowDarkMatter()) {
		gl.glBegin(GL2.GL_POINTS);
		gl.glColor3d(255, 255, 255);
		gl.glVertex3d(parameters.getScala() * m.getPoint().x, parameters.getScala() * m.getPoint().y,
			parameters.getScala() * m.getPoint().z);
		gl.glEnd();
	    }
	}
    }

    @SuppressWarnings("unused")
    private void drawUniversSimpleSphere(GL2 gl, GLU glu) {
	gl.glEnable(GL2.GL_POINT);
	gl.glPushMatrix();
	for (Matter m : univers.getListMatter()) {
	    if (m.getTypeOfObject().equals(TypeOfObject.Matter) && parameters.isShowMatter()
		    || m.getTypeOfObject().equals(TypeOfObject.Gas) && parameters.isShowGas()
		    || m.getTypeOfObject().equals(TypeOfObject.Dark) && parameters.isShowDarkMatter()) {
		gl.glLoadIdentity();
		double r = 0;
		r = (m.getRadius() * parameters.getScala() < 1 ? 1 : m.getRadius() * parameters.getScala());

		gl.glTranslated(parameters.getScala() * m.getPoint().x, parameters.getScala() * m.getPoint().y,
			parameters.getScala() * m.getPoint().z);

		gl.glColor3d(m.getColor().x, m.getColor().y, m.getColor().z);
		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(quad, GLU.GLU_OUTSIDE);
		glu.gluSphere(quad, r, 16, 16);
		glu.gluSphere(quad, r, 4, 4);
		glu.gluDeleteQuadric(quad);

		gl.glEnd();
	    }
	}
	gl.glDisable(GL2.GL_POINT);

    }

    private void drawUnivers(GL2 gl) {
	gl.glEnable(GL2.GL_BLEND);
	gl.glEnable(GL2.GL_TEXTURE_2D);
	gl.glPushMatrix();
	for (Matter m : univers.getListMatter()) {
	    if (m.getTypeOfObject().equals(TypeOfObject.Matter) && parameters.isShowMatter()
		    || m.getTypeOfObject().equals(TypeOfObject.Gas) && parameters.isShowGas()
		    || m.getTypeOfObject().equals(TypeOfObject.Dark) && parameters.isShowDarkMatter()) {
		gl.glLoadIdentity();
		double r = 0;
		switch (m.getTypeOfObject()) {
		case Matter:
		    gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[2]);
		    r = (parameters.getMatterRendererExtender() * m.getRadius() * parameters.getScala() < 1 ? 1
			    : parameters.getMatterRendererExtender() * m.getRadius() * parameters.getScala());
		    break;

		case Gas:
		    gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[2]);
		    r = (parameters.getGasRendererExtender() * m.getRadius() * parameters.getScala() < 1 ? 1
			    : parameters.getGasRendererExtender() * m.getRadius() * parameters.getScala());
		    break;

		case Dark:
		    gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]);
		    r = parameters.getDarkMatterRendererExtender() * m.getRadius() * parameters.getScala();
		    break;

		default:
		    break;
		}

		gl.glTranslated(parameters.getScala() * m.getPoint().x, parameters.getScala() * m.getPoint().y,
			parameters.getScala() * m.getPoint().z);

		double phi01 = new Vector3d(0, 0, 1).angle(parameters.getLookAt())
			* -net.jafama.FastMath.signum(parameters.getLookAt().y);
		Vector3d afterRotateX = HelperVector.rotate(new Vector3d(0, 0, 1), new Vector3d(1, 0, 0), phi01);
		double phi02 = afterRotateX.angle(parameters.getLookAt())
			* net.jafama.FastMath.signum(parameters.getLookAt().x);

		gl.glMultMatrixd(HelperVector.make3DTransformMatrix(
			new Vector3d(-phi01, -phi02, net.jafama.FastMath.random() * 2 * net.jafama.FastMath.PI)));

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
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
	processKeyEvent(e, true);
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {

    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {

    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
	Vector3d diffLookAt = new Vector3d(parameters.getLookAt());

	switch (e.getKeyCode()) {
	case KeyEvent.VK_PAGE_UP:
	    parameters.setScala(parameters.getScala() * 1.01);
	    break;
	case KeyEvent.VK_PAGE_DOWN:
	    parameters.setScala(parameters.getScala() * (1 / 1.01));
	    break;
	case KeyEvent.VK_LEFT:
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 1, 0), -theta));
	    diffLookAt.sub(parameters.getLookAt());
	    parameters.getEyes().add(diffLookAt);
	    break;
	case KeyEvent.VK_RIGHT:
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 1, 0), theta));
	    diffLookAt.sub(parameters.getLookAt());
	    parameters.getEyes().add(diffLookAt);
	    break;
	case KeyEvent.VK_UP:
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(1, 0, 0), -theta));
	    diffLookAt.sub(parameters.getLookAt());
	    parameters.getEyes().add(diffLookAt);
	    break;
	case KeyEvent.VK_DOWN:
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(1, 0, 0), theta));
	    diffLookAt.sub(parameters.getLookAt());
	    parameters.getEyes().add(diffLookAt);
	    break;
	case KeyEvent.VK_HOME:
	    parameters.setEyes(new Vector3d(0, 0, 900));
	    parameters.setLookAt(new Vector3d(0.001, 0, -900));
	    break;
	}

	switch (e.getKeyChar()) {
	case '+':
	    parameters.setTimeFactor(parameters.getTimeFactor() * 1.01);
	    break;
	case '-':
	    parameters.setTimeFactor(parameters.getTimeFactor() / 1.01);
	    break;
	case '4':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 1, 0), theta));
	    break;
	case '6':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 1, 0), -theta));
	    break;
	case '8':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(1, 0, 0), theta));
	    break;
	case '2':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(1, 0, 0), -theta));
	    break;
	case '1':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 0, 1), theta));
	    break;
	case '3':
	    parameters.setFollowCentroid(false);
	    parameters.setFollowMaxMass(false);
	    parameters.setObjectToFollow(null);
	    parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), new Vector3d(0, 0, 1), -theta));
	    break;
	}
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	// TODO Select an object and center on

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
	mousePoint = new Point(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
	double dx = (double) (mousePoint.x - e.getX()) / 3;
	double dy = (double) (mousePoint.y - e.getY()) / 3;

	Vector3d axis1 = new Vector3d(0, 1, 0);
	Vector3d axis2 = new Vector3d(0, 1, 0);
	axis2.cross(parameters.getLookAt(), axis2);

	Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
	parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), axis1, theta * dx));
	parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(), axis2, theta * dy));
	diffLookAt.sub(parameters.getLookAt());
	parameters.getEyes().add(diffLookAt);

	mousePoint = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	if (e.getPreciseWheelRotation() == 1) {
	    parameters.setScala(parameters.getScala() * (1 / 1.05));
	} else {
	    parameters.setScala(parameters.getScala() * 1.05);
	}
    }

}
