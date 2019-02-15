package com.android.incongress.cd.conference.widget.phonecode;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.incongress.cd.conference.widget.popup.InputMethodUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 类：FocusPhoneCode
 * 作者： qxc
 * 日期：2018/3/14.
 */
public class FocusPhoneCode extends RelativeLayout{
    private Context context;
    private EditText et_code1,et_code2,et_code3,et_code4,et_code5,et_code6;
    private View v1,v2,v3,v4,v5,v6;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;
    FinishListener finishListener;

    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    public FocusPhoneCode(Context context) {
        super(context);
        this.context = context;
        loadView();
    }

    public FocusPhoneCode(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    private void loadView(){
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.focus_phone_code, this);
        initView(view);
        initEvent();
    }

    private void initView(View view){
        et_code1 = (EditText) view.findViewById(R.id.et_code1);
        et_code2 = (EditText) view.findViewById(R.id.et_code2);
        et_code3 = (EditText) view.findViewById(R.id.et_code3);
        et_code4 = (EditText) view.findViewById(R.id.et_code4);
        et_code5 = (EditText) view.findViewById(R.id.et_code5);
        et_code6 = (EditText) view.findViewById(R.id.et_code6);
        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);
        v4 = view.findViewById(R.id.v4);
        v5 = view.findViewById(R.id.v5);
        v6 = view.findViewById(R.id.v6);
    }

    private void initEvent(){
        et_code1.addTextChangedListener(new InputTextWatcher());
        et_code2.addTextChangedListener(new InputTextWatcher());
        et_code3.addTextChangedListener(new InputTextWatcher());
        et_code4.addTextChangedListener(new InputTextWatcher());
        et_code5.addTextChangedListener(new InputTextWatcher());
        et_code6.addTextChangedListener(new InputTextWatcher());

        et_code1.setOnKeyListener(new InputTextKeyListener());
        et_code2.setOnKeyListener(new InputTextKeyListener());
        et_code3.setOnKeyListener(new InputTextKeyListener());
        et_code4.setOnKeyListener(new InputTextKeyListener());
        et_code5.setOnKeyListener(new InputTextKeyListener());
        et_code6.setOnKeyListener(new InputTextKeyListener());

        et_code1.setOnFocusChangeListener(new InputTextOnFocus());
        et_code2.setOnFocusChangeListener(new InputTextOnFocus());
        et_code3.setOnFocusChangeListener(new InputTextOnFocus());
        et_code4.setOnFocusChangeListener(new InputTextOnFocus());
        et_code5.setOnFocusChangeListener(new InputTextOnFocus());
        et_code6.setOnFocusChangeListener(new InputTextOnFocus());
    }

    private boolean needListen = true;
    class InputTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(needListen){
                needListen = false;
                if (codes.size() < 6 && editable.length()>0) {
                    codes.add(editable.toString().substring(0,1));
                }
                if(codes.size()>=6){
                    finishListener.isFinish(true);
                }else {
                    finishListener.isFinish(false);
                }
                showCode();
                needListen = true;
            }
        }
    }

    class InputTextKeyListener implements OnKeyListener{
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                codes.remove(codes.size() - 1);
                showCode();
                return true;
            }
            return false;
        }
    }


    class InputTextOnFocus implements OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            if(b){
                setFocus();
            }
        }
    }

    /**
     * 显示输入的验证码
     */
    private void showCode() {
        if (codes.size() >= 1) {
            String code1 = codes.get(0);
            if(!code1.equals(et_code1.getText().toString())){
                et_code1.setText(code1);
            }
        }else{
            et_code1.setText("");
        }
        if (codes.size() >= 2) {
            String code2 = codes.get(1);
            if(!code2.equals(et_code2.getText().toString())) {
                et_code2.setText(code2);
            }
        }else{
            et_code2.setText("");
        }
        if (codes.size() >= 3) {
            String code3 = codes.get(2);
            if(!code3.equals(et_code3.getText().toString())) {
                et_code3.setText(code3);
            }
        }else{
            et_code3.setText("");
        }
        if (codes.size() >= 4) {
            String code4 = codes.get(3);
            if(!code4.equals(et_code4.getText().toString())) {
                et_code4.setText(code4);
            }
        }else{
            et_code4.setText("");
        }
        if (codes.size() >= 5) {
            String code5 = codes.get(4);
            if(!code5.equals(et_code5.getText().toString())) {
                et_code5.setText(code5);
            }
        }else{
            et_code5.setText("");
        }
        if (codes.size() >= 6) {
            String code6 = codes.get(5);
            if(!code6.equals(et_code6.getText().toString())) {
                et_code6.setText(code6);
            }
        }else{
            et_code6.setText("");
        }
        setFocus();
        setColor();
    }

    //设置焦点
    private void setFocus(){
        if (codes.size() == 0) {
            et_code1.setFocusable(true);
            et_code1.setFocusableInTouchMode(true);
            et_code1.requestFocus();
            et_code1.setSelection(et_code1.getText().length());
            showSoftInput(et_code1);
        }
        if (codes.size() == 1) {
            et_code2.setFocusable(true);
            et_code2.setFocusableInTouchMode(true);
            et_code2.requestFocus();
            et_code2.setSelection(et_code2.getText().length());
            showSoftInput(et_code2);
        }
        if (codes.size() == 2) {
            et_code3.setFocusable(true);
            et_code3.setFocusableInTouchMode(true);
            et_code3.requestFocus();
            et_code3.setSelection(et_code3.getText().length());
            showSoftInput(et_code3);
        }
        if (codes.size() == 3) {
            et_code4.setFocusable(true);
            et_code4.setFocusableInTouchMode(true);
            et_code4.requestFocus();
            et_code4.setSelection(et_code4.getText().length());
            showSoftInput(et_code4);
        }
        if (codes.size() == 4) {
            et_code5.setFocusable(true);
            et_code5.setFocusableInTouchMode(true);
            et_code5.requestFocus();
            et_code5.setSelection(et_code5.getText().length());
            showSoftInput(et_code5);
        }
        if (codes.size() >= 5) {
            et_code6.setFocusable(true);
            et_code6.setFocusableInTouchMode(true);
            et_code6.requestFocus();
            et_code6.setSelection(et_code6.getText().length());
            showSoftInput(et_code6);
        }
    }
    /**
     * 设置高亮颜色
     */
    private void setColor(){
        int color_default = context.getResources().getColor(R.color.gray_99);
        int color_focus = context.getResources().getColor(R.color.theme_color);
        v1.setBackgroundColor(color_default);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
        v5.setBackgroundColor(color_default);
        v6.setBackgroundColor(color_default);
        if(codes.size()==0){
            v1.setBackgroundColor(color_focus);
        }
        if(codes.size()==1){
            v2.setBackgroundColor(color_focus);
        }
        if(codes.size()==2){
            v3.setBackgroundColor(color_focus);
        }
        if(codes.size()==3){
            v4.setBackgroundColor(color_focus);
        }
        if(codes.size()==4){
            v5.setBackgroundColor(color_focus);
        }
        if(codes.size()>=5){
            v6.setBackgroundColor(color_focus);
        }
    }

    /**
     * 显示键盘
     * @param editText 输入框控件
     */
    private void showSoftInput(final EditText editText){
        //显示软键盘
        if(imm!=null && editText!=null) {
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(editText, 0);
                }
            },200);
        }
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    public String getPhoneCode(){
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }
    //设置一个监听器
    public interface FinishListener{
        void isFinish(boolean isFinish);
    }
    //清楚数据
    public void clearData(){
        codes.clear();
        InputMethodUtils.hideSoftInput(context,et_code6);
        et_code6.setText("");
        et_code5.setText("");
        et_code4.setText("");
        et_code3.setText("");
        et_code2.setText("");
        et_code1.setText("");

    }
}
