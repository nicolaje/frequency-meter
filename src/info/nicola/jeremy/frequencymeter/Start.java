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
			try{
				int bufferLength=Integer.parseInt(args[0]);
				int fSample=Integer.parseInt(args[1]);
				boolean fSampleOK=fSample==44100||fSample==22050||fSample==11025||fSample==8000||fSample==48000;
				if(fSampleOK&&bufferLength>=2){
					new Analyzer(view, bufferLength, fSample).start();
				}else{
					new Analyzer(view, BUFFER_LENGTH, Fs).start();
				}
			}catch(NumberFormatException e){
				new Analyzer(view, BUFFER_LENGTH, Fs).start();
			}
		}else{
			new Analyzer(view, BUFFER_LENGTH, Fs).start();
		}
	}
}
