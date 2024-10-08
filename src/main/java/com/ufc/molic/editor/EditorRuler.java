/*
 * Copyright (c) 2001-2005, Gaudenz Alder
 *
 * All rights reserved.
 *
 * See LICENSE file for license details. If you are unable to locate
 * this file please contact info (at) jgraph (dot) com.
 */
package com.ufc.molic.editor;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serial;
import java.text.NumberFormat;
import java.util.TooManyListenersException;

public class EditorRuler extends JComponent implements MouseMotionListener, DropTargetListener {

    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    @Serial
    private static final long serialVersionUID = -6310912355878668096L;

    public static int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;

    protected static int INCH = 72;

    protected static int DEFAULT_PAGESCALE = 1;

    protected static boolean DEFAULT_ISMETRIC = true;

    static {
        numberFormat.setMaximumFractionDigits(2);
    }

    protected Color inactiveBackground = new Color(170, 170, 170);

    protected int orientation;

    protected int activeoffset, activelength;

    protected double scale = DEFAULT_PAGESCALE;

    protected boolean metric = DEFAULT_ISMETRIC;

    protected Font labelFont = new Font("Tahoma", Font.PLAIN, 9);

    protected int rulerSize = 16;

    protected int tickDistance = 30;

    protected mxGraphComponent graphComponent;

    protected Point mouse = new Point();

    protected double increment, units;

    protected transient mxIEventListener repaintHandler = (source, evt) -> repaint();

    public EditorRuler(mxGraphComponent graphComponent, int orientation) {
        this.orientation = orientation;
        this.graphComponent = graphComponent;
        updateIncrementAndUnits();

        graphComponent.getGraph().getView().addListener(mxEvent.SCALE, repaintHandler);
        graphComponent.getGraph().getView().addListener(mxEvent.TRANSLATE, repaintHandler);
        graphComponent.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, repaintHandler);

        graphComponent.getGraphControl().addMouseMotionListener(this);

        DropTarget dropTarget = graphComponent.getDropTarget();

        try {
            if (dropTarget != null) {
                dropTarget.addDropTargetListener(this);
            }
        } catch (TooManyListenersException ignored) {

        }

        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setActiveOffset(int offset) {
        activeoffset = (int) (offset * scale);
    }

    public void setActiveLength(int length) {
        activelength = (int) (length * scale);
    }

    public boolean isMetric() {
        return metric;
    }

    public void setMetric(boolean isMetric) {
        this.metric = isMetric;
        updateIncrementAndUnits();
        repaint();
    }

    public int getRulerSize() {
        return rulerSize;
    }

    public void setRulerSize(int rulerSize) {
        this.rulerSize = rulerSize;
    }

    public int getTickDistance() {
        return tickDistance;
    }

    public void setTickDistance(int tickDistance) {
        this.tickDistance = tickDistance;
    }

    public Dimension getPreferredSize() {
        Dimension dim = graphComponent.getGraphControl().getPreferredSize();

        if (orientation == ORIENTATION_VERTICAL) {
            dim.width = rulerSize;
        } else {
            dim.height = rulerSize;
        }

        return dim;
    }

    public void dragEnter(DropTargetDragEvent arg0) {}

    public void dragExit(DropTargetEvent arg0) {}

    public void dragOver(final DropTargetDragEvent arg0) {
        updateMousePosition(arg0.getLocation());
    }

    public void drop(DropTargetDropEvent arg0) {}

    public void dropActionChanged(DropTargetDragEvent arg0) {}

    public void mouseMoved(MouseEvent e) {
        updateMousePosition(e.getPoint());
    }

    public void mouseDragged(MouseEvent e) {
        updateMousePosition(e.getPoint());
    }

    protected void updateMousePosition(Point pt) {
        Point old = mouse;
        mouse = pt;
        repaint(old.x, old.y);
        repaint(mouse.x, mouse.y);
    }

    protected void updateIncrementAndUnits() {
        double graphScale = graphComponent.getGraph().getView().getScale();

        if (metric) {
            units = INCH / 2.54; // 2.54 dots per centimeter
            units *= graphComponent.getPageScale() * graphScale;
            increment = units;
        } else {
            units = INCH;
            units *= graphComponent.getPageScale() * graphScale;
            increment = units / 2;
        }
    }

