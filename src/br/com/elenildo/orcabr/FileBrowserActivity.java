package br.com.elenildo.orcabr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileBrowserActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	ListView LvList; 
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Button BtnCancelRestore;
    String currentPath = null;
    String selectedFilePath = null; /* Full path, i.e. /mnt/sdcard/folder/file.txt */
    String selectedFileName = null; /* File Name Only, i.e file.txt */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);
		
		try {
			
            LvList = (ListView) findViewById(R.id.LvList);
            BtnCancelRestore = (Button) findViewById(R.id.BtnCancelRestore);
                   
            LvList.setOnItemClickListener(this);            
            BtnCancelRestore.setOnClickListener(this);               
            setCurrentPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
            
        } catch (Exception ex) {
            Toast.makeText(this,
                    "Error in OpenFileActivity.onCreate: " + ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_browser, menu);
		return true;
	}
	
	void setCurrentPath(String path) {
		ArrayList<String> folders = new ArrayList<String>();
	    ArrayList<String> files = new ArrayList<String>();    
	    currentPath = path;
	        
        File[] allEntries = new File(path).listFiles();
        
        for (int i = 0; i < allEntries.length; i++) {
            if (allEntries[i].isDirectory()) {
                folders.add(allEntries[i].getName());
            } else if (allEntries[i].isFile() && allEntries[i].toString().endsWith(".db")) {
                files.add(allEntries[i].getName());
            }
        }
        
        Collections.sort(folders, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        
        Collections.sort(files, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        
        listItems.clear();
        
        for (int i = 0; i < folders.size(); i++) {
            listItems.add(folders.get(i) + "/");
        }
        
        for (int i = 0; i < files.size(); i++) {
            listItems.add(files.get(i));
        }
        
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        adapter.notifyDataSetChanged();
        
        LvList.setAdapter(adapter);
	        
	}
	
	public void onBackPressed(){
		if (!currentPath.equals(Environment.getExternalStorageDirectory().getAbsolutePath() + "/")) {
            setCurrentPath(new File(currentPath).getParent() + "/");
        } else {
            super.onBackPressed();
        }
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String entryName = (String)parent.getItemAtPosition(position); 
        if (entryName.endsWith("/")) {
            setCurrentPath(currentPath + entryName);
        } else {
            selectedFilePath = currentPath + entryName;
            selectedFileName = entryName;
            
            this.setTitle(this.getResources().getString(R.string.title_activity_file_browser)
                    + "[" + entryName + "]");
            
            restoreDB(selectedFilePath);
            
        }
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
        
        switch (v.getId()) {
//        case R.id.BtnOK:
//            
//            intent = new Intent();
//            intent.putExtra("fileName", selectedFilePath);
//            intent.putExtra("shortFileName", selectedFileName);
//            setResult(RESULT_OK, intent);
//            
//            this.finish();
//            
//            break;
        case R.id.BtnCancelRestore:
            
            intent = new Intent();
            intent.putExtra("fileName", "");
            intent.putExtra("shortFileName", "");
            setResult(RESULT_CANCELED, intent);
            
            this.finish();
            
            break;
        }
		
	}
	
	public void restoreDB(String originBackupPath){
	    
	    String destinationBackupPath = getApplicationContext().getDatabasePath("orcabd.db").getPath();
	    FileInputStream inStream;
		FileOutputStream outStream;
		
		try {
			inStream = new FileInputStream(originBackupPath);
			outStream = new FileOutputStream(destinationBackupPath);
			FileChannel inChannel = inStream.getChannel();
		    FileChannel outChannel = outStream.getChannel();
		    inChannel.transferTo(0, inChannel.size(), outChannel);
		    inStream.close();
		    outStream.close();
		    Toast.makeText(this, "Backup restaurado com sucesso!", Toast.LENGTH_LONG).show();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro ao restaurar o backup!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro: "+ e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

}
