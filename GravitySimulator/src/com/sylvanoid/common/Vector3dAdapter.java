package com.sylvanoid.common;

import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Vector3dAdapter extends XmlAdapter<Vector3dXml, Vector3d> { 

    public Vector3dXml marshal(Vector3d vector3d) throws Exception { 
        return new Vector3dXml(vector3d); 
    } 
    
    public Vector3d unmarshal(Vector3dXml inputVector) throws Exception { 
        return inputVector.getVector3d(); 
    } 
} 