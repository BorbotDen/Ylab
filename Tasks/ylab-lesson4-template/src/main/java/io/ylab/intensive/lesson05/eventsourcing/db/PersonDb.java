package io.ylab.intensive.lesson05.eventsourcing.db;

public interface PersonDb {
    void trackingMessages() throws Exception;
}
