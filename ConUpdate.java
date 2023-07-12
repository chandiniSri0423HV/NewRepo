import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class ConUpdate {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("CSV file name is missing.");
            return;
        }

        String csvFilePath = args[0];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty.");
                return;
            }

            String[] header = headerLine.split(",");

            List<JSONObject> connectionProperties = new ArrayList<>();
            List<JSONObject> securityProperties = new ArrayList<>();

            String line;
            int row = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Split the line and include empty values

               // Create the connection property
JSONObject connectionProperty = createConnectionProperty();
connectionProperties.add(connectionProperty);

// Create the security properties
JSONObject usernameProperty = createSecurityProperty("Username", "devops_user");
securityProperties.add(usernameProperty);

JSONObject passwordProperty = createSecurityProperty("Password", "Oic_Jenkins#2023");
securityProperties.add(passwordProperty);

// Execute the curl command
executeCurlCommand(connectionProperty, securityProperties);


                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createConnectionProperty() {
        JSONObject connectionProperty = new JSONObject();
        connectionProperty.put("displayName", "Connection URL");
        connectionProperty.put("hasAttachment", false);
        connectionProperty.put("hiddenFlag", false);
        connectionProperty.put("propertyGroup", "CONNECTION_PROPS");
        connectionProperty.put("propertyName", "connectionUrl");
        connectionProperty.put("propertyShortDesc", "Please make sure that this value really corresponds to the type selected above.");
        connectionProperty.put("propertyType", "URL");
        connectionProperty.put("propertyValue", "https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/");
        connectionProperty.put("requiredFlag", true);
    
        return connectionProperty;
    }
    
    private static JSONObject createSecurityProperty(String propertyName, String propertyValue) {
        JSONObject securityProperty = new JSONObject();
        securityProperty.put("displayName", propertyName);
        securityProperty.put("hasAttachment", false);
        securityProperty.put("hiddenFlag", false);
        securityProperty.put("propertyDescription", "A " + propertyName.toLowerCase() + " credential");
        securityProperty.put("propertyGroup", "CREDENTIALS");
        securityProperty.put("propertyName", propertyName.toLowerCase());
        securityProperty.put("propertyType", "STRING");
        securityProperty.put("propertyValue", propertyValue);
        securityProperty.put("requiredFlag", true);
    
        return securityProperty;
    }
    
    private static JSONObject createPasswordProperty(String[] header, String[] values) {
        JSONObject passwordProperty = new JSONObject();
        passwordProperty.put("displayName", "Password");
        passwordProperty.put("hasAttachment", false);
        passwordProperty.put("hiddenFlag", false);
        passwordProperty.put("propertyDescription", "A password credential");
        passwordProperty.put("propertyGroup", "CREDENTIALS");
        passwordProperty.put("propertyName", "password");
        passwordProperty.put("propertyType", "PASSWORD");
        passwordProperty.put("propertyValue", getValueForHeader(header, values, "Password"));
        passwordProperty.put("requiredFlag", true);

        return passwordProperty;
    }

    private static String getValueForHeader(String[] header, String[] values, String headerName) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals(headerName)) {
                return (i < values.length) ? values[i] : "";
            }
        }
        return "";
    }

    private static void executeCurlCommand(JSONObject connectionProperty, List<JSONObject> securityProperties) {
        String authorization = "ZGV2b3BzX3VzZXI6T2ljX0plbmtpbnMjMjAyMw==";
        String endpoint = "https://testinstance-idevjxz332qf-ia.integration.ocp.oraclecloud.com/ic/api/integration/v1/connections/NEWREPOCON";
    
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("connectionProperties", connectionProperty);
        jsonPayload.put("securityPolicy", "BASIC_AUTH");
        jsonPayload.put("securityProperties", securityProperties);
    
        String curlCommand = String.format(
            "curl --header \"Authorization: Basic %s\" --header \"X-HTTP-Method-Override: PATCH\" --header \"Content-Type: application/json\" -d '%s' %s",
            authorization,
            jsonPayload.toString(),
            endpoint
        );
    
        // Execute the curl command
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(curlCommand.split(" "));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("CURL command executed successfully.");
            } else {
                System.out.println("CURL command execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
