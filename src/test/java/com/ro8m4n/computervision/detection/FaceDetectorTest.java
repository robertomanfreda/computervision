package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.model.FaceResult;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FaceDetectorTest {
    private FaceDetector faceDetector;

    /*+
     * An error tolerance for photos with manny faces
     */
    private final int ERROR_TOLERANCE = 2;

    @Autowired
    public FaceDetectorTest(FaceDetector faceDetector) {
        this.faceDetector = faceDetector;
    }

    @BeforeEach
    void setUp() {
        OpenCV.loadLocally();
    }

    @AfterEach
    void tearDown() {
        faceDetector.clearFaceResults();
    }

    @org.junit.jupiter.api.Test
    void frontalFaceDetection19Faces() {    // Test with error tolerance
        // Make detection
        faceDetector.frontalFaceDetection("images/examples/19_faces.jpg");

        List<FaceResult> results = faceDetector.getFaceResults();
        System.out.println(results.toString());

        // Exact number of faces in the photo is 19
        int targetFaces = 19;

        // Assume we have 0 faces found
        int facesFound = 0;
        // Scan results
        for (FaceResult result : results) {
            int f = result.getDetectedFaces();
            // If the processing found a number of faces between 17 and 21 we are good
            if ((f >= targetFaces - ERROR_TOLERANCE && f <= targetFaces + ERROR_TOLERANCE)) {
                facesFound = result.getDetectedFaces();
                // Doesn't matter if the result belongs to an HAAR or LBP classifier
                break;
            }
        }

        // Asserting the number of faces between 17 and 21
        assert facesFound >= targetFaces - ERROR_TOLERANCE && facesFound <= targetFaces + ERROR_TOLERANCE;
    }

    @org.junit.jupiter.api.Test
    void frontalFaceDetection1FaceMan() {   // Test with NO error tolerance
        // Make detection
        faceDetector.frontalFaceDetection("images/examples/1_face_man.jpg");

        List<FaceResult> results = faceDetector.getFaceResults();
        System.out.println(results.toString());

        // Exact number of faces in the photo is 1
        int targetFaces = 1;

        // Assume we have 0 faces found
        int facesFound = 0;
        // Scan results
        for (FaceResult result : results) {
            int f = result.getDetectedFaces();
            // If the processing found a number of faces between 17 and 21 we are good
            if (f == targetFaces) {
                facesFound = result.getDetectedFaces();
                // Doesn't matter if the result belongs to an HAAR or LBP classifier
                break;
            }
        }

        // Asserting the number of faces is 1
        assert facesFound == targetFaces;
    }

    @org.junit.jupiter.api.Test
    void frontalFaceDetection1FaceWoman() { // Test with NO error tolerance
        // Make detection
        faceDetector.frontalFaceDetection("images/examples/1_face_woman.jpg");

        List<FaceResult> results = faceDetector.getFaceResults();
        System.out.println(results.toString());

        // Exact number of faces in the photo is 1
        int targetFaces = 1;

        // Assume we have 0 faces found
        int facesFound = 0;
        // Scan results
        for (FaceResult result : results) {
            int f = result.getDetectedFaces();
            // If the processing found a number of faces between 17 and 21 we are good
            if (f == targetFaces) {
                facesFound = result.getDetectedFaces();
                // Doesn't matter if the result belongs to an HAAR or LBP classifier
                break;
            }
        }

        // Asserting the number of faces is 1
        assert facesFound == targetFaces;
    }

    // TODO Load other Photos and made more tests

}
