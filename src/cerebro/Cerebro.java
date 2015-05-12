package cerebro;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        boolean replay;
        do {
            Model model = new Model();
            model.clear();
            Game game = new Game(model.getTreeFromDB());
            game.start();
            replay = game.askForReplay();
        } while(replay);
    }
}
