package com.example.cooked.hb2.Database;

import android.widget.ImageButton;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecordButton
{
    public int buttonId;
    public int button;
    public String buttonText;
    public boolean selected;

    public RecordButton
            (
                    int pButtonId,
                    int pButton,
                    String pButtonText
            )
    {
        buttonId = pButtonId;
        button = pButton;
        buttonText = pButtonText;
        selected = false;
    }

    public RecordButton()
    {
        buttonId = 0;
        button = 0;
        buttonText = "";
        selected = false;
    }
}