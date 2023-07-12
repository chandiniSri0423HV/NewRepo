package ReleaseManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadfromCSV {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the path to the CSV file: ");
        String csvFilePath = scanner.nextLine();

        JSONArray connectionProperties = new JSONArray();
        JSONArray securityProperties = new JSONArray();

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
                connectionProperties.put(connectionProperty);
                JSONObject securityProperty = createSecurityProperty(header, values);
                securityProperties.put(securityProperty);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonPayload = createJsonPayload(connectionProperties, securityProperties);

        System.out.println(jsonPayload.toString(4));

        scanner.close();
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
        connectionProperty.put("propertyValue", getValueForHeader(header, values, "ConnectionURL"));
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
        securityProperty.put("propertyValue", getValueForHeader(header, values, "Username"));
        securityProperty.put("requiredFlag", true);

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

        JSONArray securityProperties = new JSONArray();
        securityProperties.put(securityProperty);
        securityProperties.put(passwordProperty);

        return securityProperty;
    }

    private static String getValueForHeader(String[] header, String[] values, String headerName) {
        int index = getIndex(header, headerName);
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        return "";
    }

    private static JSONObject createJsonPayload(JSONArray connectionProperties, JSONArray securityProperties) {
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("connectionProperties", connectionProperties);
        jsonPayload.put("securityPolicy", "BASIC_AUTH");
        jsonPayload.put("securityProperties", securityProperties);

        return jsonPayload;
    }

    private static int getIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
