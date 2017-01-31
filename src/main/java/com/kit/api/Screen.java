package com.kit.api;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

/**
 * API for getting information from the screen.
 *
 */
public interface Screen {

    /**
     * Tries to find a color on the screen.
     *
     * @param color color
     * @return list of points with color
     */
    java.util.List<Point> findColor(Color color, int tolerance);

    /**
     * Takes a screen capture of the game buffer and saves it to
     * user.home/07kit/Screenshots/{scriptName}/{fileName}
     *
     * @param fileName the file name
     * @return file/path or null if failed
     */
    Path capture(String fileName, boolean generateEvent);

    /**
     * Takes a screenshot of the game buffer and takes a subimage of the size of the rectangle
     * If rectangle width or height is 0 then it returns the entire buffer
     *
     * @param rectangle size of subimage
     * @return BufferedImage of the buffer
     */
    BufferedImage capture(Rectangle rectangle);

}
