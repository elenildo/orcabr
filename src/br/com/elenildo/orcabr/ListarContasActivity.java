package br.com.elenildo.orcabr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListarContasActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_contas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listar_contas, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_listar_contas);
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
		
		Cursor cursor = db.rawQuery("select * from conta", null);
		
		String[] from = {"_id", "nome", "saldo"};
		int[] to = {R.id.lbl_model_banco_id, R.id.lbl_model_conta_nome, R.id.lbl_model_saldo_conta};
		
		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(getBaseContext(),
				R.layout.activity_lista_conta_model,cursor, from, to);
		
		ListView lv = (ListView) findViewById(R.id.lv_listar_contas);
		lv.setAdapter(ad);
		
		//-------------------Pegando os dados do item selecionado no listview
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.setClass(getBaseContext(), ContaActivity.class);
				
				intent.putExtra("conta_id", c.getString(0));
				intent.putExtra("conta_nome", c.getString(1));
				intent.putExtra("conta_saldo", c.getString(2));
				intent.putExtra("op", "editar");
				
				setResult(RESULT_OK, intent);
				startActivity(intent);
				finish();
			}
		});
		
		db.close();
	}
	
	public void abrirNovaConta(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), ContaActivity.class);
		startActivity(intent);
	}
	

}
