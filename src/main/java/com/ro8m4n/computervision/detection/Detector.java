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

    /**
     * Reduce the size of the {@code #src} putting the result into {@code dst} using {@code scaleFactor}
     *
     * @param src         the source {@link Mat}
     * @param dst         the destination {@link Mat}
     * @param scaleFactor the scaling value
     */
    public void resizeWithScaleFactor(Mat src, Mat dst, @Nullable Float scaleFactor) {
        float scale = null != scaleFactor ? scaleFactor : RESIZING_SCALE_FACTOR;
        Imgproc.resize(src, dst, new Size((src.width() / scale), (src.height() / scale)), 0, 0, Imgproc.INTER_CUBIC);
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
