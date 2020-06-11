package se.cygni.texasholdem.dao.model;

import java.util.ArrayList;
import java.util.List;

public class NoteworthyEventList {

    public List<NoteworthyEvent> events = new ArrayList<>();
    public final long tableId;

    public NoteworthyEventList(long tableId) {
        this.tableId = tableId;
    }

    public List<NoteworthyEvent> getEvents() {
        return events;
    }

    public long getTableId() {
        return tableId;
    }

    public void add(NoteworthyEvent event) {
        events.add(event);
    }
}
