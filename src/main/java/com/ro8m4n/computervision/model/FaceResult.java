package com.ro8m4n.computervision.model;

public class FaceResult {

    private int detectedFacesUsingHAAR;
    private int detectedFacesUsingLBP;

    public int getDetectedFacesUsingHAAR() {
        return detectedFacesUsingHAAR;
    }

    public void setDetectedFacesUsingHAAR(int detectedFacesUsingHAAR) {
        this.detectedFacesUsingHAAR = detectedFacesUsingHAAR;
    }

    public int getDetectedFacesUsingLBP() {
        return detectedFacesUsingLBP;
    }

    public void setDetectedFacesUsingLBP(int detectedFacesUsingLBP) {
        this.detectedFacesUsingLBP = detectedFacesUsingLBP;
    }

    @Override
    public String toString() {
        return "FaceResult{" +
                "detectedFacesUsingHAAR=" + detectedFacesUsingHAAR +
                ", detectedFacesUsingLBP=" + detectedFacesUsingLBP +
                '}';
    }

}