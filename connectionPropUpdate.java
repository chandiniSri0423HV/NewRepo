import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class connectionPropUpdate {
    public static void main(String[] args) {
        // Specify the path to the CSV file containing connection properties
        String csvFilePath = "C:/Docs/Git/NewRepo/ConnectionDetails.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int counter = 1;
            while ((line = br.readLine()) != null) {
                String[] properties = line.split(",");
                JSONObject payload = createPayload(properties);

                // Write JSON payload to a file
                String payloadFilePath = "path/to/payload_" + counter + ".json";
                writePayloadToFile(payload, payloadFilePath);

                // Execute CURL command to update the connection
                String curlCommand = "curl --header \"Content-Type: application/json\" --request PATCH --data @" + payloadFilePath + " <update_endpoint_url>";
                executeCurlCommand(curlCommand);

                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createPayload(String[] properties) {
        JSONObject payload = new JSONObject();
        payload.put("displayName", properties[0]);
        payload.put("propertyType", properties[1]);
        payload.put("propertyValue", properties[2]);
        // Add more properties as needed

        return payload;
    }

    private static void writePayloadToFile(JSONObject payload, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(payload.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeCurlCommand(String curlCommand) {
        try {
            Process process = Runtime.getRuntime().exec(curlCommand);
            int exitCode = process.waitFor();
            System.out.println("CURL command execution completed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
