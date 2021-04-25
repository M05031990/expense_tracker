package com.mee.expensetracker.ui.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.mee.expensetracker.R;


public class ProgressDialog {
    public static final int DIALOG_FULLSCREEN = 1;
    public static final int DIALOG_CENTERED = 2;
    public static final int DIALOG_KYC = 3;

    private Context context;
    /*ACProgressFlower dialog;*/
    private Dialog dialog;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showDialog(int dialogType){
        switch (dialogType){
            case DIALOG_CENTERED:
                centeredDialog();
                break;

            default:centeredDialog();
        }
    }


    private void centeredDialog(){
        /*Activity activity = (Activity)context;
        if(activity==null || activity.isFinishing())return;

        dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading")
                .fadeColor(Color.DKGRAY).build();
        dialog.show();*/
        this.dialog = new Dialog(this.context);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialog_progress_centered);
        if (this.dialog.getWindow() != null) {
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.dialog.getWindow().setLayout(-2, -2);
        }
        this.dialog.setCancelable(false);
        this.dialog.show();

    }

    public boolean isShowing(){
        return dialog!=null && dialog.isShowing();
    }

    public void dismiss(){
        if(dialog == null) return;
        try{
            dialog.dismiss();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
