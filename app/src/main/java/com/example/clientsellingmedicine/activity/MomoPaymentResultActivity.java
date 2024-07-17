package com.example.clientsellingmedicine.activity;


import android.content.Context;
import android.content.Intent;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.clientsellingmedicine.R;

import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.utils.Constants;
import com.example.clientsellingmedicine.utils.Convert;
import com.example.clientsellingmedicine.utils.SharedPref;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.util.Date;



public class MomoPaymentResultActivity extends AppCompatActivity {
    private Context mContext;


    private ImageView ivBack, imageView;
    private Button btn_ViewOrder, btn_ContinueShopping;
    private TextView tv_Title, tv_Content, tv_Time, tv_PaymentMethod, tv_Amount, tv_OrderID;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.momo_payment);

        addControl();
        addEvents();

    }

    private void addControl() {
        imageView = findViewById(R.id.imageView);
        ivBack = findViewById(R.id.ivBack);
        btn_ViewOrder = findViewById(R.id.btn_ViewOrder);
        btn_ContinueShopping = findViewById(R.id.btn_ContinueShopping);
        tv_Title = findViewById(R.id.tv_Title);
        tv_Content = findViewById(R.id.tv_Content);
        tv_Time = findViewById(R.id.tv_Time);
        tv_PaymentMethod = findViewById(R.id.tv_PaymentMethod);
        tv_Amount = findViewById(R.id.tv_Amount);
        tv_OrderID = findViewById(R.id.tv_OrderID);
    }

    private void addEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        btn_ViewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, OrderDetailActivity.class);
            intent.putExtra("code", code);
            finish();
            startActivity(intent);
        });

        btn_ContinueShopping.setOnClickListener(v -> {
            finish(); //back to Cart activity
        });

        Intent intent = getIntent();
        if (intent != null) {
            // open from momo app => payment with MoMo
            if (intent.getData() != null) {
                Uri deepLinkUri = intent.getData();
                handlerPaymentResultWithMoMo(deepLinkUri);
            } else {
                //open from Payment Activity => payment with COD
                Order order = (Order) intent.getSerializableExtra("Order");
                int statusCode = intent.getIntExtra("statusCode", -1);
                handlerPaymentResultWithCOD(order, statusCode);
            }
        }
    }

    private void handlerPaymentResultWithMoMo(Uri deepLinkUri) {
        String resultCode = deepLinkUri.getQueryParameter("resultCode");
        String message = deepLinkUri.getQueryParameter("message");
        String msg = "";
        try {
            msg = URLDecoder.decode(message, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String amount = deepLinkUri.getQueryParameter("amount");
        code = deepLinkUri.getQueryParameter("orderId");  // in deeplink , orderId = order code (unique)
        String partnerCode = deepLinkUri.getQueryParameter("partnerCode");
        String responseTime = deepLinkUri.getQueryParameter("responseTime");

        //display order info
        handlerDisplayOrderInfo(code, amount, partnerCode, responseTime);

        //payment failed
        if (Integer.parseInt(resultCode) != 0) {
            handlerPaymentFailed(msg);
            return;
        }

        // clear cart items in Shared Preferences
        SharedPref.clearData(mContext, Constants.CART_PREFS_NAME);
    }

    private void handlerPaymentResultWithCOD(Order order, int statusCode) {

        code = order.getCode();
        String amount = order.getTotal().toString();
        String method = order.getPaymentMethod();
        String time = order.getOrderTime().toString();

        //display order info
        handlerDisplayOrderInfo(code, amount, method, time);

        //payment failed
        if (statusCode != 201) {
            handlerPaymentFailed("đã có lỗi xảy ra. Vui lòng thử lại sau ít phút hoặc liên hệ Admin để giải quyết !");
            return;
        }

        // clear cart items in Shared Preferences
        SharedPref.clearData(mContext, Constants.CART_PREFS_NAME);
    }

    private void handlerDisplayOrderInfo(String code, String amount, String method, String responseTime) {
        tv_OrderID.setText(code);
        tv_Amount.setText(Convert.convertPrice(Integer.parseInt(amount)));
        tv_PaymentMethod.setText(method);
        String time = Convert.convertToDate(responseTime);
        tv_Time.setText(time != null ? time : getCurrentTime());
    }

    private void handlerPaymentFailed(String msg) {
        imageView.setImageResource(R.drawable.failed);
        tv_Title.setText("Thanh toán thất bại");
        tv_Content.setText("Thanh toán không thành công vì " + msg);
    }

    private String getCurrentTime(){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(now);
    }

}

