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
    3. NetBeans, A. (n.d.). Designing a Swing GUI in NetBeans IDE. https://netbeans.apache.org/kb/docs/java/quickstart-gui.html
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 96650
 */
public class Personal_server_GUI extends javax.swing.JFrame {

    /**
     * Creates new form JFrame3
     */
    public Personal_server_GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        Output = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Personal application");

        output.setColumns(20);
        output.setRows(5);
        Output.setViewportView(output);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Output, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Output, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, InterruptedException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Personal_server_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Personal_server_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Personal_server_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Personal_server_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Personal_server_GUI().setVisible(true);
            }
        });
        
        // create the personal server socket
        ServerSocket serverSocket = new ServerSocket(1345);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy 'time' HH:mm:ss");

        // medical server socket
        Socket medicalSocket = new Socket("localhost", 5555);

        //1st loop
        while (true) { // the server always on

            // accept the socket
            Socket socket = serverSocket.accept();

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

                    output.append(" \n");

                    output.append("At date: " + formatter.format(date) + ", Temperature is normal" + "\n");

                } else {
                    
                     output.append(" \n");
                    alert = true;
                    output.append("At date: " + formatter.format(date) + ", Temperature is high " + receiveTemp + ". An alert message is sent to the Medical Server." + "\n");

                }
                //---------------------------------------------------------------------------------

                // check heart rate
                String heartRate = checkHeartRate(receiveHeartRate);
                if (heartRate.equalsIgnoreCase("Above")) {

                    alert = true;
                    output.append("At date: " + formatter.format(date) + ", Hear rate is above normal " + receiveHeartRate + ". An alert message is sent to the Medical Server." + "\n");

                } else if (heartRate.equalsIgnoreCase("Below")) {

                    alert = true;

                    output.append("At date: " + formatter.format(date) + ", Hear rate is below normal " + receiveHeartRate + ". An alert message is sent to the Medical Server." + "\n");

                } else {

                    output.append("At date: " + formatter.format(date) + ", Hear rate is normal" + "\n");

                }

                //-----------------------------------------------------------------------------------------
                // check oxygen saturation
                if (checkOxygenSaturation(receiveOxygenSaturation)) {

                    output.append("At date: " + formatter.format(date) + ", Oxygen Saturation is normal" + "\n");

                } else {

                    alert = true;

                    output.append("At date: " + formatter.format(date) + ", Oxygen Saturation is low " + receiveOxygenSaturation + ". An alert message is sent to the Medical Server." + "\n");

                }
                //----------------------------------------------------------------------------------------

                output.append("");
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

    public static boolean checkTemp(double temp) {

        if (temp > 37) {
            return false;
        } else {
            return true;
        }

    }

    public static String checkHeartRate(int heartRate) {

        if (heartRate > 100) {
            return "Above";
        } else if (heartRate < 60) {
            return "Below";
        } else {
            return "Normal";
        }

    }

    public static boolean checkOxygenSaturation(int oxygenSaturation) {

        if (oxygenSaturation < 75) {
            return false;
        } else {
            return true;
        }

    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane Output;
    private javax.swing.JTextField jTextField1;
    private static javax.swing.JTextArea output;
    // End of variables declaration//GEN-END:variables
}
