package Event;

import java.util.Vector;
import java.util.Iterator;
import assets.Buffet;
import java.util.Arrays;

public class MessageEvent implements IEvent {
    private Vector<String> contents = new Vector<>();
    private Categorie categorie;
    private String name;
    private int number;
    private Buffet buffet;

    public MessageEvent(Categorie categorie, String content,String name) {
        this(categorie,content,name,0);
    }

    public MessageEvent(Categorie categorie, String content,String name,int number) {
        this(categorie,content,name,number,null);
    }

    public MessageEvent(Categorie categorie,Buffet buffet) {
        this(categorie,"","",0,buffet);
    }

    public MessageEvent(Categorie categorie,String name) {
        this(categorie,"",name,0,null);
    }

    public MessageEvent(Categorie categorie,String name,int number) {
        this(categorie,"",name,number,null);
    }

    public MessageEvent(Categorie categorie, String content,String name,int number,Buffet buffet) {
        this.categorie = categorie;
        contents.addAll(Arrays.asList(content.split(",")));
        this.name=name;
        this.number=number;
        this.buffet = buffet;
    }

    @Override
    public String getInfo() {
        return name;
    }

    public String write() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = contents.iterator();
        switch (categorie) {
            case WANT:
                sb.append(name).append(" ");
                sb.append("wants ");
                while (it.hasNext()) {
                    String temp = it.next();
                    if (temp.equals("piano")) {
                        sb.append("to play ");
                    }
                    sb.append(temp);
                    if (it.hasNext()) {
                        sb.append(" and ");
                    }
                }
                sb.append(".");
                break;
            case PLAY:
                sb.append(name).append(" plays the piano for ");
                sb.append(number).append("ms.");
                break;
            case TAKE:
                sb.append(name).append(" takes ");
                boolean eat = false;
                while (it.hasNext()) {
                    String temp = it.next();
                    sb.append(temp);
                    if (it.hasNext()) {
                        sb.append(" and ");
                    }
                    if (temp.equals("cake")) {
                        eat = true;
                    }
                }
                sb.append(" and ");
                if (eat) {
                    sb.append("eats for ");
                }
                else {
                    sb.append("drinks for ");
                }
                sb.append(number).append("ms.");
                break;
            case BUFFET:
                sb.append(" Buffet = (");
                sb.append(buffet.getCake()).append(" cakes, ");
                sb.append(buffet.getTea()).append(" teas, ");
                sb.append(buffet.getCoffee()).append(" coffees).");
                break;
            case LISTEN:
                sb.append(name).append(" listens to music for ");
                sb.append(number).append("ms.");
                break;
            case END:
                sb.append(name).append(" finished ");
                switch (contents.elementAt(0)) {
                    case "piano":
                        sb.append("playing the piano.");
                        break;
                    case "music":
                        sb.append("listening music");
                        break;
                    case "eat":
                        sb.append("eating.");
                        break;
                    case "drink":
                        sb.append("drinking.");
                        break;
                    case "wait":
                        sb.append("waiting.");
                        break;
                    default:
                        break;
                }
                break;
            case STAFF:
                sb.append(name).append(" brings ");
                sb.append(number).append(" ");
                sb.append(contents.elementAt(0));
                if (number >1) sb.append("s");
                sb.append(".");
                break;
            case KITCHEN:
                sb.append(name).append(" returns to kitchen.");
                break;
            case LOG:
                sb.append(name);
                break;
            case WAIT:
                sb.append(name).append(" waits in the queue for the ");
                sb.append(name);
                break;
            case PIANOQUEUE:
                sb.append("Queue for the piano: ");
                int i = 0;
                while(it.hasNext()) {
                    String temp = it.next();
                    sb.append(i).append(": ");
                    sb.append(temp).append(" ");
                }
            case BUFFETQUEUE:
                sb.append("Queue for the buffet: ");
                int i = 0;
                while(it.hasNext()) {
                    String temp = it.next();
                    sb.append(i).append(": ");
                    sb.append(temp).append(" ");
                }
            default:
                break;
        }
        return sb.toString();
    }
}