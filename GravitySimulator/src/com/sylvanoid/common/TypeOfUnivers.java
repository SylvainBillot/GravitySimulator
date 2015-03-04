package com.sylvanoid.common;

public enum TypeOfUnivers {
	Planetary					("With Solar System values"),
	PlanetaryRandom				("Random Planetary System"),
	Random						("Random Static Univers"),
	RandomRotateUnivers			("Random Rotate Univers (With Dark Mass)"),
	GalaxiesCollision			("Galaxies Interactions"),
	PlanetariesGenesis			("Planetaries Genenesis ?"),
	RandomRotateUniversWithoutCentralMass("Random Rotate Univers without central mass"),
	;

	private final String label;
	
	private TypeOfUnivers(String label){
		this.label=label;
	}
	
	public String getLabel(){
		return label;
	}
}
