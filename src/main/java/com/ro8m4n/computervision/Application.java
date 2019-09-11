package com.ro8m4n.computervision;

import com.ro8m4n.computervision.detection.FaceDetector;
import nu.pattern.OpenCV;

public class Application {

    // Statically load OpenCV libraries
    static {
        OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        FaceDetector faceDetector = new FaceDetector();
        faceDetector.frontalFaceDetection("examples/images/faces.jpg");
    }

}