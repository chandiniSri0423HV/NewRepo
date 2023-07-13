import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class connectionPropUpdate {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("CSV file name is missing.");
            return;
        }

        String csvFilePath = args[0];

        // Read CSV file and process the data
        List<String[]> rows = new ArrayList<>();

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
                rows.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Process each row and create JSON payload file
        for (String[] values : rows) {
            createPayloadFiles(header, values);
            executeCurlCommand(values[3]);
        }
    }

    private static void createPayloadFiles(String[] header, String[] values) {
        JSONObject connectionProperty = createConnectionProperty(header, values);
        JSONObject securityProperty = createSecurityProperty(header, values);

        // Create JSON payload files for each column individually
        try {
            // Connection Property JSON payload
            JSONObject connectionPayload = new JSONObject();
            JSONArray connectionProperties = new JSONArray();
            connectionProperties.add(connectionProperty);
            connectionPayload.put("connectionProperties", connectionProperties);
            connectionPayload.put("securityPolicy", "BASIC_AUTH");

            FileWriter connectionFileWriter = new FileWriter("NewRepoConProp.json");
            connectionFileWriter.write(connectionPayload.toJSONString());
            connectionFileWriter.close();

            // Security Property JSON payload
            JSONObject securityPayload = new JSONObject();
            JSONArray securityProperties = new JSONArray();
            securityProperties.add(securityProperty);
            securityPayload.put("securityProperties", securityProperties);
            securityPayload.put("securityPolicy", "BASIC_AUTH");

            FileWriter securityFileWriter = new FileWriter("NewRepoSecProp.json");
            securityFileWriter.write(securityPayload.toJSONString());
            securityFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createConnectionProperty(String[] header, String[] values) {
        JSONObject connectionProperty = new JSONObject();
        connectionProperty.put("displayName", "Connection URL");
        connectionProperty.put("hasAttachment", false);
        connectionProperty.put("hiddenFlag", false);
        connectionProperty.put("propertyGroup", "CONNECTION_PROPS");
        connectionProperty.put("propertyName", "connectionUrl");
        connectionProperty.put("propertyShortDesc", "Please make sure that this value really corresponds to the type selected above.");
        connectionProperty.put("propertyType", "URL");
        connectionProperty.put("propertyValue", values[0]);
        connectionProperty.put("requiredFlag", true);
        return connectionProperty;
    }

    private static JSONObject createSecurityProperty(String[] header, String[] values) {
        JSONObject securityProperty = new JSONObject();
        securityProperty.put("displayName", "Username");
        securityProperty.put("hasAttachment", false);
        securityProperty.put("hiddenFlag", false);
        securityProperty.put("propertyDescription", "A username credential");
        securityProperty.put("propertyGroup", "CREDENTIALS");
        securityProperty.put("propertyName", "username");
        securityProperty.put("propertyType", "STRING");
        securityProperty.put("propertyValue", values[1]);
        securityProperty.put("requiredFlag", true);
        return securityProperty;
    }

    private static void executeCurlCommand(String connectionName) {
        String authorization = "ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==";
        String endpoint = "https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/ic/api/integration/v1/connections/" + connectionName;

        String curlCommand = String.format(
                "curl --header \"Authorization: Basic %s\" --header \"X-HTTP-Method-Override: PATCH\" --header \"Content-Type: application/json\" -d @NewRepoConProp.json %s",
                authorization,
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
