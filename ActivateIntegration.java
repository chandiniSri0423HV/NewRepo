import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivateIntegration {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ActivateDeactivateIntegration <instanceURL> <integrationIdentifier> <action>");
            return;
        }
        
        // Read instance URL, integration identifier, and action from command-line arguments
        String instanceUrl = args[0];
        String integrationIdentifier = args[1];
        String action = args[2];

        // JSON payload
        String jsonPayload = "{\"status\": \"" + action + "\"}";

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
