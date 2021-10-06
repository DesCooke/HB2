package com.example.cooked.hb2.helper;

import android.graphics.Canvas;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.cooked.hb2.GlobalUtils.MyLog;

public class DragItemTouchHelper extends ItemTouchHelper.Callback {

    private static final float ALPHA_FULL = 1.0f;

    private final MoveHelperAdapter mAdapter;

    public DragItemTouchHelper(MoveHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        MyLog.WriteLogMessage("DragItemTouchHelper:getMovementFlags:Starting");
        int lv_retcode=0;
        try
        {
            // Set movement flags based on the layout manager
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
            {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                lv_retcode=makeMovementFlags(dragFlags, swipeFlags);
            } else
            {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                lv_retcode=makeMovementFlags(dragFlags, swipeFlags);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("DragItemTouchHelper:getMovementFlags:Ending");
        return(lv_retcode);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target)
    {
        MyLog.WriteLogMessage("DragItemTouchHelper:onMove:Starting");
        try
        {
            if (source.getItemViewType() != target.getItemViewType())
            {
                MyLog.WriteLogMessage("DragItemTouchHelper:onMove:Ending");
                return false;
            }

            // Notify the adapter of the move
            mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("DragItemTouchHelper:onMove:Ending");
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        MyLog.WriteLogMessage("DragItemTouchHelper:onChildDraw:Starting");
        try
        {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
            {
                // Fade out the view as it is swiped out of the parent's bounds
                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else
            {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("DragItemTouchHelper:onChildDraw:Ending");
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)
    {
        MyLog.WriteLogMessage("DragItemTouchHelper:onSelectedChanged:Starting");
        try
        {
            // We only want the active item to change
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            {
                if (viewHolder instanceof TouchViewHolder)
                {
                    // Let the view holder know that this item is being moved or dragged
                    TouchViewHolder itemViewHolder = (TouchViewHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }

            super.onSelectedChanged(viewHolder, actionState);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("DragItemTouchHelper:onSelectedChanged:Ending");
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        super.clearView(recyclerView, viewHolder);
        MyLog.WriteLogMessage("DragItemTouchHelper:clearView:Starting");
        try
        {

            viewHolder.itemView.setAlpha(ALPHA_FULL);

            if (viewHolder instanceof TouchViewHolder)
            {
                // Tell the view holder it's time to restore the idle state
                TouchViewHolder itemViewHolder = (TouchViewHolder) viewHolder;
                itemViewHolder.onItemClear();
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        MyLog.WriteLogMessage("DragItemTouchHelper:clearView:Ending");
    }

    public interface MoveHelperAdapter {
        void onItemMove(int fromPosition, int toPosition);
    }

    public interface TouchViewHolder {
        void onItemSelected();

        void onItemClear();
    }
}
