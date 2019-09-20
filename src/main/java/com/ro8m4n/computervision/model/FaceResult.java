package com.ro8m4n.computervision.model;

import com.google.gson.Gson;
import com.ro8m4n.computervision.classification.ClassifierCollector;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class FaceResult {
    private ClassifierCollector.CascadeType cascadeType;
    private File file;
    private int absoluteFaceSize;
    private int detectedFaces;

    private static Gson prettyGson;

    @Autowired
    public FaceResult(Gson prettyGson) {
        FaceResult.prettyGson = prettyGson;
    }

    public FaceResult(ClassifierCollector.CascadeType cascadeType, File file, int absoluteFaceSize, int detectedFaces) {
        this.cascadeType = cascadeType;
        this.file = file;
        this.absoluteFaceSize = absoluteFaceSize;
        this.detectedFaces = detectedFaces;
    }

    public static String toJson(FaceResult faceResult) {
        return prettyGson.toJson(faceResult);
    }

    public static FaceResult fromJson(String json) {
        return prettyGson.fromJson(json, FaceResult.class);
    }

    public ClassifierCollector.CascadeType getCascadeType() {
        return cascadeType;
    }

    public void setCascadeType(ClassifierCollector.CascadeType cascadeType) {
        this.cascadeType = cascadeType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getAbsoluteFaceSize() {
        return absoluteFaceSize;
    }

    public void setAbsoluteFaceSize(int absoluteFaceSize) {
        this.absoluteFaceSize = absoluteFaceSize;
    }

    public int getDetectedFaces() {
        return detectedFaces;
    }

    public void setDetectedFaces(int detectedFaces) {
        this.detectedFaces = detectedFaces;
    }

    @Override
    public String toString() {
        return "FaceResult{" +
                "cascadeType=" + cascadeType +
                ", file=" + file +
                ", absoluteFaceSize=" + absoluteFaceSize +
                ", detectedFaces=" + detectedFaces +
                '}';
    }
}
