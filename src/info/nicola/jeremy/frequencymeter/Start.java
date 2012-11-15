package info.nicola.jeremy.frequencymeter;

import info.nicola.jeremy.frequencymeter.analysis.Analyzer;
import info.nicola.jeremy.frequencymeter.view.MainView;

public class Start {
	
	public static final int Fs=44100;
	public static final int BUFFER_LENGTH=4410;
	
	public static void main(String[] args){
		MainView view=new MainView("UV 5.5 - Demo");
		view.pack();
		view.setVisible(true);
		if(args.length==2){
			
		}else{
			new Analyzer(view, BUFFER_LENGTH, Fs).start();
		}
	}
}
