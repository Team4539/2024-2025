package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rasberry extends SubsystemBase 
{
    private final HttpClient client = HttpClient.newHttpClient();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private boolean m_working = false;

    public Rasberry() 
    {
        executorService.scheduleAtFixedRate(this::getDetections, 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void periodic() 
    {
    }

    public synchronized void getDetections()
    {
        synchronized (this) {
            if (m_working) {
                return;
            }
            m_working = true;
        }

        try {
            final String URL = "http://" + Constants.PI.IP + ":" + Constants.PI.Port + "/get_detections";
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(URL)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                SmartDashboard.putBoolean("PI Connected", true);
            } else {
                SmartDashboard.putBoolean("PI Connected", false);
                return;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray detections = jsonResponse.getJSONArray("detections");

            for (int i = 0; i < detections.length(); i++) {
                JSONObject detection = detections.getJSONObject(i);
                int id = detection.getInt("id");
                double tagX = detection.getDouble("tagX");
                double tagY = detection.getDouble("tagY");
                SmartDashboard.putString("Detection " + i, "ID: " + id + " X: " + tagX + " Y: " + tagY);
            }
        } catch(Exception ex) {
            DriverStation.reportError(ex.getMessage(), ex.getStackTrace());
        } finally {
            synchronized (this) {
                m_working = false;
            }
        }
        
    }
    
    @Override
    public void simulationPeriodic() {}
}