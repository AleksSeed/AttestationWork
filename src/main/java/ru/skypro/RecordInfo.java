package ru.skypro;

public class RecordInfo {
    private final String name;
    private final String description;

    public RecordInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
