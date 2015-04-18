package cerebro;

public class Cerebro {
    public static void main(String[] args) throws Exception {
        Model model = new Model();
        Id3 tree = model.getTreeFromDB();
        System.out.println(tree);
    }
}
