package br.com.elenildo.orcabr;

import java.text.NumberFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ListarRegistrosActivity extends Activity {
	TextView lblTotal;
	Button btn_new;
	RadioGroup rgp_filtro;
	RadioButton rdb_opcao;
	Cursor cursor, cursor_contas, cursor_filter;
	boolean inicio = false;
	int selecao = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_registros);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.listar_registros, menu);
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_listar_registros);
		
		lblTotal = (TextView) findViewById(R.id.lbl_total);
		btn_new = (Button) findViewById(R.id.btn_lista_reg_add);
		rgp_filtro = (RadioGroup) findViewById(R.id.radioGroup1);
		rdb_opcao = (RadioButton) rgp_filtro.findViewById(rgp_filtro.getCheckedRadioButtonId());
		
		if(! inicio){
			listar("select r._id, r.id_tipo, r.data, r.valor, r.registrado, r.obs, c.descricao, c._id" +
					" from registro r, categoria c"+
					" where r.id_cat = c._id and r.id_tipo = 1");
		}
		
		rgp_filtro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				inicio=true;
				switch(rgp_filtro.getCheckedRadioButtonId()){
				case R.id.radio0:
					selecao=1;
					listar("select r._id, r.id_tipo, r.data, r.valor, r.registrado, r.obs, c.descricao, c._id" +
					" from registro r, categoria c"+
					" where r.id_cat = c._id and r.id_tipo = 1");
					break;
				case R.id.radio1:
					selecao=2;
					listar("select r._id, r.id_tipo, r.data, r.valor, r.registrado, r.obs, c.descricao, c._id" +
					" from registro r, categoria c"+
					" where r.id_cat = c._id and r.id_tipo = 2");
					break;
				case R.id.radio2:
					selecao=2;
					listar("select r._id, r.id_tipo, r.data, r.valor, r.registrado, r.obs, c.descricao, c._id" +
					" from registro r, categoria c"+
					" where r.id_cat = c._id");
					break;
				}
			}
		});
		
		btn_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				abrirLancamento(getCurrentFocus());
				finish();
			}
		});
	}
	
	public void listar(String string){
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
		cursor = db.rawQuery(string, null);
		
		cursor_contas = db.rawQuery("select SUM(saldo) AS total from conta", null);

		String[] from = {"_id","id_tipo","data","valor","descricao","registrado","obs","_id"};
		int[] to = {R.id.lbl_Id_reg, R.id.lbl_tipo_reg, R.id.lbl_data_reg, R.id.lbl_reg_valor, 
				R.id.lbl_cat_reg, R.id.lbl_status_reg, R.id.lbl_info_obs, R.id.lbl_id_cat};
		
		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(getBaseContext(),
				R.layout.activity_lista_reg_model,cursor, from, to);
		
		Double totalR = 0.0, totalD = 0.0, valor, total, contas=0.0;
//		while(cursor.moveToNext()){
//			valor = cursor.getDouble(cursor.getColumnIndex("valor"));
//			if(cursor.getInt(cursor.getColumnIndex("id_tipo")) == 1)
//				totalR += valor; else totalD += valor;
//		}
		while(cursor.moveToNext()){
			if(cursor.getInt(cursor.getColumnIndex("registrado")) == 1){
				valor = cursor.getDouble(cursor.getColumnIndex("valor"));
				if(cursor.getInt(cursor.getColumnIndex("id_tipo")) == 1)
					totalR += valor; else totalD += valor;
			}
		}
		
		while(cursor_contas.moveToNext()){
			contas += cursor_contas.getDouble(cursor_contas.getColumnIndex("total"));
		}
		
		if (totalR > 0){
			total = totalR - totalD + contas;
			lblTotal.setTextColor(Color.BLACK);
		}
		else{
			total = totalR - totalD;
			lblTotal.setTextColor(Color.RED);
		}
		lblTotal.setText(NumberFormat.getCurrencyInstance().format(total));
		
		ListView lv = (ListView) findViewById(R.id.lvRegistros);
		lv.setAdapter(ad);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.setClass(getBaseContext(), LancamentoActivity.class);
				
				intent.putExtra("op", "editar");
				intent.putExtra("id_reg", c.getString(0));
				intent.putExtra("tipo", c.getString(1));
				intent.putExtra("data_reg", c.getString(2));
				intent.putExtra("valor_reg", c.getString(3));
				intent.putExtra("status_reg", c.getString(4));
				intent.putExtra("obs_reg", c.getString(5));
				intent.putExtra("categoria_reg", c.getString(6));
				intent.putExtra("id_cat_reg", c.getString(7));

				setResult(RESULT_OK, intent);
				startActivity(intent);
				finish();
			}
		});
		
		db.close();
	}
	
	public void abrirLancamento(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), LancamentoActivity.class);
		if(selecao == 1){
			intent.putExtra("tipo", "1");
			intent.putExtra("titulo","Nova Receita");
		}else {
			intent.putExtra("tipo", "2");
			intent.putExtra("titulo","Nova Despesa");
		}
		startActivity(intent);
	}
	

}
