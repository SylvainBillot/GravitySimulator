package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;
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

			Univers suba = new Univers(univers, new Vector3d(cx, cy, cz),
					new Vector3d(univers.getMax().x, univers.getMax().y,
							univers.getMax().z));
			Univers subb = new Univers(univers, new Vector3d(cx, cy,
					univers.getMin().z), new Vector3d(univers.getMax().x,
					univers.getMax().y, cz));
			Univers subc = new Univers(univers, new Vector3d(cx,
					univers.getMin().y, cz), new Vector3d(univers.getMax().x,
					cy, univers.getMax().z));
			Univers subd = new Univers(univers, new Vector3d(cx,
					univers.getMin().y, univers.getMin().z), new Vector3d(
					univers.getMax().x, cy, cz));
			Univers sube = new Univers(univers, new Vector3d(
					univers.getMin().x, cy, cz), new Vector3d(cx,
					univers.getMax().y, univers.getMax().z));
			Univers subf = new Univers(univers, new Vector3d(
					univers.getMin().x, cy, univers.getMin().z), new Vector3d(
					cx, univers.getMax().y, cz));
			Univers subg = new Univers(univers, new Vector3d(
					univers.getMin().x, univers.getMin().y, cz), new Vector3d(
					cx, cy, univers.getMax().z));
			Univers subh = new Univers(univers,
					new Vector3d(univers.getMin().x, univers.getMin().y,
							univers.getMin().z), new Vector3d(cx, cy, cz));

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

			suba.computeMassLimitsCentroidSpeed(false);
			subb.computeMassLimitsCentroidSpeed(false);
			subc.computeMassLimitsCentroidSpeed(false);
			subd.computeMassLimitsCentroidSpeed(false);
			sube.computeMassLimitsCentroidSpeed(false);
			subf.computeMassLimitsCentroidSpeed(false);
			subg.computeMassLimitsCentroidSpeed(false);
			subh.computeMassLimitsCentroidSpeed(false);

			BarnesHut bha = new BarnesHut(suba);
			BarnesHut bhb = new BarnesHut(subb);
			BarnesHut bhc = new BarnesHut(subc);
			BarnesHut bhd = new BarnesHut(subd);
			BarnesHut bhe = new BarnesHut(sube);
			BarnesHut bhf = new BarnesHut(subf);
			BarnesHut bhg = new BarnesHut(subg);
			BarnesHut bhh = new BarnesHut(subh);

			// Parallelization
			if (parameters.isParallelization()) {
				bha.fork();
				bhb.fork();
				bhc.fork();
				bhd.fork();
				bhe.fork();
				bhf.fork();
				bhg.fork();
				bhh.fork();

				bha.join();
				bhb.join();
				bhc.join();
				bhd.join();
				bhe.join();
				bhf.join();
				bhg.join();
				bhh.join();
			} else {
				bha.compute();
				bhb.compute();
				bhc.compute();
				bhd.compute();
				bhe.compute();
				bhf.compute();
				bhg.compute();
				bhh.compute();
			}
			for (Univers u : subUnivers) {
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin
							&& uvoisin.getListMatter().size() > 0
							&& uvoisin.getMass() > parameters
									.getNegligeableMass()) {
						for (Matter m : u.getListMatter()) {
							parameters.setNumOfAccelCompute(parameters
									.getNumOfAccelCompute() + 1);

							double attraction = HelperNewton.attraction(m,
									uvoisin, parameters);

							if (!parameters.isStaticDarkMatter() || !m.isDark()) {
								/* test relativist effect */
								/*
								 * javax.vecmath.Vector4d tmpVect = new
								 * javax.vecmath.Vector4d(
								 * HelperVector.lorentzCoord(parameters
								 * .getTimeFactor(), HelperVector
								 * .acceleration(m.getPoint(),
								 * uvoisin.getGPoint(), attraction), m
								 * .getPoint())); System.out.println(parameters
								 * .getTimeFactor() + " " + tmpVect + " " +
								 * m.getPoint());
								 */
								/* End of */

								m.getSpeed()
										.add(HelperVector.acceleration(
												m.getPoint(),
												uvoisin.getGPoint(), attraction));

								if (parameters.isManageImpact()

										&& uvoisin.getListMatter().size() == 1
										&& m.getTypeOfObject() == uvoisin
												.getListMatter().get(0)
												.getTypeOfObject()
										&& (HelperNewton.distance(m, uvoisin) < m
												.getRayon()
												+ uvoisin.getListMatter()
														.get(0).getRayon())) {
									m.getFusionWith().add(
											uvoisin.getListMatter().get(0));
								}
							}
						}
					}
				}
			}
		}
		return univers.getListMatter().size();
	}

}
