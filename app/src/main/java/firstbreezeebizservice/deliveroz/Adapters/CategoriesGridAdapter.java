package firstbreezeebizservice.deliveroz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import firstbreezeebizservice.deliveroz.R;

public class CategoriesGridAdapter extends BaseAdapter {

    Context context;
    List<String> arrCategoriesImages = new ArrayList<>();
    List<String> getArrCategoriesNames = new ArrayList<>();

    public CategoriesGridAdapter(Context context, List<String> arrCategoriesImages, List<String> getArrCategoriesNames) {
        this.context = context;
        this.arrCategoriesImages = arrCategoriesImages;
        this.getArrCategoriesNames = getArrCategoriesNames;
    }

    @Override
    public int getCount() {
        return arrCategoriesImages.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.categories_items, viewGroup, false);

        ImageView imgCategoryImage = convertView.findViewById(R.id.imgCategoryImage);
        TextView lblCategoryName = convertView.findViewById(R.id.lblCategoryName);

        Picasso.with(context).load(arrCategoriesImages.get(i)).into(imgCategoryImage);

        lblCategoryName.setText(getArrCategoriesNames.get(i));

        return convertView;
    }
}
