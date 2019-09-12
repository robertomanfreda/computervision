package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.model.FaceResult;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.BeforeEach;

class FaceDetectorTest {
    private FaceDetector faceDetector;
    private FaceResult faceResult;

    @BeforeEach
    void setUp() {
        OpenCV.loadLocally();

        faceDetector = new FaceDetector();
    }

    @org.junit.jupiter.api.Test
    void frontalFaceDetection() {
        // 19_faces.jpg
        faceResult = faceDetector.frontalFaceDetection("images/examples/19_faces.jpg");
        //assert faceResult.getDetectedFacesUsingHAAR() > 0 && faceResult.getDetectedFacesUsingHAAR() <= 19;
        //assert faceResult.getDetectedFacesUsingLBP() > 0 && faceResult.getDetectedFacesUsingLBP() <= 19;

        // 1_face_woman.jpg
        faceResult = faceDetector.frontalFaceDetection("images/examples/1_face_woman.jpg");
        //assert faceResult.getDetectedFacesUsingHAAR() <= 1;
        //assert faceResult.getDetectedFacesUsingLBP() <= 1;

        // 1_face_man.jpg
        faceResult = faceDetector.frontalFaceDetection("images/examples/1_face_man.jpg");
        //assert faceResult.getDetectedFacesUsingHAAR() <= 1;
        //assert faceResult.getDetectedFacesUsingLBP() <= 1;
    }

}