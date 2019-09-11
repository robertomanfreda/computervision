package com.ro8m4n.computervision.detection;

import org.jetbrains.annotations.Nullable;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public abstract class Detector {

    /**
     * Helper {@link Mat}s objects
     */
    Mat matRgba = new Mat();
    Mat matGrey = new Mat();
    Mat matResized = new Mat();

    /**
     * The scale factor we will use to resize the {@link #matResized} object
     */
    private static final float RESIZING_SCALE_FACTOR = 3.0f;

    Mat resizeWithScaleFactor(@Nullable Float scaleFactor) {
        float scale = null != scaleFactor ? scaleFactor : RESIZING_SCALE_FACTOR;

        Imgproc.resize(matGrey, matResized, new Size((matGrey.width() / scale), (matGrey.height() / scale)),
                0, 0, Imgproc.INTER_CUBIC);

        return matGrey;
    }

    void release() {
        matRgba.release();
        matGrey.release();
        matResized.release();
    }

    void release(Mat mat) {
        mat.release();
    }

}
