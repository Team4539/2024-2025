package frc.lib.custom;

 /**
* Library for custom functions and calculations.
*/

/*
 *  What is fx and fy?
 *  They are calculated by calibrating a camera using opencv and a checkerboard
 *  Look at my calibration script here: 
 *  https://gist.github.com/sstock2005/b5691a735121f3d17032dc8ab17b1c57
 */

public class SamsUtils 
{
    /**
   * Calculates the distance between our camera and the target april tag.
   *
   * @param fx Focal Length X
   * @param fy Focal Length Y
   * @param tagX April Tag's X Coordinate in Pixels, found using april tag detector function
   * @param tagY April Tag's Y Coordinate in Pixels, found using april tag detector function
   * @param cameraX Camera's X position on the Field (on our robot)
   * @param cameraY Camera's Y position on the Field (on our robot)
   * @param cameraZ Camera's Z position on the Field (on our robot)
   * @param offset Offset to be in front of the april tag (Default: 1.0)
   * @return the distance in meters between the camera and target april tag offset by given offset
   */
    public static double calculateDistance(double fx, double fy, double tagX, double tagY, double cameraX, double cameraY, double cameraZ, double offset) 
    {
        // Convert pixel coordinates to normalized coordinates
        double normalizedX = (tagX - fx) / fx;
        double normalizedY = (tagY - fy) / fy;

        // Depth calculation using triangulation for a single camera
        // offset should be 1.0 unless you know what you're doing. This offsets the calculated distance to be in front of the april tag, change accordingly!
        double depth = cameraZ / (normalizedX / cameraX + normalizedY / cameraY + offset);

        return depth;
    }
}