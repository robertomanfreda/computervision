package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.classification.Classifier;
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
     * A {@link MatOfRect} where we store the result of {@link #frontalFaceDetection()}
     */
    private MatOfRect faces;

    private File exampleImage;

    public FaceDetector() {
        faces = new MatOfRect();

        // Initializing example image
        exampleImage = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("examples/images/faces.jpg")).getFile()
        );
    }

    public void frontalFaceDetection() {
        detectFrontalFaces(classifier.getHaarCascadeFrontalFace(), exampleImage);
        LOGGER.info("Detected faces: {}", faces.toArray().length);

        detectFrontalFaces(classifier.getLbpCascadeFrontalFace(), exampleImage);
        LOGGER.info("Detected faces: {}", faces.toArray().length);

        release(faces);
    }

    private void detectFrontalFaces(CascadeClassifier cascadeClassifier, File file) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Starting detection using an {}", classifier.getCascadeTypeName(cascadeClassifier));

        matRgba = Highgui.imread(file.getPath());
        matRgba.copyTo(matGrey);

        matGrey = resizeWithScaleFactor(null);

        Imgproc.cvtColor(matRgba, matResized, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(matResized, matResized);

        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 6,
                0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(0, 0));

        release();

        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Elaboration took: {} {} -> {} {}", elapsedTime, "millis", ((float) elapsedTime / 1000f), " seconds");
    }
}
