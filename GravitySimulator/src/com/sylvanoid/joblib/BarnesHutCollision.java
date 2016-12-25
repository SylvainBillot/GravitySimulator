package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;

public class BarnesHutCollision extends RecursiveTask<Integer> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Univers univers;
	private Parameters parameters;
	private ConcurrentHashMap<String, MatterPair> collisionPairs;

	public BarnesHutCollision(Univers univers) {
		this.univers = univers;
		this.collisionPairs = univers.getCollisionPairs();
		this.parameters = univers.getParameters();
	}

	@Override
	protected Integer compute() {
		Integer valReturn = 0;
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

			BarnesHutCollision bha = new BarnesHutCollision(suba);
			BarnesHutCollision bhb = new BarnesHutCollision(subb);
			BarnesHutCollision bhc = new BarnesHutCollision(subc);
			BarnesHutCollision bhd = new BarnesHutCollision(subd);
			BarnesHutCollision bhe = new BarnesHutCollision(sube);
			BarnesHutCollision bhf = new BarnesHutCollision(subf);
			BarnesHutCollision bhg = new BarnesHutCollision(subg);
			BarnesHutCollision bhh = new BarnesHutCollision(subh);

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

					valReturn += bha.join();
					valReturn += bhb.join();
					valReturn += bhc.join();
					valReturn += bhd.join();
					valReturn += bhe.join();
					valReturn += bhf.join();
					valReturn += bhg.join();
					valReturn += bhh.join();
				} else {
					valReturn += bha.compute();
					valReturn += bhb.compute();
					valReturn += bhc.compute();
					valReturn += bhd.compute();
					valReturn += bhe.compute();
					valReturn += bhf.compute();
					valReturn += bhg.compute();
					valReturn += bhh.compute();
				}
			} catch (StackOverflowError e) {
				e.printStackTrace();
			}
		}

		if (univers.getListMatter().size() == 1) {
			Matter m = univers.getListMatter().get(0);
			if (!m.isDark()) {
				Univers gu = new Univers();
				if (univers.getFather() != null) {
					gu = univers.getFather();
					boolean detectColision = false;
					do {
						detectColision = false;
						for (Matter mgu : gu.getListMatter()) {
							if (m != mgu) {
								if ((HelperNewton.distance(m, mgu)
										/ (parameters
												.getCollisionDistanceRatio() * (m
												.getRayon() + mgu.getRayon())) < 1)
										&& (m.getTypeOfObject().equals(mgu
												.getTypeOfObject()))) {
									valReturn++;
									if (!m.getFusionWith().contains(mgu)) {
										m.getFusionWith().add(mgu);
										detectColision = true;
									}

									MatterPair toAdd = new MatterPair(m, mgu,
											parameters);
									if (collisionPairs.get(m.getName()
											+ mgu.getName()) == null
											&& collisionPairs.get(mgu.getName()
													+ m.getName()) == null) {
										collisionPairs.put(
												m.getName() + mgu.getName(),
												toAdd);
									}
								}
							}
						}
						gu = gu.getFather();
						detectColision = detectColision && (gu != null);
					} while (detectColision);
				}
			}
		}
		return valReturn;
	}
}
