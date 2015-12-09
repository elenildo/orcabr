package br.com.elenildo.orcabr;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fechamento();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void abrirNovaReceita(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), LancamentoActivity.class);
		intent.putExtra("tipo", "1");
		intent.putExtra("titulo",getString(R.string.txt_new_income));
		startActivity(intent);
	}
	public void abrirNovaDespesa(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), LancamentoActivity.class);
		intent.putExtra("tipo", "2");
		intent.putExtra("titulo", getString(R.string.txt_new_expense));
		startActivity(intent);
	}
	public void abrirListaRegistros(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), ListarRegistrosActivity.class);
		startActivity(intent);
	}
	public void abrirGraficos(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), GraphActivity.class);
		startActivity(intent);
	}
	public void abrirRelatorio(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), RelatorioActivity.class);
		startActivity(intent);
	}
	public void abrirOpcoes(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), OpcoesActivity.class);
		startActivity(intent);
	}
	public void abrirLogin(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), LoginActivity.class);
		startActivity(intent);
		
	}
	
	public void fechamento(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		int last_day = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		
		if( last_day == today){
			try{
				SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
				Cursor cursor = db.rawQuery("select * from balanco", null);
				Cursor cbancos = db.rawQuery("select SUM(saldo) AS total from conta", null);
				Cursor cregistros = db.rawQuery("select id_tipo, valor, registrado from registro", null);
				
				if(cursor != null){
					ContentValues cv = new ContentValues();
					Double totalR = 0.0, totalD = 0.0, valor, total, contas=0.0;
					
					while(cregistros.moveToNext()){
						if(cregistros.getInt(cregistros.getColumnIndex("registrado")) == 1){
							valor = cregistros.getDouble(cregistros.getColumnIndex("valor"));
							if(cregistros.getInt(cregistros.getColumnIndex("id_tipo")) == 1) 
								totalR += valor; else totalD += valor;
						}
					}
					
					while(cbancos.moveToNext()){
						contas += cbancos.getDouble(cbancos.getColumnIndex("total"));
					}
					total = totalR - totalD + contas;
					
					cv.put("data", formato.format(calendar.getTime()));
					cv.put("receitas", totalR);
					cv.put("despesas", totalD);
					cv.put("resultado", total);
					
					if(cursor.moveToLast()){
						String data = cursor.getString(cursor.getColumnIndex("data"));
						
						if(! data.equals(formato.format(calendar.getTime())) ){	
							if(db.insert("balanco", "_id", cv) > 0){
								Toast.makeText(getBaseContext(), "New balance registered!", Toast.LENGTH_LONG).show();
							}else
								Toast.makeText(getBaseContext(), "Error on register new balance!", Toast.LENGTH_LONG).show();
						}
					}else{
						
						if(db.insert("balanco", "_id", cv) > 0){
							Toast.makeText(getBaseContext(), "New balance registered!", Toast.LENGTH_LONG).show();
						}else
							Toast.makeText(getBaseContext(), "Error on register new balance!", Toast.LENGTH_LONG).show();
						
					}
				}
				
				db.close();
			}catch(Exception e){
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
}
