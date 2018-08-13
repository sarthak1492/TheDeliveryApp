package sarthaksharma.theDeliveryApp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Sarthak.Sharma on 02-05-2018.
 */

public class EditTextOpenSansSemiBold extends EditText {

    public EditTextOpenSansSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf"));
    }

}
