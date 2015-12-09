package br.com.elenildo.orcabr;

import java.text.NumberFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RelatorioActivity extends Activity {

	TextView lblContas, lblReceitas, lblDespesas, lblTotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatorio);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relatorio, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_relatorio);
		
		lblContas = (TextView) findViewById(R.id.lbl_rel_saldo);
		lblReceitas = (TextView) findViewById(R.id.lbl_rel_total_receitas);
		lblDespesas = (TextView) findViewById(R.id.lbl_rel_total_despesas);
		lblTotal = (TextView) findViewById(R.id.lbl_rel_saldo_atual);
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
		
		Cursor cursor = db.rawQuery("select id_tipo, valor, registrado, data, obs from registro", null);
		Cursor cursor_contas = db.rawQuery("select SUM(saldo) AS total from conta", null);
		
		Double totalR = 0.0, totalD = 0.0, valor, total, contas=0.0;
		int cont = 0;
		
//		ArrayList<Lancamento> pendentes = new ArrayList<Lancamento>();
		
		while(cursor.moveToNext()){
			if(cursor.getInt(cursor.getColumnIndex("registrado")) == 1){
				valor = cursor.getDouble(cursor.getColumnIndex("valor"));
				if(cursor.getInt(cursor.getColumnIndex("id_tipo")) == 1)
					totalR += valor; else totalD += valor;
			}else{
//				Lancamento lan = new Lancamento();
//				lan.setData(cursor.getString(cursor.getColumnIndex("data")));
//				lan.setValor(cursor.getDouble(cursor.getColumnIndex("valor")));
//				lan.setObs(cursor.getString(cursor.getColumnIndex("obs")));
//				pendentes.add(lan);
				cont ++;
			}
		}
		
		while(cursor_contas.moveToNext()){
			contas = cursor_contas.getDouble(cursor_contas.getColumnIndex("total"));
		}
		total = totalR - totalD + contas;
		
		lblReceitas.setText(NumberFormat.getCurrencyInstance().format(totalR));
		lblDespesas.setText(NumberFormat.getCurrencyInstance().format(totalD));
		lblTotal.setText(NumberFormat.getCurrencyInstance().format(total));
		lblContas.setText(NumberFormat.getCurrencyInstance().format(contas));
		
		db.close();
		
		if(cont > 0) 
			Toast.makeText(getBaseContext(), "Há " + cont + "registro(s) pendente(s) de pagamento", Toast.LENGTH_LONG).show();
	}
	
	public void fecharRelatorio(View view){
		finish();
	}

}
