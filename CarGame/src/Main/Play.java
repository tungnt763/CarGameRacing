package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Play extends JFrame implements KeyListener, ActionListener {
    private Background bg = new Background();
    private Lane lane = new Lane();
    private Car user = new Car();

    // Chuong ngai vat
    Random random = new Random();
    private Obstacle obs = new Obstacle();
    private int cxpos1 = 0, cxpos2 = 1, cxpos3 = 2;
    private int cypos1 = random.nextInt(3), cypos2 = random.nextInt(3), cypos3 = random.nextInt(3);
    int y1pos = obs.y[cypos1], y2pos = obs.y[cypos2], y3pos = obs.y[cypos3];
    private ImageIcon[] obsImage = new ImageIcon[20];
    int[] randNum = { random.nextInt(6), random.nextInt(6), random.nextInt(6) };

    // Lam duong chuyen dong
    private int roadmove = 0;
    private int score = 0, delay = 100, speed = 90;
    private boolean rightrotate = false, gameover = false, paint = false;
    ImageIcon bgImage, carImage;

    public Play(String title) {
        super(title);
        setBounds(400, 10, 650, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        addKeyListener(this);
        setFocusable(true);
        // ko dieu chinh dc size
        setResizable(false);
    }

    public void paint(Graphics g) {
        // paint lane and background
        bgImage = new ImageIcon(bg.getPath());
        bgImage.paintIcon(this, g, bg.getX(), bg.getY());
        g.setColor(Color.gray);
        g.fillRect(100, 0, 10, 800);
        g.fillRect(540, 0, 10, 800);
        g.setColor(Color.black);
        g.fillRect(lane.getX(), lane.getY(), lane.getWidth(), lane.getHeight());

        // ve vach trang
        if (roadmove == 0) {
            for (int i = 50; i <= 700; i += 200) {
                g.setColor(Color.white);
                g.fillRect(240, i, 20, 80);
                g.fillRect(390, i, 20, 80);
            }
            roadmove = 1;
        } else if (roadmove == 1) {
            for (int i = 100; i <= 700; i += 200) {
                g.setColor(Color.white);
                g.fillRect(240, i, 20, 80);
                g.fillRect(390, i, 20, 80);
            }
            roadmove = 0;
        }

        /// for road
        carImage = new ImageIcon(user.getPath());
        Image originalImage = carImage.getImage();
        Image resizeImage = originalImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        carImage = new ImageIcon(resizeImage);
        carImage.paintIcon(this, g, user.getX(), user.getY());

        // opposite car
        int cnt = 0;
        for (int j = 0; j < 3; j++) {
            int i = randNum[j];
            obsImage[j] = new ImageIcon(obs.path[i]);
            originalImage = obsImage[j].getImage();
            resizeImage = originalImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            obsImage[j] = new ImageIcon(resizeImage);
            if (cnt == 0) {
                obsImage[j].paintIcon(this, g, obs.x[cxpos1], y1pos);
                cnt = 1;
            } else if (cnt == 1) {
                obsImage[j].paintIcon(this, g, obs.x[cxpos2], y2pos);
                cnt = 2;
            } else {
                obsImage[j].paintIcon(this, g, obs.x[cxpos3], y3pos);
                cnt = 0;
            }
        }
        y1pos += 50;
        y2pos += 50;
        y3pos += 50;

        if (y1pos > 800) {
            cypos1 = random.nextInt(3);
            y1pos = obs.y[cypos1];
        }

        if (y2pos > 800) {
            cypos2 = random.nextInt(3);
            y2pos = obs.y[cxpos2];
        }

        if (y3pos > 800) {
            cypos3 = random.nextInt(3);
            y3pos = obs.y[cxpos3];
        }

        if (cypos1 == cypos2) {
            cxpos1 -= 1;
            if (cxpos1 < 0) {
                cxpos1 += 2;
            }
            y1pos = obs.y[cxpos1];
        }

        if (cxpos1 == cxpos2 && cypos1 > -100 && cypos2 > -100) {
            cxpos1 -= 1;
            if (cxpos1 < 0) {
                cxpos1 += 2;
            }
        }

        if (cxpos1 == cxpos3 && cypos1 > -100 && cypos3 > -100) {
            cxpos3 -= 1;
            if (cxpos3 < 0) {
                cxpos3 += 2;
            }
        }

        if (cxpos2 == cxpos3 && cypos3 > -100 && cypos2 > -100) {
            cxpos2 -= 1;
            if (cxpos2 < 0) {
                cxpos2 += 2;
            }
        }

        if (y1pos < user.getY() && y1pos + 150 > user.getY() && obs.x[cxpos1] == user.getX())
            gameover = true;
        if (y2pos < user.getY() && y2pos + 150 > user.getY() && obs.x[cxpos2] == user.getX())
            gameover = true;
        if (y3pos < user.getY() && y3pos + 150 > user.getY() && obs.x[cxpos3] == user.getX())
            gameover = true;
        if (user.getY() < y1pos && user.getY() + 150 > y1pos && obs.x[cxpos1] == user.getX())
            gameover = true;
        if (user.getY() < y2pos && user.getY() + 150 > y2pos && obs.x[cxpos2] == user.getX())
            gameover = true;
        if (user.getY() < y3pos && user.getY() + 150 > y3pos && obs.x[cxpos3] == user.getX())
            gameover = true;

        // score
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 20));
        g.drawString("Score: " + score, 125, 55);
        g.drawString("Money: " + speed, 425, 55);
        score++;
        speed++;
        if (speed > 140) {
            speed = 240 - delay;

        }
        if (score % 50 == 0) {
            delay -= 10;
            if (delay < 60) {
                delay = 60;
            }
        }

        // delay
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (y1pos < user.getY() && y1pos + 150 > user.getY() && obs.x[cxpos1] == user.getX())
            gameover = true;
        if (y2pos < user.getY() && y2pos + 150 > user.getY() && obs.x[cxpos2] == user.getX())
            gameover = true;
        if (y3pos < user.getY() && y3pos + 150 > user.getY() && obs.x[cxpos3] == user.getX())
            gameover = true;

        if (gameover) {
            g.setColor(Color.gray);
            g.fillRect(120, 300, 410, 200);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(130, 310, 390, 180);
            g.setFont(new Font("Serif", Font.BOLD, 50));
            g.setColor(Color.yellow);
            g.drawString("Game Over !", 190, 370);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Press Enter to Restart", 170, 450);
            if (!paint) {
                repaint();
                paint = true;
            }
        } else {
            repaint();
        }
    }

    public static void main(String args[]) {
        Play c = new Play("Car Game");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'a' && !gameover) {
            user.setX(user.getX() - 150);

        }
        if (e.getKeyChar() == 's' && !gameover) {
            user.setX(user.getX() + 150);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameover) {
            if (user.getX() - 150 >= 125)
                user.setX(user.getX() - 150);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameover) {
            if (user.getX() + 150 <= 425)
                user.setX(user.getX() + 150);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameover) {
            gameover = false;
            paint = false;
            cxpos1 = random.nextInt(3);
            cxpos2 = random.nextInt(3);
            cypos1 = random.nextInt(5);
            cypos2 = random.nextInt(5);
            y1pos = obs.y[cypos1];
            y2pos = obs.y[cypos2];
            speed = 90;
            score = 0;
            delay = 100;
            user.setX(275);
            user.setY(600);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
