import pubsub.impl.SubscriberNode;
import utils.Hash;
import data.BusLine;

import java.util.Map;
import java.util.Scanner;

public class SubscriberRunner {

    private final static String BROKER1_IP  = "192.168.10.79";
    private final static String BROKER2_IP  = "192.168.10.146";
    private final static int BROKER_PORT   = 8089;
    private final static int SUB_ID=1;


    public static void main(String[] args) {
        Hash.loadData();

        Map<String, String> topicBrokersMap = Hash.getTopicBrokersMap();

        Scanner in = new Scanner(System.in);
        System.out.print("Enter the busLine: ");
        String input = in.nextLine();
        Map<String, BusLine> busLines = Hash.getBusLines();

        String topic = null;
        for (String key : busLines.keySet()) {
            if (busLines.get(key).getLineId().equals(input)) {
                topic = key;
                break;
            }
        }

        String brokerId = topicBrokersMap.get(topic);
        if(topic!=null) {
            SubscriberNode subscriberNode = runOnBroker(topic, brokerId);
            subscriberNode.connect();
        }else
            System.out.println("Sorry!We don't have information for this bus");
    }

    private static SubscriberNode runOnBroker(String topic, String brokerId) {
        SubscriberNode subscriberNode;
        if (brokerId.equalsIgnoreCase("1")) {
            subscriberNode = new SubscriberNode(SUB_ID, topic, BROKER1_IP, BROKER_PORT);
        } else {
            subscriberNode = new SubscriberNode(SUB_ID, topic, BROKER2_IP, BROKER_PORT);
        }
        return subscriberNode;
    }
}
