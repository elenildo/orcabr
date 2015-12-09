package br.com.elenildo.orcabr;

import br.com.elenildo.entidades.Mail;
import br.com.elenildo.entidades.SessionUser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	EditText edt_login;
	Button btn_login, btn_esqueci;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		preparaBD();
		
		edt_login = (EditText) findViewById(R.id.edt_login_active);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_esqueci = (Button) findViewById(R.id.btn_sendmail);
		
		try{
			SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);
	
	        Cursor cursor = db.rawQuery("select * from usuario", null);
	        cursor.moveToFirst();
	        
	    	final SessionUser usr = SessionUser.getInstance();
	    	
	    	usr.setPaswd(cursor.getString(cursor.getColumnIndex("senha")));
	    	usr.setEmail(cursor.getString(cursor.getColumnIndex("email")));
	    	usr.setProtect(cursor.getInt(cursor.getColumnIndex("protegido")));
	    	
	    	if(usr.getProtect() == 0){
	    		abrirTelaInicial(getCurrentFocus());
	    		finish();
	    	}else{
	    		btn_login.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(edt_login.getText().toString().equals(usr.getPaswd())){
							abrirTelaInicial(getCurrentFocus());
				    		finish();
//				    		System.out.println(usr.getPaswd());
//							System.out.println(usr.getEmail());
//							System.out.println(usr.getProtect());
						}else{
							Toast.makeText(getBaseContext(), "Senha incorreta!", Toast.LENGTH_LONG).show();
						}
					}
				});
	    		
	    	}
	        
	        db.close();
		}catch(Exception e){
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void preparaBD(){

        try {
            SQLiteDatabase db = openOrCreateDatabase("orcabd.db", Context.MODE_PRIVATE, null);

            String sql = "create table if not exists categoria( _id integer primary key, id_tipo integer, descricao varchar(30));";
            db.execSQL(sql);

            Cursor cursor = db.rawQuery("select _id from categoria", null);
            cursor.moveToFirst();
            
            if (! cursor.moveToNext()) {
                sql = "insert into categoria(id_tipo, descricao) values(1,'Salário')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(1,'Prêmio')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(1,'PLR')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(2,'Compra')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(2,'Aluguel')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(2,'Conta de luz')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(2,'Combustível')";
                db.execSQL(sql);
                sql = "insert into categoria(id_tipo, descricao) values(2,'Viagem')";
                db.execSQL(sql);
            }
            
    		
    		sql = "create table if not exists conta( _id integer primary key, nome varchar(20), saldo double);";
    		db.execSQL(sql);
    		
    		sql = "create table if not exists registro( _id integer primary key," +
    		" id_cat integer," +
    		" id_tipo integer,"+
    		" data date,"+
    		" valor double,"+
    		" registrado integer,"+
    		" obs varchar(50));";
    		db.execSQL(sql);
    		
    		
    		sql = "create table if not exists usuario( _id integer primary key, " +
            		"protegido integer, " +
            		"senha varchar(30) not null, " +
            		"email varchar(60));";
            db.execSQL(sql);
            
            
            sql = "create table if not exists balanco( _id integer primary key, " +
            		"data date, " +
            		"receitas double, " +
            		"despesas double, " +
            		"resultado double);";
            db.execSQL(sql);
            
            Cursor usr_cursor = db.rawQuery("select * from usuario", null);
            cursor.moveToFirst();
            
            if (! usr_cursor.moveToNext()) {
                sql = "insert into usuario(_id, protegido, senha, email) values(1, 0, '', '')";
                db.execSQL(sql);
            }
            db.close();
            
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
	public void abrirTelaInicial(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), MainActivity.class);
		startActivity(intent);
		
	}
	public void enviarEmail(View view){
		
		/*
		Mail m = new Mail("elenildoms@gmail.com", "elen_goo");
		
		String[] toArr = {"elenildosantos@yahoo.com.br", "elenildoms@gmailcom"}; 
	    m.set_to(toArr); 
	    m.set_from("elenildoms@gmailcom"); 
	    m.set_subject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
	    m.set_body("Email body."); 
	 
	    try { 
//	        m.addAttachment("/sdcard/filelocation"); 
	 
	       if(m.send()) { 
	          Toast.makeText(getBaseContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
	        } else { 
	          Toast.makeText(getBaseContext(), "Email was not sent.", Toast.LENGTH_LONG).show(); 
	        } 
	    } catch(Exception e) { 
	        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show(); 
	    }
	    */
		
	    Toast.makeText(getBaseContext(), getString(R.string.msg_pass_sent), Toast.LENGTH_LONG).show(); 
	}
	

}
