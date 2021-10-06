package com.example.cooked.hb2;

import java.util.ArrayList;
import java.util.List;

import com.example.cooked.hb2.Database.MyDatabase;
import com.example.cooked.hb2.Database.RecordSubCategory;
import com.example.cooked.hb2.GlobalUtils.ErrorDialog;
import com.example.cooked.hb2.GlobalUtils.MyLog;
import com.example.cooked.hb2.Widgets.AnimatedExpandableListView;
import com.example.cooked.hb2.Widgets.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * <p>
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class activityCategory2 extends AppCompatActivity
{
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    public ArrayList<RecordSubCategory> mDataset;
    public Switch swOld;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_category_list2);
            Toolbar toolbar = findViewById(R.id.toolbar);
            swOld = findViewById(R.id.swOld);

            setSupportActionBar(toolbar);

            setTitle("Categories");

            setupForm();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }

    }

    private void setupForm()
    {
        try
        {
            mDataset = MyDatabase.MyDB().getSubCategoryListWithOld(0, swOld.isChecked());

            List<CategoryItem2> items = new ArrayList<CategoryItem2>();

            // Populate our list with groups and it's children
            String lLastCategoryName = "";

            CategoryItem2 item;
            SubCategoryItem2 child;
            item = new CategoryItem2();
            items.add(item);
            for (int i = 0; i < mDataset.size(); i++)
            {
                if (i > 0 && lLastCategoryName.compareTo(mDataset.get(i).CategoryName) != 0)
                {
                    item = new CategoryItem2();
                    items.add(item);
                }

                if (lLastCategoryName.compareTo(mDataset.get(i).CategoryName) != 0)
                    item.title = mDataset.get(i).CategoryName;

                child = new SubCategoryItem2();
                child.title = mDataset.get(i).SubCategoryName;
                child.hint = mDataset.get(i).SubCategoryId.toString();

                item.items.add(child);

                lLastCategoryName = mDataset.get(i).CategoryName;
            }

            adapter = new ExampleAdapter(this);
            adapter.setData(items);

            listView = (AnimatedExpandableListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);

            // In order to show animations, we need to use a custom click handler
            // for our ExpandableListView.
            listView.setOnGroupClickListener(new OnGroupClickListener()
            {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                {
                    // We call collapseGroupWithAnimation(int) and
                    // expandGroupWithAnimation(int) to animate group
                    // expansion/collapse.
                    if (listView.isGroupExpanded(groupPosition))
                    {
                        listView.collapseGroupWithAnimation(groupPosition);
                    } else
                    {
                        listView.expandGroupWithAnimation(groupPosition);
                    }
                    return true;
                }
            });


            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
            {
                @Override
                public boolean onChildClick(
                        ExpandableListView parent, View v,
                        int groupPosition, int childPosition,
                        long id)
                {
                    Intent intent = new Intent();

                    String lSubCategoryName = ((TextView) v.findViewById(R.id.textTitle)).getText().toString();
                    String lSubCategoryId = ((TextView) v.findViewById(R.id.textHint)).getText().toString();

                    intent.putExtra("SubCategoryName", lSubCategoryName);
                    intent.putExtra("SubCategoryId", lSubCategoryId);
                    setResult(RESULT_OK, intent);
                    finish();
                    return false;
                }
            });
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.categoryselection_menu, menu);
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_maintaincategories)
            {
                Intent intent = new Intent(this, activityCategory.class);
                startActivity(intent);
                return true;
            }
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
        return super.onOptionsItemSelected(item);
    }

    public void SwitchChange(View view)
    {
        try
        {
            setupForm();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        try
        {
            setupForm();
        } catch (Exception e)
        {
            MyLog.WriteExceptionMessage(e);
        }
    }


    private static class CategoryItem2
    {
        String title;
        List<SubCategoryItem2> items = new ArrayList<SubCategoryItem2>();
    }

    private static class SubCategoryItem2
    {
        String title;
        String hint;
    }

    private static class SubCategoryItem2Holder
    {
        TextView title;
        TextView hint;
    }

    private static class CategoryItem2Holder
    {
        TextView title;
    }

    /**
     * Adapter for our list of {@link CategoryItem2}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListAdapter
    {
        private LayoutInflater inflater;

        private List<CategoryItem2> items;

        public ExampleAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<CategoryItem2> items)
        {
            this.items = items;
        }

        @Override
        public SubCategoryItem2 getChild(int groupPosition, int childPosition)
        {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            try
            {
                SubCategoryItem2Holder holder;
                SubCategoryItem2 item = getChild(groupPosition, childPosition);
                if (convertView == null)
                {
                    holder = new SubCategoryItem2Holder();
                    convertView = inflater.inflate(R.layout.cell_subcategory2, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                    holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                    convertView.setTag(holder);
                } else
                {
                    holder = (SubCategoryItem2Holder) convertView.getTag();
                }

                holder.title.setText(item.title);
                holder.hint.setText(item.hint);
            } catch (Exception e)
            {
                MyLog.WriteExceptionMessage(e);
            }

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition)
        {
            return items.get(groupPosition).items.size();
        }

        @Override
        public CategoryItem2 getGroup(int groupPosition)
        {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            try
            {
                CategoryItem2Holder holder;
                CategoryItem2 item = getGroup(groupPosition);
                if (convertView == null)
                {
                    holder = new CategoryItem2Holder();
                    convertView = inflater.inflate(R.layout.cell_category2, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                    convertView.setTag(holder);
                } else
                {
                    holder = (CategoryItem2Holder) convertView.getTag();
                }

                holder.title.setText(item.title);
            } catch (Exception e)
            {
                MyLog.WriteExceptionMessage(e);
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1)
        {
            return true;
        }

    }

}

