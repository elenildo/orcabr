package br.com.elenildo.orcabr;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CalendarActivity extends Activity implements View.OnClickListener{
	DateFormat formato = DateFormat.getDateInstance();
	Calendar calendar = Calendar.getInstance();
	TextView label;
	Button botao;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		label = (TextView) findViewById(R.id.txt_mostra_data);
		botao = (Button) findViewById(R.id.btn_ajustar_data);
		botao.setOnClickListener(this);
		atualizarData();
	}
	
	public void ajustarData(){
		new DatePickerDialog(this, dialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void atualizarData(){
		botao.setText(formato.format(calendar.getTime()));
	}
	
	DatePickerDialog.OnDateSetListener dialog = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			atualizarData();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		ajustarData();
	}

	
}
