import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivateIntegration {
    public static void main(String[] args) {
        String authorization = "ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==";
        String endpoint = "https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/ic/api/integration/v1/integrations/NEWINTEGRATION_NEWREPO|01.00.0000";
        String payload = "{\"Status\":\"ACTIVATED\"}";

        try {
            // Create the URL object
            URL url = new URL(endpoint);

            // Open the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + authorization);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setDoOutput(true);

            // Write the payload to the connection's output stream
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(payload.getBytes());
            outputStream.flush();
            outputStream.close();

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response code indicates success
            if (responseCode >= 200 && responseCode < 300) {
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the response
                System.out.println("Integration activated successfully.");
                System.out.println("Response Code: " + responseCode);
                System.out.println("Response: " + response.toString());
            } else {
                // Read the error response
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();

                // Print the error response
                System.out.println("Failed to activate integration. Error Response Code: " + responseCode);
                System.out.println("Error Response: " + errorResponse.toString());
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("An error occurred while activating the integration.");
            e.printStackTrace();
        }
    }
}
