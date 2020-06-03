package se.cygni.texasholdem.game;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Action {

    private final ActionType actionType;
    private long amount;

    @JsonCreator
    public Action(@JsonProperty("actionType") final ActionType actionType,
                  @JsonProperty("amount") final long amount) {

        this.actionType = actionType;
        this.amount = amount;
    }

    public ActionType getActionType() {

        return actionType;
    }

    public long getAmount() {

        return amount;
    }

    public void setAmount(long amount) {

        this.amount = amount;
    }

    @Override
    public String toString() {

        return "Action [actionType=" + actionType + ", amount=" + amount + "]";
    }
}
