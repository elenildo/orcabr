package br.com.elenildo.orcabr;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import br.com.elenildo.entidades.Balanco;
import br.com.elenildo.entidades.Lancamento;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.Menu;
import android.widget.LinearLayout;

public class GraphActivity extends Activity {
	
	private GraphicalView mChartView; 
//	private ArrayList<Lancamento> valores;
	private ArrayList<Balanco> valores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		createChart();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
		return true;
	}
	
	public void createChart(){
		  
		  CategorySeries series = new CategorySeries("Grafico de barras");
		  
		  XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		  dataset.addSeries(series.toXYSeries());
		  XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
		  
		  setChartSettings(renderer);
		  
		  LinearLayout layout = (LinearLayout) findViewById(R.id.layout_chart);
		  mChartView = ChartFactory.getBarChartView(this, getBarDemoDataset(), renderer, Type.STACKED);
		  
		  layout.addView(mChartView);
		  
	}
	
	private XYMultipleSeriesDataset getBarDemoDataset() {
	     XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

	     valores = this.loadDataBalanco();
	     CategorySeries series = new CategorySeries(getString(R.string.txt_chart_title_series));
	     
	     for (Balanco valor:valores){
	    	 series.add(valor.getResultado());
	     }
	     
	     dataset.addSeries(series.toXYSeries());
	     return dataset;
	   }
	
	 public XYMultipleSeriesRenderer getBarDemoRenderer() {
	     XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	     SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	     NumberFormat format = NumberFormat.getCurrencyInstance();
	     format.setMaximumFractionDigits(3);

	     r.setColor(Color.BLUE);
	     r.setChartValuesTextSize(20);
	     r.setChartValuesFormat(format);
	     
	     renderer.setAxisTitleTextSize(16);
	     renderer.setBarSpacing(1);
	     renderer.setLegendTextSize(15);
	     renderer.addSeriesRenderer(r);
	     
	     return renderer;
	   }
	 
	 private void setChartSettings(XYMultipleSeriesRenderer renderer) {
	     renderer.setShowLegend(true);
	     renderer.setAxesColor(Color.DKGRAY);
	     renderer.setXAxisMin(0.5);
	     renderer.setXAxisMax(12.5);
	     renderer.setYLabelsAlign(Align.RIGHT);
	     renderer.setZoomEnabled(true, true);
	     renderer.setGridColor(Color.GRAY);
	     renderer.setShowGridY(true);
	     renderer.setShowGridX(true);
	     renderer.setChartTitle(getString(R.string.txt_chart_title_results));
	     renderer.setDisplayChartValues(true);
	     renderer.setChartTitleTextSize(50);
	     renderer.setLabelsTextSize(20);
	     renderer.setLabelsColor(Color.BLACK);
	     
	     valores = this.loadDataBalanco();
	     int i = 0;
	     String meses[] = {"jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dez"};
	     for (Balanco valor:valores){
	    	 i++;
	    	 String mes = meses[Integer.parseInt(valor.getData().split("-")[1])-1] + "\n" + valor.getData().split("-")[0];
	    	 
	    	 renderer.addXTextLabel(i, mes);
	     }
	     
	 }
	 
	public ArrayList<Lancamento> loadData() {
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
	
		Cursor cursor = db.rawQuery("select data, valor from registro", null);
		ArrayList<Lancamento> dados = new ArrayList<Lancamento>();
		cursor.moveToFirst();
		
		while(cursor.isAfterLast() == false){
			Lancamento reg = new Lancamento(); 
			reg.setData(cursor.getString(0));
			reg.setValor(cursor.getDouble(1));
			
			dados.add(reg);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		
		return dados;
	}
	
public ArrayList<Balanco> loadDataBalanco() {
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
	
		Cursor cursor = db.rawQuery("select data, resultado from balanco", null);
		ArrayList<Balanco> dados = new ArrayList<Balanco>();
		
		cursor.moveToFirst();
		
		while(cursor.isAfterLast() == false){
			Balanco reg = new Balanco(); 
			reg.setData(cursor.getString(0));
			reg.setResultado(cursor.getDouble(1));
			
			dados.add(reg);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		
		return dados;
	}

}
