package Event;

import java.util.Vector;
import java.util.Iterator;
import assets.Buffet;
import java.util.Arrays;

public class MessageEvent implements IEvent {
    private Vector<String> contents = new Vector<>();
    private Categorie categorie;
    private String name;

    public MessageEvent(Categorie categorie, String content,String name) {
        this.categorie = categorie;
        contents.addAll(Arrays.asList(content.split(",")));
        this.name=name;
    }

    @Override
    public String getInfo() {
        return name;
    }

    public String write() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = contents.iterator();
        switch (categorie) {
            case Categorie.WANT:
                sb.append(name).append(" ");
                sb.append("wants ");
                while (it.hasNext()) {
                    sb.append(it.next());
                    if (it.hasNext()) {
                        sb.append(" and ");
                    }
                }
                sb.append(".");
                break;
            default:
                sb.append("Not codede yet.");
                break;
        }
        return sb.toString();
    }
}