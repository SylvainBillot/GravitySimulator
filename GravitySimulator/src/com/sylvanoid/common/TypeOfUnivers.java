package com.sylvanoid.common;

public enum TypeOfUnivers {
    Planetary("With Solar System values"), PlanetaryRandom("Random Planetary System"), Random(
	    "Random Static Univers"), RandomRotateUnivers("Random Rotate Univers Eliptic"), PlanetariesGenesis(
		    "Planetaries Genenesis"), RandomRotateUniverCircular(
			    "Random Rotate Univers Circular"), RandomExpensionUnivers(
				    "Random Expension Univers"), ProtoStars("Proto stars");

    private final String label;

    private TypeOfUnivers(String label) {
	this.label = label;
    }

    public String getLabel() {
	return label;
    }
}
