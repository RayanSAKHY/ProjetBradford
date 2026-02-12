package Event;

public class InputEvent implements IEvent {
    String info;

    public InputEvent(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}