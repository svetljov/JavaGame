package game;

import graphics.ImageLoader;
import graphics.SpriteSheet;

import java.awt.*;

public class Player {

    private String name;
    public int x, y;
    private int width, height, crop, velocity;
    public int[] ground;
    private SpriteSheet spriteSheet;
    public int health;
    public int strength;
    public int shock;
    public int i = 0;

    public boolean isMovingLeft, isMovingRight, isMovingUp, isMovingDown, powerUp, powerDown;

    public Player(String name, int x, int width, int height, int crop, int[] ground) {
        this.name = name;
        this.x = x;
        this.width = width;
        this.height = height;
        this.crop = crop;
        this.y = ground[x];
        this.ground = ground;

        this.velocity = 3;
        this.health = 100;
        this.strength = 100;
        this.shock = 2;
        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/players4.png"));
    }

    public void tick() {//tick for every player -> don't use main tick method for updating player
        if (isMovingLeft) {
            if (this.x >= 90 / 2) {
                this.x -= this.velocity;
            }
        } else if (isMovingRight) {
            if (this.x <= 1200 - (95 / 2)) {
                this.x += this.velocity;
            }
        }
        this.y = this.ground[this.x];
        if (isMovingUp) {
            i++;
            if (i >= 19) i = 18;
        } else if (isMovingDown) {
            i--;
            if (i <= 0) i = 0;
        }
        if (this.powerUp && this.strength < 200) {
            this.strength++;
        } else if (this.powerDown && this.strength > 100) {
            this.strength--;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(this.spriteSheet.crop(i * this.width, this.crop, this.width, this.height), this.x - 47, this.y - 70, null);
    }
}

