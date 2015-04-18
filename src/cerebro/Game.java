/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public Game(Id3 tree) {
        this.tree = tree;
        this.currentNode = tree;
    }
    
    public void start() {
        boolean propositionVraie = false;
        ArrayList<String> questionsFloues = new ArrayList<String>();
        ArrayList<String> userResponseIntitule = new ArrayList<String>();
        ArrayList<String> userResponseValue = new ArrayList<String>();
        
        
        while (!propositionVraie) {
            if (this.currentNode.m_Successors.length == 0) {
                System.out.println("Votre personnnage est-il ");
                System.out.println(this.currentNode.m_Attribute.name());
                System.out.println(this.currentNode.m_Attribute);
                System.out.println("? y/n");
                propositionVraie = this.inputUser(new String[]{"y","n"}).equals("y");
                if (propositionVraie) {
                    //C'est le GG
                    //Ajouter banane
                } else {
                    //OMG YOU SUCK
                    if (questionsFloues.isEmpty()) {
//                        //on s'a gourré
//                        System.out.println("Qui est votre personnage ?");
//                        inputUser();
//                        if(Le perso existe déjà dans la DB) {
//                            //EleverPoint...
//                        } else {
//                          System.out.println("Entrez une question qui différencie votre personnage de celui proposé : ");
//                          inputUser();
//                        //TODO ajouter la question ET LA REPONSE dans la DB
//                        //TODO ajouter personnage dans la DB
                          //TODO ajouter aussi les réponses des questions précédentes SAUF LES PEUT ETRE
//                        //TODO pour tous les anciens persos, mettre la réponse a la nouvelle question a true
//                        }
                    }
                    String qFloueCourante = questionsFloues.get(questionsFloues.size()-1);
                    int idQCourante = userResponseIntitule.indexOf(qFloueCourante);
                    //userResponseValue.set(idQCourante,!userResponseValue.get(idQCourante));
                    questionsFloues.remove(questionsFloues.size()-1);
                    
//                    TODO redescendre sur l'arbre
                }
            } else {
                //On est forcement dans un noeud
                System.out.println(currentNode.m_Attribute);
                userResponseIntitule.add(currentNode.m_Attribute.toString());
                String CurrentResponse = inputUser();
                userResponseValue.add(CurrentResponse);
                switch(CurrentResponse){
                    case "Probablement oui" :
                        questionsFloues.add(currentNode.m_Attribute.name());
                    case "Oui" :
                        currentNode = currentNode.m_Successors[1];
                        break;
                    case "Probablement non" :
                        questionsFloues.add(currentNode.m_Attribute.name());
                    case "Non" :
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
        String result = sc.nextLine();
        return sc.nextLine();
    }
}
