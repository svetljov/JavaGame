//
//package game;
//
//
//import graphics.ImageLoader;
//import graphics.SpriteSheet;
//
//import java.awt.*;
//
//public class Player02 {
//
//    private String name;
//    public static int x, y;
//    private int width, height, velocity;
//    public static int[] ground;
//    private SpriteSheet spriteSheet;
//    public static int health;
//    public static int strength;
//    public  static int shock;
//    public static int i = 0;
//
//    public static boolean isMovingLeft02, isMovingRight02, isMovingUp02, isMovingDown02, powerUp, powerDown;
//
//    public Player02(String name, int x, int width, int height, int[] ground) {
//        this.name = name;
//        this.x = x;
//        this.width = width;
//        this.height = height;
//        this.y = ground[x];
//        this.ground = ground;
//
//        this.velocity = 3;
//        this.health = 100;
//        this.strength = 100;
//        this.shock = 2;
//        this.spriteSheet = new SpriteSheet(ImageLoader.load("/images/players4.png"));
//    }
//
//    public void tick() {//tick for every player -> don't use main tick method for updating player
//        if (isMovingLeft02) {
//            if (this.x >= 90 / 2) {
//                this.x -= this.velocity;
//            }
//        } else if (isMovingRight02) {
//            if (this.x <= 1200 - (95 / 2)) {
//                this.x += this.velocity;
//            }
//        }
//        this.y = this.ground[this.x];
//        if (isMovingUp02) {
//            i++;
//            if (i >= 19) i = 18;
//        } else if (isMovingDown02) {
//            i--;
//            if (i <= 0) i = 0;
//        }
//        if (powerUp && this.strength < 200) {
//            this.strength++;
//        } else if (powerDown && this.strength > 100) {
//            this.strength--;
//        }
//
//    }
//
//    public void render(Graphics graphics) {
//        graphics.drawImage(this.spriteSheet.crop(i * this.width, this.height, this.width, this.height), this.x - 47, this.y - 70, null);
//    }
//}
