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

package medical_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Medical_server {

    public static void main(String[] args) throws IOException, InterruptedException {

        // create the medical server socket
        ServerSocket MedicalServerSocket = new ServerSocket(5555);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy 'time' HH:mm:ss");
        //1st loop
        while (true) {//the server always on 

            Socket socket = MedicalServerSocket.accept(); // accept the socket

            // initiate the variables
            int alert = -1; 
            double receiveTemp;
            String TempString = "";
            int receiveHeartRate;
            int receiveOxygenSaturation;

            while (true) {

                // to read from the personal server class
                InputStreamReader medicalInput = new InputStreamReader(socket.getInputStream());
                BufferedReader medicalReader = new BufferedReader(medicalInput);
                
                // if there is an alert from the personal server this a value must be read
                alert = medicalReader.read();
                
                // if the personal server detect any abnormal condition the alert will be send to the medical server (this server)
                if (alert == 1) {
                    
                    // read the temperature, heart rate, and oxygen saturation that sent by the personal server 
                    receiveHeartRate = medicalReader.read();
                    receiveOxygenSaturation = medicalReader.read();
                    TempString = medicalReader.readLine();
                    
                    // if the client application is terminated then the default data to be send is -1, so this method will check whether to end the connection or not
                    if (receiveHeartRate == -1) {
                        break;
                    }
                    
                    //take only one digit after the decimal point of the temperature value
                    TempString = TempString.substring(0, 4);

                    // convert the String value of temperature to a double value
                    receiveTemp = Double.valueOf(TempString);
                    Date date = new Date();

                    // If the temperature exceeds 39 and heart rate is above 100 and oxygen is below 95
                    if ((receiveTemp > 39) && (receiveHeartRate > 100) && (receiveOxygenSaturation < 95)) {
                        System.out.println("At date: " + formatter.format(date) + ", Temperature is high " + receiveTemp + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Hear rate is above normal " + receiveHeartRate + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Oxygen Saturation is low" + receiveOxygenSaturation + ".");
                        System.out.println("ACTION:  Send an ambulance to the patient!");
                        System.out.println("");
                    } // If the temperature is between 38 and 38.9, and the heart rate is between 95 and 98, and oxygen is below 80
                    else if (((receiveTemp >= 38) && (receiveTemp <= 38.9)) && ((receiveHeartRate >= 95) && (receiveHeartRate <= 98)) && (receiveOxygenSaturation < 80)) {
                        System.out.println("At date: " + formatter.format(date) + ", Temperature is high " + receiveTemp + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Hear rate is normal " + receiveHeartRate + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Oxygen Saturation is low" + receiveOxygenSaturation + ".");
                        System.out.println("ACTION: Call the patient's family!");
                        System.out.println("");
                    } // default message for the caregiver
                    else {
                        System.out.println("At date: " + formatter.format(date) + ", Temperature is " + receiveTemp + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Hear rate is " + receiveHeartRate + ".");
                        System.out.println("At date: " + formatter.format(date) + ", Oxygen Saturation is " + receiveOxygenSaturation + ".");
                        System.out.println("ACTION: Warning, advise patient to make a checkup appointment!");
                        System.out.println("");
                    }

                }

                // the program must hold for a 5s before the next iteration
                Thread.sleep(5000);
            }

            // closing the connections
            socket.close();
            
        }
    }

}
