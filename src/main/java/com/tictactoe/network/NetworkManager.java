package com.tictactoe.network;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class NetworkManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ServerSocket serverSocket;
    private boolean isHost = false; // Tracks if this instance is the Server

    public void startServer(int port, Consumer<String> onMessage) throws IOException {
        serverSocket = new ServerSocket(port);
        isHost = true;
        new Thread(() -> {
            try {
                socket = serverSocket.accept();
                setupStreams();
                // Notify controller that we are connected
                onMessage.accept("CONNECTED");
                listen(onMessage);
            } catch (IOException e) {
                onMessage.accept("CONNECTION_LOST");
            }
        }).start();
    }

    public void connectToHost(String ip, int port, Consumer<String> onMessage) throws IOException {
        isHost = false;
        new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                setupStreams();
                onMessage.accept("CONNECTED");
                listen(onMessage);
            } catch (IOException e) {
                onMessage.accept("CONNECTION_LOST");
            }
        }).start();
    }

    private void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void listen(Consumer<String> onMessage) {
        try {
            String msg;
            while (socket != null && !socket.isClosed() && (msg = in.readLine()) != null) {
                onMessage.accept(msg);
            }
        } catch (IOException e) {
            onMessage.accept("CONNECTION_LOST");
        }
    }

    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    // FIXES: "cannot find symbol method isActive()"
    public boolean isActive() {
        return socket != null && !socket.isClosed();
    }

    // FIXES: "cannot find symbol method isHost()"
    public boolean isHost() {
        return isHost;
    }

    public void stop() {
        try {
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
