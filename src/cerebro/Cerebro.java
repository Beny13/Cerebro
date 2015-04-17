package cerebro;

import java.util.ArrayList;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        Model model = new Model();
        Id3 tree = model.getTreeFromDB();
        System.out.println(tree);
        
        // TEST POUR addScore
//        ArrayList<String> q = new ArrayList<>();
//        q.add("Votre personnage peut-il mourir ?");
//        q.add("Le personnage fait-il partie des Watchmen ?");
//        model.addScore("Rorschach", q);
        
    }
}
