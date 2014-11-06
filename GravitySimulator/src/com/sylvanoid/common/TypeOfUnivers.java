package com.sylvanoid.common;

public enum TypeOfUnivers {
	Planetary	("Planetary System"),
	Random		("Random Univers"),
	Galaxy		("Galaxies Collision");

	private final String label;
	
	private TypeOfUnivers(String label){
		this.label=label;
	}
	
	public String getLabel(){
		return label;
	}
}
