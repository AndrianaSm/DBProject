import data.BusLine;
import data.BusPosition;
import data.RouteCode;
import data.Value;
import pubsub.impl.PublisherNode;
import utils.Hash;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PublisherRunner {
    private final static String BROKER1_IP  = "192.168.10.79";
    private final static String BROKER2_IP  = "192.168.10.146";
    private final static int BROKER_PORT   = 8089;

    public static void main(String[] args) {
        Hash.loadData();
        Map<String, BusLine> busLines = Hash.getBusLines();
        Map<String, RouteCode> routeCodes = Hash.getRouteCodes();
        List<BusPosition> busPositions = Hash.getBusPositions();

        Map<String, List<Value>> producedValues = new HashMap<>();
        busPositions.forEach(b -> {
            String topic = busLines.get(b.getLineCode()).getLineCode();
            if (!producedValues.containsKey(topic)) {
                producedValues.put(topic, new ArrayList<>(
                        Collections.singletonList(
                                new Value(b, routeCodes.get(b.getLineCode()), busLines.get(b.getLineCode()))
                        )));
            } else {
                producedValues.get(topic).add(
                        new Value(b, routeCodes.get(b.getLineCode()), busLines.get(b.getLineCode()))
                );
            }
        });

        Map<String, String> topicBrokersMap = Hash.getTopicBrokersMap();
        // Create a publisher for each busLine and connect to the appropriate broker
        List<PublisherNode> publisherNodes = producedValues.keySet().stream().map(t -> {
            List<Value> publisherData = producedValues.get(t);
            String brokerNode = topicBrokersMap.get(t);
            PublisherNode publisherNode;
            if (brokerNode.equals("1")) {
                publisherNode = new PublisherNode(
                        Integer.parseInt(t), t, BROKER1_IP,
                        BROKER_PORT, publisherData);
            } else {
                publisherNode = new PublisherNode(
                        Integer.parseInt(t), t, BROKER2_IP,
                        BROKER_PORT, publisherData);
            }
            return publisherNode;
        }).collect(Collectors.toList());

        ExecutorService service = Executors.newFixedThreadPool(publisherNodes.size());

        // Start all publishers to produce data
        List<? extends Future<?>> publisherFutures = publisherNodes.stream()
                .map((publisherNode -> service.submit(new Thread(publisherNode::connect))))
                .collect(Collectors.toList());

        try {
            publisherFutures.forEach(f -> {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
