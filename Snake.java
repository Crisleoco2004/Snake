import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.Color;

public class Snake extends JFrame 
{

    int width = 640;
    int height = 480;
    static int minutos = 0;
    static int segundos = 0;
    Point snake;
    Point comida;
    static boolean gameOver = false;
    int widthPoint = 10;
    int heightPoint = 10;
    static long frecuencia = 100;
    ArrayList<Point> lista = new ArrayList<Point>();
    int direccion = KeyEvent.VK_LEFT;
    ImagenSnake imagenSnake;

    public Snake() {
        setTitle("Snake");
        setSize(width, height);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Teclas teclas = new Teclas();
        this.addKeyListener(teclas);

        snake = new Point(width / 2, height / 2);

        startGame();

        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        setVisible(true);

        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame() {
        comida = new Point(200, 200);
        snake = new Point(width / 2, height / 2);
        crearComida();
        lista = new ArrayList<Point>();
        // lista.add(snake);
        crearComida();
    }

    public void crearComida() {
        Random rnd = new Random();

        comida.x = rnd.nextInt(width);
        if ((comida.x % 10) < 500) {
            comida.x = comida.x - (comida.x % 5);
        }
        if (comida.x > 600) {
            comida.x = comida.x - 50;
        }
        comida.y = rnd.nextInt(height);
        if ((comida.y % 10) < 350) {
            comida.y = comida.y - (comida.y % 5);
        }
        if (comida.y > 440) {
            comida.y = comida.y - 80;
        }
    }

    public static void main(String[] args) throws Exception {
        Snake s = new Snake();
        for (minutos = 0; minutos < 60; minutos++) {
            for (segundos = 0; segundos < 60; segundos++) {
                delaySegundo();
                if (gameOver)
                {
                    break;
                }
            }
            if (gameOver)
            {
                break;
            }
        }
    }
    
    private static void delaySegundo()
    {
        try
        {
            Thread.sleep(1000);
        }catch(InterruptedException e){}
    }

    public void actualizar()
    {
        imagenSnake.repaint();
        lista.add(0,new Point(snake.x, snake.y));
        lista.remove((lista.size()-1));

        for(int i = 1;i < lista.size();i++)
        {
            Point punto = lista.get(i);
            if(snake.x == punto.x && snake.y == punto.y)
            {
                gameOver = true;
            }
        }

        if((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10)) && (snake.y < (comida.y + 10)))
        {
            lista.add(0,new Point(snake.x,snake.y));
            frecuencia--;
            crearComida();
        }
    }

    public class ImagenSnake extends JPanel 
    {
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(new Color (0,0,255));
            g.fillRect(snake.x,snake.y, widthPoint, heightPoint);
            for (int i=0; i<lista.size();i++)
            {
                Point point = (Point)lista.get(i);
                g.fillRect(point.x, point.y, widthPoint, heightPoint);
            }

            g.setColor(new Color (255, 0, 0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            g.setColor(new Color (100, 200, 0));
            g.drawString("SCORE: " + lista.size(), 10, 10);
            g.drawString("TIME: " + minutos + ":" + segundos, 150 , 10);

            if(gameOver)
            {
                g.setColor(new Color (185, 193, 231));
                g.fillRect(0, 0, width, height);
                g.setColor(new Color (0, 0, 0));
                g.drawString("GAME OVER", width/2 - 30, height/2);
                g.drawString("Press (esc) for exit.", width/2 - 30, height/2 + 10);
                g.drawString("Your score was: " + lista.size(), width/2 - 30, height/2 + 20);
                g.drawString("Your time was: : " + minutos + ":" + segundos, width/2 - 30, height/2 + 30);
            }
        }
    }

    
    public class Teclas extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                System.exit(0);
            }else if(e.getKeyCode() == KeyEvent.VK_UP){
                if(direccion != KeyEvent.VK_DOWN)
                {
                    direccion = KeyEvent.VK_UP;
                }
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                if(direccion != KeyEvent.VK_UP)
                {
                    direccion = KeyEvent.VK_DOWN;
                }
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                if(direccion != KeyEvent.VK_RIGHT)
                {
                    direccion = KeyEvent.VK_LEFT;
                }
            }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(direccion != KeyEvent.VK_LEFT)
                {
                    direccion = KeyEvent.VK_RIGHT;
                }
            }
        }
    }

    public class Momento extends Thread 
    {
        long last = 0;
        public void run()
        {
            while(true)
            {
                if((java.lang.System.currentTimeMillis() - last) > frecuencia)
                {
                    if(!gameOver)
                    {
                    
                        if(direccion == KeyEvent.VK_UP)
                        {
                            snake.y = snake.y - heightPoint;
                            if (snake.y > height)
                            {
                                snake.y = 0;
                            }
                            if(snake.y < 0)
                            {
                                snake.y = height - heightPoint;
                            }
                        }else if(direccion == KeyEvent.VK_DOWN)
                        {
                            snake.y = snake.y + widthPoint;
                            if(snake.y > height)
                            {
                                snake.y = 0;
                            }
                            if(snake.y < 0)
                            {
                                snake.y = height - heightPoint;
                            }
                        }else if(direccion == KeyEvent.VK_LEFT)
                        {
                            snake.x = snake.x - widthPoint;
                            if(snake.x > width)
                            {
                                snake.x = 0;
                            }
                            if(snake.x < 0)
                            {
                                snake.x = width - widthPoint;
                            }
                        }else if(direccion == KeyEvent.VK_RIGHT)
                        {
                            snake.x = snake.x + widthPoint;
                            if(snake.x > width)
                            {
                                snake.x = 0;
                            }
                            if(snake.x < 0)
                            {
                                snake.x = width - widthPoint;
                            }
                        }
                    }
                    
                    actualizar();
                    last = java.lang.System.currentTimeMillis();
                    
                }
            }
        }
    }
}
