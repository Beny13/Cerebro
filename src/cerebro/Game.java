package cerebro;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private final Id3 tree;
    private Id3 currentNode;
    private final Model model;
    private ArrayList<String> userResponseIntitule = new ArrayList<String>();
    private ArrayList<String> userResponseValue = new ArrayList<String>();
    private final Window window;

    public Game(Id3 tree, Window window) {
        this.tree = tree;
        this.currentNode = tree;
        this.model = new Model();
        this.window = window;
    }

    public void start() {
        this.window.setCurrentPanel("question");
        boolean propositionVraie = false;
        ArrayList<String> questionsFloues = new ArrayList<String>();

        while (!propositionVraie) {
            if (this.currentNode.m_Successors == null) {
                String suggestedHero = this.currentNode.m_ClassAttribute.value((int) this.currentNode.m_ClassValue);
                propositionVraie = this.suggestHero(suggestedHero);
                if (propositionVraie) {
                    model.changeScore(suggestedHero, this.userResponseIntitule, this.userResponseValue);
                    // End here
                    return;
                } else {
                    if (questionsFloues.isEmpty()) {
                        String newHero = this.getNewHero();
                        // this.inputButtonUser("apprentissagePerso");
                        if(model.characterAlreadyExists(newHero)) {
                            this.window.setCurrentPanel("chargement");
                            model.changeScore(newHero,this.userResponseIntitule,this.userResponseValue);
                            // End here
                            return;
                        } else {
                            String newResponse = this.getNewResponse();
                            String newQuestion = this.getNewQuestion();
                            boolean ResponseValue;
                            ResponseValue = newResponse.equals("oui");
                            this.window.setCurrentPanel("chargement");
                            deleteMaybe();
                            model.addNewHero(newHero,newQuestion,ResponseValue,suggestedHero);

                            // End here
                            return;
                        }
                    } else {
                        String qFloueCourante = questionsFloues.get(questionsFloues.size()-1);
                        int idQCourante = userResponseIntitule.indexOf(qFloueCourante);
                        String invertedrep = (userResponseValue.get(idQCourante).equals("probablement oui"))?"non":"oui";
                        this.userResponseValue.set(idQCourante,invertedrep);
                        questionsFloues.remove(questionsFloues.size()-1);

                        currentNode = tree;

                        String previousQuestion = "";
                        while(!previousQuestion.equals(qFloueCourante)) {
                            String currentNodeResponse = userResponseValue.get(userResponseIntitule.indexOf(currentNode.m_Attribute.name()));
                            previousQuestion = currentNode.m_Attribute.name();
                            if (currentNodeResponse.equals("oui") || currentNodeResponse.equals("probablement oui")) {
                                currentNode = currentNode.m_Successors[1];
                            } else {
                                currentNode = currentNode.m_Successors[0];
                            }
                        }
                    }
                }
            } else {
                //On est forcement dans un noeud
                String CurrentResponse;
                if(userResponseIntitule.indexOf(currentNode.m_Attribute.name())!=-1) {
                    CurrentResponse = userResponseValue.get(userResponseIntitule.indexOf(currentNode.m_Attribute.name()));
                } else {
                    window.setCurrentQuestion(currentNode.m_Attribute.name());
                    userResponseIntitule.add(currentNode.m_Attribute.name());
                    CurrentResponse = inputButtonUser("question");
                    userResponseValue.add(CurrentResponse);
                }
                    switch(CurrentResponse){
                        case "probablement oui" :
                            questionsFloues.add(currentNode.m_Attribute.name());
                        case "oui" :
                            currentNode = currentNode.m_Successors[1];
                            break;
                        case "probablement non" :
                            questionsFloues.add(currentNode.m_Attribute.name());
                        case "non" :
                            currentNode = currentNode.m_Successors[0];
                            break;
                    }

            }
        }
    }

    private String inputUser(String[] rep) {
        Scanner sc = new Scanner(System.in);
        String result = sc.nextLine();
        while (java.util.Arrays.asList(rep).indexOf(result) == -1) {
            System.out.println("?");
            result = sc.nextLine();
        }
        return result;
    }

    private String inputUser() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private void deleteMaybe(){
        for(String s : this.userResponseValue) {
            if(!s.equals("oui") && !s.equals("non")) {
                this.userResponseIntitule.remove(this.userResponseValue.indexOf(s));
                this.userResponseValue.remove(this.userResponseValue.indexOf(s));
            }
        }
    }

    public boolean askForReplay() {
        this.window.setCurrentPanel("rejouer");

        boolean res = this.inputButtonUser("rejouer").equals("oui");

        if (res)
            this.window.setCurrentPanel("chargement");

        return res;
    }

    private String inputButtonUser(String panelName) {
        String res = "";
        while (res.equals("")) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }

            switch (panelName) {
                case "question":
                    res = window.getCurrentResponse();
                    break;
                case "proposition":
                    res = window.getCurrentPropostionResponse();
                    break;
                case "rejouer":
                    res = window.getCurrentRejouerResponse();
                    break;
                case "apprentissagePerso":
                    res = window.getCurrentApprentissagePersonnage();
                    break;
                case "apprentissageQuestion":
                    res = window.getCurrentApprentissageQuestion();
                    break;
                case "apprentissageReponse":
                    res = window.getCurrentApprentissageResponse();
                    break;
            }
        }
        return res;
    }

    private boolean suggestHero(String suggestedHero) {
        this.window.setCurrentPanel("proposition");
        this.window.setCurrentPropostion(suggestedHero);
        return this.inputButtonUser("proposition").equals("oui");
    }

    private String getNewHero() {
        this.window.setCurrentPanel("apprentissagePerso");
        return this.inputButtonUser("apprentissagePerso");
    }

    private String getNewQuestion() {
        this.window.setCurrentPanel("apprentissageQuestion");
        return this.inputButtonUser("apprentissageQuestion");
    }

    private String getNewResponse() {
        this.window.setCurrentPanel("apprentissageQuestion");
        return this.inputButtonUser("apprentissageReponse");
    }
}
