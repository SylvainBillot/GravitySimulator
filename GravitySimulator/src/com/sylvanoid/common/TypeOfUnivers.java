package com.sylvanoid.common;

public enum TypeOfUnivers {
	Planetary("With Solar System values"), PlanetaryRandom("Random Planetary System"), Random(
			"Random Static Univers"), RandomRotateUnivers("Random Rotate Univers Eliptic"), PlanetariesGenesis(
					"Planetaries Genenesis"), RandomRotateUniverCircular(
							"Random Rotate Univers Circular"), RandomStaticSphericalUnivers(
									"Random Static Spherical Univers"),;

	private final String label;

	private TypeOfUnivers(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
