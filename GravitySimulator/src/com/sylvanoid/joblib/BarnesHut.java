package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import javax.vecmath.Point3d;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;

public class BarnesHut extends RecursiveTask<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Univers univers;
	private Parameters parameters;

	public BarnesHut(Univers univers) {
		this.univers = univers;
		this.parameters = univers.getParameters();
	}

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub

		if (univers.getMass() > parameters.getNegligeableMass()
				&& !univers.sameCoordonate()) {
			parameters.setNumOfCompute(parameters.getNumOfCompute() + 1);
			double cx = univers.getMin().x
					+ (univers.getMax().x - univers.getMin().x) / 2;
			double cy = univers.getMin().y
					+ (univers.getMax().y - univers.getMin().y) / 2;
			double cz = univers.getMin().z
					+ (univers.getMax().z - univers.getMin().z) / 2;

			Univers suba = new Univers(univers);
			Univers subb = new Univers(univers);
			Univers subc = new Univers(univers);
			Univers subd = new Univers(univers);
			Univers sube = new Univers(univers);
			Univers subf = new Univers(univers);
			Univers subg = new Univers(univers);
			Univers subh = new Univers(univers);

			for (Matter m : univers.getListMatter()) {
				if (m.getPoint().x > cx) {
					if (m.getPoint().y > cy) {
						if (m.getPoint().z > cz) {
							suba.getListMatter().add(m);
						} else {
							subb.getListMatter().add(m);
						}
					} else {
						if (m.getPoint().z > cz) {
							subc.getListMatter().add(m);
						} else {
							subd.getListMatter().add(m);
						}
					}
				} else {
					if (m.getPoint().y > cy) {
						if (m.getPoint().z > cz) {
							sube.getListMatter().add(m);
						} else {
							subf.getListMatter().add(m);
						}
					} else {
						if (m.getPoint().z > cz) {
							subg.getListMatter().add(m);
						} else {
							subh.getListMatter().add(m);
						}
					}
				}
			}

			List<Univers> subUnivers = new ArrayList<Univers>();
			subUnivers.add(suba);
			subUnivers.add(subb);
			subUnivers.add(subc);
			subUnivers.add(subd);
			subUnivers.add(sube);
			subUnivers.add(subf);
			subUnivers.add(subg);
			subUnivers.add(subh);

			suba.computeMassLimitsCentroidSpeed();
			subb.computeMassLimitsCentroidSpeed();
			subc.computeMassLimitsCentroidSpeed();
			subd.computeMassLimitsCentroidSpeed();
			sube.computeMassLimitsCentroidSpeed();
			subf.computeMassLimitsCentroidSpeed();
			subg.computeMassLimitsCentroidSpeed();
			subh.computeMassLimitsCentroidSpeed();

			BarnesHut bha = new BarnesHut(suba);
			BarnesHut bhb = new BarnesHut(subb);
			BarnesHut bhc = new BarnesHut(subc);
			BarnesHut bhd = new BarnesHut(subd);
			BarnesHut bhe = new BarnesHut(sube);
			BarnesHut bhf = new BarnesHut(subf);
			BarnesHut bhg = new BarnesHut(subg);
			BarnesHut bhh = new BarnesHut(subh);

			// Parallelization
			/*
			 * bha.fork(); bhb.fork(); bhc.fork(); bhd.fork(); bhe.fork();
			 * bhf.fork(); bhg.fork(); bhh.fork();
			 * 
			 * bha.join(); bhb.join(); bhc.join(); bhd.join(); bhe.join();
			 * bhf.join(); bhg.join(); bhh.join();
			 */

			bha.compute();
			bhb.compute();
			bhc.compute();
			bhd.compute();
			bhe.compute();
			bhf.compute();
			bhg.compute();
			bhh.compute();

			for (Univers u : subUnivers) {
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin
							&& uvoisin.getListMatter().size() > 0
							&& uvoisin.getMass() > parameters
									.getNegligeableMass()) {
						for (Matter m : u.getListMatter()) {
							parameters.setNumOfAccelCompute(parameters
									.getNumOfAccelCompute() + 1);
							double distance = new Point3d(m.getPoint())
									.distance(new Point3d(uvoisin.getGPoint()));

							// gravite rise at C; approximation
							/*
							 * javax.vecmath.Vector3d diffSpeed = new
							 * javax.vecmath.Vector3d( uvoisin.getSpeed());
							 * diffSpeed.sub(m.getSpeed()); distance +=
							 * diffSpeed.length() * distance / HelperVariable.C;
							 */
							double attraction = parameters.getTimeFactor()
									* HelperVariable.G
									* (((uvoisin.getMass()) / Math.pow(
											distance, 2)));

							if (!parameters.isManageImpact()
									|| uvoisin.getListMatter().size() > 1
									|| m.isDark() != uvoisin.getListMatter()
											.get(0).isDark()
									|| (distance > m.getRayon()
											+ uvoisin.getListMatter().get(0)
													.getRayon())) {
								m.getSpeed()
										.add(HelperVector.acceleration(
												m.getPoint(),
												uvoisin.getGPoint(), attraction));
							} else {
								m.getFusionWith().add(
										uvoisin.getListMatter().get(0));
							}

						}
					}
				}
			}
		}
		return univers.getListMatter().size();
	}

}
