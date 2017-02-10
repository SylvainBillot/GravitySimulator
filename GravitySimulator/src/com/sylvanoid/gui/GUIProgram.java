package com.sylvanoid.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

import org.jcodec.api.awt.SequenceEncoder;

import com.jogamp.opengl.util.FPSAnimator;
import com.sylvanoid.common.DataFilter;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.MpejFilter;
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
	private BufferedWriter dataFile;
	private BufferedReader dataFileInput;
	private Renderer renderer;

	public static void main(String[] args) {

		EventQueue.invokeLater(onStart());
	}

	public GUIProgram() {
		this.me = this;
		parameters = new Parameters();
		univers = new Univers(parameters);
		forTrace = new LinkedList<List<Vector3d[]>>();
		builder();
		animator = new FPSAnimator(gljpanel, HelperVariable.MAXFPS, true);
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
		parameters.setEyes(new Vector3d(0, 0, 900));
		parameters.setLookAt(new Vector3d(0, 0, -900));
		univers = new Univers(parameters);
		renderer.reload(this);
		animator.start();
	}

	public SequenceEncoder getOut() {
		return out;
	}

	public BufferedWriter getDatafile() {
		return dataFile;
	}

	public BufferedReader getDataInputfile() {
		return dataFileInput;
	}

	private void builder() {
		setTitle("Gravity Simulator");
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setSize((int) dimension.getWidth(), (int) dimension.getHeight());
		setLocationRelativeTo(null);
		addWindowListener(onClose());
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		JMenu menuFichier = new JMenu("File");
		menuBar.add(menuFichier);
		JMenuItem menuItemBaseParam = new JMenuItem("Some random unvivers ...");
		menuItemBaseParam.addActionListener(menuParameters());
		JMenuItem menuItemExport = new JMenuItem("Export univer ...");
		menuItemExport.addActionListener(menuExport());
		JMenuItem menuItemImport = new JMenuItem("Import univers ...");
		menuItemImport.addActionListener(menuImport());
		JMenuItem menuItemAddMatterList = new JMenuItem("Add univers ...");
		menuItemAddMatterList.addActionListener(menuAddMatterList());
		JMenuItem menuItemExportParams = new JMenuItem("Export parameters ...");
		menuItemExportParams.addActionListener(menuExportParams());
		JMenuItem menuItemImportParams = new JMenuItem("Import parameters ...");
		menuItemImportParams.addActionListener(menuImportParams());
		menuFichier.add(menuItemBaseParam);
		menuFichier.add(menuItemExport);
		menuFichier.add(menuItemImport);
		menuFichier.add(menuItemAddMatterList);
		menuFichier.add(menuItemExportParams);
		menuFichier.add(menuItemImportParams);

		JMenu menuProcess = new JMenu("Process");
		menuBar.add(menuProcess);
		JMenuItem menuItemStart = new JMenuItem("Start");
		menuItemStart.addActionListener(menuStart());
		JMenuItem menuItemStop = new JMenuItem("Stop");
		menuItemStop.addActionListener(menuStop());
		JMenuItem menuItemReset = new JMenuItem("Reset");
		menuItemReset.addActionListener(menuReset());
		menuProcess.add(menuItemStart);
		menuProcess.add(menuItemStop);
		menuProcess.add(menuItemReset);

		JMenu menuVisu = new JMenu("View");
		menuBar.add(menuVisu);
		JCheckBoxMenuItem menuViewSimplePoint = new JCheckBoxMenuItem(
				"View simples points", parameters.isViewSimplePointOnly());
		menuViewSimplePoint.addActionListener(menuViewSimplePoint());
		JMenuItem menuItemStopFollow = new JMenuItem("Stop following");
		menuItemStopFollow.addActionListener(menuStopFollowing());
		JMenuItem menuItemCentreEcran = new JMenuItem("Look at 0");
		menuItemCentreEcran.addActionListener(menuLookAtZero());
		JMenuItem menuItemplusMassif = new JMenuItem("Follow maximum mass");
		menuItemplusMassif.addActionListener(menuFollowMaxMass());
		JMenuItem menuItemBarycentre = new JMenuItem("Follow centroid");
		menuItemBarycentre.addActionListener(menuFollowCentroid());
		JMenuItem menuItemFollowSomething = new JMenuItem(
				"Follow something ...");
		menuItemFollowSomething.addActionListener(menuFollowSomething());
		JCheckBoxMenuItem menuItemShowTrace = new JCheckBoxMenuItem(
				"Show Trace", parameters.isShowTrace());
		menuItemShowTrace.addActionListener(menuShowTrace());
		JCheckBoxMenuItem menuItemPermanentRotationy = new JCheckBoxMenuItem(
				"Permanent Y Rotation", parameters.isPermanentRotationy());
		menuItemPermanentRotationy.addActionListener(menuYRotation());
		JCheckBoxMenuItem menuItemShowAxis = new JCheckBoxMenuItem("Show Axis",
				parameters.isShowAxis());
		menuItemShowAxis.addActionListener(menuShowAxis());
		JCheckBoxMenuItem menuItemShowGrid = new JCheckBoxMenuItem(
				"Show grids", parameters.isShowgrid());
		menuItemShowGrid.addActionListener(menuShowGrid());
		JCheckBoxMenuItem menuItemShowInfo = new JCheckBoxMenuItem("Show info",
				parameters.isShowInfo());
		menuItemShowInfo.addActionListener(menuShowInfo());
		JCheckBoxMenuItem menuItemShowMatter = new JCheckBoxMenuItem(
				"Show matter", parameters.isShowMatter());
		menuItemShowMatter.addActionListener(menuShowMatter());
		JCheckBoxMenuItem menuItemShowGas = new JCheckBoxMenuItem("Show gas",
				parameters.isShowGas());
		menuItemShowGas.addActionListener(menuShowGas());
		JCheckBoxMenuItem menuItemShowDM = new JCheckBoxMenuItem(
				"Show dark matter", parameters.isShowDarkMatter());
		menuItemShowDM.addActionListener(menuShowDarkMatter());

		menuVisu.add(menuViewSimplePoint);
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
		menuVisu.add(menuItemShowMatter);
		menuVisu.add(menuItemShowGas);
		menuVisu.add(menuItemShowDM);

		JMenu menuData = new JMenu("Data (Experimental)");
		final JCheckBoxMenuItem menuItemExportData = new JCheckBoxMenuItem(
				"Export to ...", parameters.isExportData());
		menuItemExportData
				.addActionListener(menuExportData(menuItemExportData));

		final JCheckBoxMenuItem menuItemPlayData = new JCheckBoxMenuItem(
				"Play data ...", parameters.isPlayData());
		menuItemPlayData.addActionListener(menuPlayData(menuItemPlayData));

		menuBar.add(menuData);
		menuData.add(menuItemExportData);
		menuData.add(menuItemPlayData);

		JMenu menuVideo = new JMenu("Video");
		final JCheckBoxMenuItem menuItemExportVideo = new JCheckBoxMenuItem(
				"Record to ...", parameters.isExportToVideo());
		menuItemExportVideo.addActionListener(menuRecord(menuItemExportVideo));
		menuBar.add(menuVideo);
		menuVideo.add(menuItemExportVideo);

		JMenu menuAbout = new JMenu("?");
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuBar.add(menuAbout);
		menuItemAbout.addActionListener(menuAbout());
		menuAbout.add(menuItemAbout);

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		gljpanel = new GLJPanel(caps);
		renderer = new Renderer(this);
		gljpanel.addGLEventListener(renderer);
		gljpanel.addKeyListener(renderer);
		gljpanel.addMouseListener(renderer);
		gljpanel.addMouseMotionListener(renderer);
		gljpanel.addMouseWheelListener(renderer);
		me.add(gljpanel, BorderLayout.CENTER);
	}

	static private Runnable onStart() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.
				}
				GUIProgram ex = new GUIProgram();
				ex.setVisible(true);
			}
		};
	}

	private WindowAdapter onClose() {
		return new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					me.getOut().finish();
					me.getDatafile().flush();
					me.getDatafile().close();
					me.getDataInputfile().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					animator.stop();
					System.exit(0);
				}
			}
		};
	}

	private ActionListener menuParameters() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				GUIParam guiParam = new GUIParam(me);
				guiParam.setVisible(true);
			}
		};
	}

	private ActionListener menuExport() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new XmlFilter());
					fileChooser.setSelectedFile(new File("univers.xml"));
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
					e1.printStackTrace();
				}
				animator.start();
			}
		};
	}

	private ActionListener menuImport() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
						me.setParameters(univers.getParameters());
						for (Matter m : univers.getListMatter()) {
							m.setParameters(univers.getParameters());
						}
						renderer.reload(me);
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				animator.start();
			}
		};
	}

	private ActionListener menuAddMatterList() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new XmlFilter());
					fileChooser.setDialogTitle("Specify a file to load");
					int userSelection = fileChooser.showOpenDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						GUIAddUnivers guiAddUnivers = new GUIAddUnivers(me);
						guiAddUnivers.setVisible(true);
						if (guiAddUnivers.isOk()) {
							File file = new File(fileChooser.getSelectedFile()
									.getAbsolutePath());
							JAXBContext jaxbContext = JAXBContext
									.newInstance(Univers.class);
							Unmarshaller jaxbUnmarshaller = jaxbContext
									.createUnmarshaller();
							Univers u = new Univers();
							u = (Univers) jaxbUnmarshaller.unmarshal(file);
							for (Matter m : u.getListMatter()) {
								m.setParameters(univers.getParameters());
								m.rotate(new Vector3d(0, 0, 0),
										guiAddUnivers.getRotate());
								m.getPointBefore().add(
										guiAddUnivers.getOffset());
								m.getPoint().add(guiAddUnivers.getOffset());
								m.getSpeed().add(guiAddUnivers.getSpeed());
								univers.getListMatter().add(m);
							}
							renderer.reload(me);
						}
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				animator.start();
			}
		};
	}

	private ActionListener menuExportParams() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new XmlFilter());
					fileChooser.setSelectedFile(new File("parameters.xml"));
					int userSelection = fileChooser.showSaveDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						OutputStream output = new FileOutputStream(fileChooser
								.getSelectedFile().getAbsolutePath());
						JAXBContext context = JAXBContext
								.newInstance(Parameters.class);
						Marshaller m = context.createMarshaller();
						m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						m.marshal(parameters, output);
						output.close();
					}
				} catch (JAXBException | IOException e1) {
					e1.printStackTrace();
				}
				animator.start();
			}
		};
	}

	private ActionListener menuImportParams() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
						parameters = (Parameters) jaxbUnmarshaller
								.unmarshal(file);
						renderer.reload(me);
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				GUIParam guiParam = new GUIParam(me);
				guiParam.setVisible(true);
			}
		};
	}

	private ActionListener menuStart() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.start();
			}
		};
	}

	private ActionListener menuStop() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
			}
		};
	}

	private ActionListener menuReset() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				me.reset();
			}
		};
	}

	private ActionListener menuViewSimplePoint() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setViewSimplePointOnly(!parameters
						.isViewSimplePointOnly());
			}
		};
	}

	private ActionListener menuStopFollowing() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
			}
		};
	}

	private ActionListener menuLookAtZero() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
				Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
				diffLookAt.negate();
				parameters.setEyes(diffLookAt);
				renderer.reload(me);
			}
		};
	}

	private ActionListener menuFollowMaxMass() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setFollowCentroid(false);
				parameters.setFollowMaxMass(true);
				parameters.setObjectToFollow(null);
			}
		};
	}

	private ActionListener menuFollowCentroid() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setFollowCentroid(true);
				parameters.setFollowMaxMass(false);
				parameters.setObjectToFollow(null);
			}
		};
	}

	private ActionListener menuFollowSomething() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				GUIFollowOther guiFO = new GUIFollowOther(me);
				guiFO.setVisible(true);
			}
		};
	}

	private ActionListener menuShowTrace() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowTrace(!parameters.isShowTrace());
				me.forTrace = new LinkedList<List<Vector3d[]>>();
				renderer.reload(me);
			}
		};
	}

	private ActionListener menuYRotation() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setPermanentRotationy(!parameters
						.isPermanentRotationy());
			}
		};
	}

	private ActionListener menuShowAxis() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowAxis(!parameters.isShowAxis());
			}
		};
	}

	private ActionListener menuShowGrid() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowgrid(!parameters.isShowgrid());
			}
		};
	}

	private ActionListener menuShowInfo() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowInfo(!parameters.isShowInfo());
			}
		};
	}

	private ActionListener menuShowMatter() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowMatter(!parameters.isShowMatter());
			}
		};
	}

	private ActionListener menuShowGas() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowGas(!parameters.isShowGas());
			}
		};
	}

	private ActionListener menuShowDarkMatter() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setShowDarkMatter(!parameters.isShowDarkMatter());
			}
		};
	}

	private ActionListener menuExportData(
			final JCheckBoxMenuItem menuItemExportData) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				animator.stop();
				if (!parameters.isExportData()) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify data file name");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new DataFilter());
					fileChooser.setSelectedFile(new File("out.dat"));
					int userSelection = fileChooser.showSaveDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						try {
							if (!fileChooser.getSelectedFile()
									.getAbsolutePath().toLowerCase()
									.endsWith(".dat")) {
								File myFile = new File(fileChooser
										.getSelectedFile().getAbsolutePath()
										+ ".dat");
								GZIPOutputStream zip = new GZIPOutputStream(
										new FileOutputStream(myFile));
								dataFile = new BufferedWriter(
										new OutputStreamWriter(zip));
							} else {
								File myFile = new File(fileChooser
										.getSelectedFile().getAbsolutePath());
								GZIPOutputStream zip = new GZIPOutputStream(
										new FileOutputStream(myFile));
								dataFile = new BufferedWriter(
										new OutputStreamWriter(zip));
							}
							parameters.setExportData(true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						dataFile.flush();
						dataFile.close();
						parameters.setExportData(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				renderer.reload(me);
				animator.start();
				menuItemExportData.setSelected(parameters.isExportData());
			}
		};
	}

	private ActionListener menuPlayData(final JCheckBoxMenuItem menuItemPlayData) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				animator.stop();
				if (!parameters.isPlayData()) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileFilter(new DataFilter());
					fileChooser.setDialogTitle("Specify a file to load");
					int userSelection = fileChooser.showOpenDialog(me);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						try {
							GZIPInputStream zip = new GZIPInputStream(
									new FileInputStream(fileChooser
											.getSelectedFile()
											.getAbsolutePath()));
							dataFileInput = new BufferedReader(
									new InputStreamReader(zip));
							parameters.setPlayData(true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						dataFileInput.close();
						parameters.setPlayData(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				renderer.reload(me);
				animator.start();
				menuItemPlayData.setSelected(parameters.isPlayData());
			}
		};
	}

	private ActionListener menuRecord(
			final JCheckBoxMenuItem menuItemExportVideo) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				if (!parameters.isExportToVideo()) {
					try {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Specify video name");
						fileChooser
								.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fileChooser.setMultiSelectionEnabled(false);
						fileChooser.setFileFilter(new MpejFilter());
						fileChooser.setSelectedFile(new File("out.mp4"));
						int userSelection = fileChooser.showSaveDialog(me);
						if (userSelection == JFileChooser.APPROVE_OPTION) {
							if (!fileChooser.getSelectedFile()
									.getAbsolutePath().toLowerCase()
									.endsWith(".mp4")) {
								out = new SequenceEncoder(new File(fileChooser
										.getSelectedFile().getAbsolutePath()
										+ ".mp4"));

							} else {
								out = new SequenceEncoder(new File(fileChooser
										.getSelectedFile().getAbsolutePath()));
							}
							parameters.setExportToVideo(true);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						out.finish();
						parameters.setExportToVideo(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				renderer.reload(me);
				animator.start();
				menuItemExportVideo.setSelected(parameters.isExportToVideo());
			}
		};
	}

	private ActionListener menuAbout() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIAbout guiAbout = new GUIAbout(me);
				guiAbout.setVisible(true);
			}
		};
	}
}