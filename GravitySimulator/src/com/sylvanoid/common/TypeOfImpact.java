package com.sylvanoid.common;

public enum TypeOfImpact {
    Fusion("Fusion"), Viscosity("Viscosity"), SoftImpact("Soft Impact"), HardImpact("Hard Impact"), Solid("Solid (experimental)");

    private final String label;

    private TypeOfImpact(String label) {
	this.label = label;
    }

    public String getLabel() {
	return label;
    }
}
