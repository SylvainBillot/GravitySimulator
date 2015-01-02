package com.sylvanoid.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MpejFilter extends FileFilter{
    @Override
    public boolean accept(File f){
        return f.getName().toLowerCase().endsWith(".mpeg")||f.isDirectory();
    }
    @Override
    public String getDescription(){
        return "Text files (*.mpeg)";
    }
}