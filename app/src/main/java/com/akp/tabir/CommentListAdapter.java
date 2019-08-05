package com.akp.tabir;

import java.util.List;

import com.akp.tabir.R;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListAdapter extends ArrayAdapter<CommentObject> {
	public CommentListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
    }

    private List<CommentObject> items;
    
    public CommentListAdapter(Context context, int resource, List<CommentObject> items) {

        super(context, resource, items);

        this.items = items;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	//System.out.println("getView " + position + " " + convertView);
        //View v = convertView;

        TextView txtName = null;
        TextView txtComment = null;
        TextView txtSendDate = null;
        TextView txtCode = null;

        if (convertView == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.comment_list_item, null);

            //convertView = activity.getLayoutInflater().inflate(R.layout.itemlistrow, null);
            	
        }
        
        txtName = (TextView) convertView.findViewById(R.id.tvName);
        txtComment = (TextView) convertView.findViewById(R.id.tvComment);
        txtSendDate = (TextView) convertView.findViewById(R.id.tvSendDate);
        txtCode = (TextView) convertView.findViewById(R.id.Code_entry);
        
        CommentObject p = items.get(position);
        convertView.setTag(p);
        
        if (p != null) {

            if (txtCode != null) {
            	txtCode.setText("" + p.GetCode());
            }
            if (txtName != null) {
                txtName.setText(""+p.GetName());
            }
            if (txtSendDate != null) {

                txtSendDate.setText(""+p.GetSendDate());
            }
            if (txtComment != null) {

                txtComment.setText(""+p.GetComment());
            }
        }
        else
        {
        	convertView.getTag(position);
        }

        return convertView;

    }

}
