/**
* AmbientDrawing
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */

package gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;


import utilities.Log;

import logic.Position;
import logic.Settings;

import gui.AmbientPanel.GraphicalNode;
import gui.AmbientPanel.GraphicalJump;

/**
 * This class models instances of a drawable field. The repaint mechanism is not implemented with a thread that timely 
 * refreshes the drawing. An instance of this class should be included in some other wrapper/container in order to be 
 * controlled properly. 
 * */


@SuppressWarnings("serial")
public class AmbientDrawing extends JPanel{
	/***CLASS MEMBERS***/
	
	
	
	/***INSTANCE MEMBERS***/
	
	// flags weather the drawing should be disabled 
	private boolean disabled = false;
	
	/***CONSTRUCTORS***/
	
	AmbientDrawing(){
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(500,500));
	}
	
	/***CLASS METHODS***/
	/***INSTANCE METHODS***/
	
	//activate or deactivate this instance
	public void setActive(boolean active){
		disabled = active;
	}
	
	// override of the repaint method from the superclasses in order to draw custom objects
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		//if disabled, write it and return
		if (disabled){
			g.drawString("**DISABLED**", 10, 20);
			return;
		}
		
		
		// clear previous things
		g.clearRect(0, 0, getWidth(), getHeight());
		Log.write("Repainting..", "gui.AmbientDrawing", "USELESS");
		Graphics2D painter = (Graphics2D)g;
		
		// draw nodes
		synchronized (AmbientPanel.ambientPanel().nodes){
			Iterator<GraphicalNode> i=AmbientPanel.ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode node=i.next();
				synchronized(node){
					drawNode(painter, node);
				}	
			}
		}
		
		// draw messages
		if (AmbientPanel.allMessagesToBeShown){
			drawAllMessages(painter);
		}
		
	}
	
	
	// draws the messages recorded in AmbientPanel
	private void drawAllMessages(Graphics2D painter){
		synchronized (AmbientPanel.ambientPanel().messageList){
			Iterator<GraphicalJump> k=AmbientPanel.ambientPanel().messageList.iterator();
			while (k.hasNext()){
				GraphicalJump jump = k.next();
				if (jump.highlighted){
					drawLine(painter, new Line(jump), Color.blue);
				}
				else{
					drawLine(painter, new Line(jump), Color.black);
				}
			}
		}
	}
	
	
	// draw a single node
	private void drawNode(Graphics2D painter, GraphicalNode gnode){
		Position position=gnode.node.position();
		Log.write("Drawing node number "+gnode.node.getId()+" in "+ position, "gui.AmbientDrawing", "USELESS");
		
		// if it is a clone we highlight it
		if (gnode.isClone){
			painter.setColor(Color.RED);
			painter.draw(new Ellipse2D.Double(
					(int)(gnode.node.position().X*getWidth()-15),
					(int)(gnode.node.position().Y*getHeight()-15),
							30,30)
					);
		}
		
		
		
		// set color based on characteristics of the node
		if (gnode.isAttacked) painter.setColor(Color.BLUE);
		if (gnode.isAttacker){painter.setColor(Color.ORANGE);}
		if (gnode.isDead){painter.setColor(Color.RED);}
		if (gnode.isDetector){painter.setColor(Color.GREEN);}
		if (gnode.isHighlighted){painter.setColor(Color.MAGENTA);}
		
		// utils
		double centerX=(position.X*getWidth());
		double centerY=(position.Y*getHeight());
		
		// if id is requested we draw it
		if (AmbientPanel.IDsToBeShown){
			painter.drawString(
					gnode.toString(), 
					(int)(centerX-5), 
					(int)(centerY+5)
					);
		}
		else{
			// otherwise we draw a square
			painter.fillRect((int)(centerX-5),(int)(centerY-5), 10, 10);
			if (gnode.isIdle && AmbientPanel.IdleNodesToBeShown){
				painter.drawString(
						gnode.idleString(), 
						(int)(centerX+5), 
						(int)(centerY+5)
						);
			}
		}
		
		// if the range is requested we draw it
		if (AmbientPanel.rangesToBeShown){
			
			Random generator=new Random();
			painter.setColor(new Color(generator.nextInt(255),generator.nextInt(255),generator.nextInt(255)));
			
			Ellipse2D.Double range=new Ellipse2D.Double();
			
			range.setFrameFromCenter(centerX, centerY, 
					centerX-(Settings.transmissionRange*getWidth()), 
					centerY-(Settings.transmissionRange*getHeight()));
			painter.draw(range);
		}
		
		painter.setColor(Color.black);
		
	}
	
	
	// draw a line in the field
	private void drawLine(Graphics2D painter, Line line, Color c){
		Color old=painter.getColor();
		painter.setColor(c);
		painter.drawLine(
				(int)(line.origin.X*getWidth()),
				(int)(line.origin.Y*getHeight()),
				(int)(line.destination.X*getWidth()),
				(int)(line.destination.Y*getHeight())
				);
		painter.setColor(old);
	}

	
	// inner class 
	public static class Line{
		Position origin;
		Position destination;
		
		
		
		Line(GraphicalJump gj){
			origin = gj.start;
			destination = gj.end;
		}
	};
	
}
