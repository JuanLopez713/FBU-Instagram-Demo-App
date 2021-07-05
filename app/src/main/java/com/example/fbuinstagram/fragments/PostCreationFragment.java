package com.example.fbuinstagram.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.fbuinstagram.activities.PostingActivity;
import com.example.fbuinstagram.databinding.ActivityMainBinding;
import com.example.fbuinstagram.databinding.FragmentPostCreationBinding;
import com.example.fbuinstagram.models.Post;
import com.example.parstagram.FragmentController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostCreationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "PostCreationFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    FragmentPostCreationBinding binding;
    FragmentController controller;
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    private String photoFileName;

    Uri fileProvider;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostCreationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof com.example.parstagram.FragmentController){
            controller = (com.example.parstagram.FragmentController) context;
        }
        else{
            Log.e(TAG, "ERROR:  context passed in did NOT implement FragmentController!");
        }
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostCreationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostCreationFragment newInstance(String param1, String param2) {
        PostCreationFragment fragment = new PostCreationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        //To launch intent to gallery + handle it later --> gets the image from the photoFile!
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: " + result.getResultCode() + " " + Activity.RESULT_OK);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            if (ivPostImage != null) {
                                ivPostImage.setImageBitmap(takenImage);
                                //  imagePosted = true;
                            } else {
                                //   imagePosted = false;
                                Log.e(TAG, "imageView was null!");
                            }
                        } else {
                            //    imagePosted = false;
                            Log.d(TAG, "picture wasn't taken");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostCreationBinding.inflate(getLayoutInflater(), container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = binding.etDescription;
        btnCaptureImage = binding.btnCaptureImage;
        ivPostImage = binding.ivPostImage;
        btnSubmit = binding.btnSubmit;


        //1a.) Initialize a file to store the taken pictures:
        photoFile = getPhotoFileUri(photoFileName);          // creates a file reference for future access!
        //Encapsulate photoFile in fileprovider (Parcelable) --> able to be passed into an Intent!
        fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                //Log.d(TAG, "onClick: " + permissionCheck);
                //Do we have the permission to read the gallery --> go intent to gallery
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    //Request for the permission --> check permission again
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                   btnCaptureImage.callOnClick();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        //Initialize intent to go to camera for taking a pic --> pass in the file to store the taken pic in --> launch intent
        Intent toCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       toCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
       //        photoFile = getPhotoFileUri(photoFileName);
        cameraResultLauncher.launch(toCamera);
    }


    // Returns the File for a photo stored on disk given the fileName

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        Log.d(TAG, "savePost: " + photoFile);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");
                etDescription.setText("");
                ivPostImage.setImageResource(0);
                controller.toHomeFragment();
            }
        });

    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.d(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });
    }


}