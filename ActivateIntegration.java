
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivateIntegration {
    public static void main(String[] args) {
        // JSON payload
        String jsonPayload = "{\"status\": \"ACTIVATED\"}";

        // Escape special characters in JSON
        String escapedJsonPayload = jsonPayload.replace("\"", "\\\"");

        // Curl command
        String curlCommand = "curl -X POST -H \"Authorization: Basic ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==\" -H \"Content-Type: application/json\" -H \"X-HTTP-Method-Override: PATCH\" -d \"" + escapedJsonPayload + "\" \"https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/ic/api/integration/v1/integrations/NEWINTEGRATION_NEWREPO|01.00.0000\"";

        try {
            // Execute curl command
            Process process = Runtime.getRuntime().exec(curlCommand);

            // Read the output of the curl command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Print the exit code
            System.out.println("Curl command executed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
