/*
    -- Simulating a Remote Health Monitoring System for elderly patients using client-server TCP Sockets --
    Computer networks CPCS-371
    Team members:
        1. Hasna Othman Bukhari - B1
        2. Amani Khalid Biraik - B1
        3. Jetana Bassim Abudawood - B1
        4. Shaden Dhafer Alhashysh - B1
        5. Logain Ezzat Sendi - B2
        6. Wajd Bandar Alharbi - B1

    References:
    1. Java Socket Programming  (Java Networking Tutorial) - javatpoint. (n.d.). Retrieved January 22, 2023 from www.javatpoint.com. 
    https://www.javatpoint.com/socket-programming
    2. P. (2022, November 22). Thread.sleep() in Java - Java Thread sleep. Retrieved January 25, 2023 from DigitalOcean. 
    https://www.digitalocean.com/community/tutorials/thread-sleep-java
 */
// personal server: recieves sensored data from the client application, then checks the user's medical status and send data to medical server if neeeded.
package personal_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Personal_server {

    public static void main(String[] args) throws IOException, InterruptedException {

        // create the personal server socket
        ServerSocket serverSocket = new ServerSocket(1345);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy 'time' HH:mm:ss");

        //1st loop
        while (true) { // the server always on

            // accept the socket
            Socket socket = serverSocket.accept();

            // medical server socket
            Socket medicalSocket = new Socket("localhost", 5555);

            // to write data to medical server
            OutputStreamWriter medicalOutput = new OutputStreamWriter(medicalSocket.getOutputStream());
            BufferedWriter medicalWriter = new BufferedWriter(medicalOutput);

            // initiate variables
            double receiveTemp;
            String TempString = "";
            int receiveHeartRate;
            int receiveOxygenSaturation;

            // an alert variable is used to determine whether to send data to medical server or not
            boolean alert = false;

            // 2nd loop
            while (true) {

                // to read from the client application class
                InputStreamReader input = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(input);

                // read data from client application class
                receiveHeartRate = reader.read();
                receiveOxygenSaturation = reader.read();
                TempString = reader.readLine();

                // if the client application is terminated then the default data to be send is -1, so this method will check whether to end the connection or not
                if (receiveHeartRate == -1) {
                    alert = true;
                    break;
                }

                // make sure to take only one digit after the decimal point of the temperature value
                TempString = TempString.substring(0, 4);

                // convert the String value of temperature to a double value
                receiveTemp = Double.valueOf(TempString);

                Date date = new Date();

                // check temperature
                if (checkTemp(receiveTemp)) {

                    System.out.println("At date: " + formatter.format(date) + ", Temperature is normal");

                } else {
                    alert = true;
                    System.out.println("At date: " + formatter.format(date) + ", Temperature is high " + receiveTemp + ". An alert message is sent to the Medical Server.");

                }
                //---------------------------------------------------------------------------------

                // check heart rate
                String heartRate = checkHeartRate(receiveHeartRate);
                if (heartRate.equalsIgnoreCase("Above")) {

                    alert = true;
                    System.out.println("At date: " + formatter.format(date) + ", Hear rate is above normal " + receiveHeartRate + ". An alert message is sent to the Medical Server.");

                } else if (heartRate.equalsIgnoreCase("Below")) {

                    alert = true;
                    System.out.println("At date: " + formatter.format(date) + ", Hear rate is below normal " + receiveHeartRate + ". An alert message is sent to the Medical Server.");

                } else {

                    System.out.println("At date: " + formatter.format(date) + ", Hear rate is normal");

                }

                //-----------------------------------------------------------------------------------------
                // check oxygen saturation
                if (checkOxygenSaturation(receiveOxygenSaturation)) {

                    System.out.println("At date: " + formatter.format(date) + ", Oxygen Saturation is normal");

                } else {

                    alert = true;

                    System.out.println("At date: " + formatter.format(date) + ", Oxygen Saturation is low " + receiveOxygenSaturation + ". An alert message is sent to the Medical Server.");

                }
                //----------------------------------------------------------------------------------------

                System.out.println("");
                // check if the alert is true to send data to medical server
                if (alert) {

                    medicalWriter.write(1);
                    medicalWriter.write(receiveHeartRate);
                    medicalWriter.write(receiveOxygenSaturation);
                    medicalWriter.write(TempString);
                    medicalWriter.newLine();
                    medicalWriter.flush();

                }

                // the program must hold for a 5s before the next iteration
                Thread.sleep(5000);

            }//end of 2nd loop
            medicalWriter.write(1);
            socket.close();

        }// end of 1st loop
    }

    // check temperature method
    public static boolean checkTemp(double temp) {

        if (temp > 37) {
            return false;
        } else {
            return true;
        }

    }

    // check heart rate method
    public static String checkHeartRate(int heartRate) {

        if (heartRate > 100) {
            return "Above";
        } else if (heartRate < 60) {
            return "Below";
        } else {
            return "Normal";
        }

    }

    // check oxygen saturation method
    public static boolean checkOxygenSaturation(int oxygenSaturation) {

        if (oxygenSaturation < 75) {
            return false;
        } else {
            return true;
        }

    }

}
