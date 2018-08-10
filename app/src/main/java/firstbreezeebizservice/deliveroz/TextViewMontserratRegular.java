package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sarthak.Sharma on 03-05-2018.
 */

public class TextViewMontserratRegular extends TextView {

    public TextViewMontserratRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.otf"));
    }

}
