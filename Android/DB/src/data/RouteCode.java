package data;

public class RouteCode {

    private String routeCode;
    private String routeType;
    private String description;

    public RouteCode(String routeCode, String routeType, String description) {
        this.routeCode = routeCode;
        this.routeType = routeType;
        this.description = description;
    }

    public RouteCode(String routeCode, String lineCode, String routeType, String description) {
        this.routeCode = routeCode;
        this.routeType = routeType;
        this.description = description;
    }

    public String getRouteCode() {
        return routeCode;
    }


    public String getRouteType() {
        return routeType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "RouteCode{" +
                "RouteCode='" + routeCode + '\'' +
                ", routeType='" + routeType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
