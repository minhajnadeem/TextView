package com.example.minhaj.textview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        makeTextViewResizeAble(textView,2,"see more",true);
    }

    private void makeTextViewResizeAble(final TextView textView, final int maxLines, final String seeMore, boolean isSeeMore) {

        if (textView.getTag() == null){
            textView.setTag(textView.getText());
        }

        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver treeObserver = textView.getViewTreeObserver();
                treeObserver.removeOnGlobalLayoutListener(this);
                if (maxLines == 0){

                }else if (maxLines > 0 && textView.getLineCount()>= maxLines){
                    Log.d("tag","line count = "+textView.getLineCount());
                    //int lastLineIndex = textView.getLayout().getEllipsisStart(maxLines-1);
                    int lastLineIndex = textView.getLayout().getLineEnd(maxLines-1);
                    Log.d("tag","line last index = "+lastLineIndex);
                    String subStr = textView.getText().subSequence(0,lastLineIndex - seeMore.length()+1) +" "+seeMore;
                    Log.d("tag","sub str = "+subStr);
                    textView.setText(subStr);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setText(getSpanAbleText(Html.fromHtml(textView.getText().toString()),textView,lastLineIndex,seeMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private SpannableStringBuilder getSpanAbleText(final Spanned text, final TextView textView, int lastLineIndex, String seeMore) {
        String str = text.toString();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        if (str.contains(seeMore)){
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    textView.setLayoutParams(textView.getLayoutParams());
                    textView.setText(textView.getTag().toString(), TextView.BufferType.SPANNABLE);
                    textView.setMaxLines(Integer.MAX_VALUE);
                    textView.invalidate();
                }
            };
            spannableStringBuilder.setSpan(clickableSpan,str.indexOf(seeMore),str.indexOf(seeMore)+seeMore.length(),0);
        }
        return spannableStringBuilder;
    }

}
