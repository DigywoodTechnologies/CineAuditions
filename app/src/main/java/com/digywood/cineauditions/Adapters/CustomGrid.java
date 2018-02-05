package com.digywood.cineauditions.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.digywood.cineauditions.CategoryCheck;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleSubCategory;
import com.digywood.cineauditions.R;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {

    boolean[] checkBoxState;
    DBHelper dbHelper;
    String subCategory,category;
    private Context mContext;
    ArrayList<String> checkList=new ArrayList<>();
    private ArrayList<String> SubCategoryNamesList = new ArrayList<>();
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
            final CheckBox checkBox = (CheckBox)grid.findViewById(R.id.checkbox_preflist);

            if(checkList.size()!=0){
                if(checkList.contains(SubCategoryNamesList.get(position))){
                    textView.setText(SubCategoryNamesList.get(position));

                    checkBox.setChecked(true);
                }else{
                    textView.setText(SubCategoryNamesList.get(position));

                    checkBox.setChecked(false);
                }
            }else{

                textView.setText(SubCategoryNamesList.get(position));

                checkBox.setChecked(false);

            }
//            textView.setText(SubCategoryNamesList.get(position));
//
//            checkBox.setChecked(false);

            checkBox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        checkBoxState[position] = true;

                        if(checkList.size()!=0){
                            if(checkList.contains(SubCategoryNamesList.get(position))){

                            }else{
                                checkList.add(SubCategoryNamesList.get(position));
                            }
                        }else{
                            checkList.add(SubCategoryNamesList.get(position));
                        }


                    } else {
                        checkBoxState[position] = false;

                        if(checkList.size()!=0){
                            if(checkList.contains(SubCategoryNamesList.get(position))){
                                checkList.remove(SubCategoryNamesList.get(position));
                            }else{

                            }
                        }else{

                        }

                    }
                }
            });

        } else {
            grid = (View) convertView;
        }
        return grid;
    }

    public void updateGrid(ArrayList<String> newList){
        SubCategoryNamesList.clear();
        SubCategoryNamesList=newList;
    }

    public ArrayList<String> getChkList(){
        return checkList;
    }

}
