package com.ro8m4n.computervision;

import com.ro8m4n.computervision.detection.FaceDetector;
import nu.pattern.OpenCV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComputervisionApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputervisionApplication.class);

    // Statically load OpenCV libraries
    static {
        OpenCV.loadLocally();
    }

    private static FaceDetector faceDetector;

    @Autowired
    public ComputervisionApplication(FaceDetector faceDetector) {
        ComputervisionApplication.faceDetector = faceDetector;
    }

    public static void main(String[] args) {
        SpringApplication.run(ComputervisionApplication.class, args);
    }

}
