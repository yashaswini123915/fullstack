
package guestbook;



import com.sun.net.httpserver.*;

import java.io.*;

import java.net.InetSocketAddress;

import java.nio.charset.StandardCharsets;

import java.sql.*;



public class Guestbook {

    public static void main(String[] args) {

        try {

            // Start the server on Port 8082

            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);

            

            server.createContext("/submit", (exchange) -> {

                // 1. Set CORS Headers (Allows the browser to communicate with the server)

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");

                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");



                // 2. Handle OPTIONS Request (Prevents Double Printing/Submission)

                // Browsers send an OPTIONS request before POST. We must stop it here.

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {

                    exchange.sendResponseHeaders(204, -1);

                    exchange.close();

                    return;

                }



                // 3. Handle POST Request (Actual Data Submission)

                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                    String responseText = "";

                    int statusCode = 200;



                    try {

                        // Read data from the Request Body

                        InputStream is = exchange.getRequestBody();

                        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);



                        // Extract 'name' and 'message' from JSON string

                        String name = extractJSON(body, "name");

                        String msg = extractJSON(body, "message");



                        // 4. Save the data to SQLite Database

                        boolean isSaved = saveToDB(name, msg);



                        if (isSaved) {

                            System.out.println("\n[SUCCESS] Entry Saved: " + name + " | " + msg);

                            responseText = "Success";

                            statusCode = 200;

                        } else {

                            System.err.println("[FAILED] Database saving error.");

                            responseText = "Database Error";

                            statusCode = 500;

                        }



                    } catch (Exception e) {

                        System.err.println("Processing Error: " + e.getMessage());

                        responseText = "Server Error";

                        statusCode = 500;

                    } finally {

                        // 5. Send Response to Browser (Crucial to avoid net::ERR_EMPTY_RESPONSE)

                        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);

                        exchange.sendResponseHeaders(statusCode, responseBytes.length);

                        try (OutputStream os = exchange.getResponseBody()) {

                            os.write(responseBytes);

                          

                            

                        }//output stream

                        exchange.close(); // Closing connection properly

                    }

                } else {

                    exchange.close();

                }

            });



            server.start();

            System.out.println("🚀 Server is running on http://localhost:8082");

            System.out.println("Waiting for data from browser...");

            

        } catch (Exception e) { 

            e.printStackTrace(); 

        }

    }



    // Helper method to extract data from a simple JSON string

    private static String extractJSON(String json, String key) {

        try {

            String pattern = "\"" + key + "\":\"";

            int start = json.indexOf(pattern) + pattern.length();

            int end = json.indexOf("\"", start);

            return json.substring(start, end);

        } catch (Exception e) { return "Unknown"; }

    }



    // Database method to connect and save data to SQLite

    public static boolean saveToDB(String n, String m) {
        // FIX: Removed the space after 'jdbc:sqlite:'
        String url = "jdbc:sqlite:C:\\Users\\LENOVO\\Desktop\\projects\\fullstack\\Personal Feedback Guestbook\\guestbook.db";
        
        System.out.println("--- DB DEBUG: Starting saveToDB ---");
        System.out.println("Target File: " + url);

        try {
            System.out.println("Checkpoint A: Loading Driver...");
            Class.forName("org.sqlite.JDBC"); 

            System.out.println("Checkpoint B: Attempting Connection...");
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    System.out.println("Checkpoint C: Connection SUCCESS!");
                    
                    Statement s = conn.createStatement();
                    s.execute("CREATE TABLE IF NOT EXISTS guest_entries (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, message TEXT)");
                    
                    String sql = "INSERT INTO guest_entries(name, message) VALUES(?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, n);
                        pstmt.setString(2, m);
                        pstmt.executeUpdate();
                        System.out.println("Checkpoint D: Data Inserted into Table!");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // This will print the EXACT error message if it crashes
            System.err.println("\n!!! DATABASE ERROR !!!");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
        return false;
    }


}

