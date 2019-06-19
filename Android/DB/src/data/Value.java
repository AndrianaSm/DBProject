package data;

public class Value {

    private BusPosition busPosition;
    private RouteCode routeCode;
    private BusLine busLine;

    public Value(BusPosition busPosition, RouteCode routeCode, BusLine busLine) {
        this.busPosition = busPosition;
        this.routeCode = routeCode;
        this.busLine = busLine;
    }

    @Override
    public String toString() {
        return "Value{" +
                "busPosition=" + busPosition +
                ", routeCode=" + routeCode +
                ", busLine=" + busLine +
                '}';
    }
}
