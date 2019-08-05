package com.akp.tabir;

import com.akp.tabir.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomTabirAdapter extends SimpleCursorAdapter {
    private int layout;
    public CustomTabirAdapter(Context context, int layout, 
    Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
    }
    @Override
    public View newView(Context context, Cursor cursor, 
    ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);
        return v;
        
    }
    @Override
    public void bindView(View v, Context context, Cursor c) {
    	try
    	{
        int MeaningColIndex = c.getColumnIndex("Meaning");
        String MeaningCol = c.getString(MeaningColIndex);

        int WordColIndex = c.getColumnIndex("Word");
        String WordCol = c.getString(WordColIndex);

        int CodeColIndex = c.getColumnIndex("_id");
        String CodeCol = c.getString(CodeColIndex);

        
        if(MeaningCol.length() > 50)
        {
        	int BlankIndex = MeaningCol.indexOf(" ", 50);
        	if(BlankIndex != -1)
        		MeaningCol = MeaningCol.substring(0, BlankIndex) + "...";
        }
        MeaningCol = MeaningCol.replace("\n", "");	
        
        	
        // FIND THE VIEW AND SET THE NAME


        TextView Meaning_text = (TextView) v.findViewById(R.id.ShortMeaning_entry);
        Meaning_text.setText(MeaningCol);
        
        TextView Word_text = (TextView) v.findViewById(R.id.Word_entry);
        Word_text.setText(WordCol);        
        Word_text.setTextColor(Color.parseColor("#C11818"));

        
		Typeface BYekanFont = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/byekan.ttf");
		Meaning_text.setTypeface(BYekanFont);
		Word_text.setTypeface(BYekanFont);

        
        TextView Code_text = (TextView) v.findViewById(R.id.Code_entry);
        Code_text.setText(CodeCol);
        Code_text.setVisibility(View.INVISIBLE);
    	}
    	catch(Exception err)
    	{
    	
    	}

    }
}