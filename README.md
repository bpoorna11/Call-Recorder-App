# Call Recorder

Android friendly!

# Recorder Features

1. Automatically Records incoming/outgoing calls which might be single or conference calls.
2. Stores all recordings in internal storage in a folder “AudioRecorder” and also inside the app using shared preferences.
3. Displays Outgoing Call Log which includes phone number,Call Type(Incoming/Outgoing),Call Date,Calling Sim,Call Duration.
4. Displays the email id with current date as signed in by user while login.
5. Recorde automatically stops when call is hung up.
6. All files in "AudioRecord" folder are uploaded to server.
7. Google sign in features.

# Manual install

    gradle installDebug

# Translate

If you want to translate 'Call Recorder' to your language  please read this:

  * [HOWTO-Translate.md](/docs/HOWTO-Translate.md)

# Screenshots
| Main Page | Settings |
| ------ | ------ |
| ![shot](/docs/sshot.png) |![shot](/docs/setting.png) |
 
                                                        
# Compatibility

The application is fully compatible till android 8.1. Some audio glitches can occur in higher android versions due to restrictions on accessing the audio stream of the other end in higher android versions.
Read more about it here: https://code.google.com/p/android/issues/detail?id=206613#c13

# Permissions

Runtime permissions are implemented in code for following permissions

1. READ_CONTACTS
2. RECORD_AUDIO
3. CALL_PHONE
4. PROCESS_OUTGOING_CALLS
5. READ_EXTERNAL_STORAGE
6. WRITE_EXTERNAL_STORAGE
7. READ_PHONE_STATE

# Settings
1. AudioRecorder gives a raw sound stream which id then compressed to wav file. https://developer.android.com/reference/android/media/AudioRecord
2. Sampling rate : 16khz
3. File format : wav
4. Recording channel : Mono
5. Recording source : VOIP (MediaRecorder.AudioSource.VOICE_COMMUNICATION) https://stackoverflow.com/questions/47359587/what-is-the-best-audiosource-setting-for-calls  
6. Noise Suppressor : https://developer.android.com/reference/android/media/audiofx/NoiseSuppressor
7. Automatic Gain Controller: https://developer.android.com/reference/android/media/audiofx/AutomaticGainControl  
8. Acoustic echo canceler: https://developer.android.com/reference/android/media/audiofx/AcousticEchoCanceler

# PhoneState Receiver

The application receives broadcasts on particularly 3 events when phone is :

1. "Ringing"(CALL_STATE_RINGING)
2. "Picked up"(CALL_STATE_OFFHOOK)
3. "Hung up"(CALL_STATE_IDLE)

# User survey's

Check user surveys at project home page here:

  * [User survey's (7 phone models)](https://docs.google.com/spreadsheets/d/16y2YKZpBOnIzsRhnX8nJ2TliAhLNrv98YpQ7ssAxftY/edit?ts=5d11f7ab#gid=0)
