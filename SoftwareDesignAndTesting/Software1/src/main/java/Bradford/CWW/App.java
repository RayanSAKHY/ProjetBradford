package Bradford.CWW;

import Bradford.CWW.GUI.MainGUI;

public class App{

    public static void main(String[] args){
        if (args.length > 0 && args[0].equals("GUI")) {
            javafx.application.Application.launch(MainGUI.class);
        }
        else {
            UserInterface app = new UserInterface();
            app.UseApp();
        }
    }
}
