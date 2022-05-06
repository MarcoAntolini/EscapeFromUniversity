package escapefromuniversity.model;

public abstract class AbstractDynamicGameObject implements DynamicGameObject {

	private int id;
	private final GameObjectType type;
	private final int speed;
	private Vector2D direction;
	private Point2D position;

	public AbstractDynamicGameObject(final GameObjectType type, final Point2D position, final int speed, final Vector2D direction) {
		this.type = type;
		this.position = position;
		this.speed = speed;
		this.direction = direction;
	}
	
	public Point2D getObjectPosition() {
		return new Point2D(position);
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public void setID(final int id) {
		this.id = id;
		
	}

	@Override
	public GameObjectType getType() {
		return this.type;
	}

	@Override
	public int getSpeed() {
		return this.speed;
	}

	@Override
	public Vector2D getDirection() {
		return new Vector2D(this.direction);
	}

	@Override
	public void setDirection(final Vector2D newDirection) {
		this.direction = newDirection;	
	}
	
	@Override
	public void  setPosition(final Point2D newPosition) {
		this.position = newPosition;
	}
	
	public abstract void update(double deltaTime);
	
	public void move(final double deltaTime) {
		this.setPosition(this.getObjectPosition().sum(getDirection().multiplication(this.speed).multiplication(deltaTime)));
	}

}