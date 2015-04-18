package cerebro;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

public class Model {
    private final EntityManager em;

    public Model() {
        this.em = Persistence.createEntityManagerFactory("CerebroPU").createEntityManager();
    }

    public String getArffStringFromDB() {
        String arff = "";
        arff += "@RELATION Cerebro\n";

        // Attributes (questions + characters)
        List<Question> questions = em.createNamedQuery("Question.findAll", Question.class).getResultList();
        for (Question question : questions) {
            arff += "@ATTRIBUTE \"" + question.getQuestionText() + "\" {0,1}\n";
        }

        arff += "@ATTRIBUTE name {";
        List<Hero> list = em.createNamedQuery("Hero.findAll", Hero.class).getResultList();
        int heroIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                arff += ",";
            }

            arff += list.get(i).getCharacterName();
        }
        arff += "}\n";

        arff += "@DATA\n";
        for (Hero hero : list) {
            for (Answer heroAnswer : hero.getAnswerCollection()) {
                arff += heroAnswer.getAnswerValue() ? "1" : "0";
                arff += ",";
            }
            arff += hero.getCharacterName() + "\n";
        }

        return arff;
    }

    public Id3 getTreeFromDB() throws IOException, Exception {
        Instances data = new Instances(new StringReader(this.getArffStringFromDB()));
        data.setClassIndex(data.numAttributes() - 1);

        StringToNominal filter = new StringToNominal();
        filter.setInputFormat(data);
        Filter.useFilter(data, filter);

        Id3 tree = new Id3();
        tree.buildClassifier(data);

        return tree;
    }
}
