package info.nicola.jeremy.frequencymeter.analysis;

import info.nicola.jeremy.frequencymeter.view.MainView;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

public class Analyzer extends Thread implements WindowListener {

	private boolean run;

	private MainView view;

	private int bufferLength;
	private int fSample;
	private AudioFormat format;

	private FloatFFT_1D fft;

	TargetDataLine targetDataLine;

	public Analyzer(MainView view, int bufferLength, int fSample) {
		this.view = view;
		this.view.addWindowListener(this);

		this.bufferLength = bufferLength;
		this.fSample = fSample;
		this.format = new AudioFormat(this.fSample, 16, 1, true, false);

		this.fft = new FloatFFT_1D(bufferLength);

		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,
				format);
		try {
			this.targetDataLine = (TargetDataLine) AudioSystem
					.getLine(dataLineInfo);
			targetDataLine.open(format);
			targetDataLine.start();
		} catch (LineUnavailableException e) {
			System.err.println("Sorry, I Was not able to find your microphone entry...");
		}
	}

	@Override
	public void run() {
		this.run=true;
		while (run) {
			byte[] raw = new byte[this.bufferLength * 2];
			targetDataLine.read(raw, 0, this.bufferLength * 2);
			final float[] fl = convert(raw);
			fft.realForward(fl);
			final int frequency = new Integer(fSample
					* FindHighestAmplitude(SpectralEnergyDensity(fl)))
					/ bufferLength;
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.setFrequency(frequency);
				}
			});
		}
	}
	
	/**
	 * Converts a 2-bytes array to a float array
	 * @param data
	 * @return
	 */
	public static float[] convert(byte[] data){
		float[] res=new float[data.length/2];
		
		for(int i=0,j=0; i<data.length/2; i++, j+=2){
			res[i]=((data[j] & 0xFF) | (data[j + 1] << 8) )/ 32768.0F;
		}
		return res;
	}
	
	public static float[] SpectralEnergyDensity(float [] fftData){
		float[] res=new float[fftData.length/2];
		
		// TODO: Parallelization
		for(int i=0; i<fftData.length/2; i++){
			float real=fftData[i*2];
			float im=fftData[i*2+1];
			res[i]=(float) Math.sqrt(real*real+im*im);
		}
		return res;
	}
	
	/**
	 * Finds the most energetic peak
	 * @param signal
	 * @return
	 */
	public static int FindHighestAmplitude(float[] signal) {
		float max = 0;
		int index=0;
		for(int i=0; i<signal.length; i++){
			if(signal[i]>max){
				max=signal[i];
				index=i;
			}
		}
		return index;
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * Kills the thread when the window is closing
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.run = false;
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
