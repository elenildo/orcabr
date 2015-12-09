package br.com.elenildo.orcabr;

import br.com.elenildo.entidades.SessionUser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SenhaActivity extends Activity {
	CheckBox chk_protect;
	EditText edt_paswd, edt_confirm, edt_email;
	Button btn_save, btn_cancel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_senha);
		
		chk_protect = (CheckBox) findViewById(R.id.chk_protect_paswd);
		edt_paswd = (EditText) findViewById(R.id.edt_password);
		edt_confirm = (EditText) findViewById(R.id.edt_confirm_paswd);
		edt_email = (EditText) findViewById(R.id.edt_email_paswd);
		btn_save = (Button) findViewById(R.id.btn_save_paswd);
		btn_cancel = (Button) findViewById(R.id.btn_cancel_paswd);
		
		SessionUser usr = SessionUser.getInstance();
		
		if(usr != null){
			if(usr.getProtect() != 0) chk_protect.setChecked(true);
			edt_paswd.setText(usr.getPaswd());
			edt_confirm.setText(usr.getPaswd());
			edt_email.setText(usr.getEmail());
			edt_paswd.setEnabled(true);
			edt_confirm.setEnabled(true);
			edt_email.setEnabled(true);
		}
			
		chk_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					edt_paswd.setEnabled(true);
					edt_paswd.setHint(R.string.edt_pasword_hint);
					edt_confirm.setEnabled(true);
					edt_confirm.setHint(R.string.edt_confirm_paswd_hint);
					edt_email.setEnabled(true);
					edt_email.setHint(R.string.edt_email_hint);
				}else{
					edt_paswd.setEnabled(false);
					edt_paswd.setHint("");
					edt_confirm.setEnabled(false);
					edt_confirm.setHint("");
					edt_email.setEnabled(false);
					edt_email.setHint("");
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.senha, menu);
		return true;
	}
	
	public void salvarSenha(View view){
		String msg=null;
		SessionUser usr = SessionUser.getInstance();
		
		if(edt_paswd.getText().toString().equals("")){
			msg = "Digite uma senha!";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}else if(! edt_confirm.getText().toString().equals(edt_paswd.getText().toString())){
			msg = "As senhas precisam ser iguais!";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}else if(edt_email.getText().toString().equals("")){
			msg = "Digite um e-mail válido!";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}else{
			
			if(usr != null){
				usr.setEmail(edt_email.getText().toString());
				usr.setPaswd(edt_paswd.getText().toString());
				usr.setProtect(chk_protect.isChecked()?1:0);
				try{
					SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
					
					String sql = "update usuario SET senha = '"+ usr.getPaswd() +
							"' , email = '"+ usr.getEmail() + 
							"', protegido = " + usr.getProtect() +
							" where _id = 1;";
							
					db.execSQL(sql);
					db.close();
					msg = "Senha alterada com sucesso!";
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();	
				}catch(Exception e){
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}		
		}
		
	}
	
	public void cancelarEdicao(View view){
		finish();
	}

}
