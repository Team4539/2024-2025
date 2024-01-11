package frc.lib.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import frc.robot.Constants;

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
   * @param tagX April Tag's X Coordinate in Pixels, found using april tag detector function
   * @param tagY April Tag's Y Coordinate in Pixels, found using april tag detector function
   * @param cameraX Camera's X position on the Field (on our robot)
   * @param cameraY Camera's Y position on the Field (on our robot)
   * @param cameraZ Camera's Z position on the Field (on our robot)
   * @param offset Offset to be in front of the april tag (Default: 1.0)
   * @return the distance in meters between the camera and target april tag offset by given offset
   */
    public static double calculateDistance(double tagX, double tagY, double cameraX, double cameraY, double cameraZ, double offset) 
    {
        
        // Convert pixel coordinates to normalized coordinates
        double normalizedX = (tagX - Constants.fx) / Constants.fx;
        double normalizedY = (tagY - Constants.fy) / Constants.fy;

        // Depth calculation using triangulation for a single camera
        // offset should be 1.0 unless you know what you're doing. This offsets the calculated distance to be in front of the april tag, change accordingly!
        double depth = cameraZ / (normalizedX / cameraX + normalizedY / cameraY + offset);

        return depth;
    }

    /**
   * Sends GET Request to PI
   *
   * @return the contents of the get request, or null
   */
    public static String updatePI()
    {
        try
        {
            URL url = new URL(Constants.m_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) 
            {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        }
        catch(IOException asException)
        {
            return "null";
        }
    }

    /**
   * Gets a value from the given key and json string without external libraries
   *
   * @param jsonString The string containing JSON.
   * @param key The key for the desired variable.
   * @return The desired variable.
   */
    public static <T> T getval(String jsonString, String key) {
        int keyIndex = jsonString.indexOf("\"" + key + "\":");
        if (keyIndex != -1) {
            int startIndex = jsonString.indexOf("\"", keyIndex + key.length() + 2) + 1;
            int endIndex = jsonString.indexOf("\"", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                String valueString = jsonString.substring(startIndex, endIndex);
                if (valueString.equals("null")) {
                    return null;
                } else if (valueString.matches("-?\\d+(\\.\\d+)?")) {
                    if (valueString.contains(".")) {
                        return (T) Double.valueOf(valueString);
                    } else {
                        return (T) Integer.valueOf(valueString);
                    }
                } else {
                    return (T) valueString;
                }
            }
        }
        return null;
    }
}