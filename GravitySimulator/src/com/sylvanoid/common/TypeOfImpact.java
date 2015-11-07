package com.sylvanoid.common;

public enum TypeOfImpact {
	Fusion										("Fusion"),
	Viscosity									("Viscosity"),
	ViscosityWithoutDoubleRelaxation			("Viscosity Without double Relaxation"),
	SoftImpact									("Soft Impact"),
	HardImpact									("Hard Impact"),
	NoAcell										("No Attraction if contact"),
	;
	
	private final String label;
	
	private TypeOfImpact(String label){
		this.label=label;
	}
	
	public String getLabel(){
		return label;
	}
}
