package com.example.niks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Listner.SubCategoryItemClickListner;
import com.example.niks.Model.SubCategory;
import com.example.niks.R;

import java.util.ArrayList;

public class SubCategoryAdapter  extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<SubCategory> listSubCategory;
    SubCategoryItemClickListner subCategoryItemClickListner;

    public SubCategoryItemClickListner getSubCategoryItemClickListner() {
        return subCategoryItemClickListner;
    }

    public void setSubCategoryItemClickListner(SubCategoryItemClickListner subCategoryItemClickListner) {
        this.subCategoryItemClickListner = subCategoryItemClickListner;
    }



    public SubCategoryAdapter(Context context, ArrayList<SubCategory> listSubCategory) {
        this.context = context;
        this.listSubCategory = listSubCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.subcategory_row_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        SubCategory subCategory = listSubCategory.get(i);
        String name =  subCategory.getSubcat_name();
        viewHolder.tvSubCategory.setText(name);
        Glide.with(context).load(WebURL.KEY_SUBCAT_IMAGE_URL+subCategory.getSubcat_image()).into(viewHolder.ivSubCategory);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubCategoryItemClickListner listner = getSubCategoryItemClickListner();
                listner.setOnSubcatClicked(listSubCategory,i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listSubCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivSubCategory;
        TextView tvSubCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSubCategory = itemView.findViewById(R.id.ivSubCategory);
            tvSubCategory = itemView.findViewById(R.id.tvSubCategory);
        }
    }
}
