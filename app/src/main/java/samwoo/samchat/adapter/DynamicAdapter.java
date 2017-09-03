package samwoo.samchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.R;
import samwoo.samchat.model.DynamicItemModel;
import samwoo.samchat.widget.MultiImageView;

/**
 * Created by Administrator on 2017/9/2.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {
    private Context context;
    private List<DynamicItemModel> modelList = new ArrayList<DynamicItemModel>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_friend)
        ImageView imageView;
        @BindView(R.id.tv_friend_dynamic)
        TextView tv_dynamic;
        @BindView(R.id.tv_friend_name)
        TextView tv_name;
        @BindView(R.id.multi_image)
        MultiImageView multiImageView;
        @BindView(R.id.tv_comment)
        TextView tv_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DynamicAdapter(Context context, List<DynamicItemModel> mList) {
        this.context = context;
        this.modelList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.multiImageView.setList(modelList.get(position).getImages());
        Glide.with(context).load(modelList.get(position).getAvatarId()).into(holder.imageView);
//        holder.tv_dynamic.setText(modelList.get(position).getDynamicText());
        holder.tv_name.setText(modelList.get(position).getName());
        holder.tv_comment.setText("评论区........");

        holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float PressX, float PressY) {

            }

            @Override
            public void onItemLongClick(View view, int position, float PressX, float PressY) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList == null ? 0 : modelList.size();
    }
}
