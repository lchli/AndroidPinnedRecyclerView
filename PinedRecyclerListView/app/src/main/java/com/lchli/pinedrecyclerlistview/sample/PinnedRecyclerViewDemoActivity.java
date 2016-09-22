package com.lchli.pinedrecyclerlistview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchli.pinedrecyclerlistview.R;
import com.lchli.pinedrecyclerlistview.library.ListSectionData;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.lchli.pinedrecyclerlistview.sample.PinnedRecyclerViewDemoActivity.TestAdapter.VIEW_TYPE_SECTION_1;
import static com.lchli.pinedrecyclerlistview.sample.PinnedRecyclerViewDemoActivity.TestAdapter.VIEW_TYPE_SECTION_2;

/**
 * @author lchli
 *         this sample show how to use different section in PinnedRecyclerView.
 */
public class PinnedRecyclerViewDemoActivity extends AppCompatActivity {

    private PinnedRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_recycler_view);

        recyclerView = (PinnedRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridDividerDecoration(this, GridDividerDecoration.VERTICAL_LIST));

        List all = new ArrayList();
        for (int i = 0; i < 100; i++) {
            all.add(new Person(i + ""));
            if (i == 10 || i == 20 || i == 90) {
                TestSectionData sectionData = new TestSectionData(VIEW_TYPE_SECTION_1);
                sectionData.extraData = i + "s";
                all.add(sectionData);
            } else if (i == 3 || i == 23 || i == 63) {
                TestSectionData sectionData = new TestSectionData(VIEW_TYPE_SECTION_2);
                sectionData.extraData = i + "s";
                all.add(sectionData);
            }
        }

        TestAdapter testAdapter = new TestAdapter();
        recyclerView.setPinnedAdapter(testAdapter);
        testAdapter.refresh(all);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycler_demo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openListView:
                Intent it = new Intent(this, PinnedListViewDemoActivity.class);
                startActivity(it);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private static class TestSectionData extends ListSectionData {

        public String extraData;

        public TestSectionData(int sectionViewType) {
            super(sectionViewType);
        }
    }

    private static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }
    }


    class TestAdapter extends PinnedRecyclerAdapter {

        static final int VIEW_TYPE_SECTION_1 = 0;
        static final int VIEW_TYPE_SECTION_2 = 1;

        static final int VIEW_TYPE_ITEM = 2;


        @Override
        public int getItemViewType(int position) {
            Object o = getItem(position);
            if (o instanceof ListSectionData) {
                return ((ListSectionData) o).sectionViewType;
            } else {
                return VIEW_TYPE_ITEM;
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_ITEM:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item, parent, false);
                    return new ListItemViewHolder(view);

                case VIEW_TYPE_SECTION_1:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item_section, parent, false);
                    return new SectionViewHolder(view);

                case VIEW_TYPE_SECTION_2:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item_section2, parent, false);
                    return new Section2ViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object data = getItem(position);
            int viewType = getItemViewType(position);

            switch (viewType) {
                case VIEW_TYPE_ITEM:
                    Person person = (Person) data;
                    ListItemViewHolder viewHolder = (ListItemViewHolder) holder;
                    viewHolder.textView.setText("list item :" + person.name);
                    break;

                case VIEW_TYPE_SECTION_1:
                    TestSectionData sectionData = (TestSectionData) data;
                    SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                    sectionViewHolder.textView.setText("section:" + sectionData.extraData);
                    break;

                case VIEW_TYPE_SECTION_2:
                    TestSectionData sectionData2 = (TestSectionData) data;
                    Section2ViewHolder sectionViewHolder2 = (Section2ViewHolder) holder;
                    sectionViewHolder2.textView.setText("section2:" + sectionData2.extraData);
                    break;
            }
        }

        class ListItemViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

            public ListItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        class SectionViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

            public SectionViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        class Section2ViewHolder extends RecyclerView.ViewHolder {

            private final TextView textView;

            public Section2ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}
