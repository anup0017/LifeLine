# LifeLine : A suicide prevention chatbot android application using DialogFlow. 
## Steps to make this project from scratch:
- Create a new project in Android Studio.
-  Import packages in gradle module app.
  -	implementation 'ai.api:sdk:2.0.7@aar'

    
   	implementation 'ai.api:libai:1.6.12'

	implementation 'com.google.code.gson:gson:2.8.2'
    
	implementation 'commons-io:commons-io:2.4'

- For ChatScreen xml, define me_bubble and bot_bubble

- Create an instance of AIListener in ChatScreen.java class and import all the methods under it.

- Add the following lines in the code to connect to the dialogflow
  -	final AIConfiguration config = new AIConfiguration("Client_Access_Token",
                
	AIConfiguration.SupportedLanguages.English,
                
	AIConfiguration.RecognitionEngine.System);

- Use AIDataservice, AIservice, and AIResponse under submit button(OnClickListener) to send the request to the dialogflow and get the respopnse back using AsyncTask.

- A complete tutorial is given in the link below
  -	https://github.com/dialogflow/dialogflow-android-client