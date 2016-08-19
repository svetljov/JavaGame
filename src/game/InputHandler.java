package game;

import display.Display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public InputHandler(Display display) {
        display.getCanvas().addKeyListener(this);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (Game.turn && !Game.cloudCreated) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Game.player01.isMovingLeft = true;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Game.player01.isMovingRight = true;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Game.player01.isMovingUp = true;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Game.player01.isMovingDown = true;
            }
            if (keyCode == KeyEvent.VK_SPACE && !Game.bulletCreated) {
                Game.fire = true;
            }
            if (keyCode == KeyEvent.VK_ENTER && Game.player01.shock > 0) {
                Game.cloudCreated = true;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Game.player01.powerUp = true;
            } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
                Game.player01.powerDown = true;
            }
        } else if (!Game.turn && !Game.cloudCreated) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Game.player02.isMovingLeft = true;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Game.player02.isMovingRight = true;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Game.player02.isMovingUp = true;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Game.player02.isMovingDown = true;
            }
            if (keyCode == KeyEvent.VK_SPACE && !Game.bulletCreated) {
                Game.fire = true;
            }
            if (keyCode == KeyEvent.VK_ENTER && Game.player02.shock > 0) {
                Game.cloudCreated = true;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Game.player02.powerUp = true;
            } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
                Game.player02.powerDown = true;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (Game.turn) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Game.player01.isMovingLeft = false;

            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Game.player01.isMovingRight = false;

            }
            if (keyCode == KeyEvent.VK_UP) {
                Game.player01.isMovingUp = false;

            } else if (keyCode == KeyEvent.VK_DOWN) {
                Game.player01.isMovingDown = false;

            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = false;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Game.player01.powerUp = false;

            } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
                Game.player01.powerDown = false;

            }
        } else {
            if (keyCode == KeyEvent.VK_LEFT) {
                Game.player02.isMovingLeft = false;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Game.player02.isMovingRight = false;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Game.player02.isMovingUp = false;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Game.player02.isMovingDown = false;
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = false;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Game.player02.powerUp = false;
            } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
                Game.player02.powerDown = false;
            }
        }
    }
}
