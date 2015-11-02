package com.example.smartcomplaint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class homeActivity extends Activity implements OnClickListener{

	Button btLogin,btRegister;
	Intent intent;
	String activity="";
	final static int  LOGIN=1234;
	final static int  REGISTER=5678;
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		


	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);	
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 Log.d("tag", "called homeActivity");
		 activity=this.getIntent().getStringExtra("calledBy");
		intialize();
		setOnclickListner();
	}

	
	//set onclick listner
	private void setOnclickListner() {
		btLogin.setOnClickListener(this);
		btRegister.setOnClickListener(this);
	}


	
	private void intialize() {
		btLogin=(Button)findViewById(R.id.btLogin);
		btRegister=(Button)findViewById(R.id.btRegister);
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()){
		  
		  case R.id.btLogin:
			  intent= new Intent(homeActivity.this,loginActivity.class);
			  this.startActivityForResult(intent,LOGIN);

			  break;
		  case R.id.btRegister:
			   intent= new Intent(homeActivity.this,registrationActivity.class);
			  this.startActivityForResult(intent, REGISTER);
			  break;
		
		
		}
		
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode){

			case LOGIN :
				if(resultCode==this.RESULT_OK){
					Intent intent=new Intent(this,dashboardActivity.class);
					startActivity(intent);
					finish();
				}
				break;
			case REGISTER:
				if(resultCode==this.RESULT_OK){
					Intent intent=new Intent(this,dashboardActivity.class);
					startActivity(intent);
					finish();
				}
				break;


		}

	}

}
