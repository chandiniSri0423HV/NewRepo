package ReleaseManagement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ConnectionUpdater {
    public void updateConnection(String URL, String Username, String Password, String CSVFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSVFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Prepare JSON payload from each row
                String jsonPayload = prepareJsonPayload(line);

                // Execute CURL to update the connection
                executeCurl(URL, Username, Password, jsonPayload);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prepareJsonPayload(String csvRow) {
        // Parse the CSV row and prepare the JSON payload
        // ...
        // Placeholder implementation
        String[] columns = csvRow.split(",");
        String URL = columns[0];
        String Username = columns[1];
        String Password = columns[2];

        // Create a JSON object
        String jsonPayload = String.format("{\"URL\": \"%s\", \"Username\": \"%s\", \"Password\": \"%s\"}", URL, Username, Password);

        // Return the JSON payload
        return jsonPayload;
    }

    private void executeCurl(String oicURL, String username, String password, String jsonPayload) {
        // Execute CURL command to update the connection using the provided parameters
        // Placeholder implementation
        System.out.println("Executing CURL command...");
        System.out.println("OIC URL: " + oicURL);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("JSON Payload: " + jsonPayload);
    }

    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
        String URL = sc.nextLine();
        String Username = sc.nextLine();
        String Password = sc.nextLine();
        String CSVFilePath = sc.nextLine();

        ConnectionUpdater updater = new ConnectionUpdater();
        updater.updateConnection(URL, Username, Password, CSVFilePath);
    }
}
