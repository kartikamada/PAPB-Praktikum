package com.tifd.projectcomposed
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

    public class ApiConfig {
        companion object{
            fun getApiService():APIService{
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_GITHUB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                return retrofit.create(APIService::class.java)
            }
        }
    }
