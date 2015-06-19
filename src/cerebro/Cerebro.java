package cerebro;

import java.awt.event.WindowEvent;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        boolean replay;
        Window window = new Window();
        do {
            Model model = new Model();
            model.clear();
            Game game = new Game(model.getTreeFromDB(), window);
            game.start();
            replay = game.askForReplay();
        } while(replay);
        
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
}
