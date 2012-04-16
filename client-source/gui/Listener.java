package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import utilities.Log;

import exceptions.GraphicalNodeDoesntExists;
import exceptions.NodeNotFound;
import exceptions.WrongUrlPanel;

import logic.Ambient;
import logic.Hypervisor;
import logic.NodeID;
import logic.Settings;

public class Listener implements ActionListener {

	private static Listener singleton=null;
	
	public static  ActionListener listener(){
		if (singleton==null) singleton=new Listener();
		return singleton;
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("start".equals(e.getActionCommand())){
			if(Settings.areReady()) Hypervisor.runSimulation();
			else{
				try {
					URLPanel.showModalPanel("settings");
				} catch (WrongUrlPanel e1) {
					e1.printStackTrace();
				}
			}
		}
		if ("stop".equals(e.getActionCommand())){
			synchronized(Hypervisor.hypervisor()){
				//Hypervisor.hypervisor().stop();
				System.exit(0);
			}
		}
		if ("next".equals(e.getActionCommand())){
			synchronized(Hypervisor.hypervisor()){
				Hypervisor.hypervisor().notify();
			}
		}
		if ("pause".equals(e.getActionCommand())){
			Ambient.pause();
			ControlPanel.controlPanel().pauseButton.setActionCommand("unpause");
			ControlPanel.controlPanel().pauseButton.setText("Unpause");
			ControlPanel.notifyPause();
		}
		
		if ("lookup".equals(e.getActionCommand())){
			try {
				URLPanel.showModalPanel("server");
			} catch (WrongUrlPanel e1) {
				e1.printStackTrace();
			}
		}
		
		if ("unpause".equals(e.getActionCommand())){
			Ambient.unpause();
			ControlPanel.controlPanel().pauseButton.setActionCommand("pause");
			ControlPanel.controlPanel().pauseButton.setText("Pause");
			ControlPanel.notifyUnpause();
		}
		
		if ("fetch".equals(e.getActionCommand())){
			try {
				URLPanel.showModalPanel("settings");
			} catch (WrongUrlPanel e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if ("showAllMessages".equals(e.getActionCommand())){
			AmbientPanel.setMessagesToBeShown();
		}
		
		if ("showRanges".equals(e.getActionCommand())){
			AmbientPanel.setRangesToBeShown();
		}
		
		if ("override".equals(e.getActionCommand())){
			SettingsPanel.settingsPanel();
		}
		
		if ("showIdles".equals(e.getActionCommand())){
			AmbientPanel.setIdleNodesToBeShown();
		}
		
		if ("showIDs".equals(e.getActionCommand())){
			AmbientPanel.setIDsToBeShown();
		}
		
		if ("highlightNode".equals(e.getActionCommand())){
			try {
				NodeID id=(NodeID)ControlPanel.controlPanel().IDsCombo.getSelectedItem();
				AmbientPanel.setHighlightedNode(Ambient.nodeFromID(id));
			} 
			catch (GraphicalNodeDoesntExists e1) {} 
			catch (NodeNotFound e1) {
				
			}
		}

	}

}
