package com.akp.tabir;

public class CommentObject {
    private int Code;
    private String Name;
    private String Comment;
    private String SendDate;
    
    public CommentObject (int Code, String Name, String Comment, String SendDate)
    {
    	this.Code = Code;
    	this.Name = Name;
    	this.Comment = Comment;
    	this.SendDate = SendDate;
    }
    public int GetCode()
    {
    	return this.Code;
    }
    public String GetName()
    {
    	return this.Name;
    }
    public String GetComment()
    {
    	return this.Comment;
    }
    public String GetSendDate()
    {
    	return this.SendDate;
    }

}
