package com.i9930.croptrails.FingerAuth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.hdodenhof.circleimageview.CircleImageView;
import com.i9930.croptrails.Helper;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerAuthActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME = "fprint";
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private KeyGenerator keyGenerator;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    ImageView backImage,loginViaPassword;
    CircleImageView logoImageView;
    Context context;
    TextView hiMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        setContentView(R.layout.activity_finger_auth);
        context=this;
        loginViaPassword=findViewById(R.id.loginViaPassword);
        backImage=findViewById(R.id.backImage);
        hiMessageTv=findViewById(R.id.hiMessageTv);
        logoImageView=findViewById(R.id.logoImageView);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });loginViaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        });
        loadImage();
        if (SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_NAME)!=null&&
        !TextUtils.isEmpty(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_NAME))){
            hiMessageTv.setVisibility(View.VISIBLE);
            hiMessageTv.setText(getResources().getString(R.string.hi_label)+" "+SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_NAME));
        }else {
            hiMessageTv.setVisibility(View.GONE);
        }

    }

    private void loadImage(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.crp_trls_rounded_icon)
                .error(R.drawable.crp_trls_rounded_icon);

        String link= SharedPreferencesMethod.getString(FingerAuthActivity.this, SharedPreferencesMethod.USER_IMAGE);
        /*if (link!= null && link.contains("croptrailsimages.s3")) {
            ShowAwsImage.getInstance(context).downloadFile(Uri.parse(link), logoImageView, link);
        } else {*/
            Glide.with(context).load(link).apply(options).into(logoImageView);
        //}
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {

                //desc.setText("Your device doesn't support fingerprint authentication");

            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                    != PackageManager.PERMISSION_GRANTED) {

                // desc.setText("Please enable the fingerprint permission");
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // desc.setText("No fingerprint found. Please register minimum one fingerprint");
            }
            if (!keyguardManager.isKeyguardSecure()) {
                // desc.setText("Please enable lockscreen password in your device's Settings");
            } else {
                try {
                    generateEncryptionKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (initializeCipher()) {
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    Helper helper = new Helper(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    private void generateEncryptionKey() throws FingerprintAuthException {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintAuthException(exc);
        }
    }

    public boolean initializeCipher() {
        try {

            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintAuthException extends Exception {
        public FingerprintAuthException(Exception e) {
            super(e);
        }
    }
}
