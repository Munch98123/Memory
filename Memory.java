import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Memory extends JFrame implements ActionListener
{
    // Core game play objects
    private GameBoard gameBoard;
    private Card prevCard1 = null, prevCard2 = null;

    // Labels to display game info
    private JLabel errorLabel, timerLabel, scoreLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0;
    private int pairsFound = 0, wildCardFound;

    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;
    private Timer delay;

    public Memory()
    {
        // Call the base class constructor
        super("Hubble Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Restart");
        JButton quit = new JButton("Quit");
        timerLabel = new JLabel("Timer: 0");
        errorLabel = new JLabel("Errors: 0");

        quit.addActionListener(this);
        restart.addActionListener(this);
        //declares action listener for the game timer
        ActionListener seconds = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTime+=1;
                timerLabel.setText("Timer: " + gameTime);
            }
        };

        gameTimer = new Timer(1000,seconds);
        int timeDelay = 1000;
        //initially hide front images, declares action listener for later
        ActionListener hideDelay = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevCard1.HideFrontImage();
                prevCard2.HideFrontImage();
                prevCard1 = null;
                prevCard2 = null;
            }
        };
        delay = new Timer(timeDelay, hideDelay);
        delay.setRepeats(false);

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new GameBoard(this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 10, 10));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(2, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(timerLabel);
        labelView.add(errorLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.CENTER);

        setSize(700, 600);
        //centers the panel
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e)
    {
        //functionality of quit button
        if(e.getActionCommand().equals("Quit")){
            System.exit(0);
        }
        //functionality of restart button
        else if(e.getActionCommand().equals("Restart")){
            restartGame();
        }
        else if(!delay.isRunning()){
            Card currCard = (Card)e.getSource();
            gameTimer.start();
            //when the first card is clicked:
            //     Make sure the card is not the wild card
            //     If it is, then disable its action listener and change the prevCard back to null;
            //
            if(prevCard1 == null && prevCard2 == null){
                prevCard1 = currCard;
                if(prevCard1.ID() == 13){
                    prevCard1.displayFrontImage();
                    prevCard1.removeActionListener(this);
                    prevCard1 = null;
                    wildCardFound++;
                }
                else {
                    prevCard1.removeActionListener(this);
                    prevCard1.displayFrontImage();
                    clickCount += 1;
                }
            }
            //what to do when first card was clicked and second card has just been clicked
            else if(prevCard1 != null && prevCard2 == null){
                prevCard2 = currCard;
                prevCard2.removeActionListener(this);
                prevCard2.displayFrontImage();
            }
            if(prevCard1 != null && prevCard2 != null){
                //if the ids do not match:
                //  reinstate the listener for both
                //  flip cards back over after timer ends
                if (prevCard1.ID() != prevCard2.ID()){
                    prevCard1.addActionListener(this);
                    prevCard2.addActionListener(this);
                    delay.start();
                    errorCount +=1;
                    errorLabel.setText("Errors: " + errorCount);
                    new Timer(750, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            prevCard1.HideFrontImage();
                            prevCard2.HideFrontImage();
                            prevCard1 = null;
                            prevCard2 = null;
                        }
                    });
                }
                //if cards match:
                //  increment pairsFound by 1
                //  change previous cards back to null
                else if(prevCard1.ID() == prevCard2.ID()){
                    pairsFound +=1;
                    //greys out the button so it wont be used again(ActionListener already disabled)
                    prevCard1.setEnabled(false);
                    prevCard2.setEnabled(false);
                    prevCard1 = null;
                    prevCard2 = null;
                }
            }
        }
        //we want the user to know when the game ends so a pop up window tells them they have with their score and number of errors
        if(pairsFound == 12 && wildCardFound == 1){
            gameTimer.stop();
            String text = "You Win!\n12 Pairs found with:\n" + errorCount + "errors";
            JOptionPane.showMessageDialog(null, text,"Memory", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void restartGame()
    {
        gameTimer.stop();
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        timerLabel.setText("Timer: 0");
        errorLabel.setText("Errors: 0");
        scoreLabel.setText("Score: 0");

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard = new GameBoard(this);
        gameBoard.fillBoardView(boardView);
        //added this to template because otherwise it wouldnt display the new board
        setVisible(true);
    }

    public static void main(String args[])
    {
        Memory M = new Memory();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
