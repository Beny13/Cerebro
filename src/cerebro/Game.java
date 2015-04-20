/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cerebro;

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
        while(!propositionVraie) {
            if(this.currentNode.m_Successors.length == 0) {
                System.out.println("Votre personnnage est-il ");
                System.out.println(this.currentNode.m_Attribute.name());
                System.out.println(this.currentNode.m_Attribute);
                System.out.println("? y/n");
                propositionVraie = this.inputUser(new String[]{"y","n"}).equals("y");
                if(propositionVraie) {
                    //C'est le GG
                    //Ajouter banane
                }
            }
        }
    }
    
    private String inputUser(String[] rep) {
        Scanner sc = new Scanner(System.in);
        String result = sc.nextLine();
        while(java.util.Arrays.asList(rep).indexOf(result) == -1) {
            System.out.println("?");
            result = sc.nextLine();
        }
        return result;
    }
}
