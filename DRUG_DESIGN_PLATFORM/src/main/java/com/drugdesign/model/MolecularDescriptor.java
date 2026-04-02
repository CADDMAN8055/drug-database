package com.drugdesign.model;

public class MolecularDescriptor {
    private String name;
    private double value;
    private String unit;
    private String category;
    
    public MolecularDescriptor(String name, double value, String unit, String category) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.category = category;
    }
    
    public String getName() { return name; }
    public double getValue() { return value; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
}
