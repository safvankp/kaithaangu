package com.example.kaithaangu;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;


public class AlertDialogue {


    public interface DialogListener {
        public void alertDialogAction(String action);
    }
    public static void showDialogueOmne(Context context ,String msg,DialogListener listener){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView mpin_title = dialog.findViewById(R.id.mpin_title);
        TextView tv = dialog.findViewById(R.id.orderResult_text);
        TextView OK = dialog.findViewById(R.id.proceed_icon);

        tv.setText(msg);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoubleClick(view);
                dialog.dismiss();
                if (listener != null)
                    listener.alertDialogAction("OK");
            }
        });


        dialog.show();
    }

    public static void showDialogueDetailed(Context context ,String msg,String name,String amount,DialogListener listener){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_detailed);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView mpin_title = dialog.findViewById(R.id.mpin_title);
        TextView tv = dialog.findViewById(R.id.orderResult_text);
        TextView OK = dialog.findViewById(R.id.proceed_icon);
        TextView Name = dialog.findViewById(R.id.Name);
        TextView Amount = dialog.findViewById(R.id.Amount);

        tv.setText(msg);
        Name.setText(name);
        Amount.setText(amount);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoubleClick(view);
                dialog.dismiss();
                if (listener != null)
                    listener.alertDialogAction("OK");
            }
        });


        dialog.show();
    }

    private static void DoubleClick(final View view) {
        view.setEnabled(false);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, (long) 2000);
    }

}
