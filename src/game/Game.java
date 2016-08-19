package game;

import java.awt.*;

import display.Display;
import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game implements Runnable {

    private String title;
    private int width, height;

    private Thread thread;
    private Display display;
    private InputHandler inputHandler;
    private Random random;

    private boolean isRunning;
    public static boolean fire = false;
    public static boolean turn = true;
    public static boolean bulletCreated = false;
    public static boolean cloudCreated = false;
    public static boolean isLighting = false;
    private boolean explosion = false;
    public boolean cloudStop = false;

    private BufferStrategy buffStrat;
    private Graphics graphics;
    private SpriteSheet spriteSheet;
    private SpriteSheet spriteSheetHeart;
    private SpriteSheet spriteSheetPower;
    private SpriteSheet spriteSheetAngle;
    private SpriteSheet spriteSheetChili01;
    private SpriteSheet spriteSheetChili02;
    private SpriteSheet spriteSheetLightPic;
    private SpriteSheet spriteSheetLight;
    private SpriteSheet spriteSheetCloud05;
    private SpriteSheet spriteSheetCloud06;
    private SpriteSheet spriteSheetDarkCloud;
    private SpriteSheet spriteSheetSun;
    private SpriteSheet spriteSheetChili01Over;
    private SpriteSheet spriteSheetChili02Over;

    private int playerWidth = 95;
    private int playerHeight = 94;
    private int playerHeight02 = 94;
    private int playerWidth02 = 95;
    private int playerCrop01 = 0;
    private int playerCrop02 = 94;

    private int playerX01 = 100;
    private int playerX02 = 1_100;

    private int explosionX;
    private int explosionY;
    private int iterator = 0;
    private int cols = 0;
    private double arrowCounter = 0;
    private int lightingCounter = 0;
    private int cloudCounter01 = 0;
    private int cloudCounter02 = 1200;

    private int solarX = 50;
    private int solarY = 50;
    private int solarRadius = 50;
    private int rayLength;
    private int rayLengthDiag;

    public static int airResistance;

    public static int[] groundPoints = new int[1_200];

    public static Player player01;
    public static Player player02;
    public static Bullet bullet;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isRunning = false;
    }

    private void init() {
        this.display = new Display(this.title, this.width, this.height);
        this.display.getCanvas().requestFocus();  //making display focusable
        this.inputHandler = new InputHandler(this.display);
        //randon air resistance for each game
        this.random = new Random();
        this.airResistance = random.nextInt(100);

        //defining ground
        defineGround();
        //creating two players
        this.player01 = new Player("Player01", playerX01, playerWidth, playerHeight, playerCrop01, groundPoints);
        this.player02 = new Player("Player02", playerX02, playerWidth02, playerHeight02, playerCrop02, groundPoints);
        //defining different spriteSheets
        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/explosion.png"));
        this.spriteSheetChili01 = new SpriteSheet(ImageLoader.load("/images/player1.png"));
        this.spriteSheetChili02 = new SpriteSheet(ImageLoader.load("/images/player2.png"));
        this.spriteSheetHeart = new SpriteSheet(ImageLoader.load("/images/heart.png"));
        this.spriteSheetPower = new SpriteSheet(ImageLoader.load("/images/power.png"));
        this.spriteSheetAngle = new SpriteSheet(ImageLoader.load("/images/angle.png"));
        this.spriteSheetLightPic = new SpriteSheet(ImageLoader.load("/images/lightPic.png"));
        this.spriteSheetLight = new SpriteSheet(ImageLoader.load("/images/lightning.png"));
        this.spriteSheetCloud05 = new SpriteSheet(ImageLoader.load("/images/cloud05.png"));
        this.spriteSheetCloud06 = new SpriteSheet(ImageLoader.load("/images/cloud06.png"));
        this.spriteSheetDarkCloud = new SpriteSheet(ImageLoader.load("/images/darkCloud.png"));
        this.spriteSheetSun = new SpriteSheet(ImageLoader.load("/images/sun.png"));
        this.spriteSheetChili01Over = new SpriteSheet(ImageLoader.load("/images/player1win.png"));
        this.spriteSheetChili02Over = new SpriteSheet(ImageLoader.load("/images/player2win.png"));
    }

    private void tick() {  //TICK -> calculate
        this.player01.tick();
        this.player02.tick();
        //creating new bullet
        if (this.fire && this.turn) {
            this.bullet = new Bullet(this.player01.x, this.player01.i * 5, this.player01.strength, this.groundPoints);
            this.bulletCreated = true;
        }
        if (this.fire && !this.turn) {
            this.bullet = new Bullet(this.player02.x, this.player02.i * 5 + 90, this.player02.strength, this.groundPoints);
            this.bulletCreated = true;
        }
        //checking is bullet has been created and update it's position
        if (this.bulletCreated) {
            this.bullet.tick();
        }

        //updating explosion and than in render() crop it's subimage using iterator
        if (this.explosion) {
            iterator++;
            if (iterator == 7) {
                iterator = 0;
                cols++;
            }
            if (cols == 6) {//if cols == 6 we have displayed all animation of explosion
                this.explosion = false;
                iterator = 0;
                cols = 0;
            }
        }

        //making arrows above players moving up & down
        if (arrowCounter < 15) {
            arrowCounter += 0.35;
        } else {
            arrowCounter = 0;
        }
        //moving dark cloud
        if (cloudCreated && this.turn && !this.cloudStop) {
            cloudCounter01 += 3;
            //checking if cloud has reached player and stop moving it; there are several checks, because cloud X increases or decreases with 3 -> += 3
            //and it can sometimes miss player X
            if (cloudCounter01 == player02.x || cloudCounter01 + 1 == player02.x || cloudCounter01 + 2 == player02.x || cloudCounter01 + 3 == player02.x) {
                cloudStop = true;
            }
        } else if (cloudCreated && !this.turn && ! this.cloudStop) {
            cloudCounter02 -= 3;
            if (cloudCounter02 == player01.x || cloudCounter02 - 1 == player01.x || cloudCounter02 - 2 == player01.x || cloudCounter02 - 3 == player01.x) {
                cloudStop = true;
            }
        }
        if (this.cloudStop) {
            this.isLighting = true;
        }
        //every 10th tick of lightning we decrease player's health with one
        if (this.isLighting && this.turn) {
            lightingCounter++;
            if (lightingCounter % 10 == 0) {
                player02.health--;
            }
        } else if (this.isLighting && !this.turn) {
            lightingCounter++;
            if (lightingCounter % 10 == 0) {
                player01.health--;
            }
        }
        //stop lightning and reset counters for cloud and lightning
        if (lightingCounter >= 100) {
            this.isLighting = false;
            lightingCounter = 0;
            cloudCreated = false;
            cloudCounter01 = 0;
            cloudCounter02 = 1200;
            this.cloudStop = false;
            if (this.turn) {
                this.turn = false;
                player01.shock--;
            } else {
                this.turn = true;
                player02.shock--;
            }
        }
        //if bullet has hit ground -> we call redefineGroung() method, change players turn and delete bullet
        if ((this.bullet.xCoord >= 0 && this.bullet.xCoord <= this.groundPoints.length - 1) || this.bullet.yCoord >= 0) {
            if (this.bulletCreated && this.turn && this.bullet.yCoord >= this.groundPoints[this.bullet.xCoord]) {
                redefineGround();
                this.turn = false;
                this.bulletCreated = false;
                //creating explosion
                this.explosionX = this.bullet.xCoord;
                this.explosionY = this.bullet.yCoord;
                this.explosion = true;
                //stop moving player, because his turn is over
                this.player01.isMovingLeft = false;
                this.player01.isMovingRight = false;
                this.player01.isMovingUp = false;
                this.player01.isMovingDown = false;
                this.player01.powerUp = false;
                this.player01.powerDown = false;
                this.bullet.xCoord = 0;
                this.bullet.yCoord = 0;
            } else if (this.bulletCreated && !this.turn && this.bullet.yCoord >= this.groundPoints[this.bullet.xCoord]) {
                redefineGround();
                this.turn = true;
                this.bulletCreated = false;
                //creating explosion
                this.explosionX = this.bullet.xCoord;
                this.explosionY = this.groundPoints[this.bullet.xCoord] - 30;//this.bullet.yCoord;
                this.explosion = true;
                //stop moving player, because his turn is over
                this.player02.isMovingLeft = false;
                this.player02.isMovingRight = false;
                this.player02.isMovingUp = false;
                this.player02.isMovingDown = false;
                this.player02.powerUp = false;
                this.player02.powerDown = false;
                this.bullet.xCoord = 0;
                this.bullet.yCoord = 0;
            }
        }
        if (Bullet.player01Hitted) {
            if (Bullet.player02Hitted) {//checking if the other player is damaged by same bullet
                this.player02.health -= 10;//decreasing player's health
                Bullet.player02Hitted = false;//stop decreasing player's health
                //stop also player's movement, which is on turn, because he may shoot himself
                this.player01.isMovingLeft = false;
                this.player01.isMovingRight = false;
                this.player01.isMovingUp = false;
                this.player01.isMovingDown = false;
                this.player01.powerUp = false;
                this.player01.powerDown = false;
            }
            this.player01.health -= 10;//decreasing player's health
            Bullet.player01Hitted = false;//stop decreasing player's health
            //again creating explosion
            this.explosionX = this.player01.x - 20;
            this.explosionY = this.player01.y - 40;
            this.explosion = true;
            this.bulletCreated = false;
            this.turn = !this.turn;//changing player's turn
            //stop moving opponent player, because his turn is over
            this.player02.isMovingLeft = false;
            this.player02.isMovingRight = false;
            this.player02.isMovingUp = false;
            this.player02.isMovingDown = false;
            this.player02.powerUp = false;
            this.player02.powerDown = false;
        }

        if (Bullet.player02Hitted) {
            if (Bullet.player01Hitted) {//checking if the other player is damaged by same bullet
                this.player01.health -= 10;//decreasing player's health
                Bullet.player01Hitted = false;//stop decreasing player's health
                //stop also player's movement, which is on turn, because he may shoot himself
                this.player02.isMovingLeft = false;
                this.player02.isMovingRight = false;
                this.player02.isMovingUp = false;
                this.player02.isMovingDown = false;
                this.player02.powerUp = false;
                this.player02.powerDown = false;
            }
            this.player02.health -= 10;//decreasing player's health
            Bullet.player02Hitted = false;//stop decreasing player's health
            //again creating explosion
            this.explosionX = this.player02.x - 20;
            this.explosionY = this.player02.y - 40;
            this.explosion = true;
            this.bulletCreated = false;
            this.turn = !this.turn;//changing player's turn
            //stop moving opponent player, because his turn is over
            this.player01.isMovingLeft = false;
            this.player01.isMovingRight = false;
            this.player01.isMovingUp = false;
            this.player01.isMovingDown = false;
            this.player01.powerUp = false;
            this.player01.powerDown = false;
        }
    }

    private void render() {  //RENDER -> draws images

        this.buffStrat = this.display.getCanvas().getBufferStrategy();
        if (this.buffStrat == null) {
            this.display.getCanvas().createBufferStrategy(2);
            return;
        }

        this.graphics = this.buffStrat.getDrawGraphics();
        this.graphics.clearRect(0, 0, this.width, this.height);
        //START DRAWING

        //drawing lightblue background
        this.graphics.setColor(Color.decode("#87CEFA"));
        this.graphics.fillRect(0, 0, this.width, this.height);

        //drawingSun(); -> method, which calculates and draws sun by giver radius -> not a sprite sheet
        //drawing two clouds, which are static -> background
        this.graphics.drawImage(this.spriteSheetCloud05.crop(0, 0, 320, 208), 250, 180, null);
        this.graphics.drawImage(this.spriteSheetCloud06.crop(0, 100, 500, 400), 700, 0, null);

        //drawing random ground
        this.graphics.setColor(Color.GREEN);
        for (int i = 0; i < this.groundPoints.length; i++) {
            this.graphics.drawLine(i, this.height, i, this.groundPoints[i]);
        }

        //drawing player icons, player's power, heart, angle, light icon
        this.graphics.drawImage(this.spriteSheetChili01.crop(0, 0, 47, 80), (int) (0.05 * this.width), (int) (0.85 * this.height), null);
        this.graphics.drawImage(this.spriteSheetChili02.crop(0, 0, 41, 70), (int) (0.9 * this.width), (int) (0.85 * this.height), null);
        this.graphics.drawImage(this.spriteSheetPower.crop(0, 0, 80, 80), (int) (0.090 * this.width), (int) (0.80 * this.height), null);
        this.graphics.drawImage(this.spriteSheetPower.crop(0, 0, 80, 80), (int) (0.8 * this.width), (int) (0.80 * this.height), null);
        this.graphics.drawImage(this.spriteSheetHeart.crop(0, 0, 80, 80), (int) (0.090 * this.width), (int) (0.85 * this.height), null);
        this.graphics.drawImage(this.spriteSheetHeart.crop(0, 0, 80, 80), (int) (0.8 * this.width), (int) (0.85 * this.height), null);
        this.graphics.drawImage(this.spriteSheetAngle.crop(0, 0, 80, 80), (int) (0.0908 * this.width), (int) (0.907 * this.height), null);
        this.graphics.drawImage(this.spriteSheetAngle.crop(0, 0, 80, 80), (int) (0.805 * this.width), (int) (0.907 * this.height), null);
        this.graphics.drawImage(this.spriteSheetLightPic.crop(0, 0, 40, 40), (int) (0.18 * this.width), (int) (0.907 * this.height), null);
        this.graphics.drawImage(this.spriteSheetLightPic.crop(0, 0, 40, 40), (int) (0.73 * this.width), (int) (0.907 * this.height), null);
        //drawing sun picture
        this.graphics.drawImage(this.spriteSheetSun.crop(0, 0, 200, 200), 40, 40, null);

        this.graphics.setColor(Color.BLACK);
        this.graphics.setFont(new Font("Times New Roman", 10, 30));
        this.graphics.drawString("" + this.player01.health, (int) (0.13 * this.width), (int) (0.9 * this.height));
        this.graphics.drawString("" + this.player02.health, (int) (0.84 * this.width), (int) (0.9 * this.height));
        this.graphics.drawString("" + this.player01.strength, (int) (0.13 * this.width), (int) (0.85 * this.height));
        this.graphics.drawString("" + this.player02.strength, (int) (0.84 * this.width), (int) (0.85 * this.height));
        this.graphics.drawString("" + this.player01.i * 5, (int) (0.13 * this.width), (int) (0.95 * this.height));
        this.graphics.drawString("" + this.player02.i * 5, (int) (0.84 * this.width), (int) (0.95 * this.height));
        this.graphics.drawString("" + this.player01.shock, (int) (0.22 * this.width), (int) (0.95 * this.height));
        this.graphics.drawString("" + this.player02.shock, (int) (0.77 * this.width), (int) (0.95 * this.height));
        this.graphics.drawString("Air resistance " + this.airResistance + " m/s", (int) (0.4 * this.width), (int) (0.95 * this.height));

        //drawing Dark Cloud
        if (cloudCreated && this.turn) {
            this.graphics.drawImage(this.spriteSheetDarkCloud.crop(0, 0, 300, 143), cloudCounter01 - 160, player02.y - 420, null);
        } else if (cloudCreated && !this.turn) {
            this.graphics.drawImage(this.spriteSheetDarkCloud.crop(0, 0, 300, 143), cloudCounter02 - 130, player01.y - 420, null);
        }

        //drawing arrow above players to show which player will shoot
        if (this.turn) {
            this.graphics.drawImage(ImageLoader.load("/images/arrow.png"), this.player01.x - 30, (this.groundPoints[this.player01.x] - 50) - (int) arrowCounter, null);
        } else {
            this.graphics.drawImage(ImageLoader.load("/images/arrow.png"), this.player02.x, (this.groundPoints[this.player02.x] - 50) - (int) arrowCounter, null);
        }

        //drawing players
        this.player01.render(this.graphics);//here we call player render method-> he draws itself, counts his current condition with his tick() method
        this.player02.render(this.graphics);// -> player02

        //drawing light
        if (!this.turn && this.isLighting && lightingCounter % 2 == 0) {
            this.graphics.drawImage(this.spriteSheetLight.crop(0, 0, 267, 380), this.player01.x - 100, this.player01.y - 350, null);
        } else if (this.turn && this.isLighting && lightingCounter % 2 == 0) {
            this.graphics.drawImage(this.spriteSheetLight.crop(0, 0, 267, 380), this.player02.x - 100, this.player02.y - 350, null);
        }

        //drawing explosion if it's created
        if (explosion) {
            this.graphics.drawImage(this.spriteSheet.crop(iterator * 79, cols * 79, 79, 79), explosionX, explosionY, null);
        }

        //drawing bullet, if it has been created!!! -> to avoid exceptions
        if (bulletCreated) {
            this.bullet.render(graphics);
        }
        if (player01.health <= 0) {
            this.graphics.setColor(Color.red);
            this.graphics.setFont(new Font("Times New Roman", 30, 50));
            this.graphics.drawString("GAME OVER!", 440, 150);
            this.graphics.drawImage(this.spriteSheetChili02Over.crop(0, 0, 250, 250), 500, 150, null);
            this.graphics.drawString("WINS!", 540, 420);
        } else if (player02.health <= 0) {
            this.graphics.setColor(Color.red);
            this.graphics.setFont(new Font("Times New Roman", 30, 50));
            this.graphics.drawString("GAME OVER!", 440, 150);
            this.graphics.drawImage(this.spriteSheetChili01Over.crop(0, 0, 250, 250), 500, 150, null);
            this.graphics.drawString("WINS!", 540, 420);
        }
        //STOP DRAWING
        this.buffStrat.show();
        this.graphics.dispose();
    }

    //defining random coordinates of ground -> we call this method in init();
    private void defineGround() {
        Random r = new Random();
        double[] val1 = new double[4];
        double[] val2 = new double[4];

        for (int i = 0; i < 4; i++) {
            val1[i] = r.nextInt(100);
            val2[i] = (r.nextInt(10) - 20.0) / this.width;
        }
        for (int i = 0; i < this.groundPoints.length; i++) {
            this.groundPoints[i] = 7 * this.height / 10 + (int) (val2[0] + 0.5 * val1[1] * Math.sin(val1[0] -
                    val2[1] * i) + 0.5 * val1[0] * Math.sin(val1[1] - val2[2] * i) + 0.5 * val1[2] * Math.sin(val1[2] + val2[3] * i));
        }
    }

    //redefining ground coordinates
    private void redefineGround() {
        for (int x = 0; x < this.groundPoints.length; x++) {
            this.groundPoints[x] += 50 * Math.exp(-Math.pow((x - this.bullet.xCoord), 2) / 2000.0);
        }
    }

