package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfImpact;
import com.sylvanoid.common.TypeOfObject;

public class BarnesHutGravity extends RecursiveTask<Integer> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Univers univers;
	private Parameters parameters;

	public BarnesHutGravity(Univers univers) {
		this.univers = univers;
		this.parameters = univers.getParameters();
	}

	@Override
	protected Integer compute() {
		if (!univers.isVirtual() && univers.getMass() > parameters.getNegligeableMass() && !univers.sameCoordonate()) {
			parameters.setNumOfCompute(parameters.getNumOfCompute() + 1);

			double cx = univers.getMin().x
					+ (univers.getMax().x - univers.getMin().x) / (1.5 + net.jafama.FastMath.random());
			double cy = univers.getMin().y
					+ (univers.getMax().y - univers.getMin().y) / (1.5 + net.jafama.FastMath.random());
			double cz = univers.getMin().z
					+ (univers.getMax().z - univers.getMin().z) / (1.5 + net.jafama.FastMath.random());

			Univers suba = new Univers(univers, new Vector3d(cx, cy, cz),
					new Vector3d(univers.getMax().x, univers.getMax().y, univers.getMax().z), true);
			Univers subb = new Univers(univers, new Vector3d(cx, cy, univers.getMin().z),
					new Vector3d(univers.getMax().x, univers.getMax().y, cz), true);
			Univers subc = new Univers(univers, new Vector3d(cx, univers.getMin().y, cz),
					new Vector3d(univers.getMax().x, cy, univers.getMax().z), true);
			Univers subd = new Univers(univers, new Vector3d(cx, univers.getMin().y, univers.getMin().z),
					new Vector3d(univers.getMax().x, cy, cz), true);
			Univers sube = new Univers(univers, new Vector3d(univers.getMin().x, cy, cz),
					new Vector3d(cx, univers.getMax().y, univers.getMax().z), true);
			Univers subf = new Univers(univers, new Vector3d(univers.getMin().x, cy, univers.getMin().z),
					new Vector3d(cx, univers.getMax().y, cz), true);
			Univers subg = new Univers(univers, new Vector3d(univers.getMin().x, univers.getMin().y, cz),
					new Vector3d(cx, cy, univers.getMax().z), true);
			Univers subh = new Univers(univers,
					new Vector3d(univers.getMin().x, univers.getMin().y, univers.getMin().z), new Vector3d(cx, cy, cz),
					true);

			for (Matter m : univers.getListMatter()) {
				if (m.getPoint().x > cx) {
					if (m.getPoint().y > cy) {
						if (m.getPoint().z > cz) {
							suba.getListMatter().add(m);
							suba.setVirtual(suba.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						} else {
							subb.getListMatter().add(m);
							subb.setVirtual(subb.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						}
					} else {
						if (m.getPoint().z > cz) {
							subc.getListMatter().add(m);
							subc.setVirtual(subc.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						} else {
							subd.getListMatter().add(m);
							subd.setVirtual(subd.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						}
					}
				} else {
					if (m.getPoint().y > cy) {
						if (m.getPoint().z > cz) {
							sube.getListMatter().add(m);
							sube.setVirtual(sube.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						} else {
							subf.getListMatter().add(m);
							subf.setVirtual(subf.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						}
					} else {
						if (m.getPoint().z > cz) {
							subg.getListMatter().add(m);
							subg.setVirtual(subg.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
						} else {
							subh.getListMatter().add(m);
							subh.setVirtual(subh.isVirtual() && (m.getTypeOfObject()==TypeOfObject.Virtual));
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

			BarnesHutGravity bha = new BarnesHutGravity(suba);
			BarnesHutGravity bhb = new BarnesHutGravity(subb);
			BarnesHutGravity bhc = new BarnesHutGravity(subc);
			BarnesHutGravity bhd = new BarnesHutGravity(subd);
			BarnesHutGravity bhe = new BarnesHutGravity(sube);
			BarnesHutGravity bhf = new BarnesHutGravity(subf);
			BarnesHutGravity bhg = new BarnesHutGravity(subg);
			BarnesHutGravity bhh = new BarnesHutGravity(subh);

			try {
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
			} catch (StackOverflowError e) {
				e.printStackTrace();
			}

			for (Univers u : subUnivers) {
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin && uvoisin.getListMatter().size() > 0
							&& uvoisin.getMass() > parameters.getNegligeableMass()) {
						for (Matter m : u.getListMatter()) {
							if (m.getTypeOfObject() != TypeOfObject.Virtual
									&& (!parameters.isStaticDarkMatter() || !m.isDark())) {
								parameters.setNumOfAccelCompute(parameters.getNumOfAccelCompute() + 1);
								if (parameters.isManageImpact()
										&& parameters.getTypeOfImpact() != TypeOfImpact.Viscosity) {
									Univers uAdjusted = new Univers(uvoisin);
									if (uAdjusted.adjustMassAndCentroid(m.getFusionWith()) != 0) {
										double attraction = HelperNewton.attraction(m, uAdjusted, parameters);
										m.getAccel().add(HelperVector.acceleration(m.getPoint(), uAdjusted.getGPoint(),
												attraction));
									}
								} else {
									double attraction = HelperNewton.attraction(m, uvoisin, parameters);
									m.getAccel().add(
											HelperVector.acceleration(m.getPoint(), uvoisin.getGPoint(), attraction));
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
