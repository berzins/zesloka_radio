# Ze Sloka control/automation server

Application jumble together necessary functionality to remotely control and automate Ze Sloka radio station. 
Each functionality exists as a separate entity (Command).  If necessary, communication between commands happens through application internal or remote dispatch service. In normal conditions, Commands should never try to communicate directly.

The existing list of commands can be accessed like this:

```
cmd_get_all_cmd_params
```

As a response will be returned JSON array with available commands, descriptions and its signature.

A single command signature can be accessed by the command key:

```
download_youtube_pl
```

This will return command signature what should look like this:

```
{"cmd_key":"download_youtube_pl",
  "params":[{
    "cmd_key":"download_youtube_pl",
    "param_key":"playlist_id",
    "type":"string",
    "value":"UNDEFINED"
  }]
}
```

If all UNDEFINED values are filled input to sever like this will trigger
'download_youtube_pl' to download provided yt playlist in argument value. 

## Command chaining

To trigger more complex predefined command sequences it is possible to chain them together and store for later use:

1. You have to setup record environment calling this command:
```
recorder_set
```
where 
   'session_id' is some long number, should be same for another record commands.
  'cmd_name' : short description of command.
  'cmd_key':  command trigger key.

2. Start record by calling:
```
recorder_start
```
This will start record any incoming commands except 'recording' commands.

3. Stop record by calling
```
recorder_stop
```
This basically stops the recorder, but you can still use 'recorder_start' to continue recording session.

4. Store recorded command sequence by calling 
```
recorder_store
```
Note: recorder should be stopped to take this command any effect.

 --- For the same recording session all these commands should be called with same 'session_id ' value. ---


