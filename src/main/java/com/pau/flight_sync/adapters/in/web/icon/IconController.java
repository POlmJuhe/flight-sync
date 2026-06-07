package com.pau.flight_sync.adapters.in.web.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;

@RestController
public class IconController {

    private static final byte[] ICON_PNG = buildIcon(180);

    @GetMapping(value = {"/apple-touch-icon.png", "/apple-touch-icon-precomposed.png"}, produces = "image/png")
    public ResponseEntity<byte[]> appleIcon() {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                .body(ICON_PNG);
    }

    private static byte[] buildIcon(int size) {
        try {
            val img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            val g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Navy background with rounded corners
            val navy = new Color(0x1a3560);
            val amber = new Color(0xf0c060);
            double r = size * 0.18;

            val bg = new java.awt.geom.RoundRectangle2D.Double(0, 0, size, size, r * 2, r * 2);
            g.setColor(navy);
            g.fill(bg);

            double cx = size / 2.0, cy = size / 2.0;
            double outerR = size * 0.38, innerR = outerR * 0.88;

            // Outer ring
            g.setColor(new Color(0xf0c060));
            g.setStroke(new BasicStroke((float)(size * 0.012f)));
            val ring = new Ellipse2D.Double(cx - outerR, cy - outerR, outerR * 2, outerR * 2);
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.45f));
            g.draw(ring);
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));

            // Cardinal ticks
            g.setStroke(new BasicStroke((float)(size * 0.022f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(amber);
            double tick1 = outerR - size * 0.04, tick2 = outerR + size * 0.04;
            drawTick(g, cx, cy, 0,   tick1, tick2);
            drawTick(g, cx, cy, 90,  tick1, tick2);
            drawTick(g, cx, cy, 180, tick1, tick2);
            drawTick(g, cx, cy, 270, tick1, tick2);

            // Intercardinal ticks (dimmer)
            g.setStroke(new BasicStroke((float)(size * 0.014f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.45f));
            double tick3 = outerR - size * 0.025;
            for (int angle : new int[]{45, 135, 225, 315}) drawTick(g, cx, cy, angle, tick3, tick2);
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));

            // Needle (rotated 45°) – north gold, south white dim
            double needleLen = innerR * 0.88, halfW = size * 0.042;
            g.rotate(Math.toRadians(45), cx, cy);

            // North (gold)
            val north = new Path2D.Double();
            north.moveTo(cx, cy - needleLen);
            north.lineTo(cx - halfW, cy);
            north.lineTo(cx, cy - size * 0.055);
            north.lineTo(cx + halfW, cy);
            north.closePath();
            g.setColor(amber);
            g.fill(north);

            // South (white dim)
            val south = new Path2D.Double();
            south.moveTo(cx, cy + needleLen);
            south.lineTo(cx - halfW, cy);
            south.lineTo(cx, cy + size * 0.055);
            south.lineTo(cx + halfW, cy);
            south.closePath();
            g.setColor(new Color(255, 255, 255, 140));
            g.fill(south);

            g.rotate(-Math.toRadians(45), cx, cy);

            // Centre pivot
            double pivotR = size * 0.062;
            g.setColor(navy);
            g.fill(new Ellipse2D.Double(cx - pivotR, cy - pivotR, pivotR * 2, pivotR * 2));
            double dotR = size * 0.038;
            g.setColor(amber);
            g.fill(new Ellipse2D.Double(cx - dotR, cy - dotR, dotR * 2, dotR * 2));

            g.dispose();

            val out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("icon generation failed", e);
        }
    }

    private static void drawTick(Graphics2D g, double cx, double cy, double angleDeg, double r1, double r2) {
        double rad = Math.toRadians(angleDeg - 90);
        g.draw(new Line2D.Double(
                cx + Math.cos(rad) * r1, cy + Math.sin(rad) * r1,
                cx + Math.cos(rad) * r2, cy + Math.sin(rad) * r2));
    }
}
