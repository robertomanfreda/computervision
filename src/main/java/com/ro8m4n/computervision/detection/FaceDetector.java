package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.classification.Classifier;
import com.ro8m4n.computervision.model.FaceResult;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class FaceDetector extends Detector {
    private static final String TAG = Classifier.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(Classifier.class);

    /**
     * An instance of {@link Classifier} that contains all supported classifiers
     */
    private Classifier classifier = new Classifier();

    /**
     * A {@link MatOfRect} where we store the result of {@link #frontalFaceDetection(String)}
     */
    private MatOfRect faces = new MatOfRect();

    /**
     * A test-purposes {@link File} where load different images
     */
    private File exampleImage;

    /**
     * {@link Detector#release()} the super {@link org.opencv.core.Mat}s plus 'this' Mat(s)
     */
    @Override
    void release() {
        super.release();
        release(faces);
    }

    public FaceResult frontalFaceDetection(String filename) {
        FaceResult faceResult = new FaceResult();

        exampleImage = loadExampleImage(filename);
        detectFrontalFaces(classifier.getHaarCascadeFrontalFace(), exampleImage);
        faceResult.setDetectedFacesUsingHAAR(faces.toArray().length);
        release();

        detectFrontalFaces(classifier.getLbpCascadeFrontalFace(), exampleImage);
        faceResult.setDetectedFacesUsingLBP(faces.toArray().length);
        release();

        LOGGER.info(faceResult.toString());
        return faceResult;
    }

    private void detectFrontalFaces(CascadeClassifier cascadeClassifier, File file) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Starting detection using an {}", classifier.getCascadeTypeName(cascadeClassifier));

        matRgba = Highgui.imread(file.getPath());
        matRgba.copyTo(matGrey);

        resizeWithScaleFactor(matGrey, matResized, null);

        Imgproc.cvtColor(matRgba, matResized, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(matResized, matResized);

        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 6,
                0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(0, 0));

        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Elaboration took: {} {} -> {} {}", elapsedTime, "millis", ((float) elapsedTime / 1000f), " seconds");
    }

    private File loadExampleImage(String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile());
    }

}