package com.sylvanoid.common;

import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "Vector3d")
// Rename the XML type so it tells what it is really mapped to (Vector3d)
public class Vector3dXml {

	@XmlElement
	double x;
	@XmlElement
	double y;
	@XmlElement
	double z;

	public Vector3dXml() {
	} // JAXB needs a no-arg default constructor

	// these two methods could also be done in the marshal/unmarshal
	// methods above, but I think it was easier to put it here
	public Vector3dXml(Vector3d vector3d) {
		x = vector3d.getX();
		y = vector3d.getY();
		z = vector3d.getZ();
	}

	public Vector3d getVector3d() {
		return new Vector3d(x, y, z);
	}

}