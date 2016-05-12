#Droidodoro

Droidodoro is a playground app that mixes the pomodoro technique with Trello.

### SETUP

In order to build the app, you need to add a TrelloApiKey in your gradle.properties:

TrelloApiKey="YOURAPIKEY"


###Workflow
The first thing the user needs is an authentication token. If no token is stored, the user is immediately brought to the configuration screen where he can get a token from.
Once the token is received, the user can associate three lists from the same board to the todo, doing, done list.

The user is then brough on the tasks screen, where the three lists are visible.

By clicking on a todo item the user is brought on the timer screen, where he can start a pomodoro, pause, set the task to done, etc 

Once a task is moved is also synched with trello. The done tasks receive a comment with the total time / pomodoros spent.


## Things worth noting

Two of three screens (namely configuration and timer) use the MVP pattern.

Each asynchronous interaction with rest / the storage is done via RxJava.

The components are injected using dagger 2.

The timer one was a bit complex so I added the state pattern to its presenter which makes the code a bit more complex.

### The login / the interaction with the Trello rest api

Every call to the trello rest endpoint needs to contain the api key and an auth token given to the user. In order to retrieve the auth token, a WebView is opened on the authentication page, and intercepts the redirect call where Trello is supposed to return the token to. Once the token is received, it is stored in shared preferences.

Using an okhttp interceptor makes super easy to sign every http call with those two query argumetns.

### The synchronization with Trello

The cards / lists are fetched the first time the user picks the three lists. 

After that, all the operations done to move the cards from a list to another are performed on the local storage. Every operation marks the record on the storage as dirty and triggers a one shot task on gcm network manager. GCM Network manager wraps JobScheduler on api 21+, so the tasks are synched as soon as the task is triggered. This makes the app super responsive even in those cases where the network is not available.

### The implementation of the timer

Instead of having a live service running for the whole duration of the pomodoro (25 mins) or even worse, having a wakelock in the timer screen, the more efficient approach is to save the duration / start time on the local storage (shared prefs), run the countdown only when the screen is visible, and demand to the alarm manager to wake the app up on due time when the app is in background. This is way more efficient since there is no code runnign if the app is in background even if the timer is running.

If the app is brought on foreground again, it evaluates the time passed and updates the countdown with the correct value.
