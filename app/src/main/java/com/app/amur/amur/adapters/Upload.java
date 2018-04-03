package com.app.amur.amur.adapters;

/**
 * Created by Digriz on 06.01.2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;







    @IgnoreExtraProperties
    public class Upload{


           public String url;

        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        public Upload() {
        }

           public Upload( String url) {

            this.url= url;
        }



        public String getUrl() {
            return url;
        }
    }
