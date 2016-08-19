
package game;

import java.awt.*;

public class Bullet {
    private int x;
    private int angle;
    private int strength;
    int[] ground;
    private int[] coordinates;
    int time = 0;
    public static int xCoord;
    public static int yCoord;
    private double DELAY_FACTOR = 10.0;
    private int vTerminal = (200 - Game.airResistance) * -1;//airResistance is random defined in the init() mathod in Game class -> it's different for each game
    private int IMPACT_RADIUS = 50;                                 //it's multiplyed by -1, because must be negative value
    public static boolean player01Hitted;
    public static boolean player02Hitted;

    public Bullet(int x, int angle, int strength, int[] ground) {
        this.x = x;
        this.angle = angle;
        this.strength = strength;
        this.ground = ground;
    }

    public void tick() {

        int x0, y0;
        double vX, vY;
        if (Game.turn) {//calculating bullet trajectory for player turn
            x0 = this.x - 20 + (int) (90 * Math.cos((this.angle) * Math.PI / 180));
            y0 = this.ground[this.x] - 15 - (int) (90 * Math.sin((this.angle) * Math.PI / 180));

            vX = (this.strength / 1.5) * Math.cos((this.angle) * Math.PI / 180);
            vY = (-this.strength / 1.5) * Math.sin((this.angle) * Math.PI / 180);
        } else {//calculating bullet trajectory for player02 turn
            x0 = this.x - 20 + (int) (90 * Math.cos((270 - this.angle) * Math.PI / 180));
            y0 = this.ground[this.x] - 15 - (int) (90 * Math.sin((270 - this.angle) * Math.PI / 180));

            vX = (this.strength / 1.5) * Math.cos((270 - this.angle) * Math.PI / 180);
            vY = (-this.strength / 1.5) * Math.sin((270 - this.angle) * Math.PI / 180);
        }


        double gAcc = -9.80665; // Earth's gravity acceleration
        //bullet coordinates
        xCoord = (int) (x0 + (vX * vTerminal / gAcc) * (1 - Math.exp((-gAcc * this.time / DELAY_FACTOR) / vTerminal)));
        yCoord = (int) (y0 + (vTerminal / gAcc) * (vY + vTerminal) * (1 - Math.exp((-gAcc * this.time / DELAY_FACTOR) / vTerminal)) - vTerminal * this.time / DELAY_FACTOR);

        int[] currentVelocity = new int[]{
                (int) ((vX / DELAY_FACTOR) * Math.exp(-gAcc * time / (DELAY_FACTOR * vTerminal))),
                (int) (-vTerminal / DELAY_FACTOR + ((vY + vTerminal) / DELAY_FACTOR) * Math.exp(-gAcc * time / (DELAY_FACTOR * vTerminal)))};

        int[] normalVectorPlayer1 = new int[]{xCoord - Game.player01.x, yCoord - ground[Game.player01.x]};
        int[] normalVectorPlayer2 = new int[]{xCoord - Game.player02.x, yCoord - ground[Game.player02.x]};


        // Check whether the ball is within the Impact radius of a player and if the velocity vector points toward the bounding circle of the same player
        player01Hitted = (Math.pow((Game.player01.x - xCoord), 2) + Math.pow((ground[Game.player01.x] - yCoord), 2) <= Math.pow(IMPACT_RADIUS, 2)) &&
                (normalVectorPlayer1[0] * currentVelocity[0] + normalVectorPlayer1[1] * currentVelocity[1] < 0);
        player02Hitted = (Math.pow((Game.player02.x - xCoord), 2) + Math.pow((ground[Game.player02.x] - yCoord), 2) <= Math.pow(IMPACT_RADIUS, 2)) &&
                (normalVectorPlayer2[0] * currentVelocity[0] + normalVectorPlayer2[1] * currentVelocity[1] < 0);

        //checking if bullet is out of the frame(window), delete it, change players turn and stop moving of current player
        if (xCoord < 0 || xCoord > Game.groundPoints.length - 1 || yCoord < 0) {
            Game.turn = !Game.turn;
            Game.bulletCreated = false;

            Game.player01.isMovingLeft = false;
            Game.player01.isMovingRight = false;
            Game.player01.isMovingUp = false;
            Game.player01.isMovingDown = false;

            Game.player02.isMovingLeft = false;
            Game.player02.isMovingRight = false;
            Game.player02.isMovingUp = false;
            Game.player02.isMovingDown = false;
        }

        this.coordinates = new int[]{xCoord, yCoord};
        this.time++;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillOval(coordinates[0], coordinates[1], 10, 10);
    }
}
