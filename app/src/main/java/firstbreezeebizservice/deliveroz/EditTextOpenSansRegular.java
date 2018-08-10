package firstbreezeebizservice.deliveroz;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Sarthak.Sharma on 09-05-2018.
 */

public class EditTextOpenSansRegular extends EditText {

    public EditTextOpenSansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf"));
    }

}
