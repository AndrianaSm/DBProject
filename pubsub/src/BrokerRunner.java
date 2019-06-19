import pubsub.impl.BrokerNode;
import utils.Hash;

public class BrokerRunner {
    private final static String BROKER_IP  = "192.168.10.253";
    private final static int BROKER_PORT   = 8089;

    public static void main(String[] args) {
        Hash.loadData();

        BrokerNode brokerNode = new BrokerNode(1, BROKER_IP, BROKER_PORT);

        Thread brokerThread = new Thread(brokerNode::initialize);

        brokerThread.start();
    }
}
