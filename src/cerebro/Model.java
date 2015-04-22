package cerebro;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
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

    public void clear() {
        this.em.clear();
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

        System.out.println(arff);
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

    public void changeScore(String heroName, ArrayList<String> questions, ArrayList<String> userAnswers) {
        // Les "peut-etre" sont filtrés
        List<Answer> answers = em
            .createNamedQuery("Answer.findByQuestionNameAndHeroName", Answer.class)
            .setParameter("questions", questions)
            .setParameter("characterName", heroName)
            .getResultList()
        ;

        for (Answer answer : answers) {
            String currentUserAnswer = userAnswers.get(questions.indexOf(answer.getQuestion().getQuestionText()));
            if (answer.getAnswerValue().equals(currentUserAnswer.equals("oui"))) {
                answer.setAnswerScore(answer.getAnswerScore() + 1);
            } else {
                int newScore = answer.getAnswerScore() - 1;
                if (newScore < 0) {
                    // Switch answer
                    answer.setAnswerValue(!answer.getAnswerValue());
                    newScore = 0;
                }
                answer.setAnswerScore(newScore);
            }
        }

        this.flush();
    }

    public void addNewHero(String heroName, String newQuestionText, Boolean newAnswerValue, String suggestedHero) {
        Question newQuestion = new Question();
        newQuestion.setQuestionText(newQuestionText);
        this.persist(newQuestion);

        // Setting old heros
        List<Hero> heros = em.createNamedQuery("Hero.findAll", Hero.class).getResultList();
        for (Hero hero : heros) {
            Answer tmpAnswer = new Answer();
            tmpAnswer.setAnswerPK(new AnswerPK(hero.getCharacterId(), newQuestion.getQuestionId()));
            tmpAnswer.setAnswerValue(false);
            tmpAnswer.setAnswerScore(0);
            this.persist(tmpAnswer);
        }

        // Setting new hero
        Hero newHero = new Hero();
        newHero.setCharacterName(heroName);
        this.persist(newHero);

        // Adding answer for new question
        Answer newAnswer = new Answer();
        newAnswer.setAnswerPK(new AnswerPK(newHero.getCharacterId(), newQuestion.getQuestionId()));
        newAnswer.setAnswerValue(newAnswerValue);
        newAnswer.setAnswerScore(2);
        this.persist(newAnswer);

        // Getting suggestedHeroAnswers
        Collection<Answer> suggestedHeroAnswers = em
            .createNamedQuery("Hero.findByCharacterName", Hero.class)
            .setParameter("characterName", suggestedHero)
            .getSingleResult()
            .getAnswerCollection()
        ;

        // Copying suggestedHeroAnswers to newHero
        for (Answer answerToCopy : suggestedHeroAnswers) {
            if (answerToCopy.getQuestion().getQuestionText().equals(newQuestionText)) {
                continue;
            }

            Answer copiedAnswer = new Answer();
            copiedAnswer.setAnswerPK(new AnswerPK(
                newHero.getCharacterId(),
                answerToCopy.getAnswerPK().getQuestionId()
            ));
            copiedAnswer.setAnswerValue(answerToCopy.getAnswerValue());
            copiedAnswer.setAnswerScore(2);
            this.persist(copiedAnswer);
        }

        /*
        // Adding answers for old questions that have been answered
        int index = 0;
        for (String question : questions) {
            if (question.equals(newQuestionText)) {
                continue;
            }

            Question oldQuestion = em
                .createNamedQuery("Question.findByQuestionText", Question.class)
                .setParameter("questionText", question)
                .getSingleResult()
            ;


            // TODO INVESTIGER
            // LE PERSIST EST CASSE !

            Answer newAnswerForOldQuestion = new Answer();
            newAnswerForOldQuestion.setAnswerPK(new AnswerPK(newHero.getCharacterId(), oldQuestion.getQuestionId()));
            newAnswerForOldQuestion.setAnswerValue(answers.get(index).equals("oui"));
            newAnswerForOldQuestion.setAnswerScore(2);
            this.persist(newAnswerForOldQuestion);
            ++index;
        }

        // Adding answers for old questions that have NOT been answered
        List<Question> oldNoAnswerQuestions = em
            .createNamedQuery("Question.findByNotInQuestionTexts", Question.class)
            .setParameter("questionTexts", questions)
            .getResultList()
        ;

        for (Question oldNoAnswerQuestion : oldNoAnswerQuestions) {
            // We dont want to add the same answer twice
            if (oldNoAnswerQuestion.getQuestionText().equals(newQuestionText)) {
                continue;
            }

            Answer oldAnswer = new Answer();
            oldAnswer.setAnswerPK(new AnswerPK(newHero.getCharacterId(), oldNoAnswerQuestion.getQuestionId()));
            oldAnswer.setAnswerValue(false);
            oldAnswer.setAnswerScore(0);
            this.persist(oldAnswer);
        }
        */

        // Flushing
        this.flush();
    }

    private void persist(Object object) {
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    private void flush() {
        em.getTransaction().begin();
        em.flush();
        em.getTransaction().commit();
    }

    boolean characterAlreadyExists(String newHero) {
        List<Hero> heros = em
            .createNamedQuery("Hero.findByCharacterName", Hero.class)
            .setParameter("characterName", newHero)
            .getResultList()
        ;

        return heros.size() > 0;
    }

}
