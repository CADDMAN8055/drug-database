package com.drugdesign.model;

public class SimilarityResult {
    private String compound1;
    private String compound2;
    private double tanimoto;
    private double dice;
    private double cosine;
    private String fingerprint;
    
    public SimilarityResult() {}
    
    public SimilarityResult(String compound1, String compound2, double tanimoto, double dice, double cosine, String fingerprint) {
        this.compound1 = compound1;
        this.compound2 = compound2;
        this.tanimoto = tanimoto;
        this.dice = dice;
        this.cosine = cosine;
        this.fingerprint = fingerprint;
    }
    
    public String getCompound1() { return compound1; }
    public String getCompound2() { return compound2; }
    public double getTanimoto() { return tanimoto; }
    public double getDice() { return dice; }
    public double getCosine() { return cosine; }
    public String getFingerprint() { return fingerprint; }
}
