package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exceptions.CoordinateOutOfBounds;
import exceptions.MessageHasNotBeenSent;
import exceptions.NodeIsTooFar;

import utilities.Log;

import logic.Node;
import logic.Position;
import logic.Settings;
import messages.Message.MessageJump;

import gui.AmbientPanel.GraphicalNode;
import gui.AmbientPanel.MessageListItem;

@SuppressWarnings("serial")
public class AmbientDrawing extends JPanel{
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		Log.write("Repainting..", "gui.AmbientDrawing", "USELESS");
		Graphics2D painter = (Graphics2D)g;
		
		//draw nodes
		synchronized (AmbientPanel.ambientPanel().nodes){
			Iterator<GraphicalNode> i=AmbientPanel.ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode node=i.next();
				synchronized(node){
					drawNode(painter, node);
				}	
			}
		}
		
		//draw messages
		if (AmbientPanel.allMessagesToBeShown){
			drawAllMessages(painter);
		}
		
	}
	
	private void drawLineAnimationPoint(Graphics2D painter, Position point){
		Log.write("Drawing animation point..", "gui.AmbientDrawing", "USELESS");
		Ellipse2D.Double graphicPoint=new Ellipse2D.Double();
		
		graphicPoint.setFrameFromCenter(
				point.X*getWidth(), 
				point.Y*getHeight(), 
				(point.X*getWidth())-30, 
				(point.Y*getHeight())-30
				);
		painter.draw(graphicPoint);
	}
	
	private void drawAllMessages(Graphics2D painter){
		synchronized (AmbientPanel.ambientPanel().messageList){
			try{
				Iterator<MessageListItem> k=AmbientPanel.ambientPanel().messageList.iterator();
				
				while (k.hasNext()){	
					MessageListItem item=k.next();
					Iterator<MessageJump> j=item.message.jumps().iterator();
					while (j.hasNext()){
						MessageJump jump=j.next();
						drawLine(painter, new Line(jump), item.color);
					}
				}
			} catch(MessageHasNotBeenSent e){
				Log.write("AmbientDrawing detected a message which has not been sent", "gui.AmbientDrawing", "BUG");
			} catch (NodeIsTooFar e) {
				Log.write("AmbientDrawing detected a non legal jump (distance)", "gui.AmbientDrawing", "BUG");
			}
			
		}
	}
	
	private void drawNode(Graphics2D painter, GraphicalNode gnode){
		Position position=gnode.node.position();
		Log.write("Drawing node number "+gnode.node.getId()+" in "+ position, "gui.AmbientDrawing", "USELESS");
		if (gnode.isClone){
			painter.setColor(Color.RED);
			painter.draw(new Ellipse2D.Double(
					(int)(gnode.node.position().X*getWidth()),
					(int)(gnode.node.position().Y*getHeight()),
							10,10)
					);
		}
		
		if (AmbientPanel.rangesToBeShown){
			Random generator=new Random();
			painter.setColor(new Color(generator.nextInt(255),generator.nextInt(255),generator.nextInt(255)));
		}
		
		
		
		if (gnode.isAttacked) painter.setColor(Color.BLUE);
		if (gnode.isAttacker){painter.setColor(Color.ORANGE);}
		if (gnode.isDead){painter.setColor(Color.RED);}
		if (gnode.isHighlighted){painter.setColor(Color.MAGENTA);}
		
		//utils
		double centerX=(position.X*getWidth());
		double centerY=(position.Y*getHeight());
		
		
		if (AmbientPanel.IDsToBeShown){
			painter.drawString(
					gnode.toString(), 
					(int)(centerX-5), 
					(int)(centerY+5)
					);
		}
		else{
			painter.fillRect((int)(centerX-5),(int)(centerY-5), 10, 10);
			if (gnode.isIdle && AmbientPanel.IdleNodesToBeShown){
				painter.drawString(
						gnode.idleString(), 
						(int)(centerX+5), 
						(int)(centerY+5)
						);
			}
		}
		
		
		if (AmbientPanel.rangesToBeShown){
			Ellipse2D.Double range=new Ellipse2D.Double();
			
			range.setFrameFromCenter(centerX, centerY, 
					centerX-(Settings.transmissionRange*getWidth()), 
					centerY-(Settings.transmissionRange*getHeight()));
			painter.draw(range);
		}
		
		painter.setColor(Color.black);
		
	}
	
	AmbientDrawing(){
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(500,500));
	}
	
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

	
	
	public static class Line{
		Position origin;
		Position destination;
		
		Line(MessageJump mj){
			origin=mj.origin.position();
			destination=mj.destination.position();
		}
	};
	
}
