package cerebro;

import java.util.Scanner;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        Model model = new Model();
        Scanner sc = new Scanner(System.in);
        do {
            model.clear();
            Game game = new Game(model.getTreeFromDB());
            game.start();
            System.out.println("Voulez-vous lancer une nouvelle partie ? oui/non");
        }while(sc.nextLine().equals("oui"));

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
//        r.add("Non");
//        r.add("Non");
//
    }
}
