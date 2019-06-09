# **LifeLine : A suicide prevention chatbot android application using DialogFlow.** 

### Getting Started with Your Own App:

- Open Android Studio.


![Android Studio](https://vignette.wikia.nocookie.net/android/images/f/fb/Android_Studio_icon.svg.png/revision/latest?cb=20180401073355)

- Create a new project.


![New Project](https://developer.android.com/training/basics/firstapp/images/studio-welcome_2x.png)

- Import the following packages in gradle module app.
  	```
  	implementation 'ai.api:sdk:2.0.7@aar'
	implementation 'ai.api:libai:1.6.12
	implementation 'com.google.code.gson:gson:2.8.2'
    implementation 'commons-io:commons-io:2.4'
	```
- For ChatScreen xml, define `me_bubble` and `bot_bubble`

- Create an instance of `AIListener` in `ChatScreen.java` class and import all the methods under it.

- Add the following lines in the code to connect to the dialogflow
  	```
	final AIConfiguration config = new AIConfiguration("Client_Access_Token",`
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
	```
- To get the Client access token, login to your account in [Dialogflow](https://dialogflow.com/), create a project and you'll find the client access token in the settings.


![Client_Access_Token](https://dialogflow.com/docs/images/references/api-reference/001-authentication.png)


- Use `AIDataservice`, `AIservice`, and `AIResponse` under submit button(OnClickListener) to send the request to the dialogflow and get the response back using `AsyncTask`.

- [Click here](https://github.com/dialogflow/dialogflow-android-client) for a quick guide and to know more about `DialogFlow Android SDK`.![dialogflow](https://www.wabion.com/wp-content/uploads/2017/11/dialogflow.png)
