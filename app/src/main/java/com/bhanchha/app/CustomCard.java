package com.bhanchha.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import it.gmariotti.cardslib.library.internal.Card;

public class CustomCard extends Card {

    protected TextView cmTitleView;
    protected TextView cmBriefView;
    protected ImageView cmImageView;
    protected ImageView cmImageOverlayView;

    protected String cmTitle = null;
    protected String cmBrief = null;
    protected String cmImage = null;
    protected int cmImageOverlay = View.GONE;
    protected String identifier = null;

    protected Context context;

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
        this.context = context;
        init();
    }

    /**
     * Init
     */
    private void init(){
        //No Header
        //Set a OnClickListener listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card = Nothing", Toast.LENGTH_SHORT).show();
//                // Use interface here to communicate the click event back to parent activity
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve views
        cmTitleView = (TextView) parent.findViewById(R.id.cardTitle);
        cmBriefView = (TextView) parent.findViewById(R.id.cardBrief);
        cmImageView = (ImageView) parent.findViewById(R.id.cardImage);
        cmImageOverlayView = (ImageView) parent.findViewById(R.id.cardImageOverlay);
        //Set Properties
        if (cmTitleView!=null)
            cmTitleView.setText(cmTitle);
        if (cmBriefView!=null)
            cmBriefView.setText(cmBrief);
        if (cmImageView!=null){
            //Picasso.with(context).setIndicatorsEnabled(true);
            Picasso.with(context)
                    .load(cmImage)
                    .resize(200, 200)
                    .centerCrop()
                    .placeholder(R.drawable.bhanchha_logo)
                    .error(R.drawable.bhanchha_logo)
                    .into(cmImageView);
        }
        cmImageOverlayView.setVisibility(cmImageOverlay);

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

    public void cAddCardImage(String imageRes) {
        cmImage = imageRes;
    }

    public void cSetOverlayVisibility(int visibility) {
        cmImageOverlay = visibility;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        this.setTitle(identifier);
    }

    public String getIdentifier() {
        return identifier;
    }
}
