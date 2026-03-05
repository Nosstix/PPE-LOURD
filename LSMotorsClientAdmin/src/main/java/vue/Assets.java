package vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Assets {

    private static BufferedImage load(String name) {
        try (InputStream in = Assets.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) return null;
            return ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage logoImage() {
        return load("logo.png");
    }

    /** Logo horizontal (si présent), sinon fallback sur logo.png */
    public static BufferedImage logoWideImage() {
        BufferedImage wide = load("logo_wide.png");
        return wide != null ? wide : logoImage();
    }

    public static Image logoWindowIcon() {
        BufferedImage img = logoImage();
        if (img == null) return null;
        return img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }

    public static ImageIcon scaledKeepRatio(BufferedImage src, int targetW, int targetH) {
        if (src == null) return null;

        int sw = src.getWidth();
        int sh = src.getHeight();
        if (sw <= 0 || sh <= 0) return null;

        double r = Math.min((double) targetW / sw, (double) targetH / sh);
        int nw = Math.max(1, (int) Math.round(sw * r));
        int nh = Math.max(1, (int) Math.round(sh * r));

        Image scaled = src.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);

        BufferedImage canvas = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect(0, 0, targetW, targetH);

        int x = (targetW - nw) / 2;
        int y = (targetH - nh) / 2;
        g2.drawImage(scaled, x, y, null);
        g2.dispose();

        return new ImageIcon(canvas);
    }

    public static ImageIcon scaledLogoKeepRatio(int targetW, int targetH) {
        return scaledKeepRatio(logoImage(), targetW, targetH);
    }

    public static ImageIcon scaledWideLogoKeepRatio(int targetW, int targetH) {
        return scaledKeepRatio(logoWideImage(), targetW, targetH);
    }
}
