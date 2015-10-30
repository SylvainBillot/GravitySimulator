package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;
import com.sylvanoid.common.TypeOfObject;

public class BarnesHutNeighbors extends RecursiveTask<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Univers univers;
	private Parameters parameters;

	public BarnesHutNeighbors(Univers univers) {
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
					+ (univers.getMax().x - univers.getMin().x)
					/ (1.5 + net.jafama.FastMath.random());
			double cy = univers.getMin().y
					+ (univers.getMax().y - univers.getMin().y)
					/ (1.5 + net.jafama.FastMath.random());
			double cz = univers.getMin().z
					+ (univers.getMax().z - univers.getMin().z)
					/ (1.5 + net.jafama.FastMath.random());

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

			BarnesHutNeighbors bha = new BarnesHutNeighbors(suba);
			BarnesHutNeighbors bhb = new BarnesHutNeighbors(subb);
			BarnesHutNeighbors bhc = new BarnesHutNeighbors(subc);
			BarnesHutNeighbors bhd = new BarnesHutNeighbors(subd);
			BarnesHutNeighbors bhe = new BarnesHutNeighbors(sube);
			BarnesHutNeighbors bhf = new BarnesHutNeighbors(subf);
			BarnesHutNeighbors bhg = new BarnesHutNeighbors(subg);
			BarnesHutNeighbors bhh = new BarnesHutNeighbors(subh);

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
		}
		if (univers.getListMatter().size() == 1) {
			Matter m = univers.getListMatter().get(0);
			if (m.getTypeOfObject() != TypeOfObject.Dark) {
				Univers gu = new Univers();
				if (univers.getFather() != null) {
					gu = univers.getFather();
					boolean detectNeighbors = false;
					do {
						detectNeighbors = false;
						for (Matter mgu : gu.getListMatter()) {
							if (m != mgu
									&& HelperNewton.distance(m, mgu) < parameters
											.getNebulaRadius()
											/ parameters
													.getNebulaRadiusRatioForVolumicMass()) {
								if ((m.getTypeOfObject().equals(mgu
										.getTypeOfObject()))) {
									if (!m.getNeighbors().contains(mgu)) {
										m.getNeighbors().add(mgu);
										detectNeighbors = true;
									}
									if (!mgu.getNeighbors().contains(m)) {
										mgu.getNeighbors().add(m);
										detectNeighbors = true;
									}
								}
							}
						}
						gu = gu.getFather();
						detectNeighbors = detectNeighbors && (gu != null);
					} while (detectNeighbors);
				}
			}
		}
		return univers.getListMatter().size();
	}
}
