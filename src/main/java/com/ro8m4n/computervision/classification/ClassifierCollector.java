package com.ro8m4n.computervision.classification;

import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class ClassifierCollector {
    private static final String TAG = ClassifierCollector.class.getName();
    private final Logger LOGGER = LoggerFactory.getLogger(ClassifierCollector.class);

    /**
     * An instance of {@link CascadeClassifier} to detect objects, initialize using an xml HAAR_FRONTAL_FACE classifier
     */
    private CascadeClassifier haarCascadeFrontalFace;
    /**
     * An instance of {@link CascadeClassifier}to detect objects, initialize using an xml LBP_FRONTAL_FACE classifier
     */
    private CascadeClassifier lbpCascadeFrontalFace;

    /**
     * An enum that contains all the type of supported {@link CascadeClassifier}
     */
    public enum CascadeType {
        HAAR_FRONTAL_FACE, LBP_FRONTAL_FACE
    }

    public ClassifierCollector() {
        haarCascadeFrontalFace = initCascadeClassifier("cascades/haar/frontalface_default.xml");
        lbpCascadeFrontalFace = initCascadeClassifier("cascades/lbp/frontalface_improved.xml");
    }

    private CascadeClassifier initCascadeClassifier(String fileName) {
        // Initializing cascade classifier
        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(Objects.requireNonNull(
                classLoader.getResource(fileName)).getFile()
        );

        return new CascadeClassifier(file.getPath());
    }

    public String getCascadeTypeName(CascadeClassifier cascadeClassifier) {
        if (cascadeClassifier.equals(haarCascadeFrontalFace)) return CascadeType.HAAR_FRONTAL_FACE.name();
        if (cascadeClassifier.equals(lbpCascadeFrontalFace)) return CascadeType.LBP_FRONTAL_FACE.name();

        LOGGER.warn("That Cascade is not an instance of: {}", TAG);

        return "I don't know";
    }

    public CascadeType getCascadeType(CascadeClassifier cascadeClassifier) {
        if (cascadeClassifier.equals(haarCascadeFrontalFace)) return CascadeType.HAAR_FRONTAL_FACE;
        if (cascadeClassifier.equals(lbpCascadeFrontalFace)) return CascadeType.LBP_FRONTAL_FACE;
        return null;
    }

    public CascadeClassifier getHaarCascadeFrontalFace() {
        return haarCascadeFrontalFace;
    }

    public CascadeClassifier getLbpCascadeFrontalFace() {
        return lbpCascadeFrontalFace;
    }

}
