package escapefromuniversity.inGame;

import escapefromuniversity.model.basics.Point2D;
import escapefromuniversity.model.gameObject.GameObjectType;
import escapefromuniversity.model.gameObject.State;

public interface ControllerView {

    void updateView();

    void remuveSpriteAnimation(int id);

    void updateSpriteAnimation(int id, Point2D position, State state);

    void addSpriteAnimation(int id, State state, GameObjectType type, int height, int width);

    boolean containThisID(int id);

}