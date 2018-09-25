import javax.swing.*;
import java.lang.ClassLoader;
//Most of the code included in this file was from the template, i just changed some names to clarify functionality
public class Card extends JButton {
    private String imgFiles[] = {"facebook.png", "googleplus.png", "instagram.png", "linkdin.png",
            "messenger.png", "pinterest.png", "reddit.png", "santa.png", "snapchat.png", "twitter.png",
            "vine.png", "youtube.png", "happyface.png"};

    private ClassLoader loader = getClass().getClassLoader();
    private Icon FrontIcon;
    private Icon BackIcon = new ImageIcon(loader.getResource("cardback.png"));
    private int ID;
    private String name;

    public Card(){
        super();
    }

    public Card(ImageIcon image){
        super();
        FrontIcon = image;
        super.setIcon(BackIcon);
    }

    public void displayFrontImage(){
        super.setIcon(FrontIcon);
    }
    public void HideFrontImage(){
        super.setIcon(BackIcon);
    }

    public int ID(){
        return ID;
    }
    public void SetID(int n){
        ID = n;
    }
}
