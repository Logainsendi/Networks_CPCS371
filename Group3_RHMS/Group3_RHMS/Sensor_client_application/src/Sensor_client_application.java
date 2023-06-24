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

package sensor_client_application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Sensor_client_application {
    
    // initiate variables
    static int heartRateSensor = 0;
    static double TempSensorDouble = 0;
    static String TempSensorString = "";
    static int O2Sensor = 0;
    static int limit = 0;
    static Socket socket = null;
    static long startTime = 0;
    static long endTime = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        // ask the user to enter the desired running time of the program
        System.out.print("Enter connection time wanted in seconds: ");
        limit = input.nextInt();

        // the minimum time is 60 seconds, therefore, check if the user entered a time less than 60s
        if (limit < 60) {
            limit = 60;
        }
        
        // determine the start and end time of the program
        startTime = System.currentTimeMillis();
        endTime = startTime + limit * 1000;

	 // to get the address from another device
	 // InetAddress addresses = InetAddress.getByName("x.x.x.x");
	 // save the address in a String variable
	 // String hostName = addresses.getHostName();
	 // create the socket based on the specific port and address 
	 //Socket socket = new Socket(hostName, port);

        // create the client socket 
        socket = new Socket("localhost", 1345);

        // for sending the data to the personal server 
        OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
        BufferedWriter writer = new BufferedWriter(output);
        

        while (System.currentTimeMillis() < endTime) {

                // generate random data
                
                // Heart rate sensor is between 50 and 120 heart beats each minute
                heartRateSensor = Data(50, 120);
                // Temperature sensor data is between 36 and 41
                TempSensorDouble = DataDouble(36, 41);
                // Oxygen level in the blood data is sent by the sensor with values between 60 to 100
                O2Sensor = Data(60, 100);
                // formating the date and time
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy 'time' HH:mm:ss");
                Date date = new Date();
                
                // coverting the double value of temperature to a string value
                TempSensorString = String.valueOf(TempSensorDouble);
                // take only one digit after the decimal point
                TempSensorString = TempSensorString.substring(0,4);
                
                // print data
                System.out.println("");
                System.out.println("At date: " + formatter.format(date) + ",sensed temperature is " + TempSensorString);
                System.out.println("At date: " + formatter.format(date) + ",sensed heart rate is " + heartRateSensor);
                System.out.println("At date: " + formatter.format(date) + ",sensed oxygen saturation is " + O2Sensor);

                // send the data to the personal server
                writer.write(heartRateSensor);
                writer.write(O2Sensor);
                writer.write(TempSensorString);
                writer.newLine();
                writer.flush();

                // the program must hold for a 5s before the next iteration
                Thread.sleep(5000);


        }

        socket.close();


    }

    // this method generates an integer random number between the two values min and max
    public static int Data(int min, int max) {

        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return random_int;
    }
    
    // this method generates a double random number between the two values min and max
    public static double DataDouble(int min, int max) {
    return (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
}


}
