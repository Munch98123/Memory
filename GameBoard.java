import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class GameBoard {
    private String imgFiles[] = {"github.png", "myspace.png", "instagram.png", "linkdin.png",
            "messenger.png", "pinterest.png", "reddit.png", "google.png", "snapchat.png", "twitter.png",
            "vine.png", "youtube.png", "happyface.png"};
    private List<Card> cards = new ArrayList<Card>();
    private ClassLoader loader = getClass().getClassLoader();
    private Card FirstCardOfPair, SecondCardOfPair, WildCard;
    public GameBoard(ActionListener event)  {
        int cardPair = 1;
        /*
        **we want to set up two cards at a time so we keep the loop to about half of the total board space
        **then we check to make sure we havent gotten to the end of the loop: thats when we create the happy face card
        **each card is set up by grabbing the icon and creating the button itself with the icon attached
        **then we add it to an ArrayList for ease of use
        */
        for(int  i = 0;i < 13;i++){
            if(i == 12){
                ImageIcon imgIcon = new ImageIcon(loader.getResource(imgFiles[i]));
                WildCard = new Card(imgIcon);
                WildCard.SetID(cardPair);
                WildCard.addActionListener(event);
                cards.add(WildCard);
            }
            else {
                ImageIcon imgIcon = new ImageIcon(loader.getResource(imgFiles[i]));
                FirstCardOfPair = new Card(imgIcon);
                FirstCardOfPair.SetID(cardPair);
                FirstCardOfPair.addActionListener(event);
                FirstCardOfPair.setPreferredSize(new Dimension(20,20));
                cards.add(FirstCardOfPair);

                SecondCardOfPair = new Card(imgIcon);
                SecondCardOfPair.SetID(cardPair);
                SecondCardOfPair.addActionListener(event);
                SecondCardOfPair.setPreferredSize(new Dimension(20,20));
                cards.add(SecondCardOfPair);

                cardPair++;
            }
        }
        //really want to make sure the cards get shuffled well
        //one shuffle seemed to put cards next to each other very frequently
        Collections.shuffle(cards);
        Collections.shuffle(cards);
    }
    //heper function for filling the jpanel with cards
    public void fillBoardView(JPanel view){
        for (Card c : cards){
            view.add(c);
        }
    }
    //helper function for deleting the cards from the board
    public void resetBoard(){
        for (int c = 0;c < cards.size();c++){
            cards.remove(c);
        }
    }
}
