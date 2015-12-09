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

public class ListarCategoriaActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.listar_categoria, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_listar_categoria);
		
		SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
		
		String id_tipo = getIntent().getStringExtra("tipo");
		
		Cursor cursor = db.rawQuery("select * from categoria where id_tipo=? or id_tipo=?", new String[]{"3",id_tipo});
		
		String[] from = {"_id","descricao"};
		int[] to = {R.id.lbl_model_saldo, R.id.lbl_model_saldo_conta};
		
		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(getBaseContext(),
				R.layout.activity_lista_cat_model,cursor, from, to);
		
		ListView lv = (ListView) findViewById(R.id.lvCategorias);
		lv.setAdapter(ad);
		
		//-------------------Pegando os dados do item selecionado no listview
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(position);
				Intent intent = new Intent();
				
				intent.putExtra("id_category", c.getString(0));
				intent.putExtra("categoria", c.getString(2));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		db.close();
	}
	
	public void abrirNovaCategoria(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), NovaCategoriaActivity.class);
		startActivity(intent);
	}
	

}
