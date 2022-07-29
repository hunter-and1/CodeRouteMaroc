package com.hunterand1.coderoutemaroc;

import java.util.ArrayList;

/**
 * Created by HunTerAnD1 on 01/08/2016.
 */
public class Serie {
    int Id;
    String Label;
    ArrayList<Question> Questions ;
    public Serie(int Id,String Label,ArrayList<Question> Questions)
    {
        this.Id = Id;
        this.Label = Label;
        this.Questions = Questions;
    }
}
