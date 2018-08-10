package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sarthak.Sharma on 02-05-2018.
 */

public class TextViewOpenSansSemibold extends TextView {

    public TextViewOpenSansSemibold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf"));
    }

}
