package assets;

public class Client {
    private int queue = 0; //0 = not in a queue ,  >0 = the place in the queue

    public synchronized int getQueue() {
        return queue;
    }

    public synchronized void setQueue(int queue) {
        this.queue = queue;
    }

    public String entertain(double nbAlea) {
        String output = "";
        if (nbAlea > 0.5) {
            output = "piano";
        }
        else {
            output = "music";
        }
        return output;
    }

    public String consume(double nbAlea) {
        String output = "";
        if (nbAlea > 0.5) {
            if (nbAlea > 0.75) {
                output = "coffee";
            }
            else {
                output = "tea";
            }
        }
        else {
            if ( nbAlea > 0.33) {
                output = "cake";
            }
            else if (nbAlea > 0.165) {
                output = "cake,coffee";
            }
            else {
                output = "cake,tea";
            }
        }

        return output;
    }
}