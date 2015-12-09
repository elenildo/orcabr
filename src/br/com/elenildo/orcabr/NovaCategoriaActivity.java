package br.com.elenildo.orcabr;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NovaCategoriaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nova_categoria);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nova_categoria, menu);
		return true;
	}
	public void cadastrarClick(View v){

		EditText txt_Categoria = (EditText) findViewById(R.id.txtDescricaoCategoria);
		RadioButton rad_rec = (RadioButton) findViewById(R.id.radReceita);
		RadioButton rad_geral = (RadioButton) findViewById(R.id.radGeral);
		int tipo;
		
		if(rad_geral.isChecked())
			tipo = 3;
		else if(rad_rec.isChecked())
			tipo = 1;
		else
			tipo = 2;
		
		
		if(txt_Categoria.getText().toString().length() <= 0){
			txt_Categoria.setError(getString(R.string.ferror_empty));
			txt_Categoria.requestFocus();
		} 
		else{
			try{
				SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
				
				ContentValues cv = new ContentValues();
				cv.put("id_tipo", tipo);
				cv.put("descricao", txt_Categoria.getText().toString());
				
				if(db.insert("categoria", "_id", cv) > 0)
					Toast.makeText(getBaseContext(), getString(R.string.msg_success_insert), Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getBaseContext(), getString(R.string.msg_error_insert), Toast.LENGTH_SHORT).show();
				
				db.close();
				finish();
				
			}catch(Exception e){
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

}
