package com.drugdesign.model;

public class DockingResult {
    private int mode;
    private double bindingAffinity;
    private double rmsdLb;
    private double rmsdUb;
    private String poseFile;
    
    public DockingResult() {}
    
    public DockingResult(int mode, double bindingAffinity, double rmsdLb, double rmsdUb) {
        this.mode = mode;
        this.bindingAffinity = bindingAffinity;
        this.rmsdLb = rmsdLb;
        this.rmsdUb = rmsdUb;
    }
    
    public int getMode() { return mode; }
    public void setMode(int mode) { this.mode = mode; }
    
    public double getBindingAffinity() { return bindingAffinity; }
    public void setBindingAffinity(double bindingAffinity) { this.bindingAffinity = bindingAffinity; }
    
    public double getRmsdLb() { return rmsdLb; }
    public void setRmsdLb(double rmsdLb) { this.rmsdLb = rmsdLb; }
    
    public double getRmsdUb() { return rmsdUb; }
    public void setRmsdUb(double rmsdUb) { this.rmsdUb = rmsdUb; }
    
    public String getPoseFile() { return poseFile; }
    public void setPoseFile(String poseFile) { this.poseFile = poseFile; }
}
