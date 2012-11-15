package info.nicola.jeremy.frequencymeter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

public class MainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4878150693297785117L;

	private DefaultValueDataset dataSet;
	
	public MainView(String name){
		super(name);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(createDemoPanel());
	}
	
	public void setFrequency(int frequency){
		dataSet.setValue(new Integer(frequency));
	}
	
	public JPanel createDemoPanel()
	{
		JPanel panel=new JPanel(new BorderLayout());
		dataSet = new DefaultValueDataset(10D);
		JFreeChart jfreechart = createDialChart("UV 5.5 - Démo", "Frequency (Hz)", dataSet, 0D, 5000D, 500D, 4);
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
		
		panel.add(chartpanel);
		return panel;
	}
	
	private JFreeChart createDialChart(String title, String caption, ValueDataset dataSet, double dScaleMin, double dScaleMax, double majorTick, int tickCount){
		DialPlot dialplot = new DialPlot();
		dialplot.setDataset(dataSet);
		dialplot.setDialFrame(new StandardDialFrame());
		dialplot.setBackground(new DialBackground());
		DialTextAnnotation dialtextannotation = new DialTextAnnotation(caption);
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);
		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialplot.addLayer(dialvalueindicator);
		StandardDialScale standarddialscale = new StandardDialScale(dScaleMin, dScaleMax, -120D, -300D, 10D, 4);
		standarddialscale.setMajorTickIncrement(majorTick);
		standarddialscale.setMinorTickCount(tickCount);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);
		dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
		DialCap dialcap = new DialCap();
		dialplot.setCap(dialcap);
		return new JFreeChart(title, dialplot);
	}
}
