package com.bhanchha.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

public class CustomCard extends Card {

    protected TextView cmTitleView;
    protected TextView cmBriefView;
    protected ImageView cmImageView;

    protected String cmTitle = null;
    protected String cmBrief = null;
    protected int cmImage = 0;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public CustomCard(Context context) {
        this(context, R.layout.custom_card);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public CustomCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card = Nothing", Toast.LENGTH_LONG).show();
                // Use interface here to communicate the click event back to parent activity
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve views
        cmTitleView = (TextView) parent.findViewById(R.id.cardTitle);
        cmBriefView = (TextView) parent.findViewById(R.id.cardBrief);
        cmImageView = (ImageView) parent.findViewById(R.id.cardImage);

        //Set Properties
        if (cmTitleView!=null)
            cmTitleView.setText(cmTitle);
        if (cmBriefView!=null)
            cmBriefView.setText(cmBrief);
        if (cmImageView!=null)
            cmImageView.setImageDrawable(parent.getResources().
                    getDrawable(cmImage));

        if (cmBrief == null) {
            cmBriefView.setHeight(0);
        }

    }

    public void cAddCardTitle(String cardTitle) {
        cmTitle = cardTitle;
    }

    public void cAddCardBrief(String cardBrief) {
        cmBrief = cardBrief;
    }

    public void cAddCardImage(int imageRes) {
        cmImage = imageRes;
    }
}
