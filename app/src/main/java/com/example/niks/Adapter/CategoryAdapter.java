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
import com.example.niks.Listner.CategoryItemClickListner;
import com.example.niks.Model.Category;
import com.example.niks.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Category> listCategory;
    CategoryItemClickListner categoryItemClickListner;

    public CategoryItemClickListner getCategoryItemClickListner() {
        return categoryItemClickListner;
    }

    public void setCategoryItemClickListner(CategoryItemClickListner categoryItemClickListner) {
        this.categoryItemClickListner = categoryItemClickListner;
    }

    public CategoryAdapter(Context context, ArrayList<Category> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context= viewGroup.getContext();
        View mView=LayoutInflater.from(context).inflate(R.layout.row_griditem,viewGroup,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {

        Category category=listCategory.get(i);
        String name= category.getCat_name();
        viewHolder.tvCategory.setText(name);
        Glide.with(context).load(WebURL.KEY_CAT_IMAGE_URL+category.getCat_image()).into(viewHolder.ivCategory);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryItemClickListner listner = getCategoryItemClickListner();
                listner.setOnItemClicked(listCategory,i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;
        ImageView ivCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory=(TextView)itemView.findViewById(R.id.tvCategory);
            ivCategory = (ImageView)itemView.findViewById(R.id.ivCategory);


        }
    }
}
