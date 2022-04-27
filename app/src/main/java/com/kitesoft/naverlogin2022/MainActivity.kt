package com.kitesoft.naverlogin2022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kitesoft.naverlogin2022.databinding.ActivityMainBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.api.NidProfileApi
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        //네아로SDK 초기화
        NaverIdLoginSDK.initialize(this, "q09uL9Wo57pgaoN8cjHV", "GH4aWg_HaZ", "SignIn")

        binding.nidBtn.setOAuthLoginCallback(object : OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                Toast.makeText(this@MainActivity, "error : $message", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(this@MainActivity, "failure : $message", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()

                val accessToken:String?= NaverIdLoginSDK.getAccessToken()
                Toast.makeText(this@MainActivity, "$accessToken", Toast.LENGTH_SHORT).show()

                Log.i("token", accessToken+"");
                //new AlertDialog.Builder(MainActivity.this).setMessage(accessToken+"").show();

                val retrofit:Retrofit= Retrofit.Builder().baseUrl("https://openapi.naver.com").addConverterFactory(GsonConverterFactory.create()).build();
                retrofit.create(RetrofitService::class.java).getUserInfo("Bearer "+accessToken).enqueue(object : retrofit2.Callback<UserInfoResponse>{
                    override fun onResponse(
                        call: Call<UserInfoResponse>,
                        response: Response<UserInfoResponse>
                    ) {

                        val userInfo: UserInfoResponse?= response.body()


                        val buffer= StringBuffer();
                        buffer.append(userInfo?.response?.nickname+"\n");
                        buffer.append(userInfo?.response?.profile_image+"\n");
                        buffer.append(userInfo?.response?.email+"\n");
                        AlertDialog.Builder(this@MainActivity).setMessage(buffer.toString()).show();
                    }

                    override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                });


            }

        })

        binding.btnLogout.setOnClickListener {
            NaverIdLoginSDK.logout()
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
        }

        binding.btnDeleteToken.setOnClickListener {

        }

    }
}

