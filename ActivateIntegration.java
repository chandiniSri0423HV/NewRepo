import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivateIntegration {
    public static void main(String[] args) {
        // Read instance URL and integration identifier from command-line arguments
        String instanceUrl = args[0];
        String integrationIdentifier = args[1];

        // JSON payload
        String jsonPayload = "{\"status\": \"CONFIGURED\"}";

        // Escape special characters in JSON
        String escapedJsonPayload = jsonPayload.replace("\"", "\\\"");

        // Curl command
        String curlCommand = "curl -X POST --header \"Authorization: Basic ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==\" --header \"Content-Type: application/json\" --header \"X-HTTP-Method-Override: PATCH\" -d \"" + escapedJsonPayload + "\" -d 'enableAsyncActivationMode=true' \"" + instanceUrl + "/ic/api/integration/v1/integrations/" + integrationIdentifier + "\"";

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
