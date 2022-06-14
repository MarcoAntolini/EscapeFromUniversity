package escapefromuniversity.model.bullet;

import escapefromuniversity.model.GameCollisionType;
import escapefromuniversity.model.Point2D;
import escapefromuniversity.model.Vector2D;
import escapefromuniversity.model.enemy.Enemy;
import escapefromuniversity.model.player.Player;
import escapefromuniversity.model.gameObject.AbstractDynamicGameObject;
import escapefromuniversity.model.gameObject.GameObject;
import escapefromuniversity.model.gameObject.GameObjectType;

public class BulletImpl extends AbstractDynamicGameObject implements Bullet{
	
	private final int damage;
	

	public BulletImpl(GameObjectType type, Point2D position, int speed, Vector2D direction, int damage) {
		super(type, position, position.sum(BulletConstant.BULLET_BOX_SIZE), speed, direction);
		this.damage = damage;
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
	public void update(double deltaTime) {
		this.move(deltaTime);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collisionWith(GameObject gObj2) {
		if(this.collisionWithCheck(gObj2)) {
			return;
		}
		switch(gObj2.getType().getCollisionType()) {
		case OBSTACLE:
			this.getRoom().deleteGameObject(this);
			break;
		case BULLET:
			if(this.getType().getCollisionType().equals(GameCollisionType.IMMUNE_BULLET)) {
				this.getRoom().deleteGameObject(gObj2);
			}
			break;
		case IMMUNE_BULLET:
			if(this.getType().getCollisionType().equals(GameCollisionType.BULLET)) {
				this.getRoom().deleteGameObject(this);
			}
			break;
		case ENTITY:
			if(gObj2.getType().equals(GameObjectType.PLAYER) && !this.getType().equals(GameObjectType.BULLET_PLAYER)) {
				final Player player = (Player) gObj2;
				player.takeDamage(this.getDamage());
				this.getRoom().deleteGameObject(this);
			}else if(!gObj2.getType().equals(GameObjectType.PLAYER) && 
					 this.getType().equals(GameObjectType.BULLET_PLAYER)) {
				final Enemy enemy = (Enemy) gObj2;
				enemy.takeDamage(this.getDamage());
				this.getRoom().deleteGameObject(this);
			}
			break;
		case IMMUNE_ENTITY:
			if(gObj2.getType().equals(GameObjectType.PLAYER) && !this.getType().equals(GameObjectType.BULLET_PLAYER)) {
				this.getRoom().deleteGameObject(this);
			}else if(!gObj2.getType().equals(GameObjectType.PLAYER) && 
					 this.getType().equals(GameObjectType.BULLET_PLAYER)) {
				this.getRoom().deleteGameObject(this);
			}
		default:
			break;
		}
		
	}

}