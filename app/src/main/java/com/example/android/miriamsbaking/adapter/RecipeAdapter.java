package com.example.android.miriamsbaking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final Context context;
    private List<Recipe> recipes;
    private final RecipeClickListener listener;


    public RecipeAdapter(Context context, RecipeClickListener listener){
        this.context = context;
        this.listener = listener;
    }



    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.tvRecipeName.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        else{
            return recipes.size();
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public interface RecipeClickListener{
        void onRecipeClicked(Recipe recipe);
        void onRecipeOptionTouched(int recipeId,View view);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_recipe)
        TextView tvRecipeName;
        @BindView(R.id.iv_option)
        ImageView ivOptions;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            ivOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRecipeOptionTouched(recipes.get(getAdapterPosition()).getId(),v);
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onRecipeClicked(recipes.get(getAdapterPosition()));
        }
    }
}
