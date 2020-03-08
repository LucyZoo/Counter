package com.example.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.database.Cursor;

public class SignIn extends Activity {
    Account currentUserAccountEmail;
    Integer hashValueOfAccount;
    public boolean UserSignIn(){
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.google");
        try {
            int howManyAccounts = accounts.length;
            if (howManyAccounts == 1) {
                currentUserAccountEmail = accounts[0];
            } else if (howManyAccounts > 1) {
                currentUserAccountEmail = accounts[0]; // implement let user choose account to use
            } else {
            }

            hashValueOfAccount = currentUserAccountEmail.hashCode();
            String[] idProjection = new String[]{Integer.toString(hashValueOfAccount)};
            Cursor c = getContentResolver().query(CounterProvider.CONTENT_URI,idProjection,
                    CounterProvider.NAME, idProjection, null);

            if(c != null){

            }
            return true;
        }catch (Exception exception){
            return false;
        }
    }

}
