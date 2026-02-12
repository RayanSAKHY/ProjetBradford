package Event;

public class QuitEvent implements IEvent {
    @Override
    public String getInfo() {
        return "quit";
    }
}