package Event;

public class InputEvent implements Event {
    String info;

    public InputEvent(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}