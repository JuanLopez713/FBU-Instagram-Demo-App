package com.example.parstagram;


import com.example.fbuinstagram.models.Post;
import com.parse.ParseUser;

//Purpose:      The communicator from fragments to the MainActivity. Allows fragments to invoke other fragments through the MainActivity!
public interface FragmentController {
    void toHomeFragment();
    void toPostFragment();
    void toProfileFragment(ParseUser user);
    void signOut();
    void toPostDetailsFragment(Post post);
}
