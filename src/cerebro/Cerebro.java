package cerebro;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        Model model = new Model();
        Id3 tree = model.getTreeFromDB();
        Game game = new Game(tree);

        game.start();

        // Tests
//        -- addScore
//        ArrayList<String> q = new ArrayList<>();
//        q.add("Votre personnage peut-il mourir ?");
//        q.add("Le personnage fait-il partie des Watchmen ?");
//        model.addScore("Rorschach", q);

//        -- addCharacter
//        ArrayList<String> q = new ArrayList<>();
//        q.add("Votre personnage peut-il mourir ?");
//        q.add("Le personnage fait-il partie des Watchmen ?");
//
//        ArrayList<String> r = new ArrayList<>();
//        r.add("Non");
//        r.add("Non");
//
//        model.addNewHero("Dio_Brando", "Le personnage est-il un faggot ?", false, q, r);
    }
}
