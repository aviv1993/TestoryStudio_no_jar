package com.testorystudio.testorystudio.Entities;

import java.sql.Timestamp;

public class WebsocketMessage {
    private final String message;
    private final Timestamp timestamp;
    private final Severity severity;

    public WebsocketMessage(String message, Timestamp timestamp, Severity severity) {
        this.message = message;
        this.timestamp = timestamp;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Severity getSeverity() {
        return severity;
    }

}
