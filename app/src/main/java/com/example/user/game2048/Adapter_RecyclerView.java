package com.example.user.game2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import me.grantland.widget.AutofitHelper;

/**
 * Created by USER on 01/02/2018.
 */

public class Adapter_RecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    // Danh sách lưu trữ các ô và điểm của chúng
    private List<DataGame> dataGameList;

    public Adapter_RecyclerView(Context context, List<DataGame> dataGameList) {
        this.context = context;
        this.dataGameList = dataGameList;
    }

    // Khi tạo các ViewHolder cho RecyclerView
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    // Khi hiển thị cho ViewHolder tại vị trí thứ position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataGame item = dataGameList.get(position);
        // nếu điểm của ô bằng 0 thì không hiển thị gì
        if (item.getDiemSo() == 0)
            viewHolder.textView_hinhVuong.setText("");
        // ngược lại hiển thị số điểm
        else
            viewHolder.textView_hinhVuong.setText(String.valueOf(item.getDiemSo()));

        // lấy đối tượng background nền của ô
        // và set màu sắc cho nó dựa theo số điểm nó mang
        GradientDrawable drawable = (GradientDrawable) viewHolder.textView_hinhVuong.getBackground();
        drawable.setColor(item.getColor());
        viewHolder.textView_hinhVuong.setBackground(drawable);

        // set hiển thị cho ô mới tạo số ngẫu nhiên
        // con số sẽ có màu đỏ
        if (item.isNew()) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_insert_item);
            viewHolder.textView_hinhVuong.startAnimation(animation);
            viewHolder.textView_hinhVuong.setTextColor(context.getResources().getColor(R.color.colorRed));
            item.setNew(false);
        }
        // ngược lại con số sẽ có màu đen
        else {
            viewHolder.textView_hinhVuong.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }





    }

    // Get số lượng các ô
    @Override
    public int getItemCount() {
        return dataGameList.size();
    }

    // Khai báo ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView_HinhVuong textView_hinhVuong;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_hinhVuong = itemView.findViewById(R.id.textView_hinhVuong);
            AutofitHelper.create(textView_hinhVuong);
        }
    }
}
