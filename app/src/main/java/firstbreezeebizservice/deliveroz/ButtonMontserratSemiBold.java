package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Sarthak.Sharma on 02-05-2018.
 */

public class ButtonMontserratSemiBold extends Button {

    public ButtonMontserratSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Montserrat-SemiBold.otf"));
    }
}
