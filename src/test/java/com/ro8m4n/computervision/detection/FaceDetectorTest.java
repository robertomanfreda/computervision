package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.model.FaceResult;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.BeforeEach;

class FaceDetectorTest {

    private FaceDetector faceDetector;

    @BeforeEach
    void setUp() {
        OpenCV.loadLocally();

        faceDetector = new FaceDetector();
    }

    @org.junit.jupiter.api.Test
    void frontalFaceDetection() {
        FaceResult faceResult = faceDetector.frontalFaceDetection("examples/images/faces.jpg");

        assert faceResult.getDetectedFacesUsingHAAR() > 0 && faceResult.getDetectedFacesUsingHAAR() <= 19;
        assert faceResult.getDetectedFacesUsingLBP() > 0 && faceResult.getDetectedFacesUsingLBP() <= 19;
    }

}