package com.akp.tabir;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.tabir.DBHelper;
import com.akp.tabir.R;

public class SendTabir extends Activity{
	EditText MobileNumber;
	TextView Message;
	
	
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_send_sms);
	      
	      try
	      {
		      MobileNumber = (EditText) findViewById(R.id.txtMobileNUmber);
		      Message = (TextView) findViewById(R.id.txtMessage);
	
	
		      Bundle extras = getIntent().getExtras(); 
		      if(extras !=null)
		      {
		         String TabirVal = extras.getString("TabirMeaning");
		         
		         if(TabirVal != null){
		            //means this is the view part not the add contact part.
		        	 Message.setText(TabirVal);
		            
		           }
		      }
	      }
	      catch(Exception err)
	      {
	    	  Message = (TextView) findViewById(R.id.txtMessage);
	    	  Message.setText(err.getMessage());
	      }
	      
	      
	        Button btnSend = (Button) findViewById(R.id.btnSend);
	        
	        btnSend.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                try {
	                    SmsManager smsManager = SmsManager.getDefault();
	                    smsManager.sendTextMessage(MobileNumber.getText().toString(), null, Message.getText().toString(), null, null);
	                    Toast.makeText(getApplicationContext(), "پیامک ارسال شد",
	                    Toast.LENGTH_LONG).show();
	                    finish();
	                 } 
	                catch (Exception e) {
	                    Toast.makeText(getApplicationContext(),
	                    "خطا در ارسال پیامک",
	                    Toast.LENGTH_LONG).show();

	      	    	  Message = (TextView) findViewById(R.id.txtMessage);
	    	    	  Message.setText(e.getMessage());
	    	    	  
	                    e.printStackTrace();
	                    
	                    
	                 }
	                
	            }
	            
	        }); 	      

	   }
	
}
