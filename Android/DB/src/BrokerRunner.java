import pubsub.impl.BrokerNode;
import utils.Hash;
public class BrokerRunner {
    private final static String BROKER_IP  = "172.16.2.32";
    private final static int BROKER_PORT   = 1112;

    public static void main(String[] args) {
        Hash.loadData();

        BrokerNode brokerNode = new BrokerNode(2, BROKER_IP, BROKER_PORT);

        Thread brokerThread = new Thread(brokerNode::initialize);

        brokerThread.start();
    }
}
