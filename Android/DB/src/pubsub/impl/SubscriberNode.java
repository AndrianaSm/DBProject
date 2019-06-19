package pubsub.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SubscriberNode  {

    private int subscriberId;

    private String topic;

    private Socket clientSocket;
    private BufferedReader inputStream;

    private int port;
    private String host;

    private List<String> consumedMessages;
    private PrintWriter outputStream;

    public boolean isSent=false;

    public SubscriberNode(int subscriberId, String topic, String host, int port) {
        this.subscriberId = subscriberId;
        this.topic = topic;

        this.host = host;
        this.port = port;

        this.consumedMessages = new ArrayList<>();
    }


    public void connect() {
        System.out.println("Starting Consumer on Thread: " + Thread.currentThread().getName());
        try {
            this.clientSocket = new Socket(host, port);
            this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

            this.inputStream =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            registerToBroker(topic, subscriberId+"");

            pull();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerToBroker(String topic, String publisherId) {
        outputStream.println("Connection Request");
        outputStream.println(topic + "," + publisherId + "," + 1);
    }

    public void pull() {
       String receivedMessage ;
        try {
            while ((receivedMessage = inputStream.readLine()) != null) {
                isSent=true;
                System.out.println("[Subscriber " + subscriberId + "] Received from Broker: " + receivedMessage);
                consumedMessages.add(receivedMessage);
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println("[Subscriber " + subscriberId + "] Finished Consuming messages for topic: '" + topic + "'.");
       } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
