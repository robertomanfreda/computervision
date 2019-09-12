package com.ro8m4n.computervision;

import com.ro8m4n.computervision.detection.FaceDetector;
import nu.pattern.OpenCV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComputervisionApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputervisionApplication.class);

    // Statically load OpenCV libraries
    static {
        OpenCV.loadLocally();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ComputervisionApplication.class, args);
    }

    // override command line arguments for Spring non Spring WebApplication
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Started CommandLineRunner Application");

        FaceDetector faceDetector = new FaceDetector();
        faceDetector.frontalFaceDetection("images/examples/19_faces.jpg");

    }

}