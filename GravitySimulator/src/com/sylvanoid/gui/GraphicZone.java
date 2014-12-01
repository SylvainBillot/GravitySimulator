package com.sylvanoid.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.joblib.Matter;

public class GraphicZone extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color bgColor = Color.WHITE;
	private Color fgColor = Color.BLACK;
	
	private boolean firstTime = true;
	private GUIProgram guiProgram;

	public GraphicZone(GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
	}

	public void init(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize()
				.getHeight());
	}

	public void paint(Graphics g) {
		double cw = getSize().getWidth() / 2;
		double ch = getSize().getHeight() / 2;
		if (firstTime) {
			init(g);
			firstTime = false;
		}
		if (!HelperVariable.traceCourbe) {
			g.clearRect(0, 0, (int) getSize().getWidth(), (int) getSize()
					.getHeight());
			g.setColor(bgColor);
			g.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize()
					.getHeight());
		}
		try {
			for (Matter m : guiProgram.getUnivers().getListMatiere().values()) {
				if (!m.isDark()) {
					if (m.getX() > -cw / HelperVariable.scala
							&& m.getX() < cw / HelperVariable.scala
							&& m.getY() > -ch / HelperVariable.scala
							&& m.getY() < ch / HelperVariable.scala) {
						if (!HelperVariable.traceCourbe) {
							if (m.getRayon() * HelperVariable.scala > 1) {
								g.setColor(fgColor);
								g.drawOval(
										(int) (cw + m.getXminusV()
												* HelperVariable.scala - m
												.getRayon()
												* HelperVariable.scala),
										(int) (ch + m.getYminusV()
												* HelperVariable.scala - m
												.getRayon()
												* HelperVariable.scala),
										(int) (m.getRayon()
												* HelperVariable.scala * 2),
										(int) (m.getRayon()
												* HelperVariable.scala * 2));
							} else {
								g.setColor(fgColor);
								int rayonMin = 2;
								g.drawOval(
										(int) (cw + m.getXminusV()
												* HelperVariable.scala - rayonMin),
										(int) (ch + m.getYminusV()
												* HelperVariable.scala - rayonMin),
										rayonMin, rayonMin);
							}
						} else {
							g.setColor(fgColor);
							g.drawLine(
									(int) (cw + m.getXminusV()
											* HelperVariable.scala),
									(int) (ch + m.getYminusV()
											* HelperVariable.scala),
									(int) (cw + m.getX() * HelperVariable.scala),
									(int) (ch + m.getY() * HelperVariable.scala));
						}
						if (!HelperVariable.traceCourbe
								&& HelperVariable.displayVectors) {
							g.setColor(Color.RED);
							g.drawLine(
									(int) (cw + m.getXminusV()
											* HelperVariable.scala),
									(int) (ch + m.getYminusV()
											* HelperVariable.scala),
									(int) (cw + (m.getXminusV() + m.getSpeedX() * 5)
											* HelperVariable.scala),
									(int) (ch + (m.getYminusV() + m.getSpeedY() * 5)
											* HelperVariable.scala));

							g.setColor(Color.BLUE);
							g.drawLine((int) (cw + m.getXminusV()
									* HelperVariable.scala),
									(int) (ch + m.getYminusV()
											* HelperVariable.scala),
									(int) (cw + (m.getXminusV() + m.getaX()*5)
											* HelperVariable.scala),
									(int) (ch + (m.getYminusV() + m.getaY()*5)
											* HelperVariable.scala));
						}
					}
				} else {
					if (!HelperVariable.traceCourbe) {
						g.setColor(Color.GRAY);
						g.drawLine(
								(int) (cw + m.getX() * HelperVariable.scala) - 5,
								(int) (ch + m.getY() * HelperVariable.scala),
								(int) (cw + m.getX() * HelperVariable.scala) + 5,
								(int) (ch + m.getY() * HelperVariable.scala));
						g.drawLine(
								(int) (cw + m.getX() * HelperVariable.scala),
								(int) (ch + m.getY() * HelperVariable.scala) - 5,
								(int) (cw + m.getX() * HelperVariable.scala),
								(int) (ch + m.getY() * HelperVariable.scala) + 5);
					}
				}
			}
			if (!HelperVariable.traceCourbe) {
				g.setColor(Color.RED);
				g.drawLine((int) (cw + guiProgram.getUnivers().getGx()
						* HelperVariable.scala - 5), (int) (ch + guiProgram
						.getUnivers().getGy() * HelperVariable.scala),
						(int) (cw + guiProgram.getUnivers().getGx()
								* HelperVariable.scala + 5),
						(int) (ch + guiProgram.getUnivers().getGy()
								* HelperVariable.scala));
				g.drawLine((int) (cw + guiProgram.getUnivers().getGx()
						* HelperVariable.scala), (int) (ch
						+ guiProgram.getUnivers().getGy()
						* HelperVariable.scala - 5), (int) (cw + guiProgram
						.getUnivers().getGx() * HelperVariable.scala),
						(int) (ch + guiProgram.getUnivers().getGy()
								* HelperVariable.scala + 5));
			}
		} catch (ConcurrentModificationException e) {
			// do nothing
		}
	}
}
