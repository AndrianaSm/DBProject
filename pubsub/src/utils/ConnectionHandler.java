package utils;

import data.BusLine;
import data.BusPosition;
import data.RouteCode;
import data.Value;
import pubsub.impl.BrokerNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.Scanner;

public class ConnectionHandler implements Runnable {

    private BrokerNode broker;

    private Socket socket;

    public ConnectionHandler(BrokerNode broker, Socket socket) {
        this.broker = broker;
        this.socket = socket;
    }


    @Override
    public void run() {
        System.out.println("[Server] Received Connection: " + socket);
        try {
            Scanner inputStream = new Scanner(socket.getInputStream());
            String isConnectionRequest = inputStream.nextLine();
            if (isConnectionRequest.equalsIgnoreCase("Connection Request")) {
                String[] pubInfo = inputStream.nextLine().split(",");
                if (pubInfo[2].equalsIgnoreCase("0")) {
                    System.out.println("[Broker " + broker.getBrokerId() + "] Handling Publisher Connection");
                    broker.acceptConnection(pubInfo[0], pubInfo[1]);
                    while (inputStream.hasNextLine()) {
                        String input = inputStream.nextLine();
//                        System.out.println("[Broker " + broker.getBrokerId() + "] Received: " + input);
                        parseIncomingMessage(input);
                    }
                    inputStream.close();
                } else {
                    System.out.println("[Broker " + broker.getBrokerId() + "] Handling Subscriber Connection");
                    broker.acceptConnection(pubInfo[0], Integer.parseInt(pubInfo[1]));
                    PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                    while (true) {
                        Queue<Value> messages = broker.getTopics().get(pubInfo[0]);
                        if (messages.isEmpty()) {
                            System.out.println("No messages available");
                        } else {
                            messages.forEach(outputStream::println);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error:" + socket);
        } finally {
            try { socket.close(); } catch (IOException e) {}
            System.out.println("Closed: " + socket);
        }
    }

    private void parseIncomingMessage(String input) {
        String[] lines = input.split("Value:");

        String topic = lines[0].split(":")[1].trim().replace(",", "");

        BusPosition busPosition = extractBusPosition(lines[1].split("},")[0]);
        RouteCode routeCode     =  extractRouteCode(lines[1].split("},")[1]);
        BusLine busLine         =  extractBusLine(lines[1].split("},")[2]);

        broker.storeValueToTopic(topic, new Value(busPosition, routeCode, busLine));
    }

    private BusPosition extractBusPosition(String busPositionString) {
        String[] split = busPositionString.split(",");
        String lineCode = split[0].split("='")[1].replace("'", "");
        String routeCode = split[1].split("='")[1].replace("'", "");
        String vehicleId = split[2].split("='")[1].replace("'", "");
        String latitude = split[3].split("='")[1].replace("'", "");
        String longitude = split[4].split("='")[1].replace("'", "");
        String timestampOfBusPosition = split[5].split("='")[1].replace("'", "");

        return new BusPosition(lineCode, routeCode, vehicleId, latitude, longitude, timestampOfBusPosition);
    }

    private RouteCode extractRouteCode(String routeCodeString) {
        String[] split = routeCodeString.split("=");

        String routeCode = split[2].split(",")[0].replace("'", "");
        String routeType = split[3].split(",")[0].replace("'", "");
        String description = split[4].split(",")[0].replace("'", "");

        return new RouteCode(routeCode, routeType, description);
    }

    private BusLine extractBusLine(String busLineString) {
        String[] split = busLineString.split("=");

        String lineCode = split[2].split(",")[0].replace("'", "");
        String lineId = split[3].split(",")[0].replace("'", "");
        String description = split[4].split(",")[0].replace("'", "");

        return new BusLine(lineCode, lineId, description);
    }
}
