package com.ro8m4n.computervision.detection;

import com.ro8m4n.computervision.classification.ClassifierCollector;
import com.ro8m4n.computervision.model.FaceResult;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Detect the number of a faces in a file using HAAR or LBP {@link CascadeClassifier} from {@link ClassifierCollector}
 * <p>
 * Tests are {@link FaceDetectorTest}
 */
@Component
public class FaceDetector {
    private static final String TAG = ClassifierCollector.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassifierCollector.class);

    /**
     * An instance of {@link ClassifierCollector} that contains all supported classifiers
     */
    private ClassifierCollector classifierCollector = new ClassifierCollector();

    /**
     * The scale factor we will use to resize the {@link #matResized} object
     */
    private static final float RESIZING_SCALE_FACTOR = 2.0f;

    /**
     * Helper {@link Mat}s objects
     */
    private Mat matRgba = new Mat();
    private Mat matGrey = new Mat();
    private Mat matResized = new Mat();
    /**
     * A {@link MatOfRect} where we store the result of {@link #frontalFaceDetection(String)}
     */
    private MatOfRect faces = new MatOfRect();

    /**
     * A test-purposes {@link File} where load different images
     */
    private File exampleImage;

    /**
     * A {@link List} that handle all {@link FaceResult}
     */
    private List<FaceResult> faceResults = new ArrayList<>();

    public void frontalFaceDetection(String filename) {
        exampleImage = loadExampleImage(filename);

        detectFrontalFaces(exampleImage);
        release();

        LOGGER.info(faceResults.toString());
        System.out.println("\n");
    }

    private void detectFrontalFaces(File file) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Starting detection of file \"{}\"", file);

        // Initializing original image using RGBA Mat
        matRgba = Highgui.imread(file.getPath());

        // Copying 1st Mat into test 2nd Mat
        matRgba.copyTo(matGrey);

        // Making the 2nd Mat grey
        Imgproc.cvtColor(matRgba, matGrey, Imgproc.COLOR_RGBA2GRAY);

        // Resizing the 2nd Mat, putting the result in test 3d Mat
        resizeWithScaleFactor(matGrey, matResized);

        // Equalizing hist
        Imgproc.equalizeHist(matResized, matResized);

        // Detection using HAAR
        try {
            HAARDetection(classifierCollector.getHaarCascadeFrontalFace(), matResized, file);
        } catch (CascadeTypeNotValidException ctnve) {
            ctnve.printStackTrace();
        }

        // Detection uusing LBP
        try {
            LBPDetection(classifierCollector.getLbpCascadeFrontalFace(), matResized, file);
        } catch (CascadeTypeNotValidException ctnve) {
            ctnve.printStackTrace();
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Elaboration took: {} {} -> {} {}", elapsedTime, "millis", ((float) elapsedTime / 1000f), " seconds");
    }

    /**
     * Face detection using an HAAR cascade classifier
     *
     * @param cascadeClassifier the HAAR of type {@link ClassifierCollector.CascadeType#HAAR_FRONTAL_FACE}
     * @param matResized        the processed {@link FaceDetector#matResized}
     *                          using {@link FaceDetector#resizeWithScaleFactor(Mat, Mat)}
     * @param file              the processed {@link File}
     * @throws CascadeTypeNotValidException if {@code cascadeClassifier} isn't of type {@link ClassifierCollector.CascadeType#HAAR_FRONTAL_FACE}
     */
    private void HAARDetection(CascadeClassifier cascadeClassifier, Mat matResized, File file)
            throws CascadeTypeNotValidException {
        // code \\
        if (classifierCollector.getCascadeType(cascadeClassifier) != ClassifierCollector.CascadeType.HAAR_FRONTAL_FACE)
            throw new CascadeTypeNotValidException();

        LOGGER.debug("Starting HAAR detection Using test cascade classifier of type {}",
                classifierCollector.getCascadeTypeName(cascadeClassifier));

        int absoluteFaceSize = 30;
        LOGGER.debug("With absolute face size: {}", absoluteFaceSize);

        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 3,
                Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize),
                new Size(0, 0)
        );

        faceResults.add(new FaceResult(classifierCollector.getCascadeType(cascadeClassifier), file,
                absoluteFaceSize, faces.toArray().length));
    }

    /**
     * Face detection using an LBP {@link CascadeClassifier}
     *
     * @param cascadeClassifier the HAAR of type {@link ClassifierCollector.CascadeType#LBP_FRONTAL_FACE}
     * @param matResized        the processed {@link FaceDetector#matResized}
     *                          using {@link FaceDetector#resizeWithScaleFactor(Mat, Mat)}
     * @param file              the processed {@link File}
     * @throws CascadeTypeNotValidException if {@code cascadeClassifier} isn't of type {@link ClassifierCollector.CascadeType#LBP_FRONTAL_FACE}
     */
    private void LBPDetection(CascadeClassifier cascadeClassifier, Mat matResized, File file)
            throws CascadeTypeNotValidException {
        // code \\
        if (classifierCollector.getCascadeType(cascadeClassifier) != ClassifierCollector.CascadeType.LBP_FRONTAL_FACE)
            throw new CascadeTypeNotValidException();

        LOGGER.debug("Starting LBP detection Using test cascade classifier of type {}",
                classifierCollector.getCascadeTypeName(cascadeClassifier));

        int absoluteFaceSize = 0;
        int height = matResized.rows();
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.2f);
        }
        LOGGER.debug("With absolute face size: {}", absoluteFaceSize);

        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 3,
                0, new Size(absoluteFaceSize, absoluteFaceSize),
                new Size(0, 0)
        );

        faceResults.add(new FaceResult(classifierCollector.getCascadeType(cascadeClassifier), file,
                absoluteFaceSize, faces.toArray().length));
    }

    /**
     * Reduce the size of the {@code src} putting the result into {@code dst}
     *
     * @param src the source {@link Mat}
     * @param dst the destination {@link Mat}
     */
    private void resizeWithScaleFactor(Mat src, Mat dst) {
        Imgproc.resize(src, dst, new Size((src.width() / RESIZING_SCALE_FACTOR),
                (src.height() / RESIZING_SCALE_FACTOR)), 0, 0, Imgproc.INTER_CUBIC);
    }

    /**
     * {@link Mat#release()} all {@link Mat}s declared in {@link FaceDetector} to free allocated memory
     */
    private void release() {
        matRgba.release();
        matGrey.release();
        matResized.release();
        faces.release();
    }

    private File loadExampleImage(String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile());
    }

    public List<FaceResult> getFaceResults() {
        return faceResults;
    }

    public void clearFaceResults() {
        this.faceResults.clear();
    }
}
