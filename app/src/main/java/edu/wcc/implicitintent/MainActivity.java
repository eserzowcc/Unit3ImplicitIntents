package edu.wcc.implicitintent;

import android.support.v7.app.AppCompatActivity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.wcc.implicitintents.R;


/**
 *
 * (1) In class -- change at least one of the buttons in the onCreate method to use an anonymous
 * inner class instead of the MainActivity class itself implementing the OnClickListener
 *
 * (2) Remove the permission for the phone from the manifest file, re-run -- what happens when
 * clicking on the Telephone Call button?
 *
 * (3) Hit the home button in your emulator --> then go to settings --> apps --> <App Name></App>
 * now give it permission to use the phone and try running the application
 *
 * (4) Where did the app name come from?  Hint -> Look at the file that contains the details of
 * the app such as the : app name, permissions, activities, ...  that file name is <???>
 * Once you find the file don't forget about CTRL-B : the immensely useful keyboard combo
 * (same as Navigate->Declaration)
 *
 * (5) In the putExtra method call at line 97 -- try changing the key to a different string
 * and re-running.  What happens now when you press the text message button?
 * Note - if you pressed it previously, you may have a previous message in the SMS textfield.
 * if you do, try changing the message and try it again
 *
 */
public class MainActivity extends Activity implements OnClickListener {

    static final int CONTACTS_INTENT = 1;
    static final int VOICE_RECOGNITION=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] resources = {R.id.browser, R.id.makeCall, R.id.map, R.id.pick_contact,
                R.id.text_message};

        for (int i=0; i < resources.length; i++)
        {
            View b = (View)findViewById(resources[i]);
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Uri uri;
        switch (v.getId())
        {
            case R.id.browser:
                uri = Uri.parse("http://amazon.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.makeCall:
                uri = Uri.parse("tel:7349733471");
                //intent = new Intent(Intent.ACTION_DIAL, uri);
                //Needs: <uses-permission android:name="android.permission.CALL_PHONE"/>
                intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
                break;

            case R.id.map:
                uri = Uri.parse("geo:42.2608, -83.6600");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.pick_contact:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, CONTACTS_INTENT);
                break;

            case R.id.text_message:
                intent = new Intent(Intent.ACTION_SENDTO,  Uri.parse("sms:"+"7349733470"));

                // Take note of what is going on here in regards to putExtra...
                intent.putExtra("sms_body", "Sent from cps251 android app.");
                startActivity(intent);
                break;
        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            Toast.makeText(this, "onActivityResult NOT OK", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode)
        {
            case CONTACTS_INTENT:
                Uri uri = data.getData();
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case VOICE_RECOGNITION:
                ArrayList<String> results;
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                StringBuilder sb = new StringBuilder();
                for (int i=0; i< results.size(); i++)
                {
                    Log.d("Mine",results.get(i));
                    sb.append(results.get(i));
                }
                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
