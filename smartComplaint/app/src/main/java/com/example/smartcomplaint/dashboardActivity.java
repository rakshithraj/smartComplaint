package com.example.smartcomplaint;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class dashboardActivity extends Activity implements OnClickListener{



	  @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        // Inflate the menu; this adds items to the action bar if it is present.  
	        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu  
	        return true;  
	    }  


	Button btTraffic,btPolice,btMescom,btMNPO,btStatus,btAboutUs,btLogout;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);	
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_page);
		intialize();
		
	}

	private void intialize() {
		// TODO Auto-generated method stub
		btTraffic=(Button)findViewById(R.id.btTraffic);
		btTraffic.setOnClickListener(this);
		btPolice=(Button)findViewById(R.id.btPolice);
		btPolice.setOnClickListener(this);
		btMescom=(Button)findViewById(R.id.btMescom);
		btMescom.setOnClickListener(this);
		btMNPO=(Button)findViewById(R.id.btMNPO);
		btMNPO.setOnClickListener(this);
		btStatus=(Button)findViewById(R.id.btStatus);
		btStatus.setOnClickListener(this);
		btAboutUs=(Button)findViewById(R.id.btAboutUs);
		btAboutUs.setOnClickListener(this);
		btLogout=(Button)findViewById(R.id.btLogout);
		btLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btTraffic:
			 intent=new Intent(dashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "traffictable");
			 startActivity(intent);
			
			break;
		case R.id.btPolice:
			 intent=new Intent(dashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "policetable");
			 startActivity(intent);
			
			break;
		case R.id.btMescom:
			 intent=new Intent(dashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "mescomtable");
			 startActivity(intent);
			
			break;
		case R.id.btStatus:
			 intent=new Intent(dashboardActivity.this,ComplaintStatus_Activity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
		case R.id.btMNPO:
			 intent=new Intent(dashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
		case R.id.btAboutUs:
			 intent=new Intent(dashboardActivity.this,aboutus_Activity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
			case R.id.btLogout:
				final SharedPreferences prefs = getSharedPreferences(lanuchActivity.class.getSimpleName(),this.getApplicationContext().MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("login", false);
				editor.commit();
				Intent intent=new Intent(this,homeActivity.class);
				this.startActivity(intent);
				finish();
				break;
		
		}
		
		
		
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs = getSharedPreferences(lanuchActivity.class.getSimpleName(),dashboardActivity.this.getApplicationContext().MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.clear();
	    editor.commit();
	    Intent intent=new Intent(dashboardActivity.this,lanuchActivity.class);
		startActivity(intent);
		finish();
		return super.onMenuItemSelected(featureId, item);
		
		
	}

}
