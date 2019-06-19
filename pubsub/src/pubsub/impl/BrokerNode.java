package pubsub.impl;

import utils.ConnectionHandler;
import data.Value;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BrokerNode  {

    private int port;
    private String host;
    private int brokerId;

    private Map<String, Queue<Value>>    topics;
    private Map<String, Set<String>>  topicPublishers;
    private Map<String, Set<SubscriberNode>> topicSubscribers;


    private ServerSocket serverSocket;

    public BrokerNode(int brokerId, String host, int port) {
        this.port     = port;
        this.brokerId = brokerId;
        this.host     = host;

        this.topics              = new HashMap<>();
        this.topicPublishers     = new HashMap<>();
        this.topicSubscribers    = new HashMap<>();
    }

    public void initialize() {
        try {
            System.out.println("[Broker " + brokerId + "] Starting... ");
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
            while (true) {
                Socket socket = null;
                try {
                    synchronized (serverSocket) {
                        socket = serverSocket.accept();
                    }
                    System.out.println("A new client is connected : " + socket );

                    System.out.println("Assigning new thread for this client");

                    new Thread(new ConnectionHandler(this, socket)).start();
                }

                catch (Exception e){
                    socket .close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBrokerId() {
        return this.brokerId;
    }

    public void acceptConnection(String topic, int subscriberId) {
        System.out.println("[Broker " + brokerId + "] " + "Received Connection from subscriber with id: '" + subscriberId + "'");
        topics.keySet().forEach(System.out::println);
        if (!topics.containsKey(topic)) {
            System.out.println("Can't find the topic '" + topic + "' subscriber '" + subscriberId + "' requested.");
        }
    }

    public void acceptConnection(String topic, String publisherId) {
        System.out.println("[Broker " + brokerId + "] " + "Received Connection from publisher with id: '" + publisherId + "'");
        if (!topics.containsKey(topic)) {
            createTopic(topic);
        }
        if (!topicPublishers.containsKey(topic)) {
            topicPublishers.put(topic, new HashSet<>());
        }

        Set<String> publisherNodes = topicPublishers.get(topic);
        publisherNodes.add(publisherId);
        topicPublishers.put(topic, publisherNodes);
    }

    public void storeValueToTopic(String topic, Value value) {
        Queue<Value> topicValues = topics.get(topic);
        topicValues.add(value);
        topics.put(topic, topicValues);

        System.out.println("Topic: '" + topic + "' has '" + topics.get(topic).size() + " values.");
    }

    public boolean createTopic(String topic) {
        if (!topics.containsKey(topic)) {
            System.out.println("[Broker " + brokerId + "] Creating Topic: '" + topic + "'");
            topics.put(topic, new ConcurrentLinkedQueue<>());
            return true;
        }
        System.out.println("Topic: " + topic + " already exists.");
        return false;
    }

    public Map<String, Queue<Value>> getTopics() {
        return topics;
    }
}
