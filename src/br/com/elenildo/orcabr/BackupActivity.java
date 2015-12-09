package br.com.elenildo.orcabr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class BackupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backup, menu);
		return true;
	}
	
	public void exportDB(View view){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime()); //2015-08-12
	    
	    String currentDBPath = getApplicationContext().getDatabasePath("orcabd.db").getPath();
	    String backupDBPath = Environment.getExternalStorageDirectory() + "/orcabr/backup_"+ date + ".db";
		FileInputStream inStream;
		FileOutputStream outStream;
		
		try {
			inStream = new FileInputStream(currentDBPath);
			outStream = new FileOutputStream(backupDBPath);
			FileChannel inChannel = inStream.getChannel();
		    FileChannel outChannel = outStream.getChannel();
		    inChannel.transferTo(0, inChannel.size(), outChannel);
		    inStream.close();
		    outStream.close();
		    Toast.makeText(this, "Backup criado com sucesso em " + backupDBPath, Toast.LENGTH_LONG).show();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro ao criar o backup!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro: "+ e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
	
	public void restaurarBackup(View view){
		Intent intent = new Intent();
		
		intent.setClass(getBaseContext(), FileBrowserActivity.class);
		startActivity(intent);
	}

}
