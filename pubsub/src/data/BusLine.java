package data;

public class BusLine {

    private String lineCode;
    private String lineId;
    private String description;

    public BusLine(String lineCode, String lineId, String description) {
        this.lineCode = lineCode;
        this.lineId = lineId;
        this.description = description;
    }

    public String getLineCode() {
        return lineCode;
    }

    public String getLineId() {
        return lineId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "lineCode='" + lineCode + '\'' +
                ", lineId='" + lineId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
