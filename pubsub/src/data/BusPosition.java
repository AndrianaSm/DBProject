package data;

public class BusPosition {

    private String lineCode;
    private String routeCode;
    private String vehicleId;
    private String latitude;
    private String longitude;
    private String timestampOfBusPosition;

    public BusPosition(String lineCode,
                       String routeCode,
                       String vehicleId,
                       String latitude,
                       String longitude,
                       String timestampOfBusPosition) {
        this.lineCode = lineCode;
        this.routeCode = routeCode;
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampOfBusPosition = timestampOfBusPosition;
    }

    public String getLineCode() {
        return lineCode;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimestampOfBusPosition() {
        return timestampOfBusPosition;
    }

    @Override
    public String toString() {
        return "BusPosition{" +
                "lineCode='" + lineCode + '\'' +
                ", routeCode='" + routeCode + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", timestampOfBusPosition='" + timestampOfBusPosition + '\'' +
                '}';
    }
}