//    private void drawingSun() {
//        //drawing sun
//        rayLength = 3 * solarRadius;
//        rayLengthDiag = (int) (rayLength / Math.sqrt(2));
//        this.graphics.setColor(Color.YELLOW);
//        this.graphics.fillOval(solarX, solarY, 2 * solarRadius, 2 * solarRadius);
//
//        //drawing sun rays
//        this.graphics.drawLine((solarX + solarRadius), (solarY + solarRadius) - rayLength, (solarX + solarRadius), (solarY + solarRadius) + rayLength);
//        this.graphics.drawLine((solarX + solarRadius) - rayLength, (solarY + solarRadius), (solarX + solarRadius) + rayLength, (solarY + solarRadius));
//        this.graphics.drawLine((solarX + solarRadius) - rayLengthDiag, (solarY + solarRadius) - rayLengthDiag, (solarX + solarRadius) + rayLengthDiag, (solarY + solarRadius) + rayLengthDiag);
//        this.graphics.drawLine((solarX + solarRadius) + rayLengthDiag, (solarY + solarRadius) - rayLengthDiag, (solarX + solarRadius) - rayLengthDiag, (solarY + solarRadius) + rayLengthDiag);
//    }

    @Override
    public void run() { //  RUN -> here is while loop, here we call tick and render
        this.init();

        int fps = 50;//frame per second
        double ticksPerFrame = 1_000_000_000 / fps;//every sec we make 50 ticks!!!
        double delta = 0;
        long now;
        long lastTimeTicked = System.nanoTime();//get current time before while loop

        while (isRunning) {

            now = System.nanoTime();//get current time
            delta += (now - lastTimeTicked) / ticksPerFrame;
            lastTimeTicked = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
            if (player01.health <= 0 || player02.health <= 0) {
                this.stop();//stopping game if one of the players win -> make isRunning false and join threads -> IN while loop
            }
        }
        this.stop();//stopping game, if the window has been closed -> OUT while loop
    }

    public synchronized void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            try {
                this.isRunning = false;
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
