package TP2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.time.DateTimeException;
import java.util.Date;

public final class Hero extends DynamicThings{

    private static volatile Hero instance = null;
    private Orientation orientation=Orientation.RIGHT;
    private boolean isWalking = false;
    private boolean invincible = false;
    private long invincibilityStartTime;
    private long invincibilityDuration = 1000; // Durée de l'invincibilité en millisecondes
    private int health = 100; // Points de vie du héros
    private Hero() {
        super(120,120, 48,52);
        try{this.setImage(ImageIO.read(new File("img/heroTileSheet.png")));}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public final static Hero getInstance() {
        if (Hero.instance == null) {
            synchronized(Hero.class) {
                if (Hero.instance == null) {
                    Hero.instance = new Hero();
                }
            }
        }
        return Hero.instance;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void moveIfPossible(double dx, double dy, Dungeon dungeon) {
        boolean movePossible = true;
        this.getHitBox().move(dx, dy); // Met à jour la position de la HitBox avec le déplacement

        for (Things things : dungeon.getRenderList()) {
            if (things instanceof SolidThings) {
                if (((SolidThings) things).getHitBox().intersect(this.getHitBox())) {
                	if(things instanceof Trap) {
                		this.takeDamage(10);
                	}
                    movePossible = false;
                    break;
                }
            }
        }

        if (movePossible) {
            this.x = x + dx;
            this.y = y + dy;
        } else {
            this.getHitBox().move(-dx, -dy); // Annule le déplacement de la HitBox si le mouvement n'est pas possible
        }
    }



    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void takeDamage(int damage) {
        if (!invincible) {
            // Mettre le héros en mode invincible
            invincible = true;
            invincibilityStartTime = System.currentTimeMillis();

            // Appliquer les dégâts à la santé
            health -= damage;

            // Vérifier si la santé est épuisée
            if (health <= 0) {
                // Le héros est vaincu, vous pouvez ajouter du code ici pour gérer la défaite.
                System.out.println("Game Over");
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        int attitude = orientation.getI();
        int index = (int) ((System.currentTimeMillis() / 125) % 10);
        index = isWalking ? index : 0;

        // Dessiner le héros s'il n'est pas invincible ou s'il doit clignoter
        if (!invincible || (System.currentTimeMillis() / 200) % 2 == 0) {
            g.drawImage(image, (int) x, (int) y, (int) x + 48, (int) y + 52, index * 96, 100 * attitude, (index + 1) * 96, 100 * (attitude + 1), null, null);
        }

        // Dessiner la barre de vie
        g.setColor(Color.RED);
        int barWidth = 50;  // Largeur de la barre de vie
        int barHeight = 10;  // Hauteur de la barre de vie
        g.fillRect((int) x, (int) y - barHeight, barWidth, barHeight);

        // Dessiner la partie de la barre de vie en vert pour représenter la vie restante
        g.setColor(Color.GREEN);
        int remainingBarWidth = (int) ((double) health / 100 * barWidth);
        g.fillRect((int) x, (int) y - barHeight, remainingBarWidth, barHeight);

        // Dessiner l'invincibilité
        if (invincible) {
            g.setColor(Color.YELLOW);
            g.drawString("Je suis en train de mourir!", (int) x, (int) y - barHeight - 10);
        }

        // Vérifier si l'invincibilité a expiré
        if (invincible && System.currentTimeMillis() > invincibilityStartTime + invincibilityDuration) {
            invincible = false;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

}