package frc.robot.subsystems;

public class ArmPositionCalculator {
    // inches to robot, arm rotations (mult by 10)
    private static final int[][] data = {
        {0+36, -15},
        {12+36, 0},
        {24+36, 20},
        {36+36, 27},
        {48+36, 30},
        {60+36, 38},
        {72+36, 47},
        {84+36, 52},
        {96+36, 56},
        {108+36, 60},
        {120+36, 62}
    };

    /*
    * piecewise linear interpolation
    */
    public static double calculateArmPosition(int inchesFromTarget) {
        int[] prev = data[0];
        int[] next = data[1];
        int i = 1;

        while (i < data.length && data[i][0] < inchesFromTarget) {
            prev = data[i];
            i++;
            if (i < data.length) {
                next = data[i];
            }
        }

        if (prev[0] == inchesFromTarget) {
            return prev[1] / 10.0;
        } else if (next[0] == inchesFromTarget) {
            return next[1] / 10.0;
        } else {
            return (prev[1] + (next[1] - prev[1]) * ((double) (inchesFromTarget - prev[0]) / (next[0] - prev[0]))) / 10.0;
        }
    }
}