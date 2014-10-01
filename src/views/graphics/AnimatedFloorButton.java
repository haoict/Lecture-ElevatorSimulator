/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import models.FloorButton;
import static views.graphics.AnimatedPerson.arm_width;
import static views.graphics.AnimatedPerson.body_height;
import static views.graphics.AnimatedPerson.head_height;
import static views.graphics.AnimatedPerson.head_width;

/**
 *
 * @author Hao
 */
public class AnimatedFloorButton extends AnimatedObject {
    private FloorButton fbutton;
    boolean isLight;
    public AnimatedFloorButton(int x, int y, FloorButton fb) {
        super(x, y);
        this.fbutton = fb;
    }

    @Override
    public void updateState() {
        
    }

    @Override
    public void drawYourself(Graphics g) {
        this.isLight = fbutton.isIlluminated();
        if (this.isLight)
            g.setColor(Color.YELLOW);
        else
            g.setColor(Color.GRAY);
        
        if (fbutton.isDirection())
        {
            int[] xs = new int[]{x + (head_width / 2), x + (head_width / 2) - arm_width, x + (head_width / 2) + arm_width};
            int[] ys = new int[]{y + head_height + body_height + 12, y + head_height + body_height + 2, y + head_height + body_height + 2};
            Polygon p = new Polygon(xs, ys, 3);
            g.fillPolygon(p);
        }
        else
        {
            int[] xs = new int[]{x + (head_width / 2), x + (head_width / 2) - arm_width, x + (head_width / 2) + arm_width};
            int[] ys = new int[]{y + head_height, y + head_height + body_height + 2, y + head_height + body_height + 2};
            Polygon p = new Polygon(xs, ys, 3);
            g.fillPolygon(p);
        }

    }
    
}
