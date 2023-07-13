import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConUpdate {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("CSV file name is missing.");
            return;
        }

        String csvFilePath = args[0];

        // Read CSV file and process the data
        List<JSONObject> connectionProperties = new ArrayList<>();
        List<JSONObject> securityProperties = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            String[] header = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Split the line and include empty values

                JSONObject connectionProperty = createConnectionProperty(header, values);
                connectionProperties.add(connectionProperty);

                JSONObject securityProperty = createSecurityProperty(header, values);
                securityProperties.add(securityProperty);

                // Execute the curl command
                executeCurlCommand(connectionProperties, securityProperties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createConnectionProperty(String[] header, String[] values) {
        JSONObject connectionProperty = new JSONObject();
        connectionProperty.put("propertyGroup", "CONNECTION_PROPS");
        connectionProperty.put("hasAttachment", false);
        connectionProperty.put("hiddenFlag", false);
        connectionProperty.put("propertyName", getValueForHeader(header, values, "propertyName"));
        connectionProperty.put("displayName", getValueForHeader(header, values, "displayName"));
        connectionProperty.put("propertyShortDesc", getValueForHeader(header, values, "propertyShortDesc"));
        connectionProperty.put("propertyType", getValueForHeader(header, values, "propertyType"));
        connectionProperty.put("propertyValue", getValueForHeader(header, values, "propertyValue"));
        connectionProperty.put("requiredFlag", true);
        return connectionProperty;
    }

    private static JSONObject createSecurityProperty(String[] header, String[] values) {
        JSONObject securityProperty = new JSONObject();
        securityProperty.put("propertyGroup", "CREDENTIALS");
        securityProperty.put("hasAttachment", false);
        securityProperty.put("hiddenFlag", false);
        securityProperty.put("propertyDescription", getValueForHeader(header, values, "propertyDescription"));
        securityProperty.put("propertyName", getValueForHeader(header, values, "propertyName"));
        securityProperty.put("displayName", getValueForHeader(header, values, "displayName"));
        securityProperty.put("propertyType", getValueForHeader(header, values, "propertyType"));
        securityProperty.put("propertyValue", getValueForHeader(header, values, "propertyValue"));
        securityProperty.put("requiredFlag", true);
        return securityProperty;
    }

    private static String getValueForHeader(String[] header, String[] values, String headerName) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals(headerName)) {
                return (i < values.length) ? values[i] : "";
            }
        }
        return "";
    }

    private static void executeCurlCommand(List<JSONObject> connectionProperties, List<JSONObject> securityProperties) {
        String authorization = "ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==";
        String endpoint = "https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/ic/api/integration/v1/connections/NEWREPOCON";

        JSONObject payloadObject = new JSONObject();
        payloadObject.put("connectionProperties", connectionProperties);
        payloadObject.put("securityProperties", securityProperties);
        payloadObject.put("securityPolicy", "BASIC_AUTH");

        String jsonPayload = payloadObject.toJSONString();

        String curlCommand = String.format(
                "curl --header \"Authorization: Basic %s\" --header \"X-HTTP-Method-Override: PATCH\" --header \"Content-Type: application/json\" -d '%s' %s",
                authorization,
                jsonPayload,
                endpoint
        );
        System.out.println("Curl Command: " + curlCommand);

        // Execute the curl command
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(curlCommand.split(" "));
            Process process = processBuilder.start();

            // Capture the response output
            StringBuilder responseOutput = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String responseLine;
            while ((responseLine = responseReader.readLine()) != null) {
                responseOutput.append(responseLine).append("\n");
            }

            // Capture the error output
            StringBuilder errorOutput = new StringBuilder();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorOutput.append(errorLine).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("CURL command executed successfully.");
                System.out.println("Response Output:\n" + responseOutput.toString());
            } else {
                System.out.println("CURL command execution failed with exit code: " + exitCode);
                System.out.println("Error Output:\n" + errorOutput.toString());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
