package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.vecmath.Vector3d;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jcodec.api.SequenceEncoder;

import com.jogamp.opengl.util.FPSAnimator;
import com.sylvanoid.common.XmlFilter;
import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Parameters;
import com.sylvanoid.joblib.Univers;

public class GUIProgram extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIProgram me;
	private GLJPanel gljpanel;
	private Univers univers;
	private LinkedList<List<Vector3d[]>> forTrace;
	private Parameters parameters;
	private final FPSAnimator animator;
	private SequenceEncoder out;
	Renderer renderer;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
				    // If Nimbus is not available, you can set the GUI to another look and feel.
				}
				GUIProgram ex = new GUIProgram();
				ex.setVisible(true);
			}
		});
	}

	public GUIProgram() {
		this.me = this;
		parameters = new Parameters();
		univers = new Univers(parameters);
		forTrace = new LinkedList<List<Vector3d[]>>();
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
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		JMenu menuFichier = new JMenu("File");
		menuBar.add(menuFichier);
		JMenuItem menuItemBaseParam = new JMenuItem("Some random unvivers ...");
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
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new XmlFilter());
					int userSelection = fileChooser.showSaveDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						OutputStream output = new FileOutputStream(fileChooser
								.getSelectedFile().getAbsolutePath());
						JAXBContext context = JAXBContext
								.newInstance(Univers.class);
						Marshaller m = context.createMarshaller();
						m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						m.marshal(univers, output);
						output.close();
					}
				} catch (JAXBException | IOException e1) {
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
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new XmlFilter());
					fileChooser.setDialogTitle("Specify a file to load");
					int userSelection = fileChooser.showOpenDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File file = new File(fileChooser.getSelectedFile()
								.getAbsolutePath());
						JAXBContext jaxbContext = JAXBContext
								.newInstance(Univers.class);
						Unmarshaller jaxbUnmarshaller = jaxbContext
								.createUnmarshaller();
						univers = (Univers) jaxbUnmarshaller.unmarshal(file);
						renderer.reload(me);
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				me.setParameters(univers.getParameters());
				for (Matter m : univers.getListMatter().values()) {
					m.setParameters(univers.getParameters());
				}
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
		JMenuItem menuItemStopFollow = new JMenuItem("Stop following");
		menuItemStopFollow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
			}
		});
		JMenuItem menuItemCentreEcran = new JMenuItem("Look at 0");
		menuItemCentreEcran.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
				Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
				diffLookAt.negate();
				parameters.setEyes(diffLookAt);
				renderer.reload(me);
			}
		});
		JMenuItem menuItemplusMassif = new JMenuItem("Follow maximum mass");
		menuItemplusMassif.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(true);
				parameters.setObjectToFollow(null);
			}
		});

		JMenuItem menuItemBarycentre = new JMenuItem("Follow centroid");
		menuItemBarycentre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setFollowCentroid(true);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
			}
		});

		JMenuItem menuItemFollowSomething = new JMenuItem("Follow something ...");
		menuItemFollowSomething.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animator.stop();
				GUIFollowOther guiFO = new GUIFollowOther(me);
				guiFO.setVisible(true);
			}
		});	
		
		JCheckBoxMenuItem menuItemShowTrace = new JCheckBoxMenuItem(
				"Show Trace", parameters.isShowTrace());
		menuItemShowTrace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setShowTrace(!parameters
						.isShowTrace());
				me.forTrace = new LinkedList<List<Vector3d[]>>();
			}
		});
		
		JCheckBoxMenuItem menuItemPermanentRotationy = new JCheckBoxMenuItem(
				"Permanent Y Rotation", parameters.isPermanentRotationy());
		menuItemPermanentRotationy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setPermanentRotationy(!parameters
						.isPermanentRotationy());
			}
		});

		JCheckBoxMenuItem menuItemShowAxis = new JCheckBoxMenuItem(
				"Show Axis", parameters.isShowAxis());
		menuItemShowAxis.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setShowAxis(!parameters
						.isShowAxis());
			}
		});

		JCheckBoxMenuItem menuItemShowGrid = new JCheckBoxMenuItem(
				"Show grids", parameters.isShowgrid());
		menuItemShowGrid.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setShowgrid(!parameters
						.isShowgrid());
			}
		});

		JCheckBoxMenuItem menuItemShowInfo = new JCheckBoxMenuItem(
				"Show info", parameters.isShowInfo());
		menuItemShowInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setShowInfo(!parameters
						.isShowInfo());
			}
		});

		JCheckBoxMenuItem menuItemShowDM = new JCheckBoxMenuItem(
				"Show dark matter", parameters.isShowDarkMatter());
		menuItemShowDM.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setShowDarkMatter(!parameters
						.isShowDarkMatter());
			}
		});
		
		menuVisu.add(menuItemShowTrace);
		menuVisu.add(menuItemPermanentRotationy);
		menuVisu.add(menuItemStopFollow);
		menuVisu.add(menuItemCentreEcran);
		menuVisu.add(menuItemplusMassif);
		menuVisu.add(menuItemBarycentre);
		menuVisu.add(menuItemFollowSomething);
		menuVisu.add("");
		menuVisu.add(menuItemShowInfo);
		menuVisu.add(menuItemShowAxis);
		menuVisu.add(menuItemShowGrid);
		menuVisu.add(menuItemShowDM);

		JMenu menuVideo = new JMenu("Video");
		JCheckBoxMenuItem menuItemExportVideo = new JCheckBoxMenuItem(
				"Record to [home directory/out.mpeg]",
				parameters.isExportToVideo());
		menuItemExportVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setExportToVideo(!parameters.isExportToVideo());
				renderer.reload(me);
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
		renderer = new Renderer(this);
		gljpanel.addGLEventListener(renderer);
		gljpanel.addKeyListener(renderer);
		gljpanel.addMouseListener(renderer);
		gljpanel.addMouseMotionListener(renderer);
		me.add(gljpanel, BorderLayout.CENTER);
		animator = new FPSAnimator(gljpanel, 25, true);
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

	public LinkedList<List<Vector3d[]>> getForTrace() {
		return forTrace;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public GLJPanel getGljpanel() {
		return gljpanel;
	}

	public void setGljpanel(GLJPanel gljpanel) {
		this.gljpanel = gljpanel;
	}

	public FPSAnimator getAminator() {
		return animator;
	}

	public void reset() {
		forTrace = new LinkedList<List<Vector3d[]>>();
		animator.stop();
		parameters.setElapsedTime(0);
		univers = new Univers(parameters);
		renderer.reload(this);
		animator.start();
	}

	public SequenceEncoder getOut() {
		return out;
	}
}