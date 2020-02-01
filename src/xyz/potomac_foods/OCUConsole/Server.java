package xyz.potomac_foods.OCUConsole;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public Server(int port){
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting for Client");
            while(true) {
                socket = server.accept();
                System.out.println("Receiving Data");
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String msg;
                //String line = reader.readLine();
                //System.out.println(line);
                while ((msg = reader.readLine()) != null) {
                    System.out.println(msg);
                }
            }

        }catch (IOException e){
            System.out.println("ERROR:" + e.getMessage() + e.getStackTrace());
        }
    }
}
