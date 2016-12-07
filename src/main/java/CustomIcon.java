package main.java;

import java.awt.*;

/**
 * Created by satooon on 2016/12/07.
 */
public class CustomIcon {
    private final Image image;
    private int x;
    private int y = 0;
    private int size = 20;
    private int laneWidth;
    private int speed = 1;

    public CustomIcon(int x, int laneWidth, Image image) {
        this.x = x;
        this.image = image;
        this.laneWidth = laneWidth;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, size, size, null);
    }

    public void update() {
        x += speed;
        //端までいったら-sizeまで戻す
        if (laneWidth < x) {
            x = -size;
        }
    }
}
