package br.com.elenildo.orcabr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class OpcoesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcoes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.opcoes, menu);
		return true;
	}
	
	public void listarContas(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), ListarContasActivity.class);
		startActivity(intent);
	}
	
	public void abrirTelaBackup(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), BackupActivity.class);
		startActivity(intent);
	}
	
	public void abrirTelaSenha(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), SenhaActivity.class);
		startActivity(intent);
	}
	
	public void exportEmailInCSV(View view) throws IOException {
	    {
	    	
	        File folder = new File(Environment.getExternalStorageDirectory() + "/orcabr");

	        boolean var = false;
	        if (!folder.exists())
	            var = folder.mkdirs();

	        System.out.println("" + var);

	        final String filename = folder.toString() + "/" + "lancamentos.csv";

	        new Thread() {
	            public void run() {
	                try {
	                	
	                    FileWriter fw = new FileWriter(filename);
	                    SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
	                    
	            		Cursor cursor = db.rawQuery("select r.id_tipo, r.data, r.valor, r.registrado, r.obs, c.descricao" +
	            				" from registro r, categoria c"+
	            				" where r.id_cat = c._id", null);
	            		
	                    fw.append("Tipo");
	                    fw.append(',');

	                    fw.append("Data");
	                    fw.append(',');
	                    
	                    fw.append("Valor");
	                    fw.append(',');
	                    
	                    fw.append("Registrado?");
	                    fw.append(',');
	                    
	                    fw.append("Observação");
	                    fw.append(',');
	                    
	                    fw.append("Categoria");
	                    fw.append(',');

	                    fw.append('\n');

	                    if (cursor.moveToFirst()) {
	                        do {
	                        	if(cursor.getString(0).equals("1"))
	                        		fw.append("Receita");
	                        	else
	                        		fw.append("Despesa" ) ;
	                            fw.append(',');

	                            fw.append(cursor.getString(1));
	                            fw.append(',');

	                            fw.append(cursor.getString(2));
	                            fw.append(',');
	                            
	                            if(cursor.getString(3).equals("0"))
	                        		fw.append("Não");
	                        	else
	                        		fw.append("Sim" ) ;
	                            fw.append(',');
	                            
	                            fw.append(cursor.getString(4));
	                            fw.append(',');
	                            
	                            fw.append(cursor.getString(5));
	                            fw.append(',');

	                            fw.append('\n');

	                        } while (cursor.moveToNext());
	                    }
	                    if (cursor != null && !cursor.isClosed()) {
	                        cursor.close();
	                    }

	                    fw.close();    

	                } catch (Exception e) {
	                }

	            }
	        }.start();
	        Toast.makeText(this, "Planilha criada com sucesso em " + filename, Toast.LENGTH_LONG).show();
	    }

	}
	
	
}
