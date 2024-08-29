package MainClass;

import java.awt.Toolkit;
import java.util.ArrayList; //used to list the images in an array
import java.util.Collections; //used to shuffle the cards (jlabels)
import javax.swing.ImageIcon; //used for images
import javax.swing.JLabel; //used for the first click and 2nd click function
import javax.swing.JOptionPane; //alert

public class Level2 extends javax.swing.JFrame {

    //initialized variables
    private final JLabel[] cardButtons; //initialized card buttons
    private ImageIcon[] cardImages; //initialized images
    private String[] cardValues; //basically useless just to hold a string values (debugging purposes))
    private JLabel firstClicked; //firstclicked card
    private JLabel secondClicked; //2nd clicked card
    private final String imagePath = "/images/"; //locates file faster 
    private int matchCount = 0; //set count start to zero
    private int totalMatches = 0; //set to determine the total match of cards to compare
    private int flipCount = 1; //flip count starts to 0 therefore set it to 1 before flipping the cards
    private final boolean[] matchedCards;
    private final ImageIcon happyFace;
    private final ImageIcon sadFace;

    public Level2() {
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath + "logo.png")));
        initComponents();
        cardButtons = new JLabel[]{
            cardButtons1, cardButtons2, cardButtons3, //initialized card buttons in an array
            cardButtons4, cardButtons5, cardButtons6,
            cardButtons7, cardButtons8, cardButtons9,
            cardButtons10, cardButtons11, cardButtons12
        };

        cardImages = new ImageIcon[]{
            new ImageIcon(getClass().getResource(imagePath + "C01.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "C01.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "C02.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "C02.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "C03.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "C03.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC3K.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC3K.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC2Q.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC2Q.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC1J.jpg")),
            new ImageIcon(getClass().getResource(imagePath + "CC1J.jpg")),
        };
        
        //very important dont modify!
        cardValues = new String[]{ //holds the images as string objects
            "King of Hearts", "King of Hearts",
            "Queen of Hearts", "Queen of Hearts",
            "Jack of Hearts", "Jack of Hearts",
            "10 of Hearts", "10 of Hearts",
            "9 of Hearts", "9 of Hearts",
            "8 of Hearts", "8 of Hearts",
            "7 of Hearts", "7 of Hearts",
            "6 of Hearts", "6 of Hearts",
            "5 of Hearts", "5 of Hearts",
            "4 of Hearts", "4 of Hearts",
            "3 of Hearts", "3 of Hearts",
            "2 of Hearts", "2 of Hearts",
            "ACE of Hearts", "ACE of Hearts"
        };

        //very important dont modify!
        totalMatches = cardButtons.length / 2; //intialized matched card formula will set to stop if call the cards matched. It is to calculate total matches needed to win
        matchCount = 0; //initialized matchcount before running the game
        flipCount = 1; //initialized flipcount before running the game
        matchedCards = new boolean[cardButtons.length]; // Initialize the matched cards array
        happyFace = new ImageIcon(getClass().getResource(imagePath + "face1.png")); // add happy face image
        sadFace = new ImageIcon(getClass().getResource(imagePath + "face2.png")); // add sad face image
        initializeGame(); //initialized eveything inside of run method
    }

    private void initializeGame() {
        ArrayList<Integer> indices = new ArrayList<>(); // ArrayrList to hold card indices (cards)
        for (int i = 0; i < cardButtons.length; i++) {
            indices.add(i); // Add indices to the list
        }

        Collections.shuffle(indices); //card shuffler formula 

        ImageIcon[] shuffledImages = new ImageIcon[cardButtons.length]; // Array to hold shuffled images
        String[] shuffledValues = new String[cardButtons.length];
        for (int i = 0; i < cardButtons.length; i++) {
            int index = indices.get(i);
            shuffledImages[i] = cardImages[index];
            shuffledValues[i] = cardValues[index];
        }
        cardImages = shuffledImages;
        cardValues = shuffledValues;

        for (int i = 0; i < cardButtons.length; i++) {
            cardButtons[i].setIcon(new ImageIcon(getClass().getResource(imagePath + "cardBack.jpg")));
            final int cardIndex = i;
            cardButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardClicked(cardButtons[cardIndex]);
                }
            });
        }
    }

    private void cardClicked(JLabel clickedCard) {
        int clickedIndex = getIndex(clickedCard);
        //if the card matched
        if (matchedCards[clickedIndex]) {
            return; // Do nothing if the card is already matched
        }

        //if the first clicked is CLICKED show the card
        if (firstClicked == null) {
            firstClicked = clickedCard;
            showCard(firstClicked, clickedIndex);
        } else if (secondClicked == null && !clickedCard.equals(firstClicked)) { // if the first card is clicked but the 2nd is not
            secondClicked = clickedCard; //asign the 2nd clicked to clicked card
            showCard(secondClicked, clickedIndex); //then show the 2nd clicked card

            //check if the cards matched
            if (cardValues[getIndex(firstClicked)].equals(cardValues[getIndex(secondClicked)])) { //if the 1st card and 2nd card matched
                updateScore(); //update score
                matchCount++; //add score
                matchedCards[getIndex(firstClicked)] = true; // Mark cards as matched
                matchedCards[getIndex(secondClicked)] = true; // Mark cards as matched
                face.setIcon(happyFace); // Set the face image to happy
                firstClicked = null;
                secondClicked = null;
                if (matchCount == totalMatches) {
                    win(); //win function
                    matchCount = 1; //reset the match count
                    flipCount = 1; //reset the flip count
                }
            } else { //if the cards dont match
                face.setIcon(sadFace); // Set the face image to sad
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        hideCard(firstClicked); //back of the card
                        hideCard(secondClicked); //back of the card
                        firstClicked = null; //do nothing
                        secondClicked = null;
                        updateFlips(); //update flips if no matched cards
                        flipCount++; //must add another flip increment so we can update the flip immediately
                        if (flipCount == 16) { //if flip counts reach 10 then its game over
                            JOptionPane.showMessageDialog(Level2.this, "Number of Flips Reached"); //message dialog
                            gameOver(); //game over function at the bottom
                            matchCount = 1; //reset the match count
                            flipCount = 1; //reset the flip count
                        }
                    }
                },
                        1000 //1000 means 1 second 500 means 500 millisecond (can be reduced)
                );
            }
        }
    }

    private int getIndex(JLabel clickedCard) {
        for (int i = 0; i < cardButtons.length; i++) {
            if (cardButtons[i] == clickedCard) {
                return i;
            }
        }
        return -1;
    }

    private void showCard(JLabel card, int index) {
        card.setIcon(cardImages[index]);
    }

    private void hideCard(JLabel card) {
        card.setIcon(new ImageIcon(getClass().getResource(imagePath + "cardBack.jpg")));
    }

    private void updateScore() {
        Score.setText("Matches: " + matchCount);
    }

    private void updateFlips() {
        numOfFlips.setText("Mistakes: " + flipCount);
    }

    private void resetGame() {
        initializeGame();
        int reset = JOptionPane.showOptionDialog(this, "Are you sure you want to exit?", "Exit?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
        if (reset == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Going back to Main Menu");
            Start strt = new Start();
            strt.show();
            dispose();

        } else if (reset == JOptionPane.NO_OPTION) {
            //do nothing
        }
    }

    private void win() {
        initializeGame();
        int reset = JOptionPane.showOptionDialog(this, "Next Level?", "You Win!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
        if (reset == JOptionPane.YES_OPTION) {
            Level3 lv3 = new Level3();
            lv3.show();
            dispose();

        } else if (reset == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Going back to Main Menu");
            Start strt = new Start();
            strt.show();
            dispose();
        }
    }

    private void gameOver() {
        initializeGame();
        int reset = JOptionPane.showOptionDialog(this, "Reset the Game?", "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Yes", "No"}, JOptionPane.YES_OPTION);
        if (reset == JOptionPane.YES_OPTION) {
            Level1 lv1 = new Level1();
            lv1.show();
            dispose();

        } else if (reset == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Going back to Main Menu");
            Start strt = new Start();
            strt.show();
            dispose();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel = new javax.swing.JPanel();
        face = new javax.swing.JLabel();
        cardButtons1 = new javax.swing.JLabel();
        cardButtons2 = new javax.swing.JLabel();
        cardButtons3 = new javax.swing.JLabel();
        cardButtons4 = new javax.swing.JLabel();
        cardButtons5 = new javax.swing.JLabel();
        cardButtons6 = new javax.swing.JLabel();
        cardButtons7 = new javax.swing.JLabel();
        cardButtons8 = new javax.swing.JLabel();
        cardButtons9 = new javax.swing.JLabel();
        cardButtons10 = new javax.swing.JLabel();
        cardButtons11 = new javax.swing.JLabel();
        cardButtons12 = new javax.swing.JLabel();
        reset = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Score = new javax.swing.JLabel();
        numOfFlips = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(820, 720));
        setSize(new java.awt.Dimension(820, 720));

        Panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        Panel.add(face, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 110, 90));

        cardButtons1.setText("Deck1");
        cardButtons1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons1MouseClicked(evt);
            }
        });
        Panel.add(cardButtons1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 120, 150));

        cardButtons2.setText("Deck1");
        cardButtons2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons2MouseClicked(evt);
            }
        });
        Panel.add(cardButtons2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, 120, 150));

        cardButtons3.setText("Deck1");
        cardButtons3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons3MouseClicked(evt);
            }
        });
        Panel.add(cardButtons3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 120, 150));

        cardButtons4.setText("Deck1");
        cardButtons4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons4MouseClicked(evt);
            }
        });
        Panel.add(cardButtons4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 120, 150));

        cardButtons5.setText("Deck1");
        cardButtons5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons5MouseClicked(evt);
            }
        });
        Panel.add(cardButtons5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 120, 150));

        cardButtons6.setText("Deck1");
        cardButtons6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons6MouseClicked(evt);
            }
        });
        Panel.add(cardButtons6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 330, 120, 150));

        cardButtons7.setText("Deck1");
        cardButtons7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons7MouseClicked(evt);
            }
        });
        Panel.add(cardButtons7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 120, 150));

        cardButtons8.setText("Deck1");
        cardButtons8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons8MouseClicked(evt);
            }
        });
        Panel.add(cardButtons8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 330, 120, 150));

        cardButtons9.setText("Deck1");
        cardButtons9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons9MouseClicked(evt);
            }
        });
        Panel.add(cardButtons9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 490, 120, 150));

        cardButtons10.setText("Deck1");
        cardButtons10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons10MouseClicked(evt);
            }
        });
        Panel.add(cardButtons10, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 490, 120, 150));

        cardButtons11.setText("Deck1");
        cardButtons11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons11MouseClicked(evt);
            }
        });
        Panel.add(cardButtons11, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 120, 150));

        cardButtons12.setText("Deck1");
        cardButtons12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cardButtons12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardButtons12MouseClicked(evt);
            }
        });
        Panel.add(cardButtons12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 490, 120, 150));

        reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout.png"))); // NOI18N
        reset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetMouseClicked(evt);
            }
        });
        Panel.add(reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 60, 50));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Level 2");
        Panel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 27, 100, 30));

        Score.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Score.setText("Matches: ");
        Panel.add(Score, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 100, 30));

        numOfFlips.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        numOfFlips.setText("Mistakes:");
        Panel.add(numOfFlips, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 100, 40));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Matching game.png"))); // NOI18N
        bg.setText("bg");
        Panel.add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 680));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Panel.getAccessibleContext().setAccessibleName("Canvas");
        Panel.getAccessibleContext().setAccessibleDescription("basically where we draw sht");

        getAccessibleContext().setAccessibleName("frames");

        setSize(new java.awt.Dimension(966, 714));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cardButtons5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons5MouseClicked

    private void cardButtons6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons6MouseClicked

    private void cardButtons7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons7MouseClicked

    private void cardButtons8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons8MouseClicked

    private void cardButtons9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons9MouseClicked

    private void cardButtons4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons4MouseClicked

    private void cardButtons3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons3MouseClicked

    private void cardButtons2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons2MouseClicked

    private void cardButtons1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons1MouseClicked

    private void cardButtons10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons10MouseClicked

    private void cardButtons11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons11MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons11MouseClicked

    private void cardButtons12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardButtons12MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cardButtons12MouseClicked

    private void resetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetMouseClicked
        // TODO add your handling code here:
        resetGame();
    }//GEN-LAST:event_resetMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Level2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Level2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Level2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Level2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Level2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel;
    private javax.swing.JLabel Score;
    private javax.swing.JLabel bg;
    private javax.swing.JLabel cardButtons1;
    private javax.swing.JLabel cardButtons10;
    private javax.swing.JLabel cardButtons11;
    private javax.swing.JLabel cardButtons12;
    private javax.swing.JLabel cardButtons2;
    private javax.swing.JLabel cardButtons3;
    private javax.swing.JLabel cardButtons4;
    private javax.swing.JLabel cardButtons5;
    private javax.swing.JLabel cardButtons6;
    private javax.swing.JLabel cardButtons7;
    private javax.swing.JLabel cardButtons8;
    private javax.swing.JLabel cardButtons9;
    private javax.swing.JLabel face;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel numOfFlips;
    private javax.swing.JLabel reset;
    // End of variables declaration//GEN-END:variables
}
