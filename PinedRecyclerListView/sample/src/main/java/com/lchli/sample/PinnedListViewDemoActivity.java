package com.lchli.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchli.pinedrecyclerlistview.library.ListSectionData;
import com.lchli.pinedrecyclerlistview.library.pinnedListView.AbsAdapter;
import com.lchli.pinedrecyclerlistview.library.pinnedListView.PinnedListAdapter;
import com.lchli.pinedrecyclerlistview.library.pinnedListView.PinnedListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lchli
 *         this sample show how to use different section in PinnedListView.
 */
public class PinnedListViewDemoActivity extends AppCompatActivity {

    private PinnedListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_list_view);

        listView = (PinnedListView) findViewById(R.id.listView);

        List all = new ArrayList();
        for (int i = 0; i < 100; i++) {
            all.add(new Person(i + ""));
            if (i == 10 || i == 20 || i == 90) {
                TestSectionData sectionData = new TestSectionData(TestAdapter.VIEW_TYPE_SECTION_1);
                sectionData.extraData = i + "s";
                all.add(sectionData);
            } else if (i == 3 || i == 23 || i == 63) {
                TestSectionData sectionData = new TestSectionData(TestAdapter.VIEW_TYPE_SECTION_2);
                sectionData.extraData = i + "s";
                all.add(sectionData);
            }
        }

        TestAdapter testAdapter = new TestAdapter();
        listView.setPinnedAdapter(testAdapter);
        testAdapter.refresh(all);

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


    class TestAdapter extends PinnedListAdapter {

        static final int VIEW_TYPE_SECTION_1 = 0;
        static final int VIEW_TYPE_SECTION_2 = 1;

        static final int VIEW_TYPE_ITEM = 2;

        @Override
        public int getViewTypeCount() {
            return 3;
        }

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
        public AbsAdapter.AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_ITEM:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item, parent, false);
                    return new ListItemViewHolder(view, viewType);

                case VIEW_TYPE_SECTION_1:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item_section, parent, false);
                    return new SectionViewHolder(view, viewType);

                case VIEW_TYPE_SECTION_2:
                    view = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.list_item_section2, parent, false);
                    return new Section2ViewHolder(view, viewType);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(AbsAdapter.AbsViewHolder holder, int position) {
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

        class ListItemViewHolder extends AbsAdapter.AbsViewHolder {

            private final TextView textView;
            private final View itemView;

            public ListItemViewHolder(View itemView, int viewType) {
                super(viewType);
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.textView);
            }

            @Override
            protected View getItemView() {
                return itemView;
            }
        }

        class SectionViewHolder extends AbsAdapter.AbsViewHolder {

            private final TextView textView;
            private final View itemView;

            public SectionViewHolder(View itemView, int viewType) {
                super(viewType);
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.textView);
            }

            @Override
            protected View getItemView() {
                return itemView;
            }
        }

        class Section2ViewHolder extends AbsAdapter.AbsViewHolder {

            private final TextView textView;
            private final View itemView;

            public Section2ViewHolder(View itemView, int viewType) {
                super(viewType);
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.textView);
            }

            @Override
            protected View getItemView() {
                return itemView;
            }
        }
    }
}
