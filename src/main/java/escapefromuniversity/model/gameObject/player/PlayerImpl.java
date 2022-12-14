package escapefromuniversity.model.gameObject.player;

import escapefromuniversity.model.basics.GameCollisionType;
import escapefromuniversity.model.basics.Point2D;
import escapefromuniversity.model.basics.Vector2D;
import escapefromuniversity.model.gameObject.bullet.Bullet;
import escapefromuniversity.model.gameObject.bullet.BulletFactory;
import escapefromuniversity.model.gameObject.bullet.BulletFactoryImpl;
import escapefromuniversity.model.gameObject.AbstractDynamicGameObject;
import escapefromuniversity.model.gameObject.Direction;
import escapefromuniversity.model.gameObject.GameObject;
import escapefromuniversity.model.gameObject.GameObjectType;
import escapefromuniversity.model.GameInit;

/**
 * The implementation of the {@link Player} interface. It also extends
 * {@link AbstractDynamicGameObject}
 */
public class PlayerImpl extends AbstractDynamicGameObject implements Player {

    private static final int MAX_LIFE = 100;
    private static final int MAX_DAMAGE = 30;
    private static final int MAX_ARMOR = 20;
    private static final int START_CREDITS = 0;
    private static final int START_DAMAGE = 10;
    private static final int START_ARMOR = 0;
    private static final Direction START_DIRECTION = Direction.DOWN;
    private static final Point2D HIT_BOX_PLAYER = new Point2D(1, 1.4);
    private int life;
    private int credits;
    private int damage;
    private int armor;
    private int passed;
    private int finalMark;
    private final double shootDelay;
    private double lastShot;
    private boolean shooting;
    private boolean bonusQuiz;
    public Point2D prevPosition;
    private Direction direction;
    private final BulletFactory bulletFactory;

    /**
     * Instantiates the player.
     * 
     * @param type       the game object type: a player.
     * @param position   the position of the player.
     * @param speed      the movement speed of the player.
     * @param direction  the direction of the player.
     * @param shootDelay the shoot delay of the player.
     */
    public PlayerImpl(final GameObjectType type, final Point2D position, final double speed, final Vector2D direction,
            final double shootDelay, final GameInit map) {
        super(type, position, HIT_BOX_PLAYER, speed, direction, map);
        this.life = MAX_LIFE;
        this.credits = START_CREDITS;
        this.damage = START_DAMAGE;
        this.armor = START_ARMOR;
        this.direction = START_DIRECTION;
        this.passed = 0;
        this.shootDelay = shootDelay;
        this.shooting = false;
        this.bonusQuiz = false;
        this.lastShot = System.currentTimeMillis();
        this.bulletFactory = new BulletFactoryImpl();
        this.prevPosition = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collisionWith(final GameObject gObj2) {
        if (this.collisionWithCheck(gObj2) && gObj2.getType().getCollisionType() == GameCollisionType.OBSTACLE) {
            if (gObj2.getType() == GameObjectType.SHOP) {
                this.map.getMapManager().setupShop();
            }
            this.setPosition(this.prevPosition);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        if (this.shooting) {
            this.shoot();
        }
        this.prevPosition = this.getObjectPosition();
        this.move(deltaTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxLife() {
        return MAX_LIFE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxDamage() {
        return MAX_DAMAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxArmor() {
        return MAX_ARMOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLife() {
        return this.life;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCredits() {
        return this.credits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamage() {
        return this.damage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getArmor() {
        return this.armor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFinalMark() {
        return this.finalMark;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLife(final int life) {
        this.life = life;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increaseCredits(final int credits) {
        this.credits += credits;
    }

    @Override
    public void decreaseCredits(final int credits) {
        this.credits -= credits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamage(final int damage) {
        this.damage = damage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArmor(final int armor) {
        this.armor = armor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFinalMark(final int newMark) {
        this.finalMark = (this.finalMark * this.passed + newMark);
        this.passedExam();
        this.finalMark = this.finalMark / this.passed;
        this.finalMark = this.finalMark * 110 / 30;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetLife() {
        this.setLife(this.getMaxLife());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDamageBuff(final int damage) {
        this.setDamage(this.getDamage() + damage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArmorBuff(final int armor) {
        this.setArmor(this.getArmor() + armor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBonusQuiz(final boolean state) {
        this.bonusQuiz = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasBonusQuiz() {
        return this.bonusQuiz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMap(final GameInit map) {
        this.map = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeDamage(final int damage) {
        this.setLife(this.getLife() + this.getArmor() - damage);
        if (this.life <= 0) {
            this.getMap().getMapManager().lost();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canShoot(final double deltaTime) {
        if (deltaTime + this.lastShot >= this.shootDelay) {
            this.lastShot = 0;
            return true;
        }
        this.lastShot = this.lastShot + deltaTime;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShoot(final boolean shooting, final Direction direction) {
        if (this.canShoot(0.2)) {
            this.shooting = shooting;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot() {
        final Bullet bullet = bulletFactory.createPlayerBullet(this.getObjectPosition(), this.getDirection(),
                this.damage, this.map);
        this.getMap().addDynamicGameObject(bullet);
        this.shooting = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastDirection(final Direction direction) {
        this.direction = direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Direction getLastDirection() {
        return this.direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void passedExam() {
        this.passed++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDead() {
        return this.life <= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGraduated() {
        return this.passed >= 6;
    }

}
