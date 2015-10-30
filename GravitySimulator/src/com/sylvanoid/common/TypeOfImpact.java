package com.sylvanoid.common;

public enum TypeOfImpact {
	Fusion			("Fusion"),
	Friction		("Friction"),
	SoftImpact		("Soft Impact"),
	HardImpact		("Hard Impact"),
	SoftImpact2		("Soft Impact2"),
	NoAcell			("No Attraction if contact"),
	Experiment		("Experiment"),
	;
	
	private final String label;
	
	private TypeOfImpact(String label){
		this.label=label;
	}
	
	public String getLabel(){
		return label;
	}
}
