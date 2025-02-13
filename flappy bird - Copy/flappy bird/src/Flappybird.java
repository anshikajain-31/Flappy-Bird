import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Flappybird extends JPanel implements ActionListener,KeyListener{

public void setBackgroundImage(String imagePath) {
        backgroundImg = new ImageIcon(getClass().getResource(imagePath)).getImage(); 
        repaint();
}

    
 // private static final String keyEvent = null;
    int boardWidth=360;
    int boardheight=640;

    Image backgroundImg;
    Image birdImg;
    Image toppipeImg;
    Image bottomPipeImg;


    //bird
    int birdX=boardWidth/8;
    int birdY=boardheight/2;
    int birdwidth=54;
    int birdheight=44;

    class Bird{
        int x =birdX;  
        int y=birdY;
        int width=birdwidth;
        int height=birdheight;
        Image img;

        Bird(Image img){
            this.img=img;
        }
    }


    //pipes

    int pipeX=boardWidth;
    int pipeY=0;
    int pipewidth=64;
    int pipeheight=512;

    class pipe{
        int x=pipeX;
        int y=pipeY;
        int width= pipewidth;
        int height=pipeheight;
        Image img;
        boolean passed = false;

        pipe(Image img){
            this.img=img;
        }

        public Flappybird.pipe get(int i) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'get'");
        }

    }

    //game logic
    Bird bird;
    int velocityX=-4;//move pipe left it will simulate bird moving right
    int velocityY=0;
    int gravity= 1;


    ArrayList<pipe> pipes;
    Random random=new Random();


    Timer gameLoop;
    Timer placepipeTimer;

    boolean gameover =false;
    double score = 0;
    

    /**
     *
     */
    Flappybird(){
        setPreferredSize(new Dimension(boardWidth,boardheight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        backgroundImg=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg=new ImageIcon(getClass().getResource("./Bird-2.png")).getImage();
        toppipeImg=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg=new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

//bird
        bird=new Bird(birdImg);
        pipes=new ArrayList<pipe>();

//place pipe timer
        placepipeTimer=new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placepipes();
            }
        } );
        placepipeTimer.start();
//game timer
        gameLoop=new Timer(1000/60,this);//1000/60=16.66,60 times per second
        gameLoop.start();

    }

    public void placepipes(){

        //pipeheight=512, pipeheight/2=256, mathrandom=(0-1)*256
        //0-128-(0-256)
        //range is pipeheight/4,3pipeheight/4
        int randompipeY=(int)(pipeY-pipeheight/4-Math.random()*(pipeheight/2));
        int openingspace=boardheight/4;


        pipe toppipe=new pipe(toppipeImg);
        toppipe.y=  randompipeY;
        pipes.add(toppipe);

        pipe bottompipe=new pipe(bottomPipeImg);
        bottompipe.y=toppipe.y+pipeheight+openingspace;
        pipes.add(bottompipe);


    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImg,0,0,boardWidth,boardheight,null);

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        //pipes
        for(int i=0;i<pipes.size();i++){
            pipe pipe= pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);

        }
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameover){
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        //pipes
        for(int i=0;i<pipes.size();i++){
            pipe pipe =pipes.get(i);
            pipe.x+=velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score+= 0.5;
            }
            if (collision(bird, pipe)){
                playSound("gameoversound.wav");

                gameover = true;
            }

            if(bird.y>boardheight){
                playSound("gameoversound.wav");

                gameover=true;
            } }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        move();
        repaint();
        if(gameover){
            placepipeTimer.stop();
            gameLoop.stop();

        }
    }


    public boolean collision(Bird a, pipe b){
        
        return a.x < b.x+b.width&&
                a.x+a.width > b.x&&
                a.y < b.y+b.height&&
                a.y+a.height > b.y;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            playSound("jump-coin.wav");
            velocityY=-9;
            if(gameover){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameover = false;
                gameLoop.start();
                placepipeTimer.start();
                
            }
        }
    }
    

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override 
    public void keyReleased(KeyEvent e) {
    }
    private void playSound(String filename) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(filename)));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}