/**
* Listener
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import exceptions.GraphicalNodeDoesntExists;
import exceptions.NodeNotFound;
import exceptions.WrongUrlPanel;

import logic.Ambient;
import logic.Hypervisor;
import logic.NodeID;
import logic.Settings;

/*
 * This class listens to particular events from buttons and triggers consequent actions. 
 * 
 * */

public class Listener implements ActionListener {

	private static Listener singleton=null;
	
	public static  ActionListener listener(){
		if (singleton==null) singleton=new Listener();
		return singleton;
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("start".equals(e.getActionCommand())){
			if(Settings.areReady()) Hypervisor.startSimulations();
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
				Hypervisor.unpause();
			}
		}
		if ("pause".equals(e.getActionCommand())){
			Ambient.pause();
			ControlPanel.controlPanel().pauseButton.setActionCommand("unpause");
			ControlPanel.controlPanel().pauseButton.setText("Unpause");
			ControlPanel.notifyPause();
		}
		if ("ClonePause".equals(e.getActionCommand())){
			Hypervisor.stopsAfterDetectingClone = ! (Hypervisor.stopsAfterDetectingClone);
		}
		if ("SimPause".equals(e.getActionCommand())){
			Hypervisor.stopsAfterCompletingEverySimulation = !(Hypervisor.stopsAfterCompletingEverySimulation);
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
		
		if ("EnableGraphics".equals(e.getActionCommand())){
			if (UserInterface.enabled()){
				UserInterface.enable();
				AmbientPanel.setActive(true);
			}
			else{
				UserInterface.disable();
				AmbientPanel.setActive(false);
			}
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
			SettingsPanel.wake();
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
