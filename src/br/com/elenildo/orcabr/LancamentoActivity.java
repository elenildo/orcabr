package br.com.elenildo.orcabr;


import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LancamentoActivity extends Activity implements View.OnClickListener{
	private String tipo;
	private EditText txtId_categoria, txtObs, txtValor;
	private Button btnCat, btnCadastrar, btnExcluir;
	private CheckBox chStatus;
	private String registroId;
	
	DateFormat formato = DateFormat.getDateInstance();
	Calendar calendar = Calendar.getInstance();
	private Button btnData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_lancamento);
		txtId_categoria = (EditText) findViewById(R.id.txt_Id_categoria);
		btnData = (Button) findViewById(R.id.btn_Data);
		btnCadastrar = (Button)findViewById(R.id.btn_cadastrar_lancamento);
		btnExcluir = (Button)findViewById(R.id.btn_excluir_reg);
		txtObs = (EditText) findViewById(R.id.txtObs);
		txtValor = (EditText) findViewById(R.id.txtValor);
		chStatus = (CheckBox) findViewById(R.id.chkRegistrado);
		btnCat = (Button) findViewById(R.id.btnCategorias);
		
		btnData.setOnClickListener(this);
		chStatus.setChecked(true);
		
		atualizarData();
		
		TextView lblTitulo = (TextView) findViewById(R.id.lbl_opcoes_titulo);
		
		if(getIntent().hasExtra("titulo")){
			lblTitulo.setText(getIntent().getStringExtra("titulo"));
		}
		
		if(getIntent().hasExtra("tipo")){
			tipo = getIntent().getStringExtra("tipo");
		}
		
		if(getIntent().hasExtra("op")){
			registroId = getIntent().getStringExtra("id_reg");
			btnData.setText(getIntent().getStringExtra("data_reg"));
			txtValor.setText(getIntent().getStringExtra("valor_reg"));
			txtObs.setText(getIntent().getStringExtra("obs_reg"));
			btnCat.setText(getIntent().getStringExtra("categoria_reg"));
			txtId_categoria.setText(getIntent().getStringExtra("id_cat_reg"));
			btnCadastrar.setText(getString(R.string.btn_update));
			btnExcluir.setVisibility(View.VISIBLE);
			
			if(getIntent().getStringExtra("status_reg").equals("1")) 
				chStatus.setChecked(true);
			else 
				chStatus.setChecked(false);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lancamento, menu);
		return true;
		
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setContentView(R.layout.activity_lancamento);
//	}
	
	public void ajustarData(){
		new DatePickerDialog(this, dialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void atualizarData(){
		btnData.setText(formato.format(calendar.getTime()));
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
	
	
	public void listarCategorias(View view){
		Intent intent = new Intent();
		
		intent.setClass(this, ListarCategoriaActivity.class);
		intent.putExtra("tipo", tipo);
		startActivityForResult(intent, 1);
	}
	
	public void cadastrarClick(View v){
		
		if(txtId_categoria.getText().toString().length() <= 0){
			Toast.makeText(getBaseContext(), getString(R.string.msg_no_category), Toast.LENGTH_SHORT).show();
		}else if(txtValor.getText().toString().length() <= 0){
			txtValor.setError("Preencha o campo Valor");
			txtValor.requestFocus();
		}else{
			try{
				SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
				
				ContentValues cv = new ContentValues();
				cv.put("id_cat", txtId_categoria.getText().toString());
				cv.put("id_tipo", tipo);
				cv.put("data", btnData.getText().toString());
				cv.put("valor", txtValor.getText().toString());
				cv.put("obs", txtObs.getText().toString());
				cv.put("registrado", chStatus.isChecked());
				
				if(! getIntent().hasExtra("op")){
					if(db.insert("registro", "_id", cv) > 0)
						Toast.makeText(getBaseContext(), getString(R.string.msg_success_insert), Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getBaseContext(), getString(R.string.msg_error_insert), Toast.LENGTH_SHORT).show();
				}else{
					String sql = "update registro SET id_cat = "+ txtId_categoria.getText().toString() +
							" , id_tipo = "+ tipo+ 
							" , data = '"+ btnData.getText()+
							"' , valor = "+ txtValor.getText().toString()+
							" , registrado = "+ (chStatus.isChecked()?1:0)+
							" , obs = '" + txtObs.getText().toString()+
							"' where _id = " + registroId+
							";";
							
					db.execSQL(sql);
					Toast.makeText(getBaseContext(), getString(R.string.msg_success_update), Toast.LENGTH_SHORT).show();
					finish();
					
				}
				
				db.close();
				finish();
				
			}catch(Exception e){
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		ajustarData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			if(resultCode == RESULT_OK){
				txtId_categoria.setText(data.getStringExtra("id_category"));
				btnCat.setText(data.getStringExtra("categoria"));
			}
		}
		
		
	}
	
	public void excluirRegistro(View view){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		 
        alertDialog.setTitle(getString(R.string.msg_confirm_delete_title));
        alertDialog.setMessage(getString(R.string.msg_confirm_delete));
//        alertDialog.setIcon(R.drawable.delete);
 
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
 
            	try{
        			SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
        			
        			String sql = "delete from registro where _id = "+
        					getIntent().getStringExtra("id_reg")+
        					";";
        			
        			db.execSQL(sql);
        			db.close();
        			
        			Toast.makeText(getBaseContext(), getString(R.string.msg_deleted_sucess), Toast.LENGTH_SHORT).show();
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
