package com.example.cooked.hb2.Database;

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

}