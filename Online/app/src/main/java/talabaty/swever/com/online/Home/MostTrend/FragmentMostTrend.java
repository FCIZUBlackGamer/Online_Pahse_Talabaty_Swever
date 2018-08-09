package talabaty.swever.com.online.Home.MostTrend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import talabaty.swever.com.online.R;

public class FragmentMostTrend extends Fragment {

    GridView gridView;
    MontagAdapter booksAdapter;
    private Product[] products = new Product[20];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_most_trend,container,false);
        gridView = (GridView)view.findViewById(R.id.gridview);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        for (int x=0; x<20; x++){
            Product r = new Product(0, "IPhone", "",12000, 20,3);
            products[x] = r;
        }
        booksAdapter = new MontagAdapter(getActivity(), products);
        gridView.setAdapter(booksAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product book = products[position];
                //Todo: Make Some Action
                book.getId();
            }
        });
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        final List<Integer> favoritedBookNames = new ArrayList<>();
//        for (Product book : products) {
//            if (book.getIsFavorite()) {
//                favoritedBookNames.add(book.getName());
//            }
//        }
//
//        outState.putIntegerArrayList(favoritedBookNamesKey, (ArrayList)favoritedBookNames);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        final List<Integer> favoritedBookNames =
//                savedInstanceState.getIntegerArrayList(favoritedBookNamesKey);
//
//        // warning: typically you should avoid n^2 loops like this, use a Map instead.
//        // I'm keeping this because it is more straightforward
//        for (int bookName : favoritedBookNames) {
//            for (Book book : books) {
//                if (book.getName() == bookName) {
//                    book.setIsFavorite(true);
//                    break;
//                }
//            }
//        }
//
//    }
}
