package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import handworks_cleaning_service.handworks_mobile.R;

public class ChatFragment extends Fragment {
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        View noChatsYet = view.findViewById(R.id.noChatYet);
        int chatCount = 0;

        noChatsYet.setVisibility((chatCount == 0) ? VISIBLE : GONE);

        return view;
    }
}