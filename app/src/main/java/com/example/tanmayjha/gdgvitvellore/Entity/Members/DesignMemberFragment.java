package com.example.tanmayjha.gdgvitvellore.Entity.Members;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tanmayjha.gdgvitvellore.Entity.model.MemberModel;
import com.example.tanmayjha.gdgvitvellore.R;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DesignMemberFragment extends Fragment {

    RecyclerView mRecyclerView;
    Firebase mRef;

    public DesignMemberFragment() {
        // Required empty public constructor
    }

    public void onStart()
    {
        super.onStart();
        View view=getView();
        mRef=new Firebase("https://gdg-vit-vellore-af543.firebaseio.com/designmembers");
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_design_member);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerAdapter<MemberModel,DesignMemberFragment.MembersViewHolder> adapter=new FirebaseRecyclerAdapter<MemberModel,DesignMemberFragment.MembersViewHolder>(
                MemberModel.class,
                R.layout.card_member,
                DesignMemberFragment.MembersViewHolder.class,
                mRef.getRef()
        ) {
            @Override
            protected void populateViewHolder(DesignMemberFragment.MembersViewHolder membersViewHolder, MemberModel memberModel, int i) {
                membersViewHolder.name.setText(memberModel.getName());
                membersViewHolder.work.setText(memberModel.getWork());
                membersViewHolder.githubid.setText(memberModel.getGithubid());
                Glide.with(getActivity()).load(memberModel.getProfile_pic()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(DesignMemberFragment.MembersViewHolder.profile_pic);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder{
        TextView name,work,githubid;
        static CircleImageView profile_pic;

        public MembersViewHolder(View v) {
            super(v);
            name=(TextView)v.findViewById(R.id.member_name);
            work=(TextView)v.findViewById(R.id.member_work);
            githubid=(TextView)v.findViewById(R.id.member_github_id);
            profile_pic=(CircleImageView)v.findViewById(R.id.member_image);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_design_member, container, false);
    }

}
