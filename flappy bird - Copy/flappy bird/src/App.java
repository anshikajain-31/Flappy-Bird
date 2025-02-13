import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    public static void main(String[] args) throws Exception {
   int boardwidth=360;
   int boardheight=640;

   JFrame frame=new JFrame("Flappy bird");
   frame.setVisible(true);
   frame.setSize(boardwidth,boardheight);
   frame.setLocationRelativeTo(null);
   frame.setResizable(false);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   


   

   Flappybird flappybird =new Flappybird();
   frame.add(flappybird);
   JButton button1 = new JButton("day theme");
   JButton button2 = new JButton("night theme");

   button1.setBackground(Color.white);
        button1.setForeground(Color.black);
        button1.setBorder(BorderFactory.createLineBorder(Color.black));

        button2.setBackground(Color.white);
        button2.setForeground(Color.black);
        button2.setBorder(BorderFactory.createLineBorder(Color.black));

   button1.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Change background image to img1
        flappybird.setBackgroundImage("./flappybirdbg.png");
        flappybird.requestFocus(true);

    }
});

button2.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        flappybird.setBackgroundImage("night.png");
        flappybird.requestFocus(true);

    }
});

JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        button1.setPreferredSize(new Dimension(150, 50)); // Adjust width and height as needed
button2.setPreferredSize(new Dimension(150,50));

        // Add button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
   frame.pack();
   flappybird.requestFocus();
   frame.setVisible(true);
    }
}
