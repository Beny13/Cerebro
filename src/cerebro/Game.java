package cerebro;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Damien
 */
public class Game {
    private final Id3 tree;
    private Id3 currentNode;
    private Model model;
    private ArrayList<String> userResponseIntitule = new ArrayList<String>();
    private ArrayList<String> userResponseValue = new ArrayList<String>();

    public Game(Id3 tree) {
        this.tree = tree;
        this.currentNode = tree;
        this.model = new Model();
    }

    public void start() {
        boolean propositionVraie = false;
        ArrayList<String> questionsFloues = new ArrayList<String>();

        while (!propositionVraie) {
            if (this.currentNode.m_Successors == null) {
                String suggestedHero = this.currentNode.m_ClassAttribute.value((int) this.currentNode.m_ClassValue);
                System.out.println("Votre personnnage est-il ");
                System.out.println(suggestedHero);
                System.out.println("? oui/non");
                propositionVraie = this.inputUser(new String[]{"oui","non"}).equals("oui");
                if (propositionVraie) {
                    model.changeScore(suggestedHero, this.userResponseIntitule, this.userResponseValue);
                    // End here
                    return;
                } else {
                    if (questionsFloues.isEmpty()) {
                        System.out.println("Qui est votre personnage ?");
                        String newHero = inputUser();
                        if(model.characterAlreadyExists(newHero)) {
                            System.out.println("Ce personnage existe déjà dans la base de données. Ses informations ont été mises à jour selon vos réponses.");
                            model.changeScore(newHero,this.userResponseIntitule,this.userResponseValue);
                            // End here
                            return;
                        } else {
                            System.out.println("Entrez une question qui différencie votre personnage de celui proposé : ");
                            String newQuestion = inputUser();
                            System.out.println("Quelle est la réponse à cette question pour votre personnage ? oui/non");
                            String newResponse = inputUser(new String[]{"oui","non"});
                            boolean ResponseValue;
                            ResponseValue = newResponse.equals("oui");
                            deleteMaybe();
                            model.addNewHero(newHero,newQuestion,ResponseValue,suggestedHero);
                            System.out.println("Votre héros à bien été ajouté !");

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
                System.out.println(currentNode.m_Attribute.name() + " oui/non/probablement oui/probablement non");
                userResponseIntitule.add(currentNode.m_Attribute.name());
                String CurrentResponse = inputUser(new String[]{"oui","non","probablement oui","probablement non"});
                userResponseValue.add(CurrentResponse);
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
}
