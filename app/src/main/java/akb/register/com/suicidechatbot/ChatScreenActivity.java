package akb.register.com.suicidechatbot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatScreenActivity extends AppCompatActivity implements AIListener {

    boolean doubleBackToExitPressedOnce = false;
    String me, bot;
    EditText userInput;
    ImageView send, mic;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<ResponseMessage> responseMessageList;

    //AI initialising
    AIService aiService;
    AIError aiError;
    String input;

    //Double click back to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        userInput = (EditText) findViewById(R.id.userInput);
        send = (ImageView) findViewById(R.id.send);
        mic = (ImageView) findViewById(R.id.mic);
        recyclerView = findViewById(R.id.conversation);

        //RecyclerView Implementation with Message Adapter with ResponseMessage java Class
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(messageAdapter);

        //Hello by bot at start
        ResponseMessage responseMessage3 = new ResponseMessage("Hey there, I'm your personal therapy bot. How can I help you?", false);
        responseMessageList.add(responseMessage3);

        //Connect with dialogflow
        final AIConfiguration config = new AIConfiguration("YOUR_CLIENT_ACCESS_TOKEN",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        //Service
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        //keyboardsend button click
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    input = userInput.getText().toString();
                    if (input.equals("")) {
                        Toast.makeText(ChatScreenActivity.this, "Please Enter something to get the response!!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        final AIDataService aiDataService = new AIDataService(config);

                        final AIRequest aiRequest = new AIRequest();
                        aiRequest.setQuery(input);

                        new AsyncTask<AIRequest, Void, AIResponse>() {
                            @SuppressLint("WrongThread")
                            @Override
                            protected AIResponse doInBackground(AIRequest... requests) {
                                final AIRequest request = requests[0];
                                try {
                                    return aiDataService.request(request);
                                } catch (AIServiceException e) {
                                    aiError = new AIError(e);
                                    return null;
                                }
                            }
                            @Override
                            protected void onPostExecute(final AIResponse aiResponse) {
                                if (aiResponse != null) {
                                    // process aiResponse here
                                    onResult(aiResponse);
                                }
                            }
                        }.execute(aiRequest);
                    }
                    userInput.setText("");
                }
                return true;
            }
        });


        //send button click
        send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                input = userInput.getText().toString();
                if (input.equals("")) {

                    Toast.makeText(ChatScreenActivity.this, "Please Enter something to get the response!!", Toast.LENGTH_SHORT).show();

                }
                else {
                    final AIDataService aiDataService = new AIDataService(config);

                    final AIRequest aiRequest = new AIRequest();
                    aiRequest.setQuery(input);

                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @SuppressLint("WrongThread")
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                return aiDataService.request(request);
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {
                                // process aiResponse here
                                onResult(aiResponse);
                            }
                            else {
                                onError(aiError);
                            }
                        }
                    }.execute(aiRequest);
                }


                userInput.setText("");

            }
        });


    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }

    public void buttonClick(View view){

        //Prompt for Record Audio Permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }

        aiService.startListening();
    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }

    @Override
    public void onResult(AIResponse result) {
        Log.d("akb", result.toString());
        Result result1 = result.getResult();
        me = result1.getResolvedQuery();
        bot = result1.getFulfillment().getSpeech();
        ResponseMessage responseMessage = new ResponseMessage(me, true);
        responseMessageList.add(responseMessage);
        ResponseMessage responseMessage2 = new ResponseMessage(bot, false);
        responseMessageList.add(responseMessage2);
        messageAdapter.notifyDataSetChanged();
        if (!isLastVisible())
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
            Toast.makeText(this, "Bot is listening, please speak something...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListeningCanceled() {
        Toast.makeText(this, "Listening is being cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListeningFinished() {

        Toast.makeText(this, "Bot has finished Listening!!!", Toast.LENGTH_SHORT).show();

    }
}
