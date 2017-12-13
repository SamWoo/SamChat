package samwoo.samchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import samwoo.samchat.R;
import samwoo.samchat.model.ContactItemModel;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<ContactItemModel> mContactList;
    private OnItemClickedListener listener;

    public ContactAdapter(Context context, List<ContactItemModel> list) {
        this.context = context;
        this.mContactList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ContactItemModel item = mContactList.get(position);
        holder.mName.setText(item.name);
//        Glide.with(context).load(mContactList.get(position)).centerCrop().into(holder.mAvatar);
        holder.itemView.setTag(position);

        if (item.showFirstLetter) {
            holder.mSection.setVisibility(View.VISIBLE);
            holder.mSection.setText(item.getFirstLetterString());
        } else {
            holder.mSection.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClicked(item.name);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClicked(item.name);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList == null ? 0 : mContactList.size();
    }

    public List<ContactItemModel> getContactItemModels() {
        return mContactList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_avatar)
        ImageView mAvatar;
        @BindView(R.id.contact_name)
        TextView mName;
        @BindView(R.id.section)
        TextView mSection;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static interface OnItemClickedListener {
        void onClicked(String name);

        void onLongClicked(String name);
    }

    public void setOnItemClicked(OnItemClickedListener listener) {
        this.listener = listener;
    }
}
