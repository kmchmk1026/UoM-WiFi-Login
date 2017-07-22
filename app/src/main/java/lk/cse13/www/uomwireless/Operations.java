package lk.cse13.www.uomwireless;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Operations{
private Context context;
    public Operations(Context context) {
        this.context = context;
    }

    public void writeToFile(String data, String file) {
//        Log.i("get","inside write");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(String file) {

        String ret = "";
//        Log.i("get","inside read");
        try {
            InputStream inputStream = context.openFileInput(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
//            Log.e("Exception", "File not found: " + e.toString());
        } catch (IOException e) {
//            Log.e("Exception", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
