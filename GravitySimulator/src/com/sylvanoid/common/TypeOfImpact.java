package com.sylvanoid.common;

public enum TypeOfImpact {
    Fusion("Fusion"), Viscosity("Viscosity"), SoftImpact("Soft Impact"), HardImpact("Hard Impact");

    private final String label;

    private TypeOfImpact(String label) {
	this.label = label;
    }

    public String getLabel() {
	return label;
    }
}
