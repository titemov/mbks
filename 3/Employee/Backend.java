import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Random;

public class Backend extends Interface {
    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

}