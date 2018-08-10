package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Sarthak.Sharma on 08-05-2018.
 */

public class EditTextOpenSansBold extends EditText {

    public EditTextOpenSansBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Bold.ttf"));
    }
}
