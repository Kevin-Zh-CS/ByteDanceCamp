package com.bytedance.androidcamp.network.dou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.androidcamp.network.dou.api.IMiniDouyinService;
import com.bytedance.androidcamp.network.dou.model.GetVideosResponse;
import com.bytedance.androidcamp.network.dou.model.Video;
import com.bytedance.androidcamp.network.lib.util.ImageHelper;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("ValidFragment")
public class PlaceholderFragment extends Fragment {
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private List<Video> mVideos = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    public Button mBtn;
    //private Button mBtnRefresh; 使用下拉刷新之后这个button没用了
    private int positon;//接收第几个view pager
    private SwipeRefreshLayout swipeRefreshLayout;//下拉可以刷新的控件

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    @SuppressLint("ValidFragment")
    public PlaceholderFragment(int positon) {
        this.positon = positon;
        setHasOptionsMenu(true);
    }


    private void initBtns() {
        //mBtnRefresh = getView().findViewById(R.id.btn_refresh);
        swipeRefreshLayout = Objects.requireNonNull(getView()).findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setDistanceToTriggerSync(300);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.red,R.color.black);

//        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fetchFeed(view);
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mBtnRefresh.setText("requesting...");
//                mBtnRefresh.setEnabled(false);
                miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
                    @Override
                    public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                        List<Video> Videos = new ArrayList<>();
                        Videos.clear();
                        mVideos.clear();
                        if (response.body() != null && response.body().videos != null) {
                            Videos = response.body().videos;
                            if(positon == 0) {
                                mVideos.addAll(Videos);
                            }else{
                                for (Video video : Videos) {
                                    if(video.studentId.equals("18366345568"))
                                        mVideos.add(video);
                                }
                            }
                            Objects.requireNonNull(mRv.getAdapter()).notifyDataSetChanged();
                        }
//                        mBtnRefresh.setText(R.string.refresh_feed);
//                        mBtnRefresh.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
//                        mBtnRefresh.setText(R.string.refresh_feed);
//                        mBtnRefresh.setEnabled(true);
                        Toast.makeText(PlaceholderFragment.this.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.tv);
        }

        public void bind(final Activity activity, final Video video) {
            ImageHelper.displayWebImage(video.imageUrl, img);
            textView.setText(video.userName);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.launch(activity, video.videoUrl);
                }
            });
        }
    }

    private void initRecyclerView() {
        mRv = Objects.requireNonNull(getView()).findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRv.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder(
                        LayoutInflater.from(PlaceholderFragment.this.getContext())
                                .inflate(R.layout.video_item_view, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(i);
                viewHolder.bind(PlaceholderFragment.this.getActivity(), video);
//                ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
//                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == -1 && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                mBtn.setText(R.string.select_a_video);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                mBtn.setText(R.string.post_it);
            }
        }
    }
//
//    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
//        File f = new File(ResourceUtils.getRealPath(PlaceholderFragment.this.getContext(), uri));
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
//        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void postVideo() {
//        mBtn.setText("POSTING...");
//        mBtn.setEnabled(false);
//        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
//        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
//        Log.i("TAG",mSelectedImage.toString() + ";" + mSelectedVideo.toString());
//        //@TODO 4下面的id和名字替换成自己的
//        miniDouyinService.postVideo("18366345568", "长者", coverImagePart, videoPart).enqueue(
//                new Callback<PostVideoResponse>() {
//                    @Override
//                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
//                        Log.i("TAG","success");
//                        if (response.body() != null) {
//                            Toast.makeText(PlaceholderFragment.this.getContext(), response.body().toString(), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                        mBtn.setText(R.string.select_an_image);
//                        mBtn.setEnabled(true);
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
//                        Log.i("TAG","fail");
//                        mBtn.setText(R.string.select_an_image);
//                        mBtn.setEnabled(true);
//                        Toast.makeText(PlaceholderFragment.this.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//        Log.i("TAG","finish");
//    }

//    @SuppressLint("SetTextI18n")
//    public void fetchFeed(View view) {
//        mBtnRefresh.setText("requesting...");
//        mBtnRefresh.setEnabled(false);
//        miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
//            @Override
//            public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
//                List<Video> Videos = new ArrayList<>();
//                Videos.clear();
//                mVideos.clear();
//                if (response.body() != null && response.body().videos != null) {
//                    Videos = response.body().videos;
//                    //@TODO  5服务端没有做去重，拿到列表后，可以在端侧根据自己的id，做列表筛选。
//                    if(positon == 0) {
//                        mVideos.addAll(Videos);
//                    }else{
//                        for (Video video : Videos) {
//                            if(video.studentId.equals("18366345568"))
//                            mVideos.add(video);
//                        }
//                    }
//                    Objects.requireNonNull(mRv.getAdapter()).notifyDataSetChanged();
//                }
//                mBtnRefresh.setText(R.string.refresh_feed);
//                mBtnRefresh.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
//                mBtnRefresh.setText(R.string.refresh_feed);
//                mBtnRefresh.setEnabled(true);
//                Toast.makeText(PlaceholderFragment.this.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videoshow, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initBtns();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.searchbar, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
                    @Override
                    public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                        List<Video> Videos = new ArrayList<>();
                        Videos.clear();
                        mVideos.clear();
                        if (response.body() != null && response.body().videos != null) {
                            Videos = response.body().videos;

                            for (Video video : Videos) {
                                if(video.userName.toLowerCase().contains(s)){
                                    mVideos.add(video);
                                }
                            }
                            Objects.requireNonNull(mRv.getAdapter()).notifyDataSetChanged();
                        }
//                        mBtnRefresh.setText(R.string.refresh_feed);
//                        mBtnRefresh.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
//                        mBtnRefresh.setText(R.string.refresh_feed);
//                        mBtnRefresh.setEnabled(true);
                        Toast.makeText(PlaceholderFragment.this.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


}
