// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package info.nicola.jeremy.frequencymeter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

public class Start extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8552549315986984813L;
	public static final int Fs=44100;
	public static final int BUFFER_LENGTH=4410;
	static class DemoPanel extends JPanel
		
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3270195019288256427L;
		public static DefaultValueDataset dataset;

		public static JFreeChart createStandardDialChart(String s, String s1, ValueDataset valuedataset, double d, double d1, double d2, int i)
		{
			DialPlot dialplot = new DialPlot();
			dialplot.setDataset(valuedataset);
			dialplot.setDialFrame(new StandardDialFrame());
			dialplot.setBackground(new DialBackground());
			DialTextAnnotation dialtextannotation = new DialTextAnnotation(s1);
			dialtextannotation.setFont(new Font("Dialog", 1, 14));
			dialtextannotation.setRadius(0.69999999999999996D);
			dialplot.addLayer(dialtextannotation);
			DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
			dialplot.addLayer(dialvalueindicator);
			StandardDialScale standarddialscale = new StandardDialScale(d, d1, -120D, -300D, 10D, 4);
			standarddialscale.setMajorTickIncrement(d2);
			standarddialscale.setMinorTickCount(i);
			standarddialscale.setTickRadius(0.88D);
			standarddialscale.setTickLabelOffset(0.14999999999999999D);
			standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
			dialplot.addScale(0, standarddialscale);
			dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
			DialCap dialcap = new DialCap();
			dialplot.setCap(dialcap);
			return new JFreeChart(s, dialplot);
		}
		
		public DemoPanel()
		{
			super(new BorderLayout());
			dataset = new DefaultValueDataset(10D);
			JFreeChart jfreechart = createStandardDialChart("UV 5.5 - Démo", "Frequency", dataset, 0D, 5000D, 500D, 4);
			DialPlot dialplot = (DialPlot)jfreechart.getPlot();
			StandardDialRange standarddialrange = new StandardDialRange(40D, 60D, Color.red);
			standarddialrange.setInnerRadius(0.52000000000000002D);
			standarddialrange.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange);
			StandardDialRange standarddialrange1 = new StandardDialRange(10D, 40D, Color.orange);
			standarddialrange1.setInnerRadius(0.52000000000000002D);
			standarddialrange1.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange1);
			StandardDialRange standarddialrange2 = new StandardDialRange(-40D, 10D, Color.green);
			standarddialrange2.setInnerRadius(0.52000000000000002D);
			standarddialrange2.setOuterRadius(0.55000000000000004D);
			dialplot.addLayer(standarddialrange2);
			GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
			DialBackground dialbackground = new DialBackground(gradientpaint);
			dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
			dialplot.setBackground(dialbackground);
			dialplot.removePointer(0);
			org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
			dialplot.addPointer(pointer);
			ChartPanel chartpanel = new ChartPanel(jfreechart);
			chartpanel.setPreferredSize(new Dimension(400, 400));
			
			add(chartpanel);
		}
	}


	public Start(String s)
	{
		super(s);
		setDefaultCloseOperation(3);
		setContentPane(createDemoPanel());
	}

	public static JPanel createDemoPanel()
	{
		return new DemoPanel();
	}

	public static void main(String args[])
	{
		Start dialdemo1 = new Start("JFreeChart: DialDemo1.java");
		dialdemo1.pack();
		dialdemo1.setVisible(true);
		new Thread(new Runnable() {
			private FloatFFT_1D fft;
			float max;
			@Override
			public void run() {
				fft=new FloatFFT_1D(BUFFER_LENGTH);
				AudioFormat format=new AudioFormat(44100, 16, 1, true, false);
				DataLine.Info dataLineInfo=new DataLine.Info(TargetDataLine.class, format);
				try {
					TargetDataLine targetDataLine=(TargetDataLine)AudioSystem.getLine(dataLineInfo);
					targetDataLine.open(format);
					targetDataLine.start();
					while(true){
						byte[] raw=new byte[BUFFER_LENGTH*2];
						targetDataLine.read(raw, 0, BUFFER_LENGTH*2);
						final float[] fl=convert(raw);
						fft.realForward(fl);
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								Start.DemoPanel.dataset.setValue(new Integer(Fs*FindHighestAmplitude(spectralEnergyDensity(fl)))/BUFFER_LENGTH);
							}
						});
					}
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				
			}
			
			private float[] spectralEnergyDensity(float [] fftData){
				float[] res=new float[fftData.length/2];
				max=0;
				
				// TODO: Parallelization
				for(int i=0; i<fftData.length/2; i++){
					float real=fftData[i*2];
					float im=fftData[i*2+1];
					res[i]=(float) Math.sqrt(real*real+im*im);
					if(res[i]>max)max=res[i];
				}
				return res;
			}
		}).start();
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
}
