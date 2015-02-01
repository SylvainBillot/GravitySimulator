package com.sylvanoid.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DataFilter extends FileFilter{
    @Override
    public boolean accept(File f){
        return f.getName().toLowerCase().endsWith(".dat")||f.isDirectory();
    }
    @Override
    public String getDescription(){
        return "Data files (*.dat)";
    }
}