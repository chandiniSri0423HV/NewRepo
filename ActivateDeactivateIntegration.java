import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivateDeactivateIntegration {
    public static void main(String[] args) {
        // Get input parameters from command line arguments
        String instanceUrl = args[0];
        String integrationIdentifier = args[1];
        String action = args[2];
        String username = args[3];
        String password = args[4];

        // JSON payload for activation
        String activatePayload = "{\"status\": \"ACTIVATE\"}";

        // JSON payload for configuration
        String configurePayload = "{\"status\": \"CONFIGURED\"}";

        // Determine the JSON payload based on the action
        String jsonPayload = action.equals("ACTIVATE") ? activatePayload : configurePayload;

        // Escape special characters in JSON
        String escapedJsonPayload = jsonPayload.replace("\"", "\\\"");

        // Curl command
        String curlCommand = "curl -X POST --user " + username + ":" + password + " --header \"Content-Type: application/json\" --header \"X-HTTP-Method-Override: PATCH\" -d \"" + escapedJsonPayload + "\" -d 'enableAsyncActivationMode=true' \"" + instanceUrl + "/ic/api/integration/v1/integrations/" + integrationIdentifier + "\"";

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
