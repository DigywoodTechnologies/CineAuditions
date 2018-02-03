package com.digywood.cineauditions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleSubCategory;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {

    boolean[] checkBoxState;
    DBHelper dbHelper;
    String subCategory,category;
    private Context mContext;
    private ArrayList<String> SubCategoryNamesList = new ArrayList<String>();
    private ArrayList<String> SubCategoryCheckedList = new ArrayList<String>();
    ArrayList<SingleSubCategory> SubCategoryList = new ArrayList<SingleSubCategory>();
    ArrayList<SingleCategory> CategoryList = new ArrayList<SingleCategory>();
    ArrayList<CategoryCheck> CategoryCheckedList = new ArrayList<CategoryCheck>();
    //private final String[] web;
    //private final int[] Imageid;

    public CustomGrid(Context c, ArrayList<String> SubCategoryNamesList ) {
        mContext = c;
        //this.Imageid = Imageid;
        this.SubCategoryNamesList = SubCategoryNamesList;
        dbHelper = new DBHelper(c);
        CategoryList = dbHelper.getAllCategories();
        SubCategoryList = dbHelper.getAllSubCategories();
        checkBoxState=new boolean[SubCategoryList.size()];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return SubCategoryNamesList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.custom_pref_grid, null);
            TextView textView = (TextView) grid.findViewById(R.id.prefcat_list);
            CheckBox checkBox = (CheckBox)grid.findViewById(R.id.checkbox_preflist);
            //ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(SubCategoryNamesList.get(position));
            //imageView.setImageResource(Imageid[position]);

            checkBox.setChecked(checkBoxState[position]);

            checkBox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        checkBoxState[position] = true;

                        SubCategoryCheckedList.add(SubCategoryNamesList.get(position));

                        subCategory = SubCategoryNamesList.get(position);

                        for(int i = 0;i < SubCategoryList.size();i++){
                            if(subCategory.equals(SubCategoryList.get(i).getLongName())){
                                for(int j = 0;j < CategoryList.size();j++){
                                    if(SubCategoryList.get(i).getCategoryId().equals(CategoryList.get(j).getCategoryId())){
                                        category =  CategoryList.get(j).getLongName();
                                    }
                                }
                            }
                        }


                        Log.d("SubCategoryCheckedList", "" + SubCategoryCheckedList.size() + ":" + SubCategoryCheckedList);

                        dbHelper.insertCategoryCheck(category,SubCategoryNamesList.get(position),"created");
                        Log.d("insertAdvtCategory", "" + category + ":" + SubCategoryNamesList.get(position));
                        CategoryCheckedList = dbHelper.getCategoriesChecked("created");

                        Log.d("AdvtCategoryList.Size", "" + CategoryCheckedList.size() );

                    } else {
                        checkBoxState[position] = false;

                        SubCategoryCheckedList.remove(SubCategoryNamesList.get(position));
                        Log.d("SubCategoryCheckedList", "" + SubCategoryCheckedList.size() + ":"
                                + SubCategoryCheckedList);

                        dbHelper.deleteCategoryCheck(SubCategoryNamesList.get(position));

                        CategoryCheckedList = dbHelper.getCategoriesChecked("created");

                        Log.d("AdvtCategoryList.Size", "" + CategoryCheckedList.size() );

                    }
                }
            });

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
