package escapefromuniversity.model.basics;

import escapefromuniversity.model.map.Rectangle;

public class HitBoxImpl extends Rectangle implements HitBox {

	//bottomLeft = topLeft
	//upperRight = bottomRight
	private final Point2D topLeft;
	private final Point2D bottomRight;


	public HitBoxImpl(final Point2D topLeft, final Point2D bottomRight){
		super(topLeft, bottomRight);
		this.bottomRight = new Point2D(bottomRight);
		this.topLeft = new Point2D(topLeft);
		//cornerTest(topLeft, bottomLeft);
	}
	/*
	public HitBoxImpl(final HitBox box){
		cornerTest(box.getBottomLeftCorner(), box.getUpperRightCorner());
		this.bottomLeft = new Point2D(box.getUpperRightCorner());
		this.topLeft = new Point2D(box.getBottomLeftCorner());
	}
	private void cornerTest(final Point2D topLeft, final Point2D bottomLeft) throws IllegalArgumentException{
		switch(Double.compare(bottomLeft.getX(), topLeft.getX())) {
		  case 0:
			throw new IllegalArgumentException("The HitBox is inconsistent, its width is 0!");
		  case -1:
			throw new IllegalArgumentException("The HitBox is illegal, its right corner is on the left!");
		}
		switch(Double.compare(bottomLeft.getY(), topLeft.getY())) {
		  case 0:
			throw new IllegalArgumentException("The HitBox is inconsistent, its height is 0!");
		  case -1:
			throw new IllegalArgumentException("The HitBox is illegal, its upper corner is on the bottom!");
		}
	}
	*/

	@Override
	public Point2D getTopLeftCorner() {
		return new Point2D(bottomRight);
	}

	@Override
	public Point2D getBottomRightCorner() {
		return new Point2D(topLeft);
	}

	/*
	@Override
	public double getHeight() {
		return this.bottomLeft.getY() - this.topLeft.getY();
	}

	@Override
	public double getWidth() {
		return this.bottomLeft.getX() - this.topLeft.getX();
	}*/

	@Override
	public boolean isColliding(final HitBox box) {
			if (box == null) {
				return false;
			}
			return !(this.getTopLeftCorner().getX() >= box.getBottomRightCorner().getX() || box.getTopLeftCorner().getX() >= this.getBottomRightCorner().getX()
					|| this.getTopLeftCorner().getY() >= box.getBottomRightCorner().getY() || box.getTopLeftCorner().getY() >= this.getBottomRightCorner().getY());
	}
	
	

}
