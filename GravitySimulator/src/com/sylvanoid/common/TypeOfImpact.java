package com.sylvanoid.common;

public enum TypeOfImpact {
	Fusion			("Fusion"),
	SoftImpact		("Soft Impact"),
	HardImpact		("Hard Impact"),
	Friction		("Friction 1"),
	Friction2		("Friction 2"),
	;
	
	private final String label;
	
	private TypeOfImpact(String label){
		this.label=label;
	}
	
	public String getLabel(){
		return label;
	}
}
