package main.java;

import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarCustomComponentFactory;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.LinkedList;

/**
 * Created by satooon on 2016/12/07.
 */
public class StatusBarAction extends StatusBarCustomComponentFactory {

    private Image image;
    private final int size = 20;
    private final int laneWidth = 300;
    private LinkedList<CustomIcon> customIconList = new LinkedList<CustomIcon>();
    private static Thread thread = null;
    private JPanel root;

    public StatusBarAction() {
        try (InputStream st = this.getClass().getResourceAsStream("icon.png")){
            this.image = ImageIO.read(st);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        int cnt = (laneWidth / size / 2) + 1;
        for (int i = 0; i < cnt; i++) {
            customIconList.add(new CustomIcon(i * size * 2 - size, laneWidth, image));
        }

        if (thread == null) {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        if (root != null) {
                            customIconList.forEach(CustomIcon::update);
                            root.repaint();
                        }
                        thread.sleep(32);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public JComponent createComponent(@NotNull StatusBar statusBar) {
        root = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics;
                g.setBackground(statusBar.getComponent().getBackground());
                g.clearRect(0, 0, getWidth(), getHeight());
                customIconList.forEach(customIcon -> customIcon.draw(g));
            }
        };
        root.setPreferredSize(new Dimension(laneWidth, statusBar.getComponent().getHeight()));
        return root;
    }
}
