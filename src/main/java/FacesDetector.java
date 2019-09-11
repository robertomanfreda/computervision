import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.util.Objects;

public class FacesDetector {

    // Statically load OpenCV libraries
    static {
        OpenCV.loadLocally();
    }

    /**
     * An instance of {@link ClassLoader} useful to get resources from the resource folder
     */
    private ClassLoader classLoader;

    /**
     * An instance of {@link CascadeClassifier} to detect objects, initilized using an HAAR classifier
     */
    private CascadeClassifier faceCascadeHAAR;
    /**
     * An instance of {@link CascadeClassifier}to detect objects, initilized using an LBP classifier
     */
    private CascadeClassifier faceCascadeLBP;

    /**
     * Helper {@link Mat} objects
     */
    private Mat matRgba;
    private Mat matGrey;
    private Mat matResized;
    /**
     * A {@link MatOfRect} where we store the result of {@link #frontalFaceDetection()}
     */
    private MatOfRect faces;

    /**
     * The scale factor we will use to resize the {@link #matResized} object
     */
    private static final float RESIZING_SCALE_FACTOR = 3.0f;

    private File exampleImage;

    public FacesDetector() {
        System.out.println("Initialization Started...");

        classLoader = getClass().getClassLoader();

        faceCascadeHAAR = initializeCascadeClassifier("haarcascade_frontalface_default.xml");
        faceCascadeLBP = initializeCascadeClassifier("lbpcascade_frontalface_improved.xml");

        // Initializing matrices
        matRgba = new Mat();
        matGrey = new Mat();
        matResized = new Mat();
        faces = new MatOfRect();

        // Initializing example image
        exampleImage = new File(Objects.requireNonNull(
                classLoader.getResource("faces.jpg")).getFile()
        );

        System.out.println("Initialization Completed.");
    }

    public void frontalFaceDetection() {
        detectFrontalFaces(faceCascadeHAAR, exampleImage);
        System.out.println("{ \"faces\": " + faces.toArray().length + " }");

        detectFrontalFaces(faceCascadeLBP, exampleImage);
        System.out.println("{ \"faces\": " + faces.toArray().length + " }");
    }

    private void detectFrontalFaces(CascadeClassifier cascadeClassifier, File file) {
        long startTime = System.currentTimeMillis();
        String cascadeClassifierName = cascadeClassifier.equals(faceCascadeHAAR) ? "HAAR" : "LBP";
        System.out.println("\nStarting detection using " + cascadeClassifierName + " cascade classifier...");

        matRgba = Highgui.imread(file.getPath());
        matRgba.copyTo(matGrey);
        Imgproc.resize(matGrey, matResized, new Size((matGrey.width() / RESIZING_SCALE_FACTOR),
                (matGrey.height() / RESIZING_SCALE_FACTOR)), 0, 0, Imgproc.INTER_CUBIC);

        Imgproc.cvtColor(matRgba, matResized, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(matResized, matResized);

        cascadeClassifier.detectMultiScale(matResized, faces, 1.1, 6,
                0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(30,30), new Size(0,0));

        release();

        System.out.println("Elaboration took: " + (System.currentTimeMillis() - startTime) + "millis.");
    }

    private CascadeClassifier initializeCascadeClassifier(String fileName) {
        // Initializing cascade classifier

        File file = new File(Objects.requireNonNull(
                classLoader.getResource(fileName)).getFile()
        );
        return new CascadeClassifier(file.getPath());
    }

    private void release() {
        matRgba.release();
        matGrey.release();
        matResized.release();
    }
}
