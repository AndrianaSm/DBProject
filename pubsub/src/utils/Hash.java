package utils;

import data.DataReader;
import data.BusLine;
import data.BusPosition;
import data.RouteCode;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hash {

    private static Map<String, BusLine> busLines;
    private static Map<String, RouteCode> routeCodes;
    private static List<BusPosition> busPositions;

    public static String sha1HashFunction(String input) {
        String sha1 = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(input.getBytes(StandardCharsets.UTF_8), 0, input.length());
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Failed to hash " + input);
        }
        return sha1;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void loadData() {
        DataReader dataReader = new DataReader();

        busLines = dataReader.getBusLines();
        routeCodes = dataReader.getRouteCodes();
        busPositions = dataReader.getBusPositions();
    }

    public static Map<String, BusLine> getBusLines() {
        return busLines;
    }

    public static Map<String, RouteCode> getRouteCodes() {
        return routeCodes;
    }


    public static List<BusPosition> getBusPositions() {
        return busPositions;
    }

    public static Map<String, String> getTopicBrokersMap() {
        Map<String, String> topicBrokersMap = new HashMap<>();
        busLines.keySet().forEach(k -> {
            String hashResult = Hash.sha1HashFunction(k);
            if (Hash.isNumeric(hashResult.substring(hashResult.length() - 2)))
                topicBrokersMap.put(k, "1");
            else
                topicBrokersMap.put(k, "2");
        });
        return topicBrokersMap;
    }
}
