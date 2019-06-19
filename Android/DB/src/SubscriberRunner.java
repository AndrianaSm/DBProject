import pubsub.impl.SubscriberNode;
import utils.Hash;
import data.BusLine;

import java.util.Map;
import java.util.Scanner;

public class SubscriberRunner {

    private final static String BROKER1_IP  = "172.16.2.33";
    private final static String BROKER2_IP  = "172.16.2.32";
    private final static int BROKER_PORT1   = 1111;
    private final static int BROKER_PORT2  = 1112;
    private final static int SUB_ID=1;


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the busLine: ");
        String input = in.nextLine();

        String brokerId="1";

        SubscriberNode subscriberNode = chooseBroker(input, brokerId);
        subscriberNode.connect();
        if(!subscriberNode.isSent){
            brokerId="2";
            subscriberNode = chooseBroker(input, brokerId);
            subscriberNode.connect();
        }

    }

    private static SubscriberNode chooseBroker(String topic, String brokerId) {
        SubscriberNode subscriberNode;
        if (brokerId.equalsIgnoreCase("1")) {
            subscriberNode = new SubscriberNode(1, topic, BROKER1_IP, BROKER_PORT1);
        } else {
            subscriberNode = new SubscriberNode(SUB_ID, topic, BROKER2_IP, BROKER_PORT2);
        }
        return subscriberNode;
    }

}
