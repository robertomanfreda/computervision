package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.classification.Classifier;
import com.ro8m4n.computervision.model.FaceResult;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
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
        System.out.println("\n");
        return faceResult;
    }

    private void detectFrontalFaces(CascadeClassifier cascadeClassifier, File file) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Starting detection of file \"{}\"", file);
        LOGGER.debug("Using a cascade classifier of type {}", classifier.getCascadeTypeName(cascadeClassifier));

        // Initializing original image using RGBA Mat
        matRgba = Highgui.imread(file.getPath());

        // Copying 1st Mat into a 2nd Mat
        matRgba.copyTo(matGrey);

        // Making the 2nd Mat grey
        Imgproc.cvtColor(matRgba, matGrey, Imgproc.COLOR_RGBA2GRAY);

        // Resizing the 2nd Mat, putting the result in a 3d Mat
        resizeWithScaleFactor(matGrey, matResized, null);

        // Equalizing hist
        Imgproc.equalizeHist(matResized, matResized);

        // Finding the best size
        int absoluteFaceSize = 0;
        int height = matResized.rows();
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.2f);
        }
        LOGGER.debug("With absolute face size: {}", absoluteFaceSize);

        // Detection, put the result into faces
        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 3,
                0, new Size(absoluteFaceSize, absoluteFaceSize),
                new Size(0, 0)
        );

        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Elaboration took: {} {} -> {} {}", elapsedTime, "millis", ((float) elapsedTime / 1000f), " seconds");
    }

    private File loadExampleImage(String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile());
    }

}