package com.sylvanoid.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFilter extends FileFilter{
    @Override
    public boolean accept(File f){
        return f.getName().toLowerCase().endsWith(".xml")||f.isDirectory();
    }
    @Override
    public String getDescription(){
        return "Text files (*.xml)";
    }
}