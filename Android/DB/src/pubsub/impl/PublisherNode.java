package pubsub.impl;

import data.Value;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class PublisherNode  {

    private int publisherId;

    private String topic;
    private String host;
    private int port;

    private Socket clientSocket;
    private PrintWriter outputStream;

    private List<Value> dataToProduce;

    public PublisherNode(int publisherId, String topic, String host, int port, List<Value> dataToProduce) {
        this.publisherId = publisherId;
        this.topic = topic;

        this.host = host;
        this.port = port;
        this.dataToProduce = dataToProduce;
    }

    public void connect() {
        System.out.println("Starting Publisher on Thread: " + Thread.currentThread().getName());
        try {
            this.clientSocket = new Socket(host, port);
            this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            registerToBroker(topic, publisherId+"");
            dataToProduce.forEach(d -> {
                    push(publisherId+"", d);
                try {
                    // simulate some randomness in the producer's speed
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException  e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerToBroker(String topic, String publisherId) {
        outputStream.println("Connection Request");
        outputStream.println(topic + "," + publisherId + "," + 0);
    }

    public void push(String topic, Value value) {
        outputStream.println("[Publisher " + publisherId + "] Topic: " + topic + ", Value: " + value );
    }

    public String getTopic() {
        return topic;
    }

    public int getPublisherId() {
        return publisherId;
    }
}
