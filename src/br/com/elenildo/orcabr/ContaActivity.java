package br.com.elenildo.orcabr;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContaActivity extends Activity {
	
	private EditText edt_id; 
	private EditText edt_nome;
	private EditText edt_saldo;
	private Button btn_ok, btn_excluir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conta);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.conta, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 edt_id = (EditText) findViewById(R.id.edt_id_conta);
		 edt_nome = (EditText) findViewById(R.id.edt_nome_conta);
		 edt_saldo = (EditText) findViewById(R.id.edt_saldo_conta);
		 
		 btn_ok = (Button) findViewById(R.id.btn_salvar_conta);
		 btn_excluir = (Button) findViewById(R.id.btn_conta_excluir);
		
		if(getIntent().hasExtra("op")){
			edt_id.setText(getIntent().getStringExtra("conta_id"));
			edt_nome.setText(getIntent().getStringExtra("conta_nome"));
			edt_saldo.setText(getIntent().getStringExtra("conta_saldo"));
			btn_ok.setText("Atualizar");
			btn_excluir.setVisibility(View.VISIBLE);
		}
	}
	public void cadastrarConta(View v){
		
		if(edt_nome.getText().toString().equals("")){
			edt_nome.setError("Digite um nome para a conta");
			edt_nome.requestFocus();
		}
		
		if(edt_nome.getText().toString().equals("")){
			edt_nome.setText("0");
		}
		
		try{
			SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
			
			if(getIntent().hasExtra("op")){
	
				String sql = "update conta SET nome = '"+
						edt_nome.getText().toString() +
						"', saldo = "+
						edt_saldo.getText().toString()+
						" where _id = " +
						getIntent().getStringExtra("conta_id")+
						";";
						
				db.execSQL(sql);
				db.close();
				
				Toast.makeText(getBaseContext(), "Sucesso ao atualizar", Toast.LENGTH_SHORT).show();
				finish();
				
			}else{
				
					ContentValues cv = new ContentValues();
					cv.put("nome", edt_nome.getText().toString());
					cv.put("saldo", Double.parseDouble(edt_saldo.getText().toString()));
					
					if(db.insert("conta", "_id", cv) > 0)
						Toast.makeText(getBaseContext(), "Sucesso ao cadastrar", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getBaseContext(), "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
					
					db.close();
					finish();
				}
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}
	
	public void excluirConta(View v){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		 
        alertDialog.setTitle("Confirmar excusão...");
        alertDialog.setMessage("Tem certeza que quer excluir esta conta?");
//        alertDialog.setIcon(R.drawable.delete);
 
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
 
            	try{
        			SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
        			
        			String sql = "delete from conta where _id = "+
        					getIntent().getStringExtra("conta_id")+
        					";";
        			
        			db.execSQL(sql);
        			db.close();
        			
        			Toast.makeText(getBaseContext(), "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();
        			finish();
        			
        		}catch(Exception e){
        			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        		}
            }
        });
 
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
 
        alertDialog.show();		
		
	}


}