    public void repaint(int x, int y) {
        if (orientation == ORIENTATION_VERTICAL) {
            repaint(0, y, rulerSize, 1);
        } else {
            repaint(x, 0, 1, rulerSize);
        }
    }

    public void paintComponent(Graphics g) {
        mxGraph graph = graphComponent.getGraph();
        Rectangle clip = g.getClipBounds();
        updateIncrementAndUnits();

        if (activelength > 0 && inactiveBackground != null) {
            g.setColor(inactiveBackground);
        } else {
            g.setColor(getBackground());
        }

        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        g.setColor(getBackground());
        Point2D p = new Point2D.Double(activeoffset, activelength);

        if (orientation == ORIENTATION_HORIZONTAL) {
            g.fillRect((int) p.getX(), clip.y, (int) p.getY(), clip.height);
        } else {
            g.fillRect(clip.x, (int) p.getX(), clip.width, (int) p.getY());
        }

        double left = clip.getX();
        double top = clip.getY();
        double right = left + clip.getWidth();
        double bottom = top + clip.getHeight();

        mxPoint trans = graph.getView().getTranslate();
        double scale = graph.getView().getScale();
        double tx = trans.getX() * scale;
        double ty = trans.getY() * scale;

        double stepping = increment;

        if (stepping < tickDistance) {
            int count = (int) Math.round(Math.ceil(tickDistance / stepping) / 2) * 2;
            stepping = count * stepping;
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(labelFont);
        g.setColor(Color.black);

        int smallTick = rulerSize - rulerSize / 3;
        int middleTick = rulerSize / 2;

        if (orientation == ORIENTATION_HORIZONTAL) {
            double xs = Math.floor((left - tx) / stepping) * stepping + tx;
            double xe = Math.ceil(right / stepping) * stepping;
            xe += (int) Math.ceil(stepping);

            for (double x = xs; x <= xe; x += stepping) {
                double xx = Math.round((x - tx) / stepping) * stepping + tx;

                int ix = (int) Math.round(xx);
                g.drawLine(ix, rulerSize, ix, 0);

                String text = format((x - tx) / increment);
                g.drawString(text, ix + 2, labelFont.getSize());

                ix += (int) Math.round(stepping / 4);
                g.drawLine(ix, rulerSize, ix, smallTick);

                ix += (int) Math.round(stepping / 4);
                g.drawLine(ix, rulerSize, ix, middleTick);

                ix += (int) Math.round(stepping / 4);
                g.drawLine(ix, rulerSize, ix, smallTick);
            }
        } else {
            double ys = Math.floor((top - ty) / stepping) * stepping + ty;
            double ye = Math.ceil(bottom / stepping) * stepping;
            ye += (int) Math.ceil(stepping);

            for (double y = ys; y <= ye; y += stepping) {
                y = Math.round((y - ty) / stepping) * stepping + ty;

                int iy = (int) Math.round(y);
                g.drawLine(rulerSize, iy, 0, iy);

                String text = format((y - ty) / increment);

                AffineTransform at = ((Graphics2D) g).getTransform();
                ((Graphics2D) g).rotate(-Math.PI / 2, 0, iy);
                g.drawString(text, 1, iy + labelFont.getSize());
                ((Graphics2D) g).setTransform(at);

                iy += (int) Math.round(stepping / 4);
                g.drawLine(rulerSize, iy, smallTick, iy);

                iy += (int) Math.round(stepping / 4);
                g.drawLine(rulerSize, iy, middleTick, iy);

                iy = iy + (int) Math.round(stepping / 4);
                g.drawLine(rulerSize, iy, smallTick, iy);
            }
        }

        g.setColor(Color.green);

        if (orientation == ORIENTATION_HORIZONTAL) {
            g.drawLine(mouse.x, rulerSize, mouse.x, 0);
        } else {
            g.drawLine(rulerSize, mouse.y, 0, mouse.y);
        }
    }

    private String format(double value) {
        String text = numberFormat.format(value);

        if (text.equals("-0")) {
            text = "0";
        }

        return text;
    }

}
