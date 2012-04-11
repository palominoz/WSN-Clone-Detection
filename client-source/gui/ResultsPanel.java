package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exceptions.BadValue;

import stats.SimStat;

public class ResultsPanel extends JFrame{
	private static ResultsPanel singleton=null;
	
	private static ResultsPanel resultsPanel(){
		if (singleton==null) singleton = new ResultsPanel();
		return singleton;
	}
	
	
	
	
	
	private Vector<SimStat> stats=new Vector<SimStat>();
	
	private JComboBox simulationSelectionCombo=new JComboBox();
	private JComboBox nodeSelectionCombo=new JComboBox();
	
	private SimulationResultsComponent resultsComponent=new SimulationResultsComponent();
	
	public static void showStatistics(SimStat stats){
		resultsPanel().resultsComponent.showStatistics(stats);
	}
	
	ResultsPanel(){
		super();
		JPanel controls=new JPanel(new GridLayout(1,2));
		controls.setBorder(BorderFactory.createTitledBorder("Filters"));
		
		JPanel results=new JPanel(new GridLayout(1,2));
		results.setBorder(BorderFactory.createTitledBorder("Results"));
		
		
		controls.add(simulationSelectionCombo);
		controls.add(nodeSelectionCombo);
		
		results.add(resultsComponent);
		
		add(controls, BorderLayout.NORTH);
		add(results, BorderLayout.SOUTH);
		setSize(400,120);
	}
	
	
	public static class SimulationResultsComponent extends JPanel{
		private JLabel simulationNumber=new JLabel();
		
		private JLabel minLabel=new JLabel("Min");
		private JLabel maxLabel=new JLabel("Max");
		private JLabel avgLabel=new JLabel("Average");
		private JLabel stdevLabel=new JLabel("St. deviation");
		
		private JLabel energyLabel=new JLabel("Energy consumption per node");
			private JLabel minEnergy=new JLabel("0");
			private JLabel maxEnergy=new JLabel("0");
			private JLabel avgEnergy=new JLabel("0");
			private JLabel stdevEnergy=new JLabel("0");
			
		private JLabel receivedLabel=new JLabel("Received messages per node");
			private JLabel minReceived=new JLabel("0");
			private JLabel maxReceived=new JLabel("0");
			private JLabel avgReceived=new JLabel("0");
			private JLabel stdevReceived=new JLabel("0");
			
			
		private JLabel sentLabel=new JLabel("Sent messages per node");
			private JLabel minSent=new JLabel("0");
			private JLabel maxSent=new JLabel("0");
			private JLabel avgSent=new JLabel("0");
			private JLabel stdevSent=new JLabel("0");
			
		private JLabel signaturesLabel=new JLabel("Signature decoding per node");
			private JLabel minSignatures=new JLabel("0");
			private JLabel maxSignatures=new JLabel("0");
			private JLabel avgSignatures=new JLabel("0");
			private JLabel stdevSignatures=new JLabel("0");
			
		private JLabel storedLabel=new JLabel("Messages stored per node");
			private JLabel minStored=new JLabel("St. deviation");
			private JLabel maxStored=new JLabel("St. deviation");
			private JLabel avgStored=new JLabel("St. deviation");
			private JLabel stdevStored=new JLabel("St. deviation");
		
		SimulationResultsComponent(){
			super(new GridLayout(5,6));
			add(simulationNumber);
			add(minLabel);
			add(maxLabel);
			add(avgLabel);
			add(stdevLabel);
			//2 riga
			add(energyLabel);
			add(minEnergy);
			add(maxEnergy);
			add(avgEnergy);
			add(stdevEnergy);
			//3 riga
			add(receivedLabel);
			add(minReceived);
			add(maxReceived);
			add(avgReceived);
			add(stdevReceived);
			//4 riga
			add(sentLabel);
			add(minSent);
			add(maxSent);
			add(avgSent);
			add(stdevSent);
			//5 riga
			add(signaturesLabel);
			add(minSignatures);
			add(maxSignatures);
			add(avgSignatures);
			add(stdevSignatures);
			//5 riga
			add(storedLabel);
			add(minStored);
			add(maxStored);
			add(avgStored);
			add(stdevStored);
			
		}
		
		public void showStatistics(SimStat stats){
			try{
				minEnergy.setText(stats.minimum(SimStat.ValueType.ENERGY).toString());
				maxEnergy.setText(stats.maximum(SimStat.ValueType.ENERGY).toString());
				avgEnergy.setText(stats.average(SimStat.ValueType.ENERGY).toString());
				stdevEnergy.setText(stats.standardDeviation(SimStat.ValueType.ENERGY).toString());
		
				 minReceived.setText(stats.minimum(SimStat.ValueType.REICEVED).toString());
				 maxReceived.setText(stats.maximum(SimStat.ValueType.REICEVED).toString());
				 avgReceived.setText(stats.average(SimStat.ValueType.REICEVED).toString());
				 stdevReceived.setText(stats.standardDeviation(SimStat.ValueType.REICEVED).toString());
				
				 minSent.setText(stats.minimum(SimStat.ValueType.SENT).toString());
				 maxSent.setText(stats.maximum(SimStat.ValueType.SENT).toString());
				 avgSent.setText(stats.average(SimStat.ValueType.SENT).toString());
				 stdevSent.setText(stats.standardDeviation(SimStat.ValueType.SENT).toString());
				
	
				 minSignatures.setText(stats.minimum(SimStat.ValueType.SIGNATURES).toString());
				 maxSignatures.setText(stats.maximum(SimStat.ValueType.SIGNATURES).toString());
				 avgSignatures.setText(stats.average(SimStat.ValueType.SIGNATURES).toString());
				 stdevSignatures.setText(stats.standardDeviation(SimStat.ValueType.SIGNATURES).toString());
				
				 minStored.setText(stats.minimum(SimStat.ValueType.STORED).toString());
				 maxStored.setText(stats.maximum(SimStat.ValueType.STORED).toString());
				 avgStored.setText(stats.average(SimStat.ValueType.STORED).toString());
				 stdevStored.setText(stats.standardDeviation(SimStat.ValueType.STORED).toString());
			} catch (BadValue e){}
		}
	};
		
		
	public static class NodeResultsComponent extends JPanel{};
	
	
}
